package com.cyx.community.service;

import com.cyx.community.mapper.NavMapper;
import com.cyx.community.model.Nav;
import com.cyx.community.model.NavExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NavService {
    @Autowired
    private NavMapper navMapper;

    public List<Nav> list(){
        NavExample navExample = new NavExample();
        navExample.createCriteria().andStatusEqualTo(1);
        navExample.setOrderByClause("priority desc");
        List<Nav> list = navMapper.selectByExample(navExample);
        return list;
    }
}
