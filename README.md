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
- Without an event manager: ~1 nano per call
- With larrybus event manager: ~13 nanos per event call
- With cubk event manager: ~34 nanos per event call
- ~13 nanos slower than with no event manager
- ~21 nanos faster than cubk <br>
  (100.000 samples, 10.000 calls per sample)