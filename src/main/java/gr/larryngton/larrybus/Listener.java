package gr.larryngton.larrybus;

import gr.larryngton.larrybus.impl.Event;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

public record Listener(Object owner, Method method, Class<? extends Event> eventClass, int priority, MethodHandle handle) {
}