package com.melia.dizplai.poller.dto;

import com.melia.dizplai.poller.domain.Option;
import com.melia.dizplai.poller.domain.Poll;
import com.melia.dizplai.poller.domain.Vote;
import com.melia.dizplai.poller.repositories.PollRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
public class Mapper {

    PollRepository pollRepo;

    public OptionDto mapToDto(Option option) {
        return new OptionDto(option.getId(), option.getName(), option.getTotal());
    }

    public PollDto mapToDto(Poll poll) {
        List<OptionDto> options = poll.getOptions().stream()
                .map(this::mapToDto)
                .toList();

        return new PollDto(poll.getId(), poll.getDescription(), poll.getTotal(), options);
    }

    public VoteDto mapToDto(Vote vote) {
        return new VoteDto(vote.getPoll().getId(),
                vote.getOption().getName(),
                vote.getVotedOn().format(DateTimeFormatter.ISO_DATE_TIME));
    }

    public Vote mapFromDto(VoteDto vote) {
        Poll poll = pollRepo.findById(vote.pollId()).orElseThrow();

        Option option = poll.getOptions().stream()
                .filter(o -> o.getName().equals(vote.name()))
                .findFirst()
                .orElseThrow();

        return new Vote(poll, LocalDateTime.now(), option);
    }

    public Poll mapFromDto(PollCreateDto poll) {
        List<Option> options = poll.options().stream()
                .map(Option::new)
                .toList();

        return new Poll(poll.title(), options);
    }

}
