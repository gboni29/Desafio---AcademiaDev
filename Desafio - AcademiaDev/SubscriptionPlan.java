public interface SubscriptionPlan {
    String getPlanName();
    boolean canEnroll(Student student);
}