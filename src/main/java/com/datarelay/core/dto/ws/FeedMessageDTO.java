package com.datarelay.core.dto.ws;

import java.util.Map;

public record FeedMessageDTO (
    String schemaName,
    String timestamp,
    Map<String, Object> data
) {}
