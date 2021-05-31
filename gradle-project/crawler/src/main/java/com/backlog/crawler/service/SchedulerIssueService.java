package com.backlog.crawler.service;

import com.backlog.crawler.backlog.component.BacklogApiAdvice;
import com.backlog.crawler.backlog.dto.IssueCommentsRequestDto;
import com.backlog.crawler.backlog.dto.IssueCommentsResponseDto;
import com.backlog.crawler.backlog.dto.IssueResponseDto;
import com.backlog.crawler.backlog.dto.issue.UserDto;
import com.backlog.crawler.dto.SchedulerConfigKeyDto;
import com.backlog.crawler.logic.IssueLogic;
import com.backlog.crawler.logic.SchedulerLogic;
import com.backlog.crawler.mybatis.entity.SchedulerConfig;
import org.apache.commons.collections4.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 課題のスケジューラ監視を扱うサービスクラス.
 *
 * @author honobono
 */
@Service
@Transactional
public class SchedulerIssueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchedulerIssueService.class);

    /** BacklogApiAdvice. */
    private final BacklogApiAdvice backlogApiAdvice;

    /** SchedulerLogic. */
    private final SchedulerLogic schedulerLogic;

    /** IssueLogic. */
    private final IssueLogic issueLogic;

    @Autowired
    public SchedulerIssueService(BacklogApiAdvice backlogApiAdvice, SchedulerLogic schedulerLogic, IssueLogic issueLogic) {
        this.backlogApiAdvice = backlogApiAdvice;
        this.schedulerLogic = schedulerLogic;
        this.issueLogic = issueLogic;
    }

    /**
     * スケジューラ監視処理を実行する.
     */
    public void exce() {
        // 開始時出力ログ
        LOGGER.info("課題の監視処理を開始します。");

        // スケジューラ設定を取得する
        Map<SchedulerConfigKeyDto, SchedulerConfig> schedulerMap = schedulerLogic.createMapOfSchedulerConfig();
        if (schedulerMap == null) {
            LOGGER.warn("スケジューラ設定が取得できなかったため、処理を終了します。");
            return;
        }

        // [BacklogAPI] 課題一覧を取得する
        List<IssueResponseDto> issueList = backlogApiAdvice.issueList();
        if (CollectionUtils.isEmpty(issueList)) {
            LOGGER.info("課題一覧を取得できなかったため、処理を終了します。");
            return;
        }

        // 課題の通知対象チェック
        List<IssueCommentsResponseDto> issueCommentsResponseList = this.checkIssue(schedulerMap, issueList);

        // 処理結果をDBに登録する
        this.insertSchedulerResult(issueCommentsResponseList);

        // 終了時出力ログ
        LOGGER.info("課題の監視処理を終了します。");
    }

    /**
     * 課題の通知対象チェックを実施し、処理結果を返却する.
     *
     * @param schedulerMap スケジューラ設定のHashMap
     * @param issueList 課題一覧
     * @return 課題コメント追加APIのレスポンス情報
     */
    private List<IssueCommentsResponseDto> checkIssue(Map<SchedulerConfigKeyDto, SchedulerConfig> schedulerMap,
                                                      List<IssueResponseDto> issueList){
        List<IssueCommentsResponseDto> issueCommentsResponseList = new ArrayList<>();
        for (IssueResponseDto issueResponseDto : issueList) {
            // 各種チェック処理を実施し、処理対象の場合は該当するスケジューラ設定を取得する
            SchedulerConfig schedulerConfig = issueLogic.checkIssue(schedulerMap, issueResponseDto);
            if (schedulerConfig == null) {
                continue;
            }

            // [BacklogAPI] 課題参加者一覧を取得する
            List<UserDto> issueParticipantsList = backlogApiAdvice.issueParticipantsList(issueResponseDto.getId());
            if (issueParticipantsList == null) {
                continue;
            }

            // 課題コメント追加APIのリクエスト情報を生成する
            IssueCommentsRequestDto issueCommentsRequestDto = issueLogic.createRequestDtoForIssueComments(schedulerConfig,
                    issueResponseDto, issueParticipantsList);

            // [BacklogAPI] 課題にコメントを追加する
            IssueCommentsResponseDto issueCommentsResponseDto = backlogApiAdvice.issueComments(issueCommentsRequestDto);
            if (issueCommentsResponseDto == null) {
                continue;
            }

            // 処理結果に課題情報を設定する
            issueCommentsResponseDto = issueLogic.createResult(schedulerConfig, issueResponseDto, issueCommentsRequestDto,
                    issueCommentsResponseDto);

            issueCommentsResponseList.add(issueCommentsResponseDto);
        }

        return issueCommentsResponseList;
    }

    /**
     * スケジューラ結果情報を登録する.
     *
     * @param issueCommentsResponseList 課題コメント追加APIのレスポンス情報
     */
    private void insertSchedulerResult(List<IssueCommentsResponseDto> issueCommentsResponseList){
        if (CollectionUtils.isEmpty(issueCommentsResponseList)) {
            LOGGER.info("[処理件数] 処理対象の課題はありませんでした。");
        } else {
            Integer countOfWaiting = 0;
            Integer countOfDoing = 0;
            Integer countOfDone = 0;
            for (IssueCommentsResponseDto issueCommentsResponseDto : issueCommentsResponseList) {
                switch (issueCommentsResponseDto.getStatus()) {
                    case WAITING:
                        countOfWaiting++;
                        break;
                    case DOING:
                        countOfDoing++;
                        break;
                    case DONE:
                        countOfDone++;
                        break;
                    default:
                        break;
                }
            }
            LOGGER.info("[処理件数] 未対応：{}件, 処理中：{}件, 処理済み：{}件, 合計：{}件",
                    countOfWaiting, countOfDoing, countOfDone, issueCommentsResponseList.size());

            // 処理結果をDBに登録する
            issueLogic.insertSchedulerResult(issueCommentsResponseList);
        }
    }
}
