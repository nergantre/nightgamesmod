package nightgames.stance;

import nightgames.characters.Character;
import nightgames.combat.Combat;
import nightgames.global.Global;

public class BehindFootjob extends AbstractBehindStance {
    public BehindFootjob(Character top, Character bottom) {
        super(top, bottom, Stance.behindfootjob);
    }

    @Override
    public String describe(Combat c) {
        return Global.format(
                        "{self:SUBJECT-ACTION:are|is} holding {other:name-do} from behind with {self:possessive} legs wrapped around {other:direct-object}",
                        top, bottom);
    }

    @Override
    public int pinDifficulty(Combat c, Character self) {
        return 6;
    }

    @Override
    public boolean mobile(Character c) {
        return c != bottom;
    }

    @Override
    public String image() {
        if (bottom.hasDick()) {
            return "behind_footjob.jpg";
        } else {
            return "heelgrind.jpg";
        }
    }

    @Override
    public boolean kiss(Character c, Character target) {
        return c != top && c != bottom;
    }

    @Override
    public boolean dom(Character c) {
        return c == top;
    }

    @Override
    public boolean sub(Character c) {
        return c == bottom;
    }

    @Override
    public boolean reachTop(Character c) {
        return c != bottom;
    }

    @Override
    public boolean reachBottom(Character c) {
        return c != top && c != bottom;
    }

    @Override
    public boolean prone(Character c) {
        return false;
    }

    @Override
    public boolean feet(Character c, Character target) {
        return target == bottom;
    }

    @Override
    public boolean oral(Character c, Character target) {
        return target == bottom && c != top;
    }

    @Override
    public boolean behind(Character c) {
        return c == top;
    }

    @Override
    public boolean inserted(Character c) {
        return false;
    }

    @Override
    public float priorityMod(Character self) {
        return getSubDomBonus(self, 4.0f);
    }

    @Override
    public Position reverse(Combat c, boolean writeMessage) {
        if (writeMessage) {
            c.write(bottom, Global.format(
                            "{self:SUBJECT-ACTION:summon what little willpower you have left and grab|grabs} {other:name-possessive} feet and pull them off {self:name-possessive} crotch. Taking advantage of the momentum, {self:subject} push {other:direct-object} back with {self:name-possessive} body and hold {other:direct-object} down while sitting on top of {other:direct-object}.",
                            bottom, top));
        }
        return new ReverseMount(bottom, top);
    }

    @Override
    public double pheromoneMod(Character self) {
        return 1.5;
    }
    
    @Override
    public int dominance() {
        return 4;
    }

    @Override
    public int distance() {
        return 1;
    }

    @Override
    public void struggle(Combat c, Character struggler) {
        if (struggler.hasDick()) {
            c.write(struggler, Global.format("{self:SUBJECT-ACTION:attempt} to twist out of {other:name-possessive} grip, but "
                            + " {other:pronoun-action:wraps} {other:possessive} legs around {self:possessive} waist and steps "
                            + "on {self:possessive} cock hard, making {self:direct-object} yelp.", struggler, top));
            struggler.body.pleasure(top, top.body.getRandom("feet"), struggler.body.getRandomCock(), Global.random(6, 11), c);
        } else {
            c.write(struggler, Global.format("{self:SUBJECT-ACTION:attempt} to twist out of {other:name-possessive} grip, but "
                            + " {other:pronoun-action:wrap} {other:possessive} legs around {self:possessive} waist and digs {other:possessive} "
                            + "heels into {self:possessive} pussy, making {self:direct-object} yelp.", struggler, top));
            struggler.body.pleasure(top, top.body.getRandom("feet"), struggler.body.getRandomPussy(), Global.random(6, 11), c);
        }
        super.struggle(c, struggler);
    }

    @Override
    public void escape(Combat c, Character escapee) {
        c.write(escapee, Global.format("{self:SUBJECT-ACTION:try} to escape {other:name-possessive} hold, but with"
                        + " {other:direct-object} behind {self:direct-object} with {other:possessive} long legs wrapped around {self:possessive} waist securely, there is nothing {self:pronoun} can do.",
                        escapee, top));
        super.escape(c, escapee);
    }
}
