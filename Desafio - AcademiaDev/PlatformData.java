import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;

public class PlatformData {
    private final Map<String, Course> coursesByTitle = new LinkedHashMap<>();
    private final Map<String, User> usersByEmail = new LinkedHashMap<>();
    private final Queue<SupportTicket> supportTickets = new ArrayDeque<>();

    public Map<String, Course> getCoursesByTitle() {
        return coursesByTitle;
    }

    public Map<String, User> getUsersByEmail() {
        return usersByEmail;
    }

    public Queue<SupportTicket> getSupportTickets() {
        return supportTickets;
    }
}
