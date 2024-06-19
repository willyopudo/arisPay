package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {

	private String bankTranRef;

	private String arisTranRef;

	private Double tranAmount;

	private String bankAccount;

	private Long companyId;

	private String client;

	private String payerName;

	private String payerPhone;

	private String paymentMode;

	private String narration;

	private String apiChannel;

	private LocalDateTime transDate;

	private String crDrInd;
}
