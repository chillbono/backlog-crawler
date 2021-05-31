package com.backlog.crawler.mybatis.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Table: scheduler_result
 */
@Data
public class SchedulerResult {
    /**
     * Column: scheduler_result_id
     * Remark: スケジューラ結果ID
     */
    private Long schedulerResultId;

    /**
     * Column: scheduler_id
     * Remark: スケジューラID
     */
    private Long schedulerId;

    /**
     * Column: issue_id
     * Remark: 課題ID
     */
    private Long issueId;

    /**
     * Column: issue_key
     * Remark: 課題キー
     */
    private String issueKey;

    /**
     * Column: content
     * Remark: コメント内容
     */
    private String content;

    /**
     * Column: assignee_user_id
     * Remark: 担当者ユーザID
     */
    private String assigneeUserId;

    /**
     * Column: assignee_user_name
     * Remark: 担当者ユーザ名
     */
    private String assigneeUserName;

    /**
     * Column: created_user_id
     * Remark: 作成者ユーザID
     */
    private String createdUserId;

    /**
     * Column: created_user_name
     * Remark: 作成者ユーザ名
     */
    private String createdUserName;

    /**
     * Column: created_at
     * Remark: 作成日時
     */
    private LocalDateTime createdAt;

    /**
     * Column: updated_at
     * Remark: 更新日時
     */
    private LocalDateTime updatedAt;
}