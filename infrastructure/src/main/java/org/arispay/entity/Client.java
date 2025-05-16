package org.arispay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.arispay.enums.ClientIdentifierType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.descriptor.jdbc.SmallIntJdbcType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "client")
@NamedStoredProcedureQuery(
        name = "insert_client",
        procedureName = "insert_client",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_client_name", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_client_id_in", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_client_email", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_identifier_type", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_client_phone", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_status", type = SmallIntJdbcType.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_created_by", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_company_id", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "p_client_id", type = String.class)
        }
)
public class Client extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientName;

    @Column(unique = true)
    private String clientId;

    @Enumerated(EnumType.STRING)
    @Column(name = "identifier_type")
    private ClientIdentifierType identifierType;
    @Column(name = "client_email")
    private String clientEmail;

    private String clientPhone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Company company;
}
