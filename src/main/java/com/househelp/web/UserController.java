package com.househelp.web;

import com.househelp.comm.Const;
import com.househelp.comm.aop.LoggerManage;
import com.househelp.domain.User;
import com.househelp.domain.enums.IsDelete;
import com.househelp.domain.result.ExceptionMsg;
import com.househelp.domain.result.Response;
import com.househelp.domain.result.ResponseData;
import com.househelp.repository.UserRepository;
import com.househelp.utils.DateUtils;
import com.househelp.utils.FileUtil;
import com.househelp.utils.MD5Util;
import com.househelp.utils.MessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserRepository userRepository;
    @Resource
    private JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String mailFrom;
    @Value("${mail.subject.forgotpassword}")
    private String mailSubject;
    @Value("${mail.content.forgotpassword}")
    private String mailContent;
    @Value("${forgotpassword.url}")
    private String forgotpasswordUrl;
    @Value("${static.url}")
    private String staticUrl;
    @Value("${file.profilepictures.url}")
    private String fileProfilepicturesUrl;
    @Value("${file.backgroundpictures.url}")
    private String fileBackgroundpicturesUrl;
    @Autowired
    private ResourceLoader resourceLoader;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @LoggerManage(description = "登陆")
    public ResponseData login(User user, HttpServletResponse response) {
        try {
            //这里不是bug，前端userName有可能是邮箱也有可能是昵称。
            User loginUser = userRepository.findByEmailOrPhone(user.getUserName(), user.getUserName());
            if (loginUser == null || "YES".equals(loginUser.getIsDelete())) {
                return new ResponseData(ExceptionMsg.LoginNameNotExists);
            } else if (!loginUser.getPassWord().equals(getPwd(user.getPassWord()))) {
                return new ResponseData(ExceptionMsg.LoginNameOrPassWordError);
            }
            Cookie cookie = new Cookie(Const.LOGIN_SESSION_KEY, cookieSign(loginUser.getId().toString()));
            cookie.setMaxAge(Const.COOKIE_TIMEOUT);
            cookie.setPath("/");
            response.addCookie(cookie);
            getSession().setAttribute(Const.LOGIN_SESSION_KEY, loginUser);
            String preUrl = "/";
            if (null != getSession().getAttribute(Const.LAST_REFERER)) {
                preUrl = String.valueOf(getSession().getAttribute(Const.LAST_REFERER));
            }
            return new ResponseData(ExceptionMsg.SUCCESS, preUrl);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("login failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    @LoggerManage(description = "注册")
    public Response create(User user) {
        try {
            User registUser = userRepository.findByEmail(user.getEmail());
            if (null != registUser) {
                return result(ExceptionMsg.EmailUsed);
            }
            User phoneUser = userRepository.findByPhone(user.getPhone());
            if (null != phoneUser) {
                return result(ExceptionMsg.PhoneUsed);
            }
            User userNameUser = userRepository.findByUserName(user.getUserName());
            if (null != userNameUser) {
                return result(ExceptionMsg.UserNameUsed);
            }
            user.setPassWord(getPwd(user.getPassWord()));
            user.setCreateTime(DateUtils.getCurrentTime());
            user.setLastModifyTime(DateUtils.getCurrentTime());
            user.setProfilePicture("img/favicon.png");
            userRepository.save(user);
            // 添加默认属性设置
            getSession().setAttribute(Const.LOGIN_SESSION_KEY, user);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("create user failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    /**
     * 忘记密码-发送重置邮件
     *
     * @param email
     * @return
     */
    @RequestMapping(value = "/sendForgotPasswordEmail", method = RequestMethod.POST)
    @LoggerManage(description = "发送忘记密码邮件")
    public Response sendForgotPasswordEmail(String email) {
        try {
            User registUser = userRepository.findByEmail(email);
            if (null == registUser) {
                return result(ExceptionMsg.EmailNotRegister);
            }
            String secretKey = UUID.randomUUID().toString(); // 密钥
            Timestamp outDate = new Timestamp(System.currentTimeMillis() + 30 * 60 * 1000);// 30分钟后过期
            long date = outDate.getTime() / 1000 * 1000;
            userRepository.setOutDateAndValidataCode(outDate + "", secretKey, email);
            String key = email + "$" + date + "$" + secretKey;
            String digitalSignature = MD5Util.encrypt(key);// 数字签名
//            String basePath = this.getRequest().getScheme() + "://" + this.getRequest().getServerName() + ":" + this.getRequest().getServerPort() + this.getRequest().getContextPath() + "/newPassword";
            String resetPassHref = forgotpasswordUrl + "?sid="
                    + digitalSignature + "&email=" + email;
            String emailContent = MessageUtil.getMessage(mailContent, resetPassHref);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(mailFrom);
            helper.setTo(email);
            helper.setSubject(mailSubject);
            helper.setText(emailContent, true);
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("sendForgotPasswordEmail failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    /**
     * 忘记密码-设置新密码
     *
     * @param newpwd
     * @param email
     * @param sid
     * @return
     */
    @RequestMapping(value = "/setNewPassword", method = RequestMethod.POST)
    @LoggerManage(description = "设置新密码")
    public Response setNewPassword(String newpwd, String email, String sid) {
        try {
            User user = userRepository.findByEmail(email);
            Timestamp outDate = Timestamp.valueOf(user.getOutDate());
            if (outDate.getTime() <= System.currentTimeMillis()) { //表示已经过期
                return result(ExceptionMsg.LinkOutdated);
            }
            String key = user.getEmail() + "$" + outDate.getTime() / 1000 * 1000 + "$" + user.getValidataCode();//数字签名
            String digitalSignature = MD5Util.encrypt(key);
            if (!digitalSignature.equals(sid)) {
                return result(ExceptionMsg.LinkOutdated);
            }
            userRepository.setNewPassword(getPwd(newpwd), email);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("setNewPassword failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    /**
     * 修改密码
     *
     * @param oldPassword
     * @param newPassword
     * @return
     */
    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    @LoggerManage(description = "修改密码")
    public Response updatePassword(String oldPassword, String newPassword) {
        try {
            User user = getUser();
            String password = user.getPassWord();
            String newpwd = getPwd(newPassword);
            if (password.equals(getPwd(oldPassword))) {
                userRepository.setNewPassword(newpwd, user.getEmail());
                user.setPassWord(newpwd);
                getSession().setAttribute(Const.LOGIN_SESSION_KEY, user);
            } else {
                return result(ExceptionMsg.PassWordError);
            }
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("updatePassword failed, ", e);
            return result(ExceptionMsg.FAILED);
        }
        return result();
    }

    /**
     * 修改个人简介
     *
     * @param introduction
     * @return
     */
    @RequestMapping(value = "/updateIntroduction", method = RequestMethod.POST)
    @LoggerManage(description = "修改个人简介")
    public ResponseData updateIntroduction(String introduction) {
        try {
            User user = getUser();
            userRepository.setIntroduction(introduction, user.getEmail());
            user.setIntroduction(introduction);
            getSession().setAttribute(Const.LOGIN_SESSION_KEY, user);
            return new ResponseData(ExceptionMsg.SUCCESS, introduction);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("updateIntroduction failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    /**
     * 修改昵称
     *
     * @param userName
     * @return
     */
    @RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
    @LoggerManage(description = "修改昵称")
    public ResponseData updateUserName(String userName) {
        try {
            User loginUser = getUser();
            if (userName.equals(loginUser.getUserName())) {
                return new ResponseData(ExceptionMsg.UserNameSame);
            }
            User user = userRepository.findByUserName(userName);
            if (null != user && user.getUserName().equals(userName)) {
                return new ResponseData(ExceptionMsg.UserNameUsed);
            }
            if (userName.length() > 12) {
                return new ResponseData(ExceptionMsg.UserNameLengthLimit);
            }
            userRepository.setUserName(userName, loginUser.getEmail());
            loginUser.setUserName(userName);
            getSession().setAttribute(Const.LOGIN_SESSION_KEY, loginUser);
            return new ResponseData(ExceptionMsg.SUCCESS, userName);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("updateUserName failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/updatePhone", method = RequestMethod.POST)
    @LoggerManage(description = "修改手机号码")
    public ResponseData updatePhone(String phone) {
        try {
            User loginUser = getUser();
            User user = userRepository.findByPhone(phone);
            if (null != user && user.getPhone().equals(phone)) {
                return new ResponseData(ExceptionMsg.PhoneUsed);
            }
            userRepository.setPhone(phone, loginUser.getId());
            loginUser.setPhone(phone);
            getSession().setAttribute(Const.LOGIN_SESSION_KEY, loginUser);
            return new ResponseData(ExceptionMsg.SUCCESS, phone);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("updatePhone failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/updateEmail", method = RequestMethod.POST)
    @LoggerManage(description = "修改邮箱")
    public ResponseData updateEmail(String email) {
        try {
            User loginUser = getUser();
            User user = userRepository.findByEmail(email);
            if (null != user && user.getEmail().equals(email)) {
                return new ResponseData(ExceptionMsg.EmailUsed);
            }
            userRepository.setEmail(email, loginUser.getId());
            loginUser.setEmail(email);
            getSession().setAttribute(Const.LOGIN_SESSION_KEY, loginUser);
            return new ResponseData(ExceptionMsg.SUCCESS, email);
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("updateEmail failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    /**
     * 上传头像
     *
     * @param dataUrl
     * @return
     */
    @RequestMapping(value = "/uploadHeadPortrait", method = RequestMethod.POST)
    public ResponseData uploadHeadPortrait(String dataUrl) {
        logger.info("执行 上传头像 开始");
        try {
            String filePath = staticUrl + fileProfilepicturesUrl;
            String fileName = UUID.randomUUID().toString() + ".png";
            String savePath = fileProfilepicturesUrl + fileName;
            String oldFile = staticUrl;
            String image = dataUrl;
            String header = "data:image";
            String[] imageArr = image.split(",");
            if (imageArr[0].contains(header)) {
                image = imageArr[1];
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] decodedBytes = decoder.decodeBuffer(image);
                FileUtil.uploadFile(decodedBytes, filePath, fileName);
                User user = getUser();
                oldFile = oldFile + user.getProfilePicture();
                userRepository.setProfilePicture(savePath, user.getId());
                user.setProfilePicture(savePath);
                getSession().setAttribute(Const.LOGIN_SESSION_KEY, user);
                FileUtil.deleteFile(oldFile);
            }
            logger.info("头像地址：" + savePath);
            logger.info("执行 上传头像 结束 ");
            return new ResponseData(ExceptionMsg.SUCCESS, savePath);
        } catch (Exception e) {
            logger.error("upload head portrait failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    /**
     * 显示单张图片
     *
     * @return
     */
    @RequestMapping("showHeadPortrait")
    public ResponseEntity showPhotos(String fileName) {
        if (fileName == null || fileName == "") {
            fileName = "img/favicon.png";
        }
        try {
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            return ResponseEntity.ok(resourceLoader.getResource("file:" + staticUrl + fileName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 上传背景
     *
     * @param dataUrl
     * @return
     */
    @RequestMapping(value = "/uploadBackground", method = RequestMethod.POST)
    @LoggerManage(description = "上传背景")
    public ResponseData uploadBackground(String dataUrl) {
        try {
            String filePath = staticUrl + fileBackgroundpicturesUrl;
            String fileName = UUID.randomUUID().toString() + ".png";
            String savePath = fileBackgroundpicturesUrl + fileName;
            String image = dataUrl;
            String header = "data:image";
            String[] imageArr = image.split(",");
            if (imageArr[0].contains(header)) {
                image = imageArr[1];
                BASE64Decoder decoder = new BASE64Decoder();
                byte[] decodedBytes = decoder.decodeBuffer(image);
                FileUtil.uploadFile(decodedBytes, filePath, fileName);
                User user = getUser();
                userRepository.setBackgroundPicture(savePath, user.getId());
                user.setBackgroundPicture(savePath);
                getSession().setAttribute(Const.LOGIN_SESSION_KEY, user);
            }
            logger.info("背景地址：" + savePath);
            return new ResponseData(ExceptionMsg.SUCCESS, savePath);
        } catch (Exception e) {
            logger.error("upload background picture failed, ", e);
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    @LoggerManage(description = "逻辑删除用户")
    public Response deleteUser(Long userId) {
        try {
            userRepository.setIsDelete(IsDelete.YES, userId);
            logger.info("该用户信息逻辑删除成功!");
            return result(ExceptionMsg.SUCCESS);
        } catch (Exception e) {
            logger.error("用户删除失败", e);
            return result(ExceptionMsg.FAILED);
        }
    }

    @RequestMapping(value = "/recoverUser", method = RequestMethod.POST)
    @LoggerManage(description = "逻辑恢复用户")
    public Response recoverUser(Long userId) {
        try {
            userRepository.setIsDelete(IsDelete.NO, userId);
            logger.info("该用户信息逻辑恢复成功!");
            return result(ExceptionMsg.SUCCESS);
        } catch (Exception e) {
            logger.error("用户恢复失败", e);
            return result(ExceptionMsg.FAILED);
        }
    }
}
