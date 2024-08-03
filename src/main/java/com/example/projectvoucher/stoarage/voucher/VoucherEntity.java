package com.example.projectvoucher.stoarage.voucher;

import com.example.projectvoucher.common.type.VoucherStatusType;
import com.example.projectvoucher.stoarage.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import java.time.LocalDate;


@Table(name="voucher")
@Entity
public class VoucherEntity extends BaseEntity {
    private String code;


    @Enumerated(EnumType.STRING)
    private VoucherStatusType status;

    private LocalDate validFrom;
    private LocalDate validTo;
    private Long amount;


    public VoucherEntity(){

    }

    public VoucherEntity(String code, VoucherStatusType status, LocalDate validFrom, LocalDate validTo,Long amount){
        this.code = code;
        this.status = status;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.amount = amount;

    }

    public String code(){
        return this.code;
    }

    public VoucherStatusType status(){
        return status;
    }

    public LocalDate validTo(){
        return validTo;
    }

    public LocalDate validFrom(){
        return validFrom;

    }

    public Long amount(){
        return amount;
    }




}
