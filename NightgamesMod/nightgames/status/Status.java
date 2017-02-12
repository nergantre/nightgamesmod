package nightgames.status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.gson.JsonObject;

import nightgames.actions.Action;
import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.requirements.Requirement;
import nightgames.skills.Skill;

public abstract class Status implements Cloneable {
    public String name;
    public transient Character affected;
    protected transient Set<Stsflag> flags;
    protected transient List<Requirement> requirements;

    public Status(String name, Character affected) {
        this.name = name;
        this.affected = affected;
        requirements = new ArrayList<>();
        flags = EnumSet.noneOf(Stsflag.class);
    }

    @Override
    public String toString() {
        return name;
    }

    public Collection<Skill> allowedSkills(Combat c) {
        return Collections.emptySet();
    }
    
    public Collection<Action> allowedActions() {
        return Collections.emptySet();
    }

    public boolean meetsRequirements(Combat c, Character self, Character other) {
        return requirements.stream().allMatch((req) -> req.meets(c, self, other));
    }

    public abstract String initialMessage(Combat c, Optional<Status> replacement);

    public abstract String describe(Combat c);

    public abstract int mod(Attribute a);

    public abstract int regen(Combat c);

    public abstract int damage(Combat c, int x);

    public abstract double pleasure(Combat c, BodyPart withPart, BodyPart targetPart, double x);

    public abstract int weakened(Combat c, int x);

    public abstract int tempted(Combat c, int x);

    public double sensitivity(double x) {
        return 0;
    }

    public abstract int evade();

    public abstract int escape();

    public abstract int gainmojo(int x);

    public abstract int spendmojo(int x);

    public abstract int counter();

    public abstract int value();
    
    public int drained(Combat c, int x) {
        return 0;
    }

    public float fitnessModifier() {
        return 0;
    }

    public boolean lingering() {
        return false;
    }

    public void flag(Stsflag status) {
        flags.add(status);
    }
    
    public void unflag(Stsflag status) {
        flags.remove(status);
    }

    public Set<Stsflag> flags() {
        return flags;
    }

    public Status withFlagRemoved(Stsflag flag) {
        flags.remove(flag);
        return this;
    }
    
    public boolean overrides(Status s) {
        return s.getClass() == this.getClass();
    }

    public void replace(Status newStatus) {}

    public boolean mindgames() {
        return flags().contains(Stsflag.mindgames);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract Status instance(Character newAffected, Character newOther);

    public String getVariant() {
        return toString();
    }

    public void struggle(Character character) {}

    public void onRemove(Combat c, Character other) {}

    public void onApply(Combat c, Character other) {}

    public abstract JsonObject saveToJson();

    public abstract Status loadFromJson(JsonObject obj);

    public void tick(Combat c) {}
    
    public boolean isAddiction() { return false; }
}
