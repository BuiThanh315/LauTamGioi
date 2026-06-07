package com.lautamgioi.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Pattern;

public final class Validators {
    private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9_]{4,50}$");
    private static final Pattern PHONE = Pattern.compile("^(0|\\+84)(\\d{9})$");
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern URL = Pattern.compile("^https?://.{3,255}$");

    private Validators() {
    }

    public static String required(String value, String label, int maxLength) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.isEmpty()) {
            throw new ValidationException(label + " không được để trống.");
        }
        if (normalized.length() > maxLength) {
            throw new ValidationException(label + " không được vượt quá " + maxLength + " ký tự.");
        }
        return normalized;
    }

    public static String optional(String value, String label, int maxLength) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        return required(value, label, maxLength);
    }

    public static String username(String value) {
        String username = required(value, "Tài khoản", 50);
        if (!USERNAME.matcher(username).matches()) {
            throw new ValidationException("Tài khoản phải dài 4-50 ký tự và chỉ gồm chữ, số hoặc dấu gạch dưới.");
        }
        return username;
    }

    public static String password(String value) {
        String password = required(value, "Mật khẩu", 255);
        if (password.length() < 6) {
            throw new ValidationException("Mật khẩu phải có ít nhất 6 ký tự.");
        }
        return password;
    }

    public static String phone(String value) {
        String phone = required(value, "Số điện thoại", 20).replace(" ", "");
        if (!PHONE.matcher(phone).matches()) {
            throw new ValidationException("Số điện thoại phải đúng định dạng Việt Nam, ví dụ 0901234567.");
        }
        return phone;
    }

    public static String email(String value) {
        String email = optional(value, "Email", 100);
        if (email != null && !EMAIL.matcher(email).matches()) {
            throw new ValidationException("Email không đúng định dạng.");
        }
        return email;
    }

    public static int positiveInt(String value, String label, int max) {
        try {
            int number = Integer.parseInt(required(value, label, 12));
            if (number <= 0 || number > max) {
                throw new ValidationException(label + " phải nằm trong khoảng 1-" + max + ".");
            }
            return number;
        } catch (NumberFormatException e) {
            throw new ValidationException(label + " phải là số hợp lệ.");
        }
    }

    public static BigDecimal money(String value, String label) {
        try {
            BigDecimal money = new BigDecimal(required(value, label, 20));
            if (money.compareTo(BigDecimal.ZERO) <= 0) {
                throw new ValidationException(label + " phải lớn hơn 0.");
            }
            return money;
        } catch (NumberFormatException e) {
            throw new ValidationException(label + " phải là số tiền hợp lệ.");
        }
    }

    public static String imageUrl(String value) {
        String url = optional(value, "Đường dẫn ảnh", 255);
        if (url != null && !URL.matcher(url).matches()) {
            throw new ValidationException("Đường dẫn ảnh phải bắt đầu bằng http:// hoặc https:// và không quá 255 ký tự.");
        }
        return url;
    }

    public static LocalDate bookingDate(String value) {
        LocalDate date = LocalDate.parse(required(value, "Ngày đặt bàn", 20));
        LocalDate today = LocalDate.now();
        if (date.isBefore(today)) {
            throw new ValidationException("Ngày đặt bàn không được nằm trong quá khứ.");
        }
        if (date.isAfter(today.plusMonths(3))) {
            throw new ValidationException("Chỉ nhận đặt bàn trong vòng 3 tháng tới.");
        }
        return date;
    }

    public static LocalTime bookingTime(String value) {
        LocalTime time = LocalTime.parse(required(value, "Giờ đặt bàn", 20));
        if (time.isBefore(LocalTime.of(10, 0)) || time.isAfter(LocalTime.of(22, 0))) {
            throw new ValidationException("Giờ đặt bàn phải nằm trong khung 10:00-22:00.");
        }
        return time;
    }
}
