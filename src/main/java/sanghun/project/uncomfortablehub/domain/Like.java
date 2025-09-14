package sanghun.project.uncomfortablehub.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/** 좋아요를 나타내는 도메인 엔티티 */
@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Like {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long discomfortId;

  @Column(nullable = false, length = 255)
  private String uuid;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Builder
  private Like(Long discomfortId, String uuid) {
    this.discomfortId = discomfortId;
    this.uuid = uuid;
  }

  /** 좋아요를 생성합니다. */
  public static Like create(Long discomfortId, String uuid) {
    return Like.builder().discomfortId(discomfortId).uuid(uuid).build();
  }
}
