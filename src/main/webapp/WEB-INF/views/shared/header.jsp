<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lẩu Tam Giới</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
<header class="topbar">
    <a class="brand" href="${pageContext.request.contextPath}/home">Lẩu Tam Giới</a>
    <nav class="nav">
        <a href="${pageContext.request.contextPath}/menu">Thực đơn</a>
        <c:choose>
            <c:when test="${not empty sessionScope.account}">
                <c:if test="${sessionScope.account.role == 'CUSTOMER'}">
                    <a href="${pageContext.request.contextPath}/booking/new">Đặt bàn</a>
                    <a href="${pageContext.request.contextPath}/booking/history">Lịch sử</a>
                </c:if>
                <c:if test="${sessionScope.account.role == 'STAFF' || sessionScope.account.role == 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/staff/bookings">Vận hành</a>
                </c:if>
                <c:if test="${sessionScope.account.role == 'ADMIN'}">
                    <a href="${pageContext.request.contextPath}/admin/dishes">Món</a>
                    <a href="${pageContext.request.contextPath}/admin/categories">Danh mục</a>
                    <a href="${pageContext.request.contextPath}/admin/reports">Báo cáo</a>
                </c:if>
                <a href="${pageContext.request.contextPath}/logout">${sessionScope.account.fullName}</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>
<c:if test="${not empty sessionScope.flash}">
    <main class="page" style="padding-bottom:0">
        <div class="flash">${sessionScope.flash}</div>
        <c:remove var="flash" scope="session"/>
    </main>
</c:if>
