package com.melia.dizplai.poller.service;

import com.melia.dizplai.poller.domain.Poll;
import com.melia.dizplai.poller.dto.Mapper;
import com.melia.dizplai.poller.dto.PollCreateDto;
import com.melia.dizplai.poller.dto.PollDto;
import com.melia.dizplai.poller.dto.VoteDto;
import com.melia.dizplai.poller.repositories.OptionRepository;
import com.melia.dizplai.poller.repositories.PollRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PollService {

    private final PollRepository pollRepo;
    private final OptionRepository optionRepo;
    private final Mapper mapper;

    public PollDto getPoll(Long id) {
        return pollRepo.findById(id)
                .map(mapper::mapToDto)
                .orElseThrow();
    }

    public List<VoteDto> getVotes(Long id) {
        Poll poll = pollRepo.findById(id).orElseThrow();

        return poll.getVotes().stream()
                .map(mapper::mapToDto)
                .toList();
    }

    public PollDto getLatestPoll() {
        Poll poll = pollRepo.findFirstByOrderByCreatedOnDesc().orElseThrow();
        return mapper.mapToDto(poll);
    }

    @Transactional
    public PollDto createPoll(PollCreateDto poll) {
        Poll createdPoll = mapper.mapFromDto(poll);
        createdPoll.getOptions().forEach(optionRepo::save);
        pollRepo.save(createdPoll);

        return mapper.mapToDto(createdPoll);
    }

}
