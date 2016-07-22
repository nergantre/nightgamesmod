package nightgames.characters;

import java.util.Arrays;

import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.IEncounter;
import nightgames.combat.Result;
import nightgames.skills.Tactics;
import nightgames.trap.Trap;

public class DummyCharacter extends Character {

    public DummyCharacter(String name, String type, int level, BodyPart... parts) {
        super(name, level);
        this.type = type;
        Arrays.stream(parts).forEach(part -> body.add(part));
    }

    private String type;

    @Override
    public void ding() {}

    @Override
    public void detect() {}

    @Override
    public void faceOff(Character opponent, IEncounter enc) {}

    @Override
    public void spy(Character opponent, IEncounter enc) {}

    @Override
    public String describe(int per, Combat c) {
        return "";
    }

    @Override
    public void victory(Combat c, Result flag) {}

    @Override
    public void defeat(Combat c, Result flag) {}

    @Override
    public void intervene3p(Combat c, Character target, Character assist) {}

    @Override
    public void victory3p(Combat c, Character target, Character assist) {}

    @Override
    public boolean resist3p(Combat c, Character target, Character assist) {
        return false;
    }

    @Override
    public void act(Combat c) {}

    @Override
    public void move() {}

    @Override
    public void draw(Combat c, Result flag) {}

    @Override
    public boolean human() {
        return false;
    }

    @Override
    public String bbLiner(Combat c) {
        return "";
    }

    @Override
    public String nakedLiner(Combat c) {
        return "";
    }

    @Override
    public String stunLiner(Combat c) {
        return "";
    }

    @Override
    public String taunt(Combat c) {
        return "";
    }

    @Override
    public void intervene(IEncounter fight, Character p1, Character p2) {}

    @Override
    public void showerScene(Character target, IEncounter encounter) {}

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void afterParty() {}

    @Override
    public void emote(Emotion emo, int amt) {}

    @Override
    public String challenge(Character other) {
        return "";
    }

    @Override
    public void promptTrap(IEncounter fight, Character target, Trap trap) {
    }

    @Override
    public void counterattack(Character target, Tactics type, Combat c) {}

    @Override
    public String getPortrait(Combat c) {
        return "";
    }

    @Override public Growth getGrowth() {
        return new Growth();
    }
}
