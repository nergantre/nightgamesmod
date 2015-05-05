package items;

import global.Global;
import status.Status;
import characters.Character;
import characters.body.BodyPart;

import combat.Combat;

public class BodyModEffect extends ItemEffect {
	private BodyPart affected;
	private Effect effect;
	private int selfDuration;

	public enum Effect {
		upgrade,
		downgrade,
		replace,
		grow,
		growMultiple,
		growplus,
	}

	public BodyModEffect(String selfverb, String otherverb, BodyPart affected, Effect effect) {
		this(selfverb, otherverb, affected, effect, -1);
	}

	public BodyModEffect(String selfverb, String otherverb, BodyPart affected, Effect effect, int duration) {
		super(selfverb, otherverb, true, true);
		this.affected = affected;
		this.effect = effect;
		this.selfDuration = duration;
	}

	public boolean use(Combat c, Character user, Character opponent, Item item) {
		BodyPart original = user.body.getRandom(affected.getType());
		int duration = selfDuration >= 0 ? selfDuration : item.duration;
		String message = "";
		switch (effect) {
			case upgrade:
				if (original != null) {
					BodyPart newPart = original.upgrade();
					if (newPart == original) {
						boolean eventful = user.body.temporaryAddOrReplacePartWithType(newPart, original, duration);
						message = eventful ? Global.format(String.format("{self:NAME-POSSESSIVE} %s was reenforced", original.fullDescribe(user)), user, opponent) : "";
					} else {
						user.body.temporaryAddOrReplacePartWithType(newPart, original, duration);
						message = Global.format(String.format("{self:NAME-POSSESSIVE} %s grew into %s%s", original.fullDescribe(user), newPart.prefix(), newPart.fullDescribe(user)), user, opponent);
					}
				} else {
					message = "";
				}
				break;
			case downgrade:
				if (original != null) {
					BodyPart newPart = original.downgrade();
					if (newPart == original) {
						boolean eventful = user.body.temporaryAddOrReplacePartWithType(newPart, original, duration);
						message = eventful ? Global.format(String.format("{self:NAME-POSSESSIVE} %s was reenforced", original.fullDescribe(user)), user, opponent) : "";
					} else {
						user.body.temporaryAddOrReplacePartWithType(newPart, original, duration);
						message = Global.format(String.format("{self:NAME-POSSESSIVE} %s shrunk into %s%s", original.fullDescribe(user), newPart.prefix(), newPart.fullDescribe(user)), user, opponent);
					}
				} else {
					message = "";
				}
				break;
			case replace:
				if (original == affected) {
					boolean eventful = user.body.temporaryAddOrReplacePartWithType(affected, original, duration);
					message = eventful ? Global.format(String.format("{self:NAME-POSSESSIVE} %s was reenforced", original.fullDescribe(user)), user, opponent) : "";
				} else if (original != null) {
					user.body.temporaryAddOrReplacePartWithType(affected, original, duration);
					message = Global.format(String.format("{self:NAME-POSSESSIVE} %s turned into %s%s", original.fullDescribe(user), affected.prefix(), affected.fullDescribe(user)), user, opponent);
				} else {
					user.body.temporaryAddPart(affected, duration);
					message = Global.format(String.format("{self:SUBJECT} grew %s%s", affected.prefix(), affected.fullDescribe(user)), user, opponent);
				}
				break;
			case grow:
				if (original == affected) {
					message = "";
				} else if (original == null) {
					user.body.temporaryAddPart(affected, duration);
					message = Global.format(String.format("{self:SUBJECT} grew %s%s", affected.prefix(), affected.fullDescribe(user)), user, opponent);
				} else {
					message = "";
				}
				break;
			case growMultiple:
				user.body.temporaryAddPart(affected, duration);
				message = Global.format(String.format("{self:SUBJECT} grew %s%s", affected.prefix(), affected.fullDescribe(user)), user, opponent);
				break;
			case growplus:
				if (original == null) {
					user.body.temporaryAddPart(affected, duration);
					message = Global.format(String.format("{self:SUBJECT} grew %s%s", affected.prefix(), affected.fullDescribe(user)), user, opponent);
				} else {
					BodyPart newPart;
					if (affected.compare(original) <= 0) {
						newPart = original.upgrade();
					} else {
						newPart = affected;
					}
					if (newPart == original) {
						boolean eventful = user.body.temporaryAddOrReplacePartWithType(newPart, original, duration);
						message = eventful ? Global.format(String.format("{self:NAME-POSSESSIVE} %s was reenforced", original.fullDescribe(user)), user, opponent) : "";
					} else {
						user.body.temporaryAddOrReplacePartWithType(newPart, original, duration);
						message = Global.format(String.format("{self:NAME-POSSESSIVE} %s grew into %s%s", original.fullDescribe(user), newPart.prefix(), newPart.fullDescribe(user)), user, opponent);
					}
				}
			default:
		}
		if (c != null && !message.isEmpty()) {
			c.write(message);
		}
		return !message.isEmpty();
	}
}