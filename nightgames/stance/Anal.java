package nightgames.stance;


import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.AnalPussyPart;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.status.Stsflag;

public class Anal extends AnalSexStance {

	public Anal(Character top, Character bottom) {
		super(top, bottom, Stance.anal);
	}

	@Override
	public String describe() {
		if(top.human()){
			return "You're behind " + bottom.name() + " and your cock in buried in "+bottom.possessivePronoun()+" ass.";
		}
		else if (top.has(Trait.strapped)){
			return top.name()+" is pegging you with her strapon dildo from behind.";
		}
		else{
			return top.name()+" is fucking you in the ass from behind";
		}
	}

	public String image() {
		if (bottom.hasPussy()) {
			return "analf.jpg";
		} else {
			return "pegging.jpg";
		}
	}

	@Override
	public boolean mobile(Character c) {
		return c==top;
	}

	@Override
	public boolean kiss(Character c) {
		return false;
	}

	@Override
	public boolean dom(Character c) {
		return c==top;
	}

	@Override
	public boolean sub(Character c) {
		return c==bottom;
	}

	@Override
	public boolean reachTop(Character c) {
		return c==top;
	}

	@Override
	public boolean reachBottom(Character c) {
		return c==top;
	}

	@Override
	public boolean prone(Character c) {
		return false;
	}

	@Override
	public boolean feet(Character c) {
		return false;
	}

	@Override
	public boolean oral(Character c) {
		return false;
	}

	@Override
	public boolean behind(Character c) {
		return c==top;
	}

	@Override
	public boolean inserted(Character c) {
		return c==top;
	}

	@Override
	public Position insertRandom() {
		return new Behind(top,bottom);
	}

	public void checkOngoing(Combat c){
		Character inserter = inserted(top) ? top : bottom;
		Character inserted = inserted(top) ? bottom : top;
		
		if(!inserter.hasInsertable()){
			if(inserted.human()){
				c.write("With "+inserter.name()+"'s pole gone, your ass gets a respite.");
			} else {
				c.write(inserted.name() + " sighs with relief with your phallus gone.");
			}
			c.setStance(insertRandom());
		}
		if (inserted.body.getRandom("ass") == null) {
			if(inserted.human()){
				c.write("With your asshole suddenly disappearing, " + inserter.name() + "'s dick pops out of what was once your sphincter.");
			} else {
				c.write("Your dick pops out of " + inserted.name() + " as her asshole shrinks and disappears.");
			}
			c.setStance(insertRandom());
		}
	}

	public Position reverse(Combat c) {
		if (top.has(Trait.strapped)) {
			c.write(bottom, Global.format("As {other:subject-action:are|is} thrusting into {self:name-do} with {other:possessive} strapon, {self:subject-action:force|forces} {self:possesive} hips back and knock {other:direct-object} off balance. {self:SUBJECT-ACTION:quickly pull|quickly pulls} {other:possessive} fake cock out of {self:possessive} bottom while sitting on top of {other:direct-object}.", bottom, top));
			return new ReverseMount(bottom, top);
		} else {
			c.write(bottom, Global.format("As {other:subject-action:are|is} thrusting into {self:name-do} with {other:possessive} {other:body-part:cock}, {self:subject-action:force|forces} {self:possesive} hips back and knock {other:direct-object} off balance. {self:SUBJECT-ACTION:quickly manuever|quickly manuevers} {self:reflective} on top of {other:direct-object}, now fucking {other:direct-object} back in an anal cowgirl position.", bottom, top));
			return new AnalCowgirl(bottom, top);
		}
	}

	@Override
	public BodyPart topPart() {
		return top.body.getRandomInsertable();
	}
	
	@Override
	public BodyPart bottomPart() {
		return bottom.body.getRandomAss();
	}
	
}