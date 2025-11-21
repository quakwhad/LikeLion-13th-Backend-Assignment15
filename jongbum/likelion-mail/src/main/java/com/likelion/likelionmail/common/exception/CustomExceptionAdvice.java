package com.likelion.likelionmail.common.exception;

import com.likelion.likelionmail.common.error.ErrorCode;
import com.likelion.likelionmail.common.template.ApiResTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j // 로깅을 위한 Logger를 생성
@RestControllerAdvice // REST API 컨트롤러에 대한 예외 처리 어드바이스임을 나타내는 어노테이션
@Component // 클래스를 Spring 컴포넌트로 등록
@RequiredArgsConstructor // final 필드에 대한 생성자 자동 생성
public class CustomExceptionAdvice {

    /**
     * 500 Internal Server Error
     */
    // 원인 모를 이유의 예외 발생 시
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResTemplate handleServerException(final Exception e) {
        log.error("Internal Server Error: {}", e.getMessage(), e);
        return ApiResTemplate.errorResponse(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    /**
     * custom error
     */
    // 내부 커스텀 에러 처리하기
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResTemplate> handleCustomException(BusinessException e) {
        log.error("CustomException: {}", e.getMessage(), e);

        ApiResTemplate<?> body = ApiResTemplate.errorResponse(e.getErrorCode(), e.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus()) // 에러코드에 정의된 상태 코드 사용
                .body(body);
    }

    // 추가
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResTemplate<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        // 에러 메시지 생성
        // 에러 메세지를 저장할 errorMap 생성
        Map<String, String> errorMap = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        // 응답 생성
        return ApiResTemplate.errorResponse(ErrorCode.VALIDATION_EXCEPTION, convertMapToString(errorMap));
    }

    private String convertMapToString(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(" : ").append(entry.getValue()).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1); // 마지막 띄어쓰기 제거
        sb.deleteCharAt(sb.length() - 1); // 마지막 쉼표 제거
        return sb.toString();
    }
}
