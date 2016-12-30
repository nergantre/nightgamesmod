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

public class Fairy extends Pet {
    
    private Ptype gender;
    
    public Fairy(Character owner, Ptype gender) {
        this(owner, gender, 2, 4);
    }

    public Fairy(Character owner, Ptype gender, int power, int ac) {
        super("faerie", owner, gender, power, ac);
        this.gender=gender;
    }

    @Override
    public String describe() {
        return null;
    }

    @Override
    public void vanquish(Combat c, Pet opponent) {
        int switcher = 10 * this.gender.ordinal() + opponent.type().ordinal();//there are fewer than 10 pet types. 0,1,2 are fem,herm,male
        //unfortunately Java doesn't support dynamically evaluated switch constants, so it has to be if/else.
        if (switcher == 10 * Ptype.fairyfem.ordinal() + Ptype.fairyfem.ordinal()) {
            c.write(getSelf(), "The two faeries circle around each other vying for the upper hand. " + own()
                            + "faerie catches " + opponent.own() + "faerie by the hips and starts to eat her out. "
                            + opponent.own()
                            + " fae struggles to break free, but can barely keep flying as she rapidly reaches orgasm and vanishes.");
        } else if (switcher == 10 * Ptype.fairyfem.ordinal() + Ptype.fairymale.ordinal() 
                || switcher == 10 * Ptype.fairyherm.ordinal() + Ptype.fairymale.ordinal()) {
            c.write(getSelf(), "The faeries zip through the air like a couple dogfighting planes. " + opponent.own()
                            + "male manages to catch the female's hands, but you see her foot shoot up decisively "
                            + "between his legs. The stricken male tumbles lazily to the floor and vanishes in midair.");
        } else if (switcher == 10 * Ptype.fairyfem.ordinal() + Ptype.impfem.ordinal() 
                || switcher == 10 * Ptype.fairyherm.ordinal() + Ptype.impfem.ordinal()) {
            c.write(getSelf(), own() + " faerie flies between the legs of " + opponent.own()
                            + "imp, slipping both arms into the larger pussy. The imp trembles as falls to the floor as the faerie puts her entire "
                            + "upper body into pleasuring her. " + own()
                            + "faerie is forcefully expelled by the imp's orgasm just before the imp vanishes.");
        } else if (switcher == 10 * Ptype.fairyfem.ordinal() + Ptype.impmale.ordinal() 
                || switcher == 10 * Ptype.fairyherm.ordinal() + Ptype.impmale.ordinal()) {
            c.write(getSelf(), opponent.own() + "imp grabs at " + own()
                            + "faerie, but the nimble sprite changes direct in midair and darts between the imp's legs. You can't see exactly what happens next, "
                            + "but the imp clutches his groin in pain and disappears.");
        } else if (switcher == 10 * Ptype.fairyfem.ordinal() + Ptype.slime.ordinal() 
                || switcher == 10 * Ptype.fairyherm.ordinal() + Ptype.slime.ordinal()) {
            c.write(getSelf(), own() + "fae glows with magic as it circles " + opponent.own()
                            + "slime rapidly. The slime begins to tremble and slowly elongates into the shape of a crude phallis. It shudders "
                            + "violently and sprays liquid from the tip until the entire creature is a puddle on the floor.");
        } else if (switcher == 10 * Ptype.fairymale.ordinal() + Ptype.fairyfem.ordinal()
                || switcher == 10 * Ptype.fairymale.ordinal() + Ptype.fairyherm.ordinal()) {
            c.write(getSelf(), own() + "faerie boy chases " + opponent.own()
                            + "faerie and catches her from behind. He plays with the faerie girl's pussy and nipples while she's unable to retaliate. As she "
                            + "orgasms, she vanishes with a sparkle.");
        } else if (switcher == 10 * Ptype.fairyherm.ordinal() + Ptype.fairyherm.ordinal()
                || switcher == 10 * Ptype.fairyfem.ordinal() + Ptype.fairyherm.ordinal()) {
                                c.write(getSelf(), own() + "faerie chases " + opponent.own()
                                + "faerie and catches her from behind. He plays with the faerie's pussy, dick, and nipples while she's unable to retaliate. As she "
                                + "orgasms, she vanishes with a sparkle.");
        } else if (switcher == 10 * Ptype.fairymale.ordinal() + Ptype.fairymale.ordinal()) {
            c.write(getSelf(), "");
        } else if (switcher == 10 * Ptype.fairymale.ordinal() + Ptype.impfem.ordinal()) {
            c.write(getSelf(), own() + " faerie gets under " + opponent.own()
                            + "imp's guard and punches her squarely in her comparatively large clitoris. The imp shrieks in pain and collapses before "
                            + "vanishing.");
        } else if (switcher == 10 * Ptype.fairymale.ordinal() + Ptype.impmale.ordinal()) {
            c.write(getSelf(), own() + " faerie gets under " + opponent.own()
                            + "imp's guard and punches him squarely in his comparatively large jewels. The imp shrieks in pain and collapses before "
                            + "vanishing.");
        } else if (switcher == 10 * Ptype.fairymale.ordinal() + Ptype.slime.ordinal()) {
            c.write(getSelf(), own() + " Glows as he surrounds himself with magic before charging at "
                            + opponent.own()
                            + "slime like a tiny missile. The slime splashes more than it explodes, it's pieces "
                            + "only shudder once before going still.");
        } else {
            (new FairyTease(getSelf())).resolve(c, opponent.getSelf());
            return;
        }
        c.removePet(opponent.getSelf());
    }

    @Override
    public void caught(Combat c, Character captor) {
        if (captor.human() && (gender == Ptype.fairyfem || gender == Ptype.fairyherm)) {
            c.write(captor, "You snag " + getSelf().getName() + " out of the air. She squirms in your hand, but has no chance of breaking free. You lick the fae from pussy to breasts and the little thing squeals "
                            + "in pleasure. The taste is surprisingly sweet and makes your tongue tingle. You continue lapping up the flavor until she climaxes and disappears.");
        } else {
            c.write(captor, Global.format("{other:SUBJECT-ACTION:manage|manages} to catch {self:name-do} and starts pleasuring her with the tip of {other:possessive} finger. The sensitive fae clings to the probing finger desperately as she thrashes "
                            + "in ecstasy. Before %s can do anything to help, {self:subject} vanishes in a burst of orgasmic magic.", getSelf(), captor, owner().subject()));
        }
        if (captor.human()) {
            c.write(captor, "You snag " + getSelf().getName() + " out of the air. He squirms in your hand, but has no chance of breaking free. You lick the fae from his chest to small prick and the little thing squeals "
                            + "in pleasure. The taste is surprisingly sweet and makes your tongue tingle. You continue lapping up the flavor until he climaxes and disappears.");
        } else {
            c.write(captor, Global.format("{other:SUBJECT} snatches {self:name-do} out of the air and flicks the fairy's little testicles with {other:possessive} finger. %s in sympathy as the tiny male curls up in the fetal position "
                            + "and vanishes.", getSelf(), captor, owner().subjectAction("wince", "winces")));
        }
        c.removePet(getSelf());
        c.removePet(getSelf());
    }
    
    private CharacterSex getCharacterSex() {
        switch (gender) {
            case fairyfem:
                return CharacterSex.female;
            case fairymale:
                return CharacterSex.male;
            case fairyherm:
                return CharacterSex.herm;
            default:
                throw new RuntimeException("invalid fairy type");
        }
    }

    @Override
    protected void buildSelf() {
        Growth growth = new Growth();
        PetCharacter self = new PetCharacter(this, owner().nameOrPossessivePronoun() + " " + getName(), getName(), growth, getPower());
        // fairies are about 20 centimeters tall
        self.body.setHeight(22 - (gender==Ptype.fairyfem?2:0));
        self.body.makeGenitalOrgans(getCharacterSex());
        self.body.finishBody(getCharacterSex());
        self.learn(new FairyEnergize(self));
        self.learn(new FairyHeal(self));
        self.learn(new FairyTease(self));
        self.learn(new FairyKick(self));
        self.learn(new FairyShield(self));
        setSelf(self);
    }
}
