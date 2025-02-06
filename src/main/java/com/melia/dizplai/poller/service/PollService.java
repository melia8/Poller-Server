package com.melia.dizplai.poller.service;

import com.melia.dizplai.poller.domain.Poll;
import com.melia.dizplai.poller.dto.PollDto;
import com.melia.dizplai.poller.repositories.PollRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PollService {

    PollRepository pollRepo;

    private PollDto mapToDto(Poll poll) {
        return new PollDto(poll.getId(), poll.getDescription(), poll.getTotal(), poll.getOptions());
    }

    public PollDto getPoll(Long id) {
        Poll poll = pollRepo.findById(id).orElseThrow();
        return mapToDto(poll);
    }

    public PollDto getLatestPoll() {
        Poll poll = pollRepo.findFirstByOrderByCreatedOnDesc().orElseThrow();
        return mapToDto(poll);
    }

}
