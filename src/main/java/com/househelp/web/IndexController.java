package com.househelp.web;

import com.househelp.comm.Const;
import com.househelp.comm.aop.LoggerManage;
import com.househelp.domain.*;
import com.househelp.domain.enums.IsDelete;
import com.househelp.domain.view.SaleOrderSummary;
import com.househelp.repository.*;
import com.househelp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private BuyerService buyerService;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private MaterialService materialService;
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SaleOrderService saleOrderService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @LoggerManage(description = "首页")
    public String index(Model model) {
        User user = super.getUser();
        if (null != user) {
            model.addAttribute("user", user);
        }
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @LoggerManage(description = "登陆后首页")
    public String home(Model model) {
        model.addAttribute("user", getUser());
        return "home";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @LoggerManage(description = "登陆页面")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @LoggerManage(description = "注册页面")
    public String regist() {
        return "register";
    }

    @RequestMapping(value = "/feedback")
    @LoggerManage(description = "意见反馈页面")
    public String feedback(Model model) {
        User user = null;
        user = userRepository.findById(getUserId());
        model.addAttribute("user", user);
        return "feedback/feedback";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @LoggerManage(description = "登出")
    public String logout(HttpServletResponse response, Model model) {
        getSession().removeAttribute(Const.LOGIN_SESSION_KEY);
        getSession().removeAttribute(Const.LAST_REFERER);
        Cookie cookie = new Cookie(Const.LOGIN_SESSION_KEY, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        return "index";
    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
    @LoggerManage(description = "忘记密码页面")
    public String forgotPassword() {
        return "user/forgotpassword";
    }

    @RequestMapping(value = "/newPassword", method = RequestMethod.GET)
    public String newPassword(String email) {
        return "user/newpassword";
    }

    @RequestMapping(value = "/uploadHeadPortrait")
    @LoggerManage(description = "上传你头像页面")
    public String uploadHeadPortrait() {
        return "user/uploadheadportrait";
    }

    @RequestMapping(value = "/uploadBackground")
    @LoggerManage(description = "上传背景页面")
    public String uploadBackground() {
        return "user/uploadbackground";
    }

    @RequestMapping(value = "/userList")
    @LoggerManage(description = "获取正常用户列表")
    public String userList(Model model) {
        List<User> users = null;
        try {
            users = userRepository.findAllByIsDelete(IsDelete.NO);
        } catch (Exception e) {
            logger.error("getUsers failed, ", e);
        }
        model.addAttribute("users", users);
        return "user/userList";
    }

    @RequestMapping(value = "/userDeleteList")
    @LoggerManage(description = "获取已删用户列表")
    public String userDeleteList(Model model) {
        List<User> users = null;
        try {
            users = userRepository.findAllByIsDelete(IsDelete.YES);
        } catch (Exception e) {
            logger.error("getUsers failed, ", e);
        }
        model.addAttribute("users", users);
        return "user/userDeleteList";
    }

    @RequestMapping(value = "/addSupplier")
    @LoggerManage(description = "新增供应商")
    public String addSupplier() {
        return "supplier/addSupplier";
    }

    @RequestMapping(value = "/getSupplier/{supplierId}")
    @LoggerManage(description = "获取供应商")
    public String getSupplier(Model model, @PathVariable("supplierId") Integer supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId);
        model.addAttribute("supplier", supplier);
        return "supplier/updateSupplier";
    }

    @RequestMapping(value = "/supplierList/{corpName}/{userName}/{pageNum}")
    @LoggerManage(description = "获取正常供应商列表")
    public String supplierList(Model model, @PathVariable("corpName") String corpName, @PathVariable("userName") String userName, @PathVariable("pageNum") Integer pageNum) {
        Supplier supplier = new Supplier();
        if ("s".equals(corpName)) {
            corpName = "";
        } else {
            supplier.setCorpName(corpName);
        }
        if ("s".equals(userName)) {
            userName = "";
        } else {
            supplier.setUserName(userName);
        }
        Integer start = 0;
        Integer limit = 10;
        start = pageNum;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(start, limit, sort);
        Page<Supplier> page = supplierService.findAllByCorpNameAndUserNameAndIsDelete(corpName, userName, IsDelete.NO, pageable);
        model.addAttribute("page", page);
        model.addAttribute("supplier", supplier);
        return "supplier/supplierList";
    }

    @RequestMapping(value = "/supplierDeleteList/{corpName}/{userName}/{pageNum}")
    @LoggerManage(description = "获取已删供应商列表")
    public String supplierDeleteList(Model model, @PathVariable("corpName") String corpName, @PathVariable("userName") String userName, @PathVariable("pageNum") Integer pageNum) {
        Supplier supplier = new Supplier();
        if ("s".equals(corpName)) {
            corpName = "";
        } else {
            supplier.setCorpName(corpName);
        }
        if ("s".equals(userName)) {
            userName = "";
        } else {
            supplier.setUserName(userName);
        }
        Integer start = 0;
        Integer limit = 10;
        start = pageNum;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(start, limit, sort);
        Page<Supplier> page = supplierService.findAllByCorpNameAndUserNameAndIsDelete(corpName, userName, IsDelete.YES, pageable);
        model.addAttribute("page", page);
        model.addAttribute("supplier", supplier);
        return "supplier/supplierDeleteList";
    }

    @RequestMapping(value = "/addBuyer")
    @LoggerManage(description = "新增产品购买方")
    public String addBuyer() {
        return "buyer/addBuyer";
    }

    @RequestMapping(value = "/getBuyer/{buyerId}")
    @LoggerManage(description = "获取产品购买方")
    public String getBuyer(Model model, @PathVariable("buyerId") Integer buyerId) {
        Buyer buyer = buyerRepository.findById(buyerId);
        model.addAttribute("buyer", buyer);
        return "buyer/updateBuyer";
    }

    @RequestMapping(value = "/buyerList/{corpName}/{userName}/{pageNum}")
    @LoggerManage(description = "获取正常产品购买方列表")
    public String buyerList(Model model, @PathVariable("corpName") String corpName, @PathVariable("userName") String userName, @PathVariable("pageNum") Integer pageNum) {
        Buyer buyer = new Buyer();
        if ("s".equals(corpName)) {
            corpName = "";
        } else {
            buyer.setCorpName(corpName);
        }
        if ("s".equals(userName)) {
            userName = "";
        } else {
            buyer.setUserName(userName);
        }
        Integer start = 0;
        Integer limit = 10;
        start = pageNum;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(start, limit, sort);
        Page<Buyer> page = buyerService.findAllByCorpNameAndUserNameAndIsDelete(corpName, userName, IsDelete.NO, pageable);
        model.addAttribute("page", page);
        model.addAttribute("buyer", buyer);
        return "buyer/buyerList";
    }

    @RequestMapping(value = "/buyerDeleteList/{corpName}/{userName}/{pageNum}")
    @LoggerManage(description = "获取已删产品购买方列表")
    public String buyerDeleteList(Model model, @PathVariable("corpName") String corpName, @PathVariable("userName") String userName, @PathVariable("pageNum") Integer pageNum) {
        Buyer buyer = new Buyer();
        if ("s".equals(corpName)) {
            corpName = "";
        } else {
            buyer.setCorpName(corpName);
        }
        if ("s".equals(userName)) {
            userName = "";
        } else {
            buyer.setUserName(userName);
        }
        Integer start = 0;
        Integer limit = 10;
        start = pageNum;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(start, limit, sort);
        Page<Buyer> page = buyerService.findAllByCorpNameAndUserNameAndIsDelete(corpName, userName, IsDelete.YES, pageable);
        model.addAttribute("page", page);
        model.addAttribute("buyer", buyer);
        return "buyer/buyerDeleteList";
    }

    @RequestMapping(value = "/addMaterial")
    @LoggerManage(description = "新增原料采购单")
    public String addMaterial() {
        return "material/addMaterial";
    }

    @RequestMapping(value = "/getMaterial/{materialId}")
    @LoggerManage(description = "获取原料采购单")
    public String getMaterial(Model model, @PathVariable("materialId") Integer materialId) {
        Material material = materialRepository.findById(materialId);

        Optional<Supplier> supplier = supplierRepository.findById(material.getSupplierId());
        model.addAttribute("material", material);
        model.addAttribute("supplier", supplier.get());
        return "material/updateMaterial";
    }

    @RequestMapping(value = "/materialList/{name}/{model}/{corpName}/{pageNum}")
    @LoggerManage(description = "获取正常原料采购单列表")
    public String materialList(Model models, @PathVariable("name") String name, @PathVariable("model") String model, @PathVariable("corpName") String corpName, @PathVariable("pageNum") Integer pageNum) {
        Material material = new Material();
        Supplier supplier = new Supplier();
        if ("s".equals(corpName)) {
            corpName = "";
        } else {
            supplier.setCorpName(corpName);
        }
        if ("s".equals(name)) {
            name = "";
        } else {
            material.setName(name);
        }
        if ("s".equals(model)) {
            model = "";
        } else {
            material.setModel(model);
        }
        Integer start = 0;
        Integer limit = 10;
        start = pageNum;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(start, limit, sort);
        Page<Material> page = materialService.findAllByNameAndModelAndCorpNameAndIsDelete(name, model, corpName, IsDelete.NO, pageable);
        models.addAttribute("page", page);
        models.addAttribute("material", material);
        models.addAttribute("supplier", supplier);
        return "material/materialList";
    }

    @RequestMapping(value = "/materialDeleteList/{name}/{model}/{corpName}/{pageNum}")
    @LoggerManage(description = "获取已删原料采购单列表")
    public String materialDeleteList(Model models, @PathVariable("name") String name, @PathVariable("model") String model, @PathVariable("corpName") String corpName, @PathVariable("pageNum") Integer pageNum) {
        Material material = new Material();
        Supplier supplier = new Supplier();
        if ("s".equals(corpName)) {
            corpName = "";
        } else {
            supplier.setCorpName(corpName);
        }
        if ("s".equals(name)) {
            name = "";
        } else {
            material.setName(name);
        }
        if ("s".equals(model)) {
            model = "";
        } else {
            material.setModel(model);
        }
        Integer start = 0;
        Integer limit = 10;
        start = pageNum;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(start, limit, sort);
        Page<Material> page = materialService.findAllByNameAndModelAndCorpNameAndIsDelete(name, model, corpName, IsDelete.YES, pageable);
        models.addAttribute("page", page);
        models.addAttribute("material", material);
        models.addAttribute("supplier", supplier);
        return "material/materialDeleteList";
    }

    @RequestMapping(value = "/addProduct")
    @LoggerManage(description = "新增产品")
    public String addProduct() {
        return "product/addProduct";
    }

    @RequestMapping(value = "/getProduct/{productId}")
    @LoggerManage(description = "获取产品")
    public String getProduct(Model model, @PathVariable("productId") Integer productId) {
        Product product = productRepository.findById(productId);
        model.addAttribute("product", product);
        return "product/updateProduct";
    }

    @RequestMapping(value = "/productList/{name}/{model}/{pageNum}")
    @LoggerManage(description = "获取正常产品列表")
    public String productList(Model models, @PathVariable("name") String name, @PathVariable("model") String model, @PathVariable("pageNum") Integer pageNum) {
        Product product = new Product();
        if ("s".equals(name)) {
            name = "";
        } else {
            product.setName(name);
        }
        if ("s".equals(model)) {
            model = "";
        } else {
            product.setModel(model);
        }
        Integer start = 0;
        Integer limit = 10;
        start = pageNum;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(start, limit, sort);
        Page<Product> page = productService.findAllByNameAndModelAndIsDelete(name, model, IsDelete.NO, pageable);
        models.addAttribute("page", page);
        models.addAttribute("product", product);
        return "product/productList";
    }

    @RequestMapping(value = "/productDeleteList/{name}/{model}/{pageNum}")
    @LoggerManage(description = "获取已删产品列表")
    public String productDeleteList(Model models, @PathVariable("name") String name, @PathVariable("model") String model, @PathVariable("pageNum") Integer pageNum) {
        Product product = new Product();

        if ("s".equals(name)) {
            name = "";
        } else {
            product.setName(name);
        }
        if ("s".equals(model)) {
            model = "";
        } else {
            product.setModel(model);
        }
        Integer start = 0;
        Integer limit = 10;
        start = pageNum;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(start, limit, sort);
        Page<Product> page = productService.findAllByNameAndModelAndIsDelete(name, model, IsDelete.YES, pageable);
        models.addAttribute("page", page);
        models.addAttribute("product", product);
        return "product/productDeleteList";
    }

    @RequestMapping(value = "/addSaleOrder")
    @LoggerManage(description = "新增产品销售单")
    public String addSaleOrder() {
        return "saleOrder/addSaleOrder";
    }

    @RequestMapping(value = "/saleOrderList/{name}/{model}/{corpName}/{pageNum}")
    @LoggerManage(description = "获取正常产品销售单列表")
    public String saleOrderList(Model models, @PathVariable("name") String name, @PathVariable("model") String model, @PathVariable("corpName") String corpName, @PathVariable("pageNum") Integer pageNum) {
        if ("s".equals(corpName)) {
            corpName = "";
        }
        if ("s".equals(name)) {
            name = "";
        }
        if ("s".equals(model)) {
            model = "";
        }
        Integer start = 0;
        Integer limit = 10;
        start = pageNum;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(start, limit, sort);
        List<SaleOrderSummary> list = saleOrderService.findAllByNameAndModelAndCorpNameAndIsDelete(name, model, corpName, IsDelete.NO, pageable);
        if (list != null && list.size() > 0) {
            models.addAttribute("list", list);
            models.addAttribute("pageTotal", list.get(0).getPageTotal());
        } else {
            models.addAttribute("pageTotal", 1);
        }
        models.addAttribute("name", name);
        models.addAttribute("model", model);
        models.addAttribute("corpName", corpName);
        models.addAttribute("pageNum", pageNum);
        return "saleOrder/saleOrderList";
    }

    @RequestMapping(value = "/saleOrderDeleteList/{name}/{model}/{corpName}/{pageNum}")
    @LoggerManage(description = "获取已删产品销售单列表")
    public String saleOrderDeleteList(Model models, @PathVariable("name") String name, @PathVariable("model") String model, @PathVariable("corpName") String corpName, @PathVariable("pageNum") Integer pageNum) {
        if ("s".equals(corpName)) {
            corpName = "";
        }
        if ("s".equals(name)) {
            name = "";
        }
        if ("s".equals(model)) {
            model = "";
        }
        Integer start = 0;
        Integer limit = 10;
        start = pageNum;
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = PageRequest.of(start, limit, sort);
        List<SaleOrderSummary> list = saleOrderService.findAllByNameAndModelAndCorpNameAndIsDelete(name, model, corpName, IsDelete.YES, pageable);
        if (list != null && list.size() > 0) {
            models.addAttribute("list", list);
            models.addAttribute("pageTotal", list.get(0).getPageTotal());
        } else {
            models.addAttribute("pageTotal", 1);
        }
        models.addAttribute("name", name);
        models.addAttribute("model", model);
        models.addAttribute("corpName", corpName);
        models.addAttribute("pageNum", pageNum);
        return "saleOrder/saleOrderDeleteList";
    }
}