package app.server.validators;

import app.services.AppException;

import java.util.List;

public interface Validator<T> {
    void validate(List<String> data) throws AppException;
}