package org.example.itech_heaven.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "type_device")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TypeDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "type_name")
    private String name;
    @Column(name = "type_device_description")
    private String description;


    @OneToMany(mappedBy = "typeDevice", cascade = CascadeType.ALL)
    private List<Device> devices = new ArrayList<>();

    @ManyToMany(mappedBy = "typeDevices")
    private List<Accessory> accessories = new ArrayList<>();
}
