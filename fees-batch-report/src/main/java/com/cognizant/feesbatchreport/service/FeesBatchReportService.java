package com.cognizant.feesbatchreport.service;

import com.cognizant.feesbatchreport.infrastructure.ReportWriter;
import com.cognizant.feesbatchreport.model.dtos.FeeEventDto;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class FeesBatchReportService {

    ReportWriter reportWriter;

    public FeesBatchReportService(ReportWriter reportWriter){
        this.reportWriter= reportWriter;
    }

    public void createReport(List<FeeEventDto> feeList) throws IOException {
        reportWriter.writeReport(feeList);
    }
}
