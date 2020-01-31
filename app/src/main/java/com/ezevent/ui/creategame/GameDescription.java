package com.ezevent.ui.creategame;

import java.io.Serializable;
import java.util.List;

public class GameDescription implements Serializable {
    String gameId;
    String title,description,price;
    boolean isPrivate,isPubg;
    long createAt;
    int numberOfPlayer,gameStatus=0;

    public int getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(int gameStatus) {
        this.gameStatus = gameStatus;
    }

    GameCreator creator;
    List<GameCreator> gamerList;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isPubg() {
        return isPubg;
    }

    public void setPubg(boolean pubg) {
        isPubg = pubg;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }

    public void setNumberOfPlayer(int numberOfPlayer) {
        this.numberOfPlayer = numberOfPlayer;
    }

    public GameCreator getCreator() {
        return creator;
    }

    public void setCreator(GameCreator creator) {
        this.creator = creator;
    }

    public List<GameCreator> getGamerList() {
        return gamerList;
    }

    public void setGamerList(List<GameCreator> gamerList) {
        this.gamerList = gamerList;
    }
}
