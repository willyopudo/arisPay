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
public class TransactionRejected {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String bankTransRef;

	@Column(nullable = false)
	private Double transAmount;

	@Column(nullable = false)
	private String bankAccount;

	@Column
	private Long companyId;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "client_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
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
