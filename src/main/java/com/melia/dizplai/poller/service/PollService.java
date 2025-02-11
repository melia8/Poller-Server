package com.melia.dizplai.poller.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melia.dizplai.poller.domain.Poll;
import com.melia.dizplai.poller.dto.Mapper;
import com.melia.dizplai.poller.dto.PollCreateDto;
import com.melia.dizplai.poller.dto.PollDto;
import com.melia.dizplai.poller.dto.VoteDto;
import com.melia.dizplai.poller.repositories.OptionRepository;
import com.melia.dizplai.poller.repositories.PollRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@AllArgsConstructor
public class PollService {

    private final PollRepository pollRepo;
    private final OptionRepository optionRepo;
    private final Mapper mapper;
    private final ObjectMapper jsonMapper;


    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        return emitter;
    }

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void sendEvents() {
        for (SseEmitter emitter : emitters) {
            try {
                PollDto poll = getLatestPoll();
                emitter.send(jsonMapper.writeValueAsString(poll));
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(emitter);
            }
        }
    }

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
