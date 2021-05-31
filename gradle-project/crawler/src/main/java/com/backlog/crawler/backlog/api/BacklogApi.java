package com.backlog.crawler.backlog.api;

import com.backlog.crawler.backlog.dto.IssueCommentsRequestDto;
import com.backlog.crawler.backlog.dto.IssueCommentsResponseDto;
import com.backlog.crawler.backlog.dto.IssueResponseDto;
import com.backlog.crawler.backlog.dto.issue.UserDto;
import java.util.List;

/**
 * BacklogAPIを扱うインタフェース.
 *
 * @author honobono
 */
public interface BacklogApi {

    /**
     * 課題一覧を取得する.
     *
     * @return レスポンス情報
     */
    List<IssueResponseDto> getIssueList();

    /**
     * 課題参加者一覧を取得する.
     *
     * @param issueId 課題ID
     * @return レスポンス情報
     */
    List<UserDto> getIssueParticipantsList(Long issueId);

    /**
     * 課題コメントを追加する.
     *
     * @param issueCommentsRequestDto リクエスト情報
     * @return レスポンス情報
     */
    IssueCommentsResponseDto postIssueComments(IssueCommentsRequestDto issueCommentsRequestDto);
}
