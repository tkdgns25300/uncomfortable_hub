package sanghun.project.uncomfortablehub.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import sanghun.project.uncomfortablehub.dto.DiscomfortRequest;
import sanghun.project.uncomfortablehub.dto.DiscomfortResponse;
import sanghun.project.uncomfortablehub.repository.DiscomfortRepository;
import sanghun.project.uncomfortablehub.service.LikeService;

/** DiscomfortService 테스트 */
@DataJpaTest
@Import({DiscomfortService.class, LikeService.class})
@ActiveProfiles("test")
@TestPropertySource(
    properties = {
      "spring.jpa.hibernate.ddl-auto=create-drop",
      "spring.datasource.url=jdbc:h2:mem:testdb",
      "spring.datasource.driver-class-name=org.h2.Driver"
    })
class DiscomfortServiceTest {

  @Autowired private DiscomfortService discomfortService;

  @Autowired private DiscomfortRepository discomfortRepository;

  @Autowired private TestEntityManager entityManager;

  private DiscomfortRequest request;
  private String uuid;

  @BeforeEach
  void setUp() {
    request =
        DiscomfortRequest.builder()
            .title("테스트 불편함")
            .description("테스트 설명")
            .uuid("test-uuid-123")
            .build();
    uuid = "test-uuid-123";
  }

  @Test
  void 불편함_등록_성공() {
    // when
    DiscomfortResponse response = discomfortService.createDiscomfort(request, uuid);

    // then
    assertThat(response).isNotNull();
    assertThat(response.getTitle()).isEqualTo("테스트 불편함");
    assertThat(response.getDescription()).isEqualTo("테스트 설명");
    assertThat(response.getUuid()).isEqualTo(uuid);
    assertThat(response.getId()).isNotNull();
  }

  @Test
  void 불편함_수정_성공() {
    // given
    DiscomfortResponse created = discomfortService.createDiscomfort(request, uuid);
    DiscomfortRequest updateRequest =
        DiscomfortRequest.builder()
            .title("수정된 제목")
            .description("수정된 설명")
            .uuid("test-uuid-123")
            .build();

    // when
    DiscomfortResponse response =
        discomfortService.updateDiscomfort(created.getId(), updateRequest, uuid);

    // then
    assertThat(response.getTitle()).isEqualTo("수정된 제목");
    assertThat(response.getDescription()).isEqualTo("수정된 설명");
  }

  @Test
  void 불편함_수정_실패_존재하지_않는_ID() {
    // when & then
    assertThatThrownBy(() -> discomfortService.updateDiscomfort(999L, request, uuid))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("불편함을 찾을 수 없습니다");
  }

  @Test
  void 불편함_수정_실패_권한_없음() {
    // given
    DiscomfortResponse created = discomfortService.createDiscomfort(request, uuid);
    String otherUuid = "other-uuid";

    // when & then
    assertThatThrownBy(
            () -> discomfortService.updateDiscomfort(created.getId(), request, otherUuid))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("본인이 작성한 불편함만 수정할 수 있습니다");
  }

  @Test
  void 불편함_삭제_성공() {
    // given
    DiscomfortResponse created = discomfortService.createDiscomfort(request, uuid);

    // when
    discomfortService.deleteDiscomfort(created.getId(), uuid);

    // then
    assertThat(discomfortRepository.findById(created.getId())).isEmpty();
  }

  @Test
  void 불편함_삭제_실패_존재하지_않는_ID() {
    // when & then
    assertThatThrownBy(() -> discomfortService.deleteDiscomfort(999L, uuid))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("불편함을 찾을 수 없습니다");
  }

  @Test
  void 불편함_삭제_실패_권한_없음() {
    // given
    DiscomfortResponse created = discomfortService.createDiscomfort(request, uuid);
    String otherUuid = "other-uuid";

    // when & then
    assertThatThrownBy(() -> discomfortService.deleteDiscomfort(created.getId(), otherUuid))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("본인이 작성한 불편함만 삭제할 수 있습니다");
  }

  @Test
  void 모든_불편함_조회_성공() {
    // given
    discomfortService.createDiscomfort(request, uuid);

    // when
    var result = discomfortService.findAllDiscomforts(uuid);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getTitle()).isEqualTo("테스트 불편함");
  }

  @Test
  void ID로_불편함_조회_성공() {
    // given
    DiscomfortResponse created = discomfortService.createDiscomfort(request, uuid);

    // when
    DiscomfortResponse result = discomfortService.findDiscomfortById(created.getId(), uuid);

    // then
    assertThat(result.getTitle()).isEqualTo("테스트 불편함");
    assertThat(result.getDescription()).isEqualTo("테스트 설명");
  }

  @Test
  void ID로_불편함_조회_실패_존재하지_않는_ID() {
    // when & then
    assertThatThrownBy(() -> discomfortService.findDiscomfortById(999L, uuid))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("불편함을 찾을 수 없습니다");
  }
}
