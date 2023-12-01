package com.niko.wiki.service;

import com.niko.wiki.domain.Test;
import com.niko.wiki.mapper.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class TestService {

//    @Autowired
    @Resource
    private TestMapper testMapper;

    public List<Test> list(){
        return testMapper.list();
    }
}
