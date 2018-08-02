package com.househelp.repository;

import com.househelp.domain.SaleOrder;
import com.househelp.domain.enums.IsDelete;
import com.househelp.domain.view.SaleOrderView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface SaleOrderRepository extends JpaRepository<SaleOrder, Long> {
    public String baseSql = "select a.id as id,p.name as name,p.model as model,b.corpName as corpName,a.number as number,p.unit as unit,"
            +"a.unitPrice as unitPrice,a.totalPrice as totalPrice,a.createTime as createTime,p.id as productId,b.id as buyerId  "
            +"from SaleOrder a,Product p,Buyer b where a.productId=p.id and a.buyerId =b.id ";

    SaleOrder findById(long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update SaleOrder set isDelete=:isDelete where id=:id")
    int setIsDelete(@Param("isDelete") IsDelete isDelete, @Param("id") Long id);

    @Query(baseSql + "and p.name like ?1 and p.model like ?2 and b.corpName like ?3 and a.isDelete = ?4 ")
    Page<SaleOrderView> findAllByNameAndModelAndCorpNameAndIsDelete(String name, String model, String corpName, IsDelete isDelete, Pageable pageable);

}
