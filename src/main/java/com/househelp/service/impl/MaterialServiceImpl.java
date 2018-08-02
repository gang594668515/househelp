package com.househelp.service.impl;

import com.househelp.cache.CacheService;
import com.househelp.domain.Buyer;
import com.househelp.domain.Material;
import com.househelp.domain.enums.IsDelete;
import com.househelp.repository.BuyerRepository;
import com.househelp.repository.MaterialRepository;
import com.househelp.service.BuyerService;
import com.househelp.service.MaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("materialService")
public class MaterialServiceImpl extends CacheService implements MaterialService {
	protected Logger logger =  LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MaterialRepository materialRepository;

	@Override
	public Page<Material> findAllByNameAndModelAndCorpNameAndIsDelete(String name, String model, String corpName, IsDelete isDelete, Pageable pageable){
		// TODO Auto-generated method stub
		Page<Material> page = materialRepository.findAllByNameAndModelAndCorpNameAndIsDelete("%"+name+"%","%"+model+"%","%"+corpName+"%",isDelete,pageable);
		return page;
	}
}