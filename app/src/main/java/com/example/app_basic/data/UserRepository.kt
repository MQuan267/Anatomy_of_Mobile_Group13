package com.example.app_basic.data

import com.example.app_basic.utils.PasswordHasher

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(fullName: String, email: String, password: String): Result<String> {
        return try {
            // Kiểm tra email đã tồn tại chưa
            val existingUser = userDao.getUserByEmail(email)
            if (existingUser != null) {
                return Result.failure(Exception("Email đã được sử dụng"))
            }

            // MÃ HÓA MẬT KHẨU trước khi lưu
            val hashedPassword = PasswordHasher.hashPassword(password)

            // Tạo user mới với password đã mã hóa
            val newUser = User(
                fullName = fullName,
                email = email,
                password = hashedPassword  // Lưu hash, KHÔNG lưu plain text
            )

            userDao.insertUser(newUser)
            Result.success("Đăng ký thành công!")
        } catch (e: Exception) {
            Result.failure(Exception("Đăng ký thất bại: ${e.message}"))
        }
    }

    suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            // Lấy user theo email
            val user = userDao.getUserByEmail(email)

            if (user != null) {
                // KIỂM TRA mật khẩu bằng cách verify hash
                val isPasswordCorrect = PasswordHasher.verifyPassword(password, user.password)

                if (isPasswordCorrect) {
                    Result.success(user)
                } else {
                    Result.failure(Exception("Email hoặc mật khẩu không đúng"))
                }
            } else {
                Result.failure(Exception("Email hoặc mật khẩu không đúng"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Đăng nhập thất bại: ${e.message}"))
        }
    }

    suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    suspend fun deleteAllUsers() {
        userDao.deleteAllUsers()
    }
}