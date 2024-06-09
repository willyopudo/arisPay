package org.arispay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String banTxnRef;

	@Column(nullable = false)
	private String arisPayTxnRef;

	@Column(nullable = false)
	private Float amount;

	@Column(nullable = false)
	private String collectionAccount;

	@Column(nullable = false)
	private Long companyId;

	private Long customerId;

	private String payerName;

	private String payerPhone;

	private String paymentMode;

	private String txnNarration;

	private String ApiChannel;

	@Column(nullable = false)
	private Date txnDate;
}
