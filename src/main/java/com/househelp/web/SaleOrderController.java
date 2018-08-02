package com.househelp.web;

import com.househelp.comm.aop.LoggerManage;
import com.househelp.domain.SaleOrder;
import com.househelp.domain.enums.IsDelete;
import com.househelp.domain.result.ExceptionMsg;
import com.househelp.domain.result.Response;
import com.househelp.repository.SaleOrderRepository;
import com.househelp.repository.SaleOrderRepository;
import com.househelp.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/saleOrder")
public class SaleOrderController extends BaseController {

    @Autowired
    private SaleOrderRepository saleOrderRepository;

    @RequestMapping(value = "/addSaleOrder", method = RequestMethod.POST)
    @LoggerManage(description = "创建销售单")
    public Response create(SaleOrder saleOrder) {
        try {
            saleOrder.setIsDelete(IsDelete.NO);
            saleOrder.setCreateTime(DateUtils.getCurrentTime());
            saleOrder.setLastModifyTime(DateUtils.getCurrentTime());
            saleOrderRepository.save(saleOrder);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("create saleOrder failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    @RequestMapping(value = "/deleteSaleOrder", method = RequestMethod.POST)
    @LoggerManage(description = "逻辑删除销售单")
    public Response deleteSaleOrder(Long saleOrderId) {
        try {
            saleOrderRepository.setIsDelete(IsDelete.YES, saleOrderId);
            logger.info("该销售单信息逻辑删除成功!");
            return result(ExceptionMsg.SUCCESS);
        } catch (Exception e) {
            logger.error("销售单删除失败", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/recoverSaleOrder", method = RequestMethod.POST)
    @LoggerManage(description = "逻辑恢复销售单")
    public Response recoverSaleOrder(Long saleOrderId) {
        try {
            saleOrderRepository.setIsDelete(IsDelete.NO, saleOrderId);
            logger.info("该销售单信息逻辑恢复成功!");
            return result(ExceptionMsg.SUCCESS);
        } catch (Exception e) {
            logger.error("销售单恢复失败", e);
            return result(ExceptionMsg.FAILED);
        }
    }
}
