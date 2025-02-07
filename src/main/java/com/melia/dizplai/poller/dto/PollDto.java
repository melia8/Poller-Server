package com.melia.dizplai.poller.dto;

import java.util.List;

public record PollDto(Long id, String title, Long total, List<OptionDto> options) {
}
