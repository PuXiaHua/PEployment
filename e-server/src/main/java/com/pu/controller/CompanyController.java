package com.pu.controller;

import com.pu.epojo.Company;
import com.pu.epojo.Result;
import com.pu.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/{id}")
    public Result getCompanyInfo(@PathVariable Long id) {
        log.info("查询{}公司信息",id);
        return Result.success(companyService.getCompanyInfo(id));
    }

    @GetMapping
    public Result getAllCompany() {
        log.info("分页查询公司信息");
        return Result.success(companyService.getAllCompany());
    }

    @PostMapping
    public Result addCompanyInfo(@RequestBody Company company) {
        log.info("新增公司信息");
        companyService.addCompanyInfo(company);
        return Result.success();
    }

    @GetMapping("/user")
    public Result getCompanyInfoByUserId(){
        log.info("查看当前用户的公司信息");
        return Result.success(companyService.getCompanyInfoByUserId());
    }

    @PutMapping
    public Result updateCompanyInfo(@RequestBody Company company) {
        log.info("修改公司信息");
        companyService.updateCompanyInfo(company);
        return Result.success();
    }

    @DeleteMapping
    public Result deleteCompanyInfo(Long id) {
        log.info("注销公司信息");
        companyService.deleteCompanyInfo(id);
        return Result.success();
    }
}
