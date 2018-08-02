package com.househelp.repository;

import com.househelp.domain.Product;
import com.househelp.domain.enums.IsDelete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product findById(long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Product set isDelete=:isDelete where id=:id")
    int setIsDelete(@Param("isDelete") IsDelete isDelete, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Product set number=?1,unit=?2,unitPrice=?3,lastModifyTime=?4 where id=?5")
    int updateProduct(BigDecimal number, String unit, BigDecimal unitPrice, Long lastModifyTime, Long id);

    @Query("select a.id,a.name,a.model,a.number,a.unit,a.unitPrice,a.createTime,a.lastModifyTime from Product a where a.name like ?1 and a.model like ?2 and a.isDelete = ?3 ")
    Page<Product> findAllByNameAndModelAndIsDelete(String name, String model, IsDelete isDelete, Pageable pageable);

    @Query("select a.id,a.name,a.model,a.number,a.unit,a.unitPrice from Product a where a.name like ?1 and a.isDelete = ?2 ")
    List<Product> findByName(String name, IsDelete isDelete);
}
