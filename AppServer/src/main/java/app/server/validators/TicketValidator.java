package app.server.validators;

import app.model.Ticket;
import app.services.AppException;

import java.util.List;

public class TicketValidator implements Validator<Ticket>{
    @Override
    public void validate(List<String> data) throws AppException {
        String errors = "";

        if (data.get(0).equals("")) {
            errors = errors + "You have to select a show!\n";
        }

        if(data.get(1).equals("")){
            errors = errors + "Buyer name cannot be empty!\n";
        }

        if (!errors.equals("")) {
            throw new AppException(errors);
        }
    }
}
