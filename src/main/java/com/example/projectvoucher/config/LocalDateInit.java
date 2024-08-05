package com.example.projectvoucher.config;


import com.example.projectvoucher.storage.voucher.ContractEntity;
import com.example.projectvoucher.storage.voucher.ContractRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class LocalDateInit {
    private final ContractRepository contractRepository;


    public LocalDateInit(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @PostConstruct
    public void init(){
        contractRepository.save(new ContractEntity("CT0001", LocalDate.now().minusDays(7),LocalDate.now().plusDays(7),366*5));

    }
}
