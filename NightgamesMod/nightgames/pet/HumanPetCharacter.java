package nightgames.pet;

import nightgames.characters.Character;
import nightgames.characters.NPC;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.Outfit;

public class HumanPetCharacter extends PetCharacter {
    private NPC prototype;

    public HumanPetCharacter(Pet self, NPC prototypeCharacter, int level) throws CloneNotSupportedException {
        super(self, prototypeCharacter.getName(), prototypeCharacter.getType() + "Pet", prototypeCharacter.getGrowth(), 1);
        prototype = prototypeCharacter.clone();
        for (int i = 1; i < level; i++) {
            getGrowth().levelUp(this);
        }
        distributePoints(prototypeCharacter.ai.getPreferredAttributes());
        this.getSkills().clear();
        this.body = prototypeCharacter.body.clone(this);
        this.outfit = new Outfit(prototypeCharacter.outfit);
        this.mojo.setMax(prototype.getMojo().trueMax());
        this.arousal.setMax(prototype.getArousal().trueMax());
        this.willpower.setMax(prototype.getWillpower().trueMax());
        this.stamina.setMax(prototype.getStamina().trueMax());
        this.mojo.empty();
        this.arousal.empty();
        this.stamina.fill();
        Global.learnSkills(this);
    }

    @Override
    public PetCharacter cloneWithOwner(Character owner) throws CloneNotSupportedException {
        HumanPetCharacter clone = (HumanPetCharacter) super.cloneWithOwner(owner);
        clone.prototype = prototype.clone();
        return clone;
    }

    @Override
    public String bbLiner(Combat c, Character target) {
        return prototype.bbLiner(c, target);
    }

    @Override
    public String nakedLiner(Combat c, Character target) {
        return prototype.nakedLiner(c, target);
    }

    @Override
    public String stunLiner(Combat c, Character target) {
        return prototype.stunLiner(c, target);
    }

    @Override
    public String taunt(Combat c, Character target) {
        return prototype.taunt(c, target);
    }

    @Override
    public String temptLiner(Combat c, Character target) {
        return prototype.temptLiner(c, target);
    }

    @Override
    public String challenge(Character other) {
        return prototype.challenge(other);
    }

    @Override
    public String getPortrait(Combat c) {
        return prototype.ai.image(c);
    }
}