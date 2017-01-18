package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Kat;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.EarPart;
import nightgames.characters.body.TailPart;
import nightgames.characters.body.mods.FeralMod;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.status.Abuff;
import nightgames.status.SlimeMimicry;
import nightgames.status.Stsflag;

public class MimicCat extends Skill {

    public MimicCat(Character self) {
        super("Mimicry: Werecat", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.human() && user.get(Attribute.Slime) >= 10;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && !getSelf().is(Stsflag.mimicry) && Global.characterTypeInGame(Kat.class.getSimpleName());
    }

    @Override
    public String describe(Combat c) {
        return "Mimics a werecat";
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
            getSelf().addTemporaryTrait(Trait.pheromones, 10);
            if (getSelf().getLevel() >= 20) {
                getSelf().addTemporaryTrait(Trait.nymphomania, 10);
            }
            if (getSelf().getLevel() >= 28) {
                getSelf().addTemporaryTrait(Trait.catstongue, 10);
            }
            if (getSelf().getLevel() >= 36) {
                getSelf().addTemporaryTrait(Trait.FeralStrength, 10);
            }
            if (getSelf().getLevel() >= 44) {
                getSelf().addTemporaryTrait(Trait.BefuddlingFragrance, 10);
            }
            if (getSelf().getLevel() >= 52) {
                getSelf().addTemporaryTrait(Trait.Jackhammer, 10);
            }
            if (getSelf().getLevel() >= 60) {
                getSelf().addTemporaryTrait(Trait.Unsatisfied, 10);
            }
        }
        getSelf().addTemporaryTrait(Trait.augmentedPheromones, 10);
        getSelf().addTemporaryTrait(Trait.nymphomania, 10);
        getSelf().addTemporaryTrait(Trait.lacedjuices, 10);
        getSelf().addTemporaryTrait(Trait.catstongue, 10);
        getSelf().addTemporaryTrait(Trait.FrenzyScent, 10);
        getSelf().body.temporaryAddOrReplacePartWithType(TailPart.slimeycat, 10);
        getSelf().body.temporaryAddOrReplacePartWithType(EarPart.cat, 10);
        BreastsPart part = getSelf().body.getBreastsAbove(BreastsPart.a.size);
        if (part != null) {
            getSelf().body.temporaryAddOrReplacePartWithType(part.downgrade(), 10);
        }

        int strength = Math.max(10, getSelf().get(Attribute.Slime)) * 2 / 3;
        if (getSelf().has(Trait.Masquerade)) {
            strength = strength * 3 / 2;
        }
        getSelf().add(c, new Abuff(getSelf(), Attribute.Animism, strength, 10));
        getSelf().add(c, new SlimeMimicry("cat", getSelf(), 10));
        getSelf().body.temporaryAddPartMod("pussy", FeralMod.INSTANCE, 10);
        getSelf().body.temporaryAddPartMod("cock", CockMod.primal, 10);
        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new MimicCat(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You shift your slime and start mimicking Kat's werecat form.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return Global.format("{self:NAME-POSSESSIVE} amorphous body abruptly shifts as {other:subject-action:are|is} facing {self:direct-object}. "
                        + "Not sure what {self:pronoun} is doing, {other:subject} cautiously {other:action:approach|approaches}. Suddenly, {self:possessive} slime solidifies again, "
                        + "and a orange shadow pounces at {other:direct-object} from where {self:pronoun} was before. {other:SUBJECT-ACTION:manage|manages} to dodge it, but looking back at "
                        + "the formerly-crystal blue slime girl, {other:pronoun-action:see|sees} that {self:NAME} has transformed into a caricature of Kat's feral form, "
                        + "complete with faux cat ears and a slimey tail!", getSelf(), target);
    }

}
