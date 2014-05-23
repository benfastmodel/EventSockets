package com.fastmodel.commons.event;

/**
 * Event Sockets are the public interface for registering & de-registering
 * listeners for a specific event type.
 */
public interface IEventSocket< Event extends IEvent > {
    /**
     * Register a listener for this Event
     *
     * @param listener The listener to register
     * @return The registered listener.  This is returned as a convenience,
     *         so that a caller can easily save the listener in the same
     *         statement that registers it.
     */
    IListener< Event > addListener( IListener< Event > listener );

    /**
     * Remove a previously registered listener from this Event.
     *
     * @param listener The listener to remove
     * @return {@code true} if the specified listener was actually removed.
     */
    boolean removeListener( IListener< Event > listener );
}
