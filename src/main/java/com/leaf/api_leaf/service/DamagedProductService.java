package com.leaf.api_leaf.service;

import com.leaf.api_leaf.dto.DamagedProductDTO;
import com.leaf.api_leaf.model.AppUser;
import com.leaf.api_leaf.model.DamagedProduct;
import com.leaf.api_leaf.repository.DamagedProductRepository;
import com.leaf.api_leaf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DamagedProductService {

    private final DamagedProductRepository damagedProductRepository;
    private final UserRepository userRepository;

    public DamagedProduct create(DamagedProductDTO dto, String username) {
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        DamagedProduct product = new DamagedProduct();
        product.setReportDate(dto.getReportDate());
        product.setItemNumber(dto.getItemNumber());
        product.setQuantity(dto.getQuantity());
        product.setDescription(dto.getDescription());
        product.setReviewed(dto.isReviewed());
        product.setReportedBy(user);

        return damagedProductRepository.save(product);
    }

    public Page<DamagedProduct> getAll(LocalDate date, String itemNumber, Pageable pageable) {
        if (date != null) {
            return damagedProductRepository.findByReportDate(date, pageable);
        }
        if (itemNumber != null) {
            return damagedProductRepository.findByItemNumber(itemNumber, pageable);
        }
        return damagedProductRepository.findAll(pageable);
    }

    public DamagedProduct getById(Long id) {
        return damagedProductRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Record not found"));
    }

    public DamagedProduct update(Long id, DamagedProductDTO dto) {
        DamagedProduct product = getById(id);
        product.setReportDate(dto.getReportDate());
        product.setItemNumber(dto.getItemNumber());
        product.setQuantity(dto.getQuantity());
        product.setDescription(dto.getDescription());
        product.setReviewed(dto.isReviewed());
        return damagedProductRepository.save(product);
    }

    public void delete(Long id) {
        damagedProductRepository.deleteById(id);
    }
}
