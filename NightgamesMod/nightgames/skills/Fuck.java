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

public class Fuck extends Skill {

    public Fuck(String name, Character self, int cooldown) {
        super(name, self, cooldown);
    }

    public Fuck(Character self) {
        super("Fuck", self);
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
                stancePossible &= !c.getStance().vaginallyPenetrated(getSelf());
            }
            if (selfO.isType("ass")) {
                stancePossible &= !c.getStance().anallyPenetrated(getSelf());
            }
            if (targetO.isType("cock")) {
                stancePossible &= !c.getStance().inserted(target);
            }
            if (targetO.isType("pussy")) {
                stancePossible &= !c.getStance().vaginallyPenetrated(target);
            }
            if (targetO.isType("ass")) {
                stancePossible &= !c.getStance().anallyPenetrated(target);
            }
        }
        stancePossible &= !c.getStance().havingSex();
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
                        && (c.getStance().insert(getSelf(), getSelf()) != c.getStance()
                                        || c.getStance().insert(target, getSelf()) != c.getStance())
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

        if (!target.crotchAvailable() && getSelfOrgan().getMod(getSelf()).equals(CockMod.slimy)) {
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
        int m = 5 + Global.random(5);
        BodyPart selfO = getSelfOrgan();
        BodyPart targetO = getTargetOrgan(target);
        if (selfO.isReady(getSelf()) && targetO.isReady(target)) {
            if (getSelf().human()) {
                c.write(getSelf(), premessage + deal(c, premessage.length(), Result.normal, target));
            } else if (target.human()) {
                c.write(getSelf(), premessage + receive(c, premessage.length(), Result.normal, target));
            }
            if (selfO.isType("pussy")) {
                c.setStance(c.getStance().insert(target, getSelf()), getSelf(), getSelf().canMakeOwnDecision());
            } else {
                c.setStance(c.getStance().insert(getSelf(), getSelf()), getSelf(), getSelf().canMakeOwnDecision());
            }
            int otherm = m;
            if (getSelf().has(Trait.insertion)) {
                otherm += Math.min(getSelf().get(Attribute.Seduction) / 4, 40);
            }
            target.body.pleasure(getSelf(), selfO, targetO, m, c, this);
            getSelf().body.pleasure(target, targetO, selfO, otherm, c, this);
        } else {
            if (getSelf().human()) {
                c.write(getSelf(), premessage + deal(c, premessage.length(), Result.miss, target));
            } else if (target.human()) {
                c.write(getSelf(), premessage + receive(c, premessage.length(), Result.miss, target));
            }
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
            return "you rub the head of your " + selfO.describe(getSelf()) + " around " + target.name()
                            + "'s entrance, causing her to shiver with anticipation. Once you're sufficiently lubricated "
                            + "with her wetness, you thrust into her " + target.body.getRandomPussy().describe(target)
                            + ". " + target.name()
                            + " tries to stifle her pleasured moan as you fill her in an instant.";
        } else if (modifier == Result.miss) {
            if (!selfO.isReady(getSelf()) && !targetO.isReady(target)) {
                return "you're in a good position to fuck " + target.name()
                                + ", but neither of you are aroused enough to follow through.";
            } else if (!getTargetOrgan(target).isReady(target)) {
                return "you position your " + selfO.describe(getSelf()) + " at the entrance to " + target.name()
                                + ", but find that she's not nearly wet enough to allow a comfortable insertion. You'll need "
                                + "to arouse her more or you'll risk hurting her.";
            } else if (!selfO.isReady(getSelf())) {
                return "you're ready and willing to claim " + target.name() + "'s eager "
                                + target.body.getRandomPussy().describe(target) + ", but your shriveled "
                                + selfO.describe(getSelf())
                                + " isn't cooperating. Maybe your self-control training has become " + "too effective.";
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
            String message = getSelf().name() + " rubs her " + selfO.describe(getSelf())
                            + " against your wet snatch. She slowly but steadily pushes in, forcing "
                            + "her length into your hot, wet pussy.";
            return message;
        } else if (modifier == Result.miss) {
            if (!selfO.isReady(getSelf()) || !targetO.isReady(target)) {
                return (damage == 0 ? getSelf().name() + " " : "")
                                + "grinds her privates against yours, but since neither of you are very turned on yet, it doesn't accomplish much.";
            } else if (!targetO.isReady(target)) {
                return (damage == 0 ? getSelf().name() + " " : "") + "tries to push her " + selfO.describe(getSelf())
                                + " inside your pussy, but you're not wet enough. You're simply not horny enough for "
                                + "effective penetration yet.";
            } else {
                return (damage == 0 ? getSelf().name() + " " : "") + "tries to push her " + selfO.describe(getSelf())
                                + " into your ready pussy, but she is still limp.";
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
