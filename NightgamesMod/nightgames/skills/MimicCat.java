package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.EarPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TailPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Flag;
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
        return user.get(Attribute.Slime) >= 10;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && !getSelf().is(Stsflag.mimicry) && Global.checkFlag(Flag.Kat);
    }

    @Override
    public String describe(Combat c) {
        return "Mimics a werecat";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
        } else if (target.human()) {
            if (!target.is(Stsflag.blinded))
                c.write(getSelf(), receive(c, 0, Result.normal, target));
            else 
                printBlinded(c);
        }
        getSelf().addTemporaryTrait(Trait.augmentedPheromones, 10);
        getSelf().addTemporaryTrait(Trait.nymphomania, 10);
        getSelf().addTemporaryTrait(Trait.lacedjuices, 10);
        getSelf().addTemporaryTrait(Trait.catstongue, 10);
        getSelf().body.temporaryAddOrReplacePartWithType(TailPart.slimeycat, 10);
        getSelf().body.temporaryAddOrReplacePartWithType(EarPart.cat, 10);
        BreastsPart part = getSelf().body.getBreastsAbove(BreastsPart.a.size);
        if (part != null) {
            getSelf().body.temporaryAddOrReplacePartWithType(part.downgrade(), 10);
        }
        getSelf().add(c, new Abuff(getSelf(), Attribute.Animism, Math.max(10, getSelf().get(Attribute.Slime)), 10));
        getSelf().add(c, new SlimeMimicry("werecat", PussyPart.feral, CockMod.primal, getSelf(), 10));
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
        return Global.format("{self:NAME-POSSESSIVE} amorphous body abruptly shifts as you're facing {self:direct-object}. "
                        + "Not sure what {self:pronoun} is doing, you cautiously approach. Suddenly, {self:possessive} slime solidifies again, "
                        + "and a orange shadow pounces at you from where {self:pronoun} was before. You manage to dodge it, but looking back at "
                        + "the formerly-crystal blue slime girl, you see that {self:NAME} has transformed into a caricature of Kat's feral form, "
                        + "complete with faux cat ears and a slimey tail!", getSelf(), target);
    }

}
