package cn.denua.v2ex.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Account extends Member implements Parcelable {

    private int balance;
    private int notifications;

    private int favorNodes;
    private int favorTopics;
    private int following;

    private String join;

    private boolean login = false;

    public Account(){}

    public static Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source );
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    private Account(Parcel in){

        this.setId(in.readInt());
        this.setAvatar_mini(in.readString());
        this.setCreated(in.readLong());
        this.balance = in.readInt();
        this.notifications = in.readInt();
        this.favorNodes = in.readInt();
        this.favorTopics = in.readInt();
        this.following = in.readInt();
        this.join = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getAvatar_mini());
        dest.writeLong(getCreated());
        dest.writeInt(balance);
        dest.writeInt(notifications);
        dest.writeInt(favorNodes);
        dest.writeInt(favorTopics);
        dest.writeInt(following);
        dest.writeString(join);
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

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
}
