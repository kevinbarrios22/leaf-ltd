package com.leaf.api_leaf.service;

import com.leaf.api_leaf.dto.DamagedProductDTO;
import com.leaf.api_leaf.model.AppUser;
import com.leaf.api_leaf.model.DamagedProduct;
import com.leaf.api_leaf.repository.DamagedProductRepository;
import com.leaf.api_leaf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DamagedProductService {

    private final DamagedProductRepository damagedProductRepository;
    private final UserRepository userRepository;

    public DamagedProduct create(DamagedProductDTO dto, String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        DamagedProduct product = new DamagedProduct();
        product.setReportDate(dto.getReportDate());
        product.setItemNumber(dto.getItemNumber());
        product.setQuantity(dto.getQuantity());
        product.setDescription(dto.getDescription());
        product.setReportedBy(user);

        return damagedProductRepository.save(product);
    }

    public List<DamagedProduct> getAll(LocalDate date, String itemNumber) {
        if (date != null) {
            return damagedProductRepository.findByReportDate(date);
        }
        if (itemNumber != null) {
            return damagedProductRepository.findByItemNumber(itemNumber);
        }
        return damagedProductRepository.findAll();
    }

    public DamagedProduct getById(Long id) {
        return damagedProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro no encontrado"));
    }

    public void delete(Long id) {
        damagedProductRepository.deleteById(id);
    }
}