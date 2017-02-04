package nightgames.skills;

import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Player;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class WildThrust extends Thrust {
    public WildThrust(Character self) {
        super("Wild Thrust", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Animism) > 1 || (user.human() && ((Player)user).checkAddiction(AddictionType.BREEDER));
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return havingSex(c, target);
    }

    @Override
    public int getMojoBuilt(Combat c) {
        return 5;
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        boolean effective = super.resolve(c, target);
        if (effective && c.getStance().sub(getSelf()) && getSelf().has(Trait.Untamed) && Global.random(4) == 0 ) {
            c.write(getSelf(), Global.format("{self:SUBJECT-ACTION:fuck|fucks} {other:name-do} with such abandon that it leaves {other:direct-object} "
                            + "momentarily dazed. {self:SUBJECT-ACTION:do|does} not let this chance slip and {self:action:rotate|rotates} {self:possessive} body so that {self:pronoun-action:are|is} on top!", getSelf(), target));
            c.setStance(c.getStance().reverse(c, false));
        }
        if (effective && getSelf().has(Trait.breeder) && c.getStance().vaginallyPenetratedBy(c, getSelf(), target)
                         && target.human()) {
            c.write(getSelf(), Global.format("The sheer ferocity of {self:name-possessive} movements"
                            + " fill you with an unnatural desire to sate {self:possessive} thirst with"
                            + " your cum.", getSelf(), target));
            ((Player) target).addict(AddictionType.BREEDER, getSelf(), Addiction.LOW_INCREASE);
        }
        return effective;
    }

    @Override
    public int[] getDamage(Combat c, Character target) {
        int results[] = new int[2];

        int m = 5 + Global.random(20) + Math
                        .min(getSelf().get(Attribute.Animism), getSelf().getArousal().getReal() / 30);
        int mt = 5 + Global.random(20);
        mt = Math.max(1, mt);

        results[0] = m;
        results[1] = mt;

        Player p = null;
        if (getSelf().human()) {
            p = (Player) getSelf();
        } else if (target.human()) {
            p = (Player) target;
        }

        if (p == null) {
            return results;
        }

        Character npc = c.getOpponent(p);
        Optional<Addiction> addiction = p.getAddiction(AddictionType.BREEDER);
        if (!addiction.isPresent()) {
            return results;
        }

        Addiction add = addiction.get();
        if (getSelf().human()) {
            if (add.wasCausedBy(npc)) {
                //Increased recoil vs Kat
                mt *= 1 + ((float) add.getSeverity().ordinal() / 3.f);
                p.addict(AddictionType.BREEDER, npc, Addiction.LOW_INCREASE);
            } else {
                //Increased damage vs everyone else
                m *= 1 + ((float) add.getSeverity().ordinal() / 3.f);
            }
        } else if (target.human() && add.wasCausedBy(npc)) {
            m *= 1 + ((float) add.getSeverity().ordinal() / 4.f);
        }

        results[0] = m;
        results[1] = mt;

        return results;
    }

    @Override
    public Skill copy(Character user) {
        return new WildThrust(user);
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.anal || modifier == Result.upgrade) {
            return "You wildly pound " + target.getName()
                            + " in the ass with no regard to technique. She whimpers in pleasure and can barely summon the strength to hold herself off the floor.";
        } else if (modifier == Result.reverse) {
            return Global.format(
                            "{self:SUBJECT-ACTION:%s {other:name-possessive} cock with no regard to technique, relentlessly driving you both towards orgasm.",
                            getSelf(), target, c.getStance().sub(getSelf()) ? "grind} against" : "bounce} on");
        } else {
            return "You wildly pound your dick into " + target.getName()
                            + "'s pussy with no regard to technique. Her pleasure filled cries are proof that you're having an effect, but you're feeling it "
                            + "as much as she is.";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.anal) {
            return String.format("%s passionately pegs %s in the ass as %s %s and %s to endure the sensation.",
                            getSelf().subject(), target.nameDirectObject(), target.pronoun(),
                            target.action("groan"), target.action("try", "tries"));
        } else if (modifier == Result.upgrade) {
            return String.format("%s pistons wildly into %s while pushing %s shoulders on the ground; %s tits "
                            + "are shaking above %s head while %s strapon stimulates %s %s.", getSelf().subject(),
                            target.nameDirectObject(), target.possessiveAdjective(),
                            Global.capitalizeFirstLetter(getSelf().possessiveAdjective()), target.possessiveAdjective(),
                            getSelf().possessiveAdjective(), target.possessiveAdjective(),
                            target.hasBalls() ? "prostate" : "insides");
        } else if (modifier == Result.reverse) {
            return String.format("%s frenziedly %s %s cock, relentlessly driving %s both toward orgasm.",
                            getSelf().subject(), target.nameOrPossessivePronoun(), c.bothDirectObject(target), c.getStance().sub(getSelf()) ? "grinds against" : "bounces on");
        } else {
            return Global.format(
                            "{self:SUBJECT-ACTION:rapidly pound|rapidly pounds} {self:possessive} {self:body-part:cock} into {other:possessive} {other:body-part:pussy}, "
                                            + "relentlessly driving %s both toward orgasm",
                            getSelf(), target, c.bothDirectObject(target));
        }
    }

    @Override
    public String describe(Combat c) {
        return "Fucks opponent without holding back. Extremely random large damage.";
    }

    @Override
    public String getName(Combat c) {
        if (c.getStance().penetratedBy(c, c.getStance().getPartner(c, getSelf()), getSelf())) {
            return "Wild Thrust";
        } else if (c.getStance().sub(getSelf())) {
            return "Wil Grind";
        } else {
            return "Wild Ride";
        }
    }

    @Override
    public boolean makesContact() {
        return true;
    }
    
    @Override
    public Stage getStage() {
        return Stage.FINISHER;
    }
}
