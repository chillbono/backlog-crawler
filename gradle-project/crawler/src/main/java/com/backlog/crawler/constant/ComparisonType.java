package com.backlog.crawler.constant;

import lombok.Getter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 比較種別を表す定数.
 *
 * @author honobono
 */
public enum ComparisonType {

    /** 過去. */
    PAST("past"),

    /** 現在. */
    PRESENT("present");

    private static final Map<String, ComparisonType> TYPE_MAP = Collections.unmodifiableMap(createMap());

    @Getter
    private final String value;

    private ComparisonType(String value) {
        this.value = value;
    }

    private static Map<String, ComparisonType> createMap() {
        ComparisonType[] types = ComparisonType.values();
        Map<String, ComparisonType> map = new HashMap<String, ComparisonType>(types.length);
        for (ComparisonType type : types) {
            map.put(type.getValue(), type);
        }
        return map;
    }

    /**
     * valueに該当するComparisonTypeオブジェクトを取得する.
     *
     * @param value 状態値
     * @return comparisonType
     */
    public static ComparisonType of(String value) {
        if (value == null) {
            return null;
        }
        ComparisonType comparisonType = TYPE_MAP.get(value);
        return comparisonType;
    }

    /**
     * 値を指定して定義が存在するかどうかを調べる.
     *
     * @param val カテゴリ値
     * @return 定義済み:true
     */
    public static boolean contains(String val) {
        return TYPE_MAP.containsKey(val);
    }
}
