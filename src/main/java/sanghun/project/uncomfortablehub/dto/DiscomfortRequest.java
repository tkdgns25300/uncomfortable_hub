package sanghun.project.uncomfortablehub.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** 불편함 등록 요청 DTO */
@Getter
@NoArgsConstructor
public class DiscomfortRequest {

  @NotBlank(message = "제목은 필수입니다.")
  @Size(max = 255, message = "제목은 255자를 초과할 수 없습니다.")
  private String title;

  @NotBlank(message = "설명은 필수입니다.")
  @Size(max = 5000, message = "설명은 5000자를 초과할 수 없습니다.")
  private String description;

  @NotBlank(message = "UUID는 필수입니다.")
  @Size(max = 255, message = "UUID는 255자를 초과할 수 없습니다.")
  private String uuid;

  @Builder
  private DiscomfortRequest(String title, String description, String uuid) {
    this.title = title;
    this.description = description;
    this.uuid = uuid;
  }

  /** 수정용 생성자 */
  public static DiscomfortRequest forUpdate(String title, String description, String uuid) {
    return DiscomfortRequest.builder().title(title).description(description).uuid(uuid).build();
  }
}
