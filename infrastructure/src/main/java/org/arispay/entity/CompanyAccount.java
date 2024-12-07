package org.arispay.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company_accounts")
public class CompanyAccount extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "company_id", nullable = false)
	//@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Company company;

	@Column(nullable = false, unique = true)
	private String accountNumber;

	@Column(nullable = false)
	private String accountName;

	@Column(nullable = false)
	private String bankCode;

	@Column(nullable = false)
	private String bankName;

	@Column(nullable = false)
	private Float balance;

}
