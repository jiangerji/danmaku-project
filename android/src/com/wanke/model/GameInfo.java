package com.wanke.model;

public class GameInfo {

    private int gameId;
    private String gameName;
    private String gameCover;

    /**
     * @return the gameCover
     */
    public String getGameCover() {
        return gameCover;
    }

    /**
     * @param gameCover
     *            the gameCover to set
     */
    public void setGameCover(String gameCover) {
        this.gameCover = gameCover;
    }

    /**
     * @return the gameId
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * @param gameId
     *            the gameId to set
     */
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    /**
     * @return the gameName
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * @param gameName
     *            the gameName to set
     */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("gameName:" + getGameName() + " ");
        sb.append("gameId:" + getGameId() + " ");
        sb.append("gameCover:" + getGameCover() + " ");
        return sb.toString();
    }

}
