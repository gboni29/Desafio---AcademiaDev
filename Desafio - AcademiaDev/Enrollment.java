public class Enrollment {
    private final Student student;
    private final Course course;
    private double progress;
    private boolean active;

    public Enrollment(Student student, Course course) {
        if (student == null || course == null) {
            throw new IllegalArgumentException("Matrícula inválida.");
        }
        this.student = student;
        this.course = course;
        this.progress = 0.0;
        this.active = true;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public double getProgress() {
        return progress;
    }

    public boolean isActive() {
        return active;
    }

    public void updateProgress(double progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progresso deve estar entre 0 e 100.");
        }
        this.progress = progress;
    }

    public void cancel() {
        this.active = false;
    }

    @Override
    public String toString() {
        return "Enrollment{student='" + student.getName() + "', course='" + course.getTitle() +
                "', progress=" + progress + ", active=" + active + "}";
    }
}
