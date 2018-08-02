package com.househelp.repository;

import com.househelp.domain.Material;
import com.househelp.domain.enums.IsDelete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.math.BigDecimal;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    public String baseSql = "select a.id as id,a.name as name,a.model as model,a.number as number,a.unit as unit,"
            +"a.unitPrice as unitPrice,a.totalPrice as totalPrice,a.createTime as createTime,s.corpName as corpName "
            +"from Material a,Supplier s where a.supplierId=s.id ";
    Material findById(long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Material set isDelete=:isDelete where id=:id")
    int setIsDelete(@Param("isDelete") IsDelete isDelete, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update Material set supplierId=?1,number=?2,unit=?3,unitPrice=?4,totalPrice=?5,lastModifyTime=?6 where id=?7")
    int updateMaterial(Long supplierId, BigDecimal number, String unit, BigDecimal unitPrice, BigDecimal totalPrice, Long lastModifyTime, Long id);

    @Query(baseSql + "and a.name like ?1 and a.model like ?2 and s.corpName like ?3 and a.isDelete = ?4 ")
    Page<Material> findAllByNameAndModelAndCorpNameAndIsDelete(String name, String model, String corpName, IsDelete isDelete, Pageable pageable);
}
