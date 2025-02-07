package com.melia.dizplai.poller.controllers;

import com.melia.dizplai.poller.dto.PollCreateDto;
import com.melia.dizplai.poller.dto.PollDto;
import com.melia.dizplai.poller.dto.VoteDto;
import com.melia.dizplai.poller.service.PollService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/poll")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PollController {

    private static final int MIN_OPTIONS = 2;
    private static final int MAX_OPTIONS = 7;

    private final PollService pollService;

    @GetMapping()
    PollDto getLatestPollData() {
        return pollService.getLatestPoll();
    }

    @GetMapping("/{id}")
    PollDto getPollData(@PathVariable("id") Long pollId) {
        return pollService.getPoll(pollId);
    }

    @GetMapping("/{id}/votes")
    List<VoteDto> getVoteData(@PathVariable("id") Long pollId) {
        return pollService.getVotes(pollId);
    }

    @PostMapping
    PollDto createPoll(@RequestBody PollCreateDto poll) {
        if (poll.options().size() < MIN_OPTIONS || poll.options().size() > MAX_OPTIONS) {
            throw new IllegalArgumentException(String.format("Can only define %d to %d options", MIN_OPTIONS, MAX_OPTIONS));
        }
        return pollService.createPoll(poll);
    }
}
