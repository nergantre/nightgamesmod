package pet;

import status.Flatfooted;
import status.Shield;
import global.Global;
import characters.Character;
import characters.Trait;

import combat.Combat;

public class FairyMale extends Pet {

	public FairyMale(Character owner) {
		super("faerie", owner, Ptype.fairymale, 2, 4);
	}
	public FairyMale(Character owner, int power, int ac) {
		super("faerie", owner, Ptype.fairymale, power, ac);
	}

	@Override
	public String describe() {
		return null;
	}

	@Override
	public void act(Combat c, Character target) {
		if(owner().human()){
			switch(Global.random(4)){
			case 3:
				if(target.pantsless()){
					c.write(owner(),"Your faerie flies between "+target.name()+"'s legs and rubs her sensitive clit with both his tiny hands.");
					target.body.pleasure(null, null, target.body.getRandom("pussy"), 2+3*Global.random(power), c);					
				}
				else{
					c.write(owner(),"Your faerie crawls into "+target.name()+"'s "+target.bottom.peek().getName()+" and fondles her until she fishes him out.");
					target.body.pleasure(null, null, target.body.getRandom("pussy"), 2+3*Global.random(power), c);					
				}
				break;
			case 2:
				c.write(owner(),"Your faerie flies in front of you and creates a magic barrier, reducing the physical damage you take.");
				owner().add(c, new Shield(owner(), .5));
				break;
			case 1:
				if(Global.random(3)==0){
					c.write(owner(),"Your faerie flies behind "+target.name()+" and creates a small flash of light, distracting her.");
					target.add(c, new Flatfooted(target,1));		
				}
				else{
					c.write(owner(),"Your faerie flies at "+target.name()+", but she swats him away.");
				}
				break;
			default:
				c.write(owner(),"Your faerie lands on your shoulder and casts a spell, restoring your vitality.");
				owner().heal(c, power+Global.random(10));
			}
		}
	}

	@Override
	public void vanquish(Combat c, Pet opponent) {
		switch(opponent.type()){
		case fairyfem:
			c.write(owner(),own()+ "faerie boy chases "+opponent.own()+"faerie and catches her from behind. He plays with the faerie girl's pussy and nipples while she's unable to retaliate. As she " +
					"orgasms, she vanishes with a sparkle.");
			break;
		case fairymale:
			c.write("");
			break;
		case impfem:
			c.write(owner(),own()+" faerie gets under "+opponent.own()+"imp's guard and punches her squarely in her comparatively large clitoris. The imp shrieks in pain and collapses before " +
					"vanishing.");
			break;
		case impmale:
			c.write("");
			break;
		case slime:
			c.write(owner(),own()+" Glows as her surrounds himself with magic before charging at "+opponent.own()+"slime like a tiny missile. The slime splashes more than it explodes, it's pieces " +
					"only shudder once before going still.");
			break;
		}
		opponent.remove();
	}

	@Override
	public void caught(Combat c, Character captor) {
		if(captor.human()){
			c.write(captor,"You snag "+own()+"faerie out of the air. She squirms in your hand, but has no chance of breaking free. You lick the fae from pussy to breasts and the little thing squeals " +
					"in pleasure. The taste is surprisingly sweet and makes your tongue tingle. You continue lapping up the flavor until she climaxes and disappears.");
		}
		else{
			c.write(captor,captor.name()+" snatches your faerie out of the air and flicks his little testicles with her finger. You wince in sympathy as the tiny male curls up in the fetal position " +
					"and vanishes.");
		}
		remove();
	}

	@Override
	public boolean hasDick() {
		return true;
	}

}
