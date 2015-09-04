package nightgames.pet;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.stance.StandingOver;
import nightgames.status.Falling;
import nightgames.status.Oiled;

public class Slime extends Pet {

	public Slime(Character owner) {
		super("Slime", owner, Ptype.slime, 3, 3);
	}
	public Slime(Character owner, int power, int ac) {
		super("Slime", owner, Ptype.slime, power, ac);
	}
	@Override
	public String describe() {
		return null;
	}

	@Override
	public void act(Combat c, Character target) {
		if(target.human()){
			switch(Global.random(4)){
			case 3:
				if(target.pantsless()&&!c.getStance().penetration(target)){
					c.write(owner(),own()+"slime forms into a humanoid shape and grabs your dick. The slime hand molds to your penis and rubs you with a slippery pleasure.");
					target.body.pleasure(null, null, target.body.getRandom("cock"), 2+3*Global.random(power), c);					
				}
				else{
					c.write(owner(),"You see eyes form in "+own()+" slime as it watches the fight curiously.");
				}
				break;
			case 2:
				if(!target.top.isEmpty()){
					c.write(owner(),own()+"slime forms into a shape that's vaguely human and clearly female. Somehow it manages to look cute and innocent while still being an animated blob of slime. " +
							"While you're processing this, the slime jumps on you and your "+target.top.peek().getName()+" dissolves under its touch.");
					target.shred(0);
				}
				else if(!target.bottom.isEmpty()){
					c.write(owner(),own()+"slime forms into a shape that's vaguely human and clearly female. Somehow it manages to look cute and innocent while still being an animated blob of slime. " +
							"While you're processing this, the slime jumps on you and your "+target.bottom.peek().getName()+" dissolves under its touch.");
					target.shred(1);
				}
				else{
					c.write(owner(),own()+"slime forms into a shape that's vaguely human and clearly female. Somehow it manages to look cute and innocent while still being an animated blob of slime. " +
							"The slime suddenly pounces on you and wraps itself around you. It doesn't seem to be attacking you as much as giving you a hug, but it leaves you covered in slimy " +
							"residue.");
					target.add(c, new Oiled(target));
				}
				break;
			case 1:
				if(!c.getStance().prone(target)){
					if(power*Global.random(20)>=target.knockdownDC()){
						c.write(owner(),own()+"slime wraps around your ankles and you are unable to keep your footing.");
						target.add(c, new Falling(target));
					}
					else{
						c.write(owner(),own()+"slime glomps onto your ankles. You almost lose your balance, but manage to recover.");
					}
				}
				else{
					c.write(owner(),"You see eyes form in "+own()+"slime as it watches the fight curiously.");
				}
				break;
			default:
				c.write(owner(),own()+"slime takes on a humanoid shape and watches you like a curious child.");
			}
		}
		else{
			switch(Global.random(4)){
			case 3:
				if(target.pantsless()&&!c.getStance().penetration(target)){
					c.write(owner(),"Two long appendages extend from your slime and wrap around "+target.name()+"'s legs. A third, phallic shaped appendage forms and penetrates her " +
							"pussy. She stifles a moan as the slimy tentacles thrusts in and out of her.");
					target.body.pleasure(null, null, target.body.getRandom("pussy"), 2+3*Global.random(power), c);					
				}
				else{
					c.write(owner(),"You see eyes form in "+own()+"slime as it watches the fight curiously.");
				}
				break;
			case 2:
				if(!target.top.isEmpty()){
					c.write(owner(),"Your slime pounces on "+target.name()+" playfully, and it's corrosive body melts her "+target.top.peek().getName()+" as a fortunate accident.");
					target.shred(0);
				}
				else if(!target.bottom.isEmpty()){
					c.write(owner(),"Your slime pounces on "+target.name()+" playfully, and it's corrosive body melts her "+target.bottom.peek().getName()+" as a fortunate accident.");
					target.shred(1);
				}			
				else{
					c.write(owner(),"You slime hugs "+target.name()+" affectionately, covering her in slimy liquid.");
					target.add(c, new Oiled(target));
				}
				break;
			case 1:
				if(!c.getStance().prone(target)){
					if(power*Global.random(20)>=target.knockdownDC()){
						c.write(owner(),target.name()+" slips on your slime as it clings to her feet, losing her balance.");
						target.add(c, new Falling(target));
					}
					else{
						c.write(owner(),target.name()+" stumbles as your slime clings to her leg. She manages to catch herself and scrapes off the clingly blob.");
					}
				}
				else{
					c.write(owner(),"You see eyes form in "+own()+"slime as it watches the fight curiously.");
				}
				break;
			default:
				c.write(owner(),"Your slime forms into a girlish shape and looks up at you, as if seeking approval.");
			}
		}
	}

	@Override
	public void vanquish(Combat c, Pet opponent) {
		switch(opponent.type()){
		case fairyfem:
			c.write(owner(),opponent.own()+"faerie flies over "+own()+"slime and begins casting a spells. Without warning, several appendages shoot out from the blob and snag " +
					"the faerie girl's limbs before she can escape. More appendages attach to her breasts and groin as the slime starts to vibrate. The faerie lets out a " +
					"high pitched moan and squirms against her bonds until she shudders in orgasm and vanishes.");
			break;
		case fairymale:
			c.write(owner(),opponent.own()+"faerie flies too close to "+own()+"slime and is suddenly engulfed up to his waist before he can react. He tries to free himself, but " +
					"groans as it starts to suck and massage his penis. He tries to push the slime off his groin, but it just sucks in his hands, leaving him completely " +
					"helpless until he ejaculates.");
			break;
		case impfem:
			c.write(owner(),own()+"slime gathers around "+opponent.own()+"imp's ankles. With unexpected speed, it surges up her legs and simultaneously penetrates her pussy and " +
					"ass. She screams in pleasure and falls to her knees as the amorphous blob fucks both her holes. By the time she climaxes and disappears, she's completely " +
					"fucked senseless.");
			break;
		case impmale:
			c.write(owner(),opponent.own()+"imp grabs for "+own()+"slime, but it leaps past his guard and covers his cock. The slime forms perfectly to the imp's dick and balls, milking " +
					"as much pre-cum as it can get. The imp tries to pull off the slime, but it acts as lubricant and the imp's attempts to remove it devolve into masturbation. The " +
					"imp demon ejaculates into the slime and disappears.");
			break;
		case slime:
			c.write(owner(),"The two slimes circle around each other, while gradually taking on human shape. One of the oozes looks vaguely like "+own()+"small slimy twin, while the other takes "
		+opponent.own()+"form. The two grapple and melt into each other so it's impossible to tell where one ends and the other begins. You can make out vaguely sexual shapes being formed " +
				"in the mix. Somehow you can tell that they're each trying to pleasure the other. Eventually the battle ends and a single humanoid shape forms from the amorphous mass, " +
				"revealing that "+own()+"slime was victorious.");
			break;
		}
		opponent.remove();
	}

	@Override
	public void caught(Combat c, Character captor) {
		if(owner().human()){
			c.write(captor,captor.name()+" seizes your slime and holds it near her groin. The ooze reacts to the closeness of her vagina and immediately forms a phallic appendage. She grabs the slimy " +
					"cock before it can penetrate her and strokes it quickly. With each stroke, the shape becomes more defined, until the slime has a perfectly human penis and a set of testicles. " +
					captor.name()+" speeds up her stokes and grabs the artifical balls with her free hand. The slime ejaculates its own fluid and melts into a puddle.");
		}
		else if(captor.human()){
			c.write(captor,"You manage to catch "+own()+"slime, but you're not sure what to do with it. It occurs to you that this thing is actively seeking sexual pleasure, so you push two fingers " +
					"into the mass and pump them back and forth. Soon a convincing replica of a vagina forms around your fingers and the entire slime gradually takes the shape of a woman. As " +
					"soon at the clit forms you focus attention on it. The slime climaxes just as its girlish shape finishes forming. Its realistic face shapes in a silent moan and looks very " +
					"content until it melts into a puddle of goo.");
		}
		remove();
	}
	@Override
	public boolean hasDick() {
		return true;
	}
	@Override
	public boolean hasPussy() {
		return true;
	}
}
