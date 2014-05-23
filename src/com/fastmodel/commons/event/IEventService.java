package com.fastmodel.commons.event;

/**
 * An Event Service handles dispatching of events of the specified type,
 * and provides the Event Sockets on which listeners can be registered.
 *
 * @param <Event> The root event type dispatched by this Event Service.
 *                Actual event objects may of course extend this type.
 */
public interface IEventService< Event extends IEvent > {
    /**
     * Fire an event to all registered listeners.
     *
     * @param event The event object to fire
     *
     * @return {@code true} if the event is handled and not canceled
     */
    boolean fire( Event event );

    /**
     * Get the socket on which to register listeners for the service's
     * root event type.
     *
     * @return An event socket
     */
    IEventSocket< Event > getSocket();
}
