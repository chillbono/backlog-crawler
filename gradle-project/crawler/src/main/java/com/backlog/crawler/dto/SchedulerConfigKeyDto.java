package com.backlog.crawler.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Objects;

/**
 * スケジューラ設定のキー情報を格納するDTO.
 *
 * @author honobono
 */
@Getter
@Setter
public class SchedulerConfigKeyDto {

    /** スケジューラ種別. */
    private String schedulerType;

    /** 比較種別. */
    private String comparisonType;

    public SchedulerConfigKeyDto(String schedulerType, String comparisonType) {
        this.schedulerType = schedulerType;
        this.comparisonType = comparisonType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchedulerConfigKeyDto that = (SchedulerConfigKeyDto) o;
        return schedulerType.equals(that.schedulerType) &&
                comparisonType.equals(that.comparisonType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schedulerType, comparisonType);
    }
}
