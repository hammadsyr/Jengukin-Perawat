package com.example.affereaflaw.ux;

/**
 * Created by Affe Reaflaw on 10/6/2017.
 */
public class profilGetSet {
    private String nama, noKamar, kode, image;

    public profilGetSet(String nama, String noKamar, String kode, String image) {
        this.nama = nama;
        this.noKamar = noKamar;
        this.kode = kode;
        this.image = image;
    }

    public profilGetSet(){

    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNoKamar() {
        return noKamar;
    }

    public void setNoKamar(String noKamar) {
        this.noKamar = noKamar;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
