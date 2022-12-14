package com.cyx.community.controller;

import com.cyx.community.cache.TagCache;
import com.cyx.community.dto.QuestionDTO;
import com.cyx.community.mapper.QuestionMapper;
import com.cyx.community.mapper.UserMapper;
import com.cyx.community.model.Question;
import com.cyx.community.model.User;
import com.cyx.community.service.QuestionService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id")Long id,Model model
                       ){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "description", required = false) String description,
                            @RequestParam(value = "tag", required = false) String tag,
                            @RequestParam(value = "id", required = false) Long id,
                            HttpServletRequest request, Model model){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags",TagCache.get());

        if(title == null || title.equals("")){
            model.addAttribute("error","??????????????????");
            return "publish";
        }
        if(description == null || description.equals("")){
            model.addAttribute("error","??????????????????");
            return "publish";
        }
        if(tag == null || tag.equals("")){
            model.addAttribute("error","??????????????????");
            return "publish";
        }

        String filterInvalids = TagCache.filterInvalids(tag);
        if(StringUtils.isNotBlank(filterInvalids)){
            model.addAttribute("erroe","??????????????????"+filterInvalids);
            return "publish";
        }

          User user =(User) request.getSession().getAttribute("user");

        if(user == null){
            model.addAttribute("error","???????????????");
            return "publish";
        }

        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
