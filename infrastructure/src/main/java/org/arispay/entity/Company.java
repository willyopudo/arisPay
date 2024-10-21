package org.arispay.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.arispay.enums.CompanyIdentifierType;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company extends AuditableEntity{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String code;

	private String shortCode;

	private String businessRegNum;

	@Email
	private String email;

	private String phoneNumber;

	private String category;

	@Enumerated(EnumType.STRING)
	@Column(name = "identifier_type")
	private CompanyIdentifierType identifierType;

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
	private List<UserCompany> userCompanies;

}
