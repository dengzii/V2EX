/*
 * Copyright (c) 2018 denua.
 */

package cn.denua.v2ex.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Account extends Member implements Serializable, Parcelable, Cloneable {

    private static final long serialVersionUID  = 1L;

    private int balance;
    private int notifications;

    private int gold;
    private int silver;
    private int bronze;

    private int favorNodes;
    private int favorTopics;
    private int following;

    private String join;

    public Account(){
        super();
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

    @Override
    public Object clone() {
        return super.clone();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.balance);
        dest.writeInt(this.notifications);
        dest.writeInt(this.gold);
        dest.writeInt(this.silver);
        dest.writeInt(this.bronze);
        dest.writeInt(this.favorNodes);
        dest.writeInt(this.favorTopics);
        dest.writeInt(this.following);
        dest.writeString(this.join);
        dest.writeInt(this.id);
        dest.writeString(this.username);
        dest.writeString(this.number);
        dest.writeString(this.avatar_normal);
        dest.writeString(this.status);
        dest.writeString(this.avatar_mini);
        dest.writeString(this.avatar_large);
        dest.writeString(this.website);
        dest.writeString(this.github);
        dest.writeString(this.psn);
        dest.writeString(this.twitter);
        dest.writeString(this.bio);
        dest.writeString(this.tagline);
        dest.writeLong(this.created);
        dest.writeString(this.location);
        dest.writeString(this.btc);
    }

    protected Account(Parcel in) {
        super(in);
        this.balance = in.readInt();
        this.notifications = in.readInt();
        this.gold = in.readInt();
        this.silver = in.readInt();
        this.bronze = in.readInt();
        this.favorNodes = in.readInt();
        this.favorTopics = in.readInt();
        this.following = in.readInt();
        this.join = in.readString();
        this.id = in.readInt();
        this.username = in.readString();
        this.number = in.readString();
        this.avatar_normal = in.readString();
        this.status = in.readString();
        this.avatar_mini = in.readString();
        this.avatar_large = in.readString();
        this.website = in.readString();
        this.github = in.readString();
        this.psn = in.readString();
        this.twitter = in.readString();
        this.bio = in.readString();
        this.tagline = in.readString();
        this.created = in.readLong();
        this.location = in.readString();
        this.btc = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
