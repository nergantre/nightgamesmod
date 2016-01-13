package nightgames.skills;

import java.util.Collection;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.TailPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.stance.Stance;
import nightgames.status.BodyFetish;
import nightgames.status.Shamed;
import nightgames.status.TailFucked;

public class TailPeg extends Skill {

    public TailPeg(Character self) {
        super("Tail Peg", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        Collection<BodyPart> tails = user.body.get("tail");
        boolean hasFuckableTail = tails.stream().anyMatch(p -> p.isType("tail") && p != TailPart.cat);
        return hasFuckableTail && (user.get(Attribute.Dark) >= 1 || user.get(Attribute.Seduction) >= 20);
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().getArousal().get() >= 30 && getSelf().canAct() && target.crotchAvailable()
                        && c.getStance().en != Stance.standing && c.getStance().en != Stance.standingover;
    }

    @Override
    public int getMojoCost(Combat c) {
        return 20;
    }

    @Override
    public String describe(Combat c) {
        if (c.getStance().anallyPenetrated(c.getOther(getSelf()))) {
            return "Fuck your opponent with your tail";
        }
        return "Shove your tail up your opponent's ass.";
    }

    @Override
    public String getLabel(Combat c) {
        if (c.getStance().anallyPenetrated(c.getOther(getSelf()))) {
            return "Tail Fuck";
        } else {
            return "Tail Peg";
        }
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (target.roll(this, c, accuracy(c) + (c.getStance().havingSex() ? 20 : -20))) {
            int strength = Math.min(20, 10 + getSelf().get(Attribute.Dark) / 4);
            boolean vaginal = c.getStance().anallyPenetrated(c.getOther(getSelf()));
            boolean shamed = false;
            if (!vaginal && Global.random(4) == 2) {
                target.add(c, new Shamed(target));
                shamed = true;
            }
            if (target.human()) {
                if (vaginal) {
                    c.write(getSelf(), receive(c, 0, Result.intercourse, target));
                } else if (c.getStance().inserted(target)) {
                    c.write(getSelf(), receive(c, 0, Result.special, target));
                } else if (c.getStance().dom(target)) {
                    c.write(getSelf(), receive(c, 0, Result.critical, target));
                } else if (c.getStance().behind(getSelf())) {
                    c.write(getSelf(), receive(c, 0, Result.strong, target));
                } else {
                    c.write(getSelf(), receive(c, 0, Result.normal, target));
                }
                if (shamed) {
                    c.write(getSelf(), "The shame of having your ass violated by " + getSelf().name()
                                    + " has destroyed your confidence.");
                }
            } else if (getSelf().human()) {
                if (vaginal) {
                    c.write(getSelf(), deal(c, 0, Result.intercourse, target));
                }
                if (c.getStance().inserted(target)) {
                    c.write(getSelf(), deal(c, 0, Result.special, target));
                } else if (c.getStance().dom(target)) {
                    c.write(getSelf(), deal(c, 0, Result.critical, target));
                } else if (c.getStance().behind(getSelf())) {
                    c.write(getSelf(), deal(c, 0, Result.strong, target));
                } else {
                    c.write(getSelf(), deal(c, 0, Result.normal, target));
                }
                if (shamed) {
                    c.write(getSelf(), "The shame of having her ass violated by you has destroyed " + target.getName()
                                    + " confidence.");
                }
            }
            if (c.getStance().havingSex()) {
                if (vaginal) {
                    target.body.pleasure(getSelf(), getSelf().body.getRandom("tail"), target.body.getRandom("pussy"),
                                    strength, c);
                    target.add(c, new TailFucked(target, getSelf(), "pussy"));
                } else {
                    target.body.pleasure(getSelf(), getSelf().body.getRandom("tail"), target.body.getRandom("ass"),
                                    strength, c);
                    target.add(c, new TailFucked(target, getSelf(), "ass"));
                }
            }
            target.pain(c, strength / 2);
            target.emote(Emotion.nervous, 10);
            target.emote(Emotion.desperate, 10);
            getSelf().emote(Emotion.confident, 15);
            getSelf().emote(Emotion.dominant, 25);
            if (Global.random(100) < 5 + 2 * getSelf().get(Attribute.Fetish)) {
                target.add(c, new BodyFetish(target, getSelf(), "tail", .25));
            }
        } else {
            if (target.human()) {
                c.write(getSelf(), receive(c, 0, Result.miss, target));
            } else {
                c.write(getSelf(), deal(c, 0, Result.miss, target));
            }
            return false;
        }
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new TailPeg(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.pleasure;
    }

    @Override
    public String deal(Combat c, int magnitude, Result modifier, Character target) {
        switch (modifier) {
            case critical:
                return "You flex your prehensile tail and spread " + target.nameOrPossessivePronoun() + " legs apart. "
                                + "You quickly lube it up with " + target.possessivePronoun()
                                + " juices and slide it into her ass and start pumping.";
            case miss:
                return "You try to peg " + target.name() + " with your tail, but " + target.pronoun()
                                + " manages to clench " + target.possessivePronoun()
                                + " butt cheeks together in time to keep you out.";
            case normal:
                return "You move towards " + target.name() + " and hold " + target.possessivePronoun() + " hands above "
                                + target.possessivePronoun()
                                + " head. In the same motion, you swiftly plunge your thick tail into "
                                + target.possessivePronoun() + " ass, pumping it in and out of "
                                + target.possessivePronoun() + " tight hole.";
            case special:
                return "You smile down at " + target.name() + " and move your flexible tail behind "
                                + target.directObject() + ". You spread " + target.possessivePronoun()
                                + " cheeks with your tail and plunge it into " + target.possessivePronoun()
                                + " tight pucker. " + target.name() + " moans loudly at the sudden intrusion.";
            case intercourse:
                return "You smile down at " + target.name() + " and move your flexible tail behind "
                                + target.directObject() + ". You spread " + target.possessivePronoun()
                                + " legs with your tail and plunge it into " + target.possessivePronoun()
                                + " wet slit. " + target.name() + " moans loudly at the sudden intrusion.";
            case strong:
                if (target.body.getLargestBreasts().size >= 2) {
                    return "You hug " + target.name()
                                    + " from behind and cup her breasts with your hands. Taking advantage of her surprise, you shove your tail into her ass, and tickle her prostate with the tip.";
                } else {
                    return "You hug " + target.name() + " from behind and twist " + target.possessivePronoun()
                                    + " nipples. Taking advantage of " + target.possessivePronoun()
                                    + " surprise, you shove your tail into " + target.possessivePronoun()
                                    + " ass, and tickle " + target.possessivePronoun() + " prostate with the tip.";
                }
            default:
                return "<<This should not be displayed, please inform The Silver Bard: TailPeg-deal>>";
        }
    }

    @Override
    public String receive(Combat c, int magnitude, Result modifier, Character target) {
        switch (modifier) {
            case critical:
                return "Smiling down on you, " + getSelf().name()
                                + " spreads your legs and tickles your butt with her tail."
                                + " You notice how the tail itself is slick and wet as it"
                                + " slowly pushes through your anus, spreading your cheeks a part. " + getSelf().name()
                                + " pumps it in and out a for a few times before taking " + "it out again.";
            case miss:
                return getSelf().name() + " tries to peg you with her tail but you manage to push"
                                + " your butt cheeks together in time to keep it out.";
            case normal:
                return getSelf().name() + " suddenly moves very close to you. You expect an attack from the front"
                                + " and try to move back, but end up shoving her tail right up your ass.";
            case special:
                return getSelf().name() + " smirks and wiggles her tail behind her back. You briefly look "
                                + "at it and the see the appendage move behind you. You try to keep it"
                                + " out by clenching your butt together, but a squeeze of " + getSelf().name()
                                + "'s vagina breaks your concentration, so the tail slides up your ass"
                                + " and you almost lose it as your cock and ass are stimulated so thoroughly"
                                + " at the same time.";
            case intercourse:
                return getSelf().name() + " smirks and coils her tail around in front of you. You briefly look "
                                + "at it and the see the appendage move under you and panic. You try to keep it"
                                + " out by clamping your legs together, but a squeeze of " + getSelf().name()
                                + "'s ass breaks your concentration, so the tail slides smoothly into your pussy.";
            case strong:
                return getSelf().name() + " hugs you from behind and rubs her chest against your back."
                                + " Distracted by that, she managed to push her tail between your"
                                + " ass cheeks and started tickling your prostate with the tip.";
            default:
                return "<<This should not be displayed, please inform The Silver Bard: TailPeg-receive>>";
        }
    }

    @Override
    public boolean makesContact() {
        return true;
    }
}
