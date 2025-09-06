package sanghun.project.uncomfortablehub.dto;

import lombok.Builder;
import lombok.Getter;

/** AJAX API 응답용 DTO */
@Getter
@Builder
public class ApiResponse<T> {

  private boolean success;
  private String message;
  private T data;

  /** 성공 응답 생성 */
  public static <T> ApiResponse<T> success(T data) {
    return ApiResponse.<T>builder().success(true).message("성공").data(data).build();
  }

  /** 성공 응답 생성 (메시지 포함) */
  public static <T> ApiResponse<T> success(String message, T data) {
    return ApiResponse.<T>builder().success(true).message(message).data(data).build();
  }

  /** 실패 응답 생성 */
  public static <T> ApiResponse<T> error(String message) {
    return ApiResponse.<T>builder().success(false).message(message).data(null).build();
  }
}
