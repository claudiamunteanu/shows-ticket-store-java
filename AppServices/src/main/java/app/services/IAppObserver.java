package app.services;

import app.model.Ticket;

public interface IAppObserver {
    void ticketBought(Long[] ticketData) throws AppException;
}
