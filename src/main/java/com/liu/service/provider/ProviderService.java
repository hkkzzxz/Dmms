package com.liu.service.provider;

import com.liu.pojo.Provider;
import com.liu.pojo.Role;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ProviderService {
    public List<Provider> getProviderList(Map map) throws IOException, SQLException;
    public int delProvider(Map map)throws IOException;
    public boolean updateProvider(Map map)throws IOException;
    public Provider selectProvider(Map map)throws IOException;
    public boolean addPro(Map map)throws IOException;
}
