package combat;
import global.DebugFlags;
import global.Flag;
import global.Global;

import items.Clothing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import pet.Pet;

import skills.Skill;
import skills.Stunned;
import stance.Mount;
import stance.Neutral;
import stance.Position;
import stance.Stance;
import stance.StandingOver;
import status.Braced;
import status.CounterStatus;
import status.Flatfooted;
import status.Horny;
import status.Status;
import status.Stsflag;
import status.Wary;
import status.Winded;

import areas.Area;

import characters.Attribute;
import characters.Character;
import characters.Emotion;
import characters.NPC;
import characters.State;
import characters.Trait;


public class Combat extends Observable implements Serializable, Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8279523341570263846L;
	public Character p1;
	public Character p2;
	public int phase;
	private Skill p1act;
	private Skill p2act;
	public Area location;
	private String message;
	private Position stance;
	public Character lastTalked;
	public ArrayList<Clothing> clothespile;
	private int timer;
	public Result state;
	private HashMap<String,String> images;
	
	public Combat(Character p1, Character p2, Area loc){
		this.p1=p1;
		this.p2=p2;
		location=loc;
		stance = new Neutral(p1,p2);
		message = "";
		clothespile=new ArrayList<Clothing>();
		timer=0;
		images = new HashMap<String,String>();
		p1.state=State.combat;
		p2.state=State.combat;
	}
	public Combat(Character p1, Character p2, Area loc, Position starting){
		this(p1,p2,loc);
		stance = starting;
	}
	public Combat(Character p1, Character p2, Area loc,int code){
		this(p1,p2,loc);
		stance = new Neutral(p1,p2);
		message = "";
		clothespile=new ArrayList<Clothing>();
		timer=0;
		switch(code){
			case 1:
				p2.undress(this);
				p1.emote(Emotion.dominant, 50);
				p2.emote(Emotion.nervous, 50);
			default:
		}
		p1.state=State.combat;
		p2.state=State.combat;
	}
	public void go(){
		phase=0;
		if(p1.nude() && !p2.nude()){
			p1.emote(Emotion.nervous, 20);
		}
		if(p2.nude() && !p1.nude()){
			p2.emote(Emotion.nervous, 20);
		}
		if(!(p1.human()||p2.human())){
			automate();
		}
		this.setChanged();
		this.notifyObservers();
	}
	public void turn(){
		Result state;
		if(p1.getArousal().isFull()&&p2.getArousal().isFull()){
			state = eval();
			p1.evalChallenges(this, null);
			p2.evalChallenges(this, null);
			p2.draw(this,state);
			phase=2;
			this.setChanged();
			this.notifyObservers();
			if(!(p1.human()||p2.human())){
				end();
			}
			return;
		}
		if(p1.getArousal().isFull()){
			state = eval();
			p1.evalChallenges(this, p2);
			p2.evalChallenges(this, p2);
			p2.victory(this,state);
			phase=2;
			this.setChanged();
			this.notifyObservers();
			if(!(p1.human()||p2.human())){
				end();
			}
			return;
		}
		if(p2.getArousal().isFull()){
			state = eval();
			p1.evalChallenges(this, p1);
			p2.evalChallenges(this, p1);
			p1.victory(this,state);
			phase=2;
			this.setChanged();
			this.notifyObservers();
			if(!(p1.human()||p2.human())){
				end();
			}
			return;
		}
		if(!p1.human()&&!p2.human()&&timer>15){
			if(p1.getArousal().get()>p2.getArousal().get()){
				state = eval();
				if(Global.isDebugOn(DebugFlags.DEBUG_SCENE)){
					System.out.println(p2.name()+" victory over "+p1.name());
				}
				p2.victory(this,state);
				phase=2;
				this.setChanged();
				this.notifyObservers();
				if(!(p1.human()||p2.human())){
					end();
				}
				return;
			}
			else if(p1.getArousal().get()<p2.getArousal().get()){
				state = eval();
				if(Global.isDebugOn(DebugFlags.DEBUG_SCENE)){
					System.out.println(p1.name()+" victory over "+p2.name());
				}
				p1.victory(this,state);
				phase=2;
				this.setChanged();
				this.notifyObservers();
				if(!(p2.human()||p1.human())){
					end();
				}
				return;
			}
			else{
				state = eval();
				if(Global.isDebugOn(DebugFlags.DEBUG_SCENE)){
					System.out.println(p2.name()+" draw with "+p1.name());
				}
				p2.draw(this,state);
				phase=2;
				this.setChanged();
				this.notifyObservers();
				if(!(p1.human()||p2.human())){
					end();
				}
				return;
			}
		}
		Character player;
		Character other;
		if (p1.human()) {
			player = p1;
			other = p2;
		} else {
			player = p2;
			other = p1;
		}
		message = other.describe(player.get(Attribute.Perception))+"<p>"+getStance().describe()+"<p>"+player.describe(other.get(Attribute.Perception))+"<p>";
		phase=1;
		p1.regen(this);
		p2.regen(this);
		p1act=null;
		p2act=null;
		//if(p1.stunned()){
		//	p1act=new Stunned(p1);
		//}
		//else{
			p1.act(this);
		//}
		//if(p2.stunned()){
		//	p2act=new Stunned(p2);
		//}
		//else{
			
			p2.act(this);
		//}
		this.setChanged();
		this.notifyObservers();
	}
	
	private Result eval() {
		if(getStance().bottom.human() && getStance().en == Stance.anal) {
			return Result.anal;
		}
		else if(getStance().penetration(p1)||getStance().penetration(p2)){
			return Result.intercourse;
		}
		else{
			return Result.normal;
		}
	}
	public void act(Character c, Skill action){
		if(c==p1){
			p1act=action;
		}
		if(c==p2){
			p2act=action;
		}
		if(p1act!=null&&p2act!=null){
			clear();
			if (p1.human() || p2.human()) {
				Global.gui().clearText();
			}
			String imagePath = "";
			String artist = "";
			if (images != null)
				images.clear();
			if(Global.isDebugOn(DebugFlags.DEBUG_SCENE)){
				System.out.println(p1.name()+" uses "+p1act.toString());
				System.out.println(p2.name()+" uses "+p2act.toString());
			}
			if(p1.pet!=null&&p2.pet!=null){
				petbattle(p1.pet,p2.pet);
			}
			else if(p1.pet!=null){
				p1.pet.act(this, p2);
			}
			else if(p2.pet!=null){
				p2.pet.act(this, p1);
			}
			useSkills();
			if((p1.human()||p2.human())&&!Global.checkFlag(Flag.noimage)){
				if(!images.isEmpty()){
					imagePath = images.keySet().toArray(new String[images.size()])[Global.random(images.size())];
				}
				if(imagePath!=""){
					Global.gui().displayImage(imagePath,images.get(imagePath));
				}else{
					Global.gui().clearImage();
				}
			}
			p1.eot(this,p2,p2act);
			p2.eot(this,p1,p1act);
			getStance().decay();
			getStance().checkOngoing(this);
			this.phase=0;
			if(!(p1.human()||p2.human())){
				timer++;
				turn();
			}
			this.setChanged();
			this.notifyObservers();
		}
		
	}
	public void automate(){
		int turn = 0;
		while(!(p1.getArousal().isFull()||p2.getArousal().isFull())){
			// guarantee the fight finishes in a timely manner
			if (turn > 50) {
				p1.pleasure(turn - 50, null);
				p1.pleasure(turn - 50, null);
			}
			turn += 1;
			phase=1;
			p1.regen(this);
			p2.regen(this);
			p1act=((NPC)p1).actFast(this);
			p2act=((NPC)p2).actFast(this);
			clear();
			if(Global.isDebugOn(DebugFlags.DEBUG_SCENE)){
				System.out.println(p1.name()+" uses "+p1act.toString());
				System.out.println(p2.name()+" uses "+p2act.toString());
			}
			if(p1.pet!=null&&p2.pet!=null){
				petbattle(p1.pet,p2.pet);
			}
			else if(p1.pet!=null){
				p1.pet.act(this, p2);
			}
			else if(p2.pet!=null){
				p2.pet.act(this, p1);
			}
			useSkills();
			p1.eot(this,p2,p2act);
			p2.eot(this,p1,p1act);
			getStance().decay();
			getStance().checkOngoing(this);
			this.phase=0;
		}
		if(p1.getArousal().isFull()&&p2.getArousal().isFull()){		
			state = eval();
			p1.evalChallenges(this, null);
			p2.evalChallenges(this, null);
			p2.draw(this,state);
			end();
			return;
		}
		if(p1.getArousal().isFull()){
			state = eval();
			p1.evalChallenges(this, p2);
			p2.evalChallenges(this, p2);
			p2.victory(this,state);
			end();
			return;
		}
		if(p2.getArousal().isFull()){
			state = eval();
			p1.evalChallenges(this, p1);
			p2.evalChallenges(this, p1);
			p1.victory(this,state);
			end();
			return;
		}
	}

	private void resolveSkill(Skill skill, Character target) {
		if(Skill.skillIsUsable(this, skill, target)){
			write(skill.user().subjectAction("use ", "uses ") + skill.getName(this) + ".");
			if (target.is(Stsflag.counter) && skill.makesContact()) {
				write("Countered!");
				CounterStatus s = (CounterStatus)target.getStatus(Stsflag.counter);
				s.resolveSkill(this, skill.user());
			} else {
				Skill.resolve(skill, this, target);
			}
			checkStamina(target);
			checkStamina(skill.user());
		} else {
			write(skill.user().possessivePronoun() + " " + skill.getName(this) + " failed.");
		}
	}

	private void useSkills() {
		Skill firstSkill, secondSkill;
		Character firstCharacter, secondCharacter;
		if(p1.init()+p1act.speed()>=p2.init()+p2act.speed()){
			firstSkill = p1act;
			secondSkill = p2act;
			firstCharacter = p1;
			secondCharacter = p2;
		} else {
			firstSkill = p2act;
			secondSkill = p1act;
			firstCharacter = p2;
			secondCharacter = p1;
		}
		resolveSkill(firstSkill, secondCharacter);
		write("<br>");
		resolveSkill(secondSkill, firstCharacter);
	}
	public void clear(){
		message="";
		this.setChanged();
		this.notifyObservers();
	}

	public void write(String text){
		text = Global.capitalizeFirstLetter(text);
		assert(text.trim().length() > 0);
		String added=message+"<br>"+text;
		message = added;
		lastTalked = null;
	}

	public void write(Character user, String text){
		text = Global.capitalizeFirstLetter(text);
		assert(text.trim().length() > 0);
		if(user.human()){
			message=message+"<br><font color='rgb(200,200,255)'>"+text+"<font color='white'>";
		}else{
			message=message+"<br><font color='rgb(255,200,200)'>"+text+"<font color='white'>";
		}
		lastTalked = user;
	}
	public String getMessage(){
		return message;
	}
	public String debugMessage() {
		return "Stance: " + this.getStance().getClass().getName()
				+ "\np1: " + p1.debugMessage(getStance())
				+ "\np2: " + p2.debugMessage(getStance());
	}
	public void checkStamina(Character p){
		if(p.getStamina().isEmpty()){
			p.add(new Winded(p,1));
			if(!getStance().prone(p)){
				Character other;
				if(p==p1){
					other=p2;
				}
				else{
					other=p1;
				}
				if((getStance().penetration(p)||getStance().penetration(other))&&getStance().dom(other)){
					if(p.human()){
						write("Your legs give out, but "+other.name()+" holds you up.");
					}
					else{
						write(p.name()+" slumps in your arms, but you support her to keep her from collapsing.");
					}
				}
				else{
					setStance(new StandingOver(other,p));
					if(p.human()){
						write("You don't have the strength to stay on your feet. You slump to the floor.");
					}
					else{
						write(p.name()+" drops to the floor, exhausted.");
					}
				}
			}
		}
	}

	public void next(){
		if(phase==0){
			turn();
		}
		else if(phase==2){
			end();
		}
	}
	public void intervene(Character intruder, Character assist){
		Character target;
		if(p1==assist){
			target=p2;
		}
		else{
			target=p1;
		}
		intruder.intervene3p(this, target, assist);
		assist.victory3p(this, target,intruder);
		phase=2;
		if(!(p1.human()||p2.human()||intruder.human())){
			end();
		}
		else if(intruder.human()){
			Global.gui().watchCombat(this);
		}
		this.setChanged();
		this.notifyObservers();		
	}
	public boolean end(){
		clear();
		p1.state=State.ready;
		p2.state=State.ready;
		p1.endofbattle();
		p2.endofbattle();
		location.endEncounter();
		boolean ding=false;
		while (p1.getXP()>=95+(5*p1.getLevel())){
			p1.loseXP(95+(p1.getLevel()*5));
			p1.ding();
			if(p1.human()){
				ding=true;
			}
		}
		while (p2.getXP()>=95 +(5*p2.getLevel())){
			p2.loseXP(95+(p2.getLevel()*5));
			p2.ding();
			if(p2.human()){
				ding=true;
			}
		}
		return ding;
	}
	public void petbattle(Pet one, Pet two){
		int roll1=Global.random(20)+one.power();
		int roll2=Global.random(20)+two.power();
		if(one.hasPussy()&&two.hasDick()){
			roll1+=3;
		}
		else if(one.hasDick()&&two.hasPussy()){
			roll2+=3;
		}
		if(roll1>roll2){
			one.vanquish(this,two);
		}
		else if(roll2>roll1) {
			two.vanquish(this,one);
		}
		else{
			write(one.own()+one+" and "+two.own()+two+" engage each other for awhile, but neither can gain the upper hand.");
		}
	}
	public Combat clone() throws CloneNotSupportedException {
	     Combat c = (Combat) super.clone();
         c.p1 = (Character) p1.clone();
         c.p2 = (Character) p2.clone();
         c.clothespile = (ArrayList) clothespile.clone();
         c.stance = (Position) getStance().clone();
         if (c.getStance().top == p1) c.getStance().top = c.p1;
         if (c.getStance().top == p2) c.getStance().top = c.p2;
         if (c.getStance().bottom == p1) c.getStance().bottom = c.p1;
         if (c.getStance().bottom == p2) c.getStance().bottom = c.p2;
         return c;
	}
	public Skill lastact(Character user){
		if(user==p1){
			return p1act;
		}
		else if(user==p2){
			return p2act;
		}
		else{
			return null;
		}
	}

	public void offerImage(String path, String artist){
		images.put(path, artist);
	}

	public void forfeit(Character player){
		end();
	}

	public Position getStance() {
		return stance;
	}
	
	public void checkStanceStatus(Character c, Position oldStance, Position newStance) {
		if (oldStance.prone(c) && !newStance.prone(c)) {
			c.add(new Braced(c));
			c.add(new Wary(c, 3));
		} else if (!oldStance.mobile(c) && newStance.mobile(c)) {
			c.add(new Wary(c, 3));
		}
	}

	public void setStance(Position newStance) {
		checkStanceStatus(p1, stance, newStance);
		checkStanceStatus(p2, stance, newStance);
		this.stance = newStance;
	}
}
