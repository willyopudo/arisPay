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
	private String bankTranRef;

	@Column(nullable = false)
	private Double tranAmount;

	@Column(nullable = false)
	private String bankAccount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Company company;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Client client;

	private String payerName;

	private String payerPhone;

	private String paymentMode;

	private String narration;

	private String apiChannel;

	@Column(nullable = false)
	private LocalDateTime tranDate;

	private String crDrInd;
}
