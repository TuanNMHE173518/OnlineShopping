package org.example.itech_heaven.Repository;

import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Integer> {

    List<Sale> findByNameContaining(String name);


}
