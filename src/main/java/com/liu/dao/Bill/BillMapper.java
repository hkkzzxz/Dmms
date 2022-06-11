package com.liu.dao.Bill;

import com.liu.pojo.Bill;

import java.util.List;
import java.util.Map;

public interface BillMapper {
    public Integer getBillBypid(Map map);//写错了应该是count的
    public List<Bill> getBillList(Map map);
    public int updateByid(Map map);
    public Bill getBillBybid(Map map);
    public int delBillByid(Map map);
    public int addBill(Map map);
}
