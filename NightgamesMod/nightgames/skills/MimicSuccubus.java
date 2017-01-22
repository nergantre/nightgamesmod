package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Reyka;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.EarPart;
import nightgames.characters.body.TailPart;
import nightgames.characters.body.WingsPart;
import nightgames.characters.body.mods.DemonicMod;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Abuff;
import nightgames.status.SlimeMimicry;
import nightgames.status.Stsflag;

public class MimicSuccubus extends Skill {

    public MimicSuccubus(Character self) {
        super("Mimicry: Succubus", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.human() && user.get(Attribute.Slime) >= 10;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && !getSelf().is(Stsflag.mimicry) && Global.characterTypeInGame(Reyka.class.getSimpleName());
    }

    @Override
    public String describe(Combat c) {
        return "Mimics a succubus's abilities";
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
            getSelf().addTemporaryTrait(Trait.succubus, 10);
            getSelf().addTemporaryTrait(Trait.energydrain, 10);
            if (getSelf().getLevel() >= 20) {
                getSelf().addTemporaryTrait(Trait.spiritphage, 10);
            }
            if (getSelf().getLevel() >= 28) {
                getSelf().addTemporaryTrait(Trait.lacedjuices, 10);
            }
            if (getSelf().getLevel() >= 36) {
                getSelf().addTemporaryTrait(Trait.RawSexuality, 10);
            }
            if (getSelf().getLevel() >= 44) {
                getSelf().addTemporaryTrait(Trait.soulsucker, 10);
            }
            if (getSelf().getLevel() >= 52) {
                getSelf().addTemporaryTrait(Trait.gluttony, 10);
            }
            if (getSelf().getLevel() >= 60) {
                getSelf().body.temporaryAddPartMod("ass", DemonicMod.INSTANCE, 10);
                getSelf().body.temporaryAddPartMod("hands", DemonicMod.INSTANCE, 10);
                getSelf().body.temporaryAddPartMod("feet", DemonicMod.INSTANCE, 10);
                getSelf().body.temporaryAddPartMod("mouth", DemonicMod.INSTANCE, 10);
            }
        }
        getSelf().addTemporaryTrait(Trait.succubus, 10);
        getSelf().addTemporaryTrait(Trait.soulsucker, 10);
        getSelf().addTemporaryTrait(Trait.energydrain, 10);
        getSelf().addTemporaryTrait(Trait.spiritphage, 10);
        getSelf().body.temporaryAddOrReplacePartWithType(WingsPart.demonicslime, 10);
        getSelf().body.temporaryAddOrReplacePartWithType(TailPart.demonicslime, 10);
        getSelf().body.temporaryAddOrReplacePartWithType(EarPart.pointed, 10);
        BreastsPart part = getSelf().body.getBreastsBelow(BreastsPart.h.getSize());
        if (part != null) {
            getSelf().body.temporaryAddOrReplacePartWithType(part.upgrade().upgrade(), 10);
        }

        int strength = Math.max(10, getSelf().get(Attribute.Slime)) * 2 / 3;
        if (getSelf().has(Trait.Masquerade)) {
            strength = strength * 3 / 2;
        }
        getSelf().add(c, new Abuff(getSelf(), Attribute.Dark, strength, 10));
        getSelf().add(c, new SlimeMimicry("succubus", getSelf(), 10));
        getSelf().body.temporaryAddPartMod("pussy", DemonicMod.INSTANCE, 10);
        getSelf().body.temporaryAddPartMod("cock", CockMod.incubus, 10);

        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new MimicSuccubus(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You shift your slime into a demonic form.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return Global.format("{self:NAME-POSSESSIVE} mercurial form seems to suddenly expand, then collapse onto itself. "
                        + "Her crystal blue goo glimmers and shifts into a deep obsidian. After reforming her features out of "
                        + "her eratically flowing slime, {other:subject-action:see|sees} that she has taken on an appearance reminiscent of Reyka's succubus form, "
                        + "complete with large translucent gel wings, a thick tail and her characteristic laviscious grin.", getSelf(), target);
    }

}
