<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="shared/header.jsp" %>
<section class="hero">
    <div class="hero-content">
        <p class="eyebrow">Thiên · Nhân · Ma</p>
        <h1>Lẩu Tam Giới</h1>
        <p>Không gian lẩu Á Đông thanh lịch, nơi vị thanh của Thiên Giới, vị hòa của Nhân Giới và vị cay sâu của Ma Giới gặp nhau trong một bàn tiệc.</p>
        <div class="actions">
            <a class="btn" href="${pageContext.request.contextPath}/menu">Xem thực đơn</a>
            <c:choose>
                <c:when test="${not empty sessionScope.account && sessionScope.account.role == 'CUSTOMER'}">
                    <a class="btn secondary" href="${pageContext.request.contextPath}/booking/new">Đặt bàn</a>
                </c:when>
                <c:otherwise>
                    <a class="btn secondary" href="${pageContext.request.contextPath}/login">Đăng nhập để đặt bàn</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</section>
<main class="page">
    <div class="section-head">
        <div>
            <p class="eyebrow">Set nổi bật</p>
            <h2>Ba tầng hương vị</h2>
        </div>
        <a class="btn ghost" href="${pageContext.request.contextPath}/menu">Khám phá</a>
    </div>
    <div class="grid">
        <article class="card"><div class="card-body"><h3>Thiên Giới</h3><p class="muted">Thanh nhã, trong vị, hợp những buổi gặp mặt nhẹ nhàng.</p></div></article>
        <article class="card"><div class="card-body"><h3>Nhân Giới</h3><p class="muted">Cân bằng, dễ dùng, đủ đầy cho gia đình và nhóm bạn.</p></div></article>
        <article class="card"><div class="card-body"><h3>Ma Giới</h3><p class="muted">Cay nồng, đậm sâu, dành cho thực khách thích trải nghiệm mạnh.</p></div></article>
    </div>
</main>
<%@ include file="shared/footer.jsp" %>
