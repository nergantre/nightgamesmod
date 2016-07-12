package nightgames.modifier.skill;

import java.util.function.BiFunction;

import com.google.gson.JsonObject;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.modifier.ModifierComponentLoader;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;

public class EncourageTacticsModifier extends SkillModifier implements ModifierComponentLoader<SkillModifier> {
    private static final String name = "encourage-tactic";

    private final Tactics modified;
    private final BiFunction<Character, Combat, Double> weight;

    EncourageTacticsModifier() {
        modified = null;
        weight = null;
    }

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
        return name;
    }

    @Override
    public EncourageTacticsModifier instance(JsonObject object) {
        if (!object.has("tactic") || !object.has("weight")) {
            Tactics tact = Tactics.valueOf(object.get("tactic").getAsString());
            double weight = object.get("weight").getAsFloat();
            return new EncourageTacticsModifier(tact, weight);
        }
        throw new IllegalArgumentException("'encourage-tactic' must have a 'tactic' and a 'weight'");
    }

    @Override
    public String toString() {
        return "Encouraged:{" + modified.toString() + " " + weight.apply(Global.noneCharacter(), null) + "}";
    }
}
