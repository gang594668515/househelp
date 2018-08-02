package com.househelp.domain;

import com.househelp.domain.enums.IsDelete;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 产品管理
 *
 * @author Wangg
 */
@Entity
public class Product extends Entitys implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private BigDecimal number;
    @Column(nullable = true)
    private String unit;
    @Column(nullable = false)
    private BigDecimal unitPrice;
    @Column(nullable = false)
    private Long createTime;
    @Column(nullable = false)
    private Long lastModifyTime;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IsDelete isDelete;

    public Product() {
        super();
    }

    public Product(Long id) {
        super();
        this.id = id;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public IsDelete getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(IsDelete isDelete) {
        this.isDelete = isDelete;
    }

}