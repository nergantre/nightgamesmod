package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;

public class Dissolve extends Skill {

    public Dissolve(Character self) {
        super("Dissolve", self);
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
                if (getSelf().human()) {
                    c.write(getSelf(), deal(c, 0, Result.special, target));
                } else if (target.human()) {
                    c.write(getSelf(), receive(c, 0, Result.special, getSelf()));
                }
                shred(target, toShred);
            } else if (target.roll(this, c, accuracy(c))) {
                if (getSelf().human()) {
                    c.write(getSelf(), deal(c, 0, Result.normal, target));
                } else if (target.human()) {
                    c.write(getSelf(), receive(c, 0, Result.normal, getSelf()));
                }
                shred(target, toShred);
            } else {
                if (getSelf().human()) {
                    c.write(getSelf(), deal(c, 0, Result.miss, target));
                } else if (target.human()) {
                    c.write(getSelf(), receive(c, 0, Result.miss, target));
                }
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
            return getSelf().name()
                            + " inserts a bottle into the attachment on her arm. You're suddenly surrounded by a cloud of mist. Your clothes begin to disintegrate immediately.";
        } else if (modifier == Result.miss) {
            return getSelf().name() + " splashes a bottle of liquid in your direction, but none of it hits you.";
        } else {
            return getSelf().name()
                            + " covers you with a clear liquid. Your clothes dissolve away, but it doesn't do anything to your skin.";
        }
    }

    @Override
    public String describe(Combat c) {
        if (getSelf().get(Attribute.Slime) > 0)
            return "Use your slime to dissolve your opponent's clothes";
        return "Throws dissolving solution to destroy opponent's clothes";
    }

}
