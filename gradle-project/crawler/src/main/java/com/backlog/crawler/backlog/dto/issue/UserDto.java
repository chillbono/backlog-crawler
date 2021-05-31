package com.backlog.crawler.backlog.dto.issue;

import lombok.Getter;
import lombok.Setter;

/**
 * ユーザ情報を格納するDTO.
 *
 * @author honobono
 */
@Getter
@Setter
public class UserDto {

    /** ID. */
    private Long id;

    /** ユーザID. */
    private String userId;

    /** ユーザ名. */
    private String name;

    /** 権限種別. */
    private Integer roleType;

    /** 言語. */
    private String lang;

    /** メールアドレス. */
    private String mailAddress;
}
