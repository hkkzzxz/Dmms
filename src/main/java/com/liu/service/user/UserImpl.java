package com.liu.service.user;

import com.liu.dao.User.UserMapper;
import com.liu.pojo.User;
import com.liu.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserImpl implements UserService{
    public User login(Map map) throws Exception {
        SqlSession sqlSession=null;
        sqlSession=MybatisUtils.getSqlSession();
        UserMapper mapper=null;
        User user=null;
        mapper=sqlSession.getMapper(UserMapper.class);
        user=mapper.getLoginUser(map);
        sqlSession.close();
        return user;
    }

    @Override
    public boolean updatePwd(Map map) throws IOException {
        SqlSession  sqlSession=MybatisUtils.getSqlSession();
        UserMapper mapper=null;
        boolean flag=false;
        mapper=sqlSession.getMapper(UserMapper.class);
        if (mapper.updatePwd(map)>0)
        {
            flag=true;
        }
        sqlSession.commit();
        sqlSession.close();
        return flag;
    }

    @Override
    public int getUserCount(Map map) throws IOException {
       SqlSession sqlSession=MybatisUtils.getSqlSession();
       UserMapper mapper=null;
       int count=0;
       mapper=sqlSession.getMapper(UserMapper.class);
        count=mapper.getUserCount(map);
        sqlSession.close();
        return count;
    }

    @Override
    public List<User> getUserList(Map map) throws IOException {
        List<User> userList=null;
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        UserMapper mapper=null;
        mapper=sqlSession.getMapper(UserMapper.class);
        userList=mapper.getUserList(map);
        sqlSession.close();
        return userList;
    }

    @Override
    public boolean DelUser(Map map) throws IOException {
        boolean flag=false;
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        int tag=-1;
        tag=mapper.delUser(map);
        if (tag>0)
        {
            flag=true;
        }
        sqlSession.commit();
        sqlSession.close();
        return flag;
    }

    @Override
    public User selectUserCodeExist(Map map) throws IOException {
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        User user=null;
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        user=mapper.selectUserCodeExist(map);
        return user;
    }

    @Override
    public boolean add(Map map) throws IOException {
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        UserMapper userMapper=sqlSession.getMapper(UserMapper.class);
        boolean flag=false;
        if (userMapper.addUser(map)>0)
        {
            flag=true;
        }
        sqlSession.commit();
        sqlSession.close();
        return flag;
    }

    @Override
    public User selectUserById(Map map) throws IOException {
        User user=null;
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        user=mapper.selectUserById(map);
        sqlSession.close();
        System.out.println(user.getRoles());
        return user;
    }

    @Override
    public boolean updateUser(Map map) throws IOException {
        SqlSession sqlSession=MybatisUtils.getSqlSession();
        UserMapper mapper=sqlSession.getMapper(UserMapper.class);
        boolean flag=false;
        if (mapper.updateUser(map)>0)
        {
            flag=true;
        }
        sqlSession.commit();
        sqlSession.close();
        return flag;
    }

    @Test
    public void Test() throws IOException {
        String userName="";
        int id=3;
        HashMap map=new HashMap();
        map.put("userName",userName);
        map.put("id",id);
        UserImpl user=new UserImpl();
        System.out.println(user.getUserCount(map));
    }
    @Test
    public void Test2()throws IOException{
        String userName=null;
      int userRole=0;
        int currentPageNo=5;
        int pageSize=5;
        HashMap map=new HashMap();
        map.put("userName",userName);
        map.put("userRole",userRole);
        map.put("userStart",0);
        map.put("pageSize",5);
        map.put("orde",2);
        List<User> userList=null;
        UserImpl user=new UserImpl();
        userList=user.getUserList(map);
        for (User user1:userList)
        {
            System.out.println(user1.getUserName());
        }
    }
    @Test
    public void Test3() throws IOException {
      String UserCode="zhanghua";
      HashMap map=new HashMap();
      map.put("userCode",UserCode);
      UserImpl user=new UserImpl();
      User user1=user.selectUserCodeExist(map);
      System.out.println(user1);
    }
    @Test
    public void Test4()throws IOException
    {
        int id=17;
        HashMap map=new HashMap();
        map.put("userName","土");
        map.put("userRole",0);
        UserImpl user=new UserImpl();
        List<User> user1=user.getUserList(map);
        for (User user2:user1)
        {
            System.out.println(user2.getUserRoleName());
        }
    }
    @Test
    public void Test5() throws IOException {
        int id=17;
        String userName="hkkzzxz1";
        UserImpl user=new UserImpl();
         HashMap map=new HashMap();
         map.put("uid",id);
         map.put("userName",userName);
         user.updateUser(map);

    }
}
