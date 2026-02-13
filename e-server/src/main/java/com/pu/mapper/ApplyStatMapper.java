package com.pu.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ApplyStatMapper {
    @Update("update job_apply_stat set apply_count = apply_count + 1, update_time = now() where job_id = #{jobId} ")
    int increaseApplyCount(Long jobId);

    @Update("update job_apply_stat set pass_count = pass_count + 1, update_time = now() where job_id = #{jobId}")
    void increasePassCount(Long jobId);

    @Update("update job_apply_stat set reject_count = reject_count + 1, update_time = now() where job_id = #{jobId}")
    void increaseRejectCount(Long jobId);

    @Update("update job_apply_stat set apply_count = apply_count - 1, update_time = now(),cancel_count=cancel_count+1 where job_id = #{jobId}")
    void increaseCancelCount(Long jobId);

    @Insert("insert into job_apply_stat(job_id, apply_count, pass_count, reject_count, cancel_count, update_time) VALUES (#{jobId},0,0,0,0,now())")
    void insertIfNotExist(Long jobId);

    @Insert("insert into mq_consume_record(message_id, consume_time) VALUES (#{messageId},now())")
    void addNewMessage(String messageId);
}

