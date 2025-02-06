package com.melia.dizplai.poller.service;

import com.melia.dizplai.poller.domain.Option;
import com.melia.dizplai.poller.domain.Poll;
import com.melia.dizplai.poller.domain.Vote;
import com.melia.dizplai.poller.repositories.OptionRepository;
import com.melia.dizplai.poller.repositories.PollRepository;
import com.melia.dizplai.poller.repositories.VoteRepository;
import com.melia.dizplai.poller.dto.VoteDto;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepo;
    private final PollRepository pollRepo;
    private final OptionRepository optionRepo;

    VoteDto mapToDto(Vote vote) {
        return new VoteDto(vote.getPoll().getId(), vote.getOption().getName(), vote.getVotedOn().format(DateTimeFormatter.ISO_DATE_TIME));
    }


    Vote mapFromDto(VoteDto vote) {
        Poll poll = pollRepo.findById(vote.pollId()).orElseThrow();
        Option option = poll.getOptions().stream()
                .filter(o -> o.getName().equals(vote.name()))
                .findFirst()
                .orElseThrow();

        return new Vote(poll, LocalDateTime.now(), option);
    }

    public List<VoteDto> getVotes() {
        return voteRepo.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    public VoteDto saveVote(VoteDto v) throws IllegalArgumentException {
        Vote vote = mapFromDto(v);
        Poll poll = vote.getPoll();
        Option option = vote.getOption();

        poll.setTotal(poll.getTotal() + 1);
        option.setTotal(option.getTotal() + 1);

        pollRepo.save(poll);
        optionRepo.save(option);
        return mapToDto(voteRepo.save(vote));
    }
}
