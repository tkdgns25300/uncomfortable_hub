package sanghun.project.uncomfortablehub.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import sanghun.project.uncomfortablehub.domain.Discomfort;

/** 불편함 조회 응답 DTO */
@Getter
@Builder
public class DiscomfortResponse {

  private Long id;
  private String title;
  private String description;
  private String uuid;
  private LocalDateTime createdAt;
  private long likeCount;
  private boolean hasLiked;

  /** Discomfort 엔티티를 DiscomfortResponse로 변환합니다. */
  public static DiscomfortResponse from(Discomfort discomfort, long likeCount, boolean hasLiked) {
    return DiscomfortResponse.builder()
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
