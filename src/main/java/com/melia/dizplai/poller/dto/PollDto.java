package com.melia.dizplai.poller.dto;

import com.melia.dizplai.poller.domain.Option;

import java.util.List;

public record PollDto(Long id, String title, Long total, List<Option> options) {
}
