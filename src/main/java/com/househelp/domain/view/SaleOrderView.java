package com.househelp.domain.view;

import java.math.BigDecimal;

public interface SaleOrderView {
	Long getId();
	String getName();
	String getModel();
	String getCorpName();
	BigDecimal getNumber();
	String getUnit();
	BigDecimal getUnitPrice();
	BigDecimal getTotalPrice();
	Long getCreateTime();
}