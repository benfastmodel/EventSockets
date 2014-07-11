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

import java.lang.reflect.Method;

/**
 * Exception type which is used to signal an annotation-based listener which
 * can't be bound to the event service.
 *
 * @author Ben Schreiber
 * @version 1.0
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
