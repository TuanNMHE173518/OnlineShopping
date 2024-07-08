package org.example.itech_heaven.Service;

import org.example.itech_heaven.Entity.Sale;

import java.util.List;

public interface SaleService {
    List<Sale> getSaleList();
    void saveSale(Sale sale);
    void deleteSale(int saleId);
    List<Sale> findBySaleName(String saleName);
    Sale getSaleById(int saleId);
}
