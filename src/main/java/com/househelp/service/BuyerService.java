package com.househelp.service;

import com.househelp.domain.Buyer;
import com.househelp.domain.enums.IsDelete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BuyerService {
    public Page<Buyer> findAllByCorpNameAndUserNameAndIsDelete(String corpName, String userName, IsDelete isDelete, Pageable pageable);
}