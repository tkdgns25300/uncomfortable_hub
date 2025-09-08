package sanghun.project.uncomfortablehub.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sanghun.project.uncomfortablehub.dto.DiscomfortListResponse;
import sanghun.project.uncomfortablehub.dto.DiscomfortRequest;
import sanghun.project.uncomfortablehub.dto.DiscomfortResponse;
import sanghun.project.uncomfortablehub.service.DiscomfortService;

/** 불편함 관련 웹 요청을 처리하는 컨트롤러 */
@Controller
@RequestMapping("/discomforts")
@RequiredArgsConstructor
public class DiscomfortController {

  private final DiscomfortService discomfortService;

  /** 불편함 목록 페이지를 반환합니다. */
  @GetMapping
  public String list() {
    return "discomfort/list";
  }

  /** 모든 불편함을 조회합니다. (AJAX) */
  @GetMapping("/api")
  public ResponseEntity<List<DiscomfortListResponse>> getAllDiscomforts(@RequestParam String uuid) {
    // 1. DiscomfortService.findAllDiscomforts() 호출
    List<DiscomfortListResponse> discomforts = discomfortService.findAllDiscomforts(uuid);

    // 2. DiscomfortListResponse 리스트 반환
    return ResponseEntity.ok(discomforts);
  }

  /** ID로 불편함을 조회합니다. (AJAX) */
  @GetMapping("/api/{id}")
  public ResponseEntity<DiscomfortResponse> getDiscomfortById(
      @PathVariable Long id, @RequestParam String uuid) {
    // 1. DiscomfortService.findDiscomfortById() 호출
    DiscomfortResponse discomfort = discomfortService.findDiscomfortById(id, uuid);

    // 2. DiscomfortResponse 반환
    return ResponseEntity.ok(discomfort);
  }

  /** 불편함을 등록합니다. (AJAX 팝업) */
  @PostMapping
  public ResponseEntity<DiscomfortResponse> create(@RequestBody DiscomfortRequest request) {
    // 1. 클라이언트에서 전달받은 UUID 사용
    String uuid = request.getUuid();

    // 2. DiscomfortService.createDiscomfort() 호출
    DiscomfortResponse response = discomfortService.createDiscomfort(request, uuid);

    // 3. DiscomfortResponse 반환
    return ResponseEntity.ok(response);
  }

  /** 불편함을 수정합니다. (AJAX 팝업) */
  @PutMapping("/{id}")
  public ResponseEntity<DiscomfortResponse> update(
      @PathVariable Long id, @RequestBody DiscomfortRequest request) {
    // 1. 클라이언트에서 전달받은 UUID 사용
    String uuid = request.getUuid();

    // 2. DiscomfortService.updateDiscomfort() 호출
    DiscomfortResponse response = discomfortService.updateDiscomfort(id, request, uuid);

    // 3. DiscomfortResponse 반환
    return ResponseEntity.ok(response);
  }

  /** 불편함을 삭제합니다. (AJAX) */
  @DeleteMapping("/{id}")
  public ResponseEntity<String> delete(
      @PathVariable Long id, @RequestBody DiscomfortRequest request) {
    // 1. 클라이언트에서 전달받은 UUID 사용
    String uuid = request.getUuid();

    // 2. DiscomfortService.deleteDiscomfort() 호출
    discomfortService.deleteDiscomfort(id, uuid);

    return ResponseEntity.ok("삭제되었습니다.");
  }
}
