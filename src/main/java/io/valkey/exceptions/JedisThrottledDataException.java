package io.valkey.exceptions;

public class JedisThrottledDataException extends JedisDataException {

    private static final long serialVersionUID = 3900401231195549148L;

    public JedisThrottledDataException(String message) {
        super(message);
    }

    public JedisThrottledDataException(Throwable cause) {
        super(cause);
    }

    public JedisThrottledDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
