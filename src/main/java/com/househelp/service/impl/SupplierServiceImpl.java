package com.househelp.service.impl;

import com.househelp.cache.CacheService;
import com.househelp.domain.Supplier;
import com.househelp.domain.enums.IsDelete;
import com.househelp.repository.SupplierRepository;
import com.househelp.service.SupplierService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("supplierService")
public class SupplierServiceImpl extends CacheService implements SupplierService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public Page<Supplier> findAllByCorpNameAndUserNameAndIsDelete(String corpName, String userName, IsDelete isDelete, Pageable pageable) {
        // TODO Auto-generated method stub
        Page<Supplier> page = supplierRepository.findAllByCorpNameAndUserNameAndIsDelete("%" + corpName + "%", "%" + userName + "%", isDelete, pageable);
        return page;
    }

//    @Override
//    public List<Supplier> findAllByCorpNameAndIsDelete(String corpName,IsDelete isDelete) {
//        List<Supplier> list = supplierRepository.findAllByCorpNameAndIsDelete("%" + corpName + "%",isDelete);
//        return list;
//    }
    @Override
    public List<String> findCorpNameByCorpName(String corpName,IsDelete isDelete) {
        List<String> corpNames = supplierRepository.findCorpNameByCorpName("%" + corpName + "%",isDelete);
        return corpNames;
    }
}