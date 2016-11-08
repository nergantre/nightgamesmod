package nightgames.skills.damage;

public class Staleness {
    public static Staleness build() {
        Staleness val = new Staleness();
        val.recovery = .2;
        val.decay = .1;
        val.floor = .5;
        val.defaultAmount = 1.0;
        return val;
    }

    public Staleness withRecovery(double amount) {
        this.recovery = amount;
        return this;
    }
    public Staleness withDecay(double amount) {
        this.decay = amount;
        return this;
    }
    public Staleness withFloor(double amount) {
        this.floor = amount;
        return this;
    }
    public Staleness withDefault(double amount) {
        this.defaultAmount = amount;
        return this;
    }
    public double recovery;
    public double decay;
    public double floor;
    public double defaultAmount;
}
