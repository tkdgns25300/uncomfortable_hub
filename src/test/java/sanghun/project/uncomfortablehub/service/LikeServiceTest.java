package sanghun.project.uncomfortablehub.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import sanghun.project.uncomfortablehub.domain.Discomfort;
import sanghun.project.uncomfortablehub.repository.DiscomfortRepository;
import sanghun.project.uncomfortablehub.repository.LikeRepository;

/** LikeService 테스트 */
@DataJpaTest
@Import(LikeService.class)
@ActiveProfiles("test")
@TestPropertySource(
    properties = {
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.datasource.url=jdbc:h2:mem:testdb",
      "spring.datasource.driver-class-name=org.h2.Driver"
    })
class LikeServiceTest {

  @Autowired private LikeService likeService;

  @Autowired private LikeRepository likeRepository;

  @Autowired private DiscomfortRepository discomfortRepository;

  @Autowired private TestEntityManager entityManager;

  private Discomfort discomfort;
  private String uuid;

  @BeforeEach
  void setUp() {
    // 불편함 데이터 생성
    discomfort = Discomfort.create("테스트 불편함", "test-uuid-1", "테스트 설명");
    entityManager.persistAndFlush(discomfort);

    uuid = "test-uuid-2";
  }

  @Test
  void 좋아요_등록_성공() {
    // when
    likeService.createLike(discomfort.getId(), uuid);

    // then
    assertThat(likeService.hasLiked(discomfort.getId(), uuid)).isTrue();
    assertThat(likeService.getLikeCount(discomfort.getId())).isEqualTo(1);
  }

  @Test
  void 좋아요_중복_등록_무시() {
    // given
    likeService.createLike(discomfort.getId(), uuid);

    // when
    likeService.createLike(discomfort.getId(), uuid); // 중복 등록 시도

    // then
    assertThat(likeService.getLikeCount(discomfort.getId())).isEqualTo(1); // 여전히 1개
  }

  @Test
  void 좋아요_취소_성공() {
    // given
    likeService.createLike(discomfort.getId(), uuid);

    // when
    likeService.cancelLike(discomfort.getId(), uuid);

    // then
    assertThat(likeService.hasLiked(discomfort.getId(), uuid)).isFalse();
    assertThat(likeService.getLikeCount(discomfort.getId())).isEqualTo(0);
  }

  @Test
  void 좋아요_취소_없는_좋아요_무시() {
    // when
    likeService.cancelLike(discomfort.getId(), uuid); // 없는 좋아요 취소 시도

    // then
    assertThat(likeService.getLikeCount(discomfort.getId())).isEqualTo(0); // 여전히 0개
  }

  @Test
  void 좋아요_수_조회() {
    // given
    likeService.createLike(discomfort.getId(), uuid);
    likeService.createLike(discomfort.getId(), "other-uuid");

    // when
    long count = likeService.getLikeCount(discomfort.getId());

    // then
    assertThat(count).isEqualTo(2);
  }

  @Test
  void 좋아요_여부_확인() {
    // given
    likeService.createLike(discomfort.getId(), uuid);

    // when & then
    assertThat(likeService.hasLiked(discomfort.getId(), uuid)).isTrue();
    assertThat(likeService.hasLiked(discomfort.getId(), "other-uuid")).isFalse();
  }

  @Test
  void 좋아요_토글_동작() {
    // 1. 좋아요 등록
    likeService.createLike(discomfort.getId(), uuid);
    assertThat(likeService.hasLiked(discomfort.getId(), uuid)).isTrue();
    assertThat(likeService.getLikeCount(discomfort.getId())).isEqualTo(1);

    // 2. 좋아요 취소
    likeService.cancelLike(discomfort.getId(), uuid);
    assertThat(likeService.hasLiked(discomfort.getId(), uuid)).isFalse();
    assertThat(likeService.getLikeCount(discomfort.getId())).isEqualTo(0);

    // 3. 다시 좋아요 등록
    likeService.createLike(discomfort.getId(), uuid);
    assertThat(likeService.hasLiked(discomfort.getId(), uuid)).isTrue();
    assertThat(likeService.getLikeCount(discomfort.getId())).isEqualTo(1);
  }
}
