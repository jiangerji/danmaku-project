package com.wanke.model;

public class ChannelInfo {

    private int roomId;
    private String roomCover;
    private String roomName;
    private String ownerNickName;
    private int gameId;
    private String gameName;
    private int fans;
    private int online;

    /**
     * @return the roomId
     */
    public int getRoomId() {
        return roomId;
    }

    /**
     * @param roomId
     *            the roomId to set
     */
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    /**
     * @return the roomCover
     */
    public String getRoomCover() {
        return roomCover;
    }

    /**
     * @param roomCover
     *            the roomCover to set
     */
    public void setRoomCover(String roomCover) {
        this.roomCover = roomCover;
    }

    /**
     * @return the roomName
     */
    public String getRoomName() {
        return roomName;
    }

    /**
     * @param roomName
     *            the roomName to set
     */
    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    /**
     * @return the ownerNickName
     */
    public String getOwnerNickName() {
        return ownerNickName;
    }

    /**
     * @param ownerNickName
     *            the ownerNickName to set
     */
    public void setOwnerNickName(String ownerNickName) {
        this.ownerNickName = ownerNickName;
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

    /**
     * @return the fans
     */
    public int getFans() {
        return fans;
    }

    /**
     * @param fans
     *            the fans to set
     */
    public void setFans(int fans) {
        this.fans = fans;
    }

    /**
     * @return the online
     */
    public int getOnline() {
        return online;
    }

    /**
     * @param online
     *            the online to set
     */
    public void setOnline(int online) {
        this.online = online;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("roomId:" + getRoomId() + " ");
        sb.append("roomName:" + getRoomName() + " ");
        sb.append("roomCover:" + getRoomCover() + " ");
        sb.append("gameId:" + getGameId() + " ");
        sb.append("gameName:" + getGameName() + " ");
        sb.append("wwnerNickName:" + getOwnerNickName() + " ");
        sb.append("fans:" + getFans() + " ");
        sb.append("online:" + getOnline() + " ");
        return sb.toString();
    }
}
