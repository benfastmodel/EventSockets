# EventSockets

## A Lightweight Object Event Framework

The purpose of this framework is to provide a simple approach to implementing direct
Publish / Subscribe, similar to that used by GUI frameworks, without requiring a lot
of repeated boilerplate code.  This is not an Event Bus or Grand Central Dispatch
eventing framework.


## Concepts and Usage

### Events

Events in this framework are identified by their type.  All events must implement
`IEvent`, which is a simple tagging interface.  While this interface wasn't
strictly necessary, it helps ensure that event objects are appropriately declared.
It also serves as the base for two specialty variants.

Event types implementing `ICancelableEvent` will abort event processing if
the fired event object returns `true` from `ICancelableEvent.canceled()`.
Generally, you will want to extend the `AbstractCancellableEvent` class,
rather than directly implementing this interface.

Event objects of types implementing `IClonableEvent` will be cloned prior to
each listener call, whereas ordinary event objects are simply passed to each registered
listener in turn.  Generally, you will want to extend the `AbstractClonableEvent`
class, rather than directly implementing this interface.


### Listeners

Listeners can be created either as instances of `IListener`, or as methods
of the listening object, using the `@Listener` annotation.  The `IListener`
interface is lambda-compatible, so in Java 8 (or beyond) listeners may be
provided simply as lambda expressions.

Listeners are characterized by the type of event object which they listen for,
which is provided as the generic type for `IListener`.


### Event Sockets

Event Sockets are the glue which binds listeners to objects.

Rather than requiring objects which expose (or fire) events to implement a plethora of
add*Listener and remove*Listener methods, the class exposes one Event Socket method
for each event type.  The value returned by the Event Socket method has the appropriate
bind and unbind methods.  The term Event Socket reflects the notions that event listeners
are plugged into the event they listen for.

By convention, all Event Socket methods are named on*EventType*.


### Event Services

A class which wishes to expose events should have a private member of an appropriate
Event Service type.  The Event Service provides methods to get Event Sockets and to
fire events.  While the Event Sockets are expected to be part of the public interface
of the class, the firing of events is generally part of the private (or protected)
implementation.

There are two base types of Event Services, which both implement `IEventService`.
The `SimpleEventService` is for classes which only need to expose a single event
type.  Such classes will typically expose a single Event Socket, named either `onEvent`
or on*EventType*.  The socket is obtained by calling `SimpleEventService.getSocket()`.

The `MultiEventService` is for classes which need to expose multiple events (i.e.
multiple event types).  In addition to supporting the socket for the root event type,
the MultiEventService has the `MultiEventService.getSocket( Class eventClass )`
method, which allows event listeners to be bound to a specific event type.  It also
supports binding all annotated listeners on an object at once, using
`MultiEventService.getBinder()`.
See the `@Listener` annotation's documentation for more details.


#### Event Propagation

The provided `IEventService` implementations provide support for propagating
events to handlers registered on other objects, such as propagating a UI event
up the component hierarchy.  This propagation can be easily implemented by
overriding the Event Service's `preFire` or `postFire` methods, empty implementations
of which are defined in the `AbstractEventService` class.


## Example

```java
class EventA implements IEvent { ... }
class EventB implements IEvent { ... }

class ExposesEvents {
	private MultiEventService<IEvent> eventService = new MultiEventService<IEvent>( IEvent.class );

	public onEventA() {
		return eventService.getSocket( EventA.class );
	}

	public onEventB() {
		return eventService.getSocket( EventB.class );
	}

	public onEvents() {
		return eventService.getBinder();
	}

	public action() {
		// ...
		if ( someCondition ) {
			eventService.fire( new EventA() );
		} else {
			eventService.fire( new EventB() );
		}
	}
}

// A sample of direct listener registration:
ExposesEvents model = ...;
model.onEventA().bind( new IListener<EventA>() {
	public void handle( EventA ) {
		// listener code goes here
	}
} );

// A sample of annotation listener registration:
class HasListeners {
	@Listener
	public void onEventA( EventA ) {
		...
	}

	@Listener
	public void onEventB( EventB ) {
		...
	}
}
HasListeners myObject = new HasListeners();
model.onEvents().bind( myObject );
```

## License

Copyright 2014 Fast Model Technologies

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


## Contributions

I am open to contributions, and will endeavour to incorporate appropriate changes
in a timely manner.