package gr.larryngton.larrybus.impl;

import lombok.Getter;
import lombok.Setter;

/**
 * Marker interface for cancellable events.
 */
@Getter
@Setter
public abstract class CancellableEvent extends Event {
    /**
     * A flag indicating whether this event has been cancelled
     */
    private boolean cancelled;
}
