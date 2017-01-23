package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.StraponPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.nskills.tags.SkillTag;

public class Fuck extends Skill {

    public Fuck(String name, Character self, int cooldown) {
        super(name, self, cooldown);
        addTag(SkillTag.pleasure);
        addTag(SkillTag.fucking);
        addTag(SkillTag.petDisallowed);
    }

    public Fuck(Character self) {
        this("Fuck", self, 0);
    }

    public BodyPart getSelfOrgan() {
        BodyPart res = getSelf().body.getRandomCock();
        if (res == null && getSelf().has(Trait.strapped)) {
            res = StraponPart.generic;
        }
        return res;
    }

    public BodyPart getTargetOrgan(Character target) {
        return target.body.getRandomPussy();
    }

    public boolean fuckable(Combat c, Character target) {
        BodyPart selfO = getSelfOrgan();
        BodyPart targetO = getTargetOrgan(target);
        boolean possible = selfO != null && targetO != null;
        boolean ready = possible && selfO.isReady(getSelf());
        boolean stancePossible = false;
        if (ready) {
            stancePossible = true;
            if (selfO.isType("cock")) {
                stancePossible &= !c.getStance().inserted(getSelf());
            }
            if (selfO.isType("pussy")) {
                stancePossible &= !c.getStance().vaginallyPenetrated(c, getSelf());
            }
            if (selfO.isType("ass")) {
                stancePossible &= !c.getStance().anallyPenetrated(c, getSelf());
            }
            if (targetO.isType("cock")) {
                stancePossible &= !c.getStance().inserted(target);
            }
            if (targetO.isType("pussy")) {
                stancePossible &= !c.getStance().vaginallyPenetrated(c, target);
            }
            if (targetO.isType("ass")) {
                stancePossible &= !c.getStance().anallyPenetrated(c, target);
            }
        }
        stancePossible &= !c.getStance().havingSex(c) && !c.getStance().isFaceSitting(getSelf());
        return possible && ready && stancePossible && getSelf().clothingFuckable(selfO) && canGetToCrotch(target);
    }

    private boolean canGetToCrotch(Character target) {
        if (target.crotchAvailable())
            return true;
        if (!getSelfOrgan().moddedPartCountsAs(getSelf(), CockMod.slimy))
            return false;
        return target.outfit.getTopOfSlot(ClothingSlot.bottom).getLayer() == 0;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return fuckable(c, target)
                        && (c.getStance().insert(c, getSelf(), getSelf()) != c.getStance()
                                        || c.getStance().insert(c, target, getSelf()) != c.getStance())
                        && c.getStance().mobile(getSelf()) && !c.getStance().mobile(target) && getSelf().canAct();
    }

    protected String premessage(Combat c, Character target) {
        String premessage = "";
        Clothing underwear = getSelf().getOutfit().getSlotAt(ClothingSlot.bottom, 0);
        Clothing bottom = getSelf().getOutfit().getSlotAt(ClothingSlot.bottom, 1);
        String bottomMessage;

        if (underwear != null && bottom != null) {
            bottomMessage = underwear.getName() + " and " + bottom.getName();
        } else if (underwear != null) {
            bottomMessage = underwear.getName();
        } else if (bottom != null) {
            bottomMessage = bottom.getName();
        } else {
            bottomMessage = "";
        }

        if (!bottomMessage.isEmpty() && getSelfOrgan().isType("cock")) {
            premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} down {self:possessive} %s halfway and ",
                            bottomMessage);
        } else if (!bottomMessage.isEmpty() && getSelfOrgan().isType("pussy")) {
            premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} {self:possessive} %s to the side and ",
                            bottomMessage);
        }

        if (!target.crotchAvailable() && getSelfOrgan().moddedPartCountsAs(getSelf(), CockMod.slimy)) {
            Clothing destroyed = target.strip(ClothingSlot.bottom, c);
            assert target.outfit.slotEmpty(ClothingSlot.bottom);
            String start;
            if (premessage.isEmpty()) {
                start = "{self:SUBJECT-ACTION:place|places}";
            } else {
                start = "{self:action:place|places} ";
            }
            premessage += start + " the head of {self:possessive} {self:body-part:cock}"
                            + " against {other:name-possessive} " + destroyed.getName() + ". The corrosive slime burns"
                            + " right through them, but leaves the skin beneath untouched. Then, ";
        }

        return Global.format(premessage, getSelf(), target);
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        String premessage = premessage(c, target);
        int m = Global.random(10, 15);
        BodyPart selfO = getSelfOrgan();
        BodyPart targetO = getTargetOrgan(target);
        if (selfO.isReady(getSelf()) && targetO.isReady(target)) {
            if (targetO.isType("pussy") && target.has(Trait.temptingass) && new AssFuck(getSelf()).usable(c, target)
                && Global.random(3) == 1) {
                
                c.write(getSelf(), Global.format("%s{self:subject-action:line|lines}"
                                + " {self:possessive} {self:body-part:cock} up with {other:name-possessive}"
                                + " {other:body-part:pussy}. At the last moment before thrusting in, however,"
                                + " {self:pronoun-action:shift|shifts} to the tantalizing hole next door,"
                                + " and {self:action:sink|sinks} the hard rod into {other:name-possessive}"
                                + " hot ass instead.<br/>", getSelf(), target, premessage));
                new AssFuck(getSelf()).resolve(c, target);
                
                return true;
            }
            if (getSelf().human()) {
                c.write(getSelf(), premessage + deal(c, premessage.length(), Result.normal, target));
            } else if (c.shouldPrintReceive(target, c)) {
                c.write(getSelf(), premessage + receive(c, premessage.length(), Result.normal, target));
            }
            if (selfO.isType("pussy")) {
                c.setStance(c.getStance().insert(c, target, getSelf()), getSelf(), getSelf().canMakeOwnDecision());
            } else {
                c.setStance(c.getStance().insert(c, getSelf(), getSelf()), getSelf(), getSelf().canMakeOwnDecision());
            }
            int otherm = m;
            if (getSelf().has(Trait.insertion)) {
                otherm += Math.min(getSelf().get(Attribute.Seduction) / 4, 40);
            }
            target.body.pleasure(getSelf(), selfO, targetO, otherm, c, this);
            getSelf().body.pleasure(target, targetO, selfO, m, c, this);
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), premessage + deal(c, premessage.length(), Result.miss, target));
            } else if (c.shouldPrintReceive(target, c)) {
                c.write(getSelf(), premessage + receive(c, premessage.length(), Result.miss, target));
            }
            target.body.pleasure(getSelf(), selfO, targetO, 5, c, this);
            getSelf().body.pleasure(target, targetO, selfO, 5, c, this);
            return false;
        }
        return true;
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new Fuck(user);
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
        BodyPart selfO = getSelfOrgan();
        BodyPart targetO = getTargetOrgan(target);
        if (modifier == Result.normal) {
            return "you rub the head of your " + selfO.describe(getSelf()) + " around " + target.getName()
                            + "'s entrance, causing "+target.directObject()+" to shiver with anticipation. Once you're sufficiently lubricated "
                            + "with "+target.possessiveAdjective()+" wetness, you thrust into "+target.possessiveAdjective()+" " + targetO.describe(target)
                            + ". " + target.getName()
                            + " tries to stifle "+target.possessiveAdjective()+" pleasured moan as you fill "+target.possessiveAdjective()+" in an instant.";
        } else if (modifier == Result.miss) {
            if (!selfO.isReady(getSelf()) && !targetO.isReady(target)) {
                return "you're in a good position to fuck " + target.getName()
                                + ", but neither of you are aroused enough to follow through.";
            } else if (!targetO.isReady(target)) {
                return "you position your " + selfO.describe(getSelf()) + " at the entrance to " + target.getName()
                                + ", but find that "+target.pronoun()+"'s not nearly wet enough to allow a comfortable insertion. You'll need "
                                + "to arouse "+target.directObject()+" more or you'll risk hurting "+target.directObject()+".";
            } else if (!selfO.isReady(getSelf())) {
                return "you're ready and willing to claim " + target.getName() + "'s eager "
                                + targetO.describe(target) + ", but your shriveled "
                                + selfO.describe(getSelf())
                                + " isn't cooperating. Maybe your self-control training has become too effective.";
            }
            return "you managed to miss the mark.";
        }
        return "Bad stuff happened";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        BodyPart selfO = getSelfOrgan();
        BodyPart targetO = getTargetOrgan(target);
        if (modifier == Result.normal) {
            return String.format("%s rubs %s %s against %s wet snatch. " +
                                 "%s slowly but steadily pushes in, forcing %s length into %s hot, wet pussy.", 
                            getSelf().getName(), getSelf().possessiveAdjective(), selfO.describe(getSelf()), 
                            target.nameOrPossessivePronoun(),
                            Global.capitalizeFirstLetter(getSelf().pronoun()), getSelf().possessiveAdjective(),
                            target.possessiveAdjective());
        } else if (modifier == Result.miss) {
            String subject = (damage == 0 ? getSelf().getName() + " " : "");
            if (!selfO.isReady(getSelf()) || !targetO.isReady(target)) {
                String indicative = target.human() ? "yours" : target.nameOrPossessivePronoun();
                return String.format("%sgrinds %s privates against %ss, but since neither of %s are"
                                + " very turned on yet, it doesn't accomplish much.",
                                subject, getSelf().possessiveAdjective(), indicative,
                                c.bothDirectObject(target));
            } else if (!targetO.isReady(target)) {
                return String.format("%stries to push %s %s inside %s pussy, but %s %s not wet enough. "
                                + "%s simply not horny enough for effective penetration yet.",
                                subject, getSelf().possessiveAdjective(), selfO.describe(getSelf()),
                                target.nameOrPossessivePronoun(), target.pronoun(),
                                target.action("are", "is"),
                                Global.capitalizeFirstLetter(target.subjectAction("are", "is")));
            } else {
                return String.format("%stries to push %s %s into %s ready pussy, but %s is still limp.",
                                subject, getSelf().possessiveAdjective(), selfO.describe(getSelf()),
                                target.nameOrPossessivePronoun(), getSelf().pronoun());
            }
        }
        return "Bad stuff happened";
    }

    @Override
    public String describe(Combat c) {
        return "Penetrate your opponent, switching to a sex position";
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
