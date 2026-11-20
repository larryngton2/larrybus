package gr.larryngton.larrybus;

import gr.larryngton.larrybus.impl.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

@Getter
@Setter
@AllArgsConstructor
public class Listener {
    private Object owner;
    private Method method;
    private Class<? extends Event> eventClass;
    private int priority;
    private MethodHandle handle;
}