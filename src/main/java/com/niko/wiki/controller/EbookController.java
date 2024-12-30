package com.niko.wiki.controller;


import com.niko.wiki.req.EbookReq;
import com.niko.wiki.resp.CommonResp;
import com.niko.wiki.resp.EbookResp;
import com.niko.wiki.resp.PageResp;
import com.niko.wiki.service.EbookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class EbookController {

    @Resource
    private EbookService ebookService;

    @GetMapping("/ebook/list")
    public CommonResp list(EbookReq req){
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }
}
