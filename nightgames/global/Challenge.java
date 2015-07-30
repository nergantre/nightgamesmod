package nightgames.global;
import nightgames.areas.Deployable;
import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.combat.Combat;
import nightgames.combat.Result;
import nightgames.items.Item;

import java.util.ArrayList;

public class Challenge implements Deployable{
	private Character owner;
	private Character target;
	private GOAL goal;
	public boolean done;
	public Challenge(){
		done=false;
	}
	public GOAL pick(){
		ArrayList<GOAL> available = new ArrayList<GOAL>();
		if(!target.topless()&&!target.pantsless()){
			available.add(GOAL.clothedwin);
		}
		if(owner.getPure(Attribute.Seduction)>=9){
			available.add(GOAL.analwin);
		}
		if(owner.getAffection(target)>=10){
			available.add(GOAL.kisswin);
			available.add(GOAL.pendraw);
		}
		if(target.has(Item.Strapon)||target.has(Item.Strapon2)||target.hasDick()){
			available.add(GOAL.peggedloss);
		}
		available.add(GOAL.pendomwin);
		available.add(GOAL.subwin);
		return available.get(Global.random(available.size()));
	}
	public String message(){
		switch(goal){
		case kisswin:
			return target.name()+" seems pretty head over heels for you, at least to my eyes. I bet she'll climax if you give her a good kiss. Give it a try.";
		case clothedwin:
			return "Not everyone relies on brute force to get their opponents off. The masters of seduction often don't bother to even undress their opponents. See " +
					"if you can make "+target.name()+" cum while she's still got her clothes on.";
		case bathambush:
			return "";
		case peggedloss:
			return "Getting pegged in the ass is a hell of a thing, isn't it. I sympathize... especially since "+target.name()+" seems to have it in for you tonight. If it " +
					"happens, I'll see that you're compensated.";
		case analwin:
			return target.name()+" has been acting pretty cocky lately. If you can make her cum while fucking her in the ass, she should learn some humility.";
		case pendomwin:
			return "How good are you exactly? If you want to show "+target.name()+" that you're the best, make her cum while giving her a good fucking.";
		case pendraw:
			return "Some things are better than winning, like cumming together with your sweetheart. You and "+target.name()+" seem pretty sweet.";
		case subwin:
			return "Everyone loves an underdog. If you can win a fight with "+target.name()+" when she thinks you're at her mercy, you'll get a sizeable bonus.";
		default:
			return "";
		}
	}
	private enum GOAL{
		kisswin,
		clothedwin,
		bathambush,
		peggedloss,
		analwin,
		pendomwin,
		pendraw,
		subwin
	}
	public void check(Combat state, Character victor){
		if(!done&&(state.p1==target||state.p2==target||target==null)){
			switch(goal){
			case kisswin:
				if(victor==owner&&state.lastact(owner).toString().equals("Kiss")){
					done = true;
				}
				break;
			case clothedwin:
				if(victor==owner&&!target.topless()&&!target.pantsless()){
					done = true;
				}
				break;
			case bathambush:
				break;
			case peggedloss:
				if(target==victor&&state.state==Result.anal){
					done = true;
				}
				break;
			case analwin:
				if(owner==victor&&state.state==Result.anal){
					done = true;
				}
				break;
			case pendomwin:
				if(target==victor&&state.state==Result.intercourse){
					done = true;
				}
				break;
			case pendraw:
				if(victor==null&&state.state==Result.intercourse){
					done = true;
				}
				break;
			case subwin:
				if(victor==owner&&state.getStance().sub(owner)){
					done = true;
				}
				break;
			}
		}
	}
	@Override
	public void resolve(Character active) {
		if(active.state==State.ready){
			this.owner=active;
			target = Global.getMatch().combatants.get(Global.random(Global.getMatch().combatants.size()-1));
			for (int i = 0; i <10 && target==active; i++){
				target = Global.getMatch().combatants.get(Global.random(Global.getMatch().combatants.size()-1));
			}
			if (target == active) {return;}
			goal=pick();
			if(active.human()){
				Global.gui().message("You find a gold envelope sitting conspicously in the middle of the room. You open it up and read the note inside.\n'"+message()+"'\n");
			}	
			active.location().remove(this);
			active.accept(this);
		}
	}
	@Override
	public Character owner() {
		return null;
	}
	public int reward(){
		switch(goal){
		case kisswin:
		case clothedwin:
			return 250;
		case peggedloss:
			return 1000;
		case pendomwin:
			return 300;
		default:
			return 500;
		}
	}
}
