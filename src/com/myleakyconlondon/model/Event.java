package com.myleakyconlondon.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.myleakyconlondon.util.DateHelper;

import java.util.Date;

/**
 * User: Elizabeth
 * Date: 21/04/13
 * Time: 12:55
 */
public class Event implements Parcelable {

    private long eventId;
    private long backUpEventId;
    private long dayId;
    private long dayEndId;
    private String title, description, location, type;
    private Date startTime, endTime;
    private boolean isBackUpEvent;

    public Event() {
        title = "";
        description = "";
        location = "";
        type = "Other";
    }

    public Event(Parcel eventParcel) {
        readFromParcel(eventParcel);
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        public Event createFromParcel(Parcel parcel) {
            return new Event(parcel);
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public long getDayEndId() {
        return dayEndId;
    }

    public void setDayEndId(long dayEndId) {
        this.dayEndId = dayEndId;
    }

    public long getDayId() {
        return dayId;
    }

    public void setDayId(long dayId) {
        this.dayId = dayId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isBackUpEvent() {
        return isBackUpEvent;
    }

    public void setBackUpEvent(boolean backUpEvent) {
        isBackUpEvent = backUpEvent;
    }

    public long getBackUpEventId() {
        return backUpEventId;
    }

    public void setBackUpEventId(long backUpEventId) {
        this.backUpEventId = backUpEventId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0; 
    }
    
    @Override
    public void writeToParcel(Parcel eventParcel, int size) {

        eventParcel.writeLong(eventId);
        eventParcel.writeLong(backUpEventId);
        eventParcel.writeString(title);
        eventParcel.writeString(description);
        eventParcel.writeString(location);
        eventParcel.writeString(type);
        eventParcel.writeString(Boolean.toString(isBackUpEvent));
        eventParcel.writeString(startTime.toString());
        eventParcel.writeString(endTime.toString());
        eventParcel.writeLong(dayId);
        eventParcel.writeLong(dayEndId);
    }

    private void readFromParcel (Parcel eventParcel) {

        eventId = eventParcel.readLong();
        backUpEventId = eventParcel.readLong();
        title = eventParcel.readString();
        description = eventParcel.readString();
        location = eventParcel.readString();
        type = eventParcel.readString();
        isBackUpEvent = eventParcel.readString().equals("true");
        startTime = DateHelper.getFormattedDate(eventParcel.readString(), "EEE MMM dd HH:mm:ss z yyyy");
        endTime = DateHelper.getFormattedDate(eventParcel.readString(), "EEE MMM dd HH:mm:ss z yyyy");
        dayId = eventParcel.readLong();
        dayEndId = eventParcel.readLong();
    }
}
