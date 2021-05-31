package com.backlog.crawler.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * 日付操作ユーティリティ.
 *
 * @author honobono
 */
public final class DateUtil {

    /**
     * システム日時を返却する.
     *
     * @return システム日時
     */
    public static LocalDateTime getSysDate() {
        return LocalDateTime.now();
    }

    /**
     * システム日付を時分秒ミリ秒を0に設定した値を返却する.
     *
     * @return 時分秒ミリ秒を取り除いたシステム日時
     */
    public static LocalDateTime getTrimSysDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 00, 00, 00);
    }

    /**
     * 引数で指定された日時情報に対して、時分秒ミリ秒を0に設定した値を返却する.
     *
     * @param localDateTime 日時
     * @return 時分秒ミリ秒を取り除いた日時
     */
    public static LocalDateTime getTrimDate(LocalDateTime localDateTime) {
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 00, 00, 00);
    }

    /**
     * 引数で指定された日時情報に対して、指定された日数を加算し、時分秒ミリ秒を0に設定した値を返却する.
     *
     * @param localDateTime 日時
     * @param amount 加算日数
     * @return 日数加算後、時分秒ミリ秒を取り除いた日付
     */
    public static LocalDateTime getAddDate(LocalDateTime localDateTime, int amount) {
        localDateTime = localDateTime.plusDays(amount);
        return LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 00, 00, 00);
    }

    /**
     * 日付を指定書式の日付文字列に変換する.
     *
     * @param localDateTime 変換する日付
     * @param pattern 日付フォーマット
     * @return 変換した日付文字列
     * @see DateUtil.Format 日付書式
     */
    public static String format(LocalDateTime localDateTime, String pattern) {
        if (localDateTime == null) {
            return null;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dtf);
    }

    /**
     * 指定した2つの日付の差分日数を算出し、文字列で返却する.
     *
     * @param localDateTime1 日付
     * @param localDateTime2 日付
     * @return 差分日数
     */
    public static String getDiffDays(LocalDateTime localDateTime1, LocalDateTime localDateTime2) {
        long diffDays = ChronoUnit.DAYS.between(localDateTime2, localDateTime1);
        return Integer.valueOf(Math.toIntExact(diffDays)).toString();
    }

    /**
     * 日付書式.
     *
     * @author honobono.
     */
    public static class Format {

        /**日付書式 yyyy-MM-dd hh:mm:ss. */
        public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

        /**日付書式 yyyy/MM/dd. */
        public static final String YYYY_MM_DD = "yyyy/MM/dd";
    }
}