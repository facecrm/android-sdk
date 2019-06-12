package com.facecrm.sample.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MemberResult implements Parcelable {
    @SerializedName("data")
    @Expose
    public DataMember dataMember;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("message")
    @Expose
    public String message;

    protected MemberResult(Parcel in) {
        dataMember = in.readParcelable(DataMember.class.getClassLoader());
        status = in.readInt();
    }

    public static final Creator<MemberResult> CREATOR = new Creator<MemberResult>() {
        @Override
        public MemberResult createFromParcel(Parcel in) {
            return new MemberResult(in);
        }

        @Override
        public MemberResult[] newArray(int size) {
            return new MemberResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(dataMember, flags);
        dest.writeInt(status);
    }

    public static class DataMember implements Parcelable {
        @SerializedName("token")
        @Expose
        public String token;
        @SerializedName("type")
        @Expose
        public int type;
        @SerializedName("lastname")
        @Expose
        public String lastname;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("firstname")
        @Expose
        public String firstname;
        @SerializedName("phone")
        @Expose
        public String phone;
        @SerializedName("id")
        @Expose
        public String member_id;
        @SerializedName("sex")
        @Expose
        public int sex;

        protected DataMember(Parcel in) {
            token = in.readString();
            type = in.readInt();
            lastname = in.readString();
            email = in.readString();
            firstname = in.readString();
            phone = in.readString();
            member_id = in.readString();
            sex = in.readInt();
        }

        public static final Creator<DataMember> CREATOR = new Creator<DataMember>() {
            @Override
            public DataMember createFromParcel(Parcel in) {
                return new DataMember(in);
            }

            @Override
            public DataMember[] newArray(int size) {
                return new DataMember[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(token);
            dest.writeInt(type);
            dest.writeString(lastname);
            dest.writeString(email);
            dest.writeString(firstname);
            dest.writeString(phone);
            dest.writeString(member_id);
            dest.writeInt(sex);
        }
    }
}

