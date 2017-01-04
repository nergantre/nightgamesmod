package nightgames.skills;

import java.util.Arrays;
import java.util.List;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;

public class SlimeMimicry extends Skill {
    private final static List<Skill> MIMICRY_SKILLS = Arrays.asList(
                    new MimicAngel(Global.noneCharacter()),
                    new MimicCat(Global.noneCharacter()),
                    new MimicDryad(Global.noneCharacter()),
                    new MimicSuccubus(Global.noneCharacter()),
                    new MimicWitch(Global.noneCharacter())
                    );
    
    public SlimeMimicry(Character self) {
        super("Slime Mimicry", self);
    }

    @Override
    public boolean requirements(Combat c, Character user, Character target) {
        return user.get(Attribute.Slime) >= 10 && !user.human();
    }

    @Override
    public boolean usable(Combat c, Character target) {
        return getSelf().canRespond();
    }

    @Override
    public String describe(Combat c) {
        return "Mimics a random NPC";
    }

    @Override
    public boolean resolve(Combat c, Character target) {
        return Global.pickRandom(MIMICRY_SKILLS).get().copy(getSelf()).resolve(c, target);
    }

    @Override
    public Skill copy(Character user) {
        return new SlimeMimicry(user);
    }

    @Override
    public Tactics type(Combat c) {
        return Tactics.positioning;
    }

    @Override
    public String deal(Combat c, int damage, Result modifier, Character target) {
        return "";
    }

    @Override
    public String receive(Combat c, int damage, Result modifier, Character target) {
        return "";
    }

}
