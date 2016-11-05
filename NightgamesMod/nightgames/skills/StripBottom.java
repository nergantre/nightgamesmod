package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;

public class StripBottom extends Skill {

    private Clothing stripped, extra;

    public StripBottom(Character self) {
        super("Strip Bottoms", self);
    }

    @Override public boolean usable(Combat c, Character target) {
        return (c.getStance().oral(getSelf()) || c.getStance().reachBottom(getSelf())) && !target.crotchAvailable()
                        && getSelf().canAct();
    }

    @Override public int getMojoCost(Combat c) {
        return 10;
    }

    @Override public boolean resolve(Combat c, Character target) {
        stripped = extra = null;
        int difficulty = target.getOutfit().getTopOfSlot(ClothingSlot.bottom).dc() + target.getLevel()
                        + (target.getStamina().percent() / 4 - target.getArousal().percent()) / 5 - (
                        !target.canAct() || c.getStance().sub(target) ? 20 : 0);
        if (getSelf().check(Attribute.Cunning, difficulty) || !target.canAct()) {
            stripped = target.strip(ClothingSlot.bottom, c);
            boolean doubled = false;
            if (getSelf().get(Attribute.Cunning) >= 30 && !target.crotchAvailable() && getSelf()
                            .check(Attribute.Cunning, difficulty) || !target.canAct()) {
                extra = target.strip(ClothingSlot.bottom, c);
                doubled = true;
                writeOutput(c, Result.critical, target);
            } else {
                writeOutput(c, Result.normal, target);
            }
            if (getSelf().human() && target.mostlyNude()) {
                c.write(target, target.nakedLiner(c));
            }
            if (target.human() && target.crotchAvailable() && target.hasDick()) {
                if (target.body.getRandomCock().isReady(target)) {
                    c.write("Your boner springs out, no longer restrained by your pants.");
                } else {
                    c.write(getSelf().name() + " giggles as "+target.nameOrPossessivePronoun()
                    +" flaccid dick is exposed.");
                }
            }
            target.emote(Emotion.nervous, doubled ? 20 : 10);
        } else {
            stripped = target.outfit.getTopOfSlot(ClothingSlot.bottom);
            writeOutput(c, Result.miss, target);
            target.weaken(c, Global.random(6) + getSelf().get(Attribute.Power) / 4);
            return false;
        }
        return true;
    }

    @Override public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Cunning) >= 3;
    }

    @Override public Skill copy(Character user) {
        return new StripBottom(user);
    }

    @Override public int speed() {
        return 3;
    }

    @Override public Tactics type(Combat c) {
        return Tactics.stripping;
    }

    @Override public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You grab " + target.name() + "'s " + stripped.getName() + ", but " + target.pronoun()
                            + " scrambles away before you can strip " + target.directObject() + ".";
        } else {
            String msg = "After a brief struggle, you manage to pull off " + target.name() + "'s " + stripped.getName()
                            + ".";
            if (modifier == Result.critical && extra != null) {
                msg += String.format(" Taking advantage of the situation, you also manage to snag %s %s!",
                                target.possessivePronoun(), extra.getName());
            }
            return msg;
        }
    }

    @Override public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s tries to pull down %s %s, but %s %s them up.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            stripped.getName(), target.pronoun(), target.action("hold"));
        } else {
            String msg = String.format("%s grabs the waistband of %s %s and pulls them down.",
                            getSelf().subject(), target.nameOrPossessivePronoun(),
                            stripped.getName());
            if (modifier == Result.critical && extra != null) {
                msg += String.format(" Before %s can react, %s also strips off %s %s!", target.subject(),
                                getSelf().name, target.possessivePronoun(), extra.getName());
            }
            return msg;
        }
    }

    @Override public String describe(Combat c) {
        return "Attempt to remove opponent's pants. More likely to succeed if she's weakened and aroused";
    }

    @Override public boolean makesContact() {
        return true;
    }
}
