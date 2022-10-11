package app.network.dto;

import app.model.Entity;

import java.util.Objects;

public class ShowDTO extends Entity<Long> {
    String artistName;
    String date;
    String place;
    String availableSeats;
    String hour;

    public ShowDTO(String artistName, String date, String place, String availableSeats, String hour) {
        this.artistName = artistName;
        this.date = date;
        this.place = place;
        this.availableSeats = availableSeats;
        this.hour = hour;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(String availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShowDTO)) return false;
        ShowDTO showDTO = (ShowDTO) o;
        return artistName.equals(showDTO.artistName) && date.equals(showDTO.date) && place.equals(showDTO.place) && availableSeats.equals(showDTO.availableSeats) && hour.equals(showDTO.hour);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistName, date, place, availableSeats, hour);
    }

    @Override
    public String toString() {
        return "ShowDTO{" +
                "artistName='" + artistName + '\'' +
                ", date='" + date + '\'' +
                ", place='" + place + '\'' +
                ", availableSeats='" + availableSeats + '\'' +
                ", hour='" + hour + '\'' +
                '}';
    }
}