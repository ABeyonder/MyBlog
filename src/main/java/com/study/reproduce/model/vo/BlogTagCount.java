package com.study.reproduce.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogTagCount {
    private Integer tagId;
    private String tagName;
    private Integer tagCount;
}
