package com.example.affereaflaw.ux;

/**
 * Created by Affe Reaflaw on 10/9/2017.
 */
public class SuggestionObatGetSet {
    private String Suggestion, Obat;

    public SuggestionObatGetSet(String suggestion, String obat) {
        Suggestion = suggestion;
        Obat = obat;
    }

    public SuggestionObatGetSet(){

    }

    public String getSuggestion() {
        return Suggestion;
    }

    public void setSuggestion(String suggestion) {
        Suggestion = suggestion;
    }

    public String getObat() {
        return Obat;
    }

    public void setObat(String obat) {
        Obat = obat;
    }
}
