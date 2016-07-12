package nightgames.requirement;

import java.lang.reflect.Field;

/**
 * Created by Ryplinn on 6/24/2016.
 */
public abstract class BaseRequirement implements Requirement {
    public String getKey() {
        return getClass().getName().replace("nightgames.requirement.", "").replace("Requirement", "").toLowerCase();
    }

    @Override public String toString() {
        String s = getKey() + ": ";
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
            value = f.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            value = "INACCESSIBLE";
        }
        return value;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        return true;
    }

    @Override public int hashCode() {
        return getClass().getName().hashCode();
    }
}
