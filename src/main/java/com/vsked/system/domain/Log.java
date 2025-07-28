package com.vsked.system.domain;

public class Log {
    private LogId id;
    private LongContent  content;
    private LogDateTime dateTime;

    public Log(LogId id, LogDateTime dateTime, LongContent content) {
        this.id = id;
        this.dateTime = dateTime;
        this.content = content;
    }

    public LogId getId() {
        return id;
    }

    public LongContent getContent() {
        return content;
    }

    public LogDateTime getDateTime() {
        return dateTime;
    }
}
