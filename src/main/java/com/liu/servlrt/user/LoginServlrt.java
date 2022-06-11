package com.liu.servlrt.user;

import com.liu.pojo.User;
import com.liu.service.user.UserImpl;
import com.liu.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class LoginServlrt extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      String UserCode=req.getParameter("userCode");
      String UserPassword=req.getParameter("userPassword");
        UserImpl user1=new UserImpl();
        User user=null;
        HashMap map=new HashMap();
        map.put("userCode",UserCode);
        map.put("userPassword",UserPassword);
        try {
            user=user1.login(map);
        }catch (Exception ioException)
        {
            ioException.printStackTrace();
        }
        if (user != null) {
            System.out.println("有的");
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            resp.sendRedirect("jsp/frame.jsp");
        }else {
            req.setAttribute("error","用户或者密码不正确");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
