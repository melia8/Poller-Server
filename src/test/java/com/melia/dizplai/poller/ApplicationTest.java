package com.melia.dizplai.poller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melia.dizplai.poller.domain.Poll;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApplicationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldReturnNotFoundMessage() throws Exception {
        assertThat(restTemplate.getForObject("http://localhost:" + port + "/",
                String.class)).contains("Could not find object");
    }

    @Test
    void shouldReturnErrorWhenTooFewPollOptions() throws Exception {
        String body = """
                {"title" : "invalid poll",
                "options": ["one"]
                }
                """;

        assertThat(restTemplate.postForObject("http://localhost:" + port + "/poll", postRequest(body),
                String.class)).contains("Can only define 2 to 7 options");
    }

    @Test
    void shouldReturnErrorWhenTooManyPollOptions() throws Exception {
        String body = """
                {"title" : "invalid poll",
                "options": ["one","two", "three", "four", "five", "six", "seven", "eight"]
                }
                """;

        assertThat(restTemplate.postForObject("http://localhost:" + port + "/poll", postRequest(body),
                String.class)).contains("Can only define 2 to 7 options");
    }

    @Test
    void shouldCreateNewPoll() throws Exception {
        String body = """
                {"title" : "Who will win Premier League",
                "options": ["Liverpool", "Arsenal", "Chelsea"]
                }
                """;

        String expectedResponse = """
                {"id":1,
                "title":"Who will win Premier League",
                "total":0,
                "options":[{"id":1,"name":"Liverpool","total":0},{"id":2,"name":"Arsenal","total":0},{"id":3,"name":"Chelsea","total":0}]
                }
                """;

        assertThat(restTemplate.postForObject("http://localhost:" + port + "/poll", postRequest(body),
                String.class)).isEqualTo(unFormat(expectedResponse));
    }

    @Test
    void pollVotingIncrementsValuesCorrectly() {
        Poll poll = createTestPoll();

        IntStream.range(0, 5).forEach((i) -> voteForTeam("Liverpool", poll.getId()));
        IntStream.range(0, 2).forEach((i) -> voteForTeam("Chelsea", poll.getId()));
        IntStream.range(0, 3).forEach((i) -> voteForTeam("Arsenal", poll.getId()));

        String expectedResponse = """
                {"id":%d,
                "title":"Who will win Premier League",
                "total":10,
                "options":[{"id":%d,"name":"Liverpool","total":5},{"id":%d,"name":"Arsenal","total":3},{"id":%d,"name":"Chelsea","total":2}]}
                """.formatted(poll.getId(),
                getOptionId(poll, "Liverpool"),
                getOptionId(poll, "Arsenal"),
                getOptionId(poll, "Chelsea"));

        assertThat(restTemplate.getForObject("http://localhost:" + port + "/poll",
                String.class)).isEqualTo(unFormat(expectedResponse));
    }

    private HttpEntity<String> postRequest(String body) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<String>(body, headers);
    }

    private String unFormat(String str) {
        return str.replaceAll("[\\n\\r]", "");
    }

    @SneakyThrows
    private Poll createTestPoll() {

        String body = """
                {"title" : "Who will win Premier League",
                "options": ["Liverpool", "Arsenal", "Chelsea"]
                }
                """;

        String response = restTemplate.postForObject("http://localhost:" + port + "/poll", postRequest(body),
                String.class);

        return objectMapper.readValue(response, Poll.class);
    }

    private void voteForTeam(String team, Long id) {
        String body = """
                {"pollId":%d, "name": "%s"}
                """.formatted(id, team);

        restTemplate.postForObject("http://localhost:" + port + "/vote", postRequest(body),
                String.class);
    }

    private Long getOptionId(Poll poll, String name) {
        return poll.getOptions().stream()
                .filter(o -> o.getName().equals(name))
                .findFirst()
                .orElseThrow()
                .getId();
    }

}
