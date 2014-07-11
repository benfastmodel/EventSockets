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
 * An Event Service which can expose multiple sockets for different
 * related event types.  Events dispatched by implementations of this service
 * are only sent to listeners registered on sockets for compatible types.
 *
 * A Multi-Event Service is an efficient way to maintain a single dispatcher
 * for a type which exposes multiple event types.
 *
 * @author Ben Schreiber
 * @version 1.0
 */
public interface IMultiEventService< Event extends IEvent > extends IEventService< Event > {

    /**
     * Get a socket for registering handlers for a specific event type.
     * This method is used to ensure type-safe event registration and dispatching
     * in spite of Erasure.
     *
     * @param eventClass The target event type's class object
     * @param <SocketEvent> The target event type
     *
     * @return The requested socket
     */
    <SocketEvent extends Event> IEventSocket< SocketEvent > getSocket( Class< SocketEvent > eventClass );
}
