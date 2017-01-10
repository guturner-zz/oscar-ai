package com.guy.home.ai.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.guy.home.ai.wikipedia.WikipediaHelper;

@Controller
public class MainController {

    @RequestMapping("/")
    public String greeting() {
    	return "main/main";
    }

}