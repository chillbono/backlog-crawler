package com.backlog.crawler.backlog.dto;

import com.backlog.crawler.backlog.dto.issue.UserDto;
import com.backlog.crawler.constant.IssueStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * レスポンスで受け取った課題コメント情報を格納するDTO.
 *
 * @author honobono
 */
@Getter
@Setter
public class IssueCommentsResponseDto {

    /** 課題コメントID. */
    private String id;

    /** コメント内容. */
    private String content;

    /** 登録ユーザ. */
    private UserDto createdUser;

    /** 作成日時. */
    private LocalDateTime created;

    /** 更新日時. */
    private LocalDateTime updated;

    /** スケジューラID. */
    private Long schedulerId;

    /** 課題ID. */
    private Long issueId;

    /** 課題キー. */
    private String issueKey;

    /** 課題状態. */
    private IssueStatus status;

    /** 担当者 */
    private UserDto assigneeUser;
}
