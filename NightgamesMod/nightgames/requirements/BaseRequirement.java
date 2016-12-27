package nightgames.requirements;

import java.lang.reflect.Field;

/**
 * Base class for requirements. Provides consistent requirements name handling.
 */
public abstract class BaseRequirement implements Requirement {
    public String getName() {
        return getClass().getName().replace("nightgames.requirements.", "").replace("Requirement", "").toLowerCase();
    }

    @Override public String toString() {
        String s = getName() + ": ";
        Field[] fields = getClass().getDeclaredFields();
        if (fields.length == 0) {
            s += "\"\"";
        } else if (fields.length == 1) {
            s += getFieldValue(fields[0]);
        } else {
            s += "{";
            // Preceding comma; don't print on first entry.
            boolean comma = false;
            for (Field f : fields) {
                if (comma) {
                    s += ", ";
                } else {
                    comma = true;
                }
                s += f.getName() + ": " + getFieldValue(f);
            }
            s += "}";
        }
        return s;
    }

    private Object getFieldValue(Field f) {
        Object value;
        try {
            f.setAccessible(true);
            value = f.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            value = "INACCESSIBLE";
        }
        return value;
    }

    @Override public boolean equals(Object o) {
        return this == o || !(o == null || getClass() != o.getClass());

    }

    @Override public int hashCode() {
        return getClass().getName().hashCode();
    }
}
