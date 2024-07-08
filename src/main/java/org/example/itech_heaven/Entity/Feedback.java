package org.example.itech_heaven.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "feedbacks")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String content;
    private int star;
    @Column(name = "feedback_date")
    private LocalDate date;
    @ManyToOne
    @JoinColumn(name= "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="device_id")
    private Device device;

    @ManyToOne
    @JoinColumn(name="accessory_id")
    private Accessory accessory;


    @Column(name = "product_type")
    @Enumerated(value = EnumType.STRING)
    private ProductType productType;

    public String formatDate(){
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(outputFormatter);
    }
}
