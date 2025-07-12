package com.guilhermescherer.msservicewatch.controller;

import com.guilhermescherer.msservicewatch.enums.ErrorCatalog;
import com.guilhermescherer.msservicewatch.exception.HttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class RestControllerAdviceHandler {

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<ProblemDetail> handleHttpException(HttpException ex, WebRequest request) {
        HttpStatus status = ex.getStatus();

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(status.getReasonPhrase());
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problem.setType(URI.create(ex.getError().getUrl()));

        return ResponseEntity.status(status).body(problem);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(ErrorCatalog.VALIDATION_ERROR.getStatus());
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));
        problem.setTitle(ErrorCatalog.VALIDATION_ERROR.getMessage());
        problem.setType(URI.create(ErrorCatalog.VALIDATION_ERROR.getUrl()));

        List<Map<String, String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "message", error.getDefaultMessage() != null ? error.getDefaultMessage() : "Something went wrong"
                ))
                .toList();

        problem.setProperty("fieldErrors", fieldErrors);


        return ResponseEntity.status(ErrorCatalog.VALIDATION_ERROR.getStatus()).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception ex, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(ErrorCatalog.GENERAL_ERROR.getStatus());
        problem.setTitle(ErrorCatalog.GENERAL_ERROR.getMessage());
        problem.setType(URI.create(ErrorCatalog.GENERAL_ERROR.getUrl()));
        problem.setInstance(URI.create(request.getDescription(false).replace("uri=", "")));

        return ResponseEntity.status(ErrorCatalog.GENERAL_ERROR.getStatus()).body(problem);
    }
}
