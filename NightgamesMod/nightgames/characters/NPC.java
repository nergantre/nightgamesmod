package nightgames.characters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import nightgames.actions.Shortcut;
import nightgames.actions.Action;
import nightgames.actions.Move;
import nightgames.actions.Movement;
import nightgames.areas.Area;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.StraponPart;
import nightgames.characters.custom.RecruitmentData;
import nightgames.characters.custom.effect.CustomEffect;
import nightgames.combat.Combat;
import nightgames.combat.Encounter;
import nightgames.combat.Result;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.global.DefaultModifier;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.skills.Nothing;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.stance.Behind;
import nightgames.stance.Neutral;
import nightgames.stance.Position;
import nightgames.status.Enthralled;
import nightgames.status.Horny;
import nightgames.status.Masochistic;
import nightgames.status.Status;
import nightgames.status.Stsflag;
import nightgames.trap.Trap;

public class NPC extends Character {
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = -7213318631528080442L;
	public Personality ai;
	public HashMap<Emotion, Integer> emotes;
	public Emotion mood;
	public Plan plan;
	private boolean fakeHuman;

	public NPC(String name, int level, Personality ai) {
		super(name, level);
		this.ai = ai;
		this.fakeHuman = false;
		emotes = new HashMap<Emotion, Integer>();
		for (Emotion e : Emotion.values()) {
			emotes.put(e, 0);
		}
	}

	@Override
	public String describe(int per, Combat c) {
		String description = ai.describeAll(c);
		for (Status s : status) {
			description = description + "<br>" + s.describe(c);
		}
		description = description + "<p>";
		description = description + outfit.describe(this);
		description = description + observe(per);
		return description;
	}

	private String observe(int per) {
		String visible = "";
		for (Status s : status) {
			if (s.flags().contains(Stsflag.unreadable)) {
				return visible;
			}
		}
		if (per >= 9) {
			visible = visible + "Her arousal is at " + arousal.percent()
					+ "%<br>";
		}
		if (per >= 8) {
			visible = visible + "Her stamina is at " + stamina.percent()
					+ "%<br>";
		}
		if (per >= 9) {
			visible = visible + "Her willpower is at " + willpower.percent()
					+ "%<br>";
		}
		if (per >= 7) {
			visible = visible + "She looks " + mood.name() + "<br>";
		}
		if (per >= 7 && per < 9) {
			if (arousal.percent() >= 75) {
				visible = visible
						+ "She's dripping with arousal and breathing heavily. She's at least 3/4 of the way to orgasm<br>";
			} else if (arousal.percent() >= 50) {
				visible = visible
						+ "She's showing signs of arousal. She's at least halfway to orgasm<br>";
			} else if (arousal.percent() >= 25) {
				visible = visible
						+ "She's starting to look noticeably arousal, maybe a quarter of her limit<br>";
			}
			if (willpower.percent() <= 75) {
				visible = visible + "She still seems ready to fight.<br>";
			} else if (willpower.percent() <= 50) {
				visible = visible
						+ "She seems a bit unsettled, but she still has some spirit left in her.<br>";
			} else if (willpower.percent() <= 25) {
				visible = visible
						+ "Her eyes seem glazed over and ready to give in.<br>";
			}
		}
		if (per >= 6 && per < 8) {
			if (stamina.percent() <= 66) {
				visible = visible + "She's starting to look tired<br>";
			} else if (stamina.percent() <= 33) {
				visible = visible + "She looks a bit unsteady on her feet<br>";
			}
		}
		if (per >= 3 && per < 7) {
			if (arousal.percent() >= 50) {
				visible = visible
						+ "She's showing clear sign of arousal. You're definitely getting to her.<br>";
			}
			if (willpower.percent() <= 50) {
				visible = visible
						+ "She seems a bit distracted and unable to look you in the eye.<br>";

			}
		}
		if (per >= 4 && per < 6) {
			if (stamina.percent() <= 50) {
				visible = visible + "She looks pretty tired<br>";
			}
		}

		if (per >= 6 && this.status.size() > 0) {
			visible += "List of statuses:<br><i>";
			for (Status s : this.status) {
				visible += s + ", ";
			}
			visible += "</i><br>";
		}
		return visible;
	}

	@Override
	public void victory(Combat c, Result flag) {
		Character target;
		if (c.p1 == this) {
			target = c.p2;
		} else {
			target = c.p1;
		}
		this.gainXP(getVictoryXP(target));
		target.gainXP(getDefeatXP(this));
		if (c.getStance().inserted() && c.getStance().dom(this)) {
			getMojo().gain(2);
			if (has(Trait.mojoMaster)) {
				getMojo().gain(2);
			}
		}
		target.arousal.empty();
		if (target.has(Trait.insatiable)) {
			target.arousal.restore((int) (arousal.max() * .2));
		}
		dress(c);
		target.undress(c);
		gainTrophy(c, target);

		target.defeated(this);
		c.write(ai.victory(c, flag));
		gainAttraction(target, 1);
		target.gainAttraction(this, 2);
	}

	@Override
	public void defeat(Combat c, Result flag) {
		Character target;
		if (c.p1 == this) {
			target = c.p2;
		} else {
			target = c.p1;
		}
		this.gainXP(getDefeatXP(target));
		target.gainXP(getVictoryXP(this));
		this.arousal.empty();
		if (!target.human()
				|| Global.getMatch().condition != DefaultModifier.norecovery) {
			target.arousal.empty();
		}
		if (this.has(Trait.insatiable)) {
			this.arousal.restore((int) (arousal.max() * .2));
		}
		if (target.has(Trait.insatiable)) {
			target.arousal.restore((int) (arousal.max() * .2));
		}
		target.dress(c);
		this.undress(c);
		target.gainTrophy(c, this);
		this.defeated(target);
		c.write(ai.defeat(c, flag));
		gainAttraction(target, 2);
		target.gainAttraction(this, 1);
	}

	public void intervene3p(Combat c, Character target, Character assist) {
		this.gainXP(getAssistXP(target));
		target.defeated(this);
		c.write(ai.intervene3p(c, target, assist));
		assist.gainAttraction(this, 1);
	}

	public void victory3p(Combat c, Character target, Character assist) {
		this.gainXP(getVictoryXP(target));
		target.gainXP(getDefeatXP(this));
		target.arousal.empty();
		if (target.has(Trait.insatiable)) {
			target.arousal.restore((int) (arousal.max() * .2));
		}
		dress(c);
		target.undress(c);
		gainTrophy(c, target);
		target.defeated(this);
		c.write(ai.victory3p(c, target, assist));
		gainAttraction(target, 1);
	}

	@Override
	public boolean resist3p(Combat combat, Character intruder, Character assist) { 
		if (has(Trait.cursed)) {
	       Global.gui().message(this.ai.resist3p(combat, intruder, assist));
	       return true;
	     }	     
	     return false;
	   }
	
	@Override
	public void act(Combat c) {
		HashSet<Skill> available = new HashSet<Skill>();
		Character target;
		if (c.p1 == this) {
			target = c.p2;
		} else {
			target = c.p1;
		}
		if (target.human() && Global.isDebugOn(DebugFlags.DEBUG_SKILL_CHOICES)) {
			pickSkillsWithGUI(c, target);
		} else {
			for (Skill act : skills) {
				if (Skill.skillIsUsable(c, act, target)
						&& cooldownAvailable(act)) {
					available.add(act);
				}
			}
			Skill.filterAllowedSkills(c, available, this, target);
			if (available.size() == 0) {
				available.add(new Nothing(this));
			}
			c.act(this, ai.act(available, c), "");
		}
	}

	public Skill actFast(Combat c) {
		HashSet<Skill> available = new HashSet<Skill>();
		Character target;
		if (c.p1 == this) {
			target = c.p2;
		} else {
			target = c.p1;
		}
		for (Skill act : skills) {
			if (Skill.skillIsUsable(c, act, target) && cooldownAvailable(act)) {
				available.add(act);
			}
		}
		Skill.filterAllowedSkills(c, available, this, target);
		if (available.size() == 0) {
			available.add(new Nothing(this));
		}
		return ai.act(available, c);
	}

	@Override
	public boolean human() {
		return fakeHuman;
	}

	public void setFakeHuman(boolean val) {
		fakeHuman = val;
	}

	@Override
	public void draw(Combat c, Result flag) {
		Character target;
		if (c.p1 == this) {
			target = c.p2;
		} else {
			target = c.p1;
		}
		this.gainXP(getVictoryXP(target));
		target.gainXP(getVictoryXP(this));
		this.arousal.empty();
		target.arousal.empty();
		if (this.has(Trait.insatiable)) {
			this.arousal.restore((int) (arousal.max() * .2));
		}
		if (target.has(Trait.insatiable)) {
			target.arousal.restore((int) (arousal.max() * .2));
		}
		target.undress(c);
		this.undress(c);
		target.gainTrophy(c, this);
		this.gainTrophy(c, target);
		target.defeated(this);
		this.defeated(target);
		c.write(ai.draw(c, flag));
		gainAttraction(target, 4);
		target.gainAttraction(this, 4);
		if (getAffection(target) > 0) {
			gainAffection(target, 1);
			target.gainAffection(this, 1);
			if (this.has(Trait.affectionate) || target.has(Trait.affectionate)) {
				gainAffection(target, 2);
				target.gainAffection(this, 2);
			}
		}
	}

	@Override
	public String orgasmLiner(Combat c) {
		return ai.orgasmLiner(c);
	}

	@Override
	public String makeOrgasmLiner(Combat c) {
		return ai.makeOrgasmLiner(c);
	}

	@Override
	public String bbLiner(Combat c) {
		return ai.bbLiner(c);
	}

	@Override
	public String nakedLiner(Combat c) {
		return ai.nakedLiner(c);
	}

	@Override
	public String stunLiner(Combat c) {
		return ai.stunLiner(c);
	}

	@Override
	public String taunt(Combat c) {
		return ai.taunt(c);
	}

	@Override
	public String temptLiner(Combat c) {
		return ai.temptLiner(c);
	}

	@Override
	public void detect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void move() {
		if (state == State.combat) {
			location.fight.battle();
		} else if (busy > 0) {
			busy--;
		} else if (this.is(Stsflag.enthralled) && !has(Trait.immobile)) {
			Character master;
			master = ((Enthralled) getStatus(Stsflag.enthralled)).master;
			Move compelled = findPath(master.location);
			if (compelled != null) {
				compelled.execute(this);
				return;
			}
		} else if (state == State.shower || state == State.lostclothes) {
			bathe();
		} else if (state == State.crafting) {
			craft();
		} else if (state == State.searching) {
			search();
		} else if (state == State.resupplying) {
			resupply();
		} else if (state == State.webbed) {
			state = State.ready;
		} else if (state == State.masturbating) {
			masturbate();
		} else {
			if (!location.encounter(this)) {
				HashSet<Action> available = new HashSet<Action>();
				HashSet<Movement> radar = new HashSet<Movement>();
				if (!has(Trait.immobile)) {
					for (Area path : location.adjacent) {
						available.add(new Move(path));
						if (path.ping(get(Attribute.Perception))) {
							radar.add(path.id());
						}
					}
					if (getPure(Attribute.Cunning) >= 28) {
				         for (Area path : this.location.shortcut) {
				           available.add(new Shortcut(path));
				         }
				    }
				}
				for (Action act : Global.getActions()) {
					if (act.usable(this)) {
						available.add(act);
					}
				}
				if (location.humanPresent()) {
					Global.gui().message(
							"You notice "
									+ name()
									+ ai.move(available, radar).execute(this)
											.describe());
				} else {
					ai.move(available, radar).execute(this);
				}
			}
		}
	}

	@Override
	public void faceOff(Character opponent, Encounter enc) {
		enc.fightOrFlight(this, ai.fightFlight(opponent));
	}

	@Override
	public void spy(Character opponent, Encounter enc) {
		if (ai.attack(opponent)) {
			enc.ambush(this, opponent);
		} else {
			location.endEncounter();
		}
	}

	public void ding() {
		level++;
		ai.ding();
		String message = Global.gainSkills(this);
		if (human())
			Global.gui().message(message);
	}

	@Override
	public void showerScene(Character target, Encounter encounter) {
		if (this.has(Item.Aphrodisiac)) {
			encounter.aphrodisiactrick(this, target);
		} else if (!target.mostlyNude() && Global.random(3) >= 2) {
			encounter.steal(this, target);
		} else {
			encounter.showerambush(this, target);
		}
	}

	@Override
	public void intervene(Encounter enc, Character p1, Character p2) {
		if (Global.random(20) + getAffection(p1)
				+ (p1.has(Trait.sympathetic) ? 10 : 0) >= Global.random(20)
				+ getAffection(p2) + (p2.has(Trait.sympathetic) ? 10 : 0)) {
			enc.intrude(this, p1);
		} else {
			enc.intrude(this, p2);
		}
	}

	@Override
	public String challenge(Character other) {
		return ai.startBattle(other);
	}

	@Override
	public void promptTrap(Encounter enc, Character target, Trap trap) {
		if (ai.attack(target)) {
			enc.trap(this, target, trap);
		} else {
			location.endEncounter();
		}
	}

	@Override
	public void afterParty() {
		Global.gui().message(ai.night());
	}

	public void daytime(int time) {
		ai.rest(time);
	}

	public Emotion getMood() {
		return mood;
	}

	@Override
	public void counterattack(Character target, Tactics type, Combat c) {
		switch (type) {
		case damage:
			c.write(this,
					name()
							+ " avoids your clumsy attack and swings her fist into your nuts.");
			target.pain(c,
					4 + Math.min(Global.random(get(Attribute.Power)), 20));
			break;
		case pleasure:
			if (target.hasDick()) {
				if (target.crotchAvailable()) {
					c.write(this,
							name()
									+ " catches you by the penis and rubs your sensitive glans.");
					target.body
							.pleasure(this, body.getRandom("hands"),
									target.body.getRandom("cock"),
									4 + Math.min(Global
											.random(get(Attribute.Seduction)),
											20), c);
				} else {
					c.write(this,
							name()
									+ " catches you as you approach and grinds her knee into the tent in your "
									+ target.getOutfit().getTopOfSlot(
											ClothingSlot.bottom));
					target.body
							.pleasure(this, body.getRandom("legs"), target.body
									.getRandom("cock"),
									4 + Math.min(Global
											.random(get(Attribute.Seduction)),
											20), c);
				}
			} else {
				c.write(this,
						name()
								+ " pulls you off balance and licks your sensitive ear. You tremble as she nibbles on your earlobe.");
				target.body
						.pleasure(this, body.getRandom("tongue"), target.body
								.getRandom("ears"), 4 + Math.min(
								Global.random(get(Attribute.Seduction)), 20), c);
			}
			break;
		case fucking:
			if (c.getStance().sub(this)) {
				Position reverse = c.getStance().reverse(c);
				if (reverse != c.getStance() && !BodyPart.hasOnlyType(reverse.bottomParts(), "strapon")) {
					c.setStance(reverse);
				} else {
					c.write(this,
							Global.format(
									"{self:NAME-POSSESSIVE} quick wits find a gap in {other:name-possessive} hold and {self:action:slip|slips} away.",
									this, target));
					c.setStance(new Neutral(this, target));
				}
			} else {
				target.body.pleasure(this, body.getRandom("hands"), target.body.getRandomBreasts(),
						4 + Math.min(Global.random(get(Attribute.Seduction)), 20), c);
				c.write(this,
						Global.format(
								"{self:SUBJECT-ACTION:pinch|pinches} {other:possessive} nipples with {self:possessive} hands as {other:subject-action:try|tries} to fuck {self:direct-object}. "
										+ "While {other:subject-action:yelp|yelps} with surprise, {self:subject-action:take|takes} the chance to pleasure {other:possessive} body",
								this, target));
			}

			break;
		case stripping:
			Clothing clothes = target.stripRandom(c);
			if (clothes != null) {
				c.write(this,
						name()
								+ " manages to catch you groping her clothing, and in a swift motion strips off your "
								+ clothes.getName());
			} else {
				c.write(this,
						name()
								+ " manages to dodge your groping hands and gives a retaliating slap in return");
				target.pain(c,
						4 + Math.min(Global.random(get(Attribute.Power)), 20));
			}
			break;
		case positioning:
			if (c.getStance().dom(this)) {
				c.write(this,
						name()
								+ " outmanuevers you and you're exhausted from the struggle.");
				target.weaken(c, 10);
			} else {
				c.write(this,
						name()
								+ " outmanuevers you and catches you from behind when you stumble.");
				c.setStance(new Behind(this, target));
			}
			break;
		default:
			c.write(this,
					name()
							+ " manages to dodge your attack and gives a retaliating slap in return");
			target.pain(c,
					4 + Math.min(Global.random(get(Attribute.Power)), 20));
		}
	}

	public Skill prioritize(ArrayList<WeightedSkill> plist) {
		if (plist.isEmpty()) {
			return null;
		}
		float sum = 0;
		ArrayList<WeightedSkill> wlist = new ArrayList<WeightedSkill>();
		for (WeightedSkill wskill : plist) {
			sum += wskill.weight;
			wlist.add(new WeightedSkill(sum, wskill.skill));
			if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS))
				System.out.printf("%.1f %s\n", sum, wskill.skill);
		}

		if (wlist.isEmpty()) {
			return null;
		}
		float s = Global.randomfloat() * sum;
		for (WeightedSkill wskill : wlist) {
			if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS))
				System.out.printf("%.1f/%.1f %s\n", wskill.weight, s,
						wskill.skill);
			if (wskill.weight > s) {
				return wskill.skill;
			}
		}
		return plist.get(plist.size() - 1).skill;
	}

	public void emote(Emotion emo, int amt) {
		if (Global.isDebugOn(DebugFlags.DEBUG_MOOD)) {
			System.out.printf("%s: %+d %s", getName(), amt, emo.name());
		}
		if (emo == mood) {
			// if already this mood, cut gain by half
			amt = Math.max(1, amt / 2);
		}
		emotes.put(emo, emotes.get(emo) + amt);
	}

	public Emotion moodSwing(Combat c) {
		Emotion current = mood;
		for (Emotion e : emotes.keySet()) {
			if (ai.checkMood(c, e, emotes.get(e))) {
				emotes.put(e, 0);
				// cut all the other emotions by half so that the new mood
				// persists for a bit
				for (Emotion e2 : emotes.keySet()) {
					emotes.put(e2, emotes.get(e2) / 2);
				}
				mood = e;
				if (Global.isDebugOn(DebugFlags.DEBUG_MOOD)) {
					System.out.printf("Moodswing: %s is now %s\n", name,
							mood.name());
				}
				if (c.p1.human() || c.p2.human())
					Global.gui().loadPortrait(c, c.p1, c.p2);
				return e;
			}
		}
		return current;
	}

	@Override
	public void eot(Combat c, Character opponent, Skill last) {
		super.eot(c, opponent, last);
		if (opponent.pet != null && canAct() && c.getStance().mobile(this)
				&& !c.getStance().prone(this)) {
			if (get(Attribute.Speed) > opponent.pet.ac() * Global.random(20)) {
				opponent.pet.caught(c, this);
			}
		}
		if (opponent.has(Trait.pheromones)
				&& opponent.getArousal().percent() >= 20
				&& opponent.rollPheromones(c)) {
			c.write(opponent,
					"<br>You see "
							+ name()
							+ " swoon slightly as she gets close to you. Seems like she's starting to feel the effects of your musk.");
			add(c,
					new Horny(this, opponent.getPheromonePower(), 10, opponent.nameOrPossessivePronoun()
							+ " pheromones"));
		}
		if (opponent.has(Trait.smqueen) && !is(Stsflag.masochism)) {
			c.write(Global.capitalizeFirstLetter(String
					.format("<br>%s seems to shudder in arousal at the thought of pain.",
							subject())));
			add(c, new Masochistic(this));
		}
		if (has(Trait.RawSexuality)) {
			tempt(c, opponent, getArousal().max() / 25);
			opponent.tempt(c, this, opponent.getArousal().max() / 25);
		}
		if (c.getStance().dom(this)) {
			emote(Emotion.dominant, 20);
			emote(Emotion.confident, 10);
		} else if (c.getStance().sub(this)) {
			emote(Emotion.nervous, 15);
			emote(Emotion.desperate, 10);
		}
		if (opponent.mostlyNude()) {
			emote(Emotion.horny, 15);
			emote(Emotion.confident, 10);
		}
		if (mostlyNude()) {
			emote(Emotion.nervous, 10);
			if (has(Trait.exhibitionist)) {
				emote(Emotion.horny, 20);
			}
		}
		if (opponent.getArousal().percent() >= 75) {
			emote(Emotion.confident, 20);
		}

		if (getArousal().percent() >= 50) {
			emote(Emotion.horny, getArousal().percent() / 4);
		}

		if (getArousal().percent() >= 50) {
			emote(Emotion.desperate, 10);
		}
		if (getArousal().percent() >= 75) {
			emote(Emotion.desperate, 20);
			emote(Emotion.nervous, 10);
		}
		if (getArousal().percent() >= 90) {
			emote(Emotion.desperate, 20);
		}
		if (!canAct()) {
			emote(Emotion.desperate, 10);
		}
		if (!opponent.canAct()) {
			emote(Emotion.dominant, 20);
		}
		moodSwing(c);
	}

	public NPC clone() throws CloneNotSupportedException {
		return (NPC) super.clone();
	}

	public float rateAction(Combat c, float selfFit, float otherFit,
			CustomEffect effect) {
		// Clone ourselves a new combat... This should clone our characters, too
		Combat c2;
		try {
			c2 = c.clone();
		} catch (CloneNotSupportedException e) {
			return 0;
		}

		Global.debugSimulation += 1;
		Character newSelf;
		Character newOther;
		if (c.p1 == this) {
			newSelf = c2.p1;
			newOther = c2.p2;
		} else if (c.p2 == this) {
			newSelf = c2.p2;
			newOther = c2.p1;
		} else {
			throw new IllegalArgumentException(
					"Tried to use a badly cloned combat");
		}
		effect.execute(c2, newSelf, newOther);
		Global.debugSimulation -= 1;
		float selfFitnessDelta = newSelf.getFitness(c) - selfFit;
		float otherFitnessDelta = newSelf.getOtherFitness(c, newOther)
				- otherFit;
		if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS_RATING)
				&& (c2.p1.human() || c2.p2.human())) {
			System.out.println("After:\n" + c2.debugMessage());
		}
		return selfFitnessDelta - otherFitnessDelta;
	}

	private float rateMove(Skill skill, Combat c, float selfFit, float otherFit) {
		// Clone ourselves a new combat... This should clone our characters, too
		if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS_RATING)
				&& (c.p1.human() || c.p2.human())) {
			System.out.println("===> Rating " + skill);
			System.out.println("Before:\n" + c.debugMessage());
		}
		return rateAction(c, selfFit, otherFit, (combat, self, other) -> {
			skill.setSelf(self);
			skill.resolve(combat, other);
			skill.setSelf(this);
			return true;
		});
	}

	public Skill prioritizeNew(ArrayList<WeightedSkill> plist, Combat c) {
		if (plist.isEmpty()) {
			return null;
		}
		// The higher, the better the AI will plan for "rare" events better
		final int RUN_COUNT = 5;
		// Decrease to get an "easier" AI. Make negative to get a suicidal AI.
		final float RATING_FACTOR = 0.02f;

		// Starting fitness
		Character other = c.getOther(this);
		float selfFit = getFitness(c);
		float otherFit = getOtherFitness(c, other);

		// Now simulate the result of all actions
		ArrayList<WeightedSkill> moveList = new ArrayList<WeightedSkill>();
		float sum = 0;
		for (WeightedSkill wskill : plist) {
			// Run it a couple of times
			float rating, raw_rating = 0;
			if (wskill.skill.type(c) == Tactics.fucking && has(Trait.experienced)) {
				wskill.weight += 1.0;
			}
			if (wskill.skill.type(c) == Tactics.damage && has(Trait.smqueen)) {
				wskill.weight += 1.0;
			}
			for (int j = 0; j < RUN_COUNT; j++) {
				raw_rating += rateMove(wskill.skill, c, selfFit, otherFit);
			}

	        wskill.weight += ai.getAiModifiers().modAttack(wskill.skill.getClass());
			// Sum up rating, add to map
			rating = (float) Math.exp(RATING_FACTOR * raw_rating + wskill.weight + wskill.skill.priorityMod(c));
			sum += rating;
			moveList.add(new WeightedSkill(sum, raw_rating, rating, wskill.skill));
		}
		if (sum == 0 || moveList.size() == 0)
			return null;
		// Debug
		if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS)) {
			String s = "AI choices: ";
			for (WeightedSkill entry : moveList) {
				s += String.format("\n(%.1f\t\t%.1f\t\tculm: %.1f\t\t/ %.1f)\t\t-> %s", entry.raw_rating, entry.rating,
						entry.weight, entry.rating * 100.0f / sum, entry.skill.getLabel(c));
			}
			System.out.println(s);
		}
		// Select
		float s = Global.randomfloat() * sum;
		for (WeightedSkill entry : moveList) {
			if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS))
				System.out.printf("%.1f/%.1f %s\n", entry.weight, s, entry.skill.toString());
			if (entry.weight > s) {
				return entry.skill;
			}
		}
		return moveList.get(moveList.size() - 1).skill;
	}

	@Override
	public String getPortrait(Combat c) {
		return ai.image(c);
	}

	@Override
	public String getType() {
		return ai.getType();
	}

	public RecruitmentData getRecruitmentData() {
		return ai.getRecruitmentData();
	}

	@Override
	public double dickPreference() {
		return ai instanceof Eve ? 10.0 : super.dickPreference();
	}
}
