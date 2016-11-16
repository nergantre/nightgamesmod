package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.nskills.tags.SkillTag;

public class Dissolve extends Skill {

    public Dissolve(Character self) {
        super("Dissolve", self);
        addTag(SkillTag.stripping);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().mobile(getSelf()) && getSelf().canAct()
                        && (getSelf().has(Item.DisSol) || getSelf().get(Attribute.Slime) > 0)
                        && target.outfit.getRandomShreddableSlot() != null && !c.getStance().prone(getSelf());
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        ClothingSlot toShred = null;
        if (!target.outfit.slotOpen(ClothingSlot.bottom) && !target.outfit.slotUnshreddable(ClothingSlot.bottom)) {
            toShred = ClothingSlot.bottom;
        } else if (!target.outfit.slotOpen(ClothingSlot.top) && !target.outfit.slotUnshreddable(ClothingSlot.top)) {
            toShred = ClothingSlot.top;
        }
        if (getSelf().get(Attribute.Slime) > 0) {
            Clothing destroyed = shred(target, toShred);
            String msg = "{self:SUBJECT-ACTION:reach|reaches} out with a slimy hand and"
                            + " {self:action:caress|caresses} {other:possessive} " + destroyed.getName()
                            + ". Slowly, it dissolves away beneath {self:possessive} touch.";
            c.write(getSelf(), Global.format(msg, getSelf(), target));
        } else {
            getSelf().consume(Item.DisSol, 1);
            if (getSelf().has(Item.Aersolizer)) {
                writeOutput(c, Result.special, target);
                shred(target, toShred);
            } else if (target.roll(getSelf(), c, accuracy(c))) {
                writeOutput(c, Result.normal, target);
                shred(target, toShred);
            } else {
                writeOutput(c, Result.miss, target);
                return false;
            }
        }
        return true;
    }

    private Clothing shred(Character target, ClothingSlot slot) {
        if (slot == null)
            return target.shredRandom();
        return target.shred(slot);
    }
    
    @Override
    public Skill copy(Character user) {
        return new Dissolve(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.stripping;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.special) {
            return "You pop a Dissolving Solution into your Aerosolizer and spray " + target.name()
                            + " with a cloud of mist. She emerges from the cloud with her clothes rapidly "
                            + "melting off her body.";
        } else if (modifier == Result.miss) {
            return "You throw a Dissolving Solution at " + target.name()
                            + ", but she avoids most of it. Only a couple drops burn through her outfit.";
        } else {
            return "You throw a Dissolving Solution at " + target.name() + ", which eats away her clothes.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character attacker) {
        if (modifier == Result.special) {
            return String.format("%s inserts a bottle into the attachment on her arm. "
                            + "%s suddenly surrounded by a cloud of mist."
                            + " %s clothes begin to disintegrate immediately.",
                            getSelf().subject(), Global.capitalizeFirstLetter(attacker.subjectAction("are", "is")),
                            Global.capitalizeFirstLetter(attacker.nameOrPossessivePronoun()));
        } else if (modifier == Result.miss) {
            return String.format("%s splashes a bottle of liquid in %s direction, but none of it hits %s.",
                            getSelf().subject(), attacker.nameOrPossessivePronoun(), attacker.directObject());
        } else {
            return String.format("%s covers you with a clear liquid. %s clothes dissolve away, but it doesn't do anything to %s skin.",
                            getSelf().subject(), Global.capitalizeFirstLetter(attacker.subject()), attacker.possessivePronoun());
        }
    }

    @Override
    public String describe(Combat c) {
        if (getSelf().get(Attribute.Slime) > 0)
            return "Use your slime to dissolve your opponent's clothes";
        return "Throws dissolving solution to destroy opponent's clothes";
    }

}
