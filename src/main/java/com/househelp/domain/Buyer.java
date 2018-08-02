package com.househelp.domain;

import com.househelp.domain.enums.IsDelete;
import com.househelp.domain.enums.QualityType;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 产品购买商
 *
 * @author Wangg
 */
@Entity
public class Buyer extends Entitys implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String corpName;
    @Column(nullable = true)
    private String userName;
    @Column(nullable = true)
    private String phone;
    @Column(nullable = true)
    private String email;
    @Column(nullable = true)
    private String zipCode;
    @Column(nullable = true)
    private String address;
    @Enumerated(EnumType.STRING)
    private QualityType qualityType;
    @Column(nullable = false)
    private Long createTime;
    @Column(nullable = false)
    private Long lastModifyTime;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IsDelete isDelete;

    public Buyer() {
        super();
    }

    public Buyer(String corpName, String userName, String phone, String email) {
        super();
        this.email = email;
        this.corpName = corpName;
        this.userName = userName;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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

    public QualityType getQualityType() {
        return qualityType;
    }

    public void setQualityType(QualityType qualityType) {
        this.qualityType = qualityType;
    }
}