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

public class Handjob extends Skill {

    public Handjob(Character self) {
        super("Handjob", self);
        addTag(SkillTag.usesHands);
        addTag(SkillTag.pleasure);
    }

    public Handjob(String string, Character self) {
        super(string, self);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return c.getStance().reachBottom(getSelf())
                        && (target.crotchAvailable() || getSelf().has(Trait.dexterous)
                                        && target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getLayer() <= 1)
                        && target.hasDick() && getSelf().canAct()
                        && (!c.getStance().inserted(target) || c.getStance().en == Stance.anal);
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 7;
    }

    @Override
    public int accuracy(Combat c) {
        return c.getStance().en == Stance.neutral ? 35 : 100;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        int m = Global.random(8, 13);

        if (target.roll(getSelf(), c, accuracy(c))) {
            if (getSelf().get(Attribute.Seduction) >= 8) {
                m += 6;
                writeOutput(c, Result.normal, target);
                target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("cock"), m, c, this);
            } else {
                writeOutput(c, Result.weak, target);
                target.body.pleasure(getSelf(), getSelf().body.getRandom("hands"), target.body.getRandom("cock"), m, c, this);
            }
        } else {
            writeOutput(c, Result.miss, target);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return !user.has(Trait.temptress) && user.get(Attribute.Seduction) >= 5;
    }

    @Override
    public Skill copy(Character user) {
        return new Handjob(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return "You reach for " + target.name() + "'s dick but miss.";
        } else {
            return "You grab " + target.name()
                            + "'s girl-cock and stroke it using the techniques you use when masturbating.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.miss) {
            return String.format("%s grabs for %s dick and misses.",
                            getSelf().subject(), target.nameOrPossessivePronoun());
        }
        int r;
        if (!target.crotchAvailable()) {
            return String.format("%s slips %s hand into %s %s and strokes %s dick.",
                            getSelf().subject(), getSelf().possessivePronoun(),
                            target.nameOrPossessivePronoun(),
                            target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName(),
                            target.possessivePronoun());
        } else if (modifier == Result.weak) {
            return String.format("%s clumsily fondles %s crotch. It's not skillful by"
                            + " any means, but it's also not entirely ineffective.",
                            getSelf().subject(), target.nameOrPossessivePronoun());
        } else {
            if (target.getArousal().get() < 15) {
                return String.format("%s grabs %s soft penis and plays with the sensitive organ "
                                + "until it springs into readiness.",
                                getSelf().subject(), target.nameOrPossessivePronoun());
            }

            else if ((r = Global.random(3)) == 0) {
                return String.format("%s strokes and teases %s dick, sending shivers of pleasure up %s spine.",
                                getSelf().subject(), target.nameOrPossessivePronoun(),
                                target.possessivePronoun());
            } else if (r == 1) {
                return String.format("%s rubs the sensitive head of %s penis and fondles %s balls.",
                                getSelf().subject(), target.nameOrPossessivePronoun(),
                                target.possessivePronoun());
            } else {
                return String.format("%s jerks %s off like she's trying to milk every drop of %s cum.",
                                getSelf().subject(), target.subject(),
                                target.possessivePronoun());
            }
        }
    }

    @Override
    public String describe(Combat c) {
        return "Rub your opponent's dick";
    }

    @Override
    public boolean makesContact() {
        return true;
    }
    
    @Override
    public Stage getStage() {
        return Stage.FOREPLAY;
    }
}
