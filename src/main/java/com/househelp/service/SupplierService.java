package com.househelp.service;

import com.househelp.domain.Supplier;
import com.househelp.domain.enums.IsDelete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupplierService {
    public Page<Supplier> findAllByCorpNameAndUserNameAndIsDelete(String corpName, String userName, IsDelete isDelete, Pageable pageable);

    public List<String> findCorpNameByCorpName(String corpName, IsDelete isDelete);
}