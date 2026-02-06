package com.pu.service;

import com.pu.epojo.Company;
import com.pu.epojo.PageResult;

public interface CompanyService {
    Company getCompanyInfo(Long id);

    void addCompanyInfo(Company company);

    void updateCompanyInfo(Company company);

    void deleteCompanyInfo(Long id);

    Company getCompanyInfoByUserId();

    PageResult<Company> getAllCompany();
}
