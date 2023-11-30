package com.niko.wiki.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class TestController{

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }
}
