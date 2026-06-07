<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../shared/header.jsp" %>
<main class="auth-shell">
    <section class="panel">
        <h1>Đăng ký</h1>
        <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
        <form method="post" action="${pageContext.request.contextPath}/register" class="form-grid">
            <label>Họ tên <input name="fullName" maxlength="100" required></label>
            <label>Tài khoản <input name="username" minlength="4" maxlength="50" pattern="[A-Za-z0-9_]{4,50}" required></label>
            <label>Mật khẩu <input type="password" name="password" minlength="6" maxlength="255" required></label>
            <label>Email <input type="email" name="email" maxlength="100"></label>
            <label>Số điện thoại <input name="phone" maxlength="20" pattern="(0|\+84)\d{9}"></label>
            <button type="submit">Tạo tài khoản</button>
        </form>
    </section>
</main>
<%@ include file="../shared/footer.jsp" %>
