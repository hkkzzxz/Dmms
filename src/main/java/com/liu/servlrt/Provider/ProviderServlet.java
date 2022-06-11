package com.liu.servlrt.Provider;

import com.alibaba.fastjson.JSON;
import com.liu.pojo.Provider;
import com.liu.pojo.User;
import com.liu.service.provider.ProviderImpI;
import com.liu.service.provider.ProviderService;
import com.liu.service.user.UserImpl;
import com.liu.service.user.UserService;
import com.liu.util.Constants;
import com.mysql.cj.util.StringUtils;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProviderServlet extends HttpServlet {
    //这里后期需要把代码的链接改成url好一点
    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method != null && method.equals("query")) {
           this.query(req,resp);
        }else if (method!=null &&method.equals("modifysave"))
        {
            this.updateProByid(req,resp);
        }else if (method!=null&&method.equals("modify"))
        {
            this.selectProByid(req,resp,"providermodify.jsp");
        }else if (method!=null&&method.equals("delprovider"))
        {
            System.out.println("在删除");
            this.delProvider(req,resp);
        }else if (method!=null&&method.equals("add"))
        {
            this.add(req,resp);
        }else if (method!=null&&method.equals("view"))
        {
            this.ProView(req,resp);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
    public void query(HttpServletRequest req,HttpServletResponse resp) throws SQLException, IOException, ServletException {
        String queryProCode=req.getParameter("queryProCode");
        String queryProName=req.getParameter("queryProName");
        if (StringUtils.isNullOrEmpty(queryProCode))
        {
            queryProCode="";
        }
        if (StringUtils.isNullOrEmpty(queryProName))
        {
            queryProName="";
        }
        HashMap map=new HashMap();
        map.put("proCode",queryProCode);
        map.put("proName",queryProName);
        ProviderService providerService=new ProviderImpI();
        List<Provider> providerList=null;
        providerList=providerService.getProviderList(map);
        req.setAttribute("providerList", providerList);
        req.setAttribute("queryProName", queryProName);
        req.setAttribute("queryProCode", queryProCode);
        req.getRequestDispatcher("providerlist.jsp").forward(req, resp);
    }
    public void delProvider(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        String pid=req.getParameter("proid");
        System.out.println(pid);
        HashMap map=new HashMap();
        map.put("pid",pid);
        ProviderService providerService=new ProviderImpI();
        int delCount=-1;
        delCount=providerService.delProvider(map);
        HashMap resultMap=new HashMap();
        if (delCount==0)
        {
            resultMap.put("delResult","true");
        }else if (delCount==-1)
        {
            resultMap.put("delResult","false");
        }else if (delCount>0)
        {
            resultMap.put("delResult",String.valueOf(delCount));
        }else {
            resultMap.put("delResult","notexist");
        }
        resp.setContentType("application/json");
        PrintWriter printWriter=resp.getWriter();
        printWriter.write(JSON.toJSONString(resultMap));
        printWriter.flush();
        printWriter.close();
    }
    public void updateProByid(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException {
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");
        String id = req.getParameter("proid");
        HashMap map=new HashMap();
        //map.put("proContact",proContact);
        map.put("proPhone",proPhone);
        map.put("proAddress",proAddress);
        map.put("proFax",proFax);
        map.put("proDesc",proDesc);
        map.put("proContact",proContact);
        map.put("pid",Integer.parseInt(id));
        map.put("modifyDate", new Date());
        map.put("modifyBy", ((User) req.getSession().getAttribute(Constants.USER_SESSION)).getId());

        ProviderService providerService=new ProviderImpI();
        if(providerService.updateProvider(map)){
            resp.sendRedirect(req.getContextPath()+"/jsp/provider.do?method=query");
        }else{
            req.getRequestDispatcher("providermodify.jsp").forward(req, resp);
        }
    }
    public void selectProByid(HttpServletRequest req, HttpServletResponse resp, String url) throws IOException, ServletException {
        String id=req.getParameter("proid");
        if (!StringUtils.isNullOrEmpty(id))
        {
            ProviderService providerService=new ProviderImpI();
            HashMap map=new HashMap();
            map.put("pid",Integer.parseInt(id));
            Provider provider=providerService.selectProvider(map);
            req.setAttribute("provider",provider);
            req.getRequestDispatcher(url).forward(req,resp);
        }
    }
    public void add(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException {
        String proCode = req.getParameter("proCode");
        String proName = req.getParameter("proName");
        String proContact = req.getParameter("proContact");
        String proPhone = req.getParameter("proPhone");
        String proAddress = req.getParameter("proAddress");
        String proFax = req.getParameter("proFax");
        String proDesc = req.getParameter("proDesc");
        HashMap map=new HashMap();
        map.put("proCode",proCode);
        map.put("proName",proName);
        map.put("proContact",proContact);
        map.put("proPhone",proPhone);
        map.put("proAddress",proAddress);
        map.put("proFax",proFax);
        map.put("proDesc",proDesc);
        map.put("creationDate", new Date());
        map.put("createdBy", ((User) req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        ProviderService providerService=new ProviderImpI();
        boolean flag=false;
        flag=providerService.addPro(map);
        if (flag)
        {
            resp.sendRedirect(req.getContextPath() + "/jsp/provider.do?method=query#");
        }else {
            req.getRequestDispatcher("provideradd.jsp").forward(req, resp);
        }
    }
    public void ProView(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException, SQLException {
        String id = req.getParameter("proid");
        if(!StringUtils.isNullOrEmpty(id)){
            ProviderService providerService = new ProviderImpI();
            Provider provider = null;
            HashMap map=new HashMap();
            map.put("pid",id);
            provider = providerService.selectProvider(map);
            req.setAttribute("provider", provider);
            req.getRequestDispatcher("providerview.jsp").forward(req, resp);
        }
    }
}
