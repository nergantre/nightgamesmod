package nightgames.skills;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.damage.Staleness;
import nightgames.status.FiredUp;
import nightgames.status.Status;
import nightgames.status.Stsflag;

public abstract class Skill {
    /**
     *
     */
    private String name;
    private Character self;
    private int cooldown;
    private Set<SkillTag> tags;
    public String choice;
    private Staleness staleness;

    public Skill(String name, Character self) {
        this(name, self, 0);
    }

    public Skill(String name, Character self, int cooldown) {
        this.name = name;
        setSelf(self);
        this.cooldown = cooldown;
        choice = "";
        tags = new HashSet<>();
        staleness = Staleness.build().withDecay(.1).withFloor(.5).withRecovery(.05);
    }

    public final boolean requirements(Combat c, Character target) {
        return requirements(c, getSelf(), target);
    }

    public abstract boolean requirements(Combat c, Character user, Character target);

    public static void filterAllowedSkills(Combat c, Collection<Skill> skills, Character user) {
        filterAllowedSkills(c, skills, user, null);
    }
    public static void filterAllowedSkills(Combat c, Collection<Skill> skills, Character user, Character target) {
        boolean filtered = false;
        Set<Skill> stanceSkills = new HashSet<Skill>(c.getStance().availSkills(c, user));

        if (stanceSkills.size() > 0) {
            skills.retainAll(stanceSkills);
            filtered = true;
        }
        Set<Skill> availSkills = new HashSet<Skill>();
        for (Status st : user.status) {
            for (Skill sk : st.allowedSkills(c)) {
                if ((target != null && skillIsUsable(c, sk, target)) || skillIsUsable(c, sk)) {
                    availSkills.add(sk);
                }
            }
        }
        if (availSkills.size() > 0) {
            skills.retainAll(availSkills);
            filtered = true;
        }
        Set<Skill> noReqs = new HashSet<Skill>();
        if (!filtered) {
            // if the skill is restricted by status/stance, do not check for
            // requirements
            for (Skill sk : skills) {
                if (!sk.requirements(c, target != null? target : sk.getDefaultTarget(c))) {
                    noReqs.add(sk);
                }
            }
            skills.removeAll(noReqs);
        }
    }

    public static boolean skillIsUsable(Combat c, Skill s) {
        return skillIsUsable(c, s, null);
    }
    public static boolean skillIsUsable(Combat c, Skill s, Character target) {
        if (target == null) {
            target = s.getDefaultTarget(c);
        }
        boolean charmRestricted = (s.getSelf().is(Stsflag.charmed))
                        && s.type(c) != Tactics.fucking && s.type(c) != Tactics.pleasure && s.type(c) != Tactics.misc;
        boolean allureRestricted =
                        target.is(Stsflag.alluring) && (s.type(c) == Tactics.damage || s.type(c) == Tactics.debuff);
        boolean modifierRestricted = !Global.getMatch().condition.getSkillModifier().allowedSkill(c,s);
        boolean usable = s.usable(c, target) && s.getSelf().canSpend(s.getMojoCost(c)) && !charmRestricted
                        && !allureRestricted && !modifierRestricted;
        return usable;
    }

    public int getMojoBuilt(Combat c) {
        return 0;
    }

    public int getMojoCost(Combat c) {
        return 0;
    }

    public abstract boolean usable(Combat c, Character target);

    public abstract String describe(Combat c);

    public abstract boolean resolve(Combat c, Character target);

    public abstract Skill copy(Character user);

    public abstract Tactics type(Combat c);

    public abstract String deal(Combat c, int damage, Result modifier, Character target);

    public abstract String receive(Combat c, int damage, Result modifier, Character target);

    public boolean isReverseFuck(Character target) {
        return target.hasDick() && getSelf().hasPussy();
    }

    public float priorityMod(Combat c) {
        return 0.0f;
    }

    public int accuracy(Combat c) {
        return 90;
    }

    public Staleness getStaleness() {
        return this.staleness;
    }

    public int speed() {
        return 5;
    }

    public String getLabel(Combat c) {
        return getName(c);
    }

    public final String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public Character user() {
        return getSelf();
    }

    public void setSelf(Character self) {
        this.self = self;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        return toString().equals(other.toString());
    }

    @Override
    public int hashCode() {
        return ("Skill:" + toString()).hashCode();
    }

    public String getName(Combat c) {
        return toString();
    }

    public boolean makesContact() {
        return false;
    }

    public static boolean resolve(Skill skill, Combat c, Character target) {
        skill.user().addCooldown(skill);
        // save the mojo built of the skill before resolving it (or the status
        // may change)
        int generated = skill.getMojoBuilt(c);

        // Horrendously ugly, I know.
        // But you were the one who removed getWithOrganType...
        if (skill.user().has(Trait.temptress)) {
            FiredUp status = (FiredUp) skill.user().status.stream().filter(s -> s instanceof FiredUp).findAny()
                            .orElse(null);
            if (status != null) {
                if (status.getPart().equals("hands") && skill.getClass() != TemptressHandjob.class
                                || status.getPart().equals("mouth") && skill.getClass() != TemptressBlowjob.class
                                || status.getPart().equals("pussy") && skill.getClass() != TemptressRide.class) {
                    skill.user().removeStatus(Stsflag.firedup);
                }
            }
        }

        boolean success = skill.resolve(c, target);
        skill.user().spendMojo(c, skill.getMojoCost(c));
        if (success) {
            skill.user().buildMojo(c, generated);
        }
        if (c.getCombatantData(skill.getSelf()) != null) {
            c.getCombatantData(skill.getSelf()).decreaseMoveModifier(c, skill);
        }
        if (c.getCombatantData(skill.user()) != null) { 
            c.getCombatantData(skill.user()).setLastUsedSkillName(skill.getName());
        }
        return success;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Collection<String> subChoices() {
        return Collections.emptySet();
    }

    public Character getSelf() {
        return self;
    }
    
    protected void printBlinded(Combat c) {
        c.write(getSelf(), "<i>You're sure something is happening, but you can't figure out what it is.</i>");
    }
    
    public Stage getStage() {
        return Stage.REGULAR;
    }

    public Character getDefaultTarget(Combat c) {
        return c.getOpponent(getSelf());
    }

    public final double multiplierForStage(Character target) {
        return getStage().multiplierFor(target);
    }
    
    protected void writeOutput(Combat c, Result result, Character target) {
        writeOutput(c, 0, result, target);
    }
    
    protected void writeOutput(Combat c, int mag, Result result, Character target) {
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, mag, result, target));
        } else if (c.shouldPrintReceive(target, c)) {
            c.write(getSelf(), receive(c, mag, result, target));
        }
    }

    protected void addTag(SkillTag tag) {
        tags.add(tag);
    }

    protected void removeTag(SkillTag tag) {
        tags.remove(tag);
    }
    public Set<SkillTag> getTags() {
        return Collections.unmodifiableSet(tags);
    }
}
