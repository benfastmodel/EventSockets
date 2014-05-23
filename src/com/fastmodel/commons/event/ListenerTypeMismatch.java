package com.fastmodel.commons.event;

import java.lang.reflect.Method;

/**
 * Exception type which is used to signal an annotation-based listener which
 * can't be bound to the event service.
 */
public class ListenerTypeMismatch extends RuntimeException {
    private Method method;
    private Class eventType;

    /**
     * Constructs a new runtime exception with <code>null</code> as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public ListenerTypeMismatch( Method method, Class eventType ) {
        super( String.format( "Annotated method %s cannot handle any events of type %s", method.getName(), eventType.getName() ));  // NON-NLS
        this.method = method;
        this.eventType = eventType;
    }

    public Method getMethod() {
        return method;
    }

    public Class getEventType() {
        return eventType;
    }
}
