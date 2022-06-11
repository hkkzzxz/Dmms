package com.liu.service.role;

import com.liu.dao.Role.RoleMapper;
import com.liu.pojo.Role;
import com.liu.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RoleImpI implements RoleService{
    public List<Role> getRoleList() throws IOException, SQLException {
        SqlSession sqlSession= MybatisUtils.getSqlSession();
        RoleMapper mapper=sqlSession.getMapper(RoleMapper.class);
        List<Role> roleList=null;
        roleList=mapper.getRoleList();
        sqlSession.close();
        return roleList;
    }
    @Test
    public void Test() throws SQLException, IOException {
        RoleImpI roleImpI=new RoleImpI();
        System.out.println(roleImpI.getRoleList());
    }
}
