package com.backlog.crawler.backlog.dto.issue;

import lombok.Getter;
import lombok.Setter;

/**
 * ステータスを格納するDTO.
 *
 * @author honobono
 */
@Getter
@Setter
public class StatusDto {

    /** ステータスID. */
    private Long id;

    /** プロジェクトID. */
    private Long projectId;

    /** ステータス名. */
    private String name;

    /** 色. */
    private String color;

    /** 表示順. */
    private Integer displayOrder;
}
