package com.househelp.web;

import com.househelp.comm.aop.LoggerManage;
import com.househelp.domain.Buyer;
import com.househelp.domain.enums.IsDelete;
import com.househelp.domain.result.ExceptionMsg;
import com.househelp.domain.result.Response;
import com.househelp.repository.BuyerRepository;
import com.househelp.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/buyer")
public class BuyerController extends BaseController {

    @Autowired
    private BuyerRepository buyerRepository;

    @RequestMapping(value = "/addBuyer", method = RequestMethod.POST)
    @LoggerManage(description = "创建产品购买商")
    public Response create(Buyer buyer) {
        try {
            Buyer formBuyer = buyerRepository.findByCorpName(buyer.getCorpName());
            if (null != formBuyer) {
                return result(ExceptionMsg.CorpNameUsed);
            }
            buyer.setIsDelete(IsDelete.NO);
            buyer.setCreateTime(DateUtils.getCurrentTime());
            buyer.setLastModifyTime(DateUtils.getCurrentTime());
            buyerRepository.save(buyer);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("create buyer failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    @RequestMapping(value = "/deleteBuyer", method = RequestMethod.POST)
    @LoggerManage(description = "逻辑删除产品购买商")
    public Response deleteBuyer(Long buyerId) {
        try {
            buyerRepository.setIsDelete(IsDelete.YES, buyerId);
            logger.info("该产品购买商信息逻辑删除成功!");
            return result(ExceptionMsg.SUCCESS);
        } catch (Exception e) {
            logger.error("产品购买商删除失败", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/recoverBuyer", method = RequestMethod.POST)
    @LoggerManage(description = "逻辑恢复产品购买商")
    public Response recoverBuyer(Long buyerId) {
        try {
            buyerRepository.setIsDelete(IsDelete.NO, buyerId);
            logger.info("该产品购买商信息逻辑恢复成功!");
            return result(ExceptionMsg.SUCCESS);
        } catch (Exception e) {
            logger.error("产品购买商恢复失败", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/updateBuyer", method = RequestMethod.POST)
    @LoggerManage(description = "更新产品购买商")
    public Response update(Buyer buyer) {
        try {
            buyer.setLastModifyTime(DateUtils.getCurrentTime());
            buyerRepository.updateBuyer(buyer.getUserName(), buyer.getPhone(), buyer.getEmail(),buyer.getZipCode(),
                    buyer.getAddress(),buyer.getQualityType(), buyer.getLastModifyTime(), buyer.getId());
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("create buyer failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    @RequestMapping(value = "/selectCorpName",method = RequestMethod.POST)
    @LoggerManage(description = "获取id和企业名称")
    public List<Buyer> selectCorpName(Buyer buyer){
        List<Buyer> list = buyerRepository.findByCorpName("%"+buyer.getCorpName()+"%",IsDelete.NO);
        return list;
    }




}
