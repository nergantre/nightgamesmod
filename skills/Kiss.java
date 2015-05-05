package skills;

import global.Global;
import characters.Attribute;
import characters.Character;
import characters.Trait;

import combat.Combat;
import combat.Result;

public class Kiss extends Skill {

	public Kiss(Character self) {
		super("Kiss", self);
	}

	@Override
	public boolean usable(Combat c, Character target) {
		return c.getStance().kiss(self)&&self.canAct();
	}

	@Override
	public void resolve(Combat c, Character target) {
		int m = 2+Global.random(2);
		if(self.has(Trait.romantic)){
			m += 3;
		}
		if(self.has(Trait.experttongue)){
			m += 5;
			if(self.human()){
				c.write(self,deal(c,m,Result.special, target));
			}
			else if(target.human()){
				c.write(self,receive(c,m,Result.special, target));
			}
			target.body.pleasure(self, self.body.getRandom("mouth"), target.body.getRandom("mouth"), m, c);
			self.body.pleasure(target, target.body.getRandom("mouth"), self.body.getRandom("mouth"), 1, c);
			self.buildMojo(c, 10);
		}
		else if(self.get(Attribute.Seduction)>=9){
			m += 2 + Global.random(2);
			if(self.human()){
				c.write(self,deal(c,m,Result.normal, target));
			}
			else if(target.human()){
				c.write(self,receive(c,m,Result.normal, target));
			}
			target.body.pleasure(self, self.body.getRandom("mouth"), target.body.getRandom("mouth"), m, c);
			self.body.pleasure(target, target.body.getRandom("mouth"), self.body.getRandom("mouth"), 1, c);
			self.buildMojo(c, 10);
		}
		else{
			if(self.human()){
				c.write(self,deal(c,m,Result.weak, target));
			}
			else if(target.human()){
				c.write(self,receive(c,m,Result.weak, target));
			}
			target.body.pleasure(self, self.body.getRandom("mouth"), target.body.getRandom("mouth"), m, c);
			self.body.pleasure(target, target.body.getRandom("mouth"), self.body.getRandom("mouth"), 1, c);
			self.buildMojo(c, 5);
		}
	}

	@Override
	public boolean requirements(Character user) {
		return true;
	}

	@Override
	public Skill copy(Character user) {
		return new Kiss(user);
	}
	public int speed(){
		return 6;
	}
	public Tactics type(Combat c) {
		return Tactics.pleasure;
	}

	@Override
	public String deal(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.special){
			return "You pull "+target.name()+" to you and kiss her passionately. You run your tongue over her lips until her opens them and immediately invade her mouth. " +
					"You tangle your tongue around hers and probe the sensitive insides her mouth. As you finally break the kiss, she leans against you, looking kiss-drunk and needy.";
		}
		else if(modifier==Result.weak){
			return "You aggressively kiss "+target.name()+" on the lips. It catches her off guard for a moment, but she soon responds approvingly.";
		}
		else{
			switch(Global.random(4)){
			case 0: return "You pull "+target.name()+" close and capture her lips. She returns the kiss enthusiastically and lets out a soft noise of approval when you " +
					"push your tongue into her mouth.";
			case 1: return "You press your lips to "+target.name()+"'s in a romantic kiss. You tease out her tongue and meet it with your own.";
			case 2: return "You kiss "+target.name()+" deeply, overwhelming her senses and swapping quite a bit of saliva.";
			default: return "You steal a quick kiss from "+target.name()+", pulling back before she can respond. As she hesitates in confusion, you kiss her twice more, "+
						"lingering on the last to run your tongue over her lips.";
			}
			
		}
	}

	@Override
	public String receive(Combat c, int damage, Result modifier, Character target) {
		if(modifier==Result.special){
			return self.name()+" seductively pulls you into a deep kiss. As first you try to match her enthusiastic tongue with your own, but you're quickly overwhelmed. She draws " +
					"your tongue into her mouth and sucks on it in a way that seems to fill your mind with a pleasant, but intoxicating fog.";
		}
		else if(modifier==Result.weak){
			return self.name()+" presses her lips against yours in a passionate, if not particularly skillful, kiss.";
		}
		else{
			switch(Global.random(3)){
			case 0: return self.name()+" grabs you and kisses you passionately on the mouth. As you break for air, she gently nibbles on your bottom lip.";
			case 1: return self.name()+" peppers quick little kisses around your mouth before suddenly taking your lips forcefully and invading your mouth with her tongue.";
			default: return self.name()+" kisses you softly and romantically, slowy drawing you into her embrace. As you part, she teasingly brushes her lips against yours.";
			}
		}
	}

	@Override
	public String describe() {
		return "Kiss your opponent";
	}
	@Override
	public boolean makesContact() {
		return true;
	}
}
