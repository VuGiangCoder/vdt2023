package com.viettel.vdt2023.gitlab.api.webhook;


import com.viettel.vdt2023.gitlab.api.utils.JacksonJson;

public class ChangeContainer<T> {
    
    private T previous;
    private T current;

    public T getPrevious() {
        return previous;
    }

    public void setPrevious(T previous) {
        this.previous = previous;
    }

    public T getCurrent() {
        return current;
    }

    public void setCurrent(T current) {
        this.current = current;
    }

    @Override
    public String toString() {
        return (JacksonJson.toJsonString(this));
    }
}
