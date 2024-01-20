package com.example.world;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Person implements Parcelable {
    private int age;
    private String name;
    public Person(){}

    protected Person(Parcel in) {
        age = in.readInt();
        name = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(age);
        dest.writeString(name);
    }
}
