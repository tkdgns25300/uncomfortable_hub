package sanghun.project.uncomfortablehub.dto;

import lombok.Builder;
import lombok.Getter;

/** 좋아요 응답 DTO */
@Getter
@Builder
public class LikeResponse {

  private Long discomfortId;
  private long likeCount;
  private boolean hasLiked;

  /** 좋아요 응답 생성 */
  public static LikeResponse of(Long discomfortId, long likeCount, boolean hasLiked) {
    return LikeResponse.builder()
        .discomfortId(discomfortId)
        .likeCount(likeCount)
        .hasLiked(hasLiked)
        .build();
  }
}
