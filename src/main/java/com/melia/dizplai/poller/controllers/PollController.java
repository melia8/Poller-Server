package com.melia.dizplai.poller.controllers;

import com.melia.dizplai.poller.dto.PollDto;
import com.melia.dizplai.poller.service.PollService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/poll")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PollController {

    private final PollService pollService;

    @GetMapping("/{id}")
    PollDto getPollData(@PathVariable("id") Long pollId) {
        return pollService.getPoll(pollId);
    }

    @GetMapping()
    PollDto getLatestPollData() {
        return pollService.getLatestPoll();
    }
}
