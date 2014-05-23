package com.fastmodel.commons.event;

/**
 * An EventBinder is like an {@link IEventSocket}, except it binds all event listeners
 * declared on the specified object, rather than dealing in a single listener.
 */
public interface IEventBinder {
    /**
     * Add all methods annotated with {@link com.fastmodel.commons.event.Listener} as listeners in this service.
     *
     * @param object        The object for which to register listeners
     * @param <ObjectType>  The type of the object
     * @return The registered object, for chaining etc.
     *
     * @throws com.fastmodel.commons.event.ListenerTypeMismatch if the listener method cannot be bound
     *                      to any event type that can be fired by this service
     */
    <ObjectType> ObjectType addListenersFor( ObjectType object );

    /**
     * Un-registers all listeners for the specified object previously registered with
     * {@link #addListenersFor(Object)}.
     *
     * @param object The object
     * @return {@code true} if any listeners were removed.
     */
    boolean removeListenersFor( Object object );
}
