package org.arispay.entity.fbl;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;
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

    private LocalDateTime valueDate;

    private String currency;

    private double totalAmount;

    private String status;

    private String statusDescription;

    @OneToMany(mappedBy = "transaction")
    private List<Detail> dtl;

    private LocalDateTime processTime;

    private String processFlg;

    private int noOfTries = 0;

    private LocalDateTime postingTime;

    private String postingFlg;

    private int postingTryCount = 0;
}
