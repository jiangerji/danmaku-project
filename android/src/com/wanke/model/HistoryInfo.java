package com.wanke.model;

public class HistoryInfo {

    /**
     * 
     * @author Administrator
     * 
     */

    private String number;
    private String name;
    private String gamename;
    private String videoname;

    public HistoryInfo(String number, String name, String gamename,
            String videoname) {
        this.number = number;
        this.name = name;
        this.gamename = gamename;
        this.videoname = videoname;
    }

    public String getVideoname() {
        return videoname;
    }

    public void setVideoname(String videoname) {
        this.videoname = videoname;
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BlackNumberInfo [number=" + number + "videoname=" + videoname
                + ", gamename=" + gamename
                + ", name=" + name + "]";
    }

}