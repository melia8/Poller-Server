package com.melia.dizplai.poller.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
@Data
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "poll_id")
    private Poll poll;

    @ManyToOne
    @JoinColumn(name = "option_id")
    private Option option;

    private LocalDateTime votedOn;

    public Vote(Poll poll, LocalDateTime votedOn, Option option) {
        this.poll = poll;
        this.votedOn = votedOn;
        this.option = option;
    }
}
