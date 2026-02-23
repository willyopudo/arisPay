package org.arispay.ports.api;

import org.arispay.data.BulkDisbursementParseResult;
import org.springframework.web.multipart.MultipartFile;

public interface BulkDisbursementFileParserPort {
    BulkDisbursementParseResult parseFile(MultipartFile file);
}
