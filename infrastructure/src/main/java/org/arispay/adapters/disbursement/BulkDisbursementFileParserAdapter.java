package org.arispay.adapters.disbursement;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.arispay.data.BulkDisbursementParseResult;
import org.arispay.data.DisbursementItemDto;
import org.arispay.ports.api.BulkDisbursementFileParserPort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class BulkDisbursementFileParserAdapter implements BulkDisbursementFileParserPort {

    private static final Logger logger = LogManager.getLogger(BulkDisbursementFileParserAdapter.class);

    private static final Set<String> VALID_BANK_CODES = Set.of(
            "FBL", "ABSA", "KCB", "EQUITY", "COOP", "STANBIC", "DTB", "NBK", "SCB", "NCBA"
    );

    @Override
    public BulkDisbursementParseResult parseFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            return BulkDisbursementParseResult.builder()
                    .items(List.of())
                    .errors(List.of("File name is missing"))
                    .totalRecords(0)
                    .totalAmount(0)
                    .valid(false)
                    .build();
        }

        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();

        return switch (extension) {
            case "csv" -> parseCsv(file);
            case "xlsx", "xls" -> parseExcel(file);
            default -> BulkDisbursementParseResult.builder()
                    .items(List.of())
                    .errors(List.of("Unsupported file type: " + extension + ". Please upload a CSV or Excel file."))
                    .totalRecords(0)
                    .totalAmount(0)
                    .valid(false)
                    .build();
        };
    }

    private BulkDisbursementParseResult parseCsv(MultipartFile file) {
        List<DisbursementItemDto> items = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> rows = reader.readAll();

            if (rows.isEmpty()) {
                errors.add("File is empty");
                return buildResult(items, errors);
            }

            // Skip header row
            String[] headers = rows.get(0);
            int[] columnMap = mapColumns(headers, errors);

            if (!errors.isEmpty()) {
                return buildResult(items, errors);
            }

            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);
                parseRow(row, i + 1, columnMap, items, errors);
            }

        } catch (IOException | CsvException e) {
            logger.error("Error parsing CSV file: {}", e.getMessage(), e);
            errors.add("Error reading CSV file: " + e.getMessage());
        }

        return buildResult(items, errors);
    }

    private BulkDisbursementParseResult parseExcel(MultipartFile file) {
        List<DisbursementItemDto> items = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            if (sheet.getPhysicalNumberOfRows() == 0) {
                errors.add("File is empty");
                return buildResult(items, errors);
            }

            // Read header row
            Row headerRow = sheet.getRow(0);
            String[] headers = new String[headerRow.getLastCellNum()];
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                headers[i] = cell != null ? getCellStringValue(cell).trim() : "";
            }

            int[] columnMap = mapColumns(headers, errors);
            if (!errors.isEmpty()) {
                return buildResult(items, errors);
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String[] rowData = new String[headers.length];
                for (int j = 0; j < headers.length; j++) {
                    Cell cell = row.getCell(j);
                    rowData[j] = cell != null ? getCellStringValue(cell).trim() : "";
                }
                parseRow(rowData, i + 1, columnMap, items, errors);
            }

        } catch (IOException e) {
            logger.error("Error parsing Excel file: {}", e.getMessage(), e);
            errors.add("Error reading Excel file: " + e.getMessage());
        }

        return buildResult(items, errors);
    }

    private String getCellStringValue(Cell cell) {
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                double val = cell.getNumericCellValue();
                if (val == Math.floor(val) && !Double.isInfinite(val)) {
                    yield String.valueOf((long) val);
                }
                yield String.valueOf(val);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getStringCellValue();
            default -> "";
        };
    }

    /**
     * Maps column headers to indices. Returns array:
     * [0]=beneficiaryName, [1]=beneficiaryAccount, [2]=beneficiaryBank, [3]=amount,
     * [4]=purpose, [5]=remarks, [6]=paymentType, [7]=currency
     */
    private int[] mapColumns(String[] headers, List<String> errors) {
        int[] map = new int[]{-1, -1, -1, -1, -1, -1, -1, -1};

        for (int i = 0; i < headers.length; i++) {
            String h = headers[i].trim().toLowerCase().replaceAll("[^a-z]", "");
            switch (h) {
                case "beneficiaryname", "name", "recipientname" -> map[0] = i;
                case "beneficiaryaccount", "account", "accountnumber", "recipientaccount" -> map[1] = i;
                case "beneficiarybank", "bank", "bankcode" -> map[2] = i;
                case "amount", "paymentamount" -> map[3] = i;
                case "purpose" -> map[4] = i;
                case "remarks", "description", "memo" -> map[5] = i;
                case "paymenttype", "type" -> map[6] = i;
                case "currency", "curr" -> map[7] = i;
            }
        }

        // Validate required columns
        if (map[0] == -1) errors.add("Missing required column: beneficiaryName");
        if (map[1] == -1) errors.add("Missing required column: beneficiaryAccount");
        if (map[2] == -1) errors.add("Missing required column: beneficiaryBank");
        if (map[3] == -1) errors.add("Missing required column: amount");

        return map;
    }

    private void parseRow(String[] row, int rowNum, int[] columnMap,
                          List<DisbursementItemDto> items, List<String> errors) {
        // Skip completely empty rows
        boolean allEmpty = true;
        for (String cell : row) {
            if (cell != null && !cell.trim().isEmpty()) {
                allEmpty = false;
                break;
            }
        }
        if (allEmpty) return;

        String beneficiaryName = getCell(row, columnMap[0]);
        String beneficiaryAccount = getCell(row, columnMap[1]);
        String beneficiaryBank = getCell(row, columnMap[2]);
        String amountStr = getCell(row, columnMap[3]);
        String purpose = getCell(row, columnMap[4]);
        String remarks = getCell(row, columnMap[5]);
        String paymentType = getCell(row, columnMap[6]);
        String currency = getCell(row, columnMap[7]);

        // Validate required fields
        List<String> rowErrors = new ArrayList<>();
        if (beneficiaryName == null || beneficiaryName.isEmpty()) {
            rowErrors.add("beneficiaryName is required");
        }
        if (beneficiaryAccount == null || beneficiaryAccount.isEmpty()) {
            rowErrors.add("beneficiaryAccount is required");
        }
        if (beneficiaryBank == null || beneficiaryBank.isEmpty()) {
            rowErrors.add("beneficiaryBank is required");
        }

        double amount = 0;
        if (amountStr == null || amountStr.isEmpty()) {
            rowErrors.add("amount is required");
        } else {
            try {
                amount = Double.parseDouble(amountStr.replaceAll(",", ""));
                if (amount <= 0) {
                    rowErrors.add("amount must be greater than 0");
                }
            } catch (NumberFormatException e) {
                rowErrors.add("invalid amount: " + amountStr);
            }
        }

        if (!rowErrors.isEmpty()) {
            errors.add("Row " + rowNum + ": " + String.join(", ", rowErrors));
            return;
        }

        items.add(DisbursementItemDto.builder()
                .beneficiaryName(beneficiaryName)
                .beneficiaryAccount(beneficiaryAccount)
                .beneficiaryBank(beneficiaryBank.toUpperCase())
                .amount(amount)
                .purpose(purpose)
                .remarks(remarks)
                .paymentType(paymentType != null && !paymentType.isEmpty() ? paymentType : "EFT")
                .currency(currency != null && !currency.isEmpty() ? currency : "KES")
                .build());
    }

    private String getCell(String[] row, int index) {
        if (index < 0 || index >= row.length) return null;
        String val = row[index];
        return (val != null && !val.trim().isEmpty()) ? val.trim() : null;
    }

    private BulkDisbursementParseResult buildResult(List<DisbursementItemDto> items, List<String> errors) {
        double totalAmount = items.stream().mapToDouble(DisbursementItemDto::getAmount).sum();
        return BulkDisbursementParseResult.builder()
                .items(items)
                .errors(errors)
                .totalRecords(items.size())
                .totalAmount(totalAmount)
                .valid(errors.isEmpty() && !items.isEmpty())
                .build();
    }
}
