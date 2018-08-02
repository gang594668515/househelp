package com.househelp.service.impl;

import com.househelp.cache.CacheService;
import com.househelp.domain.Product;
import com.househelp.domain.enums.IsDelete;
import com.househelp.repository.ProductRepository;
import com.househelp.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("productService")
public class ProductServiceImpl extends CacheService implements ProductService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<Product> findAllByNameAndModelAndIsDelete(String name, String model, IsDelete isDelete, Pageable pageable) {
        // TODO Auto-generated method stub
        Page<Product> page = productRepository.findAllByNameAndModelAndIsDelete("%" + name + "%", "%" + model + "%", isDelete, pageable);
        return page;
    }

}