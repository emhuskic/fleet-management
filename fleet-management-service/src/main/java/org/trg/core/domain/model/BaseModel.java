package org.trg.core.domain.model;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseModel implements Serializable {

    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }
}
