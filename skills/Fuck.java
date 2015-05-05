package skills;

import global.Global;
import characters.Character;
import characters.Trait;
import characters.body.BodyPart;
import characters.body.StraponPart;

import combat.Combat;
import combat.Result;

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
		BodyPart res = self.body.getRandomCock();
		if (res == null && self.has(Trait.strapped)) {
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
		boolean ready = possible && selfO.isReady(self);

		return possible && ready
				&&self.clothingFuckable(selfO)
				&&target.pantsless();
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return fuckable(c, target)
				&&c.getStance().mobile(self)
				&&!c.getStance().mobile(target)
				&&self.canAct()
				&&!c.getStance().penetration(self)
				&&!c.getStance().penetration(target);
	}

	@Override
	public void resolve(Combat c, Character target) {
		String premessage = "";
		if(!self.bottom.empty() && getSelfOrgan().isType("cock")) {
			if (self.bottom.size() == 1) {
				premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} down {self:possessive} %s halfway and", self.bottom.get(0).name());
			} else if (self.bottom.size() == 2) {
				premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} down {self:possessive} %s and %s halfway and", self.bottom.get(0).name(), self.bottom.get(1).name());
			}
		} else if(!self.bottom.empty() && getSelfOrgan().isType("pussy")) {
			if (self.bottom.size() == 1) {
				premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} {self:possessive} %s to the side and", self.bottom.get(0).name());
			} else if (self.bottom.size() == 2) {
				premessage = String.format("{self:SUBJECT-ACTION:pull|pulls} {self:possessive} %s and %s to the side and", self.bottom.get(0).name(), self.bottom.get(1).name());
			}
		}
		premessage = Global.format(premessage, self, target);
		
		int m = 5+Global.random(5);
		BodyPart selfO = getSelfOrgan();
		BodyPart targetO = getTargetOrgan(target);
 		if(selfO.isReady(self) && targetO.isReady(target)){
			if(self.human()){
				c.offerImage("Fuck.jpg", "Art by Fujin Hitokiri");
				c.write(self,premessage + deal(c,m,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,premessage + receive(c,m,Result.normal, target));
			}
			if (selfO.isType("pussy")) {
				c.setStance(c.getStance().insert(self, target));
			} else {
				c.setStance(c.getStance().insert(self, self));
			}
			target.body.pleasure(self, selfO, targetO, m, c);
			self.body.pleasure(target, targetO, selfO, m, c);
			self.buildMojo(c, 25);
		} else {
			if(self.human()){
				c.write(self,premessage + deal(c,0,Result.miss, target));
			} else if(target.human()) {
				c.write(self,premessage + receive(c,0,Result.miss, target));
			}
		}
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
			if (!selfO.isReady(self) && !targetO.isReady(target))
				return "you're in a good position to fuck "+target.name()+", but neither of you are aroused enough to follow through.";
			else if(!getTargetOrgan(target).isReady(target)){
				return "you position your dick at the entrance to "+target.name()+", but find that she's not nearly wet enough to allow a comfortable insertion. You'll need " +
						"to arouse her more or you'll risk hurting her.";
			}
			else if (!selfO.isReady(self)){
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
			String message = self.name()+" rubs her dick against your wet snatch. She slowly but steadily pushes in, forcing " +
					"her length into your hot, wet pussy.";
			return message;
		}
		else if(modifier == Result.miss){
			if(!selfO.isReady(self) || !targetO.isReady(target) ){
				return self.name()+" grinds her privates against yours, but since neither of you are very turned on yet, it doesn't accomplish much.";
			}
			else if(!targetO.isReady(target)){
				return self.name()+" tries to push her cock inside your pussy, but you're not wet enough. You're simply not horny enough for " +
						"effective penetration yet.";
			}
			else{
				return self.name()+" tries to push her cock into your ready pussy, but she is still limp.";
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
}
