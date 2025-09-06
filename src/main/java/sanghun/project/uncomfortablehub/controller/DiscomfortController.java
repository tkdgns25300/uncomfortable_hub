package sanghun.project.uncomfortablehub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sanghun.project.uncomfortablehub.dto.DiscomfortRequest;
import sanghun.project.uncomfortablehub.dto.DiscomfortResponse;

/** 불편함 관련 웹 요청을 처리하는 컨트롤러 */
@Controller
@RequestMapping("/discomforts")
public class DiscomfortController {

  /** 불편함 목록 페이지를 반환합니다. */
  @GetMapping
  public String list() {
    return "discomfort/list";
  }

  /** 불편함을 등록합니다. (AJAX 팝업) */
  @PostMapping
  public ResponseEntity<DiscomfortResponse> create(@RequestBody DiscomfortRequest request) {
    // TODO: 구현 예정 - AJAX로 팝업에서 처리
    // 1. UUID 생성
    // 2. DiscomfortService.createDiscomfort() 호출
    // 3. DiscomfortResponse 반환
    return ResponseEntity.ok(DiscomfortResponse.builder().build());
  }

  /** 불편함을 수정합니다. (AJAX 팝업) */
  @PutMapping("/{id}")
  public ResponseEntity<DiscomfortResponse> update(
      @PathVariable Long id, @RequestBody DiscomfortRequest request) {
    // TODO: 구현 예정 - AJAX로 팝업에서 처리
    // 1. DiscomfortService.updateDiscomfort() 호출
    // 2. DiscomfortResponse 반환
    return ResponseEntity.ok(DiscomfortResponse.builder().build());
  }

  /** 불편함을 삭제합니다. (AJAX) */
  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(@PathVariable Long id) {
    // TODO: 구현 예정 - AJAX로 삭제 처리
    // 1. DiscomfortService.deleteDiscomfort() 호출
    return ResponseEntity.ok("삭제되었습니다.");
  }
}
