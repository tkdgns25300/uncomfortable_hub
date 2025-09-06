package sanghun.project.uncomfortablehub.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** 좋아요 관련 비즈니스 로직을 처리하는 서비스 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class LikeService {

  /** 좋아요를 등록합니다. */
  @Transactional
  public void createLike(Long discomfortId, String uuid) {
    log.info("Creating like for discomfort id: {} by uuid: {}", discomfortId, uuid);
    // TODO: 구현 예정
  }

  /** 좋아요를 취소합니다. */
  @Transactional
  public void cancelLike(Long discomfortId, String uuid) {
    log.info("Canceling like for discomfort id: {} by uuid: {}", discomfortId, uuid);
    // TODO: 구현 예정
  }

  /** 특정 불편함의 좋아요 수를 조회합니다. */
  public long getLikeCount(Long discomfortId) {
    log.info("Getting like count for discomfort id: {}", discomfortId);
    // TODO: 구현 예정
    return 0;
  }

  /** UUID로 특정 불편함에 좋아요를 눌렀는지 확인합니다. */
  public boolean hasLiked(Long discomfortId, String uuid) {
    log.info("Checking if uuid: {} has liked discomfort id: {}", uuid, discomfortId);
    // TODO: 구현 예정
    return false;
  }
}
