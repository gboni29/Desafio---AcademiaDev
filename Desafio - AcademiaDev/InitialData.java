public final class InitialData {
    private InitialData() {}

    public static PlatformData load() {
        PlatformData data = new PlatformData();

        data.getCoursesByTitle().put("Java Fundamentals", new Course(
                "Java Fundamentals",
                "Base da linguagem Java",
                "Giovani Silva",
                20,
                DifficultyLevel.BEGINNER,
                CourseStatus.ACTIVE
        ));

        data.getCoursesByTitle().put("Spring Boot API", new Course(
                "Spring Boot API",
                "Construção de APIs REST",
                "Fernanda Lima",
                30,
                DifficultyLevel.INTERMEDIATE,
                CourseStatus.ACTIVE
        ));

        data.getCoursesByTitle().put("Arquitetura de Software", new Course(
                "Arquitetura de Software",
                "Boas práticas de arquitetura",
                "Giovani Silva",
                25,
                DifficultyLevel.ADVANCED,
                CourseStatus.INACTIVE
        ));

        data.getCoursesByTitle().put("Banco de Dados SQL", new Course(
                "Banco de Dados SQL",
                "Consultas e modelagem",
                "Patrícia Souza",
                18,
                DifficultyLevel.BEGINNER,
                CourseStatus.ACTIVE
        ));

        Admin admin = new Admin("Admin Master", "admin@academiadev.com");
        Student s1 = new Student("Gustavo", "gustavo@email.com", new BasicPlan());
        Student s2 = new Student("Ana", "ana@email.com", new PremiumPlan());
        Student s3 = new Student("Carlos", "carlos@email.com", new BasicPlan());

        data.getUsersByEmail().put(admin.getEmail(), admin);
        data.getUsersByEmail().put(s1.getEmail(), s1);
        data.getUsersByEmail().put(s2.getEmail(), s2);
        data.getUsersByEmail().put(s3.getEmail(), s3);

        return data;
    }
}