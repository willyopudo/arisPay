package org.arispay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users_companies")
//@IdClass(UserCompanyId.class)
public class UserCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @Column(name = "is_default")
    private boolean isDefault;

    public UserCompany(Company company, boolean isDefault) {
        this.company = company;
        this.isDefault = isDefault;
    }
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (!(o instanceof UserCompany)) return false;
//        UserCompany that = (UserCompany) o;
//        return Objects.equals(user.getFirstName(), that.user.getFirstName()) &&
//                Objects.equals(company.getName(), that.company.getName()) &&
//                Objects.equals(isDefault, that.isDefault);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(user.getFirstName(), company.getName(), isDefault);
//    }

}
