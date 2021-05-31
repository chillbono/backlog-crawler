package com.backlog.crawler.mybatis.mapper;

import com.backlog.crawler.mybatis.entity.SchedulerResult;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface SchedulerResultMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    @Delete({
        "delete from scheduler_result",
        "where scheduler_result_id = #{schedulerResultId,jdbcType=BIGINT}"
    })
    int deleteByPrimaryKey(Long schedulerResultId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    @Insert({
        "insert into scheduler_result (scheduler_result_id, scheduler_id, ",
        "issue_id, issue_key, ",
        "content, assignee_user_id, ",
        "assignee_user_name, created_user_id, ",
        "created_user_name, created_at, ",
        "updated_at)",
        "values (#{schedulerResultId,jdbcType=BIGINT}, #{schedulerId,jdbcType=BIGINT}, ",
        "#{issueId,jdbcType=BIGINT}, #{issueKey,jdbcType=VARCHAR}, ",
        "#{content,jdbcType=VARCHAR}, #{assigneeUserId,jdbcType=VARCHAR}, ",
        "#{assigneeUserName,jdbcType=VARCHAR}, #{createdUserId,jdbcType=VARCHAR}, ",
        "#{createdUserName,jdbcType=VARCHAR}, #{createdAt,jdbcType=TIMESTAMP}, ",
        "#{updatedAt,jdbcType=TIMESTAMP})"
    })
    int insert(SchedulerResult record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(SchedulerResult record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    @Select({
        "select",
        "scheduler_result_id, scheduler_id, issue_id, issue_key, content, assignee_user_id, ",
        "assignee_user_name, created_user_id, created_user_name, created_at, updated_at",
        "from scheduler_result",
        "where scheduler_result_id = #{schedulerResultId,jdbcType=BIGINT}"
    })
    @ResultMap("com.backlog.crawler.mybatis.mapper.SchedulerResultMapper.BaseResultMap")
    SchedulerResult selectByPrimaryKeyWithLock(Long schedulerResultId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    @Select({
        "select",
        "scheduler_result_id, scheduler_id, issue_id, issue_key, content, assignee_user_id, ",
        "assignee_user_name, created_user_id, created_user_name, created_at, updated_at",
        "from scheduler_result",
        "where scheduler_result_id = #{schedulerResultId,jdbcType=BIGINT}"
    })
    @ResultMap("com.backlog.crawler.mybatis.mapper.SchedulerResultMapper.BaseResultMap")
    SchedulerResult selectByPrimaryKey(Long schedulerResultId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(SchedulerResult record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    @Update({
        "update scheduler_result",
        "set scheduler_id = #{schedulerId,jdbcType=BIGINT},",
          "issue_id = #{issueId,jdbcType=BIGINT},",
          "issue_key = #{issueKey,jdbcType=VARCHAR},",
          "content = #{content,jdbcType=VARCHAR},",
          "assignee_user_id = #{assigneeUserId,jdbcType=VARCHAR},",
          "assignee_user_name = #{assigneeUserName,jdbcType=VARCHAR},",
          "created_user_id = #{createdUserId,jdbcType=VARCHAR},",
          "created_user_name = #{createdUserName,jdbcType=VARCHAR},",
          "created_at = #{createdAt,jdbcType=TIMESTAMP},",
          "updated_at = #{updatedAt,jdbcType=TIMESTAMP}",
        "where scheduler_result_id = #{schedulerResultId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(SchedulerResult record);

    /**
     * Bulk Insert.
     */
    int insertBulk(List<SchedulerResult> recordList);
}