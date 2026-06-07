<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="../shared/header.jsp" %>
<main class="page">
    <div class="section-head">
        <div><p class="eyebrow">Revenue</p><h1>Báo cáo doanh thu</h1></div>
    </div>
    <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
    <section class="toolbar">
        <form method="get" action="${pageContext.request.contextPath}/admin/reports">
            <label>Kiểu báo cáo
                <select name="type">
                    <option value="daily" ${type == 'daily' ? 'selected' : ''}>Theo ngày</option>
                    <option value="monthly" ${type == 'monthly' ? 'selected' : ''}>Theo tháng</option>
                </select>
            </label>
            <label>Từ ngày <input type="date" name="from" value="${from}"></label>
            <label>Đến ngày <input type="date" name="to" value="${to}"></label>
            <button type="submit">Xem</button>
        </form>
    </section>
    <table>
        <thead><tr><th>Kỳ</th><th>Số hóa đơn</th><th>Doanh thu</th></tr></thead>
        <tbody>
        <c:forEach items="${rows}" var="row">
            <tr>
                <td>${row.period}</td>
                <td>${row.invoiceCount}</td>
                <td class="price"><fmt:formatNumber value="${row.total}" type="number" groupingUsed="true"/> VND</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>
<%@ include file="../shared/footer.jsp" %>
