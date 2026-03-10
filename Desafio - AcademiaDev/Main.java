import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        PlatformData data = InitialData.load();
        PlatformService service = new PlatformService(data);

        while (true) {
            System.out.println("\n=== AcademiaDev ===");
            System.out.println("1 - Login");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            int op = readInt();

            if (op == 0) {
                break;
            }
            if (op == 1) {
                System.out.print("Email: ");
                String email = sc.nextLine();
                User user = service.login(email);
                if (user == null) {
                    System.out.println("Usuário não encontrado.");
                    continue;
                }
                if (user instanceof Admin admin) {
                    adminMenu(service, admin);
                } else if (user instanceof Student student) {
                    studentMenu(service, student);
                }
            }
        }
        sc.close();
    }

    private static void adminMenu(PlatformService service, Admin admin) {
        int op;
        do {
            System.out.println("\n=== Menu Admin ===");
            System.out.println("1 - Ativar/Inativar curso");
            System.out.println("2 - Alterar plano de aluno");
            System.out.println("3 - Atender próximo ticket");
            System.out.println("4 - Relatórios");
            System.out.println("5 - Exportar cursos CSV");
            System.out.println("6 - Exportar alunos CSV");
            System.out.println("7 - Listar catálogo ativo");
            System.out.println("8 - Abrir ticket");
            System.out.println("0 - Logout");
            System.out.print("Escolha: ");
            op = readInt();

            try {
                switch (op) {
                    case 1 -> {
                        service.allCourses().forEach(System.out::println);
                        System.out.print("Título do curso: ");
                        String title = sc.nextLine();
                        System.out.print("1 para ACTIVE, 2 para INACTIVE: ");
                        int statusOp = readInt();
                        CourseStatus status = statusOp == 1 ? CourseStatus.ACTIVE : CourseStatus.INACTIVE;
                        service.changeCourseStatus(admin, title, status);
                        System.out.println("Status alterado.");
                    }
                    case 2 -> {
                        service.allStudents().forEach(System.out::println);
                        System.out.print("Email do aluno: ");
                        String email = sc.nextLine();
                        System.out.print("1 para Basic, 2 para Premium: ");
                        int planOp = readInt();
                        SubscriptionPlan plan = planOp == 1 ? new BasicPlan() : new PremiumPlan();
                        service.changeStudentPlan(admin, email, plan);
                        System.out.println("Plano alterado.");
                    }
                    case 3 -> {
                        SupportTicket ticket = service.processNextTicket(admin);
                        System.out.println(ticket == null ? "Nenhum ticket na fila." : ticket);
                    }
                    case 4 -> showReports(service);
                    case 5 -> exportCourses(service);
                    case 6 -> exportStudents(service);
                    case 7 -> service.listActiveCourses().forEach(System.out::println);
                    case 8 -> openTicket(service, admin);
                }
            } catch (RuntimeException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (op != 0);
    }

    private static void studentMenu(PlatformService service, Student student) {
        int op;
        do {
            System.out.println("\n=== Menu Student ===");
            System.out.println("1 - Consultar catálogo ativo");
            System.out.println("2 - Matricular-se em curso");
            System.out.println("3 - Consultar matrículas");
            System.out.println("4 - Atualizar progresso");
            System.out.println("5 - Cancelar matrícula");
            System.out.println("6 - Abrir ticket");
            System.out.println("0 - Logout");
            System.out.print("Escolha: ");
            op = readInt();

            try {
                switch (op) {
                    case 1 -> service.listActiveCourses().forEach(System.out::println);
                    case 2 -> {
                        System.out.print("Título do curso: ");
                        String title = sc.nextLine();
                        service.enroll(student, title);
                        System.out.println("Matrícula realizada.");
                    }
                    case 3 -> service.getStudentEnrollments(student).forEach(System.out::println);
                    case 4 -> {
                        System.out.print("Título do curso: ");
                        String title = sc.nextLine();
                        System.out.print("Novo progresso: ");
                        double progress = readDouble();
                        service.updateProgress(student, title, progress);
                        System.out.println("Progresso atualizado.");
                    }
                    case 5 -> {
                        System.out.print("Título do curso: ");
                        String title = sc.nextLine();
                        service.cancelEnrollment(student, title);
                        System.out.println("Matrícula cancelada.");
                    }
                    case 6 -> openTicket(service, student);
                }
            } catch (RuntimeException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        } while (op != 0);
    }

    private static void showReports(PlatformService service) {
        System.out.println("\n=== Relatórios ===");
        System.out.println("1 - Cursos por dificuldade");
        System.out.println("2 - Instrutores únicos de cursos ativos");
        System.out.println("3 - Alunos agrupados por plano");
        System.out.println("4 - Média geral de progresso");
        System.out.println("5 - Aluno com mais matrículas ativas");
        System.out.print("Escolha: ");
        int op = readInt();

        switch (op) {
            case 1 -> {
                System.out.print("1 BEGINNER | 2 INTERMEDIATE | 3 ADVANCED: ");
                int n = readInt();
                DifficultyLevel level = switch (n) {
                    case 2 -> DifficultyLevel.INTERMEDIATE;
                    case 3 -> DifficultyLevel.ADVANCED;
                    default -> DifficultyLevel.BEGINNER;
                };
                service.coursesByDifficulty(level).forEach(System.out::println);
            }
            case 2 -> service.uniqueActiveInstructors().forEach(System.out::println);
            case 3 -> service.studentsByPlan().forEach((k, v) -> System.out.println(k + " -> " + v));
            case 4 -> System.out.println("Média geral de progresso: " + service.averageProgress());
            case 5 -> {
                Optional<Student> student = service.studentWithMostActiveEnrollments();
                System.out.println(student.map(Student::toString).orElse("Nenhum aluno encontrado."));
            }
        }
    }

    private static void exportCourses(PlatformService service) {
        System.out.println("Campos disponíveis em Course: title, description, instructorName, durationInHours, difficultyLevel, status");
        System.out.print("Digite os campos separados por vírgula: ");
        List<String> columns = Arrays.stream(sc.nextLine().split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
        String csv = GenericCsvExporter.export(service.allCourses(), columns);
        System.out.println(csv);
    }

    private static void exportStudents(PlatformService service) {
        System.out.println("Campos disponíveis em Student: name, email, subscriptionPlan");
        System.out.print("Digite os campos separados por vírgula: ");
        List<String> columns = Arrays.stream(sc.nextLine().split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
        String csv = GenericCsvExporter.export(service.allStudents(), columns);
        System.out.println(csv);
    }

    private static void openTicket(PlatformService service, User user) {
        System.out.print("Título do ticket: ");
        String title = sc.nextLine();
        System.out.print("Mensagem: ");
        String message = sc.nextLine();
        service.openTicket(user, title, message);
        System.out.println("Ticket aberto.");
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (Exception e) {
                System.out.print("Número inválido. Tente novamente: ");
            }
        }
    }

    private static double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
            } catch (Exception e) {
                System.out.print("Valor inválido. Tente novamente: ");
            }
        }
    }
}