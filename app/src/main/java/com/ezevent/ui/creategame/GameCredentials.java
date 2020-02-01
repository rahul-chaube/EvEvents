package com.ezevent.ui.creategame;

import java.io.Serializable;

public class GameCredentials implements Serializable {
    String gameName,password;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
