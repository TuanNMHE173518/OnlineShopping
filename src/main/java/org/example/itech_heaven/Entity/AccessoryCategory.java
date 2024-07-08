package org.example.itech_heaven.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accessory_categories")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccessoryCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "accessory_category_name")
    private String name;
    @Column(name = "accessory_category_description")
    private String description;


    @OneToMany(mappedBy = "accessoryCategory", cascade = CascadeType.ALL)
    private List<Accessory> accessories = new ArrayList<>();
}
