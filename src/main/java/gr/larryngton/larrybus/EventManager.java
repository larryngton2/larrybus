package gr.larryngton.larrybus;

import gr.larryngton.larrybus.annotations.EventPriority;
import gr.larryngton.larrybus.annotations.EventTarget;
import gr.larryngton.larrybus.impl.Event;
import lombok.Getter;
import lombok.Setter;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {
    private final Map<Class<? extends Event>, List<Listener>> listenerMap = new ConcurrentHashMap<>();
    private final Map<Class<?>, List<Listener>> dispatchCache = new ConcurrentHashMap<>();
    @Getter
    @Setter
    private boolean printExceptions = false;

    public EventManager() {
    }

    public EventManager(boolean printExceptions) {
        this.printExceptions = printExceptions;
    }

    /***
     * Registers a new event listener
     * @param obj the class to register
     */
    public void register(Object obj) {
        dispatchCache.clear();
        for (Method method : obj.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventTarget.class)) {
                continue;
            }

            /*
                 i was doing:

                 @EventTarget
                 public void onEnable() {
                 }

                 so i prob need this LOL
             */
            if (method.getParameterCount() != 1) {
                throw new IllegalArgumentException("Event listener must have one parameter: " + method);
            }

            Class<?> parameterType = method.getParameterTypes()[0];

            if (!Event.class.isAssignableFrom(parameterType)) {
                throw new IllegalArgumentException("Event listener parameter must extend Event: " + method);
            }

            Class<? extends Event> eventClass = (Class<? extends Event>) parameterType;

            EventPriority annotation = method.getAnnotation(EventPriority.class);

            try {
                MethodHandle handle = MethodHandles.privateLookupIn(obj.getClass(), MethodHandles.lookup()).unreflect(method).bindTo(obj);
                int priority = annotation != null ? annotation.value() : 0;

                Listener listener = new Listener(obj, method, eventClass, priority, handle);
                listenerMap.compute(eventClass, (_, listeners) -> {
                    List<Listener> updated = listeners == null ? new ArrayList<>() : new ArrayList<>(listeners);
                    updated.add(listener);
                    updated.sort(Comparator.comparingInt(Listener::getPriority));

                    return new CopyOnWriteArrayList<>(updated);
                });
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Cannot access event listener: " + method, e);
            }
        }
    }

    /***
     * Unregisters a registered event listener
     * @param obj the listener to unregister
     */
    public void unregister(Object obj) {
        dispatchCache.clear();
        listenerMap.forEach((eventClass, listeners) -> {
            listeners.removeIf(listener -> listener.getOwner() == obj);

            if (listeners.isEmpty()) {
                listenerMap.remove(eventClass, listeners);
            }
        });
    }

    /***
     * Invokes all event listeners associated with the provided event
     * @param event the event to call the registered methods for
     */
    public Event call(Event event) {
        Class<?> eventClass = event.getClass();
        if (!dispatchCache.containsKey(eventClass)) {
            listenerMap.forEach((registeredClass, listeners) -> {
                if (registeredClass.isAssignableFrom(eventClass)) {
                    dispatchCache.put(eventClass, listeners);
                }
            });
        }

        boolean printExceptions = this.printExceptions;
        for (Listener listener : dispatchCache.getOrDefault(eventClass, new ArrayList<>())) {
            try {
                listener.getHandle().invoke(event);
            } catch (Throwable e) {
                if (printExceptions) {
                    e.getCause().printStackTrace();
                }
            }
        }
        return event;
    }
}
