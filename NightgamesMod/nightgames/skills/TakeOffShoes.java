package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.items.clothing.ClothingSlot;
import nightgames.nskills.tags.SkillTag;

public class TakeOffShoes extends Skill {

    public TakeOffShoes(Character self) {
        super("Remove Shoes", self);
        addTag(SkillTag.undressing);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Cunning) >= 5 && !user.human();
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && !getSelf().outfit.hasNoShoes();
    }

    @Override
    public String describe(Combat c) {
        return "Remove your own shoes";
    }

    @Override
    public float priorityMod(Combat c) {
        return -10.0f;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        getSelf().strip(ClothingSlot.feet, c);
        if (getSelf().human()) {
            deal(c, 0, null, target);
        } else {
            receive(c, 0, null, target);            
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new TakeOffShoes(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.misc;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You take a moment to kick off your footwear.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return getSelf().subject() + " takes a moment to kick off " + getSelf().possessivePronoun() + " footwear.";
    }
}
