package com.pu.mapper;

import com.github.pagehelper.Page;
import com.pu.epojo.Company;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CompanyMapper {

    @Select("select * from employment.company where id=#{id}")
    Company getCompanyInfo(Long id);

    @Insert("insert into employment.company (user_id, name, description, address, status, create_time, update_time, company_email) VALUES (#{userId},#{name},#{description},#{address},#{status},#{createTime},#{updateTime},#{companyEmail})")
    void addCompanyInfo(Company company);

    @Update("update employment.company set name=#{name},description=#{description},address=#{address},update_time=#{updateTime},company_email=#{companyEmail} where id=#{id}")
    void updateCompanyInfo(Company company);

    @Delete("delete from employment.company where id=#{id}")
    void deleteCompanyInfo(Long id);

    @Select("select * from employment.company where user_id=#{userId}")
    Company getCompanyInfoByUserId(Long userId);

    @Select("select * from employment.company")
    List<Company> getAllCompany();
}
