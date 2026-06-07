<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../shared/header.jsp" %>
<main class="auth-shell">
    <section class="panel">
        <h1>Đăng nhập</h1>
        <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
        <form method="post" action="${pageContext.request.contextPath}/login" class="form-grid">
            <label>Tài khoản
                <input name="username" minlength="4" maxlength="50" pattern="[A-Za-z0-9_]{4,50}" required>
            </label>
            <label>Mật khẩu
                <input type="password" name="password" minlength="6" maxlength="255" required>
            </label>
            <button type="submit">Vào hệ thống</button>
        </form>
    </section>
</main>
<%@ include file="../shared/footer.jsp" %>
