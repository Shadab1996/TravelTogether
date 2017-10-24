package com.stinkinsweet.traveltogether;

/**
 * Created by Funkies PC on 17-Dec-16.
 */
public class Message {
    //name and address string
    private String Name;
    private String Message;


    public Message() {
      /*Blank default constructor essential for Firebase*/
    }
    //Getters and setters

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
