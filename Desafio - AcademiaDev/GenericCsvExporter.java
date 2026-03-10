import java.lang.reflect.Field;
import java.util.List;

public final class GenericCsvExporter {
    private GenericCsvExporter() {}

    public static <T> String export(List<T> data, List<String> columns) {
        if (data == null || data.isEmpty()) {
            return "";
        }
        if (columns == null || columns.isEmpty()) {
            throw new IllegalArgumentException("Selecione ao menos uma coluna.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.join(",", columns)).append("\n");

        for (T item : data) {
            Class<?> clazz = item.getClass();
            for (int i = 0; i < columns.size(); i++) {
                String col = columns.get(i);
                try {
                    Field field = findField(clazz, col);
                    field.setAccessible(true);
                    Object value = field.get(item);
                    sb.append(value == null ? "" : String.valueOf(value));
                } catch (Exception e) {
                    sb.append("[campo_invalido]");
                }
                if (i < columns.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static Field findField(Class<?> clazz, String name) throws NoSuchFieldException {
        Class<?> current = clazz;
        while (current != null) {
            try {
                return current.getDeclaredField(name);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(name);
    }
}