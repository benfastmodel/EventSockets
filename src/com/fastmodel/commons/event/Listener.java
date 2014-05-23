package com.fastmodel.commons.event;

import java.lang.annotation.*;

/**
 * Annotation which can be applied to event listeners, for annotation-based event binding.
 * This annotation can only be applied to methods, and the method must have only a single
 * parameter, whose type determines the event type for which it will be registered.  The
 * return type doesn't matter, and any return value will be ignored.
 */
@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Listener {
    /**
     * If the listener isn't strict, it will be ignored if an attempt is made to bind the
     * object to a class which doesn't fire the specified type.  Otherwise, a
     * {@link ListenerTypeMismatch} exception
     */
    public boolean strict() default true;
    public Class on() default Object.class;
}
