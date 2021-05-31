package com.backlog.crawler.logic;

import com.backlog.crawler.backlog.dto.IssueCommentsRequestDto;
import com.backlog.crawler.backlog.dto.IssueCommentsResponseDto;
import com.backlog.crawler.backlog.dto.IssueResponseDto;
import com.backlog.crawler.backlog.dto.issue.UserDto;
import com.backlog.crawler.constant.ComparisonType;
import com.backlog.crawler.constant.IssueStatus;
import com.backlog.crawler.constant.SchedulerType;
import com.backlog.crawler.dto.SchedulerConfigKeyDto;
import com.backlog.crawler.exception.CrawlerSystemException;
import com.backlog.crawler.mybatis.entity.SchedulerConfig;
import com.backlog.crawler.mybatis.entity.SchedulerResult;
import com.backlog.crawler.mybatis.mapper.SchedulerResultMapper;
import com.backlog.crawler.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 課題関連の共通ロジック.
 *
 * @author honobono
 */
@Component
public class IssueLogic {

    private static final Logger LOGGER = LoggerFactory.getLogger(IssueLogic.class);

    /** SchedulerLogic. */
    private final SchedulerLogic schedulerLogic;

    /** SchedulerResultMapper. */
    private final SchedulerResultMapper schedulerResultMapper;

    @Autowired
    public IssueLogic(SchedulerLogic schedulerLogic, SchedulerResultMapper schedulerResultMapper) {
        this.schedulerLogic = schedulerLogic;
        this.schedulerResultMapper = schedulerResultMapper;
    }

    /**
     * 課題に対して、各種チェック処理を実施する.
     *
     * @param schedulerMap スケジューラ設定のHashMap
     * @param issueResponseDto 課題一覧取得APIのレスポンス情報
     * @return 処理対象の場合はSchedulerConfigを返却 / 処理対象外の場合はnullを返却
     */
    public SchedulerConfig checkIssue(Map<SchedulerConfigKeyDto, SchedulerConfig> schedulerMap, IssueResponseDto issueResponseDto){
        Assert.notNull(schedulerMap, "schedulerMap is null.");
        Assert.notNull(issueResponseDto, "issueResponseDto is null.");

        // 期限日の超過チェック
        SchedulerConfig confOfPast = schedulerLogic.getSchedulerConfig(schedulerMap,
                new SchedulerConfigKeyDto(SchedulerType.ISSUE.getValue(), ComparisonType.PAST.getValue()));
        if (confOfPast != null && this.isExceededDue(confOfPast, issueResponseDto)) {
            return confOfPast;
        }

        // 期限日チェック
        SchedulerConfig confOfPresent = schedulerLogic.getSchedulerConfig(schedulerMap,
                new SchedulerConfigKeyDto(SchedulerType.ISSUE.getValue(), ComparisonType.PRESENT.getValue()));
        if (confOfPresent != null && this.isDue(issueResponseDto)) {
            return confOfPresent;
        }

        return null;
    }

    /**
     * 期限日の超過チェックを実施する.
     *
     * @param schedulerConfig スケジューラ設定
     * @param issueResponseDto 課題一覧取得APIのレスポンス情報
     * @return 超過している場合：true / 超過していない場合：false
     */
    private Boolean isExceededDue(SchedulerConfig schedulerConfig, IssueResponseDto issueResponseDto){
        // 課題状態チェック
        if (this.isIssueStatus(issueResponseDto, IssueStatus.COMPLETED)) {
            return false;
        }

        // 期限日チェック
        if (!this.isExceededDueDate(schedulerConfig.getExceededDays(), issueResponseDto.getDueDate())) {
            return false;
        }

        return true;
    }

    /**
     * 期限日とシステム日付の一致チェックを実施する.
     *
     * @param issueResponseDto 課題一覧取得APIのレスポンス情報
     * @return 一致している場合：true / 一致していない場合：false
     */
    private Boolean isDue(IssueResponseDto issueResponseDto){
        // 課題状態チェック
        if (this.isIssueStatus(issueResponseDto, IssueStatus.COMPLETED)) {
            return false;
        }

        // 期限日チェック
        if (!this.isDueDate(issueResponseDto.getDueDate())) {
            return false;
        }

        return true;
    }

    /**
     * 取得した課題の課題状態に対して、指定した課題状態との一致チェックを実施する.
     *
     * @param issueResponseDto 課題一覧取得APIのレスポンス情報
     * @param status 課題状態
     * @return 一致する場合：true / 不一致の場合：false
     */
    private Boolean isIssueStatus(IssueResponseDto issueResponseDto, IssueStatus status){
        if (status != IssueStatus.of(issueResponseDto.getStatus().getName())) {
            return false;
        }

        return true;
    }

    /**
     * 指定日付に対して、超過日数のチェックを実施する.
     *
     * @param exceededDays 超過日数（スケジューラ設定値）
     * @param targetDate 日付
     * @return 超過している場合：true / 超過していない場合、期限日の設定がない場合：false
     */
    private Boolean isExceededDueDate(Integer exceededDays, LocalDateTime targetDate){
        Assert.notNull(exceededDays, "exceededDays is null.");

        // 日付が設定されていない場合、falseを返却
        if (targetDate == null) {
            return false;
        }

        // 日付に超過日数の値を加算した日付を算出する
        targetDate = DateUtil.getAddDate(targetDate, exceededDays);
        if (DateUtil.getTrimSysDate().compareTo(targetDate) < 0) {
            return false;
        }

        return true;
    }

    /**
     * 指定日付とシステム日付の一致チェックを実施する.
     *
     * @param targetDate 日付
     * @return 一致している場合：true / 一致していない場合、日付の設定がない場合：false
     */
    private Boolean isDueDate(LocalDateTime targetDate){
        // 期限日が設定されていない場合、falseを返却
        if (targetDate == null) {
            return false;
        }

        if (DateUtil.getTrimSysDate().compareTo(DateUtil.getTrimDate(targetDate)) != 0) {
            return false;
        }

        return true;
    }

    /**
     * 課題コメント追加APIのリクエスト情報を生成する.
     *
     * @param schedulerConfig スケジューラ設定
     * @param issueResponseDto 課題一覧取得APIのレスポンス情報
     * @param issueParticipantsList 課題参加者一覧取得APIのレスポンス情報
     * @return 課題コメント追加APIのリクエスト情報
     */
    public IssueCommentsRequestDto createRequestDtoForIssueComments(SchedulerConfig schedulerConfig,
                                                                    IssueResponseDto issueResponseDto,
                                                                    List<UserDto> issueParticipantsList){
        Assert.notNull(schedulerConfig, "schedulerConfig is null.");
        Assert.notNull(issueResponseDto, "issueResponseDto is null.");
        Assert.notNull(issueParticipantsList, "issueParticipantsList is null.");

        IssueCommentsRequestDto issueCommentsRequestDto = new IssueCommentsRequestDto();

        // 課題ID
        issueCommentsRequestDto.setIssueId(issueResponseDto.getId());

        // 課題キー
        issueCommentsRequestDto.setIssueKey(issueResponseDto.getIssueKey());

        // コメント内容（コメントテンプレートに対して文字列の置換処理を実施）
        String content = this.replaceContent(issueResponseDto, schedulerConfig.getComment());
        issueCommentsRequestDto.setContent(content);

        // 通知ユーザ（通知対象：作成者 / 担当者 / 課題参加者）
        List<Long> NotifiedList = new ArrayList<>();
        NotifiedList.add(issueResponseDto.getCreatedUser().getId());
        if (issueResponseDto.getAssignee() != null) {
            NotifiedList.add(issueResponseDto.getAssignee().getId());
        }
        for (UserDto userDto : issueParticipantsList) {
            NotifiedList.add(userDto.getId());
        }
        issueCommentsRequestDto.setNotifiedUserId(NotifiedList);

        return issueCommentsRequestDto;
    }

    /**
     * スケジューラ設定のコメント内容に対して、置換処理を実施する.
     *
     * @param issueResponseDto 課題一覧取得APIのレスポンス情報
     * @param content コメント内容（置換前）
     * @return コメント内容（置換後）
     */
    private String replaceContent(IssueResponseDto issueResponseDto, String content){
        // 担当者
        if (content.contains("{ASSIGNEE_USER_NAME}")) {
            if (issueResponseDto.getAssignee() != null) {
                content = content.replace("{ASSIGNEE_USER_NAME}", issueResponseDto.getAssignee().getName());
            } else {
                content = content.replace("{ASSIGNEE_USER_NAME}", "（指定なし）");
            }
        }

        // 期限日
        if (content.contains("{DUE_DATE}")) {
            content = content.replace("{DUE_DATE}", DateUtil.format(issueResponseDto.getDueDate(),
                    DateUtil.Format.YYYY_MM_DD));
        }

        // 超過日数
        if (content.contains("{EXCEEDED_DAYS}")) {
            content = content.replace("{EXCEEDED_DAYS}", DateUtil.getDiffDays(DateUtil.getTrimSysDate(),
                    DateUtil.getTrimDate(issueResponseDto.getDueDate())));
        }

        return content;
    }

    /**
     * 課題コメント追加APIのレスポンス情報に課題情報を設定する.
     *
     * @param schedulerConfig スケジューラ設定
     * @param issueResponseDto 課題一覧取得APIのレスポンス情報
     * @param issueCommentsRequestDto 課題コメント追加APIのリクエスト情報
     * @param issueCommentsResponseDto 課題コメント追加APIのレスポンス情報
     * @return 課題コメント追加APIのレスポンス情報
     */
    public IssueCommentsResponseDto createResult(SchedulerConfig schedulerConfig, IssueResponseDto issueResponseDto,
                                                  IssueCommentsRequestDto issueCommentsRequestDto,
                                                 IssueCommentsResponseDto issueCommentsResponseDto){
        // スケジューラID
        issueCommentsResponseDto.setSchedulerId(schedulerConfig.getSchedulerId());
        // 課題ID
        issueCommentsResponseDto.setIssueId(issueCommentsRequestDto.getIssueId());
        // 課題キー
        issueCommentsResponseDto.setIssueKey(issueCommentsRequestDto.getIssueKey());
        // 課題状態
        issueCommentsResponseDto.setStatus(IssueStatus.of(issueResponseDto.getStatus().getName()));
        // 担当者
        issueCommentsResponseDto.setAssigneeUser(issueResponseDto.getAssignee());

        return issueCommentsResponseDto;
    }

    /**
     * スケジューラ結果情報を登録する.
     *
     * @param issueCommentsResponseList 課題コメント追加APIのレスポンス情報
     */
    public void insertSchedulerResult(List<IssueCommentsResponseDto> issueCommentsResponseList){
        Assert.notNull(issueCommentsResponseList, "issueCommentsResponseList is null.");

        List<SchedulerResult> schedulerResultList = new ArrayList<>();
        for (IssueCommentsResponseDto issueCommentsResponseDto : issueCommentsResponseList) {
            SchedulerResult schedulerResult = new SchedulerResult();
            schedulerResult.setSchedulerId(issueCommentsResponseDto.getSchedulerId());
            schedulerResult.setIssueId(issueCommentsResponseDto.getIssueId());
            schedulerResult.setIssueKey(issueCommentsResponseDto.getIssueKey());
            schedulerResult.setContent(issueCommentsResponseDto.getContent());
            if (issueCommentsResponseDto.getAssigneeUser() != null) {
                schedulerResult.setAssigneeUserId(issueCommentsResponseDto.getAssigneeUser().getUserId());
                schedulerResult.setAssigneeUserName(issueCommentsResponseDto.getAssigneeUser().getName());
            } else {
                schedulerResult.setAssigneeUserId(null);
                schedulerResult.setAssigneeUserName(null);
            }
            schedulerResult.setCreatedUserId(issueCommentsResponseDto.getCreatedUser().getUserId());
            schedulerResult.setCreatedUserName(issueCommentsResponseDto.getCreatedUser().getName());
            schedulerResult.setCreatedAt(DateUtil.getSysDate());
            schedulerResult.setUpdatedAt(DateUtil.getSysDate());
            schedulerResultList.add(schedulerResult);
        }

        Integer count = schedulerResultMapper.insertBulk(schedulerResultList);
        if (count <= 0) {
            LOGGER.error("スケジューラ結果の登録に失敗しました。");
            throw new CrawlerSystemException("insert error.");
        }

        LOGGER.debug("スケジューラ結果の登録が完了しました。");
    }
}
