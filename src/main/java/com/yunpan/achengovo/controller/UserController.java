package com.yunpan.achengovo.controller;

import com.yunpan.achengovo.entity.AutoLogin;
import com.yunpan.achengovo.entity.User;
import com.yunpan.achengovo.service.UserService;
import com.yunpan.achengovo.utils.SendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import static com.yunpan.achengovo.utils.Md5Util.remd5;
import static com.yunpan.achengovo.utils.VerCode.getRandomVerCode;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@RequestBody User user, HttpSession session, HttpServletResponse response) {
        Boolean isAutoLogin = user.getAutoLogin();
        System.out.println(user);
        user = userService.login(user);
        System.out.println(user);
        if (user != null) {
            if (isAutoLogin) {
                String autoLoginUUID = String.valueOf(UUID.randomUUID());
                Cookie autoLoginCode = new Cookie("autoLoginCode", autoLoginUUID);
                autoLoginCode.setMaxAge(7 * 24 * 60 * 60);
                autoLoginCode.setPath("/");
                response.addCookie(autoLoginCode);
                AutoLogin autoLogin = new AutoLogin();
                autoLogin.setAutoLoginCode(autoLoginUUID);
                autoLogin.setUserId(user.getUserId());
                userService.addAutoLogin(autoLogin);
            }
            if (user.getUserType().equals("admin")) {
                session.setAttribute("ADMIN_SESSION", user);
                return "admin";
            }
            session.setAttribute("USER_SESSION", user);
            return "file";
        }
        return "用户名或密码错误";
    }

    /**
     * 通过Cookie登录
     */
    @RequestMapping(value = "/loginByCookie", method = RequestMethod.POST)
    public String loginByCookie(HttpSession session, HttpServletRequest request) throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();
        String autoLoginCodeCookie = "";
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("autoLoginCode")) {
                    autoLoginCodeCookie = cookie.getValue();
                }
            }
        }
        System.out.println(autoLoginCodeCookie);
        AutoLogin autoLogin = new AutoLogin();
        autoLogin.setAutoLoginCode(autoLoginCodeCookie);
        autoLogin = userService.isAutoLogin(autoLogin);
        if (autoLogin != null) {
            User user = new User();
            user.setUserId(autoLogin.getUserId());
            user = userService.loginById(user);
            if (user != null) {
                if (user.getUserType().equals("admin")) {
                    session.setAttribute("ADMIN_SESSION", user);
                    return "admin";
                }
                session.setAttribute("USER_SESSION", user);
                return "file";
            }
        }
        return "toLogin";
    }

    /**
     * 退出登录
     */
    @ResponseBody
    @RequestMapping(value = "/loginOut", method = RequestMethod.POST)
    public String loginOut(HttpSession session, HttpServletResponse response) {
        //清除Session
        session.invalidate();
        //清除自动登录cookie
        Cookie autoLoginCode = new Cookie("autoLoginCode", "autoLoginCode");
        autoLoginCode.setMaxAge(0);
        autoLoginCode.setPath("/");
        response.addCookie(autoLoginCode);
        return "success";
    }

    /**
     * 注册验证码
     */
    @ResponseBody
    @RequestMapping(value = "/registerCode")
    public String register(String email, HttpSession session) throws MessagingException {
        String varCode = getRandomVerCode(5);
        session.setAttribute("varCode", varCode);
        SendMail.sendNetMail(email, "您本次操作的验证码为【" + varCode + "】", "MyFiles验证码");
        return "success";
    }

    /**
     * 用户注册
     *
     * @param user
     * @param session
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody User user, HttpSession session) {
        System.out.println(user);
        String varCode = (String) session.getAttribute("varCode");
        if (user.getVarCode().equalsIgnoreCase(varCode)) {
            if (userService.hasUser(user) == 0) {
                if (userService.register(user) != 0) {
                    System.out.println("注册成功");
                    user = userService.login(user);
                    session.setAttribute("USER_SESSION", user);
                    return "success";
                } else {
                    return "注册失败";
                }
            } else {
                return "用户名已存在";
            }
        } else {
            return "验证码错误";
        }

    }

    /**
     * 向注册页面跳转
     *
     * @return
     */
    @RequestMapping("/toRegister")
    public String toRegister() {
        return "register";
    }

    /**
     * 向用户登录页面跳转
     */
    @RequestMapping(value = "/toLogin", method = RequestMethod.GET)
    public String toLogin(HttpServletRequest request, HttpSession session, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        String autoLoginCodeCookie = "";
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("autoLoginCode")) {
                    autoLoginCodeCookie = cookie.getValue();
                }
            }
        }
        System.out.println(autoLoginCodeCookie);
        AutoLogin autoLogin = new AutoLogin();
        autoLogin.setAutoLoginCode(autoLoginCodeCookie);
        autoLogin = userService.isAutoLogin(autoLogin);
        if (autoLogin != null) {
            User user = new User();
            user.setUserId(autoLogin.getUserId());
            user = userService.loginById(user);
            if (user != null) {
                if (user.getUserType().equals("admin")) {
                    session.setAttribute("ADMIN_SESSION", user);
                    response.sendRedirect(request.getContextPath() + "/admin");
                    return "admin";
                }
                session.setAttribute("USER_SESSION", user);
                response.sendRedirect(request.getContextPath() + "/file");
                return "file";
            }
        }
        return "login";
    }

    /**
     * 向admin页面跳转
     */
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String toAdmin() {
        return "admin";
    }

    /**
     * 向用户文件页面跳转
     *
     * @return
     */
    @RequestMapping(value = "/file", method = RequestMethod.GET)
    public String toFile() {
        return "file";
    }

    /**
     * 向个人中心页面跳转
     */
    @RequestMapping(value = "/user")
    public String user() {
        return "user";
    }

    /**
     * 获取个人资料
     */
    @ResponseBody
    @RequestMapping(value = "/getUserInfo")
    public User getUserInfo(HttpSession session) throws MessagingException {
        User user = (User) session.getAttribute("USER_SESSION");
        user = userService.getUserInfo(user);
        session.setAttribute("USER_SESSION", user);
        user.setUserPass("");
        return user;
    }

    /**
     * 上传头像
     */
    @ResponseBody
    @RequestMapping(value = "/uploadAvatar", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public String upload(
            @RequestParam("uploadAvatar") List<MultipartFile> uploadfile,
            HttpServletRequest request, HttpSession session) {
        User user = (User) session.getAttribute("USER_SESSION");
        //判断上传文件是否存在
        if (!uploadfile.isEmpty() && uploadfile.size() > 0) {
            String newFilename = "";
            //循环输出上传的文件
            for (MultipartFile file : uploadfile) {
                //上传文件的原始名称
                String originalFilename = file.getOriginalFilename();
                //设置上传文件的保存地址目录
//                String dirPath = "D://桌面/upload/";
                String dirPath = request.getServletContext().getRealPath("/static/avatar/");
                File filePath = new File(dirPath);
                //如果保存文件的地址不存在就先创建目录
                if (!filePath.exists()) {
                    filePath.mkdirs();
                }
                //使用UUID重新命名上传的文件名称
                newFilename = UUID.randomUUID() + "_" + originalFilename;
                try {
                    file.transferTo(new File(dirPath + newFilename));
                    user.setUserAvatar(newFilename);
                    if (userService.uploadAvatar(user) == 0) {
                        return "error";
                    }
                    return newFilename;
                } catch (Exception e) {
                    e.printStackTrace();
                    return "error";
                }
            }
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 修改用户信息
     */
    @ResponseBody
    @RequestMapping(value = "/changeUser", method = RequestMethod.POST)
    public String changeUser(@RequestBody User user, HttpSession session) {
        User user1 = (User) session.getAttribute("USER_SESSION");
        if (!user1.getUserEmail().equals(user.getUserEmail())) {
            String varCode = (String) session.getAttribute("varCode");
            if (!varCode.equalsIgnoreCase(user.getVarCode())) {
                return "验证码错误";
            }
        }
        if (!user.getUserName().equals(user1.getUserName())) {
            if (userService.hasUser(user) > 0) {
                return "用户名已存在";
            }
        }
        user.setUserId(user1.getUserId());
        if (userService.changeUser(user) > 0) {
//            user=userService.login(user1);
//            session.setAttribute("USER_SESSION",user);
            return "success";
        } else {
            return "修改失败";
        }
    }

    /**
     * 修改密码
     */
    @ResponseBody
    @RequestMapping(value = "/changePassByPass", method = RequestMethod.POST, produces = "application/json;charset=utf8")
    public String changePassByPass(@RequestParam String userPass, @RequestParam String newPass, HttpSession session, HttpServletResponse response) {
        userPass = remd5(userPass);
        newPass = remd5(newPass);
        User user1 = (User) session.getAttribute("USER_SESSION");
        Integer userId = user1.getUserId();
        if (userService.changePassByPass(userId, userPass, newPass) > 0) {
            //清除Session
            session.invalidate();
            //清除自动登录cookie
            Cookie autoLoginCode = new Cookie("autoLoginCode", "autoLoginCode");
            autoLoginCode.setMaxAge(0);
            autoLoginCode.setPath("/");
            response.addCookie(autoLoginCode);
            return "success";
        } else {
            return "fail";
        }
    }

    /**
     * 去忘记密码页面
     */
    @RequestMapping(value = "/toForgetPass")
    public String toForgetPass() {
        return "forgetPass";
    }

    /**
     * 根据用户名发送邮件
     */
    @ResponseBody
    @RequestMapping(value = "/forgetPassSendEmail")
    public String forgetPassSendEmail(@RequestBody User user, HttpSession session) throws MessagingException {
        user = userService.getUserByUserName(user);
        if (user != null) {
            String varCode = getRandomVerCode(5);
            session.setAttribute("varCode", varCode);
            session.setAttribute("userName", user.getUserName());
            SendMail.sendNetMail(user.getUserEmail(), "您本次操作的验证码为【" + varCode + "】", "MyFiles验证码");
            return "success";
        } else {
            return "用户名不存在";
        }
    }

    /**
     * 判断验证码是否正确
     */
    @ResponseBody
    @RequestMapping(value = "/forgetPassCode")
    public String forgetPassCode(@RequestParam String varcode, HttpSession session) {
        String varCode = (String) session.getAttribute("varCode");
        if (varcode.equalsIgnoreCase(varCode)) {
            return "success";
        } else {
            return "验证码错误";
        }
    }

    /**
     * 新密码
     */
    @ResponseBody
    @RequestMapping(value = "/forgetPassNewPass")
    public String forgetPassNewPass(@RequestBody User user, HttpSession session) {
        String varCode = (String) session.getAttribute("varCode");
        if (user.getVarCode().equalsIgnoreCase(varCode)) {
            user.setUserName((String) session.getAttribute("userName"));
            if (userService.updatePassByUserName(user) > 0) {
                return "success";
            } else {
                return "错误";
            }
        } else {
            return "验证码错误";
        }
    }
}
