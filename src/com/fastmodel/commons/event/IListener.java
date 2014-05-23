package com.fastmodel.commons.event;

/**
 * A Listener for an event
 */
public interface IListener< Event extends IEvent > {
    void handle( Event event );
}
