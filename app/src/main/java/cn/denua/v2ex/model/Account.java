/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.model;

public class Account extends Member {

    private int balance;
    private int notifications;

    private int gold;
    private int silver;
    private int bronze;

    private int favorNodes;
    private int favorTopics;
    private int following;

    private String join;

    private int sign;
    private boolean isLogin = false;

    public Account(){
        super();
    }


    public boolean isLogin() {
        return isLogin;
    }

    public void logout(){
        isLogin = false;
    }

    public void login(){
        isLogin = true;
    }

    public Account(String username, String avatar_normal) {
        super(username, avatar_normal);
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getSilver() {
        return silver;
    }

    public void setSilver(int silver) {
        this.silver = silver;
    }

    public int getBronze() {
        return bronze;
    }

    public void setBronze(int bronze) {
        this.bronze = bronze;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getNotifications() {
        return notifications;
    }

    public void setNotifications(int notifications) {
        this.notifications = notifications;
    }

    public int getFavorNodes() {
        return favorNodes;
    }

    public void setFavorNodes(int favorNodes) {
        this.favorNodes = favorNodes;
    }

    public int getFavorTopics() {
        return favorTopics;
    }

    public void setFavorTopics(int favorTopics) {
        this.favorTopics = favorTopics;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }
}
