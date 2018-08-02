package com.househelp.domain.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class SaleOrderSummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String model;
    private String corpName;
    private BigDecimal number;
    private String unit;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private Long createTime;

    private Integer pageTotal;

    public SaleOrderSummary() {

    }

    public SaleOrderSummary(SaleOrderView view) {
        this.id = view.getId();
        this.name = view.getName();
        this.model = view.getModel();
        this.corpName = view.getCorpName();
        this.number = view.getNumber();
        this.unit = view.getUnit();
        this.unitPrice = view.getUnitPrice();
        this.totalPrice = view.getTotalPrice();
        this.createTime = view.getCreateTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(Integer pageTotal) {
        this.pageTotal = pageTotal;
    }
}
