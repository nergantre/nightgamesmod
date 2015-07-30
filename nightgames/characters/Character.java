package nightgames.characters;

import nightgames.actions.Move;
import nightgames.actions.Movement;
import nightgames.areas.Area;
import nightgames.characters.body.Body;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Encounter;
import nightgames.combat.Result;
import nightgames.global.Challenge;
import nightgames.global.DebugFlags;
import nightgames.global.Global;
import nightgames.global.Modifier;
import nightgames.items.Clothing;
import nightgames.items.Item;
import nightgames.pet.Pet;
import nightgames.skills.CounterDrain;
import nightgames.skills.CounterRide;
import nightgames.skills.Distracted;
import nightgames.skills.FondleBreasts;
import nightgames.skills.Fuck;
import nightgames.skills.Kiss;
import nightgames.skills.Nothing;
import nightgames.skills.PullOut;
import nightgames.skills.Recover;
import nightgames.skills.ReverseStraddle;
import nightgames.skills.Shove;
import nightgames.skills.Skill;
import nightgames.skills.Straddle;
import nightgames.skills.StripBottom;
import nightgames.skills.StripTop;
import nightgames.skills.Struggle;
import nightgames.skills.Stunned;
import nightgames.skills.Tactics;
import nightgames.skills.ThrowDraft;
import nightgames.skills.Tickle;
import nightgames.skills.UseDraft;
import nightgames.skills.Wait;
import nightgames.stance.Position;
import nightgames.stance.Stance;
import nightgames.status.Enthralled;
import nightgames.status.Horny;
import nightgames.status.Resistance;
import nightgames.status.Status;
import nightgames.status.Stsflag;
import nightgames.trap.Trap;

import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

public abstract class Character extends Observable implements Cloneable{
	/**
	 * 
	 */
	protected String name;
	protected int level;
	protected int xp;
	protected int rank;
	public int money;
	public HashMap<Attribute,Integer> att;
	protected Meter stamina;
	protected Meter arousal;
	protected Meter mojo;
	protected Meter willpower;
	public Stack<Clothing> top;
	public Stack<Clothing> bottom;
	protected Area location;
	public Stack<Clothing>[] outfit;
	protected HashSet<Skill> skills;
	public HashSet<Status> status;
	protected HashSet<Trait> traits;
	protected HashMap<Trait, Integer> temporaryAddedTraits;
	protected HashMap<Trait, Integer> temporaryRemovedTraits;
	public HashSet<Status> removelist;
	public HashSet<Status> addlist;
	public HashMap<String, Integer> cooldowns;
	private HashSet<Character> mercy;
	protected Map<Item, Integer> inventory;
	protected Item trophy;
	public State state;
	protected int busy;
	protected HashMap<Character,Integer> attractions;
	protected HashMap<Character,Integer> affections;
	public HashSet<Clothing> closet;
	public Pet pet;
	public ArrayList<Challenge> challenges;
	public Body body;
	public int availableAttributePoints;

	public Character(String name, int level){
		this.name=name;
		this.level=level;
		body = new Body(this);
		att = new HashMap<Attribute,Integer>();
		cooldowns = new HashMap<String, Integer>();
		att.put(Attribute.Power, 5);
		att.put(Attribute.Cunning, 5);
		att.put(Attribute.Seduction, 5);
		att.put(Attribute.Perception, 5);
		att.put(Attribute.Speed, 5);
		this.money=0;
		stamina=new Meter(22+3*level);
		stamina.fill();
		arousal = new Meter(90+10*level);
		mojo = new Meter(33+2*level);
		willpower = new Meter(50);
		top = new Stack<Clothing>();
		bottom = new Stack<Clothing>();
		outfit = new Stack[2];
		outfit[0]=new Stack<Clothing>();
		outfit[1]=new Stack<Clothing>();
		closet = new HashSet<Clothing>();
		skills = new HashSet<Skill>();
		status = new HashSet<Status>();
		traits = new HashSet<Trait>();
		temporaryAddedTraits = new HashMap<Trait, Integer>();
		temporaryRemovedTraits = new HashMap<Trait, Integer>();
		removelist = new HashSet<Status>();
		addlist = new HashSet<Status>();
		mercy=new HashSet<Character>();
		inventory=new HashMap<Item, Integer>();
		attractions = new HashMap<Character, Integer>();
		affections = new HashMap<Character, Integer>();
		challenges=new ArrayList<Challenge>();
		location=new Area("","",null);
		state=State.ready;
		busy=0;
		setRank(0);
		this.pet=null;
		Global.learnSkills(this);
	}
	public Character clone() throws CloneNotSupportedException {
	    Character c = (Character) super.clone();
	    c.att = (HashMap) att.clone();
		c.stamina = (Meter) stamina.clone();
		c.arousal = (Meter) arousal.clone();
		c.mojo = (Meter) mojo.clone();
		c.willpower = (Meter) willpower.clone();
		c.top = (Stack) top.clone();
		c.bottom = (Stack) bottom.clone();
		c.outfit = (Stack[]) outfit.clone();
		c.skills = (HashSet) skills.clone();
		c.status = new HashSet<Status>();
		for (Status s: status) {
			Status clone = (Status) s.clone();
			clone.affected = c;
			c.status.add(clone);
		}
		c.traits = (HashSet) traits.clone();
		c.temporaryAddedTraits = new HashMap<Trait, Integer>(temporaryAddedTraits);
		c.temporaryRemovedTraits = new HashMap<Trait, Integer>(temporaryRemovedTraits);
		c.removelist = (HashSet) removelist.clone();
		c.addlist = (HashSet) addlist.clone();
		c.mercy = (HashSet) mercy.clone();
		c.inventory = (Map<Item, Integer>) ((HashMap<Item, Integer>) inventory).clone();
		c.attractions = (HashMap) attractions.clone();
		c.affections = (HashMap) affections.clone();
		c.skills = (HashSet) skills.clone();
		c.body = (Body) body.clone();
		c.body.character = c;
		return c;
	}
	public String name(){
		return name;
	}

	public List<Resistance> getResistances() {
		List<Resistance> resists = new ArrayList<>();
		for (Trait t : traits) {
			Trait.getResistance(t);
		}
		return resists;
	}

	public int getXPReqToNextLevel() {
		return Math.min(45+(5*getLevel()), 100);
	}

	public int get(Attribute a){
		int total =0;
		if(att.containsKey(a)){
			total = att.get(a);
		}
		for(Status s:status){
			total+=s.mod(a);
		}
		total += body.mod(a, total);
		switch(a){
		case Arcane:
			if(has(Trait.mystic)){
				total+=2;
			}
			break;
		case Dark:
			if(has(Trait.broody)){
				total+=2;
			}
			break;
		case Ki:
			if(has(Trait.martial)){
				total+=2;
			}
			break;
		case Fetish:
			if(has(Trait.kinky)){
				total+=2;
			}
			break;		
		case Power:
				if(has(Trait.testosterone) && hasDick()){
					total+=Math.max(10, 2 + getLevel() / 2);
				}
				break;
		case Science:
			if(has(Trait.geeky)){
				total+=2;
			}
			break;
		case Speed:
			for(Clothing article:top){
				if(article.attribute()==Trait.bulky){
					total--;
				}
			}
			for(Clothing article:bottom){
				if(article.attribute()==Trait.bulky){
					total--;
				}
			}
		}
		return total;
	}
	public int getPure(Attribute a){
		int total =0;
		if(att.containsKey(a)){
			total = att.get(a);
		}
		return total;
	}
	public void mod(Attribute a, int i){
		if(att.containsKey(a)){
			att.put(a, att.get(a)+i);
		}
		else{
			set(a,i);
		}
	}
	public void set(Attribute a, int i){
		att.put(a, i);
	}
	public boolean check(Attribute a, int dc){
		int rand = Global.random(20);
		if (rand == 0) {
			//critical hit, don't check
			return true;
		}
		if(get(a)==0){
			return false;
		}
		else{
			return get(a)+rand>=dc;
		}
	}
	public int getLevel(){
		return level;
	}
	public void gainXP(int i){
		assert(i >= 0);
		double rate = 1.0;
		if (has(Trait.fastLearner)) {
			rate += .2;
		}
		rate *= Global.xpRate;
		xp+=Math.round(i * rate);
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public void rankup(){
		this.rank++;
	}
	public abstract void ding();

	public String dong(){
		level--;
		String message = loseRandomAttributes(Global.random(3) + 2);
		if(countFeats() > level/4){
			message += loseFeat();
		}
		
		getStamina().gain(-2);
		getArousal().gain(-4);
		getMojo().gain(-1);
		return message + Global.gainSkills(this);
	}

	public String loseRandomAttributes(int number) {
		ArrayList<Attribute> avail = new ArrayList<Attribute>();
		String message = "";
		while (number > 0) {
			avail.clear();
			for (Attribute a : att.keySet()) {
				if (getPure(a) > 1 && a != Attribute.Speed && a != Attribute.Perception) {
					avail.add(a);
				}
			}
			if (avail.size() == 0) {break;}
			Attribute removed = avail.get(Global.random(avail.size()));
			mod(removed, -1);
			if (human())
				message += "You've lost a point in "+removed.toString()+".<br>";
			number -= 1;
		}
		return message;
	}

	public String loseFeat() {
		String string = "";
		ArrayList<Trait> available = new ArrayList<Trait>();
		for(Trait feat: Global.getFeats(this)){
			if(has(feat)){
				available.add(feat);
			}
		}
		Trait removed = available.get(Global.random(available.size()));
		if(human()){
			string += "You've lost "+removed.toString()+".<br>";
		}
		remove(removed);
		return string;
	}
	public int getXP(){
		return xp;
	}
	public void pain(Combat c, int i){
		int pain = i;
		Character other = c == null ? null : (this == c.p1 ? c.p2 : c.p1);

		if (has(Trait.cute) && other != null) {
			i = Math.max(i - get(Attribute.Seduction), i/4 + 1);
			c.write(this, Global.format("{self:NAME-POSSESSIVE} innocent appearance throws {other:direct-object} off and {other:subject-action:use|uses} much less strength than intended.", this, other));
		}

		if (has(Trait.slime)) {
			i = i/2;
			c.write(this, "The blow glances off " + nameOrPossessivePronoun() + " slimy body.");
		}
		for(Status s: status){
			pain+=s.damage(c, i);
		}
		i = Math.max(1, i);
		emote(Emotion.angry, i / 3);
		if (c!=null) {
			c.write(String.format("%s hurt for <font color='rgb(250,10,10)'>%d<font color='white'>", this.subjectWas(), i));
		}
		stamina.reduce(pain);
	}

	public void weaken(Combat c, int i){
		int weak = i;
		for(Status s: status){
			weak+=s.weakened(i);
		}
		if(weak>=stamina.get()){
			weak = stamina.get();
		}
		i = Math.max(1, i);
		if (c!=null) {
			c.write(String.format("%s weaked by <font color='rgb(200,200,200)'>%d<font color='white'>", this.subjectWas(), i));
		}
		stamina.reduce(weak);
	}
	public void heal(Combat c, int i){
		i = Math.max(1, i);
		if (c!=null) {
			c.write(String.format("%s healed for <font color='rgb(100,240,30)'>%d<font color='white'>", this.subjectWas(), i));
		}
		stamina.restore(i);
	}
	public String subject() {
		return name();
	}
	public void pleasure(int i, Combat c){
		int pleasure = i;

		emote(Emotion.horny, i / 4 + 1);
		for(Status s: status){
			pleasure+=s.pleasure(null, i);
		}
		if(pleasure<1){
			pleasure=1;
		}
		//pleasure = 0;
		arousal.restore(Math.round(pleasure));
	}
	public void tempt(Combat c, int i) {
		tempt(c, null, i);
	}

	public void tempt(Combat c, Character tempter, int i) {
		if (tempter != null) {
			double temptMultiplier = body.getCharismaBonus(tempter);
			int dmg = (int) Math.round(i * temptMultiplier);
			tempt(dmg);
			String message = String.format("%s tempted %s for <font color='rgb(240,100,100)'>%d<font color='white'> (base:%d, charisma:%.1f)\n",
					Global.capitalizeFirstLetter(tempter.subject()), tempter == this ? reflectivePronoun() : directObject(), dmg, i, temptMultiplier);
			if (Global.isDebugOn(DebugFlags.DEBUG_DAMAGE))
				System.out.printf(message);
			if (c != null) {
				c.write(message);
			}
		} else {
			if (c != null) {
				c.write(String.format("%s tempted for <font color='rgb(240,100,100)'>%d<font color='white'>\n", subjectWas(), i));
			}
			tempt(i);
		}
	}

	public void arouse(int i, Combat c){
		String message = String.format("%s aroused for <font color='rgb(240,100,100)'>%d<font color='white'>\n",
				Global.capitalizeFirstLetter(subjectWas()), i);
		if (c != null)
			c.write(message);
		tempt(i);
	}

	public String subjectAction(String verb, String pluralverb) {
		return subject() + " " + pluralverb;
	}

	public String subjectWas() {
		return subject() + " was";
	}

	public void tempt(int i){
		int temptation = i;

		emote(Emotion.horny, i/4);
		if (arousal.isFull()) {return;}
		for(Status s: status){
			temptation+=s.tempted(i);
		}
		if(has(Trait.desensitized2)){
			temptation /= 2;
		}
		arousal.restore(temptation);
		if(arousal.isFull()){
			arousal.reduce(1);
		}
	}
	public void calm(Combat c, int i){
		if (c != null) {
			String message = String.format("%s calmed down by <font color='rgb(80,145,200)'>%d<font color='white'>\n",
					Global.capitalizeFirstLetter(subjectAction("have", "has")), i);
			c.write(message);
		}
		arousal.reduce(i);
	}
	public Meter getStamina(){
		return stamina;
	}
	public Meter getArousal(){
		return arousal;
	}
	public Meter getMojo(){
		return mojo;
	}
	public Meter getWillpower(){
		return willpower;
	}
	public void buildMojo(Combat c, int percent){
		int x = (percent*Math.min(mojo.max(), 200))/100;
		for(Status s: status){
			x+=s.gainmojo(x);
		}
		if (x > 0) {
			mojo.restore(x);
			if (c != null) {
				c.write(Global.capitalizeFirstLetter(String.format("%s <font color='rgb(100,200,255)'>%d<font color='white'> mojo.",this.subjectAction("built", "built"), x)));
			}
		}
	}
	public void spendMojo(Combat c, int i){
		int cost = i;
		for(Status s: status){
			cost+=s.spendmojo(i);
		}
		mojo.reduce(cost);
		if(mojo.get()<0){
			mojo.set(0);
		}
		if (c != null && i != 0) {
			c.write(Global.capitalizeFirstLetter(String.format("%s <font color='rgb(150,150,250)'>%d<font color='white'> mojo.",this.subjectAction("spent", "spent"), cost)));
		}
	}
	public int loseMojo(Combat c, int i) {
		int amt = Math.min(mojo.get(), i);
		mojo.reduce(amt);
		if(mojo.get()<0){
			mojo.set(0);
		}
		if (c != null) {
			c.write(Global.capitalizeFirstLetter(String.format("%s <font color='rgb(150,150,250)'>%d<font color='white'> mojo.",this.subjectAction("lost", "lost"), amt)));
		}
		return amt;
	}
	public Area location(){
		return location;
	}
	public int init(){
		return att.get(Attribute.Speed)+Global.random(10);
	}
	public boolean nude(){
		return topless()&&pantsless();
	}
	public boolean topless(){
		for(Clothing article: top){
			if(article.attribute()!=Trait.ineffective){
				return false;
			}
		}
		return true;
	}
	public boolean pantsless(){
		for(Clothing article: bottom){
			if(article.attribute()!=Trait.ineffective){
				return false;
			}
		}
		return true;
	}
	public void dress(Combat c){
		for(Clothing article: outfit[0]){
			if(c.clothespile.contains(article)&&!top.contains(article))
			  top.push(article);
		}
		for(Clothing article: outfit[1]){
			if(c.clothespile.contains(article)&&!bottom.contains(article))
			  bottom.push(article);
		}
	}
	public void change(Modifier rule){
		top=(Stack<Clothing>) outfit[0].clone();
		bottom=(Stack<Clothing>) outfit[1].clone();
	}
	public String getName() {
		return name;
	}
	public void undress(Combat c){
		c.clothespile.addAll(top);
		c.clothespile.addAll(bottom);
		top.clear();
		bottom.clear();
	}
	public boolean nudify(){
		ArrayList<Clothing> temp = new ArrayList<Clothing>();
		temp.addAll(top);
		for(Clothing article: temp){
			if(article.attribute()!=Trait.indestructible){
				top.remove(article);
			}
		}
		temp.clear();
		temp.addAll(bottom);
		for(Clothing article: temp){
			if(article.attribute()!=Trait.indestructible){
				bottom.remove(article);
			}
		}
		return nude();
	}
	public Clothing strip(int half, Combat c){
		if(half==0){
			if(topless()){
				return null;
			}
			else{
				c.clothespile.add(top.peek());
				return top.pop();
			}
		}
		else{
			if(pantsless()){
				return null;
			}
			else{
				c.clothespile.add(bottom.peek());
				return bottom.pop();
			}
		}
	}
	public Clothing stripRandom(Combat c){
		int half;
		if (topless() && !pantsless()) {
			half = 1;
		} else if (topless() && !pantsless()) {
			half = 0;
		} else if (!topless() && !pantsless()) {
			half = Global.random(2);
		} else {
			return null;
		}
		if(half==0){
			if(topless()){
				return null;
			}
			else{
				c.clothespile.add(top.peek());
				return top.pop();
			}
		}
		else{
			if(pantsless()){
				return null;
			}
			else{
				c.clothespile.add(bottom.peek());
				return bottom.pop();
			}
		}
	}
	public Clothing shred(int half){
		if(half==0){
			if(!topless()&&top.peek().attribute()!=Trait.indestructible){
				return top.pop();
			}
			else{
				return null;
			}
		}
		else{
			if(!pantsless()&&bottom.peek().attribute()!=Trait.indestructible){
				return bottom.pop();
			}
			else{
				return null;
			}
		}
	}
	
	private void countdown(HashMap<Trait, Integer> counterSet) {
		Set<Trait> keys = new HashSet<Trait>(counterSet.keySet());
		for (Trait t : keys) {
			counterSet.put(t, counterSet.get(t) - 1);
			if (counterSet.get(t) <= 0) {
				counterSet.remove(t);
			}
		}
	}

	public void tick(Combat c) {
		body.tick(c);
		countdown(temporaryAddedTraits);
		countdown(temporaryRemovedTraits);
	}

	public Collection<Trait> getTraits() {
		Collection<Trait> allTraits = new HashSet<Trait>();
		allTraits.addAll(traits);
		allTraits.addAll(temporaryAddedTraits.keySet());
		allTraits.removeAll(temporaryRemovedTraits.keySet());
		return allTraits;
	}

	public boolean addTemporaryTrait(Trait t, int duration) {
		if (!getTraits().contains(t)) {
			temporaryAddedTraits.put(t, duration);
			return true;
		} else if (temporaryAddedTraits.containsKey(t)) {
			temporaryAddedTraits.put(t, Math.max(duration, temporaryAddedTraits.get(t)));
			return true;
		}
		return false;
	}

	public boolean removeTemporaryTrait(Trait t, int duration) {
		if (temporaryRemovedTraits.containsKey(t)) {
			temporaryRemovedTraits.put(t, Math.max(duration, temporaryRemovedTraits.get(t)));
			return true;
		} else if (traits.contains(t)) {
			temporaryRemovedTraits.put(t, duration);
			return true;
		}
		return false;
	}

	public void add(Trait t){
		traits.add(t);
	}
	public void remove(Trait t){
		traits.remove(t);
	}
	public boolean hasPure(Trait t){
		for(Clothing shirt:top){
			if(shirt.adds(t)){
				return true;
			}
		}
		for(Clothing pants:bottom){
			if(pants.adds(t)){
				return true;
			}
		}
		return getTraits().contains(t);
	}

	public boolean has(Trait t){
		boolean hasTrait = false;
		if (t.parent != null)
			hasTrait = hasTrait || getTraits().contains(t.parent);
		hasTrait = hasTrait || this.hasPure(t);
		return hasTrait;
	}

	public boolean hasDick(){
		return body.get("cock").size() > 0;
	}
	public boolean hasBalls(){
		return body.get("balls").size() > 0;
	}
	public boolean hasPussy(){
		return body.get("pussy").size() > 0;
	}
	public boolean hasBreasts(){
		return body.get("breasts").size() > 0;
	}
	public int countFeats(){
		int count = 0;
		for(Trait t: traits){
			if(t.isFeat()){
				count++;
			}
		}
		return count;
	}

	public void regen() {
		regen(null, false);
	}

	public void regen(Combat c) {
		regen(c, true);
	}

	public void regen(Combat c, boolean combat){
		int regen = 1;
		for(Status s: status){
			regen+=s.regen(c);
		}
		if(has(Trait.BoundlessEnergy)){
			regen+=1;
		}
		heal(null, regen);
		if(combat){
			if(has(Trait.exhibitionist)&&nude()){
				buildMojo(c, 5);
			}
			if(has(Trait.stylish)){
				buildMojo(c, 3);
			}
			if(has(Trait.SexualGroove)){
				buildMojo(c, 2);
			}
			if(has(Trait.lame)){
				buildMojo(c, -3);
			}
		}
	}

	public void add(Status status){
		add(null, status);
	}

	public void add(Combat c, Status status){
		boolean cynical = false;
		String message = "";
		for(Status s:this.status){
			if(s.flags().contains(Stsflag.cynical)){
				cynical = true;
			}
		}
		if(cynical && status.mindgames()){
			message = subjectAction("resist", "resists") + " " + status.describe() + " (Cynical).";
		} else {
			for (Resistance r : getResistances()) {
				String resistReason = "";
				resistReason = r.resisted(this, status);
				if (!resistReason .isEmpty()) { 
					message = subjectAction("resist", "resists") + " " + status.describe() + " (" + resistReason + ").";
					break;
				}
			}
		}
		if (message.isEmpty()){
			boolean unique = true;
			for (Status s : this.status) {
				if (s.getVariant().equals(status.getVariant())) {
					s.replace(status);
					message = subjectAction("are", "is") + " " + s.describe() + " again!";
					return;
				}
				if (s.overrides(status)) {
					unique = false;
				}
			}
			if (unique) {
				this.status.add(status);
				message = subjectAction("are", "is") + " now " + status.describe() + "!";
			}
		}
	}
	public void dropStatus(){
		status.removeAll(removelist);
		for(Status s: addlist){
			add(s);
		}
		removelist.clear();
		addlist.clear();
	}
	public boolean is(Stsflag sts){
		for(Status s: status){
			if(s.flags().contains(sts)){
				return true;
			}
		}
		return false;
	}
	public boolean is(Stsflag sts, String variant){
		for(Status s: status){
			if(s.flags().contains(sts) && s.getVariant().equals(variant)){
				return true;
			}
		}
		return false;
	}
	public boolean stunned(){
		for(Status s: status){
			if(s.flags().contains(Stsflag.stunned)){
				return true;
			}
		}
		return false;
	}
	public boolean distracted(){
		for(Status s: status){
			if(s.flags().contains(Stsflag.distracted)||s.flags().contains(Stsflag.trance)){
				return true;
			}
		}
		return false;
	}
	public boolean hasStatus(Stsflag flag){
		for(Status s: status){
			if(s.flags().contains(flag)){
				return true;
			}
		}
		return false;
	}
	public void removeStatus(Stsflag flag){
		for(Status s:status){
			if(s.flags().contains(flag)){
				removelist.add(s);
			}
		}
	}
	public boolean bound(){
		for(Status s: status){
			if(s.flags().contains(Stsflag.bound)){
				return true;
			}
		}
		return false;
	}
	public void free(){
		for(Status s:status){
			if(s.flags().contains(Stsflag.bound)){
				removelist.add(s);
			}
		}
	}
	
	public void struggle() {
		for(Status s: status){
			s.struggle(this);
		}
	}
	public int escape() {
		int total=0;
		for(Status s: status){
			total+=s.escape();
		}
		if(has(Trait.freeSpirit)){
			total+=5;
		}
		return total;
	}
	public boolean canAct(){
		return !(stunned()||distracted()||bound()||is(Stsflag.enthralled));
	}
	public boolean canRespond(){
		return !(stunned()||distracted()||is(Stsflag.enthralled));
	}
	public abstract void detect();
	public abstract void faceOff(Character opponent,Encounter enc);
	public abstract void spy(Character opponent,Encounter enc);
	public abstract String describe(int per);
	public abstract void victory(Combat c, Result flag);
	public abstract void defeat(Combat c, Result flag);
	public abstract void intervene3p(Combat c,Character target, Character assist);
	public abstract void victory3p(Combat c,Character target, Character assist);
	public abstract void act(Combat c);
	public abstract void move();
	public abstract void draw(Combat c, Result flag);
	public abstract boolean human();
	public abstract String bbLiner();
	public abstract String nakedLiner();
	public abstract String stunLiner();
	public abstract String winningLiner();
	public abstract String taunt();
	public abstract void intervene(Encounter enc, Character p1,Character p2);
	public abstract void showerScene(Character target, Encounter encounter);
	public void save(PrintWriter saver) {
		saver.write(name+"\n");
		saver.write(level+"\n");
		saver.write(getRank()+"\n");
		saver.write(xp+"\n");
		saver.write(money+"\n");
		for(Attribute a: att.keySet()){
			saver.write(a+" "+get(a)+"\n");
		}
		saver.write("@\n");
		saver.write(stamina.max()+"\n");
		saver.write(arousal.max()+"\n");
		saver.write(mojo.max()+"\n");
		saver.write(willpower.max()+"\n");
		for(Character player:affections.keySet()){
			saver.write(player.name()+" "+getAffection(player)+"\n");
		}
		saver.write("*\n");
		for(Character player2:attractions.keySet()){
			saver.write(player2.name()+" "+getAttraction(player2)+"\n");
		}
		saver.write("*\n");
		for(Clothing c:outfit[0]){
			saver.write(c.toString()+"\n");
		}
		saver.write("!\n");
		for(Clothing c:outfit[1]){
			saver.write(c.toString()+"\n");
		}
		saver.write("!!\n");
		for(Clothing c:closet){
			saver.write(c.toString()+"\n");
		}
		saver.write("!!!\n");
		for(Trait t: traits){
			saver.write(t.name()+"\n");
		}
		saver.write("!!!!\n");
		body.save(saver);
		saver.write("@\n");
		for(Item item: inventory.keySet()){
			saver.write(item.toString() + ":" + count(item) + "\n");
		}
		saver.write("$\n");
	}
	public void load(Scanner loader) {
		level=Integer.parseInt(loader.next());
		setRank(Integer.parseInt(loader.next()));
		xp=Integer.parseInt(loader.next());
		money=Integer.parseInt(loader.next());
		String e = loader.next();
		while(!e.equals("@")){
			set(Attribute.valueOf(e),Integer.parseInt(loader.next()));
			e = loader.next();
		}
		stamina.setMax(Integer.parseInt(loader.next()));
		arousal.setMax(Integer.parseInt(loader.next()));
		mojo.setMax(Integer.parseInt(loader.next()));
		willpower.setMax(Integer.parseInt(loader.next()));
		e = loader.next();
		while(!e.equals("*")){
			gainAffection(Global.lookup(e),Integer.parseInt(loader.next()));
			e = loader.next();
		}
		e = loader.next();
		while(!e.equals("*")){
			gainAttraction(Global.lookup(e),Integer.parseInt(loader.next()));
			e = loader.next();
		}
		e = loader.next();
		outfit[0].clear();
		while(!e.equals("!")){
			outfit[0].add(Clothing.valueOf(e));
			e = loader.next();
		}
		e=loader.next();
		outfit[1].clear();
		while(!e.equals("!")&&!e.equals("!!")){
			outfit[1].add(Clothing.valueOf(e));
			e = loader.next();
		}
		if(e.equals("!!")){
			e=loader.next();
			closet.clear();
			while(!e.equals("!!!")){
				closet.add(Clothing.valueOf(e));
				e = loader.next();
			}
		}
		e=loader.next();
		while(!e.equals("!!!!")){
			traits.add(Trait.valueOf(e));
			e = loader.next();
		}
		this.body = Body.load(loader, this);

		e = loader.next();
		int tries = 0;
		while(!e.equals("@")){
			if (tries > 50) {
				change(Modifier.normal);
				Global.gainSkills(this);
				return;
			}
			e = loader.next();
		}
		e=loader.next();
		while(!e.equals("$")){
			String itemString[] = e.split(":");
			int amt = itemString.length > 1 ? Integer.valueOf(itemString[1]) : 1;
			gain(Item.valueOf(itemString[0]), amt);
			e = loader.next();
		}
		change(Modifier.normal);
		Global.gainSkills(this);
		Global.learnSkills(this);
	}

	public abstract void afterParty();

	public void doOrgasm(Combat c, Character opponent, Skill last) {
		String opponentOrganType = "";
		String selfOrganType = "";
		if (last.user() == this) {
			opponentOrganType = last.getTargetOrganType(c, opponent);
			selfOrganType = last.getWithOrganType(c, this);
		} else {
			opponentOrganType = last.getWithOrganType(c, opponent);
			selfOrganType = last.getTargetOrganType(c, this);
		}
		c.write(this, "<br>");
		if (c.getStance().inserted(this) && !has(Trait.strapped)) {
			c.write(this, Global.format("<b>{self:SUBJECT-ACTION:tense|tenses} up as {self:possessive} hips wildly buck against {other:direct-object}. In no time, {self:possessive} hot seed spills into {other:possessive} pulsing hole.</b>", this, opponent));
			if (c.getStance().en == Stance.anal) {
				opponent.body.receiveCum(c, this, opponent.body.getRandom("ass"));
			} else {
				opponent.body.receiveCum(c, this, opponent.body.getRandom("pussy"));
			}
		} else if ((selfOrganType.equals("cock") && last.getSelf() == this) && !opponentOrganType.equals("none") ) {
			c.write(this, Global.format("<b>{self:NAME-POSSESSIVE} back arches as tick ropes of jizz fires from {self:possessive} dick and land on {other:name-possessive} " + last.getWithOrganType(c, opponent) + ".</b>", this, opponent));
			opponent.body.receiveCum(c, opponent, opponent.body.getRandom(opponentOrganType));
		} else {
			c.write(this, Global.format("<b>{self:SUBJECT-ACTION:shudder|shudders} as {other:subject-action:bring|brings} {self:direct-object} to a toe-curling climax.</b>", this, opponent));
		}
		c.write(this, "<b>" + orgasmLiner() + "</b>");
		c.write(opponent, opponent.makeOrgasmLiner());
		getArousal().empty();
		if (has(Trait.insatiable)) {
			arousal.restore((int) (arousal.max()*.2));
		}
		loseWillpower(c, getOrgasmWillpowerLoss());
	}

	public void loseWillpower(Combat c, int i) {
		if (has(Trait.strongwilled)) {
			i = i / 2 + 1;
		}
		willpower.reduce(i);
		c.write(String.format("%s lost <font color='rgb(220,130,40)'>%d<font color='white'> willpower" + (has(Trait.strongwilled) ? " (Strong-willed)." : "."), this.subject(), i));
	}

	public void restoreWillpower(Combat c, int i) {
		willpower.restore(i);
		c.write(String.format("%s regained <font color='rgb(181,230,30)'>%d<font color='white'> willpower.", this.subject(), i));
	}


	public void eot(Combat c, Character opponent, Skill last) {
		for(Status s: status){
			s.eot(c);
		}
		dropStatus();
		tick(c);
		List<String> removed = new ArrayList<String>();
		for (String s : cooldowns.keySet()) {
			if (cooldowns.get(s) <= 1) {
				removed.add(s);
			} else {
				cooldowns.put(s, cooldowns.get(s) - 1);
			}
		}
		for (String s : removed) {
			cooldowns.remove(s);
		}
		if (c.getStance().inserted()) {
			BodyPart selfOrgan;
			BodyPart otherOrgan;
			if (c.getStance().inserted(this)) {
				selfOrgan = body.getRandomCock();
				if (c.getStance().en == Stance.anal) {
					otherOrgan = opponent.body.getRandom("ass");
				} else {
					otherOrgan = opponent.body.getRandomPussy();
				}
			} else {
				otherOrgan = opponent.body.getRandomCock();
				if (c.getStance().en == Stance.anal) {
					selfOrgan = body.getRandom("ass");
				} else {
					selfOrgan = body.getRandomPussy();
				}
			}
			if (has(Trait.energydrain) && selfOrgan != null && otherOrgan != null) {
				c.write(this, Global.format("{self:NAME-POSSESSIVE} body glows purple as {other:subject-action:feel|feels} {other:possessive} very spirit drained into {self:possessive} " + selfOrgan.describe(this) + " through your connection.", this, opponent));
				int m = Global.random(5) + 5;
				opponent.weaken(c, m);
				heal(c, m);
			}
			//TODO this works weirdly when both have both organs.
			body.tickHolding(c, opponent, selfOrgan, otherOrgan);
		}
		if(opponent.has(Trait.magicEyeEnthrall)&&getArousal().percent()>=50&& c.getStance().facing() &&Global.random(20)==0){
			c.write(opponent,Global.format("<br>{other:NAME-POSSESSIVE} eyes start glowing and captures both {self:name-possessive} gaze and consciousness.", this, opponent));
			add(c, new Enthralled(this, opponent, 2));
		}
	}

	public String orgasmLiner() {
		return "";
	}
	public String makeOrgasmLiner() {
		return "";
	}

	private int getOrgasmWillpowerLoss() {
		return 25 + Global.random(25);
	}

	public abstract void emote(Emotion emo,int amt);
	public void learn(Skill copy) {
		skills.add(copy.copy(this));
	}
	public void forget(Skill copy) {
		skills.remove(copy);
	}
	public boolean stealthCheck(int perception) {
		return check(Attribute.Cunning,Global.random(20)+perception)||(state==State.hidden);
	}
	public boolean spotCheck(int perception){
		if(state==State.hidden){
			int dc = perception+10;
			if(has(Trait.Sneaky)){
				dc-=5;
			}
			return check(Attribute.Cunning,dc);
		}
		else{
			return true;
		}
	}
	public void travel(Area dest){
		state=State.ready;
		location.exit(this);
		location=dest;
		dest.enter(this);
		if (dest.name.isEmpty()) {
			throw new RuntimeException ("empty location");
		}
	}
	public void flee(Area location2) {
		Area[] adjacent = location2.adjacent.toArray(new Area[location2.adjacent.size()]);
		travel(adjacent[Global.random(adjacent.length)]);
		location2.endEncounter();
	}
	public void upkeep() {
		regen();
		tick(null);
		if(has(Trait.Confident)){
			willpower.restore(10);
			mojo.reduce(5);
		} else {
			willpower.restore(5);
			mojo.reduce(10);
		}
		if(bound()){
			free();
		}
		dropStatus();
		if(has(Trait.QuickRecovery)){
			heal(null, 4);
		}
		setChanged();
		notifyObservers();
	}
	public String debugMessage(Position p) {
		String mood;
		if (this instanceof NPC) {
			mood = "mood: " + ((NPC)this).mood.toString();
		} else {
			mood = "";
		}
		return String.format("[%s] %s s: %d/%d a: %d/%d m: %d/%d c:%d f:%f",
				name, mood,
				stamina.get(), stamina.max(),
				arousal.get(), arousal.max(),
				mojo.get(), mojo.max(), top.size() + bottom.size(),
				getFitness(getUrgency(), p));
	}

	public void gain(Item item) {
		gain(item, 1);
	}

	public void remove(Item item) {
		gain(item, -1);
	}

	public void gain(Clothing item) {
		closet.add(item);
	}

	public void gain(Item item, int q){
		int amt = 0;
		if (inventory.containsKey(item)) {
			amt = count(item);
		}
		inventory.put(item, Math.max(0, amt + q));
	}

	public boolean has(Item item){
		return has(item, 1);
	}

	public boolean has(Item item, int quantity){
		if (inventory.containsKey(item)) {
			return inventory.get(item) >= quantity;
		}
		return false;
	}

	public boolean has(Clothing item){
		return closet.contains(item);
	}

	public void consume(Item item, int quantity){
		if(has(Trait.resourceful)&&Global.random(5)==0){
			quantity--;
		}
		if (inventory.containsKey(item)) {
			gain(item, -quantity);
		}
	}
	public int count(Item item){
		if (inventory.containsKey(item))
			return inventory.get(item);
		return 0;
	}
	public void chargeBattery(){
		int power = count(Item.Battery);
		if(power<20){
			gain(Item.Battery,20-power);
		}
	}
	public void defeated(Character victor){
		mercy.add(victor);
	}
	public void resupply(){
		for(Character victor: mercy){
			victor.bounty();
		}
		mercy.clear();
		change(Global.getMatch().condition);
		state=State.ready;
		getWillpower().fill();
		if(location().present.size()>1){
			if(location().id()==Movement.dorm){
				if(Global.getMatch().gps("Quad").present.isEmpty()){
					if(human()){
						Global.gui().message("You hear your opponents searching around the dorm, so once you finish changing, you hop out the window and head to the quad.");
					}
					travel(Global.getMatch().gps("Quad"));
				}
				else{
					if(human()){
						Global.gui().message("You hear your opponents searching around the dorm, so once you finish changing, you quietly move downstairs to the laundry room.");
					}
					travel(Global.getMatch().gps("Laundry"));
				}
			}
			if(location().id()==Movement.union){
				if(Global.getMatch().gps("Quad").present.isEmpty()){
					if(human()){
						Global.gui().message("You don't want to be ambushed leaving the student union, so once you finish changing, you hop out the window and head to the quad.");
					}
					travel(Global.getMatch().gps("Quad"));
				}
				else{
					if(human()){
						Global.gui().message("You don't want to be ambushed leaving the student union, so once you finish changing, you sneak out the back door and head to the pool.");
					}
					travel(Global.getMatch().gps("Pool"));
				}
			}
		}
	}
	public void finishMatch(){
		for(Character victor: mercy){
			victor.bounty();
		}
		Global.gui().clearImage();
		mercy.clear();
		change(Global.getMatch().condition);
		clearStatus();
		temporaryAddedTraits.clear();
		temporaryRemovedTraits.clear();
		body.clearReplacements();
		getStamina().fill();
		getArousal().empty();
		getMojo().empty();
	}
	public void place(Area loc){
		location=loc;
		loc.present.add(this);
		if (loc.name.isEmpty()) {
			throw new RuntimeException ("empty location");
		}
	}
	public void bounty(){
		Global.getMatch().score(this);
	}
	public boolean eligible(Character p2) {
		return (!mercy.contains(p2))&&state!=State.resupplying;
	}
	public void setTrophy(Item trophy){
		this.trophy = trophy;
	}
	public Item getTrophy(){
		return trophy;
	}
	public void bathe(){
		status.clear();
		stamina.fill();
		state=State.ready;
	}
	public void masturbate(){
		arousal.empty();
		state=State.ready;
	}
	public void craft(){
		int roll = Global.random(15);
		if(check(Attribute.Cunning, 25)){
			if(roll==9){
				gain(Item.Aphrodisiac);
				gain(Item.DisSol);
			}
			else if(roll>=5){
				gain(Item.Aphrodisiac);
			}
			else{
				gain(Item.Lubricant);
				gain(Item.Sedative);
			}
		}
		else if(check(Attribute.Cunning, 20)){
			if(roll==9){
				gain(Item.Aphrodisiac);
			}
			else if(roll>=7){
				gain(Item.DisSol);
			}
			else if(roll>=5){
				gain(Item.Lubricant);
			}
			else if(roll>=3){
				gain(Item.Sedative);
			}
			else{
				gain(Item.EnergyDrink);
			}
		}
		else if(check(Attribute.Cunning, 15)){
			if(roll==9){
				gain(Item.Aphrodisiac);
			}
			else if(roll>=8){
				gain(Item.DisSol);
			}
			else if(roll>=7){
				gain(Item.Lubricant);
			}
			else if(roll>=6){
				gain(Item.EnergyDrink);
			}
		}
		else{
			if(roll>=7){
				gain(Item.Lubricant);
			}
			else if(roll>=5){
				gain(Item.Sedative);
			}
		}
		state=State.ready;
	}
	public void search(){
		int roll = Global.random(15);
		switch(roll){
		case 9:
			gain(Item.Tripwire);
			gain(Item.Tripwire);
			break;
		case 8:
			gain(Item.ZipTie);
			gain(Item.ZipTie);
			gain(Item.ZipTie);
			break;
		case 7:
			gain(Item.Phone);
			break;
		case 6:
			gain(Item.Rope);
			break;
		case 5:
			gain(Item.Spring);
		}
		state=State.ready;
	}
	public abstract String challenge();
	public void delay(int i){
		busy+=i;
	}
	public abstract void promptTrap(Encounter enc,Character target,Trap trap);
	public int lvlBonus(Character opponent){
		if(opponent.getLevel()>this.getLevel()){
			return 10*(opponent.getLevel()-this.getLevel());
		}
		else{
			return 0;
		}
	}

	public int getVictoryXP(Character opponent) {
		return 20 + lvlBonus(opponent);
	}

	public int getAssistXP(Character opponent) {
		return 10 + lvlBonus(opponent);
	}

	public int getDefeatXP(Character opponent) {
		return 20 + lvlBonus(opponent);
	}

	public int getAttraction(Character other){
		if(attractions.containsKey(other)){
			return attractions.get(other);
		}
		else{
			return 0;
		}
	}
	public void gainAttraction(Character other, int x){
		if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
			System.out.printf("%s gained affection for %s\n", this.name(), other.name());
		}
		if(attractions.containsKey(other)){
			attractions.put(other,attractions.get(other)+x);
		}
		else{
			attractions.put(other, x);
		}
	}
	public int getAffection(Character other){
		if(affections.containsKey(other)){
			return affections.get(other);
		}
		else{
			return 0;
		}
	}
	public void gainAffection(Character other, int x){
		if (Global.isDebugOn(DebugFlags.DEBUG_SCENE)) {
			System.out.printf("%s gained affection for %s\n", this.name(), other.name());
		}
		if(affections.containsKey(other)){
			affections.put(other,affections.get(other)+x);
		}
		else{
			affections.put(other, x);
		}
	}
	public int ac(){
		int ac = getLevel()+get(Attribute.Perception)+get(Attribute.Speed);
		for(Status s: status){
			ac += s.evade();
		}
		if(has(Trait.clairvoyance)){
			ac += 5;
		}
		return ac;
	}

	public int counterChance(Character opponent, Skill skill){
		int counter = 0;
		counter += Math.max(0,get(Attribute.Cunning) - opponent.get(Attribute.Cunning)) / 2;
		counter += get(Attribute.Perception);
		counter += (get(Attribute.Speed) - opponent.get(Attribute.Speed))/2;
		counter += 5 - skill.accuracy();
		for(Status s: status){
			counter += s.counter();
		}
		if(has(Trait.clairvoyance)){
			counter += 3;
		}
		if(has(Trait.aikidoNovice)){
			counter +=3;
		}
		if(opponent.is(Stsflag.countered)) {
			counter -= 10;
		}
		return Math.max(0, counter);
	}

	public int tohit(){
		int hit = get(Attribute.Speed)+getLevel();
		return hit;
	}
	public boolean roll(Skill attack, Combat c, int accuracy){
		int attackroll = attack.user().tohit() + accuracy + 2 + Global.random(8) + Global.random(8);
		return attackroll > ac();
	}

	public int knockdownDC() {
		int dc = 10+(getStamina().get())/2;
		if(is(Stsflag.braced)) {
			dc+=getStatus(Stsflag.braced).value();
		}
		return dc;
	}

	public abstract void counterattack(Character target, Tactics type, Combat c);

	public void clearStatus(){
		status.clear();
	}
	public Status getStatus(Stsflag flag){
		for(Status s: status){
			if(s.flags().contains(flag)){
				return s;
			}
		}
		return null;
	}
	public Integer prize() {
		if(getRank()==1){
			return 200;
		}
		else{
			return 50;
		}
	}
	public Move findPath(Area target) {
		if (this.location.name.equals(target.name))
			return null;
		ArrayDeque<Area> queue = new ArrayDeque<Area>();
		Vector<Area> vector = new Vector<Area>();
		HashMap<Area, Area> parents = new HashMap<Area,Area>();
		queue.push(this.location);
		vector.add(this.location);
		Area last = null;
		while (!queue.isEmpty()) {
			Area t = queue.pop();
			parents.put(t, last);
			if (t.name.equals(target.name)) {
				while (!this.location.adjacent.contains(t)) {
					t = parents.get(t);
				}
				return new Move(t);
			}
			for (Area area : t.adjacent) {
				if (!vector.contains(area)) {
					vector.add(area);
					queue.push(area);
				}
			}
			last = t;
		}
		return null;
	}
	public boolean knows(Skill skill){
		for(Skill s:skills){
			if(s.equals(skill)){
				return true;
			}
		}
		return false;
	}
	public void endofbattle(){
		for(Status s: status){
			if(!s.lingering())
			  removelist.add(s);
		}
		if(pet!=null){
			pet.remove();
		}
		cooldowns.clear();
		dropStatus();
	}
	public boolean canSpend(int mojo){
		int cost=mojo;
		for(Status s:status){
			cost+=s.spendmojo(mojo);
		}
		return getMojo().get()>=cost;
	}
	public Map<Item, Integer> getInventory() {
		return inventory;
	}
	public ArrayList<String> listStatus() {
		ArrayList<String> result = new ArrayList<String>();
		for(Status s: status){
			result.add(s.toString());
		}
		return result;
	}
	public String dumpstats() {
		StringBuilder b = new StringBuilder();
		b.append("<b>");
		b.append(name()+": Level "+getLevel()+"; ");
		for(Attribute a: att.keySet()){
			b.append(a.name()+" "+att.get(a)+", ");
		}
		b.append("</b>");
		b.append("<br>Max Stamina "+stamina.max()+", Max Arousal "+arousal.max()+", Max Mojo "+mojo.max()+", Max Willpower "+willpower.max()+".");
		b.append("<br>");
		body.describeNotable(b, this);
		if (getTraits().size() > 0) {
			b.append("<br>Traits:<br>");
			for (Trait t : getTraits()) {
				b.append(t + ": " + t.getDesc());
				b.append("<br>");
			}
		}
		b.append("</p>");

		return b.toString();
	}
	public void accept(Challenge c){
		challenges.add(c);
	}
	public void evalChallenges(Combat c, Character victor){
		for(Challenge chal: challenges){
			chal.check(c, victor);
		}
	}
	public float getUrgency() {

         // How urgent do we want the fight to end? We are
         // essentially trying to approximate how many
         // moves we will still have until the end of the
         // fight.

        // Once arousal is too high, the fight ends.
         float remArousal = getArousal().max() - getArousal().get();

         // Arousal is worse the more clothes we lose, because
         // we will probably lose it quicker.
         float arousalPerRound = 5;
         for(int i = 0; i < top.size(); i++) arousalPerRound /= 1.2;
         for(int i = 0; i < bottom.size(); i++) arousalPerRound /= 1.2;

         // Depending on our stamina, we should count on
         // spending a few of these rounds knocked down,
         // so effectively we have even less rounds to
         // work with - let's make them count!
         float fitness = Math.max(0.5f, Math.min(1.0f,
                         (float) getStamina().get() / 40));

         // cap urgency so that very high maximum arousal does not round fitness to 0
         return Math.min(100, remArousal / arousalPerRound * fitness);
	}
	public float rateUrgency(float urgency, float urgency2) {
		// How important is it to reach a goal with given
		// urgency, given that we are gunning roughly for
		// the second urgency? We use a normal distribution
		// (hah, fancy!) that gives roughly 50% weight at
		// a difference of 15.
		float x = (urgency - urgency2) / 15;
		return (float) Math.exp(-x*x);
	}

	public boolean taintedFluids() {
		return Global.random(get(Attribute.Dark)/4 + 5) >= 4;
	}
	
	protected void pickSkillsWithGUI(Combat c, Character target) {
		if (Global.isDebugOn(DebugFlags.DEBUG_SKILL_CHOICES)) {
			c.write(this, nameOrPossessivePronoun() + " turn...");
			c.updateAndClearMessage();
		}
		HashSet<Skill> available = new HashSet<Skill>();
		HashSet<Skill> cds = new HashSet<Skill>();
		for(Skill a:skills){
			if(Skill.skillIsUsable(c, a, target)) {
				if (cooldownAvailable(a)) {
					available.add(a);
				} else {
					cds.add(a);
				}
			}
		}
		HashSet<Skill> damage = new HashSet<Skill>();
		HashSet<Skill> pleasure = new HashSet<Skill>();
		HashSet<Skill> fucking = new HashSet<Skill>();
		HashSet<Skill> position = new HashSet<Skill>();
		HashSet<Skill> debuff = new HashSet<Skill>();
		HashSet<Skill> recovery = new HashSet<Skill>();
		HashSet<Skill> summoning = new HashSet<Skill>();
		HashSet<Skill> stripping = new HashSet<Skill>();
		HashSet<Skill> misc = new HashSet<Skill>();
		Skill.filterAllowedSkills(c, available, this, target);
		if (available.size() == 0) {
			available.add(new Nothing(this));
		}
		available.addAll(cds);
		for (Skill a : available) {
			if(a.type(c)==Tactics.damage){
				damage.add(a);
			}
			else if(a.type(c)==Tactics.pleasure){
				pleasure.add(a);
			}else if(a.type(c)==Tactics.fucking){
				fucking.add(a);
			}
			else if(a.type(c)==Tactics.positioning){
				position.add(a);
			}
			else if(a.type(c)==Tactics.debuff){
				debuff.add(a);
			}
			else if(a.type(c)==Tactics.recovery||a.type(c)==Tactics.calming){
				recovery.add(a);
			}
			else if(a.type(c)==Tactics.summoning){
				summoning.add(a);
			}
			else if(a.type(c)==Tactics.stripping){
				stripping.add(a);
			}
			else{
				misc.add(a);
			}
		}
		Global.gui().clearCommand();
		for(Skill a: stripping){
			Global.gui().addSkill(a,c);
		}
		for(Skill a: position){
			Global.gui().addSkill(a,c);
		}
		for(Skill a: fucking){
			Global.gui().addSkill(a,c);
		}
		for(Skill a: pleasure){
			Global.gui().addSkill(a,c);
		}
		if(Global.getMatch().condition!=Modifier.pacifist){
			for(Skill a: damage){
				Global.gui().addSkill(a,c);
			}
		}
		for(Skill a: debuff){
			Global.gui().addSkill(a,c);
		}
		for(Skill a: summoning){
			Global.gui().addSkill(a,c);
		}
		for(Skill a: recovery){
			Global.gui().addSkill(a,c);
		}
		for(Skill a: misc){
			Global.gui().addSkill(a,c);
		}
		Global.gui().showSkills(0);
	}

	public float getFitness(float urgency, Position p) {
		float fit = 0;
        // Urgency marks
		float ushort = 1.0f;
		float umid = 1.0f;
        float ulong = 1.0f;
        float usum = ushort + umid + ulong;
        Character other = p.other(this);
		// Always important: Position
		fit += p.priorityMod(this) * 12;
		// Also important: Clothing.
		float topMultiplier = 2.0f;
		float botMultiplier = 3.0f;
		if (hasDick()) {
			botMultiplier += 4;
		}
		if (has(Trait.submissive)) {
			botMultiplier = -5;
			topMultiplier = -5;
		}
		if (has(Trait.pheromones)) {
			topMultiplier -= 1;
			botMultiplier -= 2;
		}
		fit += top.size() * topMultiplier + bottom.size() * botMultiplier;
		fit += this.body.getCharismaBonus(other) * (other.getArousal().percent()) / 2;
		// Also somewhat of a factor: Inventory (so we don't
		// just use it without thinking)
		for(Item item : inventory.keySet())
		        fit += (float) item.getPrice() / 10;
		// Extreme situations
		if (arousal.isFull()) fit -= 100;
		if (stamina.isEmpty()) fit -= umid * 3;
		fit += 100.0f * (getWillpower().max() - getWillpower().get()) / Math.min(100, getWillpower().max());
		// Short-term: Arousal
		fit += ushort / usum * 100.0f * (getArousal().max() - getArousal().get()) / Math.min(100, getArousal().max());
		// Mid-term: Stamina
		fit += umid / usum * 50.0f * ( 1 - Math.exp(-((float)getStamina().get()) / Math.min(getStamina().max(), 100.0f)));
		// Long term: Mojo
		fit += ulong / usum * 50.0f * ( 1 - Math.exp(- ((float)getMojo().get()) / Math.min(getMojo().max(), 40.0f)));
		for (Status status : this.status) {
			fit += status.fitnessModifier();
		}
		return fit;
	}

	public String nameOrPossessivePronoun() {
		return name + "'s";
	}

	public int getSkimpiness(){
		int result = 2;
		for(Clothing article: top){
			if(article.attribute()!=Trait.skimpy&&article.attribute()!=Trait.ineffective){
				result--;
				break;
			}
		}
		for(Clothing article: bottom){
			if(article.attribute()!=Trait.skimpy&&article.attribute()!=Trait.ineffective){
				result--;
				break;
			}
		}
		return result;
	}
	public abstract String getPortrait();

	public void gainMoney(int i) {
		if (human())
			Global.gui().message("You've gained $" + Math.round(i * Global.moneyRate) +".");;
		money += Math.round(i * Global.moneyRate);
	}
	public void loseXP(int i) {
		assert(i >= 0);
		xp -= i;
	}
	public String pronoun() {
		if (hasPussy()) {
			return "she";
		} else {
			return "he";
		}
	}
	public Emotion getMood() {
		return Emotion.confident;
	}
	public String possessivePronoun() {
		if (hasPussy()) {
			return "her";
		} else {
			return "his";
		}
	}
	public String directObject() {
		if (hasPussy()) {
			return "her";
		} else {
			return "him";
		}
	}

	public String nameDirectObject() {
		return name();
	}

	public String reflectivePronoun() {
		return possessivePronoun() + "self";
	}

	public boolean clothingFuckable(BodyPart part) {
		if (part.isType("strapon")) {
			return true;
		} if (part.isType("cock")) {
			for(Clothing article: bottom){
				if(article.buff()==Trait.armored){
					return false;
				}
			}
			return true;
		} else if (part.isType("pussy") || part.isType("ass")) {
			for(Clothing article: bottom){
				Trait attribute = article.attribute();
				if(attribute != Trait.skimpy || attribute != Trait.ineffective || attribute != Trait.flexible){
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	public double pussyPreference() {
		return 10;
	}
	public double dickPreference() {
		return 1;
	}
	public boolean wary() {
		return hasStatus(Stsflag.wary);
	}

	public void gain(Combat c, Item item) {
		if (c != null)
			c.write(Global.format("<b>{self:subject-action:have|has} gained "+item.pre()+item.getName()+"</b>", this, this));
		gain(item, 1);
	}
	public String temptLiner(Character target) {
		//placeholder
		return Global.format("{self:SUBJECT-ACTION:tempt|tempts} {other:direct-object}.", this, target);
	}
	public String action(String firstPerson, String thirdPerson) {
		return thirdPerson;
	}

	public void addCooldown(Skill skill) {
		if (skill.getCooldown() <= 0) { return; }
		if (cooldowns.containsKey(skill.toString())) {
			cooldowns.put(skill.toString(), cooldowns.get(skill.toString()) + skill.getCooldown());
		} else {
			cooldowns.put(skill.toString(), skill.getCooldown());
		}
	}

	public boolean cooldownAvailable(Skill s) {
		boolean cooledDown = true;
		if (cooldowns.containsKey(s.toString()) && cooldowns.get(s.toString()) > 0) {
			cooledDown = false;
		}
		return cooledDown;
	}
	public Object getCooldown(Skill s) {
		if (cooldowns.containsKey(s.toString()) && cooldowns.get(s.toString()) > 0) {
			return cooldowns.get(s.toString());
		} else {
			return 0;
		}
	}
	public boolean checkLoss() {
		return willpower.isEmpty();
	}
}