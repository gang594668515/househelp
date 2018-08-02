package com.househelp.repository;

import com.househelp.domain.Supplier;
import com.househelp.domain.enums.IsDelete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {

    Supplier findByCorpName(String corpName);

    Supplier findById(long id);

    Supplier findById(Supplier supplier);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Supplier set isDelete=:isDelete where id=:id")
    int setIsDelete(@Param("isDelete") IsDelete isDelete, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Supplier set userName=?1,phone=?2,email=?3,address=?4,payMethod=?5,account=?6,lastModifyTime=?7 where id=?8")
    int updateSupplier(String userName,String phone,String email,String address,String payMethod,String account,Long lastModifyTime,Long id);

    @Query("select a.id,a.corpName,a.userName,a.phone,a.email,a.address,a.payMethod,a.account from Supplier a where a.corpName like ?1 and a.userName like ?2 and a.isDelete = ?3 ")
    Page<Supplier> findAllByCorpNameAndUserNameAndIsDelete(String corpName, String userName, IsDelete isDelete, Pageable pageable);

//    @Query(value = "select  id,corpName from Supplier where corpName like ?1 and isDelete= ?2")
//    List<Supplier> findAllByCorpNameAndIsDelete(String corpName,IsDelete isDelete);
    @Query(value = "select corpName from Supplier where corpName like ?1 and isDelete= ?2")
    List<String> findCorpNameByCorpName(String corpName,IsDelete isDelete);
}
