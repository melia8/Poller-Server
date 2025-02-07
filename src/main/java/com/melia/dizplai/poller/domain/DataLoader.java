package com.melia.dizplai.poller.domain;

import com.melia.dizplai.poller.repositories.OptionRepository;
import com.melia.dizplai.poller.repositories.PollRepository;
import com.melia.dizplai.poller.repositories.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

@Component
@AllArgsConstructor
@Profile("!test")
public class DataLoader implements CommandLineRunner {
    private final OptionRepository optionRepo;
    private final PollRepository pollRepo;
    private final VoteRepository voteRepo;

    @Override
    public void run(String... args) throws Exception {

        //Set up a previous poll
        Option liverpoolCL = optionRepo.save(new Option("Liverpool"));
        Option barcelonaCL = optionRepo.save(new Option("Barcelona"));
        Option arsenalCL = optionRepo.save(new Option("Arsenal"));
        Option astonVillaCL = optionRepo.save(new Option("Aston Villa"));

        List<Option> optionsCL = Arrays.asList(liverpoolCL, barcelonaCL, arsenalCL, astonVillaCL);
        Poll champsPoll = new Poll("Who will win Champions League", optionsCL);

        champsPoll.setTotal(15L);
        pollRepo.save(champsPoll);

        liverpoolCL.setTotal(10L);
        optionRepo.save(liverpoolCL);
        IntStream.range(0, 10).forEach((ignore) -> voteRepo.save(new Vote(champsPoll, LocalDateTime.now(), liverpoolCL)));

        barcelonaCL.setTotal(5L);
        optionRepo.save(barcelonaCL);
        IntStream.range(0, 5).forEach((ignore) -> voteRepo.save(new Vote(champsPoll, LocalDateTime.now(), barcelonaCL)));

        //Set up the current poll
        Option liverpool = optionRepo.save(new Option("Liverpool"));
        Option manCity = optionRepo.save(new Option("Manchester City"));
        Option arsenal = optionRepo.save(new Option("Arsenal"));
        Option chelsea = optionRepo.save(new Option("Chelsea"));

        List<Option> options = Arrays.asList(liverpool, manCity, arsenal, chelsea);
        pollRepo.save(new Poll("Who will win the Premier League?", options));
    }
}
