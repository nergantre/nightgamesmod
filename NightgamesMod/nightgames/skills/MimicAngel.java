package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.WingsPart;
import nightgames.characters.body.mods.DivineMod;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Abuff;
import nightgames.status.SlimeMimicry;
import nightgames.status.Stsflag;

public class MimicAngel extends Skill {

    public MimicAngel(Character self) {
        super("Mimicry: Angel", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.human() && user.get(Attribute.Slime) >= 10;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && !getSelf().is(Stsflag.mimicry) && Global.getNPC("Angel").has(Trait.demigoddess);
    }

    @Override
    public String describe(Combat c) {
        return "Mimics an angel's powers";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
        } else if (c.shouldPrintReceive(target, c)) {
            if (!target.is(Stsflag.blinded))
                c.write(getSelf(), receive(c, 0, Result.normal, target));
            else 
                printBlinded(c);
        }
        if (getSelf().has(Trait.ImitatedStrength)) {
            getSelf().addTemporaryTrait(Trait.divinity, 10);
            if (getSelf().getLevel() >= 20) {
                getSelf().addTemporaryTrait(Trait.objectOfWorship, 10);
            }
            if (getSelf().getLevel() >= 28) {
                getSelf().addTemporaryTrait(Trait.lastStand, 10);
            }
            if (getSelf().getLevel() >= 36) {
                getSelf().addTemporaryTrait(Trait.erophage, 10);
            }
            if (getSelf().getLevel() >= 44) {
                getSelf().addTemporaryTrait(Trait.sacrosanct, 10);
            }
            if (getSelf().getLevel() >= 52) {
                getSelf().addTemporaryTrait(Trait.genuflection, 10);
            }
            if (getSelf().getLevel() >= 60) {
                getSelf().addTemporaryTrait(Trait.revered, 10);
            }
        }
        getSelf().body.temporaryAddOrReplacePartWithType(WingsPart.angelicslime, 10);
        BreastsPart part = getSelf().body.getBreastsBelow(BreastsPart.h.size);
        if (part != null) {
            getSelf().body.temporaryAddOrReplacePartWithType(part.upgrade().upgrade(), 10);
        }
        int strength = Math.max(10, getSelf().get(Attribute.Slime)) * 2 / 3;
        if (getSelf().has(Trait.Masquerade)) {
            strength = strength * 3 / 2;
        }
        getSelf().add(c, new Abuff(getSelf(), Attribute.Divinity, strength, 10));
        getSelf().add(c, new SlimeMimicry("angel", getSelf(), 10));
        getSelf().body.temporaryAddPartMod("pussy", DivineMod.INSTANCE, 10);
        getSelf().body.temporaryAddPartMod("cock", CockMod.blessed, 10);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new MimicAngel(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You shift your slime and start mimicking Angel's... angel form.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return Global.format("{self:NAME-POSSESSIVE} amorphous body jiggles violently and she shrinks her body into a sphere. "
                        + "{other:SUBJECT} cautiously {other:action:approach|approaches} the unknown object, but hesistate when {other:pronoun-action:see|sees} it suddenly turns pure white "
                        + "as if someone dumped a bucket of bleach on it. "
                        + "The sphere unwraps itself in layers, with each layer forming a pair of pristine translucent gelatinous feathered wings. "
                        + "{self:NAME} {self:reflective} stands up in the center, giving {other:name-do} a haughty look. "
                        + "Looks like {self:NAME} is mimicking Angel's er... angel form!", getSelf(), target);
    }

}
