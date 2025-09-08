package sanghun.project.uncomfortablehub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/** 메인 페이지 컨트롤러 */
@Controller
public class HomeController {

  /** 메인 페이지를 반환합니다. */
  @GetMapping("/")
  public String home() {
    return "index";
  }
}
