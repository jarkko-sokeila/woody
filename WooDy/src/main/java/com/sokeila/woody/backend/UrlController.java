package com.sokeila.woody.backend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UrlController {

    @RequestMapping(value = {"/", "/news", "/sports", "it", "entertainment", "cars", "motorbikes", "science", "lifestyle"})
    public String index() {
        return "index.html";
    }
}