package com.vsked.system.domain;

import java.time.LocalDateTime;

public class LogDateTime {

    private LocalDateTime dateTime;

    public LogDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
