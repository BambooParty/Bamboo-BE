package org.pandas.bambooclub.global;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ApiResponse<T> {
    private HttpStatus status; // success or fail
    private T data;        // 데이터
    private String message; // 메시지
    private Object error;   // 에러 정보 (Optional)

    // 생성자
    public ApiResponse(HttpStatus status, T data, String message, Object error) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.error = error;
    }

    // 성공 응답 생성 메서드
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(HttpStatus.OK, data, message, null);
    }

    // 실패 응답 생성 메서드
    public static <T> ApiResponse<T> fail(String message, Object error) {
        return new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR, null, message, error);
    }
}

