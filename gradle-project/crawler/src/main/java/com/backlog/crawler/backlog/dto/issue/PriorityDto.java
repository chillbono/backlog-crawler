package com.backlog.crawler.backlog.dto.issue;

import lombok.Getter;
import lombok.Setter;

/**
 * 優先順位を格納するDTO.
 *
 * @author honobono
 */
@Getter
@Setter
public class PriorityDto {

    /** プライオリティID. */
    private Long id;

    /** プライオリティ名. */
    private String name;
}
