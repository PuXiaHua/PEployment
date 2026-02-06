package com.pu.service;

import com.pu.edto.ApplicationDTO;
import com.pu.epojo.Application;
import com.pu.epojo.PageResult;

import java.util.List;

public interface ApplyService {

    void addApply(Application application);

    void cancelApply(Long id);

    PageResult<ApplicationDTO> getApplyList();

    PageResult<ApplicationDTO> getCompanyApply();

    void rejectApply(Long id);

    void passApply(Long id);
}
