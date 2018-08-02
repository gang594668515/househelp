package com.househelp.web;

import com.househelp.comm.aop.LoggerManage;
import com.househelp.domain.Product;
import com.househelp.domain.enums.IsDelete;
import com.househelp.domain.result.ExceptionMsg;
import com.househelp.domain.result.Response;
import com.househelp.repository.ProductRepository;
import com.househelp.service.ProductService;
import com.househelp.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController extends BaseController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    @LoggerManage(description = "创建产品")
    public Response create(Product product) {
        try {
            product.setIsDelete(IsDelete.NO);
            product.setCreateTime(DateUtils.getCurrentTime());
            product.setLastModifyTime(DateUtils.getCurrentTime());
            productRepository.save(product);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("create product failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    @RequestMapping(value = "/deleteProduct", method = RequestMethod.POST)
    @LoggerManage(description = "逻辑删除产品")
    public Response deleteProduct(Long productId) {
        try {
            productRepository.setIsDelete(IsDelete.YES, productId);
            logger.info("该产品信息逻辑删除成功!");
            return result(ExceptionMsg.SUCCESS);
        } catch (Exception e) {
            logger.error("产品删除失败", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/recoverProduct", method = RequestMethod.POST)
    @LoggerManage(description = "逻辑恢复产品")
    public Response recoverProduct(Long productId) {
        try {
            productRepository.setIsDelete(IsDelete.NO, productId);
            logger.info("该产品信息逻辑恢复成功!");
            return result(ExceptionMsg.SUCCESS);
        } catch (Exception e) {
            logger.error("产品恢复失败", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    @LoggerManage(description = "更新产品")
    public Response update(Product product) {
        try {
            product.setLastModifyTime(DateUtils.getCurrentTime());
            productRepository.updateProduct(product.getNumber(), product.getUnit(), product.getUnitPrice(),
                    product.getLastModifyTime(), product.getId());
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("create product failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    @RequestMapping(value = "/selectProductName", method = RequestMethod.POST)
    @LoggerManage(description = "获取产品名称、id、规格模型、数量、单价")
    public List<Product> selectProductName(Product product) {
        String name = "";
        if (product != null && !"".equals(product.getName())) {
            name = product.getName();
        }

        List<Product> list = productRepository.findByName("%" + name + "%", IsDelete.NO);
        return list;
    }
}
