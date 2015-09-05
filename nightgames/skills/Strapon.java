package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.global.Modifier;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.stance.Stance;

public class Strapon extends Skill {

	public Strapon(Character self) {
		super("Strap On", self);
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return getSelf().canAct()
				&&c.getStance().mobile(getSelf())
				&&!c.getStance().prone(getSelf())
				&&getSelf().crotchAvailable()
				&&(getSelf().has(Item.Strapon)||getSelf().has(Item.Strapon2))
				&&!getSelf().hasDick()
				&&!c.getStance().penetration(getSelf())
				&&!c.getStance().penetration(target)
				&&(!getSelf().human()||Global.getMatch().condition!=Modifier.notoys)
				&&c.getStance().enumerate()!=Stance.facesitting;
	}

	@Override
	public String describe(Combat c) {
		return "Put on the strapon dildo";
	}

	@Override
	public int getMojoBuilt(Combat c) {
		return 15;
	}
	@Override
	public boolean resolve(Combat c, Character target) {
		getSelf().getOutfit().equip(Clothing.getByName("strapon"));
		if(getSelf().human()){
			c.write(getSelf(),deal(c,0,Result.normal, target));
		}
		else if(target.human()){
			c.write(getSelf(),receive(c,0,Result.normal, target));
		}
		target.loseMojo(c, 10);
		target.emote(Emotion.nervous, 10);
		getSelf().emote(Emotion.confident, 30);
		getSelf().emote(Emotion.dominant, 40);
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Strapon(user);
	}

	@Override
	public Tactics type(Combat c) {
		return Tactics.positioning;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		return "You put on a strap on dildo.";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		return getSelf().name()+" straps on a thick rubber cock and grins at you in a way that makes you feel a bit nervous.";
	}

}
