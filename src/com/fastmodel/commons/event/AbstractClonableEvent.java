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
 * A base implementation for Event objects which should be cloned before being passed
 * to each listener, so that the original event object remains untouched.  This
 * implementation simply delegates the cloning to {@link Object#clone()}, which
 * performs a simple shallow cloning.
 *
 * @author Ben Schreiber
 * @version 1.0
 */
abstract public class AbstractClonableEvent implements IClonableEvent {

	/**
	 * Perform a simple shallow cloning of the object, by delegating to {@link Object#clone()}.
	 *
	 * @return A clone of the event object
	 */
	public AbstractClonableEvent clone() throws CloneNotSupportedException {
		return (AbstractClonableEvent) super.clone();
	}
}
