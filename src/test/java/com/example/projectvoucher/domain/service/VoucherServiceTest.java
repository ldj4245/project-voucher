package com.example.projectvoucher.domain.service;

import com.example.projectvoucher.common.type.VoucherStatusType;
import com.example.projectvoucher.stoarage.voucher.VoucherEntity;
import com.example.projectvoucher.stoarage.voucher.VoucherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VoucherServiceTest {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherRepository voucherRepository;


    @DisplayName("발행된 상품권은 code로 조회할 수 있어야 한다.")
    @Test
    void test(){

        //given
        final LocalDate validFrom = LocalDate.now();
        final LocalDate validTo = LocalDate.now().plusDays(30);
        final Long amount = 10000L;

        final String code = voucherService.publish(validFrom,validTo,amount);


        //when
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();


        //then
        assertThat(voucherEntity.code()).isEqualTo(code);
        assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherEntity.validFrom()).isEqualTo(validFrom);
        assertThat(voucherEntity.validTo()).isEqualTo(validTo);





    }
}