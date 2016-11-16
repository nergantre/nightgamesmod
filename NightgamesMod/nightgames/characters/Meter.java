package nightgames.characters;

import java.io.Serializable;

import nightgames.global.Global;

public class Meter implements Serializable, Cloneable {
    /**
     *
     */
    private static final long serialVersionUID = 2L;
    private int current;
    private float max;
    private int temporaryMax;

    public Meter(int max) {
        this.max = max;
        this.temporaryMax = Integer.MAX_VALUE;
        current = 0;
    }

    public int get() {
        if (current > max()) {
            return max();
        } else {
            return current;
        }
    }

    public int getReal() {
        return current;
    }

    public int getOverflow() {
        return Math.max(0, current - max());
    }

    public int max() {
        return (int) maxFull();
    }

    public float maxFull() {
        return Math.min(max, temporaryMax);
    }

    public float trueMax() {
        return max;
    }
    
    public void reduce(int i) {
        if (i > 0) {
            current -= i;
        }
        if (current < 0) {
            current = 0;
        }
    }

    public void restore(int i) {
        if (current < max()) {
            current = Math.min(max(), current + i);
        }
    }

    public void restoreNoLimit(int i) {
        current += i;
    }

    public boolean isEmpty() {
        return current <= 0;
    }

    public boolean isFull() {
        return current >= max();
    }

    public void empty() {
        current = 0;
    }

    public void fill() {
        current = Math.max(max(), current);
    }

    public void set(int i) {
        current = i;
        if (current < 0) {
            current = 0;
        }
    }

    public void gain(float i) {
        max += i;
        if (current > max()) {
            current = max();
        }
    }

    public void setMax(float i) {
        max = i;
        current = max();
    }
    
    public void setTemporaryMax(int i) {
        if (i <= 0) {
            i = Integer.MAX_VALUE;
        }
        temporaryMax = i;
        current = Math.min(current, max());
    }

    public int percent() {
        return Math.min(100, 100 * current / max());
    }

    @Override
    public Meter clone() throws CloneNotSupportedException {
        return (Meter) super.clone();
    }

    @Override
    public String toString() {
        return String.format("current: %s / max: %s", Global.formatDecimal(current), Global.formatDecimal(max()));
    }

    public double remaining() {
        return max() - getReal();
    }
}
