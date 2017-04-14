package kr.co.poscoict.sample.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import kr.co.poscoict.sample.common.exception.ResourceConflictException;
import kr.co.poscoict.sample.common.exception.ResourceNotFoundException;
import kr.co.poscoict.sample.common.model.ErrorResponse;
import kr.co.poscoict.sample.common.util.MessageSourceUtil;

/**
 * REST API Exception handler
 * @author libedi
 *
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
	
	@Autowired
	private MessageSourceUtil messageSource;
	
	/**
	 * 204 처리 (NO CONTENT)
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = {ResourceNotFoundException.class})
	protected ResponseEntity<Object> handleNoContent(RuntimeException e, WebRequest request) {
		logger.error(e.getMessage(), e);
		return super.handleExceptionInternal(e, new ErrorResponse(messageSource.getMessage("api.error.notFound")),
				new HttpHeaders(), HttpStatus.NO_CONTENT, request);
	}
	
	/**
	 * 400 처리 (BAD_REQUEST)
	 * @param e
	 * @param request
	 * @return
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		logger.error(e.getMessage(), e);
		return super.handleExceptionInternal(e,
				new ErrorResponse(e.getBindingResult().getFieldError().getField(), e.getBindingResult().getFieldError().getDefaultMessage()),
				new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	/**
	 * 409 처리 (CONFLICT)
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = {ResourceConflictException.class})
	protected ResponseEntity<Object> handleConflict(RuntimeException e, WebRequest request) {
		logger.error(e.getMessage(), e);
		return super.handleExceptionInternal(e, new ErrorResponse(messageSource.getMessage("api.error.conflict")),
				new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
	
	/**
	 * 500 처리 (INTERNAL_SERVER_ERROR)
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler(value = {Exception.class})
	protected ResponseEntity<Object> handleInternalServerError(RuntimeException e, WebRequest request) {
		logger.error(e.getMessage(), e);
		return super.handleExceptionInternal(e, new ErrorResponse(messageSource.getMessage("api.error.serverError")),
				new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
