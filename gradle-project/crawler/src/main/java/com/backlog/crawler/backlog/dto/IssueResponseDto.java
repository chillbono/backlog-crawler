package com.backlog.crawler.backlog.dto;

import com.backlog.crawler.backlog.dto.issue.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * レスポンスで受け取った課題情報を格納するDTO.
 *
 * @author honobono
 */
@Getter
@Setter
public class IssueResponseDto {

    /** 課題ID. */
    private Long id;

    /** プロジェクトID. */
    private Long projectId;

    /** 課題キー. */
    private String issueKey;

    /** キーID. */
    private Long keyId;

    /** 課題種別. */
    private IssueTypeDto issueType;

    /** 概要. */
    private String summary;

    /** 説明. */
    private String description;

    /** 決議. */
    private String resolutions;

    /** 優先順位. */
    private PriorityDto priority;

    /** ステータス. */
    private StatusDto status;

    /** 担当者. */
    private UserDto assignee;

    /** 開始日. */
    private LocalDateTime startDate;

    /** 期限日. */
    private LocalDateTime dueDate;

    /** 見積もり工数. */
    private String estimatedHours;

    /** 稼働時間. */
    private String actualHours;

    /** 親課題ID. */
    private String parentIssueId;

    /** 作成ユーザ. */
    private UserDto createdUser;

    /** 作成日時. */
    private LocalDateTime created;

    /** 更新ユーザ. */
    private UserDto updatedUser;

    /** 更新日時. */
    private LocalDateTime updated;
}
