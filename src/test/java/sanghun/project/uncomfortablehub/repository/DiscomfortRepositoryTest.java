package sanghun.project.uncomfortablehub.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import sanghun.project.uncomfortablehub.domain.Discomfort;

/** DiscomfortRepository 테스트 */
@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(
    properties = {
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.datasource.url=jdbc:h2:mem:testdb",
      "spring.datasource.driver-class-name=org.h2.Driver"
    })
class DiscomfortRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private DiscomfortRepository discomfortRepository;

  private Discomfort discomfort1;
  private Discomfort discomfort2;
  private Discomfort discomfort3;

  @BeforeEach
  void setUp() {
    // 테스트 데이터 생성
    discomfort1 = Discomfort.create("첫 번째 불편함", "uuid-1", "첫 번째 설명");
    discomfort2 = Discomfort.create("두 번째 불편함", "uuid-2", "두 번째 설명");
    discomfort3 = Discomfort.create("세 번째 불편함", "uuid-1", "세 번째 설명");

    // 엔티티 저장
    entityManager.persistAndFlush(discomfort1);
    entityManager.persistAndFlush(discomfort2);
    entityManager.persistAndFlush(discomfort3);
  }

  @Test
  void 생성일시_기준_내림차순_조회() {
    // when
    List<Discomfort> result = discomfortRepository.findAllByOrderByCreatedAtDesc();

    // then
    assertThat(result).hasSize(3);
    assertThat(result.get(0).getTitle()).isEqualTo("세 번째 불편함");
    assertThat(result.get(1).getTitle()).isEqualTo("두 번째 불편함");
    assertThat(result.get(2).getTitle()).isEqualTo("첫 번째 불편함");
  }

  @Test
  void UUID별_조회() {
    // when
    List<Discomfort> result = discomfortRepository.findByUuidOrderByCreatedAtDesc("uuid-1");

    // then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getTitle()).isEqualTo("세 번째 불편함");
    assertThat(result.get(1).getTitle()).isEqualTo("첫 번째 불편함");
  }

  @Test
  void 제목_검색() {
    // when
    List<Discomfort> result =
        discomfortRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc("두 번째");

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTitle()).isEqualTo("두 번째 불편함");
  }

  @Test
  void 설명_검색() {
    // when
    List<Discomfort> result =
        discomfortRepository.findByDescriptionContainingIgnoreCaseOrderByCreatedAtDesc("세 번째");

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getDescription()).isEqualTo("세 번째 설명");
  }

  @Test
  void 제목_또는_설명_검색() {
    // when
    List<Discomfort> result =
        discomfortRepository.findByTitleOrDescriptionContainingIgnoreCase("첫 번째");

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTitle()).isEqualTo("첫 번째 불편함");
  }

  @Test
  void UUID별_개수_조회() {
    // when
    long count = discomfortRepository.countByUuid("uuid-1");

    // then
    assertThat(count).isEqualTo(2);
  }

  @Test
  void 존재_여부_확인() {
    // when & then
    assertThat(discomfortRepository.existsByIdAndUuid(discomfort1.getId(), "uuid-1")).isTrue();
    assertThat(discomfortRepository.existsByIdAndUuid(discomfort1.getId(), "uuid-2")).isFalse();
  }
}
