package org.arispay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true)
	private String phoneNumber;

	@Column(nullable = false)
	private byte status = 1;

	@Column(nullable = false, unique = true)
	private String idNumber;

	@Column(nullable = false)
	private String address;

	private String zipCode;

	@Column(nullable = false)
	private String town;

	@Column(nullable = false)
	private byte recordStatus;

	@Column(nullable = false)
	private String createdBy;

	@Column(nullable = false)
	private Date createdDate = new java.util.Date();

	private String modifiedBy;

	private Date modifiedDate = new java.util.Date();

	@ManyToOne
	@JoinColumn(name = "company_id", nullable = false)
	private Company company;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "users_roles",
			joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
	private List<Role> roles = new ArrayList<>();

}