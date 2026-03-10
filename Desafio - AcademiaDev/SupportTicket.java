public class SupportTicket {
    private final String userEmail;
    private final String title;
    private final String message;

    public SupportTicket(String userEmail, String title, String message) {
        if (userEmail == null || userEmail.isBlank() || title == null || title.isBlank() || message == null || message.isBlank()) {
            throw new IllegalArgumentException("Ticket inválido.");
        }
        this.userEmail = userEmail.trim().toLowerCase();
        this.title = title.trim();
        this.message = message.trim();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "SupportTicket{userEmail='" + userEmail + "', title='" + title + "', message='" + message + "'}";
    }
}
