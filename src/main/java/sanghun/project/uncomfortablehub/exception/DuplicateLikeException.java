package sanghun.project.uncomfortablehub.exception;

/** 중복 좋아요 시도 시 발생하는 예외 */
public class DuplicateLikeException extends RuntimeException {

  public DuplicateLikeException(String message) {
    super(message);
  }

  public DuplicateLikeException(String message, Throwable cause) {
    super(message, cause);
  }
}
