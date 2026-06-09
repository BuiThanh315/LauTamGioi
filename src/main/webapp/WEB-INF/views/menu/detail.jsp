<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="../shared/header.jsp" %>
<main class="page">
    <c:choose>
        <c:when test="${empty dish}">
            <section class="panel"><h1>Không tìm thấy món</h1></section>
        </c:when>
        <c:otherwise>
            <section class="card">
                <img class="dish-img" src="${dish.imageUrl}" alt="${dish.name}" onerror="this.onerror=null;this.src='https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=900&q=80';">
                <div class="card-body">
                    <span class="badge">${dish.categoryName}</span>
                    <h1>${dish.name}</h1>
                    <p class="muted">${dish.description}</p>
                    <p class="price"><fmt:formatNumber value="${dish.price}" type="number" groupingUsed="true"/> VND</p>
                    <c:if test="${not empty sessionScope.account && sessionScope.account.role == 'CUSTOMER'}">
                        <a class="btn" href="${pageContext.request.contextPath}/booking/new">Đặt bàn</a>
                    </c:if>
                </div>
            </section>
        </c:otherwise>
    </c:choose>
</main>
<%@ include file="../shared/footer.jsp" %>
