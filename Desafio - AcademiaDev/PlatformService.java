import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PlatformService {
    private final PlatformData data;

    public PlatformService(PlatformData data) {
        this.data = data;
    }

    public User login(String email) {
        if (email == null) return null;
        return data.getUsersByEmail().get(email.trim().toLowerCase());
    }

    public List<Course> listActiveCourses() {
        return data.getCoursesByTitle().values().stream()
                .filter(c -> c.getStatus() == CourseStatus.ACTIVE)
                .toList();
    }

    public void changeCourseStatus(Admin admin, String title, CourseStatus status) {
        requireAdmin(admin);
        Course course = findCourse(title);
        course.setStatus(status);
    }

    public void changeStudentPlan(Admin admin, String studentEmail, SubscriptionPlan newPlan) {
        requireAdmin(admin);
        User user = findUser(studentEmail);
        if (!(user instanceof Student student)) {
            throw new IllegalArgumentException("Usuário informado não é aluno.");
        }
        student.setSubscriptionPlan(newPlan);
    }

    public void enroll(Student student, String courseTitle) {
        Course course = findCourse(courseTitle);

        if (course.getStatus() != CourseStatus.ACTIVE) {
            throw new EnrollmentException("Curso inativo. Não é possível realizar matrícula.");
        }
        if (student.isEnrolledIn(courseTitle)) {
            throw new EnrollmentException("Aluno já matriculado nesse curso.");
        }
        if (!student.getSubscriptionPlan().canEnroll(student)) {
            throw new EnrollmentException("Plano não permite mais matrículas ativas.");
        }

        Enrollment enrollment = new Enrollment(student, course);
        student.addEnrollment(enrollment);
    }

    public void updateProgress(Student student, String courseTitle, double progress) {
        Enrollment enrollment = findActiveEnrollment(student, courseTitle)
                .orElseThrow(() -> new EnrollmentException("Matrícula ativa não encontrada."));
        enrollment.updateProgress(progress);
    }

    public void cancelEnrollment(Student student, String courseTitle) {
        Enrollment enrollment = findActiveEnrollment(student, courseTitle)
                .orElseThrow(() -> new EnrollmentException("Matrícula ativa não encontrada."));
        enrollment.cancel();
    }

    public List<Enrollment> getStudentEnrollments(Student student) {
        return student.getEnrollments();
    }

    public void openTicket(User user, String title, String message) {
        data.getSupportTickets().offer(new SupportTicket(user.getEmail(), title, message));
    }

    public SupportTicket processNextTicket(Admin admin) {
        requireAdmin(admin);
        return data.getSupportTickets().poll();
    }

    public List<Course> coursesByDifficulty(DifficultyLevel level) {
        return data.getCoursesByTitle().values().stream()
                .filter(c -> c.getDifficultyLevel() == level)
                .sorted(Comparator.comparing(Course::getTitle))
                .toList();
    }

    public Set<String> uniqueActiveInstructors() {
        return data.getCoursesByTitle().values().stream()
                .filter(c -> c.getStatus() == CourseStatus.ACTIVE)
                .map(Course::getInstructorName)
                .collect(Collectors.toSet());
    }

    public Map<String, List<Student>> studentsByPlan() {
        return data.getUsersByEmail().values().stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .collect(Collectors.groupingBy(s -> s.getSubscriptionPlan().getPlanName()));
    }

    public double averageProgress() {
        return data.getUsersByEmail().values().stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .flatMap(s -> s.getEnrollments().stream())
                .mapToDouble(Enrollment::getProgress)
                .average()
                .orElse(0.0);
    }

    public Optional<Student> studentWithMostActiveEnrollments() {
        return data.getUsersByEmail().values().stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .max(Comparator.comparingInt(Student::getActiveEnrollmentsCount));
    }

    public List<Student> allStudents() {
        return data.getUsersByEmail().values().stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .toList();
    }

    public List<Course> allCourses() {
        return new ArrayList<>(data.getCoursesByTitle().values());
    }

    private Course findCourse(String title) {
        Course course = data.getCoursesByTitle().get(title);
        if (course == null) {
            throw new IllegalArgumentException("Curso não encontrado.");
        }
        return course;
    }

    private User findUser(String email) {
        User user = data.getUsersByEmail().get(email.trim().toLowerCase());
        if (user == null) {
            throw new IllegalArgumentException("Usuário não encontrado.");
        }
        return user;
    }

    private Optional<Enrollment> findActiveEnrollment(Student student, String courseTitle) {
        return student.getEnrollments().stream()
                .filter(Enrollment::isActive)
                .filter(e -> e.getCourse().getTitle().equalsIgnoreCase(courseTitle))
                .findFirst();
    }

    private void requireAdmin(Admin admin) {
        if (admin == null) {
            throw new AuthorizationException("Operação permitida apenas para admin.");
        }
    }
}