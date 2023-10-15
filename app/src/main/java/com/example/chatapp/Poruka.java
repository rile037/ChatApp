package com.example.chatapp;

import android.app.Activity;

public class Poruka {
    String emailPosiljaoca, emailPrimaoca, poruka;

    public Poruka(String emailPosiljaoca, String emailPrimaoca, String poruka) {
        this.emailPosiljaoca = emailPosiljaoca;
        this.emailPrimaoca = emailPrimaoca;
        this.poruka = poruka;
    }

    public String getEmailPosiljaoca() {
        return emailPosiljaoca;
    }

    public void setEmailPosiljaoca(String emailPosiljaoca) {
        this.emailPosiljaoca = emailPosiljaoca;
    }

    public String getEmailPrimaoca() {
        return emailPrimaoca;
    }

    public void setEmailPrimaoca(String emailPrimaoca) {
        this.emailPrimaoca = emailPrimaoca;
    }

    public String getPoruka() {
        return poruka;
    }

    public void setPoruka(String poruka) {
        this.poruka = poruka;
    }
}
