/**
 * Uncomfortable Hub 메인 애플리케이션
 * AJAX 통신, UUID 관리, UI 이벤트 처리를 담당합니다.
 */
class UncomfortableHub {
    constructor() {
        this.uuid = this.getOrCreateUUID();
        this.init();
    }

    /**
     * 애플리케이션 초기화
     */
    init() {
        this.loadDiscomforts();
        this.bindEvents();
        this.setupCharacterCounter();
    }

    /**
     * UUID 관리 - 로컬 스토리지에서 가져오거나 새로 생성
     */
    getOrCreateUUID() {
        let uuid = localStorage.getItem("uncomfortable_hub_uuid");
        if (!uuid) {
            uuid = this.generateUUID();
            localStorage.setItem("uncomfortable_hub_uuid", uuid);
        }
        return uuid;
    }

    /**
     * UUID 생성
     */
    generateUUID() {
        return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (c) {
            const r = (Math.random() * 16) | 0;
            const v = c === "x" ? r : (r & 0x3) | 0x8;
            return v.toString(16);
        });
    }

    /**
     * 이벤트 바인딩
     */
    bindEvents() {
        // 폼 제출 이벤트
        document.getElementById("discomfortForm").addEventListener("submit", (e) => {
            e.preventDefault();
            this.handleFormSubmit();
        });

        // 모달 등록 버튼
        document.getElementById("confirmRegister").addEventListener("click", () => {
            this.handleModalRegister();
        });
    }

    /**
     * 문자 카운터 설정
     */
    setupCharacterCounter() {
        const description = document.getElementById("description");
        const charCount = document.getElementById("charCount");

        description.addEventListener("input", () => {
            const count = description.value.length;
            charCount.textContent = count;

            // 500자에 가까워지면 색상 변경
            if (count > 450) {
                charCount.style.color = "#dc3545";
            } else if (count > 400) {
                charCount.style.color = "#ffc107";
            } else {
                charCount.style.color = "#6c757d";
            }
        });
    }

    /**
     * 폼 제출 처리
     */
    async handleFormSubmit() {
        const title = document.getElementById("title").value.trim();
        const description = document.getElementById("description").value.trim();

        // 폼 검증
        if (!title) {
            this.showAlert("Please enter a title.", "danger");
            document.getElementById("title").focus();
            return;
        }

        if (title.length > 255) {
            this.showAlert("Title cannot exceed 255 characters.", "danger");
            document.getElementById("title").focus();
            return;
        }

        if (description.length > 500) {
            this.showAlert("Description cannot exceed 500 characters.", "danger");
            document.getElementById("description").focus();
            return;
        }

        // 제출 버튼 비활성화
        const submitBtn = document.querySelector('#discomfortForm button[type="submit"]');
        const originalText = submitBtn.innerHTML;
        submitBtn.disabled = true;
        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Sharing...';

        try {
            await this.createDiscomfort(title, description);
            this.clearForm();
            this.showAlert("Discomfort shared successfully!", "success");
        } catch (error) {
            console.error("Discomfort registration failed:", error);
            this.showAlert("Failed to share discomfort. Please try again.", "danger");
        } finally {
            // 제출 버튼 복원
            submitBtn.disabled = false;
            submitBtn.innerHTML = originalText;
        }
    }

    /**
     * 모달 등록 처리
     */
    async handleModalRegister() {
        const title = document.getElementById("modalTitle").value.trim();
        const description = document.getElementById("modalDescription").value.trim();

        if (!title) {
            this.showAlert("Please enter a title.", "danger");
            return;
        }

        try {
            await this.createDiscomfort(title, description);
            this.closeModal("registerModal");
            this.showAlert("Discomfort shared successfully!", "success");
        } catch (error) {
            console.error("Discomfort registration failed:", error);
            this.showAlert("Failed to share discomfort. Please try again.", "danger");
        }
    }

    /**
     * 불편함 목록 로드
     */
    async loadDiscomforts() {
        try {
            // 로딩 상태 표시
            this.showLoadingState();

            const response = await fetch(`/discomforts/api?uuid=${this.uuid}`);
            if (!response.ok) {
                throw new Error(`Server response error: ${response.status}`);
            }
            const discomforts = await response.json();
            this.renderDiscomforts(discomforts);
        } catch (error) {
            console.error("Failed to load discomforts:", error);
            this.renderErrorState();
        }
    }

    /**
     * 로딩 상태 표시
     */
    showLoadingState() {
        const container = document.getElementById("discomfortList");
        container.innerHTML = `
            <div class="col-12 text-center py-5">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Loading...</span>
                </div>
                <p class="mt-3 text-muted">Loading discomforts...</p>
            </div>
        `;
    }

    /**
     * 불편함 목록 렌더링
     */
    renderDiscomforts(discomforts) {
        const container = document.getElementById("discomfortList");

        if (discomforts.length === 0) {
            container.innerHTML = this.getEmptyStateHTML();
            return;
        }

        const html = discomforts.map((discomfort) => this.getDiscomfortCardHTML(discomfort)).join("");
        container.innerHTML = html;

        // 카드 클릭 이벤트 바인딩
        this.bindCardEvents();

        // 애니메이션 효과 추가
        this.addFadeInAnimation();
    }

    /**
     * 페이드인 애니메이션 추가
     */
    addFadeInAnimation() {
        const cards = document.querySelectorAll(".discomfort-card");
        cards.forEach((card, index) => {
            card.style.opacity = "0";
            card.style.transform = "translateY(20px)";

            setTimeout(() => {
                card.style.transition = "opacity 0.5s ease, transform 0.5s ease";
                card.style.opacity = "1";
                card.style.transform = "translateY(0)";
            }, index * 100); // 순차적으로 나타나도록
        });
    }

    /**
     * 불편함 카드 HTML 생성
     */
    getDiscomfortCardHTML(discomfort) {
        const timeAgo = this.getTimeAgo(discomfort.createdAt);
        const likedClass = discomfort.hasLiked ? "liked" : "";

        return `
            <div class="col-12 col-md-6 col-lg-4">
                <div class="card discomfort-card shadow-sm h-100" data-id="${discomfort.id}">
                    <div class="card-body">
                        <h5 class="card-title">${this.escapeHtml(discomfort.title)}</h5>
                        <p class="card-text">${this.escapeHtml(discomfort.description)}</p>
                        <div class="card-meta">
                            <small class="text-muted">${timeAgo}</small>
                            <button class="like-btn ${likedClass}" data-id="${discomfort.id}">
                                <i class="fas fa-heart"></i>
                                <span class="ms-1">${discomfort.likeCount}</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    }

    /**
     * 빈 상태 HTML
     */
    getEmptyStateHTML() {
        return `
            <div class="col-12">
                <div class="empty-state">
                    <i class="fas fa-frown-open"></i>
                    <h4>No discomforts shared yet</h4>
                    <p>Be the first to share your uncomfortable moment!</p>
                </div>
            </div>
        `;
    }

    /**
     * 에러 상태 렌더링
     */
    renderErrorState() {
        const container = document.getElementById("discomfortList");
        container.innerHTML = `
            <div class="col-12">
                <div class="empty-state">
                    <i class="fas fa-exclamation-triangle"></i>
                    <h4>Unable to load discomforts</h4>
                    <p>Please try again later.</p>
                    <button class="btn btn-primary" onclick="location.reload()">Refresh</button>
                </div>
            </div>
        `;
    }

    /**
     * 카드 이벤트 바인딩
     */
    bindCardEvents() {
        // 카드 클릭 이벤트 (상세 조회)
        document.querySelectorAll(".discomfort-card").forEach((card) => {
            card.addEventListener("click", (e) => {
                if (!e.target.closest(".like-btn")) {
                    const id = card.dataset.id;
                    this.showDiscomfortDetail(id);
                }
            });
        });

        // 좋아요 버튼 클릭 이벤트
        document.querySelectorAll(".like-btn").forEach((btn) => {
            btn.addEventListener("click", (e) => {
                e.stopPropagation();
                const id = btn.dataset.id;
                this.toggleLike(id);
            });
        });
    }

    /**
     * 불편함 등록
     */
    async createDiscomfort(title, description) {
        const response = await fetch("/discomforts", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                title: title,
                description: description,
                uuid: this.uuid,
            }),
        });

        if (!response.ok) {
            throw new Error("Registration failed");
        }

        // 목록 새로고침
        await this.loadDiscomforts();
    }

    /**
     * 좋아요 토글
     */
    async toggleLike(discomfortId) {
        try {
            // 현재 좋아요 상태 확인
            const currentBtn = document.querySelector(`.like-btn[data-id="${discomfortId}"]`);
            const isCurrentlyLiked = currentBtn?.classList.contains("liked");

            const url = isCurrentlyLiked ? "/likes/cancel" : "/likes";

            const response = await fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    discomfortId: parseInt(discomfortId),
                    uuid: this.uuid,
                }),
            });

            if (!response.ok) {
                throw new Error("Like processing failed");
            }

            const result = await response.json();

            // 성공 메시지 표시
            const message = isCurrentlyLiked ? "Like removed." : "Like added.";
            this.showAlert(message, "success");

            // 목록 새로고침
            await this.loadDiscomforts();
        } catch (error) {
            console.error("Like processing failed:", error);
            this.showAlert("Failed to process like.", "danger");
        }
    }

    /**
     * 불편함 상세 조회
     */
    async showDiscomfortDetail(id) {
        try {
            const response = await fetch(`/discomforts/api/${id}?uuid=${this.uuid}`);
            if (!response.ok) {
                throw new Error("Detail view failed");
            }
            const discomfort = await response.json();
            this.renderDiscomfortDetail(discomfort);
            this.openModal("detailModal");
        } catch (error) {
            console.error("Detail view failed:", error);
            this.showAlert("Unable to load details.", "danger");
        }
    }

    /**
     * 불편함 상세 내용 렌더링
     */
    renderDiscomfortDetail(discomfort) {
        const timeAgo = this.getTimeAgo(discomfort.createdAt);
        const likedClass = discomfort.hasLiked ? "liked" : "";

        document.getElementById("detailContent").innerHTML = `
            <div class="mb-3">
                <h4 class="fw-bold">${this.escapeHtml(discomfort.title)}</h4>
                <small class="text-muted">${timeAgo}</small>
            </div>
            <div class="mb-4">
                <p class="lead">${this.escapeHtml(discomfort.description)}</p>
            </div>
            <div class="d-flex justify-content-between align-items-center">
                <button class="like-btn ${likedClass}" data-id="${discomfort.id}">
                    <i class="fas fa-heart"></i>
                    <span class="ms-2">${discomfort.likeCount} people agree</span>
                </button>
            </div>
        `;

        // 상세 모달의 좋아요 버튼 이벤트 바인딩
        const likeBtn = document.querySelector("#detailContent .like-btn");
        if (likeBtn) {
            likeBtn.addEventListener("click", (e) => {
                e.stopPropagation();
                this.toggleLike(discomfort.id);
            });
        }
    }

    /**
     * 폼 초기화
     */
    clearForm() {
        document.getElementById("title").value = "";
        document.getElementById("description").value = "";
        document.getElementById("charCount").textContent = "0";
        document.getElementById("charCount").style.color = "#6c757d";
    }

    /**
     * 모달 열기
     */
    openModal(modalId) {
        const modal = new bootstrap.Modal(document.getElementById(modalId));
        modal.show();
    }

    /**
     * 모달 닫기
     */
    closeModal(modalId) {
        const modal = bootstrap.Modal.getInstance(document.getElementById(modalId));
        if (modal) {
            modal.hide();
        }
    }

    /**
     * 알림 메시지 표시
     */
    showAlert(message, type = "info") {
        const alertDiv = document.createElement("div");
        alertDiv.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
        alertDiv.style.cssText = "top: 20px; right: 20px; z-index: 9999; min-width: 300px;";
        alertDiv.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;

        document.body.appendChild(alertDiv);

        // 3초 후 자동 제거
        setTimeout(() => {
            if (alertDiv.parentNode) {
                alertDiv.parentNode.removeChild(alertDiv);
            }
        }, 3000);
    }

    /**
     * 시간 경과 표시
     */
    getTimeAgo(dateString) {
        const now = new Date();
        const date = new Date(dateString);
        const diffInSeconds = Math.floor((now - date) / 1000);

        if (diffInSeconds < 60) {
            return "just now";
        } else if (diffInSeconds < 3600) {
            const minutes = Math.floor(diffInSeconds / 60);
            return `${minutes} minutes ago`;
        } else if (diffInSeconds < 86400) {
            const hours = Math.floor(diffInSeconds / 3600);
            return `${hours} hours ago`;
        } else {
            const days = Math.floor(diffInSeconds / 86400);
            return `${days} days ago`;
        }
    }

    /**
     * HTML 이스케이프
     */
    escapeHtml(text) {
        const div = document.createElement("div");
        div.textContent = text;
        return div.innerHTML;
    }
}

// 페이지 로드 시 애플리케이션 초기화
document.addEventListener("DOMContentLoaded", () => {
    new UncomfortableHub();
});
