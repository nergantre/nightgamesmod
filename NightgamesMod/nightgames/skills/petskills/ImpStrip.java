package nightgames.skills.petskills;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.nskills.tags.SkillTag;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;

public class ImpStrip extends SimpleEnemySkill {
    public ImpStrip(Character self) {
        super("Imp Strip", self);
        addTag(SkillTag.stripping);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return super.usable(c, target) && !getStrippableSlots(c, target).isEmpty();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 5;
    }

    private List<ClothingSlot> getStrippableSlots(Combat c, Character target) {
        List<ClothingSlot> strippable = new ArrayList<>();
        if (!target.crotchAvailable()) {
            strippable.add(ClothingSlot.bottom);
        }
        if (!target.breastsAvailable()) {
            strippable.add(ClothingSlot.top);
        }
        return strippable;
    }
    @Override
    public boolean resolve(Combat c, Character target) {        
        Optional<ClothingSlot> targetSlot = Global.pickRandom(getStrippableSlots(c, target));
        int difficulty = !targetSlot.isPresent() ? 999999 : target.getOutfit().getTopOfSlot(targetSlot.get()).dc() + target.getLevel()
                + (target.getStamina().percent() / 5 - target.getArousal().percent()) / 4
                - (!target.canAct() || c.getStance().sub(target) ? 20 : 0);
        if (getSelf().check(Attribute.Cunning, difficulty)) {
            // should never be null here, since otherwise we can't use the skill          
            Clothing stripped = target.strip(targetSlot.get(), c);
            c.write(getSelf(), Global.format("{self:SUBJECT} steals {other:name-possessive} %s and runs off with it.", 
                            getSelf(), target, stripped.getName()));
            target.emote(Emotion.nervous, 10);
        } else {
            c.write(getSelf(), Global.format("{self:SUBJECT} yanks on {other:name-possessive} %s ineffectually.",
                            getSelf(), target, target.outfit.getTopOfSlot(targetSlot.orElse(ClothingSlot.top))));
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new ImpStrip(user);
    }

    @Override
    public int speed() {
        return 8;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.stripping;
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
