package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.body.PussyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.status.Stsflag;

public class ToggleSlimePussy extends Skill {

    public ToggleSlimePussy(Character self) {
        super("Toggle Slime Pussy", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return getSelf().get(Attribute.Slime) > 14;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && (!hasSlimePussy() || !c.getStance().vaginallyPenetrated(c, getSelf()));
    }

    @Override
    public String getLabel(Combat c) {
        if (hasSlimePussy()) {
            return "Remove Pussy";
        } else {
            return "Grow Pussy";
        }
    }

    @Override
    public float priorityMod(Combat c) {
        return (float) ((getSelf().dickPreference() - 5) * (hasSlimePussy() ? .3 : -.3));
    }

    @Override
    public int getMojoCost(Combat c) {
        return 15;
    }

    @Override
    public String describe(Combat c) {
        if (hasSlimePussy()) {
            return "Fill up the hole between your legs with extra slime, closing it off";
        } else {
            return "Form a gooey pussy between your legs";
        }
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        String msg = "{self:SUBJECT-ACTION:close|closes} {self:possessive} eyes ";
        if (hasSlimePussy()) {
            if (getSelf().crotchAvailable() || getSelf().human()) {
                msg += "and the cleft of {self:possessive} {self:body-part:pussy} flattens out, leaving only smooth slime.";
            } else {
                msg += getSelf().outfit.getTopOfSlot(ClothingSlot.bottom).getName()
                                + " loses some of its definition, as if something that was beneath them no longer is.";
            }
            getSelf().body.removeAll("pussy");
        } else {
            if (getSelf().crotchAvailable() || getSelf().human()) {
                msg += "and a slit forms in {self:possessive} slime. The new pussy's lips shudder invitingly.";
            } else {
                msg += "but you see no outside changs. Perhaps they are hidden under {self:possessive} clothes?";
            }
            getSelf().body.add(PussyPart.gooey);
        }
        if (!target.human() || !target.is(Stsflag.blinded))
            c.write(getSelf(), Global.format(msg, getSelf(), target));
        else 
            printBlinded(c);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new ToggleSlimePussy(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.misc;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return null;
    }

    private boolean hasSlimePussy() {
        return getSelf().hasPussy() && getSelf().body.getRandomPussy().moddedPartCountsAs(getSelf(), PussyPart.gooey);
    }
}
