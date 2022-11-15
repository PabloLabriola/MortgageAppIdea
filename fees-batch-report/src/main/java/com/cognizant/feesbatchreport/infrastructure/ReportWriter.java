package com.cognizant.feesbatchreport.infrastructure;

import com.cognizant.feesbatchreport.model.dtos.FeeEventDto;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@Component
public class ReportWriter {

    public void writeReport(List<FeeEventDto> dtoList) throws IOException {

        FileWriter fileWriter = new FileWriter("fees"+LocalDate.now()+".csv");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        dtoList.forEach(dto -> {
            printWriter.printf("%s,%s,%s,%s", dto.getCompany(), dto.getMortgageId().toString(), dto.getFeeCalculation().toString(), dto.getTotalAmount().toString());
        });

        printWriter.close();
    }
}
