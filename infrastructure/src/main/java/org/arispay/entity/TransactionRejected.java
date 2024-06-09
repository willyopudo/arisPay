package org.arispay.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions_rejected")
public class TransactionRejected {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String banTxnRef;

    @Column(nullable = false)
    private String arisPayTxnRef;

    @Column(nullable = false)
    private Float amount;

    @Column(nullable = false)
    private String collectionAccount;

    @Column(nullable = false)
    private Long companyId;

    private Long customerId;

    private String payerName;

    private String payerPhone;

    private String paymentMode;

    private String txnNarration;

    private String ApiChannel;

    @Column(nullable = false)
    private Date txnDate;
}
