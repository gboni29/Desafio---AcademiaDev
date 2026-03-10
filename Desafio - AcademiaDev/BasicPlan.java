public class BasicPlan implements SubscriptionPlan {
    private static final int MAX_ACTIVE_ENROLLMENTS = 3;

    @Override
    public String getPlanName() {
        return "Basic";
    }

    @Override
    public boolean canEnroll(Student student) {
        return student.getActiveEnrollmentsCount() < MAX_ACTIVE_ENROLLMENTS;
    }

    @Override
    public String toString() {
        return getPlanName();
    }
}