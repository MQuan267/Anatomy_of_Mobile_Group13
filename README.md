<h1 align="center">BÀI TẬP ANATOMY OF MOBILE</h1>

<p align="center">
  <strong>Lớp:</strong> NT213.Q12.ANTT<br>
  <strong>GVHD:</strong> Nghi Hoàng Khoa
</p>

---

## Thông tin thành viên nhóm 
|    Mã SV | Họ và tên        | Nhóm |
| -------: | ---------------- | :--: |
| 23520538 | Phạm Huy Hoàng   |  13  |
| 23521265 | Nguyễn Minh Quân |  13  |
| 23521271 | Trần Hồng Quân   |  13  |
| 23521343 | Nguyễn Ngọc Sáng |  13  |

**I. GIỚI THIỆU:**

Trong bài tập này, nhóm xây dựng một ứng dụng Android đơn giản hỗ trợ các chức năng:

- Đăng ký, đăng nhập và đăng xuất tài khoản
- Lưu trữ tài khoản trong SQLite thông qua Room
- Hash mật khẩu bằng PBKDF2
- Sử dụng ProGuard để tối ưu và bảo vệ mã nguồn
- UI bằng Jetpack Compose

**II. CÔNG NGHỆ SỬ DỤNG:**

- **Ngôn ngữ:** Kotlin
- **Giao diện:** Jetpack Compose
- **Database:** SQLite + Room ORM
- **Security:** PBKDF2WithHmacSHA256
- **Architectural Components:** Repository, DAO
- **Navigation:** Navigation Compose

**III. KIẾN TRÚC TỔNG QUÁT:**

<p align="center">
  <img src="./images/Picture1.png" alt="A screenshot">
</p>

**Giải thích ngắn:**

-UI: giao diện nhập dữ liệu, bấm nút, hiển thị thông báo.

-Repository: Đây được gọi là nơi xử lý logic như kiểm tra email đã tồn tại chưa, hash mật khẩu, kiểm tra mật khẩu có đúng không và cuối cùng trả về kết quả.

-DAO: Chỉ có nhiệm vụ chạy truy vấn SQL.

-Room: Thư viện hỗ trợ làm việc với SQLite dễ hơn

**IV. TẠO CÁC FILE CẦN THIẾT:**

-Tạo File User để thiết kế dữ liệu

<p align="center">
  <img src="./images/Picture2.png" alt="A screenshot">
</p>

**Bảng users:**

- id: Int (PRIMARY KEY)
- fullName: String
- email: String (duy nhất)
- password: String (lưu hash mật khẩu)

-Tạo File UserDao chứa các hàm truy vấn SQL dành cho bảng users, thực hiện việc thao tác dữ liệu như insert, select và delete

<p align="center">
  <img src="./images/Picture3.png" alt="A screenshot">
</p>

-Tiếp tục tạo file UserRepository  để xử lý toàn bộ logic liên quan đến người dùng như kiểm tra email trùng, hash mật khẩu, xác thực đăng nhập, rồi mới gọi DAO để truy vấn SQLite

<p align="center">
  <img src="./images/Picture4.png" alt="A screenshot">
</p>

\- File AppDatabase là nơi khai báo và quản lý toàn bộ cơ sở dữ liệu SQLite của ứng dụng. Nó cho Room biết ứng dụng có những bảng nào và cung cấp các DAO để thao tác với dữ liệu

<p align="center">
  <img src="./images/Picture5.png" alt="A screenshot">
</p>

-File AppNav. có nhiệm vụ điều hướng giữa các màn hình trong ứng dụng. File này sử dụng Navigation Compose để khai báo các route như Login, Register, Home và xác định UI nào sẽ được hiển thị khi người dùng chuyển trang

<p align="center">
  <img src="./images/Picture6.png" alt="A screenshot">
</p>

\- PasswordHasher sẽ hash lại mật khẩu người dùng nhập và so sánh với hash đã lưu để xác thực

<p align="center">
  <img src="./images/Picture7.png" alt="A screenshot">
</p>

- Khi người dùng đăng ký tài khoản, PasswordHasher tạo ra một “salt” ngẫu nhiên 64 bytes. Salt giúp cho việc hash bảo mật hơn bởi vì hai mật khẩu giống nhau sẽ cho ra hai hash khác nhau.
- Sau khi có salt, lớp này sử dụng thuật toán PBKDF2WithHmacSHA256 để tạo hash cho mật khẩu,thuật toán này lặp 10.000 lần để làm chậm quá trình bẻ khóa, tăng độ an toàn.
- Kết quả hash và salt được ghép lại thành chuỗi "salt:hash" để lưu trong SQLite. Nhờ vậy, khi đăng nhập, ứng dụng có thể tách riêng salt và hash để kiểm tra.

**V. ĐĂNG NHẬP, ĐĂNG KÝ VÀ HOMEPAGE:**

-Đăng nhập:

<p align="center">
  <img src="./images/Picture8.png" alt="A screenshot">
</p>

- Khi bấm nút “ĐĂNG NHẬP”, màn hình kiểm tra nhanh hai ô nhập có bị bỏ trống hay không và hiển thị thông báo nếu thiếu thông tin.
- Nếu dữ liệu hợp lệ, LoginScreen gọi hàm loginUser() của UserRepository. Toàn bộ quá trình kiểm tra tài khoản được thực hiện ở Repository, không nằm trong UI.
- Repository sẽ tự lấy user theo email từ SQLite, hash lại mật khẩu mà người dùng vừa nhập và so sánh với mật khẩu đã hash đang lưu trong database.
- LoginScreen nhận kết quả từ Repository rồi hiển thị thông báo tương ứng: thành công thì chuyển màn hình sang HomeScreen hoặc thất bại  thì thông báo lỗi

-Đăng ký:

<p align="center">
  <img src="./images/Picture9.png" alt="A screenshot">
</p>

- Khi người dùng bấm nút “ĐĂNG KÝ”, giao diện kiểm tra nhanh các lỗi cơ bản:
- Các ô không được để trống
- Mật khẩu và xác nhận mật khẩu phải trùng nhau
- Mật khẩu phải có ít nhất 6 ký tự
- Nếu dữ liệu nhập hợp lệ, RegisterScreen gọi hàm registerUser() trong UserRepository để xử lý việc đăng ký.
- Toàn bộ logic đăng ký (kiểm tra email trùng, hash mật khẩu bằng PBKDF2, tạo đối tượng User và lưu vào SQLite) đều được thực hiện ở Repository, không nằm trong giao diện.

-Homepage là màn hình được hiển thị sau khi người dùng đăng nhập thành công.

<p align="center">
  <img src="./images/Picture10.png" alt="A screenshot">
</p>

**VI. PROGUARD:**

-ProGuard là công cụ được sử dụng trong bản build Release của Android để tối ưu và bảo vệ mã nguồn.

-Trong app này, ProGuard có nhiệm vụ làm rối (obfuscate) tên class, hàm và biến, khiến mã nguồn khó bị dịch ngược khi ai đó bung file APK, loại bỏ các đoạn mã không được sử dụng, loại bỏ các log để tránh lộ thông tin

-Cụ thể:

<p align="center">
  <img src="./images/Picture11.png" alt="A screenshot">
</p>

-Đoạn này giữ lại tất cả những phần liên quan đến Room và database, vì Room cần dùng reflection để tạo bảng và truy vấn SQLite, nên nếu ProGuard làm rối hoặc xoá nhầm class thì app sẽ crash ngay khi mở. Nói dễ hiểu hơn thì đoạn này giúp giữ Room lại để database hoạt động bình thường

<p align="center">
  <img src="./images/Picture12.png" alt="A screenshot">
</p>

-Do Compose hoạt động dựa vào annotation và code tự sinh thêm, nếu ProGuard đổi tên hoặc tối ưu mạnh tay, giao diện có thể lỗi hoặc không hiển thị. Vì vậy đoạn này cũng có nghĩa là nói với ProGuard đừng đụng vào Compose và để nó yên cho nó chạy

<p align="center">
  <img src="./images/Picture13.png" alt="A screenshot">
</p>

-Navigation sẽ chuyển màn hình dựa trên tên route và class screen và nếu ProGuard đổi tên class của các màn hình thì Navigation sẽ không tìm thấy đường đi sẽ làm cho app crash. Đoạn này giúp giữ nguyên các file điều hướng và màn hình để chuyển trang không bị lỗi

<p align="center">
  <img src="./images/Picture14.png" alt="A screenshot">
</p>

-Giữ nguyên thư viện Kotlin và Coroutine để tránh lỗi khi chạy nền

<p align="center">
  <img src="./images/Picture15.png" alt="A screenshot">
</p>

-Activity, Service, BroadcastReceiver, ContentProvider đều do hệ điều hành gọi nên đoạn này giúp giữ nguyên các thành phần mà Android cần gọi trực tiếp, tránh việc ProGuard đổi tên chúng

<p align="center">
  <img src="./images/Picture16.png" alt="A screenshot">
</p>

-Đoạn rule này dùng để xoá toàn bộ lệnh Log trong bản Release để tránh lộ thông tin

-2 rule cuối  giúp tăng mức độ làm rối mã nguồn trong file APK, khiến việc dịch ngược và đọc code trở nên khó khăn hơn. Như repackageclasses sẽ gom tất cả các class vào những package mới với tên rất ngắn (ví dụ: a, b, c),  overloadaggressively cho phép ProGuard đặt trùng tên nhiều hàm trong cùng một class, miễn là kiểu tham số khác nhau.

<p align="center">
  <img src="./images/Picture17Picture17.png" alt="A screenshot">
</p>

-Phần này dùng để giữ lại những hàm quan trọng của enum (values() và valueOf()) và các class được đánh dấu @keep. Điều này cần thiết vì các hàm đặc biệt của enum và các phần dùng reflection phải giữ nguyên tên thì ứng dụng mới hoạt động đúng sau khi mã bị làm rối bằng ProGuard

**VII. GIAO DIỆN**

-Login:

<p align="center">
  <img src="./images/Picture18.png" alt="A screenshot">
</p>

-Register:

<p align="center">
  <img src="./images/Picture19.png" alt="A screenshot">
</p>

-Khi thực hiện xong đăng kí, thì thông tin user sẽ được lưu trong SQLite:

<p align="center">
  <img src="./images/Picture20.png" alt="A screenshot">
</p>

-Home:

<p align="center">
  <img src="./images/Picture21.png" alt="A screenshot">
</p>

**VIII. KẾT LUẬN**

Nhóm đã xây dựng thành công:

- Hệ thống đăng ký , đăng nhập đầy đủ
- Lưu dữ liệu người dùng với SQLite qua Room
- Hash mật khẩu an toàn
- ProGuard tối ưu mã nguồn

