package com.househelp.service;

import com.househelp.domain.enums.IsDelete;
import com.househelp.domain.view.SaleOrderSummary;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SaleOrderService {
    public List<SaleOrderSummary> findAllByNameAndModelAndCorpNameAndIsDelete(String name, String model, String corpName, IsDelete isDelete, Pageable pageable);
}