package sanghun.project.uncomfortablehub.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sanghun.project.uncomfortablehub.domain.Discomfort;
import sanghun.project.uncomfortablehub.dto.DiscomfortListResponse;
import sanghun.project.uncomfortablehub.dto.DiscomfortRequest;
import sanghun.project.uncomfortablehub.dto.DiscomfortResponse;
import sanghun.project.uncomfortablehub.repository.DiscomfortRepository;

/** 불편함 관련 비즈니스 로직을 처리하는 서비스 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class DiscomfortService {

  private final DiscomfortRepository discomfortRepository;
  private final LikeService likeService;

  /** 불편함을 등록합니다. */
  @Transactional
  public DiscomfortResponse createDiscomfort(DiscomfortRequest request, String uuid) {
    log.info("Creating new discomfort with title: {} by uuid: {}", request.getTitle(), uuid);

    // 1. Discomfort 엔티티 생성
    Discomfort discomfort = Discomfort.create(request.getTitle(), uuid, request.getDescription());

    // 2. 데이터베이스에 저장
    Discomfort savedDiscomfort = discomfortRepository.save(discomfort);

    log.info("Successfully created discomfort with id: {}", savedDiscomfort.getId());

    // 3. DiscomfortResponse로 변환하여 반환
    return DiscomfortResponse.from(savedDiscomfort);
  }

  /** 모든 불편함을 조회합니다. */
  public List<DiscomfortListResponse> findAllDiscomforts(String userUuid) {
    log.info("Finding all discomforts for user: {}", userUuid);

    // 1. 모든 불편함 조회 (생성일시 내림차순)
    List<Discomfort> discomforts = discomfortRepository.findAllByOrderByCreatedAtDesc();

    // 2. 각 불편함에 대해 좋아요 정보를 포함하여 DTO 변환
    return discomforts.stream()
        .map(
            discomfort -> {
              long likeCount = likeService.getLikeCount(discomfort.getId());
              boolean hasLiked = likeService.hasLiked(discomfort.getId(), userUuid);
              return DiscomfortListResponse.from(discomfort, likeCount, hasLiked);
            })
        .collect(Collectors.toList());
  }

  /** ID로 불편함을 조회합니다. */
  public DiscomfortResponse findDiscomfortById(Long id, String userUuid) {
    log.info("Finding discomfort by id: {} for user: {}", id, userUuid);

    // 1. 불편함 조회
    Discomfort discomfort =
        discomfortRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("불편함을 찾을 수 없습니다. ID: " + id));

    // 2. 좋아요 정보 조회
    long likeCount = likeService.getLikeCount(discomfort.getId());
    boolean hasLiked = likeService.hasLiked(discomfort.getId(), userUuid);

    // 3. DiscomfortResponse로 변환하여 반환
    return DiscomfortResponse.from(discomfort, likeCount, hasLiked);
  }

  /** 불편함을 수정합니다. */
  @Transactional
  public DiscomfortResponse updateDiscomfort(Long id, DiscomfortRequest request, String uuid) {
    log.info("Updating discomfort with id: {} by uuid: {}", id, uuid);

    // 1. 불편함 존재 여부 및 소유권 확인
    Discomfort discomfort =
        discomfortRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("불편함을 찾을 수 없습니다. ID: " + id));

    if (!discomfort.getUuid().equals(uuid)) {
      throw new IllegalArgumentException("본인이 작성한 불편함만 수정할 수 있습니다.");
    }

    // 2. 불편함 정보 업데이트
    discomfort.update(request.getTitle(), request.getDescription());

    log.info("Successfully updated discomfort with id: {}", id);

    // 3. DiscomfortResponse로 변환하여 반환
    return DiscomfortResponse.from(discomfort);
  }

  /** 불편함을 삭제합니다. */
  @Transactional
  public void deleteDiscomfort(Long id, String uuid) {
    log.info("Deleting discomfort with id: {} by uuid: {}", id, uuid);

    // 1. 불편함 존재 여부 및 소유권 확인
    Discomfort discomfort =
        discomfortRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("불편함을 찾을 수 없습니다. ID: " + id));

    if (!discomfort.getUuid().equals(uuid)) {
      throw new IllegalArgumentException("본인이 작성한 불편함만 삭제할 수 있습니다.");
    }

    // 2. 불편함 삭제
    discomfortRepository.delete(discomfort);

    log.info("Successfully deleted discomfort with id: {}", id);
  }
}
