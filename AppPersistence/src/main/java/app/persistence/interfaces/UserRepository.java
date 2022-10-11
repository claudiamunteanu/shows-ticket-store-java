package app.persistence.interfaces;

import app.model.User;

public interface UserRepository extends Repository<Long, User>{
    /**
     * @param username - the username of the user
     * @param password - the password of the account
     *           date and passowrd must not be null
     * @return the user, or null if the user doesn't exist
     * @throws IllegalArgumentException if username or password is null.
     */
    User getUser(String username, String password);
}
