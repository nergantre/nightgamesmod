package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.WingsPart;
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
        return user.get(Attribute.Slime) >= 10;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond() && !getSelf().is(Stsflag.mimicry) && Global.getNPC("Angel").has(Trait.divinity);
    }

    @Override
    public String describe(Combat c) {
        return "Mimics an angel's powers";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
        } else if (target.human()) {
            c.write(getSelf(), receive(c, 0, Result.normal, target));
        }
        getSelf().addTemporaryTrait(Trait.divinity, 10);
        getSelf().addTemporaryTrait(Trait.objectOfWorship, 10);
        getSelf().addTemporaryTrait(Trait.erophage, 10);
        getSelf().addTemporaryTrait(Trait.revered, 10);
        getSelf().body.temporaryAddOrReplacePartWithType(WingsPart.angelicslime, 10);
        BreastsPart part = getSelf().body.getBreastsBelow(BreastsPart.h.size);
        if (part != null) {
            getSelf().body.temporaryAddOrReplacePartWithType(part.upgrade().upgrade(), 10);
        }
        getSelf().add(c, new Abuff(getSelf(), Attribute.Divinity, Math.max(10, getSelf().get(Attribute.Slime)), 10));
        getSelf().add(c, new SlimeMimicry("angel", PussyPart.divine, CockMod.blessed, getSelf(), 10));
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
        return "You shift your slime and start mimicking Cassie's witch form.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return Global.format("{self:NAME-POSSESSIVE} amorphous body jiggles violently and she shrinks her body into a sphere. "
                        + "You cautiously approach the unknown object, but hesistate when you see it suddenly turns pure white "
                        + "as if someone dumped a bucket of bleach on it. "
                        + "The sphere unwraps itself in layers, with each layer forming a pair of pristine translucent gelatinous feathered wings. "
                        + "{self:NAME} {self:reflective} stands up in the center, giving you a haughty look. "
                        + "Looks like {self:NAME} is mimicking Angel's er... angel form!.", getSelf(), target);
    }

}
