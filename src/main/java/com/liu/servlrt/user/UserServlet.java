package com.liu.servlrt.user;

import com.alibaba.fastjson.JSONArray;
import com.liu.pojo.Role;
import com.liu.pojo.User;
import com.liu.service.role.RoleImpI;
import com.liu.service.role.RoleService;
import com.liu.service.user.UserImpl;
import com.liu.service.user.UserService;
import com.liu.util.Constants;
import com.liu.util.PageSupport;
import com.mysql.cj.util.StringUtils;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method != null && method.equals("pwdmodify")) {
            System.out.println(method);
            this.pwdModify(req, resp);
        } else if (method != null && method.equals("savepwd")) {
            this.updatePwd(req, resp);
        } else if (method != null && method.equals("query")) {
            this.query(req, resp);
        } else if (method != null && method.equals("deluser")) {
            this.delUser(req, resp);
        } else if (method != null && method.equals("getrolelist")) {
            this.getRoleList(req, resp);
        } else if (method != null && method.equals("ucexist")) {
            this.userCodeExist(req, resp);
        } else if (method != null && method.equals("add")) {
            this.add(req, resp);
        }else if (method != null && method.equals("view"))
        {
            this.getUserById(req,resp,"userview.jsp");
        }else if(method != null && method.equals("modify")) {
            this.getUserById(req, resp,"usermodify.jsp");
        }else if(method!=null && method.equals("modifyexe"))
        {
            this.updateUserById(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String newPassword = req.getParameter("newpassword");
        boolean flag = false;
        if (o != null && !StringUtils.isNullOrEmpty("newpassword")) {
            UserService userService = new UserImpl();
            HashMap map = new HashMap();
            map.put("id", ((User) o).getId());
            map.put("userPassword", newPassword);
            flag = userService.updatePwd(map);
            if (flag) {
                req.setAttribute("message", "修改密码成功，请用新密码登录");
                req.getSession().removeAttribute(Constants.USER_SESSION);
            } else {
                req.setAttribute("message", "密码修改失败");
            }
        } else {
            req.setAttribute("message", "新密码有问题");
        }
        req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
    }

    public void pwdModify(HttpServletRequest req, HttpServletResponse resp) {
        Object o = req.getSession().getAttribute(Constants.USER_SESSION);
        String soldPassword = ((User) o).getUserPassword();
        String oldPassword = req.getParameter("oldpassword");
        Map<String, String> resultMap = new HashMap<String, String>();
        System.out.println("现在输入的" + oldPassword);
        System.out.println("系统有的" + soldPassword);
        if (o == null) {
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldPassword)) {
            resultMap.put("result", "error");
        } else {
            if (oldPassword.equals(soldPassword)) {
                resultMap.put("result", "true");
            } else {
                resultMap.put("result", "false");
            }
        }
        try {
            resp.setContentType("application/json");
            PrintWriter writer = resp.getWriter();
            writer.write(JSONArray.toJSONString(resultMap));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void query(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException, ServletException {
        UserService userService = new UserImpl();
        String queryUserName = req.getParameter("queryname");
        String temp = req.getParameter("queryUserRole");
        String pageIndex = req.getParameter("pageIndex");
        int queryUserRole = 0;
        int pageSize = 5;
        //int userStart=*/;
        //当前页数
        int currentPageNo = 1;
        List<User> userList = null;
        if (temp != null && !temp.equals("")) {
            queryUserRole = Integer.parseInt(temp);//给查询赋值
        }
        if (pageIndex != null) {
            try {
                currentPageNo = Integer.valueOf(pageIndex);
            } catch (NumberFormatException e) {
                resp.sendRedirect("error.jsp");
            }
        }
        HashMap countmap = new HashMap();
        countmap.put("userName", queryUserName);
        countmap.put("id", queryUserRole);
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);
        //总数量（表）
        int totalCount = userService.getUserCount(countmap);
        pages.setPageSize(pageSize);
        pages.setTotalCount(totalCount);
        int totalPageCount = pages.getTotalPageCount();
        //控制首页和尾页
        if (currentPageNo < 1) {
            currentPageNo = 1;
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }
        int userStart = pageSize * (currentPageNo - 1);
        HashMap map = new HashMap();
        map.put("userName", queryUserName);
        map.put("userRole", queryUserRole);
        map.put("userStart", userStart);
        map.put("pageSize", pageSize);
        map.put("orde", 2);
        userList = userService.getUserList(map);
        req.setAttribute("userList", userList);
        List<Role> roleList = null;
        RoleService roleService = new RoleImpI();
        roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);
        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.getRequestDispatcher("userlist.jsp").forward(req, resp);
    }

    public void delUser(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String tmp = req.getParameter("uid");
        System.out.println(tmp);
        Integer uid = 0;
        try {
            uid = Integer.parseInt(tmp);
        } catch (Exception e) {
            uid = 0;
        }
        HashMap resultMap = new HashMap();
        HashMap map = new HashMap();
        if (uid <= 0) {
            resultMap.put("delResult", "notexist");
        } else {
            UserService userService = new UserImpl();
            map.put("id", uid);
            if (userService.DelUser(map)) {
                resultMap.put("delResult", "true");
            } else {
                resultMap.put("delResult", "false");
            }
        }
        resp.setContentType("application/json");
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(JSONArray.toJSONString(resultMap));
        printWriter.flush();
        printWriter.close();
    }

    public void getRoleList(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        RoleService roleService = new RoleImpI();
        List<Role> roleList = null;
        roleList = roleService.getRoleList();
        resp.setContentType("application/json");
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(JSONArray.toJSONString(roleList));
        printWriter.flush();
        printWriter.close();
    }

    public void userCodeExist(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String userCode = null;
        User user = null;
        userCode = req.getParameter("userCode");
        HashMap map = new HashMap();
        HashMap resultMap = new HashMap();
        if (StringUtils.isNullOrEmpty(userCode)) {
            resultMap.put("userCode", "exist");
        } else {
            UserService service = new UserImpl();
            map.put("userCode", userCode);
            user = service.selectUserCodeExist(map);
            if (null != user) {
                resultMap.put("userCode", "exist");
            } else {
                resultMap.put("userCode", "noexist");
            }
        }
        resp.setContentType("application/json");
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(JSONArray.toJSONString(resultMap));
        printWriter.flush();
        printWriter.close();
    }

    public void add(HttpServletRequest req, HttpServletResponse resp) throws ParseException, IOException, ServletException {
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        HashMap map = new HashMap();
        map.put("userCode", userCode);
        map.put("userName", userName);
        map.put("userPassword", userPassword);
        map.put("gender", Integer.parseInt(gender));
        map.put("birthday", new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        map.put("phone", phone);
        map.put("address", address);
        map.put("userRole", Integer.parseInt(userRole));
        map.put("creationDate", new Date());
        map.put("createdBy", ((User) req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        //记得在dao中添加创建时间，刚才忘记写了，待会也要记的在servlet中添加创建时间以及创建者
        UserService userService = new UserImpl();
        if (userService.add(map)) {
            resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
        } else {
            req.getRequestDispatcher("useradd.jsp").forward(req, resp);
        }
    }

    public void getUserById(HttpServletRequest req, HttpServletResponse resp, String url) throws IOException, ServletException {
        UserService service = new UserImpl();
        String id = req.getParameter("uid");
        System.out.println(id);
        if (!StringUtils.isNullOrEmpty(id)) {
            //调用后台方法得到user对象
            HashMap map = new HashMap();
            map.put("uid", id);
            UserService userService = new UserImpl();
            User user = userService.selectUserById(map);
            req.setAttribute("user", user);
            req.getRequestDispatcher(url).forward(req, resp);
        }
    }
    public void updateUserById(HttpServletRequest req,HttpServletResponse resp) throws ParseException, IOException, ServletException {
        String id = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");
        HashMap map=new HashMap();
        map.put("uid", Integer.parseInt(id));
        map.put("userName", userName);
        map.put("gender", Integer.parseInt(gender));
        //map.put("birthday", new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        try {
           map.put("birthday",new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        map.put("phone", phone);
        map.put("address", address);
        map.put("userRole", Integer.parseInt(userRole));
        map.put("modifyDate", new Date());
        map.put("modifyBy", ((User) req.getSession().getAttribute(Constants.USER_SESSION)).getId());

        UserService userService = new UserImpl();
        if(userService.updateUser(map)){
            resp.sendRedirect(req.getContextPath()+"/jsp/user.do?method=query");
        }else{
            req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
        }
    }
}
