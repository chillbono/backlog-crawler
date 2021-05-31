package com.backlog.crawler.controller;

import com.backlog.crawler.service.SchedulerIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 課題のスケジューラ監視を扱うコントローラクラス.
 *
 * @author honobono
 */
@Component
public class SchedulerController {

    /** SchedulerIssueService. */
    private final SchedulerIssueService schedulerIssueService;

    @Autowired
    public SchedulerController(SchedulerIssueService schedulerIssueService) {
        this.schedulerIssueService = schedulerIssueService;
    }

    /**
     * 課題監視タスクのスケジューラを起動.
     */
    @Scheduled(cron = "${scheduler.cron.issue}", zone = "${scheduler.cron.timezone}")
    public void exceIssue() {
        schedulerIssueService.exce();
    }
}
