package nightgames.modifier.skill;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;

public abstract class SkillModifier {

    public static final List<SkillModifier> TYPES =
                    Collections.unmodifiableList(Arrays.asList(new BanSkillsModifier(), new BanTacticsModifier(),
                                    new EncourageSkillsModifier(null, 0), new EncourageTacticsModifier(null, 0)));

    public static final SkillModifier NULL_MODIFIER = new SkillModifier() {
        @Override
        public String toString() {
            return "null-skill-modifier";
        }
    };

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

    public boolean allowedSkill(Combat c, Skill s) {
        return !(bannedSkills().contains(s) || bannedTactics().contains(s.type(c)));
    }

    public double encouragement(Skill s, Combat c, Character user) {
        return encouragedSkills().getOrDefault(s, 0.0);
    }

    public SkillModifier andThen(SkillModifier next) {
        SkillModifier me = this;
        return new SkillModifier() {
            @Override
            public Set<Skill> allowedSkills(Combat c) {
                Set<Skill> skills = me.allowedSkills(c);
                skills.retainAll(next.allowedSkills(c));
                return skills;
            }

            @Override
            public double encouragement(Skill s, Combat c, Character u) {
                return me.encouragement(s, c, u) + next.encouragement(s, c, u);
            }

            @Override
            public String toString() {
                return me.toString() + ", " + next.toString();
            }
        };
    }

    public static SkillModifier forAll(SkillModifier mod) {
        return new SkillModifier() {
            @Override
            public boolean playerOnly() {
                return false;
            }

            @Override
            public Set<Skill> allowedSkills(Combat c) {
                return mod.allowedSkills(c);
            }

            @Override
            public double encouragement(Skill s, Combat c, Character u) {
                return mod.encouragement(s, c, u);
            }

            @Override
            public String toString() {
                return mod.toString();
            }
        };
    }

    public static SkillModifier allOf(SkillModifier... modifiers) {
        if (modifiers.length == 0) {
            return NULL_MODIFIER;
        }
        SkillModifier result = modifiers[0];
        for (int i = 1; i < modifiers.length; i++) {
            result = result.andThen(modifiers[i]);
        }
        return result;
    }

    @Override
    public abstract String toString();
}
