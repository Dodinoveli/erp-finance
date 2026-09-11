package com.logikaintermedia.erp.utility;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface HasCursor {
    OffsetDateTime getCreatedAt();

    UUID getId();
}