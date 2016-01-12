package nightgames.characters;

import nightgames.characters.body.BreastsPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.GenericBodyPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TentaclePart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.Item;

public class Airi extends BasePersonality {
	/**
	 *
	 */
	private static final long serialVersionUID = -8169646189131720872L;

	public Airi() {
		super();
		character = new NPC("Airi", 10, this);
		character.change();
		character.setTrophy(Item.AiriTrophy);
		preferredCockMod = CockMod.slimy;

		character.set(Attribute.Power, 6);
		character.set(Attribute.Bio, 20);
		character.set(Attribute.Cunning, 15);
		character.set(Attribute.Speed, 4);
		character.set(Attribute.Seduction, 17);
		character.getStamina().setMax(80);
		character.getArousal().setMax(200);
		character.getMojo().setMax(60);
		character.getWillpower().setMax(500);
		character.add(Trait.dexterous);
		character.add(Trait.slime);
		character.add(Trait.imagination);
		character.add(Trait.softheart);
		character.add(Trait.alwaysready);

		character.plan = Plan.retreating;
		character.mood = Emotion.confident;
		character.body.add(BreastsPart.b);
		character.body.add(PussyPart.gooey);
		character.body.add(new GenericBodyPart("gooey skin", .5, 1.5, .8, "skin", ""));
		character.body.add(new TentaclePart("slime pseudopod", "back", "slime", 0.0, 1.0, 1.0));
		character.body.add(new TentaclePart("gooey feelers", "hands", "slime", 0.0, 1.0, 1.0));
		character.body.add(new TentaclePart("gooey feelers", "feet", "slime", 0.0, 1.0, 1.0));
		character.body.add(new TentaclePart("slime filaments", "pussy", "slime", 0.0, 1.0, 1.0));
		character.body.finishBody(CharacterSex.female);
	}

	@Override
	public void setGrowth() {
		growth.stamina = 1;
		growth.arousal = 2;
		growth.mojo = 2;
		growth.bonusStamina = 1;
		growth.bonusArousal = 1;
		growth.bonusMojo = 2;
		growth.addTrait(9, Trait.steady);
		growth.addTrait(12, Trait.lacedjuices);
		growth.addTrait(15, Trait.QuickRecovery);
		growth.addTrait(18, Trait.BoundlessEnergy);
		growth.addTrait(21, Trait.shameless);
		growth.addTrait(24, Trait.Sneaky);
		growth.addTrait(27, Trait.lactating);
		growth.addTrait(30, Trait.addictivefluids);
		growth.addTrait(33, Trait.autonomousPussy);
		growth.addTrait(36, Trait.entrallingjuices);
		growth.addTrait(39, Trait.energydrain);
		growth.addTrait(42, Trait.desensitized);
		growth.addTrait(45, Trait.vaginaltongue);
	}

	@Override
	public void rest(int time) {
		super.rest(time);
		Decider.visit(character);
		int r;
		for (int i = 0; i < time; i++) {
			r = Global.random(4);
			if (r == 1) {
				if (character.has(Trait.fitnessNut)) {
					character.getStamina().gain(1);
				}
				character.getStamina().gain(1);
			} else if (r == 3) {
				if (character.has(Trait.expertGoogler)) {
					character.getArousal().gain(2);
				}
				character.getArousal().gain(2);
			} else if (r == 2) {
				if (character.has(Trait.mojoMaster)) {
					character.getMojo().gain(2);
				}
				character.getMojo().gain(1);
			}
		}
	}

	@Override
	public String bbLiner(Combat c) {
		return "Airi grimaces as you fall. <i>\"Apologies... but necessary.... Please understand...\"</i>";
	}

	@Override
	public String nakedLiner(Combat c) {
		// always naked
		return "";
	}

	@Override
	public String stunLiner(Combat c) {
		return "Airi glares at you from the puddle she formed on the floor. <i>\"Unforgivable...\"</i>";
	}

	@Override
	public String taunt(Combat c) {
		return "Airi coos at you <i>\"About to cum..? ...even trying..?\"</i>";
	}

	@Override
	public String temptLiner(Combat c) {
		return "<i>\"Fill me with yourself... You will forget about everything...\"</i>";
	}

	@Override
	public String victory(Combat c, Result flag) {
		Character opponent;
		if (c.p1 == character) {
			opponent = c.p2;
		} else {
			opponent = c.p1;
		}
		character.arousal.empty();
		opponent.arousal.empty();
		return "Airi crawls over to you at an agonizing pace. Her slime rapidly flows on top of your penis and covers it in a sticky bulb. <i>\"Time… for you to cum…\"</i><br><br>"
				+ "Her previously still slime suddenly starts to frantically squeeze and knead your cock, pulsating in waves of sticky goo on top of you. Startled by the sudden stimulation, you barely manage to hold on. Unfortunately--or perhaps fortunately--for you, Airi is not finished. She also covers your chest with her own sticky breasts and engulfs your nipples inside hers. Although it’s just slime, you feel as if her lips are on your nipples, sucking them and rolling the tips around inside her mouth.<br><br>"
				+ "As you’re being overloaded with sensations, Airi brings her face close to yours and whispers in your ear.<i>\"Cum… cum… cum…\"<i> With a groan of agonising pleasure, you come hard, firing ropes of your seed inside her translucent depths.<br><br>"
				+ "Panting with exertion from the aftershocks of your orgasm, you see your cum floating around in her body quickly getting absorbed and disappearing into nothing. Sensing danger, you glance at Airi's face <i>\"...Not enough... I need more food...\"</i><br><br>"
				+ "This time Airi engulfs your whole body, leaving only your face outside, facing the sky. Try as you might, you can't even move your neck to see what's happening below. Feeling frightened at what she might do, you tense up your body to attempt to resist. <i>\"Are you... ready..? I'll begin...\"</i> Whatever you expected, it was not this. Her whole body begins to churn around your own, both massaging and licking every square inch of you. You feel a tendril of slime enter your ass and press against your prostate. At the same time two tendrils of slime enter your ears and attach themselves to something deep inside your head. In seconds, you feel Airi literally inside your head.<br><br>"
				+ "<i>\"Cum...\"</i> An orgasm like nothing you felt before tears through your body, searing your head until your vision turns white.<br><br>"
				+ "<i>\"Cum...\"</i> Another climax wracks you, suspending all your thoughts.<br><br>"
				+ "<i>\"Cum...\"</i> Your cum turns thin, flowing out like water.<br><br>"
				+ "<i>\"Give yourself... to me...\"</i> One final orgasm leaves you out cold. When you come to, you see Airi has left, taking your boxers like that. Wow, you're not sure how many more of these you can endure<br><br>";
	}

	@Override
	public String defeat(Combat c, Result flag) {
		return "Fighting Airi is not easy. Her stickiness makes it"
				+ " quite difficult for you to accomplish much of anything. Still, "
				+ "considering her incoherent babbling she's probably not got much fight left in her. "
				+ "In a desperate attempt, she launces herself at you, knocking you both to the ground."
				+ " You react quickly, rolling over and getting on top before she can stick you to the "
				+ "floor. Her slime crawls up your sides, seeking to engulf and immobilize you, but you "
				+ "raise yourself up slightly, out of her reach. You reach down with one hand and deftly"
				+ " get to work on the area of slime shaped like a pussy. It's very different from a "
				+ "regular girl's, considering that your fingers actually <i>sink in</i> rather than graze"
				+ " over, and that she is sticky enough to prevent them from moving very fast. Still, "
				+ "it's clearly having the desired effect. You redouble your efforts, even leaning in to "
				+ "kiss her, not caring about the effects her slime has when ingested. Before long, Airi's "
				+ "mumbling reaches a crescendo, and she wraps her gooey limbs around you. Her body loses"
				+ " shapes as she cums, as if it's melting around you. Within a few seconds you are lying "
				+ "half-embedded in an amorphous heap of slime. Worried for Airi's safety, you jump up and call her name."
				+ " Fortunately, it is not long before she reassumes her human form with a look of ecstasy still"
				+ " on her face. <i>\"Wonderful... Reward...\"</i> she mutters, even breathier than usual. "
				+ "She gently pulls you back down onto her. Since you've already won, you do not resist as Airi "
				+ "lets you sink in to her body slightly. Nor when every bit of her body starts vibrating against "
				+ "your skin. The full-body massage is wonderful, especially when it starts focussing more and more "
				+ "on your dick. It's somehow ended up in her pussy, which is milking you greedily. Seeing no reason"
				+ " to stop her, you just relax in her embrace and let her slowly drive you closer. Right before you "
				+ "cum, she kisses you again, pushing you over the edge. There is no frantic milking this time, just "
				+ "a great orgasm. When you have both recovered, you get up and she hands you a small bit of slime "
				+ "that she separated from her body. <i>\"Trophy... No clothes... Will grow back...\"</i> You take "
				+ "the slime from her, examining it before stashing it away. It tingles in your hand, and you "
				+ "wonder just how much control she still has over it as she glides away.";
	}

	@Override
	public String describe(Combat c) {
		return "A crystal blue figure stands in front of you. Well, \"stands\" might be an exaggeration. Airi sports a cute face, and a tight body, but her thighs end in a giant"
				+ "ball of slime. Indeed, while her body might look human at a distance, she seems to be composed of a soft translucent gel.";
	}

	@Override
	public String draw(Combat c, Result flag) {
		return "You make each other cum at the same time. Woohoo!";

	}

	@Override
	public boolean fightFlight(Character opponent) {
		return true;
	}

	@Override
	public boolean attack(Character opponent) {
		return true;
	}

	@Override
	public String victory3p(Combat c, Character target, Character assist) {
		if (target.human()) {
			return "Airi crawls over to you at an agonizing pace. Her slime rapidly flows on top of your penis and covers it in a sticky bulb. <i>\"Time… for you to cum…\"</i><br><br>"
					+ "Her previously still slime suddenly starts to frantically squeeze and knead your cock, pulsating in waves of sticky goo on top of you. Startled by the sudden stimulation, you cum in seconds, spilling the proof of your defeat inside her tendril<br><br>";
		} else {
			return "Airi flows over to " + target.name()
					+ ". Her slime pools into a long and flexible appendage and worms itself inside "
					+ target.possessivePronoun()
					+ " depths. The appendage starts to twist and squirm inside her poor victim and almost instantly causes her victim to scream out in pleasure.<br><br>";
		}
	}

	@Override
	public String intervene3p(Combat c, Character target, Character assist) {
		if (target.human()) {
			return Global.format(
					"Your fight with {other:name} seemed to have ran into a stalemate. Neither of you is willing to get close enough to each other for anything substantial to happen. You just continue to trade taunt while waiting for an opportunity.<br><br>"
							+ "Suddenly, you feel something grasp your ankles and pull you off balance. You brace yourself for the fall, but after a second, you only feel softness on your back. It’s Airi. She somehow snuck up on you and tripped you into falling on top of her. She quickly engulfs your hands and legs in her slime and presents your helpless body to {other:name}’s ministrations.",
					character, assist);
		} else {
			return Global.format(
					"Your fight with {other:name} seemed to have ran into a stalemate. Neither of you is willing to get close enough to each other for anything substantial to happen. You just continue to trade taunt while waiting for an opportunity."
							+ "Suddenly, a blue blob appears in your line of sight. It’s Airi! More swiftly than you would expect, Airi moves to {other:name}’s side and engulfs her body in her own. After dissolving her clothing with her slime, Airi surfaces only {other:name-possessive} torso and sex, presenting her to you. Well, presented with a gift on a silver platter, you’re not going to refuse!",
					character, target);
		}
	}

	@Override
	public String startBattle(Character other) {
		return "Airi's main body rises up from her slime blob and forms the demure beauty you're used to seeing. <i>\"Delicious... Quickly... Give me your seed...\"</i>";
	}

	@Override
	public boolean fit() {
		return !character.mostlyNude() && character.getStamina().percent() >= 50
				|| character.getArousal().percent() > 50;
	}

	@Override
	public String night() {
		return "You walk back to your dorm after the match, and decide to take a shower after all that exertion. Who knew sex fighting a bunch of girls would be so exhausting? You strip off your shirt and boxers and head straight into the bathroom. As you flip on the lights, you notice that the tub seems already filled with water. Just as you wonder if you forgot to drain the tub from last night, the liquid in the tub quivers and… stands up. "
				+ "<br><br>It’s Airi. What’s she doing here? You ask her how did get in, since you were sure the door was locked. <i>Followed you… flowed under door… No problem…</i> Well, that explains it. Noticing the time, you let her know that you really need to take your shower now and head to bed or you won’t make it tomorrow for your morning classes. Airi looks at you for a second and nods. <i>Un… will help you clean…</i> Wait what? Oh n-! Airi pulls you into the tub with her gooey appendages and submerges you inside her body. <i>Relax… I’ll clean you up… Inside and out…</i>";

	}

	public void advance() {

	}

	@Override
	public boolean checkMood(Combat c, Emotion mood, int value) {
		switch (mood) {
		case confident:
			return value >= 50;
		case desperate:
			return value >= 150;
		default:
			return value >= 100;
		}
	}

	@Override
	public String orgasmLiner(Combat c) {
		return "<i>\"Ahhnn... forgot how good... feels... Will return favor...\"</i>.";
	}

	@Override
	public String makeOrgasmLiner(Combat c) {
		return "<i>\"...Feels good..? I'll suck more out... I'll drain you dry...\"</i>";
	}
}
