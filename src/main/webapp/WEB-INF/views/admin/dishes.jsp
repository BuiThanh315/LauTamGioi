<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="../shared/header.jsp" %>
<main class="page">
    <div class="section-head">
        <div><p class="eyebrow">Admin</p><h1>Quản lý món</h1></div>
    </div>
    <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
    <section class="panel">
        <form method="post" action="${pageContext.request.contextPath}/admin/dish/save" class="form-grid">
            <input type="hidden" name="id" value="0">
            <label>Tên món <input name="name" maxlength="150" required></label>
            <label>Danh mục
                <select name="categoryId" required>
                    <c:forEach items="${categories}" var="category">
                        <option value="${category.id}">${category.name}</option>
                    </c:forEach>
                </select>
            </label>
            <label>Giá <input type="number" name="price" min="1000" step="1000" required></label>
            <label>Ảnh <input name="imageUrl" maxlength="255" placeholder="https://..."></label>
            <label style="grid-column:1/-1">Mô tả <textarea name="description" maxlength="1000"></textarea></label>
            <label>Còn món <input type="checkbox" name="active" checked></label>
            <button type="submit">Lưu món</button>
        </form>
    </section>
    <table>
        <thead><tr><th>Mã</th><th>Món</th><th>Danh mục</th><th>Giá</th><th>Trạng thái</th><th></th></tr></thead>
        <tbody>
        <c:forEach items="${dishes}" var="dish">
            <tr>
                <td>#${dish.id}</td>
                <td>${dish.name}<br><span class="muted">${dish.description}</span></td>
                <td>${dish.categoryName}</td>
                <td><fmt:formatNumber value="${dish.price}" type="number" groupingUsed="true"/> VND</td>
                <td><span class="badge">${dish.status}</span></td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/admin/dish/delete">
                        <input type="hidden" name="id" value="${dish.id}">
                        <button type="submit" class="secondary">Hết món</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>
<%@ include file="../shared/footer.jsp" %>
