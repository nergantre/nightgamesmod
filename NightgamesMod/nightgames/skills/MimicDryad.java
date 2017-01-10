package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.EarPart;
import nightgames.characters.body.PussyPart;
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
        return getSelf().canRespond() && !getSelf().is(Stsflag.mimicry);
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
        getSelf().add(c, new Abuff(getSelf(), Attribute.Bio, Math.max(10, getSelf().get(Attribute.Slime)), 10));
        getSelf().add(c, new SlimeMimicry("dryad", PussyPart.plant, CockMod.error, getSelf(), 10));
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
