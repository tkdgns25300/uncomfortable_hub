package sanghun.project.uncomfortablehub.exception;

/** 불편함을 찾을 수 없을 때 발생하는 예외 */
public class DiscomfortNotFoundException extends RuntimeException {

  public DiscomfortNotFoundException(String message) {
    super(message);
  }

  public DiscomfortNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
