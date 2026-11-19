
package com.foodordering.backend.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "coupon_redemptions",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"coupon_id", "customer_id"}
                ),
                @UniqueConstraint(
                        columnNames = {"order_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CouponRedemption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private CustomerOrder customerOrder;

    @Column(nullable = false, updatable = false)
    private LocalDateTime redeemedAt;

    @PrePersist
    protected void onCreate() {

        if (redeemedAt == null) {
            redeemedAt = LocalDateTime.now();
        }
    }
}
