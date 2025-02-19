package com.http200ok.finbuddy.product.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@DiscriminatorValue("SAVING")
@Getter @Setter
@NoArgsConstructor
public class SavingProductOption extends ProductOption {

    @Column
    private String reserveType;

    @Column
    private String reserveTypeName;
}
