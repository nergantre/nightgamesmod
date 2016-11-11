package nightgames.pet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Growth;
import nightgames.characters.custom.effect.CustomEffect;
import nightgames.combat.Combat;
import nightgames.combat.IEncounter;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.trap.Trap;

public class PetCharacter extends Character {
    private String type;
    private String ownerType;
    private Pet self;

    public PetCharacter(Pet self, String name, String type, Growth growth, int level) {
        super(name, 1);
        this.ownerType = self.owner().getType();
        this.self = self;
        this.type = type;
        for (int i = 1; i < level; i++) {
            growth.levelUp(this);
        }
        distributePoints(Arrays.asList());
        this.getSkills().clear();
        this.mojo.setMax(100);
    }

    public PetCharacter cloneWithOwner(Character owner) throws CloneNotSupportedException {
        PetCharacter clone = (PetCharacter) clone();
        clone.self = getSelf().cloneWithOwner(owner);
        return clone;
    }

    @Override
    public void ding() {}

    @Override
    public void detect() {}

    @Override
    public void faceOff(Character opponent, IEncounter enc) {}

    @Override
    public void spy(Character opponent, IEncounter enc) {}

    @Override
    public String describe(int per, Combat c) {
        return "";
    }

    @Override
    public void victory(Combat c, Result flag) {}

    @Override
    public void defeat(Combat c, Result flag) {}

    @Override
    public void intervene3p(Combat c, Character target, Character assist) {}

    @Override
    public void victory3p(Combat c, Character target, Character assist) {}

    @Override
    public boolean resist3p(Combat c, Character target, Character assist) {
        return true;
    }

    @Override
    public void act(Combat c) {
        act(c, c.getOpponent(this));
    }

    public void act(Combat c, Character target) {
        List<CustomEffect> skillUse = new ArrayList<>(); 
        List<Skill> allowedEnemySkills = new ArrayList<>(getSkills()
                        .stream().filter(skill -> Skill.skillIsUsable(c, skill, target))
                        .collect(Collectors.toList()));
        Skill.filterAllowedSkills(c, allowedEnemySkills, this, target);
        allowedEnemySkills.forEach(skill -> skillUse.add((combat, self, dontcare) -> Skill.resolve(skill, combat, target)));
        
        List<Skill> allowedMasterSkills = new ArrayList<>(getSkills()
                        .stream().filter(skill -> Skill.skillIsUsable(c, skill, getSelf().owner))
                        .collect(Collectors.toList()));
        Skill.filterAllowedSkills(c, allowedMasterSkills, this, getSelf().owner);
        allowedEnemySkills.forEach(skill -> skillUse.add((combat, self, dontcare) -> Skill.resolve(skill, combat, getSelf().owner)));

        Global.pickRandom(skillUse).ifPresent(use -> use.execute(c, this, target));
    }

    @Override
    public void move() {}

    @Override
    public void draw(Combat c, Result flag) {}

    @Override
    public boolean human() {
        return false;
    }

    @Override
    public String bbLiner(Combat c) {
        return "";
    }

    @Override
    public String nakedLiner(Combat c) {
        return "";
    }

    @Override
    public String stunLiner(Combat c) {
        return "";
    }

    @Override
    public String taunt(Combat c) {
        return "";
    }

    @Override
    public void intervene(IEncounter fight, Character p1, Character p2) {}

    @Override
    public void showerScene(Character target, IEncounter encounter) {}

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void afterParty() {}

    @Override
    public void emote(Emotion emo, int amt) {}

    @Override
    public String challenge(Character other) {
        return "";
    }

    @Override
    public void promptTrap(IEncounter fight, Character target, Trap trap) {}

    @Override
    public void counterattack(Character target, Tactics type, Combat c) {}

    @Override
    public String getPortrait(Combat c) {
        return "";
    }

    @Override
    public Growth getGrowth() {
        return new Growth();
    }

    public boolean isPetOf(Character other) {
        return other != null && ownerType.equals(other.getType());
    }

    public Pet getSelf() {
        return self;
    }

    public boolean isPet() {
        return true;
    }
}