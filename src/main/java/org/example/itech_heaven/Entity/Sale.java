package org.example.itech_heaven.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "sales")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sale {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;

    @Column(name = "sale_name")
    private String name;

    private int discount;
    private String image;
    private LocalDate startDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<Device> devices = new ArrayList<>();

    public String formatDiscount(){
        return discount + " %";
    }

    public String formatDate(LocalDate originalDate){
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return originalDate.format(outputFormatter);
    }
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<Device> accessories = new ArrayList<>();
}
