package app.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Show extends Entity<Long>{
    private String artistName;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm", shape = JsonFormat.Shape.STRING)
    private LocalDateTime dateTime;
    private String place;
    private int availableSeats;
    private int soldSeats;

    public Show() {
    }

    public Show(String artistName, LocalDateTime dateTime, String place, int availableSeats, int soldSeats) {
        this.artistName = artistName;
        this.dateTime = dateTime;
        this.place = place;
        this.availableSeats = availableSeats;
        this.soldSeats = soldSeats;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

//    @JsonFormat(pattern = "yyyy-MM-ddTHH:mm",shape = JsonFormat.Shape.STRING)
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getSoldSeats() {
        return soldSeats;
    }

    public void setSoldSeats(int soldSeats) {
        this.soldSeats = soldSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Show)) return false;
        Show show = (Show) o;
        return availableSeats == show.availableSeats && soldSeats == show.soldSeats && artistName.equals(show.artistName) && dateTime.equals(show.dateTime) && place.equals(show.place);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artistName, dateTime, place, availableSeats, soldSeats);
    }

    @Override
    public String toString() {
        return "Show{" +
                "id=" + getId() +
                ", artistName='" + artistName + '\'' +
                ", dateTime=" + dateTime +
                ", place='" + place + '\'' +
                ", availableSeats=" + availableSeats +
                ", soldSeats=" + soldSeats +
                '}';
    }
}
