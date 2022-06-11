package com.liu.service.Bill;

import com.liu.dao.Bill.BillMapper;
import com.liu.pojo.Bill;
import com.liu.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BillImpl implements BillService{
    @Override
    public int getBillBypid(Map map) {
        int count=-1;
        SqlSession sqlSession= MybatisUtils.getSqlSession();
        BillMapper mapper=sqlSession.getMapper(BillMapper.class);
        count=mapper.getBillBypid(map);
        sqlSession.close();
        return count;
    }

    @Override
    public List<Bill> getBillList(Map map) {
        List<Bill> bills=null;
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        BillMapper mapper=sqlSession.getMapper(BillMapper.class);
        bills=mapper.getBillList(map);
        System.out.println("在找了");
        sqlSession.close();
        return bills;
    }

    @Override
    public boolean modifyBill(Map map) {
        boolean flag=false;
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        BillMapper mapper=sqlSession.getMapper(BillMapper.class);
        int count=0;
        try {
            System.out.println("在搞了");
            count=mapper.updateByid(map);
            if (count>0)
            {
                flag=true;
                sqlSession.commit();
            }
        }catch (Exception e)
        {
            System.out.println("出错了");
            e.printStackTrace();
            sqlSession.rollback();
        }
        return flag;
    }

    @Override
    public Bill getBillBybid(Map map) {
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        BillMapper mapper=sqlSession.getMapper(BillMapper.class);
        Bill bill=mapper.getBillBybid(map);
        sqlSession.close();
        return bill;
    }

    @Override
    public boolean delBillByid(Map map) {
        int count=0;
        boolean flag=false;
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        BillMapper mapper=sqlSession.getMapper(BillMapper.class);
        try {
            count=mapper.delBillByid(map);
            if (count>0)
            {
                flag=true;
                sqlSession.commit();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            sqlSession.rollback();
        }
        sqlSession.close();
        return flag;
    }

    @Override
    public boolean addBill(Map map) {
        boolean flag=false;
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        BillMapper mapper=sqlSession.getMapper(BillMapper.class);
        int count=0;
        try {
            count=mapper.addBill(map);
            if (count>0)
            {
                flag=true;
                sqlSession.commit();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            sqlSession.rollback();
        }
        return flag;
    }
}
