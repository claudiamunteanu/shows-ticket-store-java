package app.server.validators;

import app.model.User;
import app.services.AppException;

import java.util.List;

public class UserValidator implements Validator<User>{
    @Override
    public void validate(List<String> data) throws AppException {
        String errors = "";

        if (data.get(0).equals("")) {
            errors = errors + "The username cannot be empty!\n";
        }

        if(data.get(1).equals("")){
            errors = errors + "The password cannot be empty!\n";
        }

        if (!errors.equals("")) {
            throw new AppException(errors);
        }
    }
}
