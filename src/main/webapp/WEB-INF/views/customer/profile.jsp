<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../shared/header.jsp" %>
<main class="page">
    <div class="section-head">
        <div>
            <p class="eyebrow">Customer</p>
            <h1>Thông tin tài khoản</h1>
        </div>
    </div>
    <section class="two-col profile-layout">
        <aside class="panel profile-summary">
            <span class="profile-mark">Tam Giới</span>
            <h2>${profile.fullName}</h2>
            <p class="muted">Tài khoản khách hàng dùng để theo dõi lịch sử đặt bàn và cập nhật thông tin liên hệ.</p>
            <dl class="profile-meta">
                <div><dt>Tài khoản</dt><dd>${profile.username}</dd></div>
                <div><dt>Vai trò</dt><dd>${profile.role}</dd></div>
                <div><dt>Trạng thái</dt><dd>${profile.status}</dd></div>
            </dl>
        </aside>
        <section class="panel">
            <h2>Cập nhật liên hệ</h2>
            <p class="muted">Bạn có thể chỉnh sửa email và số điện thoại để hệ thống xác nhận đặt bàn chính xác hơn.</p>
            <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
            <form method="post" action="${pageContext.request.contextPath}/customer/profile" class="form-grid">
                <label>Họ tên
                    <input value="${profile.fullName}" disabled>
                </label>
                <label>Tài khoản
                    <input value="${profile.username}" disabled>
                </label>
                <label>Email
                    <input type="email" name="email" maxlength="100" value="${profile.email}">
                </label>
                <label>Số điện thoại
                    <input name="phone" maxlength="20" pattern="(0|\+84)\d{9}" value="${profile.phone}" required>
                </label>
                <div class="form-actions" style="grid-column:1/-1">
                    <button type="submit">Cập nhật</button>
                </div>
            </form>
        </section>
    </section>
</main>
<%@ include file="../shared/footer.jsp" %>
