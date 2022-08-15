package com.cyx.community.controller;

import com.cyx.community.cache.QuestionRateLimiter;
import com.cyx.community.dto.CommentCreateDTO;
import com.cyx.community.dto.CommentDTO;
import com.cyx.community.dto.ResultDTO;
import com.cyx.community.enums.CommentTypeEnum;
import com.cyx.community.exception.CustomizeErrorCode;
import com.cyx.community.mapper.CommentMapper;
import com.cyx.community.model.Comment;
import com.cyx.community.model.User;
import com.cyx.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionRateLimiter questionRateLimiter;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }

        if (user.getDisable() != null && user.getDisable() == 1) {
            return ResultDTO.errorOf(CustomizeErrorCode.USER_DISABLE);
        }

        if (commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        if (questionRateLimiter.reachLimit(user.getId())) {
            return ResultDTO.errorOf(CustomizeErrorCode.RATE_LIMIT);
        }
        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        commentService.insert(comment,user);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id")Long id){
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
