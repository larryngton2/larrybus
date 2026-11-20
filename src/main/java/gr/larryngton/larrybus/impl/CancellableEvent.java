package gr.larryngton.larrybus.impl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CancellableEvent extends Event {
    private boolean cancelled;
}
