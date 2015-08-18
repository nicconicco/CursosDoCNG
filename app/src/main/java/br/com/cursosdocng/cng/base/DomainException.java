package br.com.cursosdocng.cng.base;

/**
 * Created by Carlos Nicolau Galves on 8/17/15.
 */
public class DomainException extends Exception {
    private static final long serialVersionUID = 7419997425845289500L;
    private String key;

    public DomainException(String message) {
        super(message);
        this.key = message;
    }

    public DomainException(String message, Throwable e) {
        super(message, e);
    }

    public String getKey() {
        return this.key;
    }
}
