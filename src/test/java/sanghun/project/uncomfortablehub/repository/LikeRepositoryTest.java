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
import sanghun.project.uncomfortablehub.domain.Like;

/** LikeRepository 테스트 */
@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(
    properties = {
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.datasource.url=jdbc:h2:mem:testdb",
      "spring.datasource.driver-class-name=org.h2.Driver"
    })
class LikeRepositoryTest {

  @Autowired private TestEntityManager entityManager;

  @Autowired private LikeRepository likeRepository;

  @Autowired private DiscomfortRepository discomfortRepository;

  private Discomfort discomfort1;
  private Discomfort discomfort2;
  private Like like1;
  private Like like2;
  private Like like3;

  @BeforeEach
  void setUp() {
    // 불편함 데이터 생성
    discomfort1 = Discomfort.create("첫 번째 불편함", "uuid-1", "첫 번째 설명");
    discomfort2 = Discomfort.create("두 번째 불편함", "uuid-2", "두 번째 설명");

    // 불편함 저장
    entityManager.persistAndFlush(discomfort1);
    entityManager.persistAndFlush(discomfort2);

    // 좋아요 데이터 생성
    like1 = Like.create(discomfort1.getId(), "uuid-3");
    like2 = Like.create(discomfort1.getId(), "uuid-4");
    like3 = Like.create(discomfort2.getId(), "uuid-3");

    // 좋아요 저장
    entityManager.persistAndFlush(like1);
    entityManager.persistAndFlush(like2);
    entityManager.persistAndFlush(like3);
  }

  @Test
  void 특정_불편함과_UUID로_좋아요_조회() {
    // when
    var result = likeRepository.findByDiscomfortIdAndUuid(discomfort1.getId(), "uuid-3");

    // then
    assertThat(result).isPresent();
    assertThat(result.get().getDiscomfortId()).isEqualTo(discomfort1.getId());
    assertThat(result.get().getUuid()).isEqualTo("uuid-3");
  }

  @Test
  void 특정_불편함의_좋아요_수_조회() {
    // when
    long count = likeRepository.countByDiscomfortId(discomfort1.getId());

    // then
    assertThat(count).isEqualTo(2);
  }

  @Test
  void 특정_불편함에_대한_좋아요_존재_여부_확인() {
    // when & then
    assertThat(likeRepository.existsByDiscomfortIdAndUuid(discomfort1.getId(), "uuid-3")).isTrue();
    assertThat(likeRepository.existsByDiscomfortIdAndUuid(discomfort1.getId(), "uuid-5")).isFalse();
  }

  @Test
  void 특정_UUID가_작성한_모든_좋아요_조회() {
    // when
    List<Like> result = likeRepository.findByUuidOrderByCreatedAtDesc("uuid-3");

    // then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getDiscomfortId()).isEqualTo(discomfort2.getId());
    assertThat(result.get(1).getDiscomfortId()).isEqualTo(discomfort1.getId());
  }

  @Test
  void 특정_불편함의_모든_좋아요_조회() {
    // when
    List<Like> result = likeRepository.findByDiscomfortIdOrderByCreatedAtDesc(discomfort1.getId());

    // then
    assertThat(result).hasSize(2);
    assertThat(result.get(0).getUuid()).isEqualTo("uuid-4");
    assertThat(result.get(1).getUuid()).isEqualTo("uuid-3");
  }

  @Test
  void 특정_UUID가_작성한_좋아요_수_조회() {
    // when
    long count = likeRepository.countByUuid("uuid-3");

    // then
    assertThat(count).isEqualTo(2);
  }

  @Test
  void 특정_불편함의_좋아요_삭제() {
    // when
    likeRepository.deleteByDiscomfortId(discomfort1.getId());

    // then
    assertThat(likeRepository.countByDiscomfortId(discomfort1.getId())).isEqualTo(0);
    assertThat(likeRepository.countByDiscomfortId(discomfort2.getId())).isEqualTo(1);
  }

  @Test
  void 특정_불편함과_UUID로_좋아요_삭제() {
    // when
    likeRepository.deleteByDiscomfortIdAndUuid(discomfort1.getId(), "uuid-3");

    // then
    assertThat(likeRepository.existsByDiscomfortIdAndUuid(discomfort1.getId(), "uuid-3")).isFalse();
    assertThat(likeRepository.existsByDiscomfortIdAndUuid(discomfort1.getId(), "uuid-4")).isTrue();
  }
}
