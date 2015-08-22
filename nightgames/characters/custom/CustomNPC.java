package nightgames.characters.custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import nightgames.characters.Attribute;
import nightgames.characters.BasePersonality;
import nightgames.characters.Character;
import nightgames.characters.Emotion;
import nightgames.characters.NPC;
import nightgames.characters.Trait;
import nightgames.characters.BasePersonality.PreferredAttribute;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.global.Modifier;
import nightgames.items.ItemAmount;
import nightgames.skills.Tactics;
public class CustomNPC extends BasePersonality {
	/**
	 * 
	 */
	private NPCData data;
	private static final long serialVersionUID = -8169646189131720872L;
	public CustomNPC(NPCData data){
		super();
		this.data = data;
		growth = data.getGrowth();
		preferredAttributes = new ArrayList<PreferredAttribute>(data.getPreferredAttributes());
		character = new NPC(data.getName(),data.getStats().level,this);
		character.outfit[0].addAll(data.getTopOutfit());
		character.outfit[1].addAll(data.getBottomOutfit());
		character.closet.addAll(character.outfit[0]);
		character.closet.addAll(character.outfit[1]);
		character.change(Modifier.normal);
		character.att = new HashMap<Attribute, Integer>(data.getStats().attributes);
		character.traits = new HashSet<Trait>(data.getStats().traits);
		character.getArousal().setMax(data.getStats().arousal);
		character.getStamina().setMax(data.getStats().stamina);
		character.getMojo().setMax(data.getStats().mojo);
		character.getWillpower().setMax(data.getStats().willpower);
		character.setTrophy(data.getTrophy());
		character.plan = data.getPlan();
		character.mood = Emotion.confident;
		for (BodyPart part : data.getBodyParts()) {
			character.body.add(part);
		}
		character.body.finishBody(data.getGender());
		for (ItemAmount i : data.getStartingItems()) {
			character.gain(i.item, i.amount);;
		}
		Global.gainSkills(character);
	}

	@Override
	public void rest() {
		for (ItemAmount i : data.getPurchasedItems()) {
			buyUpTo(i.item, i.amount);
		}
	}

	@Override
	public String bbLiner(Combat c) {
		return data.getLine("hurt", c, this.character, c.getOther(character));
	}

	@Override
	public String nakedLiner(Combat c) {
		return data.getLine("naked", c, this.character, c.getOther(character));
	}

	@Override
	public String stunLiner(Combat c) {
		return data.getLine("stunned", c, this.character, c.getOther(character));
	}

	@Override
	public String taunt(Combat c) {
		return data.getLine("taunt", c, this.character, c.getOther(character));
	}

	@Override
	public String temptLiner(Combat c) {
		return data.getLine("tempt", c, this.character, c.getOther(character));
	}

	@Override
	public String victory(Combat c,Result flag) {
		character.getArousal().empty();
		return data.getLine("victory", c, this.character, c.getOther(character));
	}

	@Override
	public String defeat(Combat c, Result flag) {
		return data.getLine("defeat", c, this.character, c.getOther(character));
	}

	@Override
	public String describe(Combat c) {
		return data.getLine("describe", c, this.character, c.getOther(character));
	}

	@Override
	public String draw(Combat c,Result flag) {
		return data.getLine("draw", c, this.character, c.getOther(character));
	}

	@Override
	public boolean fightFlight(Character opponent) {
		return !character.nude()||opponent.nude();
	}

	@Override
	public boolean attack(Character opponent) {
		return true;
	}

	@Override
	public String victory3p(Combat c, Character target, Character assist) {
		if (target.human())
			return data.getLine("victory3p", c, this.character, assist);
		else
			return data.getLine("victory3pAssist", c, this.character, target);
	}

	@Override
	public String intervene3p(Combat c, Character target, Character assist) {
		if (target.human())
			return data.getLine("intervene3p", c, this.character, assist);
		else
			return data.getLine("intervene3pAssist", c, this.character, target);
	}

	@Override
	public String startBattle(Character other) {
		return data.getLine("startBattle", null, this.character, other);
	}
	@Override
	public boolean fit() {
		return !character.nude()&&character.getStamina().percent()>=50;
	}
	@Override
	public String night() {
		return data.getLine("startBattle", null, this.character, Global.getPlayer());
	}

	public boolean checkMood(Emotion mood, int value) {
		return data.checkMood(this.character, mood, value);
	}

	@Override
	public String orgasmLiner(Combat c) {
		return data.getLine("orgasm", c, this.character, c.getOther(character));
	}

	@Override
	public String makeOrgasmLiner(Combat c) {
		return data.getLine("makeOrgasm", c, this.character, c.getOther(character));
	}

	@Override
	public String getType() {
		return "CUSTOM_" + data.getType();
	}
	
	@Override
	public String image(Combat c) {
		Character other = c.getOther(character);
		return data.getPortraitName(c, character, other);
	}
	
	public String defaultImage() {
		return data.getDefaultPortraitName();
	}
}
