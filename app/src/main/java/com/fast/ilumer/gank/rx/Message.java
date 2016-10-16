package com.fast.ilumer.gank.rx;

/**
 * Created by root on 10/15/16.
 *
 */

public class Message {
    private String flag;
    private Object object;

    public Message(String code, Object o) {
        this.flag = code;
        this.object = o;
    }


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
