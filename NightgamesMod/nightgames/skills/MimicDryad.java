package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.EarPart;
import nightgames.characters.body.mods.PlantMod;
import nightgames.characters.body.mods.TentacledMod;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Abuff;
import nightgames.status.SlimeMimicry;
import nightgames.status.Stsflag;

public class MimicDryad extends Skill {
    public MimicDryad(Character self) {
        super("Mimicry: Dryad", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.human() && user.get(Attribute.Slime) >= 10;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && !getSelf().is(Stsflag.mimicry) && Global.getParticipants().stream().anyMatch(character -> character.has(Trait.dryad));
    }

    @Override
    public String describe(Combat c) {
        return "Mimics a dryad's abilities";
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
            getSelf().addTemporaryTrait(Trait.dryad, 10);
            if (getSelf().getLevel() >= 20) {
                getSelf().addTemporaryTrait(Trait.magicEyeFrenzy, 10);
            }
            if (getSelf().getLevel() >= 28) {
                getSelf().addTemporaryTrait(Trait.lacedjuices, 10);
            }
            if (getSelf().getLevel() >= 36) {
                getSelf().addTemporaryTrait(Trait.RawSexuality, 10);
            }
            if (getSelf().getLevel() >= 44) {
                getSelf().addTemporaryTrait(Trait.temptingtits, 10);
            }
            if (getSelf().getLevel() >= 52) {
                getSelf().addTemporaryTrait(Trait.addictivefluids, 10);
            }
            if (getSelf().getLevel() >= 60) {
                getSelf().body.temporaryAddPartMod("pussy", TentacledMod.INSTANCE, 10);
            }
        }
        getSelf().addTemporaryTrait(Trait.dryad, 10);
        getSelf().addTemporaryTrait(Trait.magicEyeFrenzy, 10);
        getSelf().addTemporaryTrait(Trait.frenzyingjuices, 10);
        getSelf().addTemporaryTrait(Trait.RawSexuality, 10);
        getSelf().addTemporaryTrait(Trait.temptingtits, 10);
        getSelf().body.temporaryAddOrReplacePartWithType(EarPart.pointed, 10);
        BreastsPart part = getSelf().body.getBreastsBelow(BreastsPart.h.size);
        if (part != null) {
            getSelf().body.temporaryAddOrReplacePartWithType(part.upgrade(), 10);
        }

        int strength = Math.max(10, getSelf().get(Attribute.Slime)) * 2 / 3;
        if (getSelf().has(Trait.Masquerade)) {
            strength = strength * 3 / 2;
        }
        getSelf().add(c, new Abuff(getSelf(), Attribute.Bio, strength, 10));
        getSelf().add(c, new SlimeMimicry("dryad", getSelf(), 10));
        getSelf().body.temporaryAddPartMod("pussy", PlantMod.INSTANCE, 10);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new MimicDryad(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You shift your slime into a one mimicking a dryad.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return Global.format("{self:NAME-POSSESSIVE} amorphous body quivers and collapses into a puddle. "
                        + "Starting from the center, the slime matter dyes itself green, transforming itself into a verdant emerald hue within seconds. "
                        + "After reforming her features out of her slime, {other:subject-action:see|sees} that {self:NAME} has taken on an appearance reminiscent of Rosea the dryad, "
                        + "complete with a large slime-parody of a flower replacing where her usual vagina is.", getSelf(), target);
    }

}
