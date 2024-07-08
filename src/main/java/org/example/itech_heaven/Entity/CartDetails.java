package org.example.itech_heaven.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cart_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "accessory_id")
    private Accessory accessory;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @Column(nullable = false)
    private String productType;
}
