package com.househelp.service;

import com.househelp.domain.Material;
import com.househelp.domain.enums.IsDelete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MaterialService {
    Page<Material> findAllByNameAndModelAndCorpNameAndIsDelete(String name, String model, String corpName, IsDelete isDelete, Pageable pageable);
}