package com.example.affereaflaw.ux;

/**
 * Created by Affe Reaflaw on 10/21/2017.
 */
public class MessagesGetSet {
    private String pesan;
    private String from;

    public MessagesGetSet(String pesan, String from) {
        this.pesan = pesan;
        this.from = from;
    }

    public MessagesGetSet(){}

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
