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
 * A common base class for Cancellable events
 *
 * @author Ben Schreiber
 * @version 1.0
 */
abstract public class AbstractCancelableEvent implements ICancelableEvent {

	private boolean cancelled = false;

	/**
	 * @return {@code true} if event notification / propagation should be canceled.
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Cancels (or un-cancels) the event
	 *
	 * @param cancelled {@code true} to cancel event propagation
	 */
	public void setCancelled( boolean cancelled ) {
		this.cancelled = cancelled;
	}

	/**
	 * Cancels any additional processing for this event object
	 */
	public void cancel() {
		setCancelled( true );
	}
}
