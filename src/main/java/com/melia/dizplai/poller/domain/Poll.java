package com.melia.dizplai.poller.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "poll")
@Data
@NoArgsConstructor
public class Poll {
    @Id
    @GeneratedValue
    private Long id;
    private String description;
    private Long total;
    private LocalDateTime createdOn;

    @OneToMany
    private List<Option> options;

    @OneToMany(mappedBy = "poll")
    private List<Vote> votes;

    public Poll(String description, List<Option> options) {
        this.description = description;
        this.options = options;
        this.total = 0L;
        this.createdOn = LocalDateTime.now();
    }
}
