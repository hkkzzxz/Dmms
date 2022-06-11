package com.liu.dao.Role;

import com.liu.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface RoleMapper {
    public List<Role> getRoleList() throws SQLException;
}
