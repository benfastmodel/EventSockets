package com.fastmodel.commons.event;

/**
 * An Event Service which can expose multiple sockets for different
 * related event types.  Events dispatched by implementations of this service
 * are only sent to listeners registered on sockets for compatible types.
 *
 * A Multi-Event Service is an efficient way to maintain a single dispatcher
 * for a type which exposes multiple event types.
 */
public interface IMultiEventService< Event extends IEvent > extends IEventService< Event > {

    /**
     * Get a socket for registering handlers for a specific event type.
     * This method is used to ensure type-safe event registration and dispatching
     * in spite of Erasure.
     *
     * @param eventClass The target event type's class object
     * @param <SocketEvent> The target event type
     *
     * @return The requested socket
     */
    <SocketEvent extends Event> IEventSocket< SocketEvent > getSocket( Class< SocketEvent > eventClass );
}
