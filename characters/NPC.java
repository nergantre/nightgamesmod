package characters;
import global.DebugFlags;
import global.Global;
import global.Modifier;

import items.Clothing;
import items.Item;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.Icon;

import characters.body.Body;

import skills.Nothing;
import skills.Skill;
import skills.Tactics;
import stance.Behind;
import status.Enthralled;
import status.Horny;
import status.Masochistic;
import status.Status;
import status.Stsflag;
import trap.Trap;

import actions.Action;
import actions.Move;
import actions.Movement;
import areas.Area;

import combat.Combat;
import combat.Encounter;
import combat.Result;


public class NPC extends Character {
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = -7213318631528080442L;
	Personality ai;
	public HashMap<Emotion,Integer> emotes;
	public Emotion mood;
	public Tactics plan;
	

	public NPC(String name, int level,Personality ai){
		super(name,level);
		this.ai=ai;	
		emotes = new HashMap<Emotion,Integer>();
		for(Emotion e: Emotion.values()){
			emotes.put(e, 0);
		}
	}
	@Override
	public String describe(int per) {
		String description=ai.describeAll();
		for(Status s:status){
			description = description+"<br>"+s.describe();
		}
		description = description+"<p>";
		if(top.empty()&&bottom.empty()){
			description = description+"She is completely naked.<br>";
		}
		else{
			if(top.empty()){
				description = description+"She is topless and ";
				if(!bottom.empty()){
					description=description+"wearing ";
				}
			}
			else{
				description = description+"She is wearing "+top.peek().pre()+top.peek().getName()+" and ";
			}
			if(bottom.empty()){
				description = description+"is naked from the waist down.<br>";
			}
			else{
				description = description+bottom.peek().pre()+bottom.peek().getName()+".<br>";
			}
		}
		description = description+observe(per);
		return description;
	}
	private String observe(int per){
		String visible = "";
		for(Status s: status){
			if(s.flags().contains(Stsflag.unreadable)){
				return visible;
			}
		}
		if(per>=9){
			visible = visible+"Her arousal is at "+arousal.percent()+"%<br>";
		}
		if(per>=8){
			visible = visible+"Her stamina is at "+stamina.percent()+"%<br>";
		}
		if(per>=7){
			visible = visible+"She looks " + mood.name() + "<br>";
		}
		if(per>=7 && per<9){
			if(arousal.percent()>=75){
				visible = visible+"She's dripping with arousal and breathing heavily. She's at least 3/4 of the way to orgasm<br>";
			}
			else if(arousal.percent()>=50){
				visible = visible+"She's showing signs of arousal. She's at least halfway to orgasm<br>";
			}
			else if(arousal.percent()>=25){
				visible = visible+"She's starting to look noticeably arousal, maybe a quarter of her limit<br>";
			}
		}
		if(per>=6 && per<8){
			if(stamina.percent()<=66){
				visible = visible+"She's starting to look tired<br>";
			}
			else if(stamina.percent()<=33){
				visible = visible+"She looks a bit unsteady on her feet<br>";
			}
		}
		if(per>=3 && per<7){
			if(arousal.percent()>=50){
				visible = visible+"She's showing clear sign of arousal. You're definitely getting to her.<br>";
			}
		}
		if(per>=4 && per<6){
			if(stamina.percent()<=50){
				visible = visible+"She looks pretty tired<br>";
			}
		}

		if(per>=6 && this.status.size() > 0){
			visible += "List of statuses:<br><i>";
			for (Status s : this.status) {
				visible += s + ", ";
			}
			visible += "</i><br>";
		}
		return visible;
	}
	
	@Override
	public void victory(Combat c, Result flag) {
		Character target;
		if(c.p1==this){
			target=c.p2;
		}
		else{
			target=c.p1;
		}
		this.gainXP(20+lvlBonus(target));
		target.gainXP(10+target.lvlBonus(this));
		if(c.getStance().penetration(this)){
			getMojo().gain(2);
			if(has(Trait.mojoMaster)){
				getMojo().gain(2);
			}
		}
		target.arousal.empty();
		if(target.has(Trait.insatiable)){
			target.arousal.restore((int) (arousal.max()*.2));
		}
		dress(c);
		target.undress(c);
		if(c.clothespile.contains(target.outfit[1].firstElement())){
			this.gain(target.getUnderwear());
		}
		target.defeated(this);
		c.write(ai.victory(c, flag));
		gainAttraction(target,1);
		target.gainAttraction(this,2);
	}

	@Override
	public void defeat(Combat c, Result flag) {
		Character target;
		if(c.p1==this){
			target=c.p2;
		}
		else{
			target=c.p1;
		}
		this.gainXP(10+lvlBonus(target));
		target.gainXP(20+target.lvlBonus(this));
		this.arousal.empty();
		if(!target.human()||Global.getMatch().condition!=Modifier.norecovery){
			target.arousal.empty();
		}
		if(this.has(Trait.insatiable)){
			this.arousal.restore((int) (arousal.max()*.2));
		}
		if(target.has(Trait.insatiable)){
			target.arousal.restore((int) (arousal.max()*.2));
		}
		target.dress(c);
		this.undress(c);
		if(c.clothespile.contains(this.outfit[1].firstElement())){
			target.gain(this.getUnderwear());
		}
		this.defeated(target);
		c.write(ai.defeat(c,flag));
		gainAttraction(target,2);
		target.gainAttraction(this,1);
	}
	public void intervene3p(Combat c,Character target, Character assist){
		this.gainXP(20+lvlBonus(target));
		target.defeated(this);
		c.write(ai.intervene3p(c, target,assist));
		assist.gainAttraction(this, 1);
	}
	public void victory3p(Combat c,Character target, Character assist){
		this.gainXP(20+lvlBonus(target));
		target.gainXP(10+target.lvlBonus(this)+target.lvlBonus(assist));
		target.arousal.empty();
		if(target.has(Trait.insatiable)){
			target.arousal.restore((int) (arousal.max()*.2));
		}
		dress(c);
		target.undress(c);
		if(c.clothespile.contains(target.outfit[1].firstElement())){
			this.gain(target.getUnderwear());
		}
		target.defeated(this);
		c.write(ai.victory3p(c,target,assist));
		gainAttraction(target,1);
	}
	@Override
	public void act(Combat c) {
		HashSet<Skill> available = new HashSet<Skill>();
		Character target;
		if(c.p1==this){
			target=c.p2;
		}
		else{
			target=c.p1;
		}
		for(Skill act:skills){
			if(Skill.skillIsUsable(c, act, target) && cooldownAvailable(act)) {
				available.add(act);
			}
		}
		if (available.size() == 0) {
			available.add(new Nothing(this));
		}
		c.act(this, ai.act(available,c));
	}
	
	public Skill actFast(Combat c) {
		HashSet<Skill> available = new HashSet<Skill>();
		Character target;
		if(c.p1==this){
			target=c.p2;
		}
		else{
			target=c.p1;
		}
		for(Skill act:skills){
			if(Skill.skillIsUsable(c, act, target) && cooldownAvailable(act)){
				available.add(act);
			}
		}
		if (available.size() == 0) {
			available.add(new Nothing(this));
		}
		return ai.act(available,c);
	}
	@Override
	public boolean human() {
		return false;
	}
	@Override
	public void draw(Combat c, Result flag) {
		Character target;
		if(c.p1==this){
			target=c.p2;
		}
		else{
			target=c.p1;
		}
		this.gainXP(20+lvlBonus(target));
		target.gainXP(20+target.lvlBonus(this));
		this.arousal.empty();
		target.arousal.empty();
		if(this.has(Trait.insatiable)){
			this.arousal.restore((int) (arousal.max()*.2));
		}
		if(target.has(Trait.insatiable)){
			target.arousal.restore((int) (arousal.max()*.2));
		}
		target.undress(c);
		this.undress(c);
		if(c.clothespile.contains(this.outfit[1].firstElement())){
			target.gain(this.getUnderwear());
		}
		if(c.clothespile.contains(target.outfit[1].firstElement())){
			this.gain(target.getUnderwear());
		}
		target.defeated(this);
		this.defeated(target);
		c.write(ai.draw(c,flag));
		gainAttraction(target,4);
		target.gainAttraction(this,4);
		if(getAffection(target)>0){
			gainAffection(target,1);
			target.gainAffection(this,1);
			if(this.has(Trait.affectionate)||target.has(Trait.affectionate)){
				gainAffection(target,2);
				target.gainAffection(this,2);
			}
		}
	}
	@Override
	public String bbLiner() {
		return ai.bbLiner();
	}
	@Override
	public String nakedLiner() {
		return ai.nakedLiner();
	}
	@Override
	public String stunLiner() {
		return ai.stunLiner();
	}
	@Override
	public String winningLiner() {
		return ai.winningLiner();
	}
	@Override
	public String taunt() {
		return ai.taunt();
	}
	@Override
	public String temptLiner(Character target) {
		return ai.temptLiner(target);
	}
	@Override
	public void detect() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void move() {
		if(state==State.combat){
			location.fight.battle();
		}
		else if(busy>0){
			busy--;
		}
		else if (this.is(Stsflag.enthralled)) {
			Character master;
			master = ((Enthralled)getStatus(Stsflag.enthralled)).master;
			Move compelled = findPath(master.location);
			if (compelled != null){
				compelled.execute(this);
				return;
			}
		}
		else if(state==State.shower||state==State.lostclothes){
			bathe();
		}
		else if(state==State.crafting){
			craft();
		}
		else if(state==State.searching){
			search();
		}
		else if(state==State.resupplying){
			resupply();
		}
		else if(state==State.webbed){
			state=State.ready;
		}
		else if(state==State.masturbating){
			masturbate();
		}
		else{
			if(!location.encounter(this)){
				HashSet<Action> available = new HashSet<Action>();
				HashSet<Movement> radar = new HashSet<Movement>();
				for(Area path:location.adjacent){
					available.add(new Move(path));
					if(path.ping(get(Attribute.Perception))){
						radar.add(path.id());
					}
				}
				for(Action act:Global.getActions()){
					if(act.usable(this)){
						available.add(act);
					}
				}
				if(location.humanPresent()){
					Global.gui().message("You notice "+name()+ai.move(available,radar).execute(this).describe());
				}
				else{
					ai.move(available,radar).execute(this);
				}
			}
		}
	}
	@Override
	public void faceOff(Character opponent, Encounter enc) {
		enc.fightOrFlight(this, ai.fightFlight(opponent));
	}
	@Override
	public void spy(Character opponent, Encounter enc) {
		if(ai.attack(opponent)){
			enc.ambush(this, opponent);
		}
		else{
			location.endEncounter();
		}
	}
	public void ding(){
		level++;
		ai.ding();
		if(countFeats()<level/4){
			ai.pickFeat();
		}
		String message = Global.gainSkills(this);
		if (human())
			Global.gui().message(message);
	}

	@Override
	public void showerScene(Character target, Encounter encounter) {
		if(this.has(Item.Aphrodisiac)){
			encounter.aphrodisiactrick(this, target);
		}
		else if(!target.nude()&&Global.random(3)>=2){
			encounter.steal(this, target);
		}
		else{
			encounter.showerambush(this, target);
		}
	}
	@Override
	public void intervene(Encounter enc, Character p1, Character p2) {
		if(Global.random(20)+getAffection(p1) + (p1.has(Trait.sympathetic)?10:0)>=Global.random(20)+getAffection(p2)+ (p2.has(Trait.sympathetic)?10:0)){
			enc.intrude(this, p1);
		}
		else{
			enc.intrude(this, p2);
		}
	}
	public void save(PrintWriter saver) {
		saver.write("NPC\n");
		super.save(saver);
	}

	@Override
	public String challenge() {
		return ai.startBattle();
	}
	@Override
	public void promptTrap(Encounter enc,Character target,Trap trap) {
		if(ai.attack(target)){
			enc.trap(this, target,trap);
		}
		else{
			location.endEncounter();
		}
	}
	@Override
	public void afterParty() {
		Global.gui().message(ai.night());
	}
	public void daytime(){
		ai.rest();
	}
	public Emotion getMood() {
		return mood;
	}
	@Override
	public void counterattack(Character target, Tactics type, Combat c) {
		switch(type){
		case damage:
			c.write(name()+" avoids your clumsy attack and swings her fist into your nuts.");
			target.pain(c, 4+Global.random(get(Attribute.Cunning)));
			break;
		case pleasure:
			if(target.pantsless()){
				c.write(name()+" catches you by the penis and rubs your sensitive glans.");
				target.body.pleasure(this, body.getRandom("hands"), target.body.getRandom("cock"), 4+Global.random(get(Attribute.Cunning)), c);
			}
			else{
				c.write(name()+" catches you as you approach and grinds her knee into the tent in your "+target.bottom.peek());
				target.body.pleasure(this, body.getRandom("legs"), target.body.getRandom("cock"), 4+Global.random(get(Attribute.Cunning)), c);
			}
			break;
		case fucking:
			c.write(name()+" squeezes your dick every time you thrust into her, arousing you way more than you would expect.");
			target.body.pleasure(this, body.getRandomPussy(), target.body.getRandomCock(), 4+Global.random(get(Attribute.Cunning)), c);
			break;
		case stripping:
			Clothing clothes = target.stripRandom(c);
			if (clothes != null) {
				c.write(name()+" manages to catch you groping her clothing, and in a swing motion strips off your " + clothes.getName());
			}
			break;
		case positioning:
			c.write(name()+" outmanuevers you and catches you from behind when you stumble.");
			c.setStance(new Behind(this,target));
			break;
		default:
		}
	}
	public Skill prioritize(ArrayList<WeightedSkill> plist){
		if(plist.isEmpty()){
			return null;
		}
		float sum = 0;
		ArrayList<WeightedSkill> wlist = new ArrayList<WeightedSkill>();
		for(WeightedSkill wskill : plist){
			sum += wskill.weight;
			wlist.add(new WeightedSkill(sum, wskill.skill));
			if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS))
				System.out.printf("%.1f %s\n", sum, wskill.skill);
		}
		
		if(wlist.isEmpty()){
			return null;
		}
		float s = Global.randomfloat() * sum;
		for(WeightedSkill wskill : wlist){
			if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS))
				System.out.printf("%.1f/%.1f %s\n", wskill.weight,s, wskill.skill);
			if (wskill.weight > s) {
				return wskill.skill;
			}
		}
		return plist.get(plist.size()-1).skill;
	}
	public void emote(Emotion emo,int amt){
		if (emo == mood) {
			// if already this mood, cut gain by half
			amt = Math.max(1, amt/2);
		}
		emotes.put(emo, emotes.get(emo)+amt);
	}
	public Emotion moodSwing(Combat c){
		Emotion current = mood;
		for(Emotion e: emotes.keySet()){
			if(ai.checkMood(e, emotes.get(e))){
				emotes.put(e, 0);
				// cut all the other emotions by half so that the new mood persists for a bit
				for(Emotion e2: emotes.keySet()){
					emotes.put(e2, emotes.get(e2)/2);
				}
				mood=e;
				if (Global.isDebugOn(DebugFlags.DEBUG_MOOD)) {
					System.out.printf("Moodswing: %s is now %s\n", name, mood.name());
				}
				if (c.p1.human() || c.p2.human())
					Global.gui().loadPortrait(c.p1, c.p2);
				return e;
			}
		}
		return current;
	}
	
	@Override
	public void eot(Combat c, Character opponent, Skill last) {
		super.eot(c, opponent, last);
		if(opponent.pet!=null&&canAct()&&c.getStance().mobile(this)&&!c.getStance().prone(this)){
			if(get(Attribute.Speed)>opponent.pet.ac()*Global.random(20)){
				opponent.pet.caught(c,this);
			}
		}
		int pheromoneChance = opponent.top.size() + opponent.bottom.size();
		if(opponent.has(Trait.pheromones)&&opponent.getArousal().percent()>=20&&Global.random(2 + pheromoneChance)==0){
			c.write(opponent,"<br>You see "+name()+" swoon slightly as she gets close to you. Seems like she's starting to feel the effects of your musk.");
			add(new Horny(this, opponent.has(Trait.augmentedPheromones) ? 2 : 1, 10, opponent.nameOrPossessivePronoun() + " pheromones"));
		}
		if(opponent.has(Trait.smqueen)&&!is(Stsflag.masochism)){
			c.write(String.format("<br>%s seems to shudder in arousal at the thought of pain.", subject()));
			add(new Masochistic(this));
		}
		if(has(Trait.RawSexuality)){
			tempt(c, opponent, getArousal().max() / 25);
			opponent.tempt(c, this, opponent.getArousal().max() / 25);
		}
		if(c.getStance().dom(this)){
			emote(Emotion.dominant,20);
			emote(Emotion.confident,10);
		}
		else if(c.getStance().sub(this)){
			emote(Emotion.nervous,15);
			emote(Emotion.desperate,10);
		}
		if(opponent.nude()){
			emote(Emotion.horny,15);
			emote(Emotion.confident,10);
		}
		if(nude()){
			emote(Emotion.nervous,10);
			if(has(Trait.exhibitionist)){
				emote(Emotion.horny,20);
			}
		}
		if(opponent.getArousal().percent()>=75){
			emote(Emotion.confident,20);
		}

		if(getArousal().percent()>=50){
		emote(Emotion.horny, getArousal().percent() / 4);
		}

		if(getArousal().percent()>=50){
			emote(Emotion.desperate,10);	
		}
		if(getArousal().percent()>=75){
			emote(Emotion.desperate,20);
			emote(Emotion.nervous,10);	
		}
		if(getArousal().percent()>=90){
			emote(Emotion.desperate,20);
		}
		if(!canAct()){
			emote(Emotion.desperate,10);
		}
		if(!opponent.canAct()){
			emote(Emotion.dominant,20);
		}
		moodSwing(c);
	}
	public NPC clone() throws CloneNotSupportedException {
	    return (NPC) super.clone();
	}
	private float rateMove(Skill skill, Combat c, float urg, float fit1, float fit2) {
	      // Clone ourselves a new combat... This should clone our characters, too
		Combat c2;
		try {
			c2 = c.clone();
		} catch(CloneNotSupportedException e) {
			return 0;
		}
		if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS_RATING) && (c.p1.human() || c.p2.human()) ) {
			System.out.println("===> Rating " + skill);
			System.out.println("Before:\n" + c.debugMessage());
		}
       // Make sure the clone is authentic
		/*if ( urg != Math.min(c2.p1.getUrgency(), c2.p2.getUrgency())
				|| fit1 != c2.p1.getFitness(urg, c2.stance)
				|| fit2 != c2.p2.getFitness(urg, c2.stance))
			throw new AssertionError();*/
		// Now do it!
		Global.debugSimulation = true;
		if (c.p1 == this) {
			skill.setSelf(c2.p1);
			skill.resolve(c2, c2.p2);
			skill.setSelf(c.p1);
       } else {
    	   skill.setSelf(c2.p2);
    	   skill.resolve(c2, c2.p1);
    	   skill.setSelf(c.p2);
      }
		Global.debugSimulation = false;
		// How close is the fight to finishing?
       float urgency = Math.min(c2.p1.getUrgency(), c2.p2.getUrgency());
       float dfit1 = c2.p1.getFitness(urgency, c2.getStance()) - fit1;
       float dfit2 = c2.p2.getFitness(urgency, c2.getStance()) - fit2;
       if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS_RATING) && (c2.p1.human() || c2.p2.human())) {
			System.out.println("After:\n" + c2.debugMessage());
		}
       return (c.p1 == this ? dfit1 - dfit2 : dfit2 - dfit1);
   }

	public Skill prioritizeNew(ArrayList<WeightedSkill> plist, Combat c)
	{
		if (plist.isEmpty()) {
			return null;
	    }
	    // The higher, the better the AI will plan for "rare" events better
	    final int RUN_COUNT = 5;
	    // Decrease to get an "easier" AI. Make negative to get a suicidal AI.
	    final float RATING_FACTOR = 0.02f;
	    // Increase to take input weights more into consideration
	    final float INPUT_WEIGHT = 1.0f;

	    // Starting fitness
	    float urgency0 = Math.min(c.p1.getUrgency(), c.p2.getUrgency());
	    float fit1 = c.p1.getFitness(urgency0, c.getStance());
	    float fit2 = c.p2.getFitness(urgency0, c.getStance());
	    
	    // Now simulate the result of all actions
	    ArrayList<WeightedSkill> moveList = new ArrayList<WeightedSkill>();
	    float sum = 0;
	    for (WeightedSkill wskill : plist) {
    	   // Run it a couple of times
           float rating, raw_rating = 0;
           if (wskill.skill.type(c) == Tactics.fucking && has(Trait.experienced)) {
        	   wskill.weight += 1.0;
           }
           if (wskill.skill.type(c) == Tactics.damage && has(Trait.smqueen)) {
        	   wskill.weight += 1.0;
           }
           for (int j = 0; j < RUN_COUNT; j++) {
        	   raw_rating += rateMove(wskill.skill, c, urgency0, fit1, fit2);
           }
           // Sum up rating, add to map
           rating = (float) Math.exp(RATING_FACTOR * raw_rating + wskill.weight + wskill.skill.priorityMod(c));
           sum += rating;
           moveList.add(new WeightedSkill(sum, raw_rating, rating, wskill.skill));
	    }
	    if (sum == 0 || moveList.size() == 0) return null;
	    // Debug
	    if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS)) {
	       String s = "AI choices: ";
	       for (WeightedSkill entry : moveList) {
	               s += String.format("\n(%.1f\t\t%.1f\t\tculm: %.1f\t\t/ %.1f)\t\t-> %s", entry.raw_rating, entry.rating, entry.weight, entry.rating * 100.0f /sum, entry.skill.toString());
	       }
	       System.out.println(s);
	    }
	    // Select
	    float s = Global.randomfloat() * sum;
	    for (WeightedSkill entry : moveList) {
	    	if (Global.isDebugOn(DebugFlags.DEBUG_SKILLS))
	    		System.out.printf("%.1f/%.1f %s\n", entry.weight, s, entry.skill.toString());
	    	if (entry.weight > s) {
	    		return entry.skill;
	    	}
	    }
	    return moveList.get(moveList.size()-1).skill;
	}
	@Override
	public String getPortrait() {
		return ai.image();
	}
}
