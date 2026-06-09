<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="../shared/header.jsp" %>
<main class="page">
    <div class="section-head">
        <div>
            <p class="eyebrow">Menu</p>
            <h1>Thực đơn Tam Giới</h1>
        </div>
    </div>
    <section class="toolbar">
        <form method="get" action="${pageContext.request.contextPath}/menu">
            <label>Tìm món <input name="q" maxlength="80" value="${param.q}" placeholder="Tên món, nguyên liệu"></label>
            <label>Danh mục
                <select name="categoryId">
                    <option value="">Tất cả</option>
                    <c:forEach items="${categories}" var="category">
                        <option value="${category.id}" ${param.categoryId == category.id ? 'selected' : ''}>${category.name}</option>
                    </c:forEach>
                </select>
            </label>
            <button type="submit">Tìm kiếm</button>
        </form>
    </section>
    <section class="grid">
        <c:forEach items="${dishes}" var="dish">
            <article class="card">
                <img class="dish-img" src="${dish.imageUrl}" alt="${dish.name}" onerror="this.onerror=null;this.src='https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=900&q=80';">
                <div class="card-body">
                    <span class="badge">${dish.categoryName}</span>
                    <h3>${dish.name}</h3>
                    <p class="muted">${dish.description}</p>
                    <p class="price"><fmt:formatNumber value="${dish.price}" type="number" groupingUsed="true"/> VND</p>
                    <div class="actions">
                        <a class="btn ghost" href="${pageContext.request.contextPath}/dish?id=${dish.id}">Chi tiết</a>
                    </div>
                </div>
            </article>
        </c:forEach>
    </section>
</main>
<%@ include file="../shared/footer.jsp" %>
