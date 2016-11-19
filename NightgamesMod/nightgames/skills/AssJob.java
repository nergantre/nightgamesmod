package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.ClothingSlot;
import nightgames.nskills.tags.SkillTag;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;

public class AssJob extends Skill {

    public AssJob(Character self) {
        super("Assjob", self);
        addTag(SkillTag.anal);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 25;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && target.hasDick() && selfNakedOrUnderwear()
                        && (c.getStance().behind(target)
                                        || (c.getStance().en == Stance.reversemount && c.getStance().dom(getSelf()))
                                        || c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf())
                                                        && !c.getStance().behind(getSelf()));
    }

    @Override
    public String describe(Combat c) {
        return "Hump your opponent's cock with your ass";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (c.getStance().behind(target)) {
            writeOutput(c, Result.special, target);
            int m = 4 + Global.random(4);
            int fetishChance = 20 + getSelf().get(Attribute.Fetish) / 2;
            if (target.crotchAvailable()) {
                if (getSelf().crotchAvailable()) {
                    m += 6;
                    fetishChance += 30;
                } else {
                    m += 3;
                    fetishChance += 15;
                }
                if (getSelf().has(Trait.bewitchingbottom)) {
                    fetishChance *= 2;
                }
            }
            target.body.pleasure(getSelf(), getSelf().body.getRandomAss(), target.body.getRandomCock(), m, c, this);

            if (Global.random(100) < fetishChance) {
                target.add(c, new BodyFetish(target, getSelf(), "ass", .1 + getSelf().get(Attribute.Fetish) * .05));
            }
        } else if (target.roll(getSelf(), c, accuracy(c))) {
            if (c.getStance().en == Stance.reversemount) {
                writeOutput(c, Result.strong, target);
                int m = 4 + Global.random(4);
                int fetishChance = 20 + getSelf().get(Attribute.Fetish) / 2;
                if (target.crotchAvailable()) {
                    if (getSelf().crotchAvailable()) {
                        m += 6;
                        fetishChance += 30;
                    } else {
                        m += 3;
                        fetishChance += 15;
                    }
                    if (getSelf().has(Trait.bewitchingbottom)) {
                        fetishChance *= 2;
                    }
                }
                if (target.body.getRandomCock().isReady(target)) {
                    target.body.pleasure(getSelf(), getSelf().body.getRandomAss(), target.body.getRandomCock(), m, c, this);
                } else {
                    target.tempt(c, getSelf(), getSelf().body.getRandomAss(), m);
                }

                if (Global.random(100) < fetishChance) {
                    target.add(new BodyFetish(target, getSelf(), "ass", .1 + getSelf().get(Attribute.Fetish) * .05));
                }
            } else {
                writeOutput(c, Result.normal, target);
                int m = 4 + Global.random(3);
                if (target.crotchAvailable()) {
                    if (getSelf().crotchAvailable()) {
                        m += 6;
                    } else {
                        m += 3;
                    }
                }
                target.body.pleasure(getSelf(), getSelf().body.getRandomAss(), target.body.getRandomCock(), m, c, this);
            }
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new AssJob(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    private boolean selfNakedOrUnderwear() {
        return getSelf().getOutfit().slotEmptyOrMeetsCondition(ClothingSlot.bottom, c -> c.getLayer() == 0);
    }

    private boolean selfWearingUnderwear() {
        return getSelf().getOutfit().getSlotAt(ClothingSlot.bottom, 0) != null;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        switch (modifier) {
            case special:
                if (getSelf().crotchAvailable() && target.crotchAvailable()) {
                    return String.format("You push your naked ass back against" + " %s %s, rubbing it with vigor.",
                                    target.nameOrPossessivePronoun(), target.body.getRandomCock().describe(target));
                } else {
                    return String.format("You relax slightly in %s arms and rub your ass" + " into %s crotch.",
                                    target.nameOrPossessivePronoun(), target.possessivePronoun());
                }
            case strong:
                if (!target.crotchAvailable()) {
                    return String.format("You hump your ass against %s covered groin.",
                                    target.nameOrPossessivePronoun());
                } else if (target.body.getRandomCock().isReady(getSelf())) {
                    return String.format(
                                    "You wedge %s %s in your soft crack and"
                                                    + " firmly rub it up against you, eliciting a quiet moan from"
                                                    + " %s.",
                                    target.nameOrPossessivePronoun(), target.body.getRandomCock().describe(target),
                                    target.directObject());
                } else {
                    return String.format(
                                    "You lean back and rub your ass against %s, but"
                                                    + " %s %s is still too soft to really get into it.",
                                    target.name(), target.possessivePronoun(),
                                    target.body.getRandomCock().describe(target));
                }
            case normal:
                return String.format("You back up against %s and grab %s by the waist."
                                + " Before %s has a chance to push you away, you rub your ass against" + " %s crotch.",
                                target.name(), target.directObject(), target.pronoun(), target.possessivePronoun());
            case miss:
            default:
                return String.format("You try to mash your ass against %s crotch, but %s" + " pushes you away.",
                                target.nameOrPossessivePronoun(), target.pronoun());
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        switch (modifier) {
            case special:
                String res = String.format(
                                "%s %s tight, thinking %s intends to break "
                                                + "free from %s hold, but instead %s pushes %s firm asscheeks"
                                                + " against %s cock and grinds them against %s. ",
                                target.subjectAction("hold"), getSelf().name(), getSelf().pronoun(), 
                                target.possessivePronoun(), getSelf().pronoun(),
                                getSelf().possessivePronoun(), target.possessivePronoun(), target.directObject());
                if (getSelf().crotchAvailable() && target.crotchAvailable()) {
                    res += String.format("%s %s slides between %s mounds as if it belongs there.",
                                    target.possessivePronoun(), target.body.getRandomCock().describe(target), 
                                    getSelf().possessivePronoun());
                } else {
                    res += String.format(
                                    "The swells of %s ass feel great on %s cock even through the clothing between %s.",
                                    getSelf().possessivePronoun(), target.possessivePronoun(), c.bothDirectObject(target));
                }
                return res;
            case strong:
                if (!target.crotchAvailable()) {
                    return String.format(
                                    "%s sits firmly on %s crotch and starts "
                                                    + "dryhumping %s with an impish grin. As %s grinds against %s "
                                                    + "%s restlessly, %s %s definitely feeling it much more than %s is.",
                                    getSelf().name(), target.nameOrPossessivePronoun(), target.directObject(), 
                                    getSelf().pronoun(), target.possessivePronoun(),
                                    target.outfit.getTopOfSlot(ClothingSlot.bottom).getName(),
                                    target.pronoun(), target.action("are", "is"), getSelf().pronoun());
                } else if (target.body.getRandomCock().isReady(getSelf())) {
                    return String.format(
                                    "%s lays back on %s, squeezing %s %s between %s soft asscheeks. %s %s to "
                                                    + "crawl away, but %s grinds %s perky butt against %s, massaging %s hard-on %s.",
                                    getSelf().name(), target.subject(), target.possessivePronoun(),
                                    target.body.getRandomCock().describe(getSelf()),
                                    getSelf().possessivePronoun(),
                                    Global.capitalizeFirstLetter(target.pronoun()),
                                    target.action("try", "tries"),
                                    getSelf().pronoun(), getSelf()
                                                    .possessivePronoun(),
                                                    target.directObject(),
                                                    target.possessivePronoun(),
                                    selfWearingUnderwear()
                                                    ? "with "+getSelf().possessivePronoun()+" soft " + getSelf().getOutfit()
                                                                    .getBottomOfSlot(ClothingSlot.bottom).getName()
                                                    : "in "+getSelf().possessivePronoun()+" luscious crack");
                } else {
                    return String.format(
                                    "%s to slide from under %s, but %s leans "
                                                    + "forward, holding down %s legs. %s feel %s round ass press"
                                                    + " against %s groin as %s sits back on %s. <i>\"Like what "
                                                    + "you see?\"</i> - %s taunts %s, shaking %s hips invitingly.",
                                                    target.subjectAction("try", "tries"),
                                    getSelf().name(), getSelf().pronoun(), 
                                    target.possessivePronoun(), Global.capitalizeFirstLetter(target.pronoun()),
                                    getSelf().possessivePronoun(), target.possessivePronoun(),
                                    getSelf().pronoun(), target.directObject(),
                                    getSelf().pronoun(), target.directObject(), getSelf().possessivePronoun());
                }
            case normal:
                return String.format(
                                "Unexpectedly, %s turns around and rams %s waist against "
                                                + "%s groin, taking hold of %s arms before %s can recover %s balance."
                                                + " %s takes the opportunity to tease %s, rubbing %s bubble butt against "
                                                + "%s sensitive %s.",
                                getSelf().name(), getSelf().possessivePronoun(),
                                target.possessivePronoun(), target.possessivePronoun(), target.pronoun(), target.possessivePronoun(),
                                Global.capitalizeFirstLetter(getSelf().pronoun()), 
                                target.directObject(), getSelf().possessivePronoun(),
                                target.possessivePronoun(), target.body.getRandomCock().describe(target));
            case miss:
            default:
                return String.format("%s moves %s ass towards %s crotch, but %s %s her away.", getSelf().name(),
                                getSelf().possessivePronoun(), target.nameOrPossessivePronoun(), target.pronoun(),
                                target.action("push", "pushes"));
        }
    }

}
