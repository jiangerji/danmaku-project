package com.wanke.model;

public class HistoryInfo {

    /**
     * 
     * @author Administrator
     * 
     */
    private String roomId;
    private String roomName;
    private String ownerNickname;
    private String gameName;
    private int fans;

    /**
     * @return the roomId
     */
    public String getRoomId() {
        return roomId;
    }

    /**
     * @param roomId
     *            the roomId to set
     */
    public void setRoomId(String roomId) {
        this.roomId = roomId;
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
     * @return the ownerNickname
     */
    public String getOwnerNickname() {
        return ownerNickname;
    }

    /**
     * @param ownerNickname
     *            the ownerNickname to set
     */
    public void setOwnerNickname(String ownerNickname) {
        this.ownerNickname = ownerNickname;
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

}
