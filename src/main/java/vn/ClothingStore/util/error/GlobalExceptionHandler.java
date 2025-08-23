package vn.ClothingStore.util.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import vn.ClothingStore.dtos.RestResponse;

import java.util.*;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /* -------------------- 400: VALIDATION (BODY) -------------------- */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public org.springframework.http.ResponseEntity<RestResponse<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, List<String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        fe -> fe.getField(),
                        LinkedHashMap::new,
                        Collectors.mapping(fe -> Optional.ofNullable(fe.getDefaultMessage()).orElse("Invalid"), Collectors.toList())
                ));

        return build(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors, ex, true);
    }

    /* -------------------- 400: VALIDATION (PARAM/QUERY/PATH) -------------------- */
    @ExceptionHandler(ConstraintViolationException.class)
    public org.springframework.http.ResponseEntity<RestResponse<Object>> handleConstraintViolation(ConstraintViolationException ex) {
        // Trả về dạng "param" -> ["message1", "message2"...]
        Map<String, List<String>> paramErrors = new LinkedHashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            String param = Optional.ofNullable(v.getPropertyPath()).map(Object::toString).orElse("param");
            paramErrors.computeIfAbsent(param, k -> new ArrayList<>()).add(Optional.ofNullable(v.getMessage()).orElse("Invalid"));
        }
        return build(HttpStatus.BAD_REQUEST, "Constraint violation", paramErrors, ex, true);
    }

    /* -------------------- 400: BODY JSON/FORMAT SAI -------------------- */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public org.springframework.http.ResponseEntity<RestResponse<Object>> handleNotReadable(HttpMessageNotReadableException ex) {
        return build(HttpStatus.BAD_REQUEST, "Malformed JSON request", null, ex, false);
    }

    /* -------------------- 400: THIẾU THAM SỐ / HEADER / PATH VAR -------------------- */
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MissingRequestHeaderException.class,
            MissingPathVariableException.class
    })
    public org.springframework.http.ResponseEntity<RestResponse<Object>> handleMissingParams(Exception ex) {
        return build(HttpStatus.BAD_REQUEST, "Missing required parameter/header/path variable", null, ex, false);
    }

    /* -------------------- 400: SAI KIỂU THAM SỐ -------------------- */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public org.springframework.http.ResponseEntity<RestResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = String.format("Parameter '%s' should be of type %s",
                ex.getName(),
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "expected type");
        return build(HttpStatus.BAD_REQUEST, msg, null, ex, false);
    }

    /* -------------------- 405: METHOD NOT SUPPORTED -------------------- */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public org.springframework.http.ResponseEntity<RestResponse<Object>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        String msg = "HTTP method not supported";
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("method", ex.getMethod());
        info.put("supported", ex.getSupportedHttpMethods());
        return build(HttpStatus.METHOD_NOT_ALLOWED, msg, info, ex, false);
    }

    /* -------------------- 415: MEDIA TYPE NOT SUPPORTED -------------------- */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public org.springframework.http.ResponseEntity<RestResponse<Object>> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        String msg = "Content-Type not supported";
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("contentType", Optional.ofNullable(ex.getContentType()).map(MediaType::toString).orElse(null));
        info.put("supported", ex.getSupportedMediaTypes().stream().map(MediaType::toString).toList());
        return build(HttpStatus.UNSUPPORTED_MEDIA_TYPE, msg, info, ex, false);
    }

    /* -------------------- 403: ACCESS DENIED (Spring Security) -------------------- */
//    @ExceptionHandler(AccessDeniedException.class)
//    public org.springframework.http.ResponseEntity<RestResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
//        return build(HttpStatus.FORBIDDEN, "Access denied", null, ex, false);
//    }

    /* -------------------- 409: DATA INTEGRITY / UNIQUE / FK ... -------------------- */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public org.springframework.http.ResponseEntity<RestResponse<Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        return build(HttpStatus.CONFLICT, "Data integrity violation", null, ex, false);
    }

    /* -------------------- 404: NO HANDLER (BẬT THÊM PROPERTY) -------------------- */
    @ExceptionHandler(NoHandlerFoundException.class)
    public org.springframework.http.ResponseEntity<RestResponse<Object>> handleNoHandler(NoHandlerFoundException ex) {
        String path = ex.getRequestURL();
        Map<String, Object> info = Map.of("path", path, "httpMethod", ex.getHttpMethod());
        return build(HttpStatus.NOT_FOUND, "No handler found for request", info, ex, false);
    }

    /* -------------------- 500: LỖI CHUNG -------------------- */
    @ExceptionHandler(Exception.class)
    public org.springframework.http.ResponseEntity<RestResponse<Object>> handleAll(Exception ex) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", null, ex, false);
    }

    /* ==================== HELPER ==================== */

    private org.springframework.http.ResponseEntity<RestResponse<Object>> build(HttpStatus status,
                                                                                String message,
                                                                                Object details,
                                                                                Exception ex,
                                                                                boolean isClientErrorExpected) {
        // Log: client error (4xx) dùng warn, server error (5xx) dùng error
        if (status.is5xxServerError()) {
            log.error("[{}] {}", status.value(), ex.getMessage(), ex);
        } else if (isClientErrorExpected) {
            log.debug("[{}] {}", status.value(), ex.getMessage());
        } else {
            log.warn("[{}] {}", status.value(), ex.getMessage());
        }

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(status.value());
        res.setError(message);
        // message có thể là String hoặc List/Map theo RestResponse<Object>
        res.setMessage(details != null ? details : message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new org.springframework.http.ResponseEntity<>(res, headers, status);
    }
}
