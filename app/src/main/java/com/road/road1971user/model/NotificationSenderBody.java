package com.road.road1971user.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationSenderBody {
    @SerializedName("to")
    @Expose
    private String to;
//    @SerializedName("notification")
//    @Expose
//    private NotificationData notificationData;

    @SerializedName("data")
    @Expose
    private NotificationData data;

    public NotificationSenderBody(String token, NotificationData notificationData) {
        this.to = token;
        // this.notificationData = notificationData;
        this.data = notificationData;
    }

    public NotificationData getData() {
        return data;
    }

    public void setData(NotificationData data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
