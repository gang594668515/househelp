package com.househelp.repository;

import com.househelp.domain.Buyer;
import com.househelp.domain.enums.IsDelete;
import com.househelp.domain.enums.QualityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {

    Buyer findByCorpName(String corpName);

    Buyer findById(long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Buyer set isDelete=:isDelete where id=:id")
    int setIsDelete(@Param("isDelete") IsDelete isDelete, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Buyer set userName=?1,phone=?2,email=?3,zipCode=?4,address=?5,qualityType=?6,lastModifyTime=?7 where id=?8")
    int updateBuyer(String userName, String phone, String email, String zipCode, String address, QualityType qualityType, Long lastModifyTime, Long id);

    @Query("select a.id,a.corpName,a.userName,a.phone,a.email,a.zipCode,a.address,a.qualityType from Buyer a where a.corpName like ?1 and a.userName like ?2 and a.isDelete = ?3 ")
    Page<Buyer> findAllByCorpNameAndUserNameAndIsDelete(String corpName, String userName, IsDelete isDelete, Pageable pageable);

    @Query("select a.id,a.corpName from Buyer a where a.corpName like ?1 ")
    List<Buyer> findByCorpName(String corpName, IsDelete isDelete);
}
