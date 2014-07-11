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
 * Event Sockets are the public interface for registering & de-registering
 * listeners for a specific event type.
 *
 * @author Ben Schreiber
 * @version 1.0
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
    IListener< Event > bind( IListener<Event> listener );

    /**
     * Remove a previously registered listener from this Event.
     *
     * @param listener The listener to remove
     * @return {@code true} if the specified listener was actually removed.
     */
    boolean unbind( IListener<Event> listener );
}
