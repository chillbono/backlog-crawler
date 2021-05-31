package com.backlog.crawler.backlog.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

/**
 * 課題コメント追加APIのリクエスト情報を格納するDTO.
 *
 * @author honobono
 */
@Getter
@Setter
public class IssueCommentsRequestDto {

    /** 課題ID. */
    private Long issueId;

    /** 課題キー. */
    private String issueKey;

    /** コメント内容. */
    private String content;

    /** 通知ユーザID. */
    private List<Long> notifiedUserId;
}
