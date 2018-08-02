package com.househelp.service.impl;

import com.househelp.cache.CacheService;
import com.househelp.domain.Buyer;
import com.househelp.domain.enums.IsDelete;
import com.househelp.repository.BuyerRepository;
import com.househelp.service.BuyerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("buyerService")
public class BuyerServiceImpl extends CacheService implements BuyerService {
	protected Logger logger =  LoggerFactory.getLogger(this.getClass());

	@Autowired
	private BuyerRepository buyerRepository;

	@Override
	public Page<Buyer> findAllByCorpNameAndUserNameAndIsDelete(String corpName, String userName, IsDelete isDelete, Pageable pageable){
		// TODO Auto-generated method stub
		Page<Buyer> page = buyerRepository.findAllByCorpNameAndUserNameAndIsDelete("%"+corpName+"%","%"+userName+"%",isDelete,pageable);
		return page;
	}
}