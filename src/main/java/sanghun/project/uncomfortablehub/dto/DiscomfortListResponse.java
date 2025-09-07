package sanghun.project.uncomfortablehub.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import sanghun.project.uncomfortablehub.domain.Discomfort;

/** 불편함 목록 조회 응답 DTO */
@Getter
@Builder
public class DiscomfortListResponse {

  private Long id;
  private String title;
  private String description;
  private String uuid;
  private LocalDateTime createdAt;
  private long likeCount;
  private boolean hasLiked;

  /** Discomfort 엔티티를 DiscomfortListResponse로 변환합니다. (기본값) */
  public static DiscomfortListResponse from(Discomfort discomfort) {
    return from(discomfort, 0, false);
  }

  /** Discomfort 엔티티를 DiscomfortListResponse로 변환합니다. (좋아요 정보 포함) */
  public static DiscomfortListResponse from(
      Discomfort discomfort, long likeCount, boolean hasLiked) {
    return DiscomfortListResponse.builder()
        .id(discomfort.getId())
        .title(discomfort.getTitle())
        .description(discomfort.getDescription())
        .uuid(discomfort.getUuid())
        .createdAt(discomfort.getCreatedAt())
        .likeCount(likeCount)
        .hasLiked(hasLiked)
        .build();
  }
}
