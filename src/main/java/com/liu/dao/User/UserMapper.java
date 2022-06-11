package com.liu.dao.User;

import com.liu.pojo.User;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface UserMapper {
   public User getLoginUser(Map map)throws IOException;
   public int updatePwd(Map map)throws IOException;
   public Integer getUserCount(Map map)throws IOException;
   public List<User> getUserList(Map map)throws IOException;
   public int delUser(Map map)throws IOException;
   public User selectUserCodeExist(Map map)throws IOException;
   public int addUser(Map map)throws IOException;
   public User selectUserById(Map map)throws IOException;
   public int updateUser(Map map)throws IOException;
}
