<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../shared/header.jsp" %>
<main class="page">
    <div class="section-head">
        <div><p class="eyebrow">Admin</p><h1>Danh mục</h1></div>
    </div>
    <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
    <section class="two-col">
        <div class="panel">
            <h2>${empty editingCategory ? 'Tạo danh mục' : 'Sửa danh mục'}</h2>
            <form method="post" action="${pageContext.request.contextPath}/admin/category/save" class="form-grid">
                <input type="hidden" name="id" value="${empty editingCategory ? 0 : editingCategory.id}">
                <label>Tên danh mục <input name="name" maxlength="100" value="${empty editingCategory ? '' : editingCategory.name}" required></label>
                <label style="grid-column:1/-1">Mô tả <textarea name="description" maxlength="1000">${empty editingCategory ? '' : editingCategory.description}</textarea></label>
                <div class="form-actions">
                    <button type="submit">${empty editingCategory ? 'Lưu danh mục' : 'Lưu thay đổi'}</button>
                    <c:if test="${not empty editingCategory}">
                        <a class="btn ghost" href="${pageContext.request.contextPath}/admin/categories">Bỏ chọn</a>
                    </c:if>
                </div>
            </form>
        </div>
        <div>
            <table>
                <thead><tr><th>Mã</th><th>Tên</th><th>Mô tả</th><th>Thao tác</th></tr></thead>
                <tbody>
                <c:forEach items="${categories}" var="category">
                    <tr>
                        <td>#${category.id}</td>
                        <td>${category.name}</td>
                        <td>${category.description}</td>
                        <td>
                            <div class="table-actions">
                                <a class="btn ghost" href="${pageContext.request.contextPath}/admin/categories?editCategoryId=${category.id}">Sửa</a>
                                <form method="post" action="${pageContext.request.contextPath}/admin/category/delete" class="inline-form" onsubmit="return confirm('Xóa danh mục này?');">
                                    <input type="hidden" name="id" value="${category.id}">
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
