package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.stance.Anal;
import nightgames.stance.AnalProne;
import nightgames.status.Flatfooted;
import nightgames.status.Frenzied;
import nightgames.status.IgnoreOrgasm;
import nightgames.status.Oiled;
import nightgames.status.Stsflag;

public class AssFuck extends Fuck {
    public AssFuck(Character self) {
        super("Ass Fuck", self, 0);
    }

    @Override
    public float priorityMod(Combat c) {
        return 0.0f + (getSelf().getMood() == Emotion.dominant ? 1.0f : 0);
    }

    @Override
    public BodyPart getTargetOrgan(Character target) {
        return target.body.getRandom("ass");
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return fuckable(c, target) && c.getStance().mobile(getSelf())
                        && (c.getStance().behind(getSelf())
                                        || (c.getStance().prone(target) && !c.getStance().mobile(target)))
                        && getSelf().canAct()
                        && (getTargetOrgan(target).isReady(target) || getSelf().has(Item.Lubricant)
                                        || getSelf().getArousal().percent() > 50 || getSelf().has(Trait.alwaysready)
                                        || getSelf().has(Trait.assmaster))
                        && (!target.hasPussy() || !PullOut.blockedByAddiction(getSelf()));
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        String premessage = premessage(c, target);
        if (!target.hasStatus(Stsflag.oiled) && getSelf().getArousal().percent() > 50
                        || getSelf().has(Trait.alwaysready) || getSelf().has(Trait.assmaster)) {
            String fluids = target.hasDick() ? "copious pre-cum" : "own juices";
            if (premessage.isEmpty()) {
                premessage = "{self:subject-action:lube|lubes}";
            } else {
                premessage += "{self:action:lube|lubes}";
            }
            premessage += " up {other:possessive} ass with {self:possessive} " + fluids + ".";
            target.add(c, new Oiled(target));
        } else if (!target.hasStatus(Stsflag.oiled) && getSelf().has(Item.Lubricant)) {
            if (premessage.isEmpty()) {
                premessage = "{self:subject-action:lube|lubes}";
            } else {
                premessage += "{self:action:lube|lubes}";
            }
            premessage += " up {other:possessive} ass.";
            target.add(c, new Oiled(target));
            getSelf().consume(Item.Lubricant, 1);
        }
        c.write(getSelf(), Global.format(premessage, getSelf(), target));

        int m = Global.random(10, 15);
        if (getSelf().has(Trait.strapped) && getSelf().has(Item.Strapon2)) {
            m += 3;
        }
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, premessage.length(), Result.normal, target));
        } else if (target.human()) {
            if (!c.getStance().behind(getSelf()) && getSelf().has(Trait.strapped)) {
                c.write(getSelf(), receive(c, premessage.length(), Result.upgrade, target));
            } else if (getSelf().getType().equals("Eve") && c.getStance().behind(getSelf())) {
                m += 5;
                c.write(getSelf(), receive(c, premessage.length(), Result.special, target));
            } else {
                c.write(getSelf(), receive(c, premessage.length(), Result.normal, target));
            }
        } else if (c.isBeingObserved()) {
            if (!c.getStance().behind(getSelf()) && getSelf().has(Trait.strapped)) {
                c.write(getSelf(), receive(c, premessage.length(), Result.upgrade, target));
            } else {
                c.write(getSelf(), receive(c, premessage.length(), Result.normal, target));
            }
        }
        
        boolean voluntary = getSelf().canMakeOwnDecision();
        if (c.getStance().behind(getSelf())) {
            if (getSelf().getType().equals("Eve")) {
                c.setStance(new AnalProne(getSelf(), target), getSelf(), voluntary);
            } else {
                c.setStance(new Anal(getSelf(), target), getSelf(), voluntary);
            }
        } else {
            c.setStance(new AnalProne(getSelf(), target), getSelf(), voluntary);
        }
        int otherm = m;
        if (getSelf().has(Trait.insertion)) {
            otherm += Math.min(getSelf().get(Attribute.Seduction) / 4, 40);
        }
        target.body.pleasure(getSelf(), getSelfOrgan(), getTargetOrgan(target), otherm, c, this);
        if (!getSelf().has(Trait.strapped)) {
            getSelf().body.pleasure(target, getTargetOrgan(target), getSelfOrgan(), m / 2, c, this);
        }
        getSelf().emote(Emotion.dominant, 100);
        target.emote(Emotion.desperate, 50);
        if (!target.has(Trait.Unflappable)) {
            target.add(c, new Flatfooted(target, 1));
        }
        if (getSelf().has(Trait.analFanatic) && getSelf().hasDick()) {
            c.write(getSelf(),
                            String.format("Now with %s %s deeply embedded within %s ass,"
                                            + " %s mind clears itself of everything but fucking %s as hard as possible.",
                            getSelf().possessivePronoun(), getSelf().body.getRandomCock().describe(getSelf()),
                            target.nameOrPossessivePronoun(), getSelf().nameOrPossessivePronoun(),
                            target.directObject()));
            getSelf().add(c, new Frenzied(getSelf(), 4));
            getSelf().add(c, new IgnoreOrgasm(getSelf(), 4));
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Seduction) >= 15;
    }

    @Override
    public Skill copy(Character user) {
        return new AssFuck(user);
    }

    @Override
    public int speed() {
        return 2;
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.fucking;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.normal) {
            return String.format(
                            (damage == 0 ? "You" : "After you")
                                            + " make sure %s ass is sufficiently lubricated, you push your %s into %s %s.",
                            target.nameOrPossessivePronoun(), getSelfOrgan().describe(getSelf()),
                            target.possessivePronoun(), getTargetOrgan(target).describe(target));
        } else {
            return target.name() + "'s ass is oiled up and ready to go, but you're still too soft to penetrate "+target.directObject()+".";
        }
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        if (modifier == Result.upgrade) {
            return String.format("%s %s legs apart and teasingly pokes the strap-on against %s anus."
                            + " %s %s to struggle away, but %s %s %s hips closer and slowly pushes the dildo"
                            + " inside %s ass.", getSelf().subjectAction("spread"), target.nameOrPossessivePronoun(),
                            target.possessivePronoun(),
                            Global.capitalizeFirstLetter(target.pronoun()), target.action("try", "tries"),
                            getSelf().subject(), getSelf().action("pull"), target.possessivePronoun(), 
                            target.possessivePronoun());
        }
        if (modifier == Result.normal) {
            if (getSelf().has(Trait.strapped)) {
                if (getSelf().has(Item.Strapon2)) {
                    return String.format("%s %s strap-on behind %s and pushes it into %s lubricated ass. After pushing it"
                                    + " in completely, %s pushes a button on a controller which causes the dildo to vibrate in"
                                    + " %s ass, giving %s a slight shiver.", getSelf().subjectAction("align"), getSelf().possessivePronoun(),
                                    target.nameDirectObject(), target.possessivePronoun(), getSelf().pronoun(), target.possessivePronoun(),
                                    target.directObject());
                } else {
                    return String.format("%s lubes up %s strap-on, positions %s behind %s, and shoves it into %s ass.", 
                                    getSelf().name(), getSelf().possessivePronoun(), getSelf().reflectivePronoun(), 
                                    target.nameDirectObject(), target.possessivePronoun());
                }
            } else {
                return String.format("%s rubs %s cock up and down %s ass before thrusting %s hips to penetrate %s.",
                                getSelf().name(), getSelf().possessivePronoun(), target.nameOrPossessivePronoun(),
                                getSelf().possessivePronoun(), target.directObject());
            }
        } else if (modifier == Result.special) {
            // Eve
            return String.format(
                            "While maintaining a firm grip, Eve runs her hands down your sides. <i>\"Are you ready for"
                                            + " me now, %s? Actually, I don't care if you are. It's not like you can stop me now.\"</i> There's only"
                                            + " one thing that could mean, and you don't want any part of it. You struggle in Eve's arms, trying to"
                                            + " get away as she is dryhumping your ass, getting it wet for her. Finally, you manage to stumble away,"
                                            + " but Eve trips you before you regain your balance. She follows you to the ground, rolling you onto your"
                                            + " back and lifting your legs. <i>\"Uh uh, you're not going anywhere, my little cumslut-to-be. Now just lay back and"
                                            + " take it.\"</i> Keeping your legs up with one arm, she uses the other to line up her %s with your hole. Then,"
                                            + " she brutually slams it all the way in in one go. Your screams and Eve's laughter fill the air as she starts"
                                            + " fucking you at a furious pace.",
                            target.name(), getSelf().body.getRandomCock().describe(getSelf()));
        } else {
            return String.format("%s rubs %s dick against %s ass, but it's still flaccid and can't actually penetrate %s.",
                            getSelf().name(), getSelf().possessivePronoun(), target.nameOrPossessivePronoun(), target.directObject());
        }
    }

    @Override
    public String describe(Combat c) {
        return "Penetrate your opponent's ass.";
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
