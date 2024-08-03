package com.example.projectvoucher.storage;


import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreatedDate
    @Column(updatable = false) // 회원이 생성된 날짜는 변하지 않으므로
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    public Long id(){
        return id;
    }

    public LocalDateTime cratedAt(){
        return createdAt;
    }

    public LocalDateTime updatedAt(){
        return updatedAt;
    }
}
