package com.fastmodel.commons.event;

/**
 * An event whose propagation can be cancelled by a handler.
 */
public interface ICancelableEvent extends IEvent {

    /**
     * @return {@code true} if event notification / propagation should be canceled.
     */
    boolean isCancelled();
}
