package nightgames.modifier.skill;

import java.util.*;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.modifier.ModifierComponent;
import nightgames.modifier.ModifierCategory;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;

public abstract class SkillModifier implements ModifierCategory<SkillModifier>, ModifierComponent {
    public static final SkillModifierLoader loader = new SkillModifierLoader();
    public static final SkillModifierCombiner combiner = new SkillModifierCombiner();

    public Set<Skill> bannedSkills() {
        return Collections.emptySet();
    }

    public Set<Tactics> bannedTactics() {
        return Collections.emptySet();
    }

    public Map<Skill, Double> encouragedSkills() {
        return Collections.emptyMap();
    }

    public boolean playerOnly() {
        return true;
    }

    public Set<Skill> allowedSkills(Combat c) {
        Set<Skill> skills = new HashSet<>(Global.getSkillPool());
        skills.removeIf(s -> bannedSkills().contains(s));
        skills.removeIf(s -> bannedTactics().contains(s.type(c)));
        return skills;
    }

    public SkillModifier combine(SkillModifier next) {
        SkillModifier first = this;
        return new SkillModifier() {
            @Override public String toString() {
                return first.toString() + ", " + next.toString();
            }

            @Override public String name() {
                return first.name() + " then " + next.name();
            }

            @Override public Set<Skill> allowedSkills(Combat c) {
                Set<Skill> skills = first.allowedSkills(c);
                skills.retainAll(next.allowedSkills(c));
                return skills;
            }

            @Override public double encouragement(Skill s, Combat c, Character u) {
                return first.encouragement(s, c, u) + next.encouragement(s, c, u);
            }
        };
    }

    public boolean allowedSkill(Combat c, Skill s) {
        return !(bannedSkills().contains(s) || bannedTactics().contains(s.type(c)));
    }

    public double encouragement(Skill s, Combat c, Character user) {
        return encouragedSkills().getOrDefault(s, 0.0);
    }

    @Override
    public abstract String toString();

}
