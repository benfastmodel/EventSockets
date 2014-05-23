package com.fastmodel.commons.event;

/**
 * Utilities and base implementations for Event Service implementations.
 */
public abstract class AbstractEventService< Event extends IEvent > implements IEventService<Event> {
    /**
     * Check if the event has been cancelled.
     *
     * @param ev The event object to check
     * @return {@code true} if the event is cancellable and has been cancelled.
     */
    protected static <Event> boolean isCanceled( Event ev ) {
        return ev instanceof ICancelableEvent && ( (ICancelableEvent) ev ).isCancelled();
    }

    /**
     * Override this method to provide pre-handling for event firing, eg.
     * to propagate the event prior to invoking handlers in this service.
     * Handlers in this service will not be invoked if the event is cancelled
     * by this method.
     *
     * @param event The original event object
     * @return {@code true} if any handlers were invoked
     */
    protected boolean preFire( Event event ) {
        return false;
    }

    /**
     * Override this method to provide post-handling, eg. to propagate an event
     * up the model hierarchy.
     *
     * @param event   The original event object
     * @param handled {@code true} if at least one handler has been invoked.
     *
     * @return {@code true} if at least one handler has been invoked.
     */
    protected boolean postFire( Event event, boolean handled ) {
        return handled;
    }

    /**
     * Prepare the event object to be passed to a listener: if the event
     * object implements {@link com.fastmodel.commons.event.IClonableEvent}, the event is cloned.
     *
     * @param event The fired event object
     * @return The instance of the event object to pass to the listener, or {@code null} if cloning failed
     */
    protected Event prepEvent( Event event ) {
        if ( event instanceof IClonableEvent ) {
            try {
                //noinspection unchecked
                return (Event) ( (IClonableEvent) event ).clone();
            } catch ( CloneNotSupportedException e ) {
                return null;
            }
        } else {
            return event;
        }
    }
}
