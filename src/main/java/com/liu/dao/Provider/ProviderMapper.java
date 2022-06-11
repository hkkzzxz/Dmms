package com.liu.dao.Provider;

import com.liu.pojo.Provider;

import java.util.List;
import java.util.Map;

public interface ProviderMapper {
    public List<Provider> getProviderList(Map map);
    public int delProByid(Map map);
    public int updateProByid(Map map);
    public Provider getProviderByid(Map map);
    public int addPro(Map map);
}
