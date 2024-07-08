package org.example.itech_heaven.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime date;
    @Column(name = "total_money")
    private long totalMoney;

    private String status;
    @Column(name = "name_receiver")
    private String nameReceiver;
    @Column(name = "phone_receiver")
    private String phoneReceiver;
    @Column(name = "address_receiver")
    private String addressReceiver;
    @Column(name = "email_receiver")
    private String emailReceiver;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public String formatDate(){
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(outputFormatter);
    }

    public String formatPrice(){
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(totalMoney) + " đ";
    }
}
