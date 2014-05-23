package com.fastmodel.commons.event;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * An event service capable of dispatching events of different types to the
 * appropriate listeners.  Maintains a single list of registered listeners.
 * <p/>
 * Event listeners can be bound to this service in any combination of the
 * following three ways:<ul>
 *
 * <li> To the base event type using {@link #getSocket()}.
 *      An event listener bound in this manner will be invoked for all events fired
 *      on this service.  If this is the only way the service is being used, consider
 *      instead using the {@link SimpleEventService}.  By convention, the delegating
 *      method on the object using the service should be named {@code onEvent}.</li>
 *
 * <li> To a derived event type, using {@link #getSocket(Class)}.  Listeners bound
 *      in this manner will be invoked for any events of the specified type (or types
 *      derived from that type).  By convention, the delegating method on the object
 *      using the service should be named <tt>on<i>EventType</i></tt>.</li>
 *
 * <li> All annotated event listeners on an object can be bound (or unbound) at once
 *      using {@link #getAnnotationSocket()}.  Listener methods on the object are
 *      identified by the {@link Listener} annotation, and have a single parameter
 *      which is of the event type to be bound.  By convention, the delegating method
 *      on the object using the service should be named {@code onEvents}.</li>
 * </ul>
 *
 * @param <Event> The common root event type for this service.  {@link IEvent} may
 *               be used to create an instance which is capable of handling
 *               any event type.
 */
public class MultiEventService< Event extends IEvent > extends AbstractEventService< Event > implements IMultiEventService< Event > {

    private Class< Event > eventClass;
    private List< ListenerWrapper< ? extends Event >> listeners;
    private IEventSocket< Event > defaultSocket;

    public MultiEventService( Class<Event> eventClass ) {
        this.eventClass = eventClass;
    }

    /**
     * Get a socket for registering handlers for a specific event type.
     * This method is used to ensure type-safe event registration and dispatching
     * in spite of Erasure.
     *
     * @param socketEventClass  The target event type's class object
     * @param <SocketEvent>     The target event type
     * @return The requested socket
     */
    public <SocketEvent extends Event> IEventSocket<SocketEvent> getSocket( Class<SocketEvent> socketEventClass ) {
        // Implementation note: I am going with the assumption that it is overall cheaper to
        // use temporary helper objects than to reduce the number of helper object allocations
        // at the expense of tying up memory with cached helpers.
        return new TypedEventSocket<SocketEvent>( socketEventClass );
    }

    /**
     * Fire an event to all registered listeners.
     *
     * @param event The event object to fire
     * @return {@code true} if the event is handled and not canceled
     */
    public boolean fire( Event event ) {
        boolean handled = preFire( event );

        if ( isCanceled( event )) return false;

        for ( ListenerWrapper< ? extends Event > listener : getListeners() ) {
            Event ev = listener.handle( event );
            if ( ev != null ) {
                if ( isCanceled( ev )) return false;
                handled = true;
            }
        }

        handled = postFire( event, handled );

        return handled;
    }

    /**
     * Get the socket on which to register listeners for the service's
     * root event type.  Listeners registered on this socket will be notified
     * of all events fired through this EventService.
     *
     * @return An event socket
     */
    public IEventSocket<Event> getSocket() {
        if ( defaultSocket == null ) {
            defaultSocket = new TypedEventSocket<Event>( eventClass );
        }
        return defaultSocket;
    }

    /**
     * Get a binder which can be used to bind or unbind all event listeners declared
     * with the {@link Listener} annotation.
     *
     * @return An event binder
     */
    public IEventBinder getAnnotationSocket() {
        return new AnnotationBinder();
    }

    /**
     * Don't instantiate the listeners until needed.
     *
     * @return The listeners
     */
    private List<ListenerWrapper<? extends Event>> getListeners() {
        if ( listeners == null ) {
            listeners = new LinkedList<ListenerWrapper<? extends Event>>();
        }
        return listeners;
    }

    /**
     * Encapsulation of listener binding by annotation
     */
    public class AnnotationBinder implements IEventBinder {

        private AnnotationBinder() {}

        /**
         * Add all methods annotated with {@link Listener} as listeners in this service.
         *
         * @param object        The object for which to register listeners
         * @param <ObjectType>  The type of the object
         * @return The registered object, for chaining etc.
         *
         * @throws ListenerTypeMismatch if the listener method cannot be bound
         *                      to any event type that can be fired by this service
         */
        public <ObjectType> ObjectType addListenersFor( final ObjectType object ) {

            Class cl = object.getClass();

            for ( final Method method : cl.getMethods() ) {
                Listener annotation = method.getAnnotation( Listener.class );
                if ( annotation != null ) {
                    if ( method.getParameterTypes().length != 1 ) {
                        throw new ListenerTypeMismatch( method, eventClass );
                    }
                    Class<?> type = annotation.on();
                    if ( type.equals( Object.class )) {
                        type = method.getParameterTypes()[0];
                    }
                    if ( !eventClass.isAssignableFrom( type )) {
                        if ( annotation.strict() )
                            throw new ListenerTypeMismatch( method, eventClass );
                        else
                            continue;
                    }

                    //noinspection unchecked
                    getListeners().add( new ObjectListenerWrapper<Event>( (Class<Event>) type, new IListener<Event>() {
                        public void handle( Event event ) {
                            try {
                                method.invoke( object, event );
                            } catch ( Exception e ) {
                                throw new RuntimeException( "Exception in Event Handler", e );
                            }
                        }
                    }, object ));
                }
            }

            return object;
        }

        /**
         * Un-registers all listeners for the specified object previously registered with
         * {@link #addListenersFor(Object)}.
         *
         * @param object The object
         * @return {@code true} if any listeners were removed.
         */
        public boolean removeListenersFor( Object object ) {
            boolean removed = false;
            for ( Iterator<ListenerWrapper<? extends Event>> iterator = getListeners().iterator(); iterator.hasNext(); ) {
                ListenerWrapper<? extends Event> wrapper = iterator.next();

                //noinspection unchecked
                if ( wrapper instanceof ObjectListenerWrapper && ((ObjectListenerWrapper) wrapper).getObject() == object ) {
                    iterator.remove();
                    removed = true;
                }
            }

            return removed;
        }
    }

    /**
     * Wraps a Listener, together with its registered event type, to cope with erasure.
     *
     * @param <ListenerEvent> The specific event type the listener is registered for
     */
    private class ListenerWrapper< ListenerEvent extends Event > {
        private Class< ListenerEvent > eventClass;
        private IListener< ListenerEvent > listener;

        private ListenerWrapper( Class<ListenerEvent> eventClass, IListener< ListenerEvent> listener ) {
            this.eventClass = eventClass;
            this.listener = listener;
        }

        /**
         * Handle the event, if it's compatible with this listener.  This method takes
         * care of event object preparation, in addition to invoking the listener.
         *
         * @param event The original event object
         *
         * @return The event object that was passed to the listener, or {@code null} if the
         *          listener wasn't invoked.
         */
        private Event handle( Event event ) {
            if ( eventClass.isAssignableFrom( event.getClass() )) {
                //noinspection unchecked
                ListenerEvent ev = (ListenerEvent) prepEvent( event );
                if ( ev != null ) listener.handle( ev );
                return ev;
            }

            return null;
        }

        private Class<ListenerEvent> getEventClass() {
            return eventClass;
        }

        private IListener< ListenerEvent> getListener() {
            return listener;
        }
    }

    /**
     * A wrapper for an auto-generated listener from object binding.  It needs to store the
     * object so we can un-register all listeners for that object.
     *
     * @param <ListenerEvent> The specific event type the listener is registered for
     */
    private class ObjectListenerWrapper< ListenerEvent extends Event > extends ListenerWrapper< ListenerEvent > {
        private Object object;

        private ObjectListenerWrapper( Class<ListenerEvent> listenerEventClass, IListener<ListenerEvent> listener, Object object ) {
            super( listenerEventClass, listener );
            this.object = object;
        }

        private Object getObject() {
            return object;
        }
    }

    /**
     * The EventSocket implementation used for this service.
     *
     * @param <SocketEvent> The specific bound event type for the socket
     */
    private class TypedEventSocket< SocketEvent extends Event > implements IEventSocket< SocketEvent > {
        private Class< SocketEvent > socketEventClass;

        private TypedEventSocket( Class<SocketEvent> socketEventClass ) {
            this.socketEventClass = socketEventClass;
        }

        /**
         * Register a listener for this Event
         *
         * @param listener The listener to register
         * @return The registered listener.  This is returned as a convenience,
         *         so that a caller can easily save the listener in the same
         *         statement that registers it.
         */
        public IListener<SocketEvent> addListener( IListener<SocketEvent> listener ) {
            getListeners().add( new ListenerWrapper<SocketEvent>( socketEventClass, listener ));
            return listener;
        }

        /**
         * Remove a previously registered listener from this Event.  The listener must have
         * been registered against this very same socket type.
         *
         * @param listener The listener to remove
         * @return {@code true} if the specified listener was actually removed.
         */
        public boolean removeListener( IListener<SocketEvent> listener ) {
            for ( Iterator<ListenerWrapper<? extends Event>> iterator = getListeners().iterator(); iterator.hasNext(); ) {
                ListenerWrapper<? extends Event> wrapper = iterator.next();
                if ( wrapper.getListener() == listener && wrapper.getEventClass().equals( socketEventClass )) {
                    iterator.remove();
                    return true;
                }
            }

            return false;
        }
    }
}
