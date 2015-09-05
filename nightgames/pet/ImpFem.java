package nightgames.pet;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.items.clothing.Clothing;
import nightgames.items.clothing.ClothingSlot;
import nightgames.items.clothing.ClothingTrait;
import nightgames.status.Horny;

public class ImpFem extends Pet {

	public ImpFem(Character owner) {
		super("Imp", owner, Ptype.impfem, 3, 2);
	}
	public ImpFem(Character owner,int power, int ac) {
		super("Imp", owner, Ptype.impfem, power, ac);
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
				if(target.crotchAvailable()&&!c.getStance().penetration(target)){
					c.write(owner(),own()+"imp grabs your dick and begins sucking it hungrily until you push her away.");
					target.body.pleasure(null, null, target.body.getRandom("cock"), 2+3*Global.random(power), c);					
				}
				else{
					c.write(owner(),own()+"imps stands at the periphery of the fight, touching herself idly.");
				}
				break;
			case 2:
				c.write(owner(),"While your attention is focused on "+owner().name()+", her imp creeps close to you and uppercuts you in the balls.");
				if(target.has(Trait.achilles)&&!target.has(ClothingTrait.armored)){
					target.pain(c, 3+Global.random(5), false);
				}
				target.pain(c, 4+3*Global.random(power), false);
				break;
			case 1:
				if(c.getStance().prone(target)){
					c.write(owner(),own()+"imp straddles your face, forcing her wet pussy onto your nose and mouth. Her scent is unnaturally intoxicating and fires up your libido.");
				}
				else{
					c.write(owner(),own()+"imp gets a running start and jumps higher than you thought possible, wrapping her legs around your head and pushing her soaked cunt into your " +
							"face. Her musky scent affects you more than it should, there must be a high concentration of pheromones in her juices.");
				}
				target.add(c, new Horny(target,3,3, "imp juices"));
				break;
			default:
				if(!target.mostlyNude()){
					Clothing article = target.getRandomStrippable();
					if((Global.random(25)>article.dc()+(target.getStamina().percent()-target.getArousal().percent())/4)||!target.canAct()){
						c.write(owner(),own()+"imp grabs your "+article.getName()+" and yanks it off.");
						target.strip(article, c);
					}
					else{
						c.write(owner(),own()+"imp pulls on your "+article.getName()+", accomplishing nothing except being slightly annoying.");
					}
				}
				else{
					c.write(owner(),own()+"imps stands at the periphery of the fight, touching herself idly.");
				}
			}
		}
		else{
			switch(Global.random(3)){
			case 2:
				c.write(owner(),"Your imp runs up to "+target.name()+" and punches her in the gut.");
				target.pain(c, 2+Global.random(power));
				break;
			case 1:
				c.write(owner(),"Your imp masturbates until her hand is coated in her own love juices, then flicks the fluid at "+target.name()+"'s face. You see her shiver slightly as the " +
						"pheromone filled juices take effect.");
				target.arouse(2+2*Global.random(power), c);
				break;
			default:
				if(!target.breastsAvailable()){
					if((Global.random(25)>target.getOutfit().getTopOfSlot(ClothingSlot.top).dc()+(target.getStamina().percent()-target.getArousal().percent())/4)||!target.canAct()){
						c.write(owner(),own()+"imp steals "+target.name()+"'s "+target.getOutfit().getTopOfSlot(ClothingSlot.top).getName()+" and runs off with it.");
						target.strip(ClothingSlot.top, c);
					}
					else{
						c.write(owner(),own()+"imp yanks on "+target.name()+"'s "+target.getOutfit().getTopOfSlot(ClothingSlot.top).getName()+" ineffectually.");
					}
				}
				else if(!target.crotchAvailable()){
					if((Global.random(25)>target.getOutfit().getTopOfSlot(ClothingSlot.bottom).dc()+(target.getStamina().percent()-target.getArousal().percent())/4)||!target.canAct()){
						c.write(owner(),own()+"imp steals "+target.name()+"'s "+target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName()+" and runs off with it.");
						target.strip(ClothingSlot.bottom, c);
					}
					else{
						c.write(owner(),own()+"imp yanks on "+target.name()+"'s "+target.getOutfit().getTopOfSlot(ClothingSlot.bottom).getName()+" ineffectually.");
					}
				}
				else{
					c.write(owner(),own()+"imps stands at the periphery of the fight, touching herself idly.");
				}
			}
		}
	}

	@Override
	public void vanquish(Combat c, Pet opponent) {
		switch(opponent.type()){
		case fairyfem:
			c.write(owner(),own()+"imp grabs "+opponent.own()+"faerie and inserts the tiny girl into her soaking cunt. She pulls the faerie out after only a few seconds, but " +
					"the sprite is completely covered with the imp's aphrodisiac wetness. The demon simply watches as the horny fae girl frantically masturbates in a sex " +
					"drunk daze. It doesn't take long until the faerie disappears with an orgasmic moan.");
			break;
		case fairymale:
			c.write(owner(),own()+"imp catches "+opponent.own()+"faerie boy and holds him in her palm. The imp uses one finger to toy with the fae's tiny penis and the little " +
					"male squirms helplessly. She dexterously rubs the faerie until it cums on her finger and vanishes with a flash.");
			break;
		case impfem:
			c.write(owner(),"The two female imps grapple with each other and "+opponent.own()+" imp throws "+own()+"imp to the floor. "+opponent.own()+" imp approaches to press her " +
					"advantage, but "+own()+"imp's tail suddenly thrusts into her pussy. As the tails fucks her, "+own()+"imp collects some of her own wetness and forces the " +
					"aphrodisiac filled fluid into the other female's mouth. "+opponent.own()+"imp's orgasmic moan is stifled and she vanishes in a puff of brimstone.");
			break;
		case impmale:
			c.write(owner(),own()+"imp grapples with "+opponent.own()+"imp and gets a hold of his erection. She uses her leverage to parade the male around the edge of the battle. " +
					"She strokes the demonic dick to the edge of orgasm and then mercilessly slams her knee into his balls. The male howls in pain and disappears.");
			break;
		case slime:
			c.write(owner(),own()+"imp shoves both her hands into "+opponent.own()+"slime. The slime trembles at her touch, encouraging her to wiggle her fingers more inside its " +
					"semi-solid body. The slime writhes more and more before it suddenly shudders, then slowly melts into a puddle.");
			break;
		}
		opponent.remove();
	}

	@Override
	public void caught(Combat c, Character captor) {
		if(owner().human()){
			c.write(captor,captor.name()+" grabs your imp and forces her to bend over. She thrusts two fingers into the little demon's pussy and pumps until she's overflowing with wetness. She " +
					"removes her fingers from the imp's lower lips and forces them into the creature's mouth. Your demon, affected by the aphrodisiacs in her own juices, spreads her legs " +
					"to "+captor.name()+" and makes a pleading sound. "+captor.name()+" rubs and pinches the imp's clit until she spasms and disappears.");
		}
		else if(captor.human()){
			c.write(captor,"You manage to catch "+own()+"imp by her tail and pull her off balance. The imp falls to the floor and you plant your foot on her wet box before she can " +
					"recover. You rub her slick folds with the sole of your foot and the demon writhes in pleasure, letting out incoherent whimpers. You locate her engorged clit " +
					"with your toes and rub it quickly to finish her off. The imp climaxes and vanishes, leaving no trace except the wetness on your foot.");
		}
		remove();
	}

	@Override
	public boolean hasDick() {
		return true;
	}
}
