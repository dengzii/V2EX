package cn.denua.v2ex.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Account extends Member implements Parcelable {

    public int number;
    public int balance;
    public int notifications;

    public int favorNodes;
    public int favorTopics;
    public int following;

    public String join;

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

        this.id = in.readString();
        this.picUrl = in.readString();
        this.number = in.readInt();
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
        dest.writeString(id);
        dest.writeString(picUrl);
        dest.writeInt(number);
        dest.writeInt(balance);
        dest.writeInt(notifications);
        dest.writeInt(favorNodes);
        dest.writeInt(favorTopics);
        dest.writeInt(following);
        dest.writeString(join);
    }
}
