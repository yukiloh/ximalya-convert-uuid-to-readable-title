package com.example.demo;

public class TrackInfo {

    private String trackID;
    private String trackTitle;
    private String albumTitle;

    @Override
    public String toString() {
        return "TrackInfo{" +
                "trackID=" + trackID +
                ", trackTitle='" + trackTitle + '\'' +
                ", albumTitle='" + albumTitle + '\'' +
                '}';
    }

    public TrackInfo() {
    }

    public TrackInfo(String trackID, String trackTitle, String albumTitle) {
        this.trackID = trackID;
        this.trackTitle = trackTitle;
        this.albumTitle = albumTitle;
    }

    public String getTrackID() {
        return trackID;
    }

    public void setTrackID(String trackID) {
        this.trackID = trackID;
    }

    public String getTrackTitle() {
        return trackTitle;
    }

    public void setTrackTitle(String trackTitle) {
        this.trackTitle = trackTitle;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }
}
