package nightgames.modifier.skill;

import java.util.function.BiFunction;

import org.json.simple.JSONObject;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.global.JSONUtils;
import nightgames.modifier.ModifierComponent;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;

public class EncourageTacticsModifier extends SkillModifier implements ModifierComponent<EncourageTacticsModifier> {

    private final Tactics modified;
    private final BiFunction<Character, Combat, Double> weight;

    public EncourageTacticsModifier(Tactics modified, BiFunction<Character, Combat, Double> weight) {
        this.modified = modified;
        this.weight = weight;
    }

    public EncourageTacticsModifier(Tactics modified, double weight) {
        this.modified = modified;
        this.weight = (ch, com) -> weight;
    }

    @Override
    public double encouragement(Skill skill, Combat c, Character user) {
        return skill.type(c) == modified ? weight.apply(user, c) : 0.0;
    }

    // This applies only to npcs anyway
    @Override
    public final boolean playerOnly() {
        return false;
    }

    @Override
    public String name() {
        return "encourage-tactic";
    }

    @Override
    public EncourageTacticsModifier instance(JSONObject obj) {
        if (obj.containsKey("tactic") && obj.containsKey("weight")) {
            Tactics tact = Tactics.valueOf(JSONUtils.readString(obj, "tactic"));
            double weight = JSONUtils.readFloat(obj, "weight");
            return new EncourageTacticsModifier(tact, weight);
        }
        throw new IllegalArgumentException("'encourage-tactic' must have a 'tactic' and a 'weight'");
    }

    @Override
    public String toString() {
        return "Encouraged:{" + modified.toString() + " " + weight.apply(Global.noneCharacter(), null) + "}";
    }
}
