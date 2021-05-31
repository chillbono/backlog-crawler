package com.backlog.crawler.backlog.component;

import com.backlog.crawler.backlog.api.BacklogApi;
import com.backlog.crawler.backlog.api.BacklogApiSetting;
import com.backlog.crawler.backlog.dto.ErrorResponseDto;
import com.backlog.crawler.backlog.dto.IssueCommentsRequestDto;
import com.backlog.crawler.backlog.dto.IssueCommentsResponseDto;
import com.backlog.crawler.backlog.dto.IssueResponseDto;
import com.backlog.crawler.backlog.dto.issue.UserDto;
import com.backlog.crawler.constant.BacklogErrorCode;
import com.backlog.crawler.exception.BacklogException;
import com.backlog.crawler.exception.CrawlerSystemException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.*;
import java.util.List;

/**
 * BacklogAPI実行前後の処理を扱うクラス.
 *
 * @author honobono
 */
@Component
public class BacklogApiAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(BacklogApiAdvice.class);

    /** BacklogApi. */
    private final BacklogApi backlogApi;

    @Autowired
    public BacklogApiAdvice(BacklogApi backlogApi) {
        this.backlogApi = backlogApi;
    }

    /**
     * 課題一覧を取得する.
     *
     * @return レスポンス情報
     */
    public List<IssueResponseDto> issueList() {
        // BacklogAPIより課題一覧を取得する
        return this.requestIssueList();
    }

    /**
     * [リクエスト実行] 課題一覧を取得する.
     *
     * @return レスポンス情報
     */
    private List<IssueResponseDto> requestIssueList() {
        LOGGER.info("[BacklogAPI] 課題一覧取得APIを実行します。");

        try {
            List<IssueResponseDto> list = backlogApi.getIssueList();
            LOGGER.info("[BacklogAPI] 課題一覧取得APIの実行が完了しました。");
            return list;
        } catch (RestClientResponseException exc) {
            // BacklogAPI実行時にレスポンス異常が発生した場合
            BacklogException backlogExc = this.handleExecution("課題一覧取得", BacklogApiSetting.PATH_GET_ISSUES, exc);
            throw backlogExc;
        } catch (ResourceAccessException exc) {
            // BacklogAPI実行時にI/O異常が発生した場合（タイムアウト等）
            LOGGER.warn("[BacklogAPI] IO異常が発生したため、処理を中断します。 detail = " + exc.getMessage());
            throw new BacklogException("Failed to " + BacklogApiSetting.PATH_GET_ISSUES + " for IO Error.", exc);
        } catch (RuntimeException exc) {
            // 想定外の異常が発生した場合
            LOGGER.error("[BacklogAPI] 想定外の異常が発生したため、処理を中断します。" + exc);
            throw exc;
        } catch (Throwable cause) {
            // 致命的な異常が発生した場合
            LOGGER.error("[BacklogAPI] 致命的な異常が発生したため、処理を中断します。" + cause);
            throw new CrawlerSystemException("Failed to " + BacklogApiSetting.PATH_GET_ISSUES + " for Fatal Error.", cause);
        }
    }

    /**
     * 課題参加者一覧を取得する.
     *
     * @param issueId 課題ID
     * @return レスポンス情報
     */
    public List<UserDto> issueParticipantsList(Long issueId) {
        try {
            // BacklogAPIより課題参加者一覧を取得する
            return this.requestIssueParticipantsList(issueId);
        } catch (BacklogException exc) {
            // エラーを検知した場合はnullを返却する
            return null;
        }
    }

    /**
     * [リクエスト実行] 課題参加者一覧を取得する.
     *
     * @param issueId 課題ID
     * @return レスポンス情報
     */
    private List<UserDto> requestIssueParticipantsList(Long issueId) {
        LOGGER.info("[BacklogAPI] 課題参加者一覧取得APIを実行します。 issueId = " + issueId);

        try {
            List<UserDto> list = backlogApi.getIssueParticipantsList(issueId);
            LOGGER.info("[BacklogAPI] 課題参加者一覧取得APIの実行が完了しました。 issueId = " + issueId);
            return list;
        } catch (RestClientResponseException exc) {
            // BacklogAPI実行時にレスポンス異常が発生した場合
            BacklogException backlogExc = this.handleExecution("課題参加者一覧取得", BacklogApiSetting.PATH_GET_ISSUES_PARTICIPANTS, exc);
            throw backlogExc;
        } catch (ResourceAccessException exc) {
            // BacklogAPI実行時にI/O異常が発生した場合（タイムアウト等）
            LOGGER.warn("[BacklogAPI] IO異常が発生したため、処理を中断します。 detail = " + exc.getMessage());
            throw new BacklogException("Failed to " + BacklogApiSetting.PATH_GET_ISSUES_PARTICIPANTS + " for IO Error.", exc);
        } catch (RuntimeException exc) {
            // 想定外の異常が発生した場合
            LOGGER.error("[BacklogAPI] 想定外の異常が発生したため、処理を中断します。" + exc);
            throw exc;
        } catch (Throwable cause) {
            // 致命的な異常が発生した場合
            LOGGER.error("[BacklogAPI] 致命的な異常が発生したため、処理を中断します。" + cause);
            throw new CrawlerSystemException("Failed to " + BacklogApiSetting.PATH_GET_ISSUES_PARTICIPANTS + " for Fatal Error.", cause);
        }
    }

    /**
     * 課題コメントを追加する.
     *
     * @param issueCommentsRequestDto リクエスト情報
     * @return レスポンス情報
     */
    public IssueCommentsResponseDto issueComments(IssueCommentsRequestDto issueCommentsRequestDto) {
        try {
            // BacklogAPIを実行し、課題コメントを追加する
            return this.requestIssueComments(issueCommentsRequestDto);
        } catch (BacklogException exc) {
            // エラーを検知した場合はnullを返却する
            return null;
        }
    }

    /**
     * [リクエスト実行] 課題コメントを追加する.
     *
     * @param issueCommentsRequestDto リクエスト情報
     * @return レスポンス情報
     */
    private IssueCommentsResponseDto requestIssueComments(IssueCommentsRequestDto issueCommentsRequestDto) {
        LOGGER.info("[BacklogAPI] 課題コメント追加APIを実行します。 issueId = " + issueCommentsRequestDto.getIssueId());

        try {
            IssueCommentsResponseDto dto = backlogApi.postIssueComments(issueCommentsRequestDto);
            LOGGER.info("[BacklogAPI] 課題コメント追加APIの実行が完了しました。 issueId = " + issueCommentsRequestDto.getIssueId());
            return dto;
        } catch (RestClientResponseException exc) {
            // BacklogAPI実行時にレスポンス異常が発生した場合
            BacklogException backlogExc = this.handleExecution("課題コメント追加", BacklogApiSetting.PATH_POST_ISSUES_COMMENTS, exc);
            throw backlogExc;
        } catch (ResourceAccessException exc) {
            // BacklogAPI実行時にI/O異常が発生した場合（タイムアウト等）
            LOGGER.warn("[BacklogAPI] IO異常が発生したため、処理を中断します。 detail = " + exc.getMessage());
            throw new BacklogException("Failed to " + BacklogApiSetting.PATH_POST_ISSUES_COMMENTS + " for IO Error.", exc);
        } catch (RuntimeException exc) {
            // 想定外の異常が発生した場合
            LOGGER.error("[BacklogAPI] 想定外の異常が発生したため、処理を中断します。" + exc);
            throw exc;
        } catch (Throwable cause) {
            // 致命的な異常が発生した場合
            LOGGER.error("[BacklogAPI] 致命的な異常が発生したため、処理を中断します。" + cause);
            throw new CrawlerSystemException("Failed to " + BacklogApiSetting.PATH_POST_ISSUES_COMMENTS + " for Fatal Error.", cause);
        }
    }

    /**
     * RestTemplateのエラーハンドリングを実施する.
     *
     * @param title API名
     * @param endpoint エンドポイント
     * @param exc RestClientResponseException
     * @return BacklogException
     */
    private BacklogException handleExecution(String title, String endpoint, RestClientResponseException exc) {
        String status = "";
        if ((exc instanceof HttpClientErrorException)) {
            // 4xx系のステータスコードが返却された場合
            HttpStatus httpStatus = ((HttpClientErrorException) exc).getStatusCode();
            status = httpStatus.toString();
        } else if ((exc instanceof HttpServerErrorException)) {
            // 5xx系のステータスコードが返却された場合
            status = ((HttpServerErrorException) exc).getStatusCode().toString();
        } else if ((exc instanceof HttpStatusCodeException)) {
            // 4xx系/5xx系以外のステータスコードが返却された場合
            status = ((HttpStatusCodeException) exc).getStatusCode().toString();
        } else if ((exc instanceof UnknownHttpStatusCodeException)) {
            // HttpStatusで未定義のステータスコードが返却された場合
            Integer rawStatus = ((UnknownHttpStatusCodeException) exc).getRawStatusCode();
            status = rawStatus.toString();
        }

        // エラーレスポンスのログ出力を実施する
        this.outputLogOfError(title, exc);

        return new BacklogException("Failed to " + endpoint + " for " + status + ".", exc);
    }

    /**
     * エラーレスポンスのログ出力を実施する.
     *
     * @param title API名
     * @param exc 例外
     */
    private void outputLogOfError(String title, RestClientResponseException exc) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ErrorResponseDto errorResponseDto = mapper.readValue(exc.getResponseBodyAsString(), ErrorResponseDto.class);

            // ログを出力
            for (ErrorResponseDto.Errors errors : errorResponseDto.getErrors()) {
                String code = errors.getCode().toString();
                String warnMsg = "[BacklogAPI] " + title + "にて異常が発生しました。" + BacklogErrorCode.getMessage(code)
                        + " errorName = " + BacklogErrorCode.getLabel(code) + ", detail = " + exc.getMessage();
                LOGGER.warn(warnMsg, exc);
            }
        } catch (JsonProcessingException jsonExc) {
            // 変換失敗時はログのみ出力
            String warnMsg = "[BacklogAPI] " + title + "にて異常が発生しました。" + " detail = " + exc.getMessage();
            LOGGER.warn(warnMsg, exc);
        }
    }
}
