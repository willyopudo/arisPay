package org.arispay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions_rejected")
public class TransactionRejected extends AuditableEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(nullable = false, unique = true)
	private String bankTranRef;

	@Column(nullable = false)
	private Double tranAmount;

	@Column(nullable = false)
	private String bankAccount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private Company company;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id")
	private Client client;

	private String payerName;

	private String payerPhone;

	private String paymentMode;

	private String narration;

	private String apiChannel;

	@Column(nullable = false)
	private LocalDateTime transDate;

	private String crDrInd;
}
