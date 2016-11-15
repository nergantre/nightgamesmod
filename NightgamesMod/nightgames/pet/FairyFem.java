package nightgames.pet;

import nightgames.characters.Character;
import nightgames.characters.CharacterSex;
import nightgames.characters.Growth;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.petskills.FairyEnergize;
import nightgames.skills.petskills.FairyHeal;
import nightgames.skills.petskills.FairyKick;
import nightgames.skills.petskills.FairyShield;
import nightgames.skills.petskills.FairyTease;

public class FairyFem extends Pet {
    public FairyFem(Character owner) {
        this(owner, 2, 4);
    }

    public FairyFem(Character owner, int power, int ac) {
        super("faerie", owner, Ptype.fairyfem, power, ac);
    }

    @Override
    public String describe() {
        return null;
    }

    @Override
    public void vanquish(Combat c, Pet opponent) {
        switch (opponent.type()) {
            case fairyfem:
                c.write(getSelf(), "The two faeries circle around each other vying for the upper hand. " + own()
                                + "faerie catches " + opponent.own() + "faerie by the hips and starts to eat her out. "
                                + opponent.own()
                                + " fae struggles to break free, but can barely keep flying as she rapidly reaches orgasm and vanishes.");
                break;
            case fairymale:
                c.write(getSelf(), "The faeries zip through the air like a couple dogfighting planes. " + opponent.own()
                                + "male manages to catch the female's hands, but you see her foot shoot up decisively "
                                + "between his legs. The stricken male tumbles lazily to the floor and vanishes in midair.");
                break;
            case impfem:
                c.write(getSelf(), own() + " faerie flies between the legs of " + opponent.own()
                                + "imp, slipping both arms into the larger pussy. The imp trembles as falls to the floor as the faerie puts her entire "
                                + "upper body into pleasuring her. " + own()
                                + "faerie is forcefully expelled by the imp's orgasm just before the imp vanishes.");
                break;
            case impmale:
                c.write(getSelf(), opponent.own() + "imp grabs at " + own()
                                + "faerie, but the nimble sprite changes direct in midair and darts between the imp's legs. You can't see exactly what happens next, "
                                + "but the imp clutches his groin in pain and disappears.");
                break;
            case slime:
                c.write(getSelf(), own() + "fae glows with magic as it circles " + opponent.own()
                                + "slime rapidly. The slime begins to tremble and slowly elongates into the shape of a crude phallis. It shudders "
                                + "violently and sprays liquid from the tip until the entire creature is a puddle on the floor.");
                break;
            default:
                break;
        }
        c.removePet(getSelf());
    }

    @Override
    public void caught(Combat c, Character captor) {
        if (captor.human()) {
            c.write(captor, "You snag " + getSelf().getName() + " out of the air. She squirms in your hand, but has no chance of breaking free. You lick the fae from pussy to breasts and the little thing squeals "
                            + "in pleasure. The taste is surprisingly sweet and makes your tongue tingle. You continue lapping up the flavor until she climaxes and disappears.");
        } else {
            c.write(captor, Global.format("{other:SUBJECT-ACTION:manage|manages} to catch {self:name-do} and starts pleasuring her with the tip of {other:possessive} finger. The sensitive fae clings to the probing finger desperately as she thrashes "
                            + "in ecstasy. Before %s can do anything to help, {self:subject} vanishes in a burst of orgasmic magic.", getSelf(), captor, owner().subject()));
        }
        c.removePet(getSelf());
    }

    @Override
    protected void buildSelf() {
        Growth growth = new Growth();
        PetCharacter self = new PetCharacter(this, owner().nameOrPossessivePronoun() + " " + getName(), getName(), growth, power);
        // fairies are about 20 centimeters tall
        self.body.setHeight(20);
        self.body.finishBody(CharacterSex.female);
        self.learn(new FairyEnergize(self));
        self.learn(new FairyHeal(self));
        self.learn(new FairyTease(self));
        self.learn(new FairyKick(self));
        self.learn(new FairyShield(self));
        setSelf(self);
    }
}
