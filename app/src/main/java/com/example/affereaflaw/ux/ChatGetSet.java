package com.example.affereaflaw.ux;

/**
 * Created by Affe Reaflaw on 10/23/2017.
 */
public class ChatGetSet {
    private String nama, username;

    public ChatGetSet(String nama, String username) {
        this.nama = nama;
        this.username = username;
    }
    public ChatGetSet(){

    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
