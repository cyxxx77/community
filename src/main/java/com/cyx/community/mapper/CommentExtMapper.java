package com.cyx.community.mapper;

import com.cyx.community.model.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}