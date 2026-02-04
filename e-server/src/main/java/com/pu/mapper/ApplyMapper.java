package com.pu.mapper;

import com.pu.edto.ApplicationDTO;
import com.pu.epojo.Application;
import com.pu.epojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ApplyMapper {

    @Insert("insert into employment.application(user_id, job_id, resume_snapshot, status, create_time, update_time) VALUES ()")
    void addApply(Application application);

    @Select("select * from application where user_id=#{userId} and job_id=#{jobId}")
    Application getApplyByUserAndJob(Long userId, Long jobId);

    @Update("update application set status=#{status},resume_snapshot=#{resumeSnapshot},update_time=#{updateTime} where id=#{id}")
    void updateStatus(Application exist);

    @Select("select a.id as applicationId,j.id AS jobId,j.name AS jobName,c.id AS companyId,c.name AS companyName,a.status AS applicationStatus,a.create_time AS applicationTime " +
            "FROM application a JOIN job j ON a.job_id = j.id JOIN company c ON j.company_id = c.id WHERE a.user_id = #{userId} ORDER BY a.create_time DESC")
    List<ApplicationDTO> getApplyByUserId(Long userId);

    @Select("select * from application where id=#{id}")
    Application getApplyById(Long id);
}
