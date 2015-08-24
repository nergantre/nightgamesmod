package nightgames.skills;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.StraponPart;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.global.Global;

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
				&&target.pantsless();
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return fuckable(c, target)
				&&c.getStance().insert() != c.getStance()
				&&c.getStance().mobile(getSelf())
				&&!c.getStance().mobile(target)
				&&getSelf().canAct()
				&&!c.getStance().penetration(getSelf())
				&&!c.getStance().penetration(target);
	}

	@Override
	public boolean resolve(Combat c, Character target) {
		String premessage = "";
		if(!getSelf().bottom.empty() && getSelfOrgan().isType("cock")) {
			if (getSelf().bottom.size() == 1) {
				premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} down {self:possessive} %s halfway and ", getSelf().bottom.get(0).getName());
			} else if (getSelf().bottom.size() == 2) {
				premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} down {self:possessive} %s and %s halfway and ", getSelf().bottom.get(0).getName(), getSelf().bottom.get(1).getName());
			}
		} else if(!getSelf().bottom.empty() && getSelfOrgan().isType("pussy")) {
			if (getSelf().bottom.size() == 1) {
				premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} {self:possessive} %s to the side and ", getSelf().bottom.get(0).getName());
			} else if (getSelf().bottom.size() == 2) {
				premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} {self:possessive} %s and %s to the side and ", getSelf().bottom.get(0).getName(), getSelf().bottom.get(1).getName());
			}
		}
		premessage = Global.format(premessage, getSelf(), target);
		
		int m = 5+Global.random(5);
		BodyPart selfO = getSelfOrgan();
		BodyPart targetO = getTargetOrgan(target);
 		if(selfO.isReady(getSelf()) && targetO.isReady(target)){
			if(getSelf().human()){
				//c.offerImage("Fuck.jpg", "Art by Fujin Hitokiri");
				c.write(getSelf(),premessage + deal(c,m,Result.normal, target));
			}
			else if(target.human()){
				c.write(getSelf(),premessage + receive(c,m,Result.normal, target));
			}
			if (selfO.isType("pussy")) {
				c.setStance(c.getStance().insert());
			} else {
				c.setStance(c.getStance().insert());
			}
			int otherm = m;
			if (getSelf().has(Trait.insertion)) {
				otherm += 10;
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
	public boolean requirements(Character user) {
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
	public String describe() {
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
