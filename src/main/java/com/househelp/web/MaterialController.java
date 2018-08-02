package com.househelp.web;

import com.househelp.comm.aop.LoggerManage;
import com.househelp.domain.Material;
import com.househelp.domain.enums.IsDelete;
import com.househelp.domain.result.ExceptionMsg;
import com.househelp.domain.result.Response;
import com.househelp.repository.MaterialRepository;
import com.househelp.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/material")
public class MaterialController extends BaseController {

    @Autowired
    private MaterialRepository materialRepository;

    @RequestMapping(value = "/addMaterial", method = RequestMethod.POST)
    @LoggerManage(description = "创建原料采购单")
    public Response create(Material material) {
        try {
            material.setIsDelete(IsDelete.NO);
            material.setCreateTime(DateUtils.getCurrentTime());
            material.setLastModifyTime(DateUtils.getCurrentTime());
            materialRepository.save(material);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("create material failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    @RequestMapping(value = "/deleteMaterial", method = RequestMethod.POST)
    @LoggerManage(description = "逻辑删除原料采购单")
    public Response deleteMaterial(Long materialId) {
        try {
            materialRepository.setIsDelete(IsDelete.YES, materialId);
            logger.info("该原料采购单信息逻辑删除成功!");
            return result(ExceptionMsg.SUCCESS);
        } catch (Exception e) {
            logger.error("原料采购单删除失败", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/recoverMaterial", method = RequestMethod.POST)
    @LoggerManage(description = "逻辑恢复原料采购单")
    public Response recoverMaterial(Long materialId) {
        try {
            materialRepository.setIsDelete(IsDelete.NO, materialId);
            logger.info("该原料采购单信息逻辑恢复成功!");
            return result(ExceptionMsg.SUCCESS);
        } catch (Exception e) {
            logger.error("原料采购单恢复失败", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/updateMaterial", method = RequestMethod.POST)
    @LoggerManage(description = "更新原料采购单")
    public Response update(Material material) {
        try {
            material.setLastModifyTime(DateUtils.getCurrentTime());
            materialRepository.updateMaterial(material.getSupplierId(), material.getNumber(), material.getUnit(), material.getUnitPrice(),
                    material.getTotalPrice(), material.getLastModifyTime(), material.getId());
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("create material failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }
}
