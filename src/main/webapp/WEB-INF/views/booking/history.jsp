<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../shared/header.jsp" %>
<main class="page">
    <div class="section-head">
        <div>
            <p class="eyebrow">Booking</p>
            <h1>Lịch sử đặt bàn</h1>
        </div>
        <a class="btn" href="${pageContext.request.contextPath}/booking/new">Đặt bàn mới</a>
    </div>
    <table>
        <thead><tr><th>Mã</th><th>Ngày</th><th>Giờ</th><th>Loại bàn</th><th>Trạng thái</th><th>Ghi chú</th></tr></thead>
        <tbody>
        <c:forEach items="${bookings}" var="booking">
            <tr>
                <td>#${booking.id}</td>
                <td>${booking.bookingDate}</td>
                <td>${booking.bookingTime}</td>
                <td>${booking.tableClass} · ${booking.capacity} khách</td>
                <td><span class="badge">${booking.status}</span></td>
                <td>${booking.note}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</main>
<%@ include file="../shared/footer.jsp" %>
