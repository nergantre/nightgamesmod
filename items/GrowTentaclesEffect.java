package items;

import global.Global;
import combat.Combat;
import characters.Character;
import characters.body.Body;
import characters.body.TentaclePart;
import status.Status;

public class GrowTentaclesEffect extends ItemEffect {
	int selfDuration;
	public GrowTentaclesEffect(String verb, String otherverb) {
		this(verb, otherverb, -1);
	}
	public GrowTentaclesEffect(String verb, String otherverb, int duration) {
		super(verb, otherverb, true, true);
		this.selfDuration = duration;
	}
	public boolean use(Combat c, Character user, Character opponent, Item item) {
		int duration = selfDuration >= 0 ? selfDuration : item.duration;
		TentaclePart part = TentaclePart.randomTentacle("tentacles", user.body, "tentacle-semen", 0, 1, 1);
		BodyModEffect effect = new BodyModEffect(getSelfVerb(), getOtherVerb(), part, BodyModEffect.Effect.growMultiple, duration);
		effect.use(null, user, opponent, item);
		c.write(part.describe(user) + " sprout from " + user.nameOrPossessivePronoun() + " " + part.attachpoint);
		return true;
	}
}