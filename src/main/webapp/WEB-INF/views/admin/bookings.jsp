<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="../shared/header.jsp" %>
<main class="page">
    <div class="section-head">
        <div><p class="eyebrow">Admin</p><h1>Quản lý đơn đặt bàn</h1></div>
    </div>
    <c:if test="${not empty error}"><div class="error">${error}</div></c:if>
    <section class="two-col">
        <div class="panel">
            <h2>${empty editingBooking ? 'Chọn đơn để sửa' : 'Sửa đơn đặt bàn'}</h2>
            <c:choose>
                <c:when test="${empty editingBooking}">
                    <div class="empty-state">Sau khi admin xác nhận, đơn sẽ có hiệu lực. Nếu khách cần thay đổi thông tin, admin có thể chọn sửa trực tiếp tại đây.</div>
                </c:when>
                <c:otherwise>
                    <form method="post" action="${pageContext.request.contextPath}/admin/booking/save" class="form-grid">
                        <input type="hidden" name="id" value="${editingBooking.id}">
                        <label>Tên khách <input name="customerName" maxlength="100" value="${editingBooking.customerName}" required></label>
                        <label>Số điện thoại <input name="customerPhone" maxlength="20" pattern="(0|\+84)\d{9}" value="${editingBooking.phone}" required></label>
                        <label>Loại bàn
                            <select name="tableTypeId" required>
                                <c:forEach items="${tableTypes}" var="type">
                                    <option value="${type.id}" ${editingBooking.tableTypeId == type.id ? 'selected' : ''}>${type.label}</option>
                                </c:forEach>
                            </select>
                        </label>
                        <label>Ngày <input type="date" name="bookingDate" value="${editingBooking.bookingDate}" required></label>
                        <label>Giờ <input type="time" name="bookingTime" value="${editingBooking.bookingTime}" min="10:00" max="22:00" required></label>
                        <label style="grid-column:1/-1">Ghi chú <textarea name="note" maxlength="1000">${editingBooking.note}</textarea></label>
                        <div class="form-actions">
                            <button type="submit">Lưu thay đổi</button>
                            <a class="btn ghost" href="${pageContext.request.contextPath}/admin/bookings">Bỏ chọn</a>
                        </div>
                    </form>
                </c:otherwise>
            </c:choose>
        </div>
        <div>
            <table>
                <thead><tr><th>Mã</th><th>Khách</th><th>Ngày giờ</th><th>Loại bàn</th><th>Bàn</th><th>Trạng thái</th><th>Thao tác</th></tr></thead>
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
                                <c:if test="${booking.status == 'PENDING'}">
                                    <form method="post" action="${pageContext.request.contextPath}/admin/booking/confirm" class="inline-form">
                                        <input type="hidden" name="bookingId" value="${booking.id}">
                                        <button type="submit">Xác nhận</button>
                                    </form>
                                </c:if>
                                <c:if test="${booking.status == 'PENDING' || booking.status == 'CONFIRMED'}">
                                    <a class="btn ghost" href="${pageContext.request.contextPath}/admin/bookings?editBookingId=${booking.id}">Sửa</a>
                                </c:if>
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
