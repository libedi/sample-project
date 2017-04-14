package kr.co.poscoict.sample.common.exception;

public class ResourceNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 8908100543787414559L;
	
	public ResourceNotFoundException() {
        super();
    }
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
    public ResourceNotFoundException(Throwable cause) {
        super(cause);
    }
}
