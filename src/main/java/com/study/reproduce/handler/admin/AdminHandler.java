package com.study.reproduce.handler.admin;

import com.study.reproduce.model.domain.Admin;
import com.study.reproduce.model.domain.AdminDetails;
import com.study.reproduce.model.request.AdminLoginData;
import com.study.reproduce.service.*;
import com.study.reproduce.utils.PatternUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/admin")
public class AdminHandler {

    @Resource
    AdminService adminService;

//    @PostMapping("/login")
    public void login(AdminLoginData loginData, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String account = loginData.getAccount();
        String password = loginData.getPassword();
        String verifyCode = loginData.getVerifyCode();
        if (StringUtils.isAnyBlank(account, password, verifyCode)) {
            request.setAttribute("errorMsg", "参数错误");
            response.sendRedirect(request.getContextPath() + "/admin/login");
        } else {
            Admin admin = adminService.login(account, password, verifyCode, request.getSession());
            if (admin == null) {
                response.sendRedirect(request.getContextPath() + "/admin/login");
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/index");
            }
        }
    }

    @PostMapping("/profile/password")
    public String passwordUpdate(HttpServletRequest request, String originalPassword, String newPassword) {
        if (StringUtils.isAnyBlank(originalPassword, newPassword)) {
            return "参数不能为空";
        }
        Integer adminUserId = getAdminUserId(request);
        if (adminUserId == null) {
            return "请先登录";
        }
        if (adminService.updatePassword(adminUserId, originalPassword, newPassword)) {
            //清除之前的登录信息
            request.getSession().removeAttribute(AdminService.GET_ADMIN_KEY);
            request.getSession().removeAttribute("errorMsg");
            //返回 success 让前端重新登录
            return "success";
        }
        return "修改失败";
    }

    /**
     * 修改名称
     * @param request
     * @param loginUserName
     * @param nickName
     * @return
     */
    @PostMapping("/profile/name")
    public String nameUpdate(HttpServletRequest request, String loginUserName, String nickName) {
        if (StringUtils.isAnyBlank(loginUserName, nickName)) {
            return "参数不能为空";
        }
        Integer adminUserId = getAdminUserId(request);
        if (adminUserId == null) {
            return "请先登录";
        }
        if (!PatternUtil.isLegal(loginUserName) || !PatternUtil.isLegal(nickName)) {
            return "参数格式不合法";
        }
        if (adminService.updateName(adminUserId, loginUserName, nickName)) {
            return "success";
        } else {
            return "修改失败";
        }
    }

    /**
     * 获取登录管理员id
     * @param request request
     * @return id
     */
    public Integer getAdminUserId(HttpServletRequest request) {
        String username = (String) request.getSession().getAttribute(AdminService.GET_ADMIN_KEY);
        if (username == null) {
            return null;
        }
        AdminDetails adminDetails = (AdminDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Admin admin = adminService.getAdminByUsername(adminDetails.getUsername());
        return admin != null ? admin.getAdminUserId() : null;
    }
}
