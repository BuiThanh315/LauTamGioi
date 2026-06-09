<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="../shared/header.jsp" %>
<main class="page">
    <div class="section-head">
        <div><p class="eyebrow">Admin</p><h1>Quản lý món ăn</h1></div>
    </div>
    <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
    <section class="two-col">
        <div class="panel">
            <h2>${empty editingDish ? 'Tạo món ăn' : 'Sửa món ăn'}</h2>
            <form method="post" action="${pageContext.request.contextPath}/admin/dish/save" class="form-grid">
                <input type="hidden" name="id" value="${empty editingDish ? 0 : editingDish.id}">
                <label>Tên món <input name="name" maxlength="150" value="${empty editingDish ? '' : editingDish.name}" required></label>
                <label>Danh mục
                    <select name="categoryId" required>
                        <c:forEach items="${categories}" var="category">
                            <option value="${category.id}" ${not empty editingDish && editingDish.categoryId == category.id ? 'selected' : ''}>${category.name}</option>
                        </c:forEach>
                    </select>
                </label>
                <label>Giá <input type="number" name="price" min="1000" step="1000" value="${empty editingDish ? '' : editingDish.price}" required></label>
                <label>Ảnh <input name="imageUrl" maxlength="255" placeholder="https://..." value="${empty editingDish ? '' : editingDish.imageUrl}"></label>
                <label>Trạng thái
                    <select name="status">
                        <option value="AVAILABLE" ${empty editingDish || editingDish.status == 'AVAILABLE' ? 'selected' : ''}>Đang bán</option>
                        <option value="OUT_OF_STOCK" ${not empty editingDish && editingDish.status == 'OUT_OF_STOCK' ? 'selected' : ''}>Hết món</option>
                    </select>
                </label>
                <label style="grid-column:1/-1">Mô tả <textarea name="description" maxlength="1000">${empty editingDish ? '' : editingDish.description}</textarea></label>
                <div class="form-actions">
                    <button type="submit">${empty editingDish ? 'Lưu món ăn' : 'Lưu thay đổi'}</button>
                    <c:if test="${not empty editingDish}">
                        <a class="btn ghost" href="${pageContext.request.contextPath}/admin/dishes">Bỏ chọn</a>
                    </c:if>
                </div>
            </form>
        </div>
        <div>
            <table>
                <thead><tr><th>Mã</th><th>Món</th><th>Danh mục</th><th>Giá</th><th>Trạng thái</th><th>Thao tác</th></tr></thead>
                <tbody>
                <c:forEach items="${dishes}" var="dish">
                    <tr>
                        <td>#${dish.id}</td>
                        <td>${dish.name}<br><span class="muted">${dish.description}</span></td>
                        <td>${dish.categoryName}</td>
                        <td><fmt:formatNumber value="${dish.price}" type="number" groupingUsed="true"/> VND</td>
                        <td><span class="badge">${dish.status}</span></td>
                        <td>
                            <div class="table-actions">
                                <a class="btn ghost" href="${pageContext.request.contextPath}/admin/dishes?editDishId=${dish.id}">Sửa</a>
                                <form method="post" action="${pageContext.request.contextPath}/admin/dish/delete" class="inline-form" onsubmit="return confirm('Xóa món ăn này?');">
                                    <input type="hidden" name="id" value="${dish.id}">
                                    <button type="submit" class="secondary">Xóa</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>
</main>
<%@ include file="../shared/footer.jsp" %>
