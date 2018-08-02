package com.househelp.service.impl;

import com.househelp.cache.CacheService;
import com.househelp.domain.enums.IsDelete;
import com.househelp.domain.view.SaleOrderSummary;
import com.househelp.domain.view.SaleOrderView;
import com.househelp.repository.SaleOrderRepository;
import com.househelp.service.SaleOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("saleOrderService")
public class SaleOrderServiceImpl extends CacheService implements SaleOrderService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @Override
    public List<SaleOrderSummary> findAllByNameAndModelAndCorpNameAndIsDelete(String name, String model, String corpName, IsDelete isDelete, Pageable pageable) {
        // TODO Auto-generated method stub
        Page<SaleOrderView> viewsPage = saleOrderRepository.findAllByNameAndModelAndCorpNameAndIsDelete("%" + name + "%", "%" + model + "%", "%" + corpName + "%", isDelete, pageable);
        List<SaleOrderSummary> summaryList = new ArrayList<SaleOrderSummary>();
        Integer pageTotal = viewsPage.getTotalPages();

        for (SaleOrderView view : viewsPage) {
            SaleOrderSummary saleOrderSummary = new SaleOrderSummary(view);
            saleOrderSummary.setPageTotal(pageTotal);
            summaryList.add(saleOrderSummary);
        }
        return summaryList;
    }
}