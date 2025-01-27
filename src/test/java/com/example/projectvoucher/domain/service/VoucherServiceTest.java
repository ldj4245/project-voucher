package com.example.projectvoucher.domain.service;

import com.example.projectvoucher.common.type.VoucherAmountType;
import com.example.projectvoucher.common.type.VoucherStatusType;
import com.example.projectvoucher.storage.voucher.VoucherEntity;
import com.example.projectvoucher.storage.voucher.VoucherRepository;
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

        final String code = voucherService.publish(validFrom,validTo, VoucherAmountType.KRW_30000);


        //when
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();


        //then
        assertThat(voucherEntity.code()).isEqualTo(code);
        assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherEntity.validFrom()).isEqualTo(validFrom);
        assertThat(voucherEntity.validTo()).isEqualTo(validTo);

    }


    @DisplayName("발행된 상품권은 사용 불가 처리 할 수 있다.")
    @Test
    void testCancel(){
        //given
        final LocalDate validFrom = LocalDate.now();
        final LocalDate validTo = LocalDate.now().plusDays(30);
        final VoucherAmountType amount = VoucherAmountType.KRW_100000;

        final String code = voucherService.publish(validFrom,validTo,amount);

        //when
        voucherService.disable(code);
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();


        //then
        assertThat(voucherEntity.code()).isEqualTo(code);
        assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.DISABLE);
        assertThat(voucherEntity.validFrom()).isEqualTo(validFrom);
        assertThat(voucherEntity.validTo()).isEqualTo(validTo);
        assertThat(voucherEntity.updatedAt()).isNotEqualTo(voucherEntity.cratedAt());


    }


    @DisplayName("발행된 상품권은 사용할 수 있다.")
    @Test
    public void test3(){
        //given
        final LocalDate validForm = LocalDate.now();
        final LocalDate validTo = LocalDate.now().plusDays(30);
        final VoucherAmountType amount = VoucherAmountType.KRW_100000;

        final String code = voucherService.publish(validForm,validTo,amount);

        //when
        voucherService.use(code);
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();



        //then
        assertThat(voucherEntity.code()).isEqualTo(code);
        assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.USE);
        assertThat(voucherEntity.validFrom()).isEqualTo(validForm);
        assertThat(voucherEntity.validTo()).isEqualTo(validTo);
        assertThat(voucherEntity.amount()).isEqualTo(amount);
        assertThat(voucherEntity.updatedAt()).isEqualTo(voucherEntity.cratedAt());




    }
}