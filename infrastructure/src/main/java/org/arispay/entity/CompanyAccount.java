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
@Table(name = "company_accounts")
public class CompanyAccount extends AuditableEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private Long companyId;

	@Column(nullable = false)
	private String accountNumber;

	@Column(nullable = false)
	private String accountName;

	@Column(nullable = false)
	private String bankCode;

	@Column(nullable = false)
	private String bankName;

	//private String pesaLinkPhone;

//	@Column(nullable = false)
//	private byte isPesaLinkRegistered;

}

