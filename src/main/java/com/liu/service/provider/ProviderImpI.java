package com.liu.service.provider;

import com.liu.dao.Bill.BillMapper;
import com.liu.dao.Provider.ProviderMapper;
import com.liu.pojo.Provider;
import com.liu.service.Bill.BillImpl;
import com.liu.service.Bill.BillService;
import com.liu.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProviderImpI implements ProviderService{

    @Override
    public List<Provider> getProviderList(Map map) throws IOException, SQLException {
        SqlSession sqlSession= MybatisUtils.getSqlSession();
        ProviderMapper mapper=sqlSession.getMapper(ProviderMapper.class);
        List<Provider>providerList=null;
        providerList=mapper.getProviderList(map);
        sqlSession.close();
        return providerList;
    }

    @Override
    public int delProvider(Map map) throws IOException {
       int billcount=-1;
        SqlSession sqlSession2=MybatisUtils.getSqlSession();
        BillService billService=new BillImpl();
        ProviderMapper mapper1=sqlSession2.getMapper(ProviderMapper.class);
        try {
            billcount=billService.getBillBypid(map);
            if (billcount==0)
            {
                System.out.println("找到了");
                mapper1.delProByid(map);
            }
            sqlSession2.commit();
        }catch (Exception e)
        {
            billcount=-1;
           e.printStackTrace();
            sqlSession2.rollback();
        }
       sqlSession2.close();
        return billcount;
    }

    @Override
    public boolean updateProvider(Map map) throws IOException {
        boolean flag=false;
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        ProviderMapper mapper=sqlSession.getMapper(ProviderMapper.class);
        int temp=0;
        try {
            temp = mapper.updateProByid(map);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        if (temp>0)
        {
            flag=true;
            sqlSession.commit();
        }
        System.out.println(temp);
        sqlSession.close();
        return flag;
    }

    @Override
    public Provider selectProvider(Map map) throws IOException {
        Provider provider=null;
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        ProviderMapper mapper=sqlSession.getMapper(ProviderMapper.class);
        provider=mapper.getProviderByid(map);
        sqlSession.close();
        return provider;
    }

    @Override
    public boolean addPro(Map map) throws IOException {
        boolean flag=false;
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        ProviderMapper mapper=sqlSession.getMapper(ProviderMapper.class);
        int count=-1;
        count=mapper.addPro(map);
        if (count>0) {
            sqlSession.commit();
            flag=true;
        }
        sqlSession.close();
        return flag;
    }

    @Test
    public void Test() throws IOException {
        HashMap map=new HashMap();
        map.put("pid",10);
        map.put("proContact","刘土豆");
        ProviderService providerService=new ProviderImpI();
        providerService.updateProvider(map);
    }
}
