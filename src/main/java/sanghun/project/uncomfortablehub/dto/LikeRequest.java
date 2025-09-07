package sanghun.project.uncomfortablehub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 좋아요 등록 요청 DTO */
@Getter
@NoArgsConstructor
public class LikeRequest {

  @NotNull(message = "불편함 ID는 필수입니다.")
  private Long discomfortId;

  @NotBlank(message = "UUID는 필수입니다.")
  @Size(max = 255, message = "UUID는 255자를 초과할 수 없습니다.")
  private String uuid;

  @Builder
  private LikeRequest(Long discomfortId, String uuid) {
    this.discomfortId = discomfortId;
    this.uuid = uuid;
  }
}
