package com.example.appanalyzer;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AppModel implements Parcelable {
        private String name, description;
        private double rating;
        private List<String> apps;
        private String iconURL;
        private List<String> screenShots;
        private String video;
        private String videoImage;
        private String androidVersion;
        private String developer;
        private String size;
        private String url;
        private String contentRating;
        private String installs;
        private String version;

    protected AppModel(Parcel in) {
        name = in.readString();
        description = in.readString();
        rating = in.readDouble();
        apps = in.createStringArrayList();
        iconURL = in.readString();
        screenShots = in.createStringArrayList();
        video = in.readString();
        videoImage = in.readString();
        androidVersion = in.readString();
        developer = in.readString();
        size = in.readString();
        url = in.readString();
        contentRating = in.readString();
        installs = in.readString();
        version = in.readString();
        appID = in.readString();
    }

    public static final Creator<AppModel> CREATOR = new Creator<AppModel>() {
        @Override
        public AppModel createFromParcel(Parcel in) {
            return new AppModel(in);
        }

        @Override
        public AppModel[] newArray(int size) {
            return new AppModel[size];
        }
    };

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    private String appID;




    public List<String> getScreenShots() {
        return screenShots;
    }

    public void setScreenShots(List<String> screenShots) {
        this.screenShots = screenShots;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentRating() {
        return contentRating;
    }

    public void setContentRating(String contentRating) {
        this.contentRating = contentRating;
    }

    public String getInstalls() {
        return installs;
    }

    public void setInstalls(String installs) {
        this.installs = installs;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public AppModel() {

        }

        public AppModel(String name, String description, String iconURL, int rating, List<String>apps) {
            this.name = name;
            this.description = description;
            this.rating = rating;
            this.apps = apps;
            this.iconURL = iconURL;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) { this.name = name; }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) { this.rating = rating; }

        public List<String> getApps() { return apps; }

        public void setApps(List<String> apps) { this.apps = apps; }

        public String getIconURL (){return iconURL;}

        public void setIconURL(String iconURL) {this.iconURL = iconURL;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeDouble(rating);
        parcel.writeStringList(apps);
        parcel.writeString(iconURL);
        parcel.writeStringList(screenShots);
        parcel.writeString(video);
        parcel.writeString(videoImage);
        parcel.writeString(androidVersion);
        parcel.writeString(developer);
        parcel.writeString(size);
        parcel.writeString(url);
        parcel.writeString(contentRating);
        parcel.writeString(installs);
        parcel.writeString(version);
        parcel.writeString(appID);
    }
}


