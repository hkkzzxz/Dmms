package com.liu.service.user;

import com.liu.pojo.User;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {
    public User login(Map map) throws Exception;
    public boolean updatePwd(Map map) throws IOException;
    public int getUserCount(Map map) throws IOException;
    public List<User> getUserList(Map map) throws  IOException;
    public boolean DelUser(Map map)throws IOException;
    public User selectUserCodeExist(Map map)throws IOException;
    public boolean add(Map map)throws IOException;
    public User selectUserById(Map map)throws IOException;
    public boolean updateUser(Map map)throws IOException;
}
