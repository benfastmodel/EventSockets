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
 * An Event Service handles dispatching of events of the specified type,
 * and provides the Event Sockets on which listeners can be registered.
 *
 * @param <Event> The root event type dispatched by this Event Service.
 *                Actual event objects may of course extend this type.
 *
 * @author Ben Schreiber
 * @version 1.0
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
