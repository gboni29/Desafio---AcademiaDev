import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Student extends User {
    private SubscriptionPlan subscriptionPlan;
    private final List<Enrollment> enrollments = new ArrayList<>();

    public Student(String name, String email, SubscriptionPlan subscriptionPlan) {
        super(name, email);
        if (subscriptionPlan == null) {
            throw new IllegalArgumentException("Plano inválido.");
        }
        this.subscriptionPlan = subscriptionPlan;
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }

    public SubscriptionPlan getSubscriptionPlan() {
        return subscriptionPlan;
    }

    public void setSubscriptionPlan(SubscriptionPlan subscriptionPlan) {
        if (subscriptionPlan == null) {
            throw new IllegalArgumentException("Plano inválido.");
        }
        this.subscriptionPlan = subscriptionPlan;
    }

    public List<Enrollment> getEnrollments() {
        return Collections.unmodifiableList(enrollments);
    }

    public int getActiveEnrollmentsCount() {
        return (int) enrollments.stream().filter(Enrollment::isActive).count();
    }

    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
    }

    public boolean isEnrolledIn(String courseTitle) {
        return enrollments.stream()
                .anyMatch(e -> e.isActive() && e.getCourse().getTitle().equalsIgnoreCase(courseTitle));
    }

    @Override
    public String toString() {
        return "Student{name='" + getName() + "', email='" + getEmail() + "', plan='" + subscriptionPlan.getPlanName() + "'}";
    }
}