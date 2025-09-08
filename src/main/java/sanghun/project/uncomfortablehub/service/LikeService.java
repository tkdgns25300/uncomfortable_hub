package sanghun.project.uncomfortablehub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sanghun.project.uncomfortablehub.domain.Like;
import sanghun.project.uncomfortablehub.repository.LikeRepository;

/** 좋아요 관련 비즈니스 로직을 처리하는 서비스 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class LikeService {

  private final LikeRepository likeRepository;

  /** 좋아요를 등록합니다. */
  @Transactional
  public void createLike(Long discomfortId, String uuid) {
    log.info("Creating like for discomfort id: {} by uuid: {}", discomfortId, uuid);

    // 1. 중복 좋아요 확인
    if (likeRepository.existsByDiscomfortIdAndUuid(discomfortId, uuid)) {
      log.warn("Like already exists for discomfort id: {} by uuid: {}", discomfortId, uuid);
      return; // 이미 좋아요를 누른 경우 무시
    }

    // 2. 좋아요 엔티티 생성 및 저장
    Like like = Like.create(discomfortId, uuid);
    likeRepository.save(like);

    log.info("Successfully created like for discomfort id: {} by uuid: {}", discomfortId, uuid);
  }

  /** 좋아요를 취소합니다. */
  @Transactional
  public void cancelLike(Long discomfortId, String uuid) {
    log.info("Canceling like for discomfort id: {} by uuid: {}", discomfortId, uuid);

    // 1. 좋아요 존재 여부 확인
    if (!likeRepository.existsByDiscomfortIdAndUuid(discomfortId, uuid)) {
      log.warn("Like does not exist for discomfort id: {} by uuid: {}", discomfortId, uuid);
      return; // 좋아요가 없는 경우 무시
    }

    // 2. 좋아요 삭제
    likeRepository.deleteByDiscomfortIdAndUuid(discomfortId, uuid);

    log.info("Successfully canceled like for discomfort id: {} by uuid: {}", discomfortId, uuid);
  }

  /** 특정 불편함의 좋아요 수를 조회합니다. */
  public long getLikeCount(Long discomfortId) {
    log.info("Getting like count for discomfort id: {}", discomfortId);

    long count = likeRepository.countByDiscomfortId(discomfortId);
    log.info("Like count for discomfort id: {} is {}", discomfortId, count);

    return count;
  }

  /** UUID로 특정 불편함에 좋아요를 눌렀는지 확인합니다. */
  public boolean hasLiked(Long discomfortId, String uuid) {
    log.info("Checking if uuid: {} has liked discomfort id: {}", uuid, discomfortId);

    boolean hasLiked = likeRepository.existsByDiscomfortIdAndUuid(discomfortId, uuid);
    log.info("UUID: {} has liked discomfort id: {}: {}", uuid, discomfortId, hasLiked);

    return hasLiked;
  }
}
