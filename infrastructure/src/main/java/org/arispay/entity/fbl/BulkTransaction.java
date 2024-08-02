package org.arispay.entity.fbl;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fbl_bulk_transaction")
public class BulkTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String batchRef;

    private String cbsRef;

    private String accountDr;

    private String narration;

    private Date valueDate;

    private String currency;

    private double totalAmount;

    private String status;

    private double statusDescription;

    @OneToMany(mappedBy = "transaction")
    private List<Detail> dtl;

    private Timestamp processTime;

    private String processFlg;

    private int noOfTries;
}
