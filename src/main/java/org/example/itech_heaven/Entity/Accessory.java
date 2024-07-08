package org.example.itech_heaven.Entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "accessories")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Accessory {

    @Id
    private int id;

    private String name;
    private double price;
    private int quantity;
    private String colorName;
    private String color;
    private String description;
    private String material;
    private String feature;
    private String mainImage;

    @ManyToOne
    @JoinColumn(name="sale_id" )
    private Sale sale;

    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(
            name = "type_device_accessory",
            joinColumns = @JoinColumn(name = "accessory_id"),
            inverseJoinColumns = @JoinColumn(name = "type_device_id")

    )
    private List<TypeDevice> typeDevices = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "accessory_category_id")
    private AccessoryCategory accessoryCategory;

    @OneToMany(mappedBy = "accessory", cascade = CascadeType.ALL)
    private List<AccessoryImage> accessoryImages = new ArrayList<>();

    @OneToMany(mappedBy = "accessory", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @OneToMany(mappedBy ="accessory", cascade = CascadeType.ALL )
    private List<Feedback> feedbacks= new ArrayList<>();


    public String formatPrice(double pri){
        DecimalFormat decimalFormat = new DecimalFormat("#,###.###");
        return decimalFormat.format((int) pri) + " đ";
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
}
