package com.melia.dizplai.poller.controllers;

import com.melia.dizplai.poller.dto.VoteCreateDto;
import com.melia.dizplai.poller.service.VoteService;
import com.melia.dizplai.poller.dto.VoteDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/vote")
@CrossOrigin(origins = "*")
public class VoteController {

    private final VoteService voteService;

    @GetMapping
    List<VoteDto> getVotes() {
        return voteService.getVotes();
    }

    @PostMapping
    VoteDto addVote(@RequestBody VoteCreateDto request) {
        return voteService.saveVote(request);
    }
}
