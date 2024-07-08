package org.example.itech_heaven.Service;

import org.example.itech_heaven.Entity.Sale;
import org.example.itech_heaven.Repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Override
    public List<Sale> getSaleList() {
        return saleRepository.findAll();
    }

    @Override
    public void saveSale(Sale sale) {
        saleRepository.save(sale);
    }

    @Override
    public void deleteSale(int saleId) {
        saleRepository.deleteById(saleId);

    }

    @Override
    public List<Sale> findBySaleName(String saleName) {
        return saleRepository.findByNameContaining(saleName);
    }

    @Override
    public Sale getSaleById(int saleId) {
        if(saleRepository.findById(saleId).isPresent()){
            return saleRepository.findById(saleId).get();
        }
        return null;
    }
}
