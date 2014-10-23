package com.wanke.model;

public class AccountInfo {

    private String uid;
    private String username;
    private String email;
    private long exp;

    /**
     * @return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid
     *            the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the exp
     */
    public long getExp() {
        return exp;
    }

    /**
     * @param exp
     *            the exp to set
     */
    public void setExp(long exp) {
        this.exp = exp;
    }

}
