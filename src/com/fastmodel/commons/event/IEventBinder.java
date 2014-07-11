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
 * An EventBinder is like an {@link IEventSocket}, except it binds all event listeners
 * declared on the specified object, rather than dealing in a single listener.
 *
 * @author Ben Schreiber
 * @version 1.0
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
    <ObjectType> ObjectType bindAll( ObjectType object );

    /**
     * Un-registers all listeners for the specified object previously registered with
     * {@link #bindAll(Object)}.
     *
     * @param object The object
     * @return {@code true} if any listeners were removed.
     */
    boolean unbindAll( Object object );
}
