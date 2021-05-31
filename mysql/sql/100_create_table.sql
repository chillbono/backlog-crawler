/**
 * Create Tables
 */

-- スケジューラ設定
DROP TABLE IF EXISTS `scheduler_config`;
CREATE TABLE `scheduler_config` (
  `scheduler_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'スケジューラID',
  `scheduler_type` varchar(16) NOT NULL COMMENT 'スケジューラ種別',
  `comparison_type` varchar(16) NOT NULL COMMENT '比較種別',
  `comment` varchar(1000) NOT NULL COMMENT 'コメント',
  `exceeded_days` int(3) unsigned COMMENT '超過日数',
  `activated` bit(1) NOT NULL DEFAULT b'0' COMMENT '有効化',
  `created_at` datetime NOT NULL COMMENT '作成日時',
  `updated_at` datetime NOT NULL COMMENT '更新日時',
  PRIMARY KEY (`scheduler_id`),
  UNIQUE KEY `scheduler_config_UN` (`scheduler_type`,`comparison_type`)
) COMMENT = 'スケジューラ設定';

-- スケジューラ結果
DROP TABLE IF EXISTS `scheduler_result`;
CREATE TABLE `scheduler_result` (
  `scheduler_result_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'スケジューラ結果ID',
  `scheduler_id` bigint(20) unsigned NOT NULL COMMENT 'スケジューラID',
  `issue_id` bigint(20) unsigned NOT NULL COMMENT '課題ID',
  `issue_key` varchar(100) NOT NULL COMMENT '課題キー',
  `content` varchar(1000) COMMENT 'コメント内容',
  `assignee_user_id` varchar(100) COMMENT '担当者ユーザID',
  `assignee_user_name` varchar(100) COMMENT '担当者ユーザ名',
  `created_user_id` varchar(100) NOT NULL COMMENT '作成者ユーザID',
  `created_user_name` varchar(100) NOT NULL COMMENT '作成者ユーザ名',
  `created_at` datetime NOT NULL COMMENT '作成日時',
  `updated_at` datetime NOT NULL COMMENT '更新日時',
  PRIMARY KEY (`scheduler_result_id`)
) COMMENT = 'スケジューラ結果';