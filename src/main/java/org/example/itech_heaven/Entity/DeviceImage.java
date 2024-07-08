package org.example.itech_heaven.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "device_images")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DeviceImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String image;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;
}
