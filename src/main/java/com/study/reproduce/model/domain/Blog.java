package com.study.reproduce.model.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName tb_blog
 */
@TableName(value ="tb_blog")
@Data
public class Blog implements Serializable {
    /**
     * 博客表主键id
     */
    @TableId(type = IdType.AUTO)
    private Long blogId;

    /**
     * 博客标题
     */
    private String blogTitle;

    /**
     * 博客自定义路径url
     */
    private String blogSubUrl;

    /**
     * 博客封面图
     */
    private String blogCoverImage;

    /**
     * 博客内容
     */
    private String blogContent;

    /**
     * 博客分类id
     */
    private Integer blogCategoryId;

    /**
     * 博客分类(冗余字段)
     */
    private String blogCategoryName;

    /**
     * 博客标签
     */
    private String blogTags;

    /**
     * 0-草稿 1-发布
     */
    private Integer blogStatus;

    /**
     * 阅读量
     */
    private Long blogViews;

    /**
     * 0-允许评论 1-不允许评论
     */
    private Integer enableComment;

    /**
     * 是否删除 0=否 1=是
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 添加时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}