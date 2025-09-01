package vn.ClothingStore.util.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vn.ClothingStore.dtos.RestResponse;

import java.util.List;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalException {

    /* ===== Helper ===== */
    private ResponseEntity<RestResponse<Object>> build(HttpStatus status, String error, Object message) {
        RestResponse<Object> body = new RestResponse<>();
        body.setStatusCode(status.value());
        body.setError(error);
        body.setMessage(message);
        return ResponseEntity.status(status).body(body);
    }

    /* ===== 401: Authentication errors ===== */
    @ExceptionHandler({ BadCredentialsException.class, UsernameNotFoundException.class })
    public ResponseEntity<RestResponse<Object>> handleAuth(Exception ex) {
        // Không lộ thông tin: luôn trả chung 401
        return build(HttpStatus.UNAUTHORIZED, "BadCredentials", "Email hoặc mật khẩu không đúng.");
    }

    @ExceptionHandler({ BadJwtException.class, JwtException.class })
    public ResponseEntity<RestResponse<Object>> handleJwt(JwtException ex) {
        return build(HttpStatus.UNAUTHORIZED, ex.getClass().getSimpleName(),
                "Token không hợp lệ (hết hạn, sai định dạng, hoặc thiếu).");
    }

    /* ===== 403: Authorization errors ===== */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RestResponse<Object>> handleAccessDenied(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "AccessDenied", "Bạn không có quyền thực hiện hành động này.");
    }

    /* ===== 400: Bad request / Validation ===== */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponse<Object>> handleBeanValidation(MethodArgumentNotValidException ex) {
        List<String> errs = ex.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).toList();
        Object msg = errs.size() <= 1 ? (errs.isEmpty() ? "Yêu cầu không hợp lệ." : errs.get(0)) : errs;
        return build(HttpStatus.BAD_REQUEST, "ValidationError", msg);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RestResponse<Object>> handleConstraint(ConstraintViolationException ex) {
        List<String> errs = ex.getConstraintViolations()
                .stream().map(ConstraintViolation::getMessage).toList();
        Object msg = errs.size() <= 1 ? (errs.isEmpty() ? "Yêu cầu không hợp lệ." : errs.get(0)) : errs;
        return build(HttpStatus.BAD_REQUEST, "ValidationError", msg);
    }

    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MissingRequestHeaderException.class,
            MissingPathVariableException.class,
            HttpMessageNotReadableException.class, // JSON/body sai định dạng
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<RestResponse<Object>> handleBadRequest(Exception ex) {
        return build(HttpStatus.BAD_REQUEST, "BadRequest", ex.getMessage());
    }

    /* ===== 405 / 415 ===== */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<RestResponse<Object>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, "MethodNotAllowed", ex.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<RestResponse<Object>> handleMediaType(HttpMediaTypeNotSupportedException ex) {
        return build(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "UnsupportedMediaType", ex.getMessage());
    }

    /* ===== 409: Data conflict (unique, FK...) ===== */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<RestResponse<Object>> handleDataIntegrity(DataIntegrityViolationException ex) {
        return build(HttpStatus.CONFLICT, "DataIntegrityViolation", "Dữ liệu không hợp lệ hoặc trùng lặp.");
    }

    /* ===== 404 ===== */
    @ExceptionHandler({ NoResourceFoundException.class, NoHandlerFoundException.class })
    public ResponseEntity<RestResponse<Object>> handleNotFound(Exception ex) {
        return build(HttpStatus.NOT_FOUND, "NotFound", "Đường dẫn hoặc tài nguyên không tồn tại.");
    }

    /* ===== 500: Fallback ===== */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Object>> handleAny(Exception ex) {
        // TODO: logger.error("Unhandled exception", ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "InternalServerError", "Có lỗi xảy ra ở máy chủ.");
    }
}
