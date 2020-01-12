package com.misaelneto.chatapp.controllers;

import com.misaelneto.chatapp.data.dtos.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.ws.rs.NotFoundException;
import java.util.Date;

@ControllerAdvice
public class AppErros{
	Logger logger = LoggerFactory.getLogger(this.getClass().getName());
//    Statistic statistic = StatisticFactory.getStatistic(Application.class);

	@ExceptionHandler({NotFoundException.class})
	public ResponseEntity<ErrorDto> handleNotFoundException(Exception ex, WebRequest request){
//		statistic.error("requestException", ex);

		logger.error(ex.getLocalizedMessage(), ex);
		String path = request.getContextPath();
		ErrorDto error = new ErrorDto();
		error.timestamp = new Date().getTime();
		error.exception = ex.getClass().getCanonicalName();
		error.error = HttpStatus.NOT_FOUND.getReasonPhrase();
		error.message = ex.getMessage();
		error.path = path;
		error.status = HttpStatus.NOT_FOUND.value();
		return new ResponseEntity<ErrorDto>(error, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDto> handleAllException(Exception ex, WebRequest request) {
//        statistic.error("requestException", ex);

		logger.error(ex.getLocalizedMessage(), ex);
		String path = request.getContextPath();
		ErrorDto error = new ErrorDto();
		error.timestamp = new Date().getTime();
		error.exception = ex.getClass().getCanonicalName();
		error.error = HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
		error.message = ex.getMessage();
		error.path = path;
		error.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		return new ResponseEntity<ErrorDto>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}