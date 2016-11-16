package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.NPC;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.pet.CharacterPet;

public class SummonYui extends Skill {
    public SummonYui(Character self) {
        super("Summon Yui", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return Global.getCharacterByType("Yui").getAffection(getSelf()) >= 10;
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canAct() && c.getStance().mobile(getSelf()) && !c.getStance().prone(getSelf())
                        && c.getPetsFor(getSelf()).size() < getSelf().getPetLimit();
    }

    @Override
    public int getMojoCost(Combat c) {
        return 10;
    }

    @Override
    public String describe(Combat c) {
        return "Summon Yui to help you in your fight. Costs a bit of affection.";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        NPC yui = Global.getNPCByType("Yui");
        int power = (getSelf().getLevel() + target.getLevel()) / 2;
        int ac = 4 + power / 3;
        if (getSelf().has(Trait.leadership)) {
            power += 5;
        }
        if (getSelf().has(Trait.tactician)) {
            ac += 3;
        }
        writeOutput(c, Result.normal, target);
        yui.gainAffection(getSelf(), -1);
        c.addPet(new CharacterPet(getSelf(), yui, power, ac).getSelf());

        return true;
    }

    @Override
    public Skill copy(Character user) {
        return new SummonYui(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.summoning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "You pull out one of those tattered scrolls Yui has given you and unroll it. "
                        + "With a firm image of the blonde girl's face in your mind, you smear the ink circle drawn on the page. "
                        + "A split second later the ink on the page seems to twist and blur until it finally coalesces into the loyal ninja's form.";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return Global.format("{self:SUBJECT-ACTION:pull|pulls} out a tattered scroll and {self:action:unroll|unrolls} it. "
                        + "{self:PRONOUN} smears the ink circle drawn on the page with {self:possessive} thumb and drops it onto the ground. "
                        + "A split second later the ink on the page seems to twist and blur until it finally coalesces into the familiar ninja's form", getSelf(), target);
    }
}
