package org.arispay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String code;

	private String shortCode;

	@Column(nullable = false)
	private Float balance;

	@Column(nullable = false)
	private byte recordStatus;

	@Column(nullable = false)
	private byte isEnabled;

	@Column(nullable = false)
	private String createdBy;

	@Column(nullable = false)
	private Date createdDate = new java.util.Date();

	private String modifiedBy;

	private Date modifiedDate = new java.util.Date();

	@OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
	private List<UserCompany> userCompanies;

}
