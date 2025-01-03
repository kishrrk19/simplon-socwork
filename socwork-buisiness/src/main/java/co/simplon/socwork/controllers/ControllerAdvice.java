package co.simplon.socwork.controllers;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BadCredentialsException.class)
	protected ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, WebRequest request){
		return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
	}
	//c'est quoi SSH
	//3 parties de JWT, encode car alleger taille de JWT, reversible, 2 algorithemes
	//JWT est un format de token, c'est pas mode d'authenitification
	//faille XXS
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode status,
			WebRequest request){
		return super.handleExceptionInternal(ex, body, null, status, request);
	}
	
	//quand on cree un account avec le meme nom par exemple, la validation cote db ne passe pas et il y a un exception qui se lance
	//@ExceptionHandlerを使い、そこに指定した例外（今回の場合DataAccessException）が発生した時、このメソッドが呼ばれるという仕組み。
	@ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<Object> handleDataAccessException(
        DataAccessException ex, WebRequest request) {
    return handleExceptionInternal(ex, null, new HttpHeaders(),
        HttpStatus.CONFLICT, request);
    }
}

