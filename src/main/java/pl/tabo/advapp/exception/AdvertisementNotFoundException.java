package pl.tabo.advapp.exception;

public class AdvertisementNotFoundException extends RuntimeException {

    public AdvertisementNotFoundException(String message) {
        super(message);
    }
}
