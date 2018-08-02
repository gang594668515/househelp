package com.househelp.web;

import com.househelp.comm.aop.LoggerManage;
import com.househelp.domain.Supplier;
import com.househelp.domain.enums.IsDelete;
import com.househelp.domain.result.ExceptionMsg;
import com.househelp.domain.result.Response;
import com.househelp.repository.SupplierRepository;
import com.househelp.service.SupplierService;
import com.househelp.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/supplier")
public class SupplierController extends BaseController {

    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierService supplierService;

    @RequestMapping(value = "/addSupplier", method = RequestMethod.POST)
    @LoggerManage(description = "创建原料供应商")
    public Response create(Supplier supplier) {
        try {
            Supplier formSupplier = supplierRepository.findByCorpName(supplier.getCorpName());
            if (null != formSupplier) {
                return result(ExceptionMsg.CorpNameUsed);
            }
            supplier.setIsDelete(IsDelete.NO);
            supplier.setCreateTime(DateUtils.getCurrentTime());
            supplier.setLastModifyTime(DateUtils.getCurrentTime());
            supplierRepository.save(supplier);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("create supplier failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    @RequestMapping(value = "/deleteSupplier", method = RequestMethod.POST)
    @LoggerManage(description = "逻辑删除原料供应商")
    public Response deleteSupplier(Long supplierId) {
        try {
            supplierRepository.setIsDelete(IsDelete.YES, supplierId);
            logger.info("该原料供应商信息逻辑删除成功!");
            return result(ExceptionMsg.SUCCESS);
        } catch (Exception e) {
            logger.error("原料供应商删除失败", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/recoverSupplier", method = RequestMethod.POST)
    @LoggerManage(description = "逻辑恢复原料供应商")
    public Response recoverSupplier(Long supplierId) {
        try {
            supplierRepository.setIsDelete(IsDelete.NO, supplierId);
            logger.info("该原料供应商信息逻辑恢复成功!");
            return result(ExceptionMsg.SUCCESS);
        } catch (Exception e) {
            logger.error("原料供应商恢复失败", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/updateSupplier", method = RequestMethod.POST)
    @LoggerManage(description = "更新原料供应商")
    public Response update(Supplier supplier) {
        try {
            supplier.setLastModifyTime(DateUtils.getCurrentTime());
            supplierRepository.updateSupplier(supplier.getUserName(), supplier.getPhone(), supplier.getEmail(),
                    supplier.getAddress(), supplier.getPayMethod(), supplier.getAccount(), supplier.getLastModifyTime(), supplier.getId());
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("create supplier failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    @RequestMapping(value = "/selectCorpName",method = RequestMethod.POST)
    @LoggerManage(description = "获取id和corpName列表")
    public List<String> selectCorpName(Supplier supplier){
        List<String> corpNameList = supplierService.findCorpNameByCorpName(supplier.getCorpName(),IsDelete.NO);
        return corpNameList;
    }

    @RequestMapping(value = "/getId",method = RequestMethod.POST)
    @LoggerManage(description = "获取corpName对应的id")
    public Long getId(String corpName){
        Supplier supplier = new Supplier();
        supplier = supplierRepository.findByCorpName(corpName);
        Long supplierId = supplier.getId();
        return supplierId;
    }
}
