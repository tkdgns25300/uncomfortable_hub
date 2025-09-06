package sanghun.project.uncomfortablehub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sanghun.project.uncomfortablehub.dto.ApiResponse;
import sanghun.project.uncomfortablehub.dto.LikeRequest;
import sanghun.project.uncomfortablehub.dto.LikeResponse;

/** 좋아요 관련 웹 요청을 처리하는 컨트롤러 */
@Controller
@RequestMapping("/likes")
public class LikeController {

  /** 좋아요를 등록합니다. (AJAX) */
  @PostMapping
  public ResponseEntity<ApiResponse<LikeResponse>> create(@RequestBody LikeRequest request) {
    // TODO: 구현 예정 - AJAX로 실시간 좋아요 처리
    // 1. LikeService.createLike() 호출
    // 2. 좋아요 수와 상태 반환
    LikeResponse likeResponse = LikeResponse.of(request.getDiscomfortId(), 0, false);
    return ResponseEntity.ok(ApiResponse.success("좋아요가 등록되었습니다.", likeResponse));
  }

  /** 좋아요를 취소합니다. (AJAX) */
  @PostMapping("/cancel")
  public ResponseEntity<ApiResponse<LikeResponse>> cancel(@RequestBody LikeRequest request) {
    // TODO: 구현 예정 - AJAX로 실시간 좋아요 취소 처리
    // 1. LikeService.cancelLike() 호출
    // 2. 좋아요 수와 상태 반환
    LikeResponse likeResponse = LikeResponse.of(request.getDiscomfortId(), 0, false);
    return ResponseEntity.ok(ApiResponse.success("좋아요가 취소되었습니다.", likeResponse));
  }
}
