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
        <div class="nav-group">
            <a href="${pageContext.request.contextPath}/menu">Thực đơn</a>
            <c:if test="${not empty sessionScope.account && sessionScope.account.role == 'CUSTOMER'}">
                <a href="${pageContext.request.contextPath}/booking/new">Đặt bàn</a>
                <a href="${pageContext.request.contextPath}/booking/history">Lịch sử đặt bàn</a>
            </c:if>
            <c:if test="${not empty sessionScope.account && sessionScope.account.role == 'STAFF'}">
                <a href="${pageContext.request.contextPath}/staff/bookings">Điều phối đặt bàn</a>
            </c:if>
            <c:if test="${not empty sessionScope.account && sessionScope.account.role == 'ADMIN'}">
                <a href="${pageContext.request.contextPath}/admin/bookings">Đơn đặt bàn</a>
                <a href="${pageContext.request.contextPath}/admin/dishes">Món ăn</a>
                <a href="${pageContext.request.contextPath}/admin/categories">Danh mục</a>
                <a href="${pageContext.request.contextPath}/admin/reports">Báo cáo</a>
            </c:if>
        </div>
        <div class="nav-group">
            <c:choose>
                <c:when test="${not empty sessionScope.account}">
                    <c:choose>
                        <c:when test="${sessionScope.account.role == 'CUSTOMER'}">
                            <a class="user-pill user-link" href="${pageContext.request.contextPath}/customer/profile">${sessionScope.account.fullName}</a>
                        </c:when>
                        <c:otherwise>
                            <span class="user-pill">${sessionScope.account.fullName}</span>
                        </c:otherwise>
                    </c:choose>
                    <a class="btn secondary" href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                    <a class="btn secondary" href="${pageContext.request.contextPath}/register">Đăng ký</a>
                </c:otherwise>
            </c:choose>
        </div>
    </nav>
</header>
<c:if test="${not empty sessionScope.flash}">
    <main class="page" style="padding-bottom:0">
        <div class="flash">${sessionScope.flash}</div>
        <c:remove var="flash" scope="session"/>
    </main>
</c:if>
