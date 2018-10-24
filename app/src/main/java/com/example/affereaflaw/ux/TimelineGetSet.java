package com.example.affereaflaw.ux;

/**
 * Created by Affe Reaflaw on 10/22/2017.
 */
public class TimelineGetSet {
    private String Timeline, Kategori, Time, Image;

    public TimelineGetSet(){

    }

    public TimelineGetSet(String timeline, String kategori, String time, String image) {
        Timeline = timeline;
        Kategori = kategori;
        Time = time;
        Image = image;
    }

    public String getTimeline() {
        return Timeline;
    }

    public void setTimeline(String timeline) {
        Timeline = timeline;
    }

    public String getKategori() {
        return Kategori;
    }

    public void setKategori(String kategori) {
        Kategori = kategori;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
