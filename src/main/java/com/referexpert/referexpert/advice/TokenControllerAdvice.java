package com.referexpert.referexpert.advice;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.referexpert.referexpert.exception.TokenRefreshException;

@RestControllerAdvice
public class TokenControllerAdvice {

  @ExceptionHandler(value = TokenRefreshException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
    return new ErrorMessage(
        HttpStatus.UNAUTHORIZED.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
  }
}
