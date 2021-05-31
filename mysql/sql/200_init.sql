/**
 * Insert Data
 */

-- スケジューラ設定
INSERT INTO crawler_db.scheduler_config (scheduler_id,scheduler_type,comparison_type,comment,exceeded_days,activated,created_at,updated_at) VALUES
	 (1,'issue','past','**【Backlog Crawler】期限日を超過しています。**

期限日を超過していますが、課題状態が「完了」になっておりません。
課題状態を更新するか、期限日の再設定を行ってください。

```
担当者：{ASSIGNEE_USER_NAME}
期限日：{DUE_DATE}（超過日数：{EXCEEDED_DAYS}日）
```

※ 本メッセージは Backlog Crawler より送信しています。',1,1,NOW(),NOW()),
	 (2,'issue','present','**【Backlog Crawler】本日が期限日です。**

本日が期限日です。
完了後は、課題状態を「完了」に更新することをお忘れなく。

```
担当者：{ASSIGNEE_USER_NAME}
期限日：{DUE_DATE}
```

※ 本メッセージは Backlog Crawler より送信しています。',NULL,1,NOW(),NOW());