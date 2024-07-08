package org.example.itech_heaven.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accessory_images")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccessoryImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String image;

    @ManyToOne
    @JoinColumn(name = "accessory_id")
    private Accessory accessory;
}
