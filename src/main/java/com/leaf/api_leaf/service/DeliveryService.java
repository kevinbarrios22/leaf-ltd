package com.leaf.api_leaf.service;

import com.leaf.api_leaf.dto.DeliveryInvoiceDTO;
import com.leaf.api_leaf.dto.DeliverySheetDTO;
import com.leaf.api_leaf.model.DeliveryInvoice;
import com.leaf.api_leaf.model.DeliverySheet;
import com.leaf.api_leaf.repository.DeliverySheetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliverySheetRepository deliverySheetRepository;

    public DeliverySheet create(DeliverySheetDTO dto) {
        DeliverySheet sheet = new DeliverySheet();
        sheet.setDeliveryDate(dto.getDeliveryDate());
        sheet.setStoreName(dto.getStoreName());
        sheet.setDriverName(dto.getDriverName());
        sheet.setVehiclePlate(dto.getVehiclePlate());

        if (dto.getInvoices() != null) {
            for (DeliveryInvoiceDTO invoiceDTO : dto.getInvoices()) {
                DeliveryInvoice invoice = new DeliveryInvoice();
                invoice.setCustomerName(invoiceDTO.getCustomerName());
                invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
                invoice.setInvoiceCount(invoiceDTO.getInvoiceCount());
                invoice.setDeliverySheet(sheet);
                sheet.getInvoices().add(invoice);
            }
        }

        return deliverySheetRepository.save(sheet);
    }

    public Page<DeliverySheet> getAll(LocalDate date, Pageable pageable) {
        if (date != null) {
            return deliverySheetRepository.findByDeliveryDate(date, pageable);
        }
        return deliverySheetRepository.findAll(pageable);
    }

    public DeliverySheet getById(Long id) {
        return deliverySheetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("List not found"));
    }

    public DeliverySheet update(Long id, DeliverySheetDTO dto) {
        DeliverySheet sheet = getById(id);
        sheet.setDeliveryDate(dto.getDeliveryDate());
        sheet.setStoreName(dto.getStoreName());
        sheet.setDriverName(dto.getDriverName());
        sheet.setVehiclePlate(dto.getVehiclePlate());

        sheet.getInvoices().clear();

        if (dto.getInvoices() != null) {
            for (DeliveryInvoiceDTO invoiceDTO : dto.getInvoices()) {
                DeliveryInvoice invoice = new DeliveryInvoice();
                invoice.setCustomerName(invoiceDTO.getCustomerName());
                invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
                invoice.setInvoiceCount(invoiceDTO.getInvoiceCount());
                invoice.setDeliverySheet(sheet);
                sheet.getInvoices().add(invoice);
            }
        }

        return deliverySheetRepository.save(sheet);
    }

    public void delete(Long id) {
        deliverySheetRepository.deleteById(id);
    }
}