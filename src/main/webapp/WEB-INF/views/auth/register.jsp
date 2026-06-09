<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../shared/header.jsp" %>
<main class="auth-shell auth-centered">
    <section class="panel auth-panel auth-panel-compact">
        <div class="auth-panel-head">
            <p class="eyebrow auth-eyebrow">Khách hàng mới</p>
            <h1>Đăng ký tài khoản</h1>
            <p class="muted">Tạo tài khoản để lưu lịch sử đặt bàn và cập nhật thông tin liên hệ cho những lần ghé sau.</p>
        </div>
        <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
        <form method="post" action="${pageContext.request.contextPath}/register" class="form-grid auth-form">
            <label>Họ tên <input name="fullName" maxlength="100" autocomplete="name" required></label>
            <label>Tài khoản <input name="username" minlength="4" maxlength="50" pattern="[A-Za-z0-9_]{4,50}" autocomplete="username" required></label>
            <label>Mật khẩu <input type="password" name="password" minlength="6" maxlength="255" autocomplete="new-password" required></label>
            <label>Email <input type="email" name="email" maxlength="100" autocomplete="email"></label>
            <label>Số điện thoại <input name="phone" maxlength="20" pattern="(0|\+84)\d{9}" autocomplete="tel"></label>
            <button type="submit">Tạo tài khoản</button>
        </form>
        <p class="auth-foot">Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập</a></p>
    </section>
</main>
<%@ include file="../shared/footer.jsp" %>
