package com.tpvgallega.backend.infrastructure.rest.exception;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, int status, String mensaje) {
}
