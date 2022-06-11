package com.liu.service.Bill;

import com.liu.pojo.Bill;

import java.util.List;
import java.util.Map;

public interface BillService {
    public int getBillBypid(Map map);
    public List<Bill> getBillList(Map map);
    public boolean modifyBill(Map map);
    public Bill getBillBybid(Map map);
    public boolean delBillByid(Map map);
    public boolean addBill(Map map);
}
