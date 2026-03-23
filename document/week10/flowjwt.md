# JWT Flow

1. User đăng nhập thành công, server tạo JWT và lưu vào cookie `accessToken`.
2. Client gọi API protected, cookie `accessToken` được gửi kèm request.
3. `JwtFilter` chạy trước `UsernamePasswordAuthenticationFilter`.
4. Filter bỏ qua các endpoint public (`/swagger-ui/**`, `/v3/api-docs/**`).
5. Filter lấy token từ cookie qua `getTokenFromCookie(request)`.
6. Từ token, hệ thống extract email bằng `jwtUtil.extractEmail(token)`.
7. Load user từ database bằng `CustomUserDetailsService.loadUserByUsername(email)`.
8. Validate token bằng `jwtUtil.isValid(token, userDetails)`.
9. Nếu token hợp lệ, set `Authentication` vào `SecurityContextHolder`.
10. Request tiếp tục vào controller và được kiểm tra quyền truy cập theo `SecurityConfig`.
11. Nếu token không hợp lệ/hết hạn, context bị clear và request bị từ chối ở bước authz.
