package com.leaf.api_leaf.service;

import com.leaf.api_leaf.dto.request.DeliveryFormRequest;
import com.leaf.api_leaf.dto.request.UpdateRowsRequest;
import com.leaf.api_leaf.model.DeliveryForm;
import com.leaf.api_leaf.model.DeliveryFormRow;
import com.leaf.api_leaf.repository.DeliveryFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DeliveryFormService {

    private final DeliveryFormRepository deliveryFormRepository;

    public DeliveryForm create(DeliveryFormRequest request) {
        DeliveryForm form = new DeliveryForm();
        form.setDate(request.getDate());
        form.setPerson(request.getPerson());
        form.setDriver(request.getDriver());
        form.setPlate(request.getPlate());
        form.setTime(request.getTime());
        form.setDeliveryDate(request.getDeliveryDate());

        if (request.getRows() != null) {
            for (DeliveryFormRequest.RowRequest rowReq : request.getRows()) {
                DeliveryFormRow row = new DeliveryFormRow();
                row.setCustomer(rowReq.getCustomer());
                row.setQuantity(rowReq.getQuantity());
                row.setDelivered(false);
                row.setDeliveryForm(form);
                form.getRows().add(row);
            }
        }

        return deliveryFormRepository.save(form);
    }

    public Page<DeliveryForm> getAll(LocalDate date, String customer, String driver, Pageable pageable) {
        return deliveryFormRepository.findByFilters(date, driver, customer, pageable);
    }

    public DeliveryForm getById(Long id) {
        return deliveryFormRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery form not found"));
    }

    public void updateRows(Long id, UpdateRowsRequest request) {
        DeliveryForm form = getById(id);
        form.getRows().clear();

        if (request.getRows() != null) {
            for (UpdateRowsRequest.RowUpdate rowReq : request.getRows()) {
                DeliveryFormRow row = new DeliveryFormRow();
                row.setCustomer(rowReq.getCustomer());
                row.setQuantity(rowReq.getQuantity());
                row.setDelivered(rowReq.isDelivered());
                row.setDeliveryForm(form);
                form.getRows().add(row);
            }
        }

        deliveryFormRepository.save(form);
    }

    public void delete(Long id) {
        deliveryFormRepository.deleteById(id);
    }
}
