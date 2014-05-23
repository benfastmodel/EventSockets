package com.fastmodel.commons.event;

import java.util.LinkedList;
import java.util.List;

/**
 * An Event Service which handles a single type of event.  Useful for models which
 * only expose a single event.
 *
 * This service can be customized by overriding {@link #preFire(IEvent)}, {@link #postFire(IEvent, boolean)}
 * or {@link #prepEvent(IEvent)}.
 */
public class SimpleEventService< Event extends IEvent > extends AbstractEventService< Event > {

    private List< IListener< Event >>   listeners;
    private IEventSocket< Event >       socket;

    /**
     * Initialize the event service
     */
    public SimpleEventService() {
    }

    /**
     * Fire an event to all registered listeners.
     *
     * @param event The event object to fire
     * @return {@code true} if the event is handled and not canceled
     */
    public final boolean fire( Event event ) {
        boolean handled = preFire( event );

        if ( isCanceled( event )) return false;

        for ( IListener< Event > listener : getListeners() ) {
            Event ev = prepEvent( event );
            if ( ev == null ) continue;
            listener.handle( ev );
            if ( isCanceled( ev )) return false;
            handled = true;
        }

        handled = postFire( event, handled );

        return handled;
    }

    /**
     * Get the socket on which to register listeners for the service's
     * root event type.
     *
     * @return An event socket
     */
    public final IEventSocket<Event> getSocket() {
        if ( socket == null ) {
            socket = new EventSocket();
        }
        return socket;
    }

    /**
     * Provides initialize-on-firs-use for the listeners
     *
     * @return The list of listeners
     */
    private List< IListener< Event >> getListeners() {
        if ( listeners == null ) {
            listeners = new LinkedList<IListener<Event>>();
        }
        return listeners;
    }

    private class EventSocket implements IEventSocket< Event > {

        /**
         * Register a listener for this Event
         *
         * @param listener The listener to register
         * @return The registered listener.  This is returned as a convenience,
         *         so that a caller can easily save the listener in the same
         *         statement that registers it.
         */
        public IListener<Event> addListener( IListener<Event> listener ) {
            getListeners().add( listener );
            return listener;
        }

        /**
         * Remove a previously registered listener from this Event.
         *
         * @param listener The listener to remove
         * @return {@code true} if the specified listener was actually removed.
         */
        public boolean removeListener( IListener<Event> listener ) {
            return getListeners().remove( listener );
        }
    }
}
