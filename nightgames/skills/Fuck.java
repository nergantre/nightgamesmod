package nightgames.skills;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.StraponPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;

public class Fuck extends Skill {

	public Fuck(String name, Character self, int cooldown) {
		super(name, self, cooldown);
	}

	public Fuck(Character self) {
		super("Fuck", self);
		if(self.human()){
			image="Fuck.jpg";
			artist="Art by Fujin Hitokiri";
		}
	}

	public BodyPart getSelfOrgan() {
		BodyPart res = getSelf().body.getRandomCock();
		if (res == null && getSelf().has(Trait.strapped)) {
			res = StraponPart.generic;
		}
		return res;
	}

	public BodyPart getTargetOrgan(Character target) {
		return target.body.getRandomPussy();
	}
	
	public boolean fuckable(Combat c, Character target) {
		BodyPart selfO = getSelfOrgan();
		BodyPart targetO = getTargetOrgan(target);
		boolean possible = selfO != null && targetO != null;
		boolean ready = possible && selfO.isReady(getSelf());

		return possible && ready
				&&getSelf().clothingFuckable(selfO)
				&&target.crotchAvailable();
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return fuckable(c, target)
				&& (c.getStance().insert(getSelf(), getSelf()) != c.getStance() || c.getStance().insert(target, getSelf()) != c.getStance())
				&&c.getStance().mobile(getSelf())
				&&!c.getStance().mobile(target)
				&&getSelf().canAct()
				&&!c.getStance().penetration(getSelf())
				&&!c.getStance().penetration(target);
	}
	
	String premessage(Combat c, Character target) {
		String premessage = "";
		Clothing underwear = getSelf().getOutfit().getSlotAt(ClothingSlot.bottom, 0);
		Clothing bottom = getSelf().getOutfit().getSlotAt(ClothingSlot.bottom, 1);
		String bottomMessage;

		if (underwear != null && bottom != null) {
			bottomMessage = underwear.getName() + " and " + bottom.getName();
		} else if (underwear != null) {
			bottomMessage = underwear.getName();
		} else if (bottom != null) {
			bottomMessage = bottom.getName();
		} else {
			bottomMessage = "";
		}

		if(!bottomMessage.isEmpty() && getSelfOrgan().isType("cock")) {
			premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} down {self:possessive} %s halfway and ", bottomMessage);
		} else if(!bottomMessage.isEmpty() && getSelfOrgan().isType("pussy")) {
			premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} {self:possessive} %s to the side and ", bottomMessage);
		}
		return Global.format(premessage, getSelf(), target);
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		String premessage = premessage(c, target);
		int m = 5+Global.random(5);
		BodyPart selfO = getSelfOrgan();
		BodyPart targetO = getTargetOrgan(target);
 		if(selfO.isReady(getSelf()) && targetO.isReady(target)){
			if(getSelf().human()){
				c.write(getSelf(),premessage + deal(c,m,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),premessage + receive(c,m,Result.normal, target));
			}
			if (selfO.isType("pussy")) {
				c.setStance(c.getStance().insert(target, getSelf()));
			} else {
				c.setStance(c.getStance().insert(getSelf(), getSelf()));
			}
			int otherm = m;
			if (getSelf().has(Trait.insertion)) {
				otherm += Math.min(getSelf().get(Attribute.Seduction) / 4, 40);
			}
			target.body.pleasure(getSelf(), selfO, targetO, m, c);
			getSelf().body.pleasure(target, targetO, selfO, otherm, c);
		} else {
			if(getSelf().human()){
				c.write(getSelf(),premessage + deal(c,0,Result.miss, target));
			} else if(target.human()) {
				c.write(getSelf(),premessage + receive(c,0,Result.miss, target));
			}
			return false;
		}
		return true;
	}

	@Override
	public boolean requirements(Combat c, Character user, Character target) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Fuck(user);
	}
	public int speed(){
		return 2;
	}
	public Tactics type(Combat c) {
		return Tactics.fucking;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		BodyPart selfO = getSelfOrgan();
		BodyPart targetO = getTargetOrgan(target);
		if(modifier == Result.normal){
			return "you rub the head of your dick around "+target.name()+"'s entrance, causing her to shiver with anticipation. Once you're sufficiently lubricated " +
					"with her wetness, you thrust into her " + target.body.getRandomPussy().describe(target) + ". "+target.name()+" tries to stifle her pleasured moan as you fill her in an instant.";
		}
		else if(modifier == Result.miss){
			if (!selfO.isReady(getSelf()) && !targetO.isReady(target))
				return "you're in a good position to fuck "+target.name()+", but neither of you are aroused enough to follow through.";
			else if(!getTargetOrgan(target).isReady(target)){
				return "you position your dick at the entrance to "+target.name()+", but find that she's not nearly wet enough to allow a comfortable insertion. You'll need " +
						"to arouse her more or you'll risk hurting her.";
			}
			else if (!selfO.isReady(getSelf())){
				return "you're ready and willing to claim "+target.name()+"'s eager " + target.body.getRandomPussy().describe(target) + ", but your shriveled dick isn't cooperating. Maybe your self-control training has become " +
						"too effective.";
			}
			return "you managed to miss the mark.";
		}
		return "Bad stuff happened";
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		BodyPart selfO = getSelfOrgan();
		BodyPart targetO = getTargetOrgan(target);
		if(modifier == Result.normal){
			String message = getSelf().name()+" rubs her dick against your wet snatch. She slowly but steadily pushes in, forcing " +
					"her length into your hot, wet pussy.";
			return message;
		}
		else if(modifier == Result.miss){
			if(!selfO.isReady(getSelf()) || !targetO.isReady(target) ){
				return getSelf().name()+" grinds her privates against yours, but since neither of you are very turned on yet, it doesn't accomplish much.";
			}
			else if(!targetO.isReady(target)){
				return getSelf().name()+" tries to push her cock inside your pussy, but you're not wet enough. You're simply not horny enough for " +
						"effective penetration yet.";
			}
			else{
				return getSelf().name()+" tries to push her cock into your ready pussy, but she is still limp.";
			}
		}
		return "Bad stuff happened";
	}

	@Override
	public String describe(Combat c) {
		return "Penetrate your opponent, switching to a sex position";
	}
	
	@Override
	public boolean makesContact() {
		return true;
	}
	public String getTargetOrganType(Combat c, Character target) {
		return getTargetOrgan(target).getType();
	}
	public String getWithOrganType(Combat c, Character target) {
		return getSelfOrgan().getType();
	}
}
