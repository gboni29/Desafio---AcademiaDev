public class Course {
    private final String title;
    private String description;
    private String instructorName;
    private int durationInHours;
    private DifficultyLevel difficultyLevel;
    private CourseStatus status;

    public Course(String title, String description, String instructorName, int durationInHours,
                  DifficultyLevel difficultyLevel, CourseStatus status) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Título inválido.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Descrição inválida.");
        }
        if (instructorName == null || instructorName.isBlank()) {
            throw new IllegalArgumentException("Instrutor inválido.");
        }
        if (durationInHours <= 0) {
            throw new IllegalArgumentException("Carga horária inválida.");
        }
        if (difficultyLevel == null || status == null) {
            throw new IllegalArgumentException("Dados do curso inválidos.");
        }
        this.title = title.trim();
        this.description = description.trim();
        this.instructorName = instructorName.trim();
        this.durationInHours = durationInHours;
        this.difficultyLevel = difficultyLevel;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public int getDurationInHours() {
        return durationInHours;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public CourseStatus getStatus() {
        return status;
    }

    public void setStatus(CourseStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Course{title='" + title + "', instructor='" + instructorName + "', hours=" + durationInHours +
                ", difficulty=" + difficultyLevel + ", status=" + status + "}";
    }
}