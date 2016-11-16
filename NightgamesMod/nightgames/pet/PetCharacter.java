package nightgames.pet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nightgames.characters.Character;
import nightgames.characters.Decider;
import nightgames.characters.Emotion;
import nightgames.characters.Growth;
import nightgames.characters.WeightedSkill;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.IEncounter;
import nightgames.combat.Result;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.status.Status;
import nightgames.trap.Trap;

public class PetCharacter extends Character {
    private static Set<SkillTag> PET_UNUSABLE_TAG = new HashSet<>();
    static {
        PET_UNUSABLE_TAG.add(SkillTag.suicidal);
        PET_UNUSABLE_TAG.add(SkillTag.counter);
    }
    
    private String type;
    private String ownerType;
    private Pet self;

    public PetCharacter(Pet self, String name, String type, Growth growth, int level) {
        super(name, 1);
        this.ownerType = self.owner().getType();
        this.self = self;
        this.type = type;
        this.setGrowth(growth);
        for (int i = 1; i < level; i++) {
            getGrowth().levelUp(this);
        }
        distributePoints(Arrays.asList());
        this.getSkills().clear();
        this.mojo.setMax(100);
        this.mojo.empty();
        this.arousal.empty();
        this.stamina.fill();
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
        List<Skill> allowedEnemySkills = new ArrayList<>(getSkills()
                        .stream().filter(skill -> Skill.skillIsUsable(c, skill, target) && !Collections.disjoint(skill.getTags(), PET_UNUSABLE_TAG))
                        .collect(Collectors.toList()));
        Skill.filterAllowedSkills(c, allowedEnemySkills, this, target);        

        List<Skill> allowedMasterSkills = new ArrayList<>(getSkills()
                        .stream().filter(skill -> Skill.skillIsUsable(c, skill, getSelf().owner)
                                        && skill.getTags().contains(SkillTag.helping)
                                        && !Collections.disjoint(skill.getTags(), PET_UNUSABLE_TAG))
                        .collect(Collectors.toList()));
        Skill.filterAllowedSkills(c, allowedMasterSkills, this, getSelf().owner);
        WeightedSkill bestEnemySkill = Decider.prioritizePet(this, target, allowedEnemySkills, c);
        WeightedSkill bestMasterSkill = Decider.prioritizePet(this, getSelf().owner, allowedMasterSkills, c);

        if (Global.isDebugOn(DebugFlags.DEBUG_PET)) {
            System.out.println("Available Enemy Skills " + allowedEnemySkills);
            System.out.println("Available Master Skills " + allowedMasterSkills);
        }

        // don't let the ratings be negative.
        double masterSkillRating = Math.max(.001, bestMasterSkill.rating);
        double enemySkillRating = Math.max(.001, bestEnemySkill.rating);

        double roll = Global.randomdouble(masterSkillRating + enemySkillRating) - masterSkillRating;
        if (Global.isDebugOn(DebugFlags.DEBUG_PET)) {
            System.out.printf("Rolled %s for master skill: %s [%.2f] and %s [%.2f]\n", roll, bestMasterSkill.skill.getLabel(c), -masterSkillRating, bestEnemySkill.skill.getLabel(c), enemySkillRating);
        }
        if (roll >= 0) {
            if (Global.isDebugOn(DebugFlags.DEBUG_PET)) {
                System.out.println("Using enemy skill " + bestEnemySkill.skill.getLabel(c));
            }
            Skill.resolve(bestEnemySkill.skill, c, target);
        } else {
            if (Global.isDebugOn(DebugFlags.DEBUG_PET)) {
                System.out.println("Using master skill " + bestMasterSkill.skill.getLabel(c));
            }
            Skill.resolve(bestMasterSkill.skill, c, self.owner());
        }
    }

    @Override
    public void add(Combat c, Status status) {
        super.add(c, status);
        if (stunned()) {
            c.write(this, Global.format("With {self:name-possessive} link to the fight weakened, {self:subject-action:disappears|disappears}..", this, this));
            c.removePet(this);
        }
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
    public String bbLiner(Combat c, Character target) {
        return "";
    }

    @Override
    public String nakedLiner(Combat c, Character target) {
        return "";
    }

    @Override
    public String stunLiner(Combat c, Character target) {
        return "";
    }

    @Override
    public String taunt(Combat c, Character target) {
        return "";
    }

    @Override
    public String challenge(Character other) {
        return "";
    }

    @Override
    public String getPortrait(Combat c) {
        return "";
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    protected void resolveOrgasm(Combat c, Character opponent, BodyPart selfPart, BodyPart opponentPart, int times, int totalTimes) {
        super.resolveOrgasm(c, opponent, selfPart, opponentPart, times, totalTimes);
        c.write(this, Global.format("The force of {self:name-possessive} orgasm destroys {self:possessive} anchor to the fight and {self:pronoun} disappears.", this, opponent));
        c.removePet(this);
    }

    @Override
    public void intervene(IEncounter fight, Character p1, Character p2) {}

    @Override
    public void showerScene(Character target, IEncounter encounter) {}
    @Override
    public void afterParty() {}
    
    @Override
    public void emote(Emotion emo, int amt) {}

    @Override
    public void promptTrap(IEncounter fight, Character target, Trap trap) {}

    @Override
    public void counterattack(Character target, Tactics type, Combat c) {}


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