package com.example.finnkino;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Movie {
    private Integer movieId;
    private String movieName;
    private String theatre;
    private String auditorium;
    private String picture;
    private Date startTime;
    private Date endTime;

    public Movie(Integer MovieId, String movieName, String theatre, String auditorium, Date startTime, Date endTime, String picture) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.theatre = theatre;
        this.auditorium = auditorium;
        this.startTime = startTime;
        this.endTime = endTime;
        this.picture = picture;
    }
    public void setPicture(String picture) { this.picture = picture; }

    public Integer getMovieId() {
        return movieId;
    }
    public String getMovieName() {
        return movieName;
    }
    public String getTheatre() {
        return theatre;
    }
    public String getAuditorium() {
        return auditorium;
    }
    public String getPicture() {
        return picture;
    }
    public String getStartTimeString() {
        return new SimpleDateFormat("HH:mm").format(startTime);
    }
    public String getEndTimeString() {
        return new SimpleDateFormat("HH:mm").format(endTime);
    };
    public Date getStartTime() {
        return startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
}
