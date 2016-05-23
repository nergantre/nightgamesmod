package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.EarPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TailPart;
import nightgames.characters.body.WingsPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Flag;
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
        return user.get(Attribute.Slime) >= 10;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && !c.getStance().sub(getSelf()) && !getSelf().is(Stsflag.form) && Global.checkFlag(Flag.Reyka);
    }

    @Override
    public String describe(Combat c) {
        return "Mimics a succubus's abilities";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        if (getSelf().human()) {
            c.write(getSelf(), deal(c, 0, Result.normal, target));
        } else if (target.human()) {
            c.write(getSelf(), receive(c, 0, Result.normal, target));
        }
        getSelf().addTemporaryTrait(Trait.succubus, 10);
        getSelf().addTemporaryTrait(Trait.soulsucker, 10);
        getSelf().addTemporaryTrait(Trait.energydrain, 10);
        getSelf().addTemporaryTrait(Trait.spiritphage, 10);
        getSelf().body.temporaryAddOrReplacePartWithType(WingsPart.demonicslime, 10);
        getSelf().body.temporaryAddOrReplacePartWithType(TailPart.demonicslime, 10);
        getSelf().body.temporaryAddOrReplacePartWithType(EarPart.pointed, 10);
        BreastsPart part = getSelf().body.getBreastsBelow(BreastsPart.h.size);
        if (part != null) {
            getSelf().body.temporaryAddOrReplacePartWithType(part.upgrade().upgrade(), 10);
        }
        getSelf().add(c, new Abuff(getSelf(), Attribute.Dark, Math.max(10, getSelf().get(Attribute.Slime)), 10));
        getSelf().add(c, new SlimeMimicry("succubus", PussyPart.succubus, CockMod.incubus, getSelf(), 10));
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
                        + "her eratically flowing slime, you see that she has taken on an appearance reminiscent of Reyka's succubus form, "
                        + "complete with large translucent gel wings, a thick tail and her characteristic laviscious grin.", getSelf(), target);
    }

}
