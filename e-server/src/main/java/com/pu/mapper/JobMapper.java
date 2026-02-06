package com.pu.mapper;

import com.pu.epojo.Job;
import com.pu.epojo.JobQueryParam;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface JobMapper {

    List<Job> findAll(JobQueryParam jobQueryParam);

    @Select("select * from employment.job where id=#{id}")
    Job getJobById(Long id);

    @Insert("insert into job(company_id, name, description, location, salary_min, salary_max, status, create_time, update_time) values ()")
    void addJob(Job job);

    void deleteJobById(Long[] ids);

    void updateJob(Job job);

    @Select("select * from job where company_id=#{companyId}")
    List<Job> getMyPublishJob(Long companyId);
}
