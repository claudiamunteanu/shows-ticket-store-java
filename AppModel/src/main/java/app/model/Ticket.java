package app.model;

import java.util.Objects;

public class Ticket extends Entity<Long>{
    private Show show;
    private String buyerName;
    private int noOfSeats;

    public Ticket(Show show, String buyerName, int noOfSeats) {
        this.show = show;
        this.buyerName = buyerName;
        this.noOfSeats = noOfSeats;
    }

    public Show getshow() {
        return show;
    }

    public void setshow(Show show) {
        this.show = show;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public int getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(int noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ticket)) return false;
        Ticket ticket = (Ticket) o;
        return show == ticket.show && noOfSeats == ticket.noOfSeats && buyerName.equals(ticket.buyerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(show, buyerName, noOfSeats);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + getId() +
                ", show=" + show +
                ", buyerName='" + buyerName + '\'' +
                ", noOfSeats=" + noOfSeats +
                '}';
    }
}
