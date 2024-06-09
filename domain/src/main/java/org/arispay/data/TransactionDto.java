package org.arispay.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
	private Long id;

	private String banTxnRef;

	private String arisPayTxnRef;

	private Float amount;

	private String collectionAccount;

	private Long companyId;

	private Long customerId;

	private String payerName;

	private String payerPhone;

	private String paymentMode;

	private String txnNarration;

	private String ApiChannel;

	private Date txnDate;
}
