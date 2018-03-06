package com.udacity.popularmovie.event;

/**
 * Created by Antonio on 05/03/2018.
 */

public class ClickMenuEvent {
    private int serviceRequested;

    public ClickMenuEvent(int serviceRequested) {
        this.serviceRequested = serviceRequested;
    }

    public int getServiceRequested() {
        return serviceRequested;
    }
}
