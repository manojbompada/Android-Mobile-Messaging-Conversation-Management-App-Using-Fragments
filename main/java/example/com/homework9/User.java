/*
    Cole Howell, Manoj Bompada
    ITCS 4180
    User.java
 */

package example.com.homework9;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by colehowell on 4/23/16.
 */
public class User implements Parcelable {

    String fullname = "", email = "", password = "", picture = "", hasUnreadMsg="true",phoneno;

    public String getHasUnreadMsg() {
        return hasUnreadMsg;
    }

    public void setHasUnreadMsg(String hasUnreadMsg) {
        this.hasUnreadMsg = hasUnreadMsg;
    }

    public User() {

    }

    public User(String fullname, String email, String password, String picture, String phoneno) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.picture = picture;
        this.phoneno = phoneno;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullname='" + fullname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", picture='" + picture + '\'' +
                ", phoneno=" + phoneno +
                '}';
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeString(fullname);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(picture);
        dest.writeString(phoneno);


    }

    protected User(Parcel in) {

        fullname = in.readString();
        email = in.readString();
        password = in.readString();
        picture = in.readString();
        phoneno = in.readString();


    }
}
