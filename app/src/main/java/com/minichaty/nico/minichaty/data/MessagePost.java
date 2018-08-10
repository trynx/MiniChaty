package com.minichaty.nico.minichaty.data;

/**
 * This class is used to send the message as an object for easier send
 * the data to firebase
 */
public class MessagePost {
    private String message;

    public MessagePost(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
