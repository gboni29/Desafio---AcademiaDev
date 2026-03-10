public class PremiumPlan implements SubscriptionPlan {
    @Override
    public String getPlanName() {
        return "Premium";
    }

    @Override
    public boolean canEnroll(Student student) {
        return true;
    }

    @Override
    public String toString() {
        return getPlanName();
    }
}