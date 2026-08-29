package com.logikaintermedia.erp.utility;

import java.time.LocalDateTime;
import java.util.UUID;

public interface HasCursor {
    LocalDateTime getCreatedAt();

    UUID getId();
}