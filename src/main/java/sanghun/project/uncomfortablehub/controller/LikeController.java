package sanghun.project.uncomfortablehub.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import sanghun.project.uncomfortablehub.dto.ApiResponse;
import sanghun.project.uncomfortablehub.dto.LikeRequest;
import sanghun.project.uncomfortablehub.dto.LikeResponse;
import sanghun.project.uncomfortablehub.service.LikeService;

/** 좋아요 관련 웹 요청을 처리하는 컨트롤러 */
@Controller
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {

  private final LikeService likeService;

  /** 좋아요를 등록합니다. (AJAX) */
  @PostMapping
  public ResponseEntity<ApiResponse<LikeResponse>> create(@RequestBody LikeRequest request) {
    // 1. LikeService.createLike() 호출
    likeService.createLike(request.getDiscomfortId(), request.getUuid());

    // 2. 좋아요 수와 상태 조회
    long likeCount = likeService.getLikeCount(request.getDiscomfortId());
    boolean hasLiked = likeService.hasLiked(request.getDiscomfortId(), request.getUuid());

    // 3. LikeResponse 생성 및 반환
    LikeResponse likeResponse = LikeResponse.of(request.getDiscomfortId(), likeCount, hasLiked);
    return ResponseEntity.ok(ApiResponse.success("좋아요가 등록되었습니다.", likeResponse));
  }

  /** 좋아요를 취소합니다. (AJAX) */
  @PostMapping("/cancel")
  public ResponseEntity<ApiResponse<LikeResponse>> cancel(@RequestBody LikeRequest request) {
    // 1. LikeService.cancelLike() 호출
    likeService.cancelLike(request.getDiscomfortId(), request.getUuid());

    // 2. 좋아요 수와 상태 조회
    long likeCount = likeService.getLikeCount(request.getDiscomfortId());
    boolean hasLiked = likeService.hasLiked(request.getDiscomfortId(), request.getUuid());

    // 3. LikeResponse 생성 및 반환
    LikeResponse likeResponse = LikeResponse.of(request.getDiscomfortId(), likeCount, hasLiked);
    return ResponseEntity.ok(ApiResponse.success("좋아요가 취소되었습니다.", likeResponse));
  }
}
