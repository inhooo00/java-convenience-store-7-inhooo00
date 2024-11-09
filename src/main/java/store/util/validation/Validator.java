package store.util.validation;

public interface Validator<T> {
    void validate(T target);
}