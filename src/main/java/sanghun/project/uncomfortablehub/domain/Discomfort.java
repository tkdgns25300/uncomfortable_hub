package sanghun.project.uncomfortablehub.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/** 불편함을 나타내는 도메인 엔티티 */
@Entity
@Table(name = "discomforts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Discomfort {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 255)
  private String title;

  @Column(nullable = false, length = 255)
  private String uuid;

  @Column(columnDefinition = "TEXT")
  private String description;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Builder
  private Discomfort(String title, String uuid, String description) {
    this.title = title;
    this.uuid = uuid;
    this.description = description;
  }

  /** 불편함을 생성합니다. */
  public static Discomfort create(String title, String uuid, String description) {
    return Discomfort.builder().title(title).uuid(uuid).description(description).build();
  }

  /** 불편함 제목을 수정합니다. */
  public void updateTitle(String title) {
    this.title = title;
  }

  /** 불편함 설명을 수정합니다. */
  public void updateDescription(String description) {
    this.description = description;
  }

  /** 불편함을 수정합니다. */
  public void update(String title, String description) {
    this.title = title;
    this.description = description;
  }
}
