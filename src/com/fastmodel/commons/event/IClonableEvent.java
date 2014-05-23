package com.fastmodel.commons.event;

/**
 * A tagging interface for Event objects which should be cloned before being passed
 * to each listener, so that the original event object remains untouched.
 */
public interface IClonableEvent extends IEvent, Cloneable {

    /**
     * A cloneable event must provide a public implementation for {@link Object#clone()}.
     *
     * @return A clone of the event object
     */
    public IClonableEvent clone() throws CloneNotSupportedException;
}
