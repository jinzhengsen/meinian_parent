package com.jzs.security;

import com.alibaba.fastjson.JSON;
import com.jzs.entity.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Description:
 * Ahthor:Jin Zhengsen
 * Date:2021/2/1  9:02
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
         if (isAjaxRequest(httpServletRequest)){// AJAX请求,使用response发送403
             Result result=new Result(false,"您没有权限哦","403");
             String json=JSON.toJSONString(result);
             httpServletResponse.getWriter().print(json);
         }else {// 同步请求处理
             httpServletRequest.getRequestDispatcher("/pages/error/403.html").forward(httpServletRequest,httpServletResponse);
         }
    }


    /**
     * 判断是否为ajax请求
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        if (request.getHeader("accept").indexOf("application/json") > -1
                || (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest"))) {
            return true;
        }
        return false;
    }
}
