package org.example.itech_heaven.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "devices")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "device_name")
    private String name;

    private double price;

    private String description;
    private String screen;
    private String camera;
    @Column(name = "front_camera")
    private String frontCamera;
    private int quantity;
    private String colorName;
    private String color;
    private String RAM;
    private String CPU;
    private String GPU;
    private String battery;
    private String SIM;
    private String ROM;
    private String OS;
    private String origin;
    private LocalDate dateRelease;
    private String HDD;
    private String weight;
    private String size;
    private String material;
    private String mainImage;

    @OneToMany(mappedBy ="device", cascade = CascadeType.ALL )
    private List<Feedback> feedbacks= new ArrayList<>();

    @ManyToOne
    @JoinColumn(name="sale_id" )
    private Sale sale;

    @ManyToOne
    @JoinColumn(name="type_device_id" )
    private TypeDevice typeDevice;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<DeviceImage> deviceImages = new ArrayList<>();


    public String formatPrice(double pri){
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format((long) pri) + " đ";
    }

    public double getSalePrice(){
        return price * (1-((double) sale.getDiscount() /100));
    }

    public List<Feedback> reverseList(){
        Collections.reverse(feedbacks);
        return feedbacks;
    }

    public int countByStar(int star){
        int count = 0;
        for(Feedback f: feedbacks){
            if(f.getStar() == star){
                count++;
            }
        }
        return count;
    }

    public String averageRating(){
        double averageRating = (double) (countByStar(5)*5
                +countByStar(4)*4
                +countByStar(3)*3
                +countByStar(2)*2
                +countByStar(1)) / feedbacks.size();
        return String.format("%.1f", averageRating);
    }

    public String formatDate(){
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return dateRelease.format(outputFormatter);
    }
}
