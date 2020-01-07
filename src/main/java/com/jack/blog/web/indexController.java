package com.jack.blog.web;

import com.jack.blog.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class indexController {

    //@GetMapping("/{id}/{name}")
    //public String index(@PathVariable Integer id,@PathVariable String name){
    @GetMapping("/")
    public String index() {
//        int i = 9/0;
//        String blog = null;
//        if(blog==null){
//            throw new NotFoundException("blog不存在");
//        }
        System.out.println("--------index---------");
        return "index";
    }

    @GetMapping("/blog")
    public String blog() {
        System.out.println("--------index---------");
        return "blog";
    }

}
