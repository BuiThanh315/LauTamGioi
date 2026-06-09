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
    <table>
        <thead>
        <tr><th>Mã</th><th>Khách</th><th>Ngày giờ</th><th>Loại bàn</th><th>Bàn hiện tại</th><th>Trạng thái</th><th>Thao tác</th></tr>
        </thead>
        <tbody>
        <c:forEach items="${bookings}" var="booking">
            <tr>
                <td>#${booking.id}</td>
                <td>${booking.customerName}<br><span class="muted">${booking.phone}</span></td>
                <td>${booking.bookingDate}<br>${booking.bookingTime}</td>
                <td>${booking.tableTypeLabel}</td>
                <td>${empty booking.assignedTableNumber ? '-' : booking.assignedTableNumber}</td>
                <td><span class="badge">${booking.status}</span></td>
                <td>
                    <div class="table-actions">
                        <c:if test="${booking.status == 'CONFIRMED'}">
                            <form method="post" action="${pageContext.request.contextPath}/staff/checkin" class="inline-form">
                                <input type="hidden" name="bookingId" value="${booking.id}">
                                <select name="tableId" required>
                                    <c:forEach items="${tables}" var="table">
                                        <c:if test="${table.typeId == booking.tableTypeId && table.status == 'EMPTY'}">
                                            <option value="${table.id}">${table.code} · ${table.typeName} · ${table.capacity} khách</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                                <button type="submit">Nhận bàn</button>
                            </form>
                        </c:if>
                        <c:if test="${booking.status == 'SEATED'}">
                            <form method="post" action="${pageContext.request.contextPath}/staff/pay" class="inline-form">
                                <input type="hidden" name="bookingId" value="${booking.id}">
                                <select name="paymentMethod">
                                    <option value="CASH">Tiền mặt</option>
                                    <option value="BANK_TRANSFER">Chuyển khoản</option>
                                </select>
                                <button type="submit">Xác nhận thanh toán</button>
                            </form>
                        </c:if>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>
<%@ include file="../shared/footer.jsp" %>
