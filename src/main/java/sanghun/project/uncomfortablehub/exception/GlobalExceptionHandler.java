package sanghun.project.uncomfortablehub.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/** 전역 예외 처리를 담당하는 클래스 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  /** 불편함을 찾을 수 없을 때의 예외 처리 */
  @ExceptionHandler(DiscomfortNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleDiscomfortNotFound(DiscomfortNotFoundException e, Model model) {
    log.error("Discomfort not found: {}", e.getMessage());
    model.addAttribute("errorMessage", e.getMessage());
    return "error/404";
  }

  /** 중복 좋아요 시도 시의 예외 처리 */
  @ExceptionHandler(DuplicateLikeException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleDuplicateLike(DuplicateLikeException e, Model model) {
    log.error("Duplicate like attempt: {}", e.getMessage());
    model.addAttribute("errorMessage", e.getMessage());
    return "error/400";
  }

  /** 일반적인 예외 처리 */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String handleGeneralException(Exception e, Model model) {
    log.error("Unexpected error occurred: ", e);
    model.addAttribute("errorMessage", "서버에 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
    return "error/500";
  }
}
