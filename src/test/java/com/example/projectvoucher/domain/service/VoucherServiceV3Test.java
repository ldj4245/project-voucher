package com.example.projectvoucher.domain.service;

import com.example.projectvoucher.common.dto.RequestContext;
import com.example.projectvoucher.common.type.RequesterType;
import com.example.projectvoucher.common.type.VoucherAmountType;
import com.example.projectvoucher.common.type.VoucherStatusType;
import com.example.projectvoucher.storage.voucher.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class VoucherServiceV3Test {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private ContractRepository contractRepository;


    @DisplayName("발행된 상품권은 계약정보의 vuhcerValidPeriodDayCount 만큼 유효기간을 가져야 된다.")
    @Test
    void test(){

        //given
        final RequestContext requestContext =
                new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final LocalDate validFrom = LocalDate.now();
        final LocalDate validTo = LocalDate.now().plusDays(30);

        final VoucherAmountType amount = VoucherAmountType.KRW_30000;

        final String contractCode = "CT0001";

        //when
        final String code = voucherService.publishV3(requestContext,contractCode,VoucherAmountType.KRW_30000);
        final VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();


        //then
        final ContractEntity contractEntity = contractRepository.findByCode(contractCode).get();

        assertThat(voucherEntity.code()).isEqualTo(code);
        assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherEntity.validFrom()).isEqualTo(LocalDate.now());
        assertThat(voucherEntity.validTo()).isEqualTo(LocalDate.now().plusDays(contractEntity.voucherValidPeriodDayCount()));
        assertThat(voucherEntity.amount()).isEqualTo(amount);


        //history
        final VoucherHistoryEntity voucherHistoryEntity = voucherEntity.histories().get(0);
        assertThat(voucherHistoryEntity.orderId()).isNotNull();
        assertThat(voucherHistoryEntity.requesterType()).isEqualTo(requestContext.requesterType());
        assertThat(voucherHistoryEntity.requesterId()).isEqualTo(requestContext.requesterId());
        assertThat(voucherHistoryEntity.status()).isEqualTo(VoucherStatusType.PUBLISH);
        assertThat(voucherHistoryEntity.description()).isEqualTo("테스트 발행");

    }

    @DisplayName("유효기간이 지난 계약으로 상품권을 발행할 수 없습니다.")
    @Test
    void test0(){

        //given
        final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final VoucherAmountType amount = VoucherAmountType.KRW_30000;
        final String contractCode = "CT0010";

        //when

        assertThatThrownBy(() -> voucherService.publishV3(requestContext,contractCode,amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효기간이 지난 계약입니다.");


        //then
    }

    @DisplayName("상품권은 발행 요청자만 사용 불가 처리를 할 수 있다.")
    @Test
    void test02(){

        //given
        final RequestContext requestContext = new RequestContext(RequesterType.PARTNER, UUID.randomUUID().toString());
        final VoucherAmountType amount = VoucherAmountType.KRW_30000;

        final String contractCode = "CT0001";
        final String code = voucherService.publishV3(requestContext,contractCode,amount);


        final RequestContext otherRequestContext = new RequestContext(RequesterType.USER, UUID.randomUUID().toString());


        //when
        assertThatThrownBy(() -> voucherService.disableV3(otherRequestContext,code))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("권한이 없습니다.");


        //then
        VoucherEntity voucherEntity = voucherRepository.findByCode(code).get();

        assertThat(voucherEntity.code()).isEqualTo(code);
        assertThat(voucherEntity.status()).isEqualTo(VoucherStatusType.PUBLISH);

    }


}