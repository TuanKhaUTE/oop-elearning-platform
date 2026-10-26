# Week 4 - Final Defense Q&A

Project: **E-Learning Platform (Courses, Enrollment, Progress Tracking)**

## Trần Tuấn Kha - 23110028

### Question 1: Polymorphism được áp dụng trong Lesson hierarchy như thế nào?

Lesson là abstract class và khai báo isComplete(CompletionEvidence evidence). VideoLesson, TextLesson, QuizLesson và AssignmentLesson đều override method này bằng completion rule riêng. Progress.updateLessonProgress(...) chỉ làm việc với kiểu Lesson và gọi lesson.isComplete(evidence), nên Progress không cần switch/if theo từng loại lesson. Runtime object quyết định implementation nào được gọi. Đây là polymorphism chính của project.

### Question 2: Week 4 chứng minh Open/Closed Principle bằng cách nào?

Kha thêm AssignmentLesson, AssignmentEvidence và AssignmentLessonExtensibilityTest mà không sửa các production class có sẵn như Lesson, Course, Progress, Enrollment hay EnrollmentFileStore. Feature commit 3a64498 có diff chỉ gồm ba file mới (A), không có file existing nào bị modified (M). AssignmentLesson kế thừa Lesson và tự override isComplete(...), vì vậy hệ thống open for extension nhưng existing core logic vẫn closed for modification.

### Question 3: Vì sao Assignment cần AssignmentEvidence riêng thay vì tái sử dụng evidence cũ?

Mỗi Lesson subtype có dữ liệu completion khác nhau. Video cần watched percentage, Quiz cần score, Text cần completed-reading flag, còn Assignment cần submitted word count. AssignmentEvidence đóng gói dữ liệu riêng này và reject số từ âm ngay ở constructor. Nhờ vậy extension Week 4 gồm cả behavior (AssignmentLesson) lẫn input model (AssignmentEvidence), không ép một evidence type cũ mang ý nghĩa không đúng.

### Question 4: Vì sao Kha viết validation tests trước khi refactor PercentageValidator ở Week 3?

Trước refactor, rule 0..100 bị lặp trong VideoLesson, QuizLesson, VideoEvidence và QuizEvidence. Kha viết LessonValidationTest trước để khóa behavior hiện tại, gồm giá trị <0, >100, NaN và boundary 0/100. Sau đó mới extract PercentageValidator.requireValid(...). Khi toàn bộ tests vẫn pass, Kha có evidence rằng refactor giảm duplicate code và centralize validation nhưng không đổi external behavior. Tests được dùng như safety net cho refactoring.

### Question 5: Prerequisite locking tương tác với polymorphic completion như thế nào?

Trong Progress.updateLessonProgress(...), thứ tự kiểm tra là: lesson phải thuộc Course; lesson phải unlocked, tức prerequisite đã hoàn thành; sau đó mới gọi polymorphic lesson.isComplete(evidence); chỉ khi trả true thì lesson mới được thêm vào completedLessons. Vì vậy Assignment có đủ 500 từ vẫn bị reject nếu Text prerequisite chưa hoàn thành. Locking thuộc trách nhiệm Progress, còn completion rule thuộc trách nhiệm từng Lesson subtype.

## Mai Trần Thùy Trang - 23110065

### Question 6: Encapsulation được áp dụng trong Progress như thế nào?

Progress giữ completedLessons ở trạng thái private và không cung cấp public setter để thay đổi trực tiếp. Để đánh dấu lesson hoàn thành, chương trình phải gọi updateLessonProgress(). Method này kiểm tra lesson có thuộc Course không, prerequisite đã hoàn thành chưa và evidence có đáp ứng Lesson.isComplete() hay không. Nhờ vậy completion state không thể bị thay đổi tùy ý qua public API thông thường.

### Question 7: Progress bảo vệ những invariants nào?

Các invariant chính gồm: lesson hoàn thành phải thuộc Course; prerequisite phải hoàn thành trước; lesson chỉ được ghi nhận khi evidence hợp lệ theo rule của subtype; Course rỗng không được cấp certificate; certificate chỉ hợp lệ khi tất cả lessons hoàn thành. Ở Week 3, edge-case test của Trang phát hiện bug Course rỗng vẫn đủ điều kiện nhận certificate; bug được sửa và test lại trước khi tiếp tục.

### Question 8: EnrollmentFileStore lưu và khôi phục Progress như thế nào?

EnrollmentFileStore.save() lưu Student ID, Course ID và danh sách completed lesson IDs vào UTF-8 snapshot. load() đọc và validate dữ liệu, sau đó gọi Enrollment.restore()/Progress.restore() để tạo state mới. Percentage và certificate eligibility không được lưu cứng mà được tính lại từ completed lessons. Trong Week 4, Assignment completion ở 75% được save rồi restore lên Student/Course/Lesson objects mới và Quiz vẫn được unlock đúng.

### Question 9: Hệ thống xử lý exceptions trong persistence như thế nào?

Caller arguments không hợp lệ được báo bằng IllegalArgumentException; lỗi I/O hoặc saved data không hợp lệ được báo bằng IOException. Khi load, file được kiểm tra version, identifiers, completed count và restored prerequisite state trước khi trả về Enrollment. Nếu validation thất bại, method ném exception thay vì trả về một Progress khôi phục dở dang, giúp bảo vệ tính nhất quán.

### Question 10: Vì sao thêm AssignmentLesson mà không cần sửa Progress hoặc EnrollmentFileStore?

AssignmentLesson kế thừa Lesson và tự triển khai isComplete() cho AssignmentEvidence. Progress chỉ gọi abstraction Lesson.isComplete(...), nên không cần biết object là Video, Text, Quiz hay Assignment. EnrollmentFileStore chỉ lưu completed lesson IDs và restore thông qua Course, không encode loại Lesson. Sáu integration tests Week 4 chứng minh Assignment hoạt động với Progress, Enrollment và persistence/restore mà không sửa production code.

## OOP mapping for defense

| Principle | Class / method | Meaning |
|---|---|---|
| Abstraction | Lesson, CompletionEvidence | Common completion contract and input abstraction |
| Inheritance | Video/Text/Quiz/AssignmentLesson extends Lesson | Each concrete lesson is-a Lesson |
| Polymorphism | Progress.updateLessonProgress -> Lesson.isComplete | Runtime subtype applies its own rule |
| Encapsulation | Progress.completedLessons private | Completion state changes only through validated APIs |
| Composition | Course -> List<Lesson>; Enrollment -> Progress | Domain objects contain their owned parts |
| Open/Closed | AssignmentLesson + AssignmentEvidence | New variant added without modifying existing core production classes |
| SRP / separation | Lesson rules, Progress state, EnrollmentFileStore I/O | Completion rule, state management and persistence are separated |
