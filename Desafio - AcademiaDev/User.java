public abstract class User {
    private final String name;
    private final String email;

    protected User(String name, String email) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nome inválido.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email inválido.");
        }
        this.name = name;
        this.email = email.toLowerCase().trim();
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public abstract String getRole();

    @Override
    public String toString() {
        return getRole() + "{name='" + name + "', email='" + email + "'}";
    }
}