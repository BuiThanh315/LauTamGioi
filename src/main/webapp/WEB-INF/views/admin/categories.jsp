<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../shared/header.jsp" %>
<main class="page">
    <div class="section-head">
        <div><p class="eyebrow">Admin</p><h1>Danh mục</h1></div>
    </div>
    <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
    <section class="panel">
        <form method="post" action="${pageContext.request.contextPath}/admin/category/save" class="form-grid">
            <input type="hidden" name="id" value="0">
            <label>Tên danh mục <input name="name" maxlength="100" required></label>
            <label>Mô tả <input name="description" maxlength="1000"></label>
            <button type="submit">Lưu danh mục</button>
        </form>
    </section>
    <table>
        <thead><tr><th>Mã</th><th>Tên</th><th>Mô tả</th><th></th></tr></thead>
        <tbody>
        <c:forEach items="${categories}" var="category">
            <tr>
                <td>#${category.id}</td>
                <td>${category.name}</td>
                <td>${category.description}</td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/admin/category/delete">
                        <input type="hidden" name="id" value="${category.id}">
                        <button type="submit" class="secondary">Xóa</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>
<%@ include file="../shared/footer.jsp" %>
