package com.backlog.crawler.backlog.dto.issue;

import lombok.Getter;
import lombok.Setter;

/**
 * 課題種別を格納するDTO.
 *
 * @author honobono
 */
@Getter
@Setter
public class IssueTypeDto {

    /** 種別ID. */
    private Long id;

    /** プロジェクトID. */
    private Long projectId;

    /** 種別名. */
    private String name;

    /** 色. */
    private String color;

    /** 表示順. */
    private Integer displayOrder;
}
