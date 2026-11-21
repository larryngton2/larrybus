# An event manager based off of [cubk](https://github.com/cubk1/EventManager)

## Usage

### Creating an Instance
```java
EventManager eventManager = new EventManager();
```

### Registering / Unregistering a Listener
```java
eventManager.register(clazz);
eventManager.unregister(clazz);
```

### Calling an Event
```java
eventManager.call(new TestEvent());
```

### Setting up an Event Target
```java
@EventTarget
@EventPriority(-10) // optional, default priority is 0
public void onTest(TestEvent event) {
    doThings();
}
```

### About Performance
- Without an event manager: ~2 nanos per call
- With larrybus event manager: ~19 nanos per event call
- With cubk event manager: ~40 nanos per event call
- ~16 nanos slower than with no event manager
- ~21 nanos faster than cubk <br>
  (10.000 samples, 1.000 calls per sample)