package com.onthe7.petking.common.domain.entity;

import com.onthe7.petking.common.enums.YesNo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;

@Getter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @Builder.Default
    @Enumerated(STRING)
    protected YesNo deleted = YesNo.N;

    @CreationTimestamp
    protected LocalDateTime createdAt;

    @CreatedBy
    protected Long createdBy;

    @UpdateTimestamp
    protected LocalDateTime updatedAt;

    @LastModifiedBy
    protected Long updatedBy;

    protected LocalDateTime deletedAt;

    protected Long deletedBy;
}
