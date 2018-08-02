package com.househelp.service;

import com.househelp.domain.Product;
import com.househelp.domain.enums.IsDelete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<Product> findAllByNameAndModelAndIsDelete(String name, String model, IsDelete isDelete, Pageable pageable);
}