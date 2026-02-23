package org.arispay.adapters.disbursement;

import org.arispay.ports.api.BankDisbursementServicePort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class BankDisbursementResolver {

    private final Map<String, BankDisbursementServicePort> bankServices;

    public BankDisbursementResolver(List<BankDisbursementServicePort> services) {
        this.bankServices = services.stream()
                .collect(Collectors.toMap(
                        BankDisbursementServicePort::getBankCode,
                        Function.identity()
                ));
    }

    public BankDisbursementServicePort resolve(String bankCode) {
        BankDisbursementServicePort service = bankServices.get(bankCode);
        if (service == null) {
            throw new UnsupportedOperationException("No disbursement service for bank: " + bankCode);
        }
        return service;
    }

    public List<String> getSupportedBankCodes() {
        return List.copyOf(bankServices.keySet());
    }
}
