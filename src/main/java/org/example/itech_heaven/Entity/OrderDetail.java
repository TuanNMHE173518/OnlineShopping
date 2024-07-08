package org.example.itech_heaven.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@Entity
@Table(name = "orderDetails")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int quantity;
    @Column(name = "sell_price")
    private double sellPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @ManyToOne
    @JoinColumn(name = "accessory_id")
    private Accessory accessory;

    @Column(name = "product_type")
    @Enumerated(value = EnumType.STRING)
    private ProductType productType;

    public String formatPrice(){
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format((long) sellPrice) + " đ";
    }
}
