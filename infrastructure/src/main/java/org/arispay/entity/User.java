package org.arispay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.arispay.data.UserSummary;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SqlResultSetMapping(
		name = "UserSummaryMapping",
		classes = @ConstructorResult(
				targetClass = UserSummary.class,
				columns = {
						@ColumnResult(name = "totalUsers", type = Long.class),
						@ColumnResult(name = "activeUsers", type = Long.class),
						@ColumnResult(name = "pendingUsers", type = Long.class),
						@ColumnResult(name = "inactiveUsers", type = Long.class)
				}
		)
)
@Table(name = "users")
public class User extends AuditableEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = true)
	private String password;

	@Column(nullable = false, unique = true)
	private String phoneNumber;

	@Column(nullable = false)
	private String address;

	private String zipCode;

	@Column(nullable = false)
	private String town;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserCompany> userCompanies;

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(
			name = "users_roles",
			joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
			inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
	private List<Role> roles = new ArrayList<>();

	private String token;

	private LocalDateTime tokenExpiration;

	private String currentPlan;

//	public void addCompany(Company company) {
//		UserCompany userCompany = new UserCompany(this, company,false);
//		companies.add(userCompany);
//		company.getUsers().add(userCompany);
//	}
//
//	public void removeCompany(Company company) {
//		for (Iterator<UserCompany> iterator = companies.iterator();
//			 iterator.hasNext(); ) {
//			UserCompany userCompany = iterator.next();
//
//			if (userCompany.getUser().equals(this) &&
//					userCompany.getCompany().equals(company)) {
//				iterator.remove();
//				userCompany.getCompany().getUsers().remove(userCompany);
//				userCompany.setUser(null);
//				userCompany.setCompany(null);
//			}
//		}
//	}

}