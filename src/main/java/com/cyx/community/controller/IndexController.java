package com.cyx.community.controller;

import com.cyx.community.cache.HotTagCache;
import com.cyx.community.dto.PaginationDTO;
import com.cyx.community.dto.QuestionDTO;
import com.cyx.community.mapper.QuestionMapper;
import com.cyx.community.mapper.UserMapper;
import com.cyx.community.model.Question;
import com.cyx.community.model.User;
import com.cyx.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private HotTagCache hotTagCache;

    @GetMapping ("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(name = "page",defaultValue = "1") Integer page,
                        @RequestParam(name = "size",defaultValue = "5") Integer size,
                        @RequestParam(name = "search",required = false) String search,
                        @RequestParam(name = "tag",required = false)String tag,
                        @RequestParam(name = "sort", required = false) String sort){

        PaginationDTO paginationDTO = questionService.list(search,tag,sort,page, size);
        List<String> tags = hotTagCache.getHots();
        model.addAttribute("pagination",paginationDTO);
        model.addAttribute("search",search);
        model.addAttribute("tag",tag);
        model.addAttribute("tags",tags);
        model.addAttribute("sort", sort);
        return "index";
    }


}
