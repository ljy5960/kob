package com.kob.backend.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/pk/")
public class IndexController {
    @RequestMapping("index.html")
    public String index(){return "index.html";}
}
