package com.yoloo.android.feature.auth;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

public final class InfoBundle implements Parcelable {
  public static final Creator<InfoBundle> CREATOR = new Creator<InfoBundle>() {
    @Override
    public InfoBundle createFromParcel(Parcel in) {
      return new InfoBundle(in);
    }

    @Override
    public InfoBundle[] newArray(int size) {
      return new InfoBundle[size];
    }
  };

  private final String name;
  private final String surname;
  private final String email;
  private final String username;
  private final String password;
  private final long birthday;

  public InfoBundle(String name, String surname, String email, String username, String password,
      long birthday) {
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.username = username;
    this.password = password;
    this.birthday = birthday;
  }

  private InfoBundle(Parcel in) {
    name = in.readString();
    surname = in.readString();
    email = in.readString();
    username = in.readString();
    password = in.readString();
    birthday = in.readLong();
  }

  public String getName() {
    return name;
  }

  public String getSurname() {
    return surname;
  }

  public String getEmail() {
    return email;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public long getBirthday() {
    return birthday;
  }

  public Date getBirthdayAsDate() {
    return new Date(birthday);
  }

  public String getFullname() {
    return name + " " + surname;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(name);
    dest.writeString(surname);
    dest.writeString(email);
    dest.writeString(username);
    dest.writeString(password);
    dest.writeLong(birthday);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public String toString() {
    return "InfoBundle{"
        + "name='"
        + name
        + '\''
        + ", surname='"
        + surname
        + '\''
        + ", email='"
        + email
        + '\''
        + ", username='"
        + username
        + '\''
        + ", password='"
        + password
        + '\''
        + ", birthday="
        + birthday
        + '}';
  }
}