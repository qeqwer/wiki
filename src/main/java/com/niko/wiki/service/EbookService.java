package com.niko.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.niko.wiki.domain.Ebook;
import com.niko.wiki.domain.EbookExample;
import com.niko.wiki.mapper.EbookMapper;
import com.niko.wiki.req.EbookReq;
import com.niko.wiki.resp.EbookResp;
import com.niko.wiki.resp.PageResp;
import com.niko.wiki.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Resource
    private EbookMapper ebookMapper;

    public PageResp<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if(!ObjectUtils.isEmpty(req.getName())){
            criteria.andNameLike( "%" + req.getName() + "%");
        }
        PageHelper.startPage(req.getPage(), req.getSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

//        List<EbookResp> respList = new ArrayList<>();
//        for(Ebook ebook : ebookList){
//            EbookResp ebookResp = new EbookResp();
//            BeanUtils.copyProperties(ebook, ebookResp);
//            respList.add(ebookResp);
//        }
        List<EbookResp> list = CopyUtil.copyList(ebookList,EbookResp.class);
        PageResp<EbookResp> pageResp = new PageResp();
        pageResp.setList(list);
        pageResp.setTotal(pageInfo.getTotal());
        return pageResp;
     }
}
