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

public class FairyMale extends Pet {   
    public FairyMale(Character owner) {
        this(owner, 2, 4);
    }

    public FairyMale(Character owner, int power, int ac) {
        super("faerie", owner, Ptype.fairymale, power, ac);
    }

    @Override
    public String describe() {
        return null;
    }

    @Override
    public void vanquish(Combat c, Pet opponent) {
        switch (opponent.type()) {
            case fairyfem:
                c.write(getSelf(), own() + "faerie boy chases " + opponent.own()
                                + "faerie and catches her from behind. He plays with the faerie girl's pussy and nipples while she's unable to retaliate. As she "
                                + "orgasms, she vanishes with a sparkle.");
                break;
            case fairymale:
                c.write(getSelf(), "");
                break;
            case impfem:
                c.write(getSelf(), own() + " faerie gets under " + opponent.own()
                                + "imp's guard and punches her squarely in her comparatively large clitoris. The imp shrieks in pain and collapses before "
                                + "vanishing.");
                break;
            case impmale:
                c.write(getSelf(), own() + " faerie gets under " + opponent.own()
                                + "imp's guard and punches him squarely in his comparatively large jewels. The imp shrieks in pain and collapses before "
                                + "vanishing.");                break;
            case slime:
                c.write(getSelf(), own() + " Glows as he surrounds himself with magic before charging at "
                                + opponent.own()
                                + "slime like a tiny missile. The slime splashes more than it explodes, it's pieces "
                                + "only shudder once before going still.");
                break;
            default:
                break;
        }
        c.removePet(getSelf());
    }

    @Override
    public void caught(Combat c, Character captor) {
        if (captor.human()) {
            c.write(captor, "You snag " + getSelf().getName() + " out of the air. He squirms in your hand, but has no chance of breaking free. You lick the fae from his chest to small prick and the little thing squeals "
                            + "in pleasure. The taste is surprisingly sweet and makes your tongue tingle. You continue lapping up the flavor until he climaxes and disappears.");
        } else {
            c.write(captor, Global.format("{other:SUBJECT} snatches {self:name-do} out of the air and flicks the fairy's little testicles with {other:possessive} finger. %s in sympathy as the tiny male curls up in the fetal position "
                            + "and vanishes.", getSelf(), captor, owner().subjectAction("wince", "winces")));
        }
        c.removePet(getSelf());
    }

    @Override
    protected void buildSelf() {
        PetCharacter self = new PetCharacter(this, owner().nameOrPossessivePronoun() + " " + getName(), getName(), new Growth(), power);
        // fairies are about 20 centimeters tall
        self.learn(new FairyEnergize(self));
        self.learn(new FairyHeal(self));
        self.learn(new FairyTease(self));
        self.learn(new FairyKick(self));
        self.learn(new FairyShield(self));
        self.body.setHeight(22);
        self.body.finishBody(CharacterSex.male);
        setSelf(self);
    }
}
