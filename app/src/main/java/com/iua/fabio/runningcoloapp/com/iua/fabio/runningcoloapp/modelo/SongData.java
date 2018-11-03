package com.iua.fabio.runningcoloapp.com.iua.fabio.runningcoloapp.modelo;

import java.io.Serializable;

public class SongData implements Serializable {
    private String song_title;
    private String album;
    private String artist;
    private String cover;
    private String song_id;

    public SongData(String song_title, String album, String artist, String cover, String song_id) {
        this.song_title = song_title;
        this.album = album;
        this.artist = artist;
        this.cover = cover;
        this.song_id = song_id;
    }

    public String getSong_title() {
        return song_title;
    }

    public void setSong_title(String song_title) {
        this.song_title = song_title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    @Override
    public String toString() {
        return "SongData{" +
                "song_title='" + song_title + '\'' +
                ", album='" + album + '\'' +
                ", artist='" + artist + '\'' +
                ", cover='" + cover + '\'' +
                ", song_id='" + song_id + '\'' +
                '}';
    }
}
