package com.liu.servlrt.Bill;

import com.alibaba.fastjson.JSONArray;
import com.liu.pojo.Bill;
import com.liu.pojo.Provider;
import com.liu.pojo.User;
import com.liu.service.Bill.BillImpl;
import com.liu.service.Bill.BillService;
import com.liu.service.provider.ProviderImpI;
import com.liu.service.provider.ProviderService;
import com.liu.util.Constants;
import com.mysql.cj.util.StringUtils;
import lombok.SneakyThrows;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BillServlet extends HttpServlet {
    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if (method != null && method.equals("query")) {
            this.query(req, resp);
        } else if (method != null && method.equals("modifysave")) {
            this.modify(req, resp);
        } else if (method != null && method.equals("modify")) {
            this.getBillById(req, resp, "billmodify.jsp");
        } else if (method != null && method.equals("getproviderlist")) {
            this.getProviderlist(req, resp);
        } else if (method != null && method.equals("delbill")) {
            this.delBillByid(req, resp);
        } else if (method != null && method.equals("add")) {
            this.addBill(req, resp);
        } else if (method != null && method.equals("view")) {
            this.getBillById(req, resp, "billview.jsp");
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
    public void query(HttpServletRequest req,HttpServletResponse resp) throws ServletException, IOException {

        String queryProductName = req.getParameter("queryProductName");
        String queryProviderId = req.getParameter("queryProviderId");
        String queryIsPayment = req.getParameter("queryIsPayment");
        if (queryProductName==null)
        {
            queryProductName="";
        }
        if (queryProviderId==null)
        {
            queryProviderId="0";
        }
        if (queryIsPayment==null)
        {
            queryIsPayment="0";
        }
        List<Bill>bills=null;
        HashMap map=new HashMap();
        map.put("productName",queryProductName);
        map.put("ProviderId",Integer.parseInt(queryProviderId));
        map.put("isPayment",Integer.parseInt(queryIsPayment));
        BillService billService=new BillImpl();
        bills=billService.getBillList(map);
        req.setAttribute("billList", bills);
        req.setAttribute("queryProductName", queryProductName);
        req.setAttribute("queryProviderId", queryProviderId);
        req.setAttribute("queryIsPayment", queryIsPayment);
        req.getRequestDispatcher("billlist.jsp").forward(req, resp);
    }
    public void modify(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException {
        String id = req.getParameter("id");
        String productName = req.getParameter("productName");
        String productDesc = req.getParameter("productDesc");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");
        HashMap map=new HashMap();
        map.put("bid",Integer.valueOf(id));
        map.put("productName",productName);
        map.put("productDesc",productDesc);
        map.put("productUnit",productUnit);
        map.put("productCount",new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
        map.put("totalPrice",new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        map.put("providerId",Integer.parseInt(providerId));
        map.put("isPayment",Integer.parseInt(isPayment));
        map.put("modifyBy",((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        map.put("modifyDate",new Date());
        BillService billService=new BillImpl();
        boolean flag=false;
        flag=billService.modifyBill(map);
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/bill.do?method=query");
        }else{
            req.getRequestDispatcher("billmodify.jsp").forward(req, resp);
        }
    }
    private void getBillById(HttpServletRequest request, HttpServletResponse response,String url)
            throws ServletException, IOException {
        String id = request.getParameter("billid");
        if(!StringUtils.isNullOrEmpty(id)){
            BillService billService = new BillImpl();
            HashMap map=new HashMap();
            Bill bill = null;
            map.put("bid",id);
            bill=billService.getBillBybid(map);
            request.setAttribute("bill", bill);
            System.out.println(bill.getProviderName());
            request.getRequestDispatcher(url).forward(request, response);
        }
    }
    private void getProviderlist(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        List<Provider> providers = new ArrayList<>();
        ProviderService providerService=new ProviderImpI();
        HashMap map=new HashMap();
        map.put("jj",1);
        providers = providerService.getProviderList(map);
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(providers));
        writer.flush();
        writer.close();
    }
    public void delBillByid(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        boolean flag=false;
        String bid=req.getParameter("billid");
        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (!StringUtils.isNullOrEmpty(bid))
        {
            HashMap map=new HashMap();
            map.put("bid",Integer.parseInt(bid));
            BillService billService=new BillImpl();
            flag=billService.delBillByid(map);
            if (flag=true)
            {
              resultMap.put("delResult","true");
            }else if (flag=false)
            {
                resultMap.put("delResult","false");
            }else {
                resultMap.put("delResult","notexist");
            }
        }
        resp.setContentType("application/json");
        PrintWriter printWriter=resp.getWriter();
        printWriter.write(JSONArray.toJSONString(resultMap));
        printWriter.flush();
        printWriter.close();
    }
    public void addBill(HttpServletRequest req,HttpServletResponse resp) throws IOException, ServletException {
        HashMap map=new HashMap();
        boolean flag=false;
        String billCode = req.getParameter("billCode");
        String productName = req.getParameter("productName");
        String productDesc = req.getParameter("productDesc");
        String productUnit = req.getParameter("productUnit");
        String productCount = req.getParameter("productCount");
        String totalPrice = req.getParameter("totalPrice");
        String providerId = req.getParameter("providerId");
        String isPayment = req.getParameter("isPayment");
        map.put("billCode",billCode);
        map.put("productName",productName);
        map.put("productDesc",productDesc);
        map.put("productUnit",productUnit);
        map.put("productCount",new BigDecimal(productCount).setScale(2,BigDecimal.ROUND_DOWN));
        map.put("totalPrice",new BigDecimal(totalPrice).setScale(2,BigDecimal.ROUND_DOWN));
        map.put("providerId",Integer.parseInt(providerId));
        map.put("isPayment",Integer.parseInt(isPayment));
        map.put("createdBy",((User)req.getSession().getAttribute(Constants.USER_SESSION)).getId());
        map.put("creationDate",new Date());
        BillService service=new BillImpl();
        flag=service.addBill(map);
        if(flag){
            resp.sendRedirect(req.getContextPath()+"/jsp/bill.do?method=query");
            //req.getRequestDispatcher("").forward(req, resp);
        }else{
            req.getRequestDispatcher("billadd.jsp").forward(req, resp);
        }
    }
}
