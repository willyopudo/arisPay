package org.arispay.entity.fbl;

import java.sql.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "fbl_bulk_detail")
public class Detail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private Date tranDate;

    private String batchRef;

    private String paymentRef;

    private String externalRef;

    private String cbsRef;

    private String paymentType;

    private String senderAccount;

    private String senderBank;

    private String senderBankBranch;

    private String senderDetails;

    private String beneficiaryAccount;

    private String beneficiaryBank;

    private String beneficiaryBankBranch;

    private String beneficiaryDetails;

    private String remarks;

    private String purpose;

    private String currency;

    private double paymentAmount;

    private String xRate;

    private String status;

    private double statusDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulk_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private BulkTransaction transaction;
}
