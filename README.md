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