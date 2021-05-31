package com.backlog.crawler.backlog.component;

import com.backlog.crawler.backlog.api.BacklogApi;
import com.backlog.crawler.backlog.api.BacklogApiSetting;
import com.backlog.crawler.backlog.dto.IssueCommentsRequestDto;
import com.backlog.crawler.backlog.dto.IssueCommentsResponseDto;
import com.backlog.crawler.backlog.dto.IssueResponseDto;
import com.backlog.crawler.backlog.dto.issue.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.*;

/**
 * BacklogAPIを扱う実装クラス.
 *
 * @author honobono
 */
@Component
public class BacklogApiComponent implements BacklogApi {

    /** RestTemplate. */
    private final RestTemplate restTemplate;

    /** BacklogApiSetting. */
    private final BacklogApiSetting backlogApiSetting;

    @Autowired
    public BacklogApiComponent(RestTemplate restTemplate, BacklogApiSetting backlogApiSetting) {
        this.restTemplate = restTemplate;
        this.backlogApiSetting = backlogApiSetting;
    }

    /**
     * 課題一覧を取得する.
     *
     * @return レスポンス情報
     */
    @Override
    public List<IssueResponseDto> getIssueList() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        UriComponentsBuilder builder = this.backlogApiSetting.createUri(BacklogApiSetting.PATH_GET_ISSUES)
                .queryParam("apiKey", this.backlogApiSetting.getApiKey());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // API実行
        HttpEntity<IssueResponseDto[]> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                IssueResponseDto[].class);

        return Arrays.asList(response.getBody());
    }

    /**
     * 課題参加者一覧を取得する.
     *
     * @param issueId 課題ID
     * @return レスポンス情報
     */
    @Override
    public List<UserDto> getIssueParticipantsList(Long issueId){
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        // URLパラメータ設定
        Map<String, String> params = new HashMap<String, String>();
        params.put("issueIdOrKey", issueId.toString());

        UriComponentsBuilder builder = this.backlogApiSetting.createUri(BacklogApiSetting.PATH_GET_ISSUES_PARTICIPANTS)
                .queryParam("apiKey", this.backlogApiSetting.getApiKey());
        HttpEntity<?> entity = new HttpEntity<>(headers);

        // API実行
        HttpEntity<UserDto[]> response = restTemplate.exchange(
                builder.buildAndExpand(params).toUri(),
                HttpMethod.GET,
                entity,
                UserDto[].class);

        return Arrays.asList(response.getBody());
    }

    /**
     * 課題コメントを追加する.
     *
     * @param issueCommentsRequestDto リクエスト情報
     * @return レスポンス情報
     */
    @Override
    public IssueCommentsResponseDto postIssueComments(IssueCommentsRequestDto issueCommentsRequestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        // URLパラメータ設定
        Map<String, String> params = new HashMap<String, String>();
        params.put("issueIdOrKey", issueCommentsRequestDto.getIssueId().toString());

        // BODYパラメータ設定
        MultiValueMap<String,String> bodyParams = new LinkedMultiValueMap<>();
        bodyParams.add("content", issueCommentsRequestDto.getContent());
        for (Long notifiedUserId : issueCommentsRequestDto.getNotifiedUserId()) {
            bodyParams.add("notifiedUserId[]", notifiedUserId.toString());
        }

        UriComponentsBuilder builder = this.backlogApiSetting.createUri(BacklogApiSetting.PATH_POST_ISSUES_COMMENTS)
                .queryParam("apiKey", this.backlogApiSetting.getApiKey());

        HttpEntity<?> entity = new HttpEntity<>(bodyParams, headers);

        // API実行
        HttpEntity<IssueCommentsResponseDto> response = restTemplate.exchange(
                builder.buildAndExpand(params).toUri(),
                HttpMethod.POST,
                entity,
                IssueCommentsResponseDto.class);

        return response.getBody();
    }
}
