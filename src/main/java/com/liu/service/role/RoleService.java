package com.liu.service.role;

import com.liu.pojo.Role;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface RoleService {
    public List<Role> getRoleList() throws IOException, SQLException;
}
