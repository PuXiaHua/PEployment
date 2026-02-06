package com.pu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pu.context.UserContext;
import com.pu.epojo.Company;
import com.pu.epojo.PageResult;
import com.pu.exception.BizException;
import com.pu.mapper.CompanyMapper;
import com.pu.service.CompanyService;
import com.pu.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyMapper companyMapper;

    @Override
    public Company getCompanyInfo(Long id) {
        return companyMapper.getCompanyInfo(id);
    }

    @Override
    public void addCompanyInfo(Company company) {
        AuthUtils.requireCompany();
        Long userId = UserContext.getUser().getId();
        Company exist = companyMapper.getCompanyInfoByUserId(userId);
        if (exist != null) {
            throw new BizException("当前用户已存在公司");
        }
        company.setStatus(1);
        company.setCreateTime(LocalDateTime.now());
        company.setUpdateTime(LocalDateTime.now());
        company.setUserId(UserContext.getUser().getId());
        companyMapper.addCompanyInfo(company);
    }

    @Override
    public void updateCompanyInfo(Company company) {
        AuthUtils.requireCompany();
        Company dbCompany = companyMapper.getCompanyInfo(company.getId());
        if (dbCompany == null) {
            throw new BizException("公司不存在");
        }

        Long currentUserId = UserContext.getUser().getId();
        if (!dbCompany.getUserId().equals(currentUserId)) {
            throw new BizException("无权修改该公司信息");
        }
        company.setUpdateTime(LocalDateTime.now());
        companyMapper.updateCompanyInfo(company);
    }

    @Override
    public void deleteCompanyInfo(Long id) {
        AuthUtils.requireCompany();
        //所有权校验 只能删除当前用户自己的
        Company dbCompany = companyMapper.getCompanyInfo(id);
        if (dbCompany == null) {
            throw new BizException("公司不存在");
        }
        Long currentUserId = UserContext.getUser().getId();
        if (!dbCompany.getUserId().equals(currentUserId)) {
            throw new BizException("无权删除该公司");
        }
        companyMapper.deleteCompanyInfo(id);
    }

    @Override
    public Company getCompanyInfoByUserId() {
        AuthUtils.requireCompany();
        return companyMapper.getCompanyInfoByUserId(UserContext.getUser().getId());
    }

    @Override
    public PageResult<Company> getAllCompany() {
        PageHelper.startPage(1, 10);
        Page<Company> page = (Page<Company>) companyMapper.getAllCompany();
        return new PageResult<Company>(page.getTotal(), page.getResult());
    }
}
