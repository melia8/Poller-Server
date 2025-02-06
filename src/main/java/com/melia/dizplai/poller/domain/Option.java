package com.melia.dizplai.poller.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "option")
@Data
@NoArgsConstructor
public class Option {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Long total = 0L;

    public Option(String name) {
        this.name = name;
    }
}
