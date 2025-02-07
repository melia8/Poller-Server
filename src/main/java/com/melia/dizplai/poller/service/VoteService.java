package com.melia.dizplai.poller.service;

import com.melia.dizplai.poller.domain.Option;
import com.melia.dizplai.poller.domain.Poll;
import com.melia.dizplai.poller.domain.Vote;
import com.melia.dizplai.poller.dto.Mapper;
import com.melia.dizplai.poller.dto.VoteCreateDto;
import com.melia.dizplai.poller.dto.VoteDto;
import com.melia.dizplai.poller.repositories.OptionRepository;
import com.melia.dizplai.poller.repositories.PollRepository;
import com.melia.dizplai.poller.repositories.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepo;
    private final PollRepository pollRepo;
    private final OptionRepository optionRepo;
    private final Mapper mapper;

    public List<VoteDto> getVotes() {
        return voteRepo.findAll().stream()
                .map(mapper::mapToDto)
                .toList();
    }

    @Transactional
    public VoteDto saveVote(VoteCreateDto v) throws IllegalArgumentException {
        Vote vote = mapper.mapFromDto(v);
        Poll poll = vote.getPoll();
        Option option = vote.getOption();

        poll.setTotal(poll.getTotal() + 1);
        option.setTotal(option.getTotal() + 1);

        pollRepo.save(poll);
        optionRepo.save(option);
        return mapper.mapToDto(voteRepo.save(vote));
    }
}
