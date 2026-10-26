# Week 4 - Individual Reflections

## Trần Tuấn Kha - 23110028

Trong bốn tuần thực hiện E-Learning Platform, phần em phụ trách chủ yếu là Course, Lesson hierarchy, các completion rules và integration/polymorphism evidence. Week 1 giúp em thấy việc phân tích responsibility trước khi code quan trọng hơn việc tạo nhiều class. Khi xác định Lesson là abstraction chung và để từng subtype tự quyết định isComplete(...), thiết kế về sau trở nên dễ mở rộng hơn.

Ở Week 2, em triển khai Course, Lesson, VideoLesson, TextLesson, QuizLesson, các CompletionEvidence subtype và Instructor. Qua đó em hiểu rõ hơn sự khác nhau giữa inheritance để biểu diễn “is-a” và composition để biểu diễn Course chứa ordered lessons. Em cũng thấy polymorphism chỉ có ý nghĩa khi caller như Progress thật sự làm việc qua abstraction thay vì kiểm tra concrete type.

Ở Week 3, em tập trung vào E2E, validation và refactoring. Em viết tests trước khi extract PercentageValidator vì muốn khóa behavior rồi mới thay đổi cấu trúc bên trong. Việc full suite vẫn pass sau refactor giúp em hiểu rằng refactoring tốt phải giảm duplication nhưng không làm thay đổi observable behavior. Đây cũng là lúc em học cách dùng test làm safety net thay vì chỉ dùng test để chứng minh code đã chạy.

Ở Week 4, em thêm AssignmentLesson và AssignmentEvidence cùng invalid-input/extensibility tests. Phần có ý nghĩa nhất là feature commit chỉ thêm file mới, không sửa Lesson, Progress, Course, EnrollmentFileStore hay các subtype cũ. Điều này làm em hiểu Open/Closed Principle theo cách thực tế: thiết kế tốt không chỉ “có inheritance”, mà phải cho phép thêm một behavior mới với phạm vi thay đổi nhỏ và không phá code cũ.

Trong toàn bộ project, em dùng ChatGPT để hỗ trợ review rubric, đề xuất test cases, kiểm tra thiết kế và chuẩn bị documentation. Tuy nhiên em luôn đối chiếu lại source code, Git diff và Maven/JUnit output; các đề xuất không phù hợp scope hoặc làm ownership không rõ được loại bỏ. Bài học lớn nhất của em là OOP nên được đánh giá qua responsibility, invariants, khả năng thay đổi và evidence kiểm thử chứ không chỉ qua số lượng nguyên lý được nhắc tên.

## Mai Trần Thùy Trang - 23110065

Trong quá trình thực hiện dự án E-Learning Platform từ Week 1 đến Week 4, em đã hiểu rõ hơn cách áp dụng các nguyên lý OOP vào một hệ thống thực tế, đặc biệt là encapsulation, polymorphism và composition.

Ở Week 1, em tham gia phân tích yêu cầu, thiết kế class diagram và xác định trách nhiệm của các lớp Student, Enrollment và Progress. Qua đó, em học được cách phân chia trách nhiệm giữa các đối tượng và xác định mối quan hệ giữa chúng trước khi bắt đầu viết code.

Ở Week 2, em triển khai các lớp Student, Enrollment và Progress, đồng thời viết unit tests kiểm tra các chức năng cơ bản và bảo vệ invariants. Em hiểu rõ hơn về encapsulation khi giữ completedLessons ở trạng thái private và chỉ cho phép cập nhật tiến độ thông qua method có validation. Khó khăn là đảm bảo lesson thuộc Course và prerequisite được hoàn thành trước khi ghi nhận progress. Em giải quyết bằng validation và JUnit tests.

Ở Week 3, em tập trung vào edge-case testing, exceptions và persistence. Trong quá trình kiểm thử, em phát hiện lỗi Course rỗng vẫn đủ điều kiện nhận certificate. Em viết test tái hiện lỗi, sửa isCertificateEligible() và chạy lại để xác nhận. Em cũng bổ sung Progress.restore(), Enrollment.restore() và EnrollmentFileStore để lưu/khôi phục trạng thái mà vẫn bảo vệ invariants. Qua đó, em hiểu rõ hơn tầm quan trọng của dữ liệu không hợp lệ và việc tách trách nhiệm lưu trữ khỏi domain logic.

Ở Week 4, em viết 6 integration tests để kiểm tra AssignmentLesson hoạt động với Progress, Enrollment và persistence/restore. Em hiểu rõ hơn cách polymorphism hỗ trợ Open/Closed Principle: hệ thống có thể tiếp nhận Lesson subtype mới mà không cần sửa các production classes hiện có. Khi gặp AccessDeniedException trong môi trường sandbox, em kiểm tra lại ngoài môi trường bị giới hạn và xác nhận tests chạy thành công.

Khó khăn lớn nhất của em trong toàn bộ dự án là chuyển từ việc viết từng class riêng lẻ sang đảm bảo các class phối hợp đúng, giữ trạng thái hợp lệ và có thể mở rộng. Em sử dụng ChatGPT/Codex để hỗ trợ phân tích thiết kế, đề xuất test cases và review code, nhưng luôn kiểm tra lại bằng source code, Git diff và kết quả JUnit/Maven. Bài học quan trọng em rút ra là thiết kế OOP tốt không chỉ nằm ở kế thừa hay đóng gói, mà còn ở khả năng bảo vệ dữ liệu, phân chia trách nhiệm rõ ràng, xử lý lỗi và mở rộng hệ thống mà không làm hỏng chức năng cũ.
