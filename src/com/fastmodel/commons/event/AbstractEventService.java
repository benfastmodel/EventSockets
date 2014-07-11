/*
   Copyright 2014 Fast Model Technologies, LLC

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.fastmodel.commons.event;

/**
 * Utilities and base implementations for Event Service implementations.
 *
 * @author Ben Schreiber
 * @version 1.0
 */
public abstract class AbstractEventService< Event extends IEvent > implements IEventService<Event> {
    /**
     * Check if the event is a cancellable event which has been cancelled.
     *
     * @param ev The event object to check
     * @return {@code true} if the event is cancellable and has been cancelled.
     */
    protected static <Event> boolean isCanceled( Event ev ) {
        return ev instanceof ICancelableEvent && ( (ICancelableEvent) ev ).isCancelled();
    }

    /**
     * <p>Invoked prior to invoking any handlers on this Service.
     *
     * <p>Override this method to provide pre-handling for event firing, eg.
     * to propagate the event prior to invoking handlers in this service.
     * Handlers in this service will not be invoked if the event is cancelled
     * by this method.
     *
     * @param event The original event object
     * @return {@code true} if any handlers were invoked and the event was not cancelled.
     */
    protected boolean preFire( Event event ) {
        return false;
    }

    /**
     * <p>Invoked after all handlers for this event (if any) have been invoked (unless
     * the event {@link #isCanceled}).
     *
     * <p>Override this method to provide post-handling, eg. to propagate an event
     * up the model hierarchy.
     *
     * @param event   The original event object
     * @param handled {@code true} if at least one handler has been invoked already.
     *
     * @return {@code true} if at least one handler has been invoked and the event
     *                      was not cancelled.
     */
    protected boolean postFire( Event event, boolean handled ) {
        return handled;
    }

    /**
     * Prepare the event object to be passed to a listener: if the event
     * object implements {@link com.fastmodel.commons.event.IClonableEvent}, the event is cloned.
     *
     * @param event The fired event object
     * @return The instance of the event object to pass to the listener, or {@code null} if
     *          cloning failed.  {@code null} may also be returned to suppress handler invocation.
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
