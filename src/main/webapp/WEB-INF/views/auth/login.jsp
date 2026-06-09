<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../shared/header.jsp" %>
<main class="auth-shell auth-centered">
    <section class="panel auth-panel auth-panel-compact">
        <div class="auth-panel-head">
            <p class="eyebrow auth-eyebrow">Tam Giới</p>
            <h1>Đăng nhập</h1>
            <p class="muted">Tiếp tục theo dõi đặt bàn, thực đơn và các thao tác theo đúng vai trò của bạn trong hệ thống.</p>
        </div>
        <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
        <form method="post" action="${pageContext.request.contextPath}/login" class="form-grid auth-form">
            <label>Tài khoản
                <input name="username" minlength="4" maxlength="50" pattern="[A-Za-z0-9_]{4,50}" autocomplete="username" required>
            </label>
            <label>Mật khẩu
                <input type="password" name="password" minlength="6" maxlength="255" autocomplete="current-password" required>
            </label>
            <button type="submit">Đăng nhập</button>
        </form>
        <p class="auth-foot">Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Tạo tài khoản khách hàng</a></p>
    </section>
</main>
<%@ include file="../shared/footer.jsp" %>
