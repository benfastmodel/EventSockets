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

import java.lang.annotation.*;

/**
 * Annotation which can be applied to event listeners, for annotation-based event binding.
 * This annotation can only be applied to methods, and the method must have only a single
 * parameter, whose type determines the event type for which it will be registered.  The
 * return type doesn't matter, and any return value will be ignored.
 *
 * @author Ben Schreiber
 * @version 1.0
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
