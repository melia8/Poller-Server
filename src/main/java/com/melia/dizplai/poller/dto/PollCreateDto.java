package com.melia.dizplai.poller.dto;

import java.util.List;

public record PollCreateDto(String title, List<String> options) {
}
