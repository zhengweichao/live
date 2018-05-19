package top.vchao.live.pro.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @ Create_time: 2018/5/18 on 9:34.
 * @ description：Parcelable 练习
 * @ author: vchao  blog: http://blog.csdn.net/zheng_weichao
 */
public class User implements Parcelable {
    private String username;
    private String other;
    private ArrayList<String> strings;
    private boolean sex;
    private int age;
    private List<Car> data;

    public List<Car> getData() {
        return data;
    }

    public void setData(List<Car> data) {
        this.data = data;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public ArrayList<String> getStrings() {
        return strings;
    }

    public void setStrings(ArrayList<String> strings) {
        this.strings = strings;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public User(String username, String other, ArrayList<String> strings, boolean sex, int age, List<Car> data) {
        this.username = username;
        this.other = other;
        this.strings = strings;
        this.sex = sex;
        this.age = age;
        this.data = data;
    }

    protected User(Parcel in) {
        username = in.readString();
        other = in.readString();
        strings = in.createStringArrayList();
        sex = in.readByte() != 0;
        age = in.readInt();
        data = in.createTypedArrayList(Car.CREATOR);
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
        dest.writeString(username);
        dest.writeString(other);
        dest.writeStringList(strings);
        dest.writeByte((byte) (sex ? 1 : 0));
        dest.writeInt(age);
        dest.writeTypedList(data);
    }


    public static class Car implements Parcelable {

        private String brand;

        public Car(String brand) {
            this.brand = brand;
        }

        protected Car(Parcel in) {
            brand = in.readString();
        }

        public static final Creator<Car> CREATOR = new Creator<Car>() {
            @Override
            public Car createFromParcel(Parcel in) {
                return new Car(in);
            }

            @Override
            public Car[] newArray(int size) {
                return new Car[size];
            }
        };

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(brand);
        }
    }

}
