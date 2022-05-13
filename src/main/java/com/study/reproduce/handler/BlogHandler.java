package com.study.reproduce.handler;

import com.study.reproduce.model.domain.Blog;
import com.study.reproduce.service.BlogService;
import com.study.reproduce.service.CategoryService;
import com.study.reproduce.utils.PageQueryUtil;
import com.study.reproduce.utils.PageResult;
import com.study.reproduce.utils.Result;
import com.study.reproduce.utils.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class BlogHandler {
    @Resource
    BlogService blogService;
    @Resource
    CategoryService categoryService;

    @GetMapping("/blogs")
    public String blogs(Model model) {
        model.addAttribute("path", "/admin/blogs");
        return "admin/blog";
    }

    @GetMapping("/blogs/edit")
    public String blogEdit(Model model) {
        model.addAttribute("path", "admin/blogs/edit");
        model.addAttribute("categories", categoryService.list());
        return "admin/edit";
    }

    @GetMapping("/blogs/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, String> params) {
        if (params.get("page") == null || params.get("limit") == null) {
            return ResultGenerator.getFailureResult("参数错误");
        }
        Integer page = Integer.valueOf(params.get("page"));
        Integer limit = Integer.valueOf(params.get("limit"));
        PageQueryUtil queryUtil = new PageQueryUtil(page, limit);
        PageResult<Blog> pageResult = blogService.queryByPageUtil(queryUtil);
        return ResultGenerator.getSuccessResult(pageResult);
    }

    @GetMapping("/blogs/edit/{blogId}")
    public String edit(Model model, @PathVariable("blogId") Long blogId) {
        model.addAttribute("path", "edit");
        Blog blog = blogService.getById(blogId);
        if (blog == null) {
            return "error/error_400";
        }
        model.addAttribute("blog", blog);
        model.addAttribute("categories", categoryService.list());
        return "admin/edit";
    }

    @PostMapping("/blogs/save")
    @ResponseBody
    public Result save(Blog blog) {
        if (blog.getBlogTitle().isEmpty()) {
            return ResultGenerator.getFailureResult("标题不能为空");
        }
        if (blog.getBlogTitle().length() > 150) {
            return ResultGenerator.getFailureResult("标题过长");
        }
        if (blog.getBlogTags().isEmpty()) {
            return ResultGenerator.getFailureResult("标签不能为空");
        }
        if (blog.getBlogTags().length() > 150) {
            return ResultGenerator.getFailureResult("标签过长");
        }
        if (blog.getBlogSubUrl().length() > 150) {
            return ResultGenerator.getFailureResult("url路径过长");
        }
        if (blog.getBlogContent().isEmpty()) {
            return ResultGenerator.getFailureResult("请输入文章内容");
        }
        if (blog.getBlogContent().length() > 50000) {
            return ResultGenerator.getFailureResult("文章内容过长");
        }
        if (blog.getBlogCoverImage().isEmpty()) {
            return ResultGenerator.getFailureResult("封面不能为空");
        }
        String result = blogService.saveBlog(blog);
        if ("新增成功".equals(result)) {
            return ResultGenerator.getSuccessResult("新增成功");
        } else {
            return ResultGenerator.getFailureResult(result);
        }
    }
}
