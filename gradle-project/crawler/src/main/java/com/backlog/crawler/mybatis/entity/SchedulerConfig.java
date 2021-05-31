package com.backlog.crawler.mybatis.entity;

import java.time.LocalDateTime;
import lombok.Data;

/**
 * Table: scheduler_config
 */
@Data
public class SchedulerConfig {
    /**
     * Column: scheduler_id
     * Remark: スケジューラID
     */
    private Long schedulerId;

    /**
     * Column: scheduler_type
     * Remark: スケジューラ種別
     */
    private String schedulerType;

    /**
     * Column: comparison_type
     * Remark: 比較種別
     */
    private String comparisonType;

    /**
     * Column: comment
     * Remark: コメント
     */
    private String comment;

    /**
     * Column: exceeded_days
     * Remark: 超過日数
     */
    private Integer exceededDays;

    /**
     * Column: activated
     * Remark: 有効化
     */
    private Boolean activated;

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