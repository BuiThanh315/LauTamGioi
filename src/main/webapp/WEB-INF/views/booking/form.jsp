<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../shared/header.jsp" %>
<main class="page">
    <div class="section-head">
        <div>
            <p class="eyebrow">Booking</p>
            <h1>Đặt bàn</h1>
        </div>
    </div>
    <section class="panel">
        <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
        <form method="post" action="${pageContext.request.contextPath}/booking/new" class="form-grid">
            <label>Tên khách <input name="customerName" maxlength="100" value="${sessionScope.account.fullName}" required></label>
            <label>Số điện thoại <input name="customerPhone" maxlength="20" pattern="(0|\+84)\d{9}" value="${sessionScope.account.phone}" required></label>
            <label>Loại bàn
                <select name="tableTypeId" required>
                    <c:forEach items="${tableTypes}" var="type">
                        <option value="${type.id}">${type.label}</option>
                    </c:forEach>
                </select>
            </label>
            <label>Ngày <input type="date" name="bookingDate" required></label>
            <label>Giờ <input type="time" name="bookingTime" min="10:00" max="22:00" required></label>
            <label style="grid-column:1/-1">Ghi chú
                <textarea name="note" maxlength="1000" placeholder="Không gian, món muốn giữ trước, yêu cầu đặc biệt"></textarea>
            </label>
            <button type="submit">Gửi đơn đặt bàn</button>
        </form>
    </section>
</main>
<%@ include file="../shared/footer.jsp" %>
