package nightgames.skills;

import java.util.Optional;

import nightgames.characters.Character;
import nightgames.characters.custom.CustomStringEntry;
import nightgames.combat.Combat;
import nightgames.combat.Result;

public class CustomSkill extends Skill {
    LoadedSkillData data;

    public CustomSkill(LoadedSkillData data, Character self) {
        super(data.name, self, data.cooldown);
        this.data = data;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return data.skillRequirements.stream().allMatch(req -> req.meets(c, user, target));
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return data.usableRequirements.stream().allMatch(req -> req.meets(c, getSelf(), target));
    }

    @Override
    public float priorityMod(Combat c) {
        return data.priorityMod;
    }

    @Override
    public String describe(Combat c) {
        return data.description;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new CustomSkill(data, user);
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return data.mojoBuilt;
    }

    @Override
    public int getMojoCost(Combat c) {
        return data.mojoCost;
    }

    @Override
    public Tactics type(Combat c) {
        return data.tactics;
    }

    @Override
    public boolean makesContact() {
        return data.makesContact;
    }

    @Override
    public int accuracy(Combat c, Character target) {
        return data.accuracy;
    }

    @Override
    public int speed() {
        return data.speed;
    }

    @Override
    public String getLabel(Combat c) {
        Optional<CustomStringEntry> picked = data.labels.stream()
                        .filter(entry -> entry.meetsRequirements(c, getSelf(), c.getOpponent(getSelf()))).findFirst();
        if (picked.isPresent()) {
            return picked.get().getLine(c, getSelf(), c.getOpponent(getSelf()));
        }
        return getName(c);
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }
}
