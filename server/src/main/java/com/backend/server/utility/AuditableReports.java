package com.backend.server.utility;

import org.springframework.data.annotation.LastModifiedBy;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditableReports extends Auditable {

    @LastModifiedBy
    @Column(name = "last_modified_by", nullable = false, updatable = true)
    private String lastModifiedBy;
    


}
