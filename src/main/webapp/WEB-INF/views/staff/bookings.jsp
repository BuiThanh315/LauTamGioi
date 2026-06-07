<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../shared/header.jsp" %>
<main class="page">
    <div class="section-head">
        <div>
            <p class="eyebrow">Staff</p>
            <h1>Điều phối đặt bàn</h1>
        </div>
    </div>
    <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
    <section class="panel">
        <h2>Thanh toán order</h2>
        <form method="post" action="${pageContext.request.contextPath}/staff/pay" class="form-grid">
            <label>Mã order <input type="number" name="orderId" min="1" max="1000000" required></label>
            <label>Phương thức
                <select name="paymentMethod">
                    <option value="CASH">Tiền mặt</option>
                    <option value="BANK_TRANSFER">Chuyển khoản</option>
                </select>
            </label>
            <button type="submit">Thanh toán</button>
        </form>
    </section>
    <table>
        <thead>
        <tr><th>Mã</th><th>Khách</th><th>Ngày giờ</th><th>Loại bàn</th><th>Trạng thái</th><th>Xác nhận</th><th>Tạo order</th></tr>
        </thead>
        <tbody>
        <c:forEach items="${bookings}" var="booking">
            <tr>
                <td>#${booking.id}</td>
                <td>${booking.customerName}<br><span class="muted">${booking.phone}</span></td>
                <td>${booking.bookingDate}<br>${booking.bookingTime}</td>
                <td>${booking.tableClass} · ${booking.capacity} khách</td>
                <td><span class="badge">${booking.status}</span></td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/staff/confirm">
                        <input type="hidden" name="bookingId" value="${booking.id}">
                        <select name="tableId" required>
                            <c:forEach items="${tables}" var="table">
                                <option value="${table.id}">${table.code} · ${table.typeName} · ${table.capacity} khách · ${table.status}</option>
                            </c:forEach>
                        </select>
                        <button type="submit">Xác nhận</button>
                    </form>
                </td>
                <td>
                    <form method="post" action="${pageContext.request.contextPath}/staff/order">
                        <input type="hidden" name="bookingId" value="${booking.id}">
                        <select name="tableId" required>
                            <c:forEach items="${tables}" var="table">
                                <option value="${table.id}">${table.code}</option>
                            </c:forEach>
                        </select>
                        <select name="dishId" required>
                            <c:forEach items="${dishes}" var="dish">
                                <option value="${dish.id}">${dish.name}</option>
                            </c:forEach>
                        </select>
                        <input type="number" name="quantity" min="1" max="100" value="1" required>
                        <button type="submit">Tạo</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>
<%@ include file="../shared/footer.jsp" %>
