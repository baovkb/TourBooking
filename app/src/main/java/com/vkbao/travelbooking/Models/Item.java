package com.vkbao.travelbooking.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Item implements Parcelable {
    private String item_id;
    private String category_id;
    private String location_id;
    private String address;
    private String banner;
    private String dateTour;
    private String description;
    private String duration;
    private int price;
    private String title;
    private String tourGuideName;
    private String tourGuidePhone;
    private String tourGuidePic;

    public Item() {
    }

    public Item(String item_id, String category_id, String location_id, String address, String banner, String dateTour, String description, String duration, int price, String title, String tourGuideName, String tourGuidePhone, String tourGuidePic) {
        this.item_id = item_id;
        this.category_id = category_id;
        this.location_id = location_id;
        this.address = address;
        this.banner = banner;
        this.dateTour = dateTour;
        this.description = description;
        this.duration = duration;
        this.price = price;
        this.title = title;
        this.tourGuideName = tourGuideName;
        this.tourGuidePhone = tourGuidePhone;
        this.tourGuidePic = tourGuidePic;
    }

    protected Item(Parcel in) {
        item_id = in.readString();
        category_id = in.readString();
        location_id = in.readString();
        address = in.readString();
        banner = in.readString();
        dateTour = in.readString();
        description = in.readString();
        duration = in.readString();
        price = in.readInt();
        title = in.readString();
        tourGuideName = in.readString();
        tourGuidePhone = in.readString();
        tourGuidePic = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getDateTour() {
        return dateTour;
    }

    public void setDateTour(String dateTour) {
        this.dateTour = dateTour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTourGuideName() {
        return tourGuideName;
    }

    public void setTourGuideName(String tourGuideName) {
        this.tourGuideName = tourGuideName;
    }

    public String getTourGuidePhone() {
        return tourGuidePhone;
    }

    public void setTourGuidePhone(String tourGuidePhone) {
        this.tourGuidePhone = tourGuidePhone;
    }

    public String getTourGuidePic() {
        return tourGuidePic;
    }

    public void setTourGuidePic(String tourGuidePic) {
        this.tourGuidePic = tourGuidePic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(item_id);
        parcel.writeString(category_id);
        parcel.writeString(location_id);
        parcel.writeString(address);
        parcel.writeString(banner);
        parcel.writeString(dateTour);
        parcel.writeString(description);
        parcel.writeString(duration);
        parcel.writeInt(price);
        parcel.writeString(title);
        parcel.writeString(tourGuideName);
        parcel.writeString(tourGuidePhone);
        parcel.writeString(tourGuidePic);
    }
}
