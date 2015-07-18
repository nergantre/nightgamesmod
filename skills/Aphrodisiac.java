package skills;

import items.Item;
import characters.Character;
import characters.Emotion;
import characters.Trait;
import characters.body.PussyPart;
import combat.Combat;
import combat.Result;
import global.Global;
import global.Modifier;

public class Aphrodisiac extends Skill {
	public Aphrodisiac(Character self) {
		super("Use Aphrodisiac", self);
	}

	public boolean requirements(Character user) {
		return true;
	}

	public boolean usable(Combat c, Character target) {
		return (c.getStance().mobile(this.getSelf()))
				&& (this.getSelf().canAct())
				&& ((this.getSelf().has(Item.Aphrodisiac)&&(!getSelf().human()||Global.getMatch().condition!=Modifier.noitems)) || ((this.getSelf()
						.hasPussy() && getSelf().body.getRandomPussy() == PussyPart.succubus) && this.getSelf().getArousal().get() >= 10))
				&& (!c.getStance().prone(this.getSelf()));
	}

	public void resolve(Combat c, Character target) {
		int magnitude = Global.random(5) + 15;
		
		if (getSelf().hasPussy() && getSelf().body.getRandomPussy() == PussyPart.succubus) {
				c.write(getSelf(),receive(c, magnitude, Result.strong, getSelf()));
				target.arouse(magnitude, c);
				target.emote(Emotion.horny, 20);
		} else if(getSelf().has(Item.Aersolizer)){
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.special, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.special, getSelf()));
			}
			this.getSelf().consume(Item.Aphrodisiac, 1);
			target.arouse(magnitude, c);
			target.emote(Emotion.horny, 20);
		}
		else if(target.roll(this, c, accuracy())){
			if (this.getSelf().human()) {
				c.write(getSelf(),deal(c, magnitude, Result.normal, target));
			} else {
				c.write(getSelf(),receive(c, magnitude, Result.normal, this.getSelf()));
			}
			target.emote(Emotion.horny, 20);
			this.getSelf().consume(Item.Aphrodisiac, 1);
			target.arouse(magnitude, c);
		}
		else{
			if(getSelf().human()){
				c.write(getSelf(),deal(c,0,Result.miss, target));
			}
			else if(target.human()){
				c.write(getSelf(),receive(c,0,Result.miss, getSelf()));
			}
		}
	}

	public Skill copy(Character user) {
		return new Aphrodisiac(user);
	}

	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	public String deal(Combat c, int damage, Result modifier, Character target) {
		if (modifier == Result.special){
			return "You pop an Aphrodisiac into your Aerosolizer and spray "+target.name()+" with a cloud of mist. She flushes and her eyes fill with lust as it takes hold.";
		}
		else if(modifier == Result.miss){
			return "You throw an Aphrodisiac at "+target.name()+", but she ducks out of the way and it splashes harmlessly on the ground. What a waste.";
		} else if (modifier == Result.strong){
			return getSelf().name()
					+ " dip a finger "
					+ (getSelf().pantsless() ? "" : ("under your "
							+ getSelf().bottom.peek() + " and "))
					+ "into your pussy. Once you have collected a drop of your juices"
					+ " on your fingertip, you pull it out and flick it at " +target.name() + ","
					+ " skillfully depositing it in her open mouth. You immediately see "
					+ " a flash of heat spread through her";
		}
		else{
			return "You uncap a small bottle of Aphrodisiac and splash it in "
					+ target.name()
					+ "'s face. For a second, she's just surprised, but gradually a growing desire "
					+ "starts to make her weak in the knees.";
		}
			
	}

	public String receive(Combat c, int damage, Result modifier, Character attacker) {
		if (modifier == Result.miss){
			return getSelf().name()+" splashes a bottle of liquid in your direction, but none of it hits you.";
		}
		else if(modifier == Result.special){
			return getSelf().name()+" inserts a bottle into the attachment on her arm. You're suddenly surrounded by a sweet smelling cloud of mist. You feel your blood boil " +
					"with desire as the unnatural gas takes effect";
		}
		else if (modifier == Result.strong){
			return attacker.name()
					+ " dips a finger "
					+ (attacker.pantsless() ? "" : ("under her "
							+ attacker.bottom.peek() + " and "))
					+ "into her pussy. Once she has collected a drop of her juices"
					+ " on her fingertip, she pulls it out and flicks it at you,"
					+ " skillfully depositing it in your open mouth. You immediately feel"
					+ " a flash of heat spread through you and only a small part of it"
					+ " results from the anger caused by her dirty move.";
		}
		else{
			return attacker.name()
					+ " throws a strange, sweet-smelling liquid in your face. An unnatural warmth spreads through your body and gathers in your dick like a fire.";
		}
	}

	public String describe() {
		return "Throws a bottle of Aphrodisiac at the opponent";
	}
}
