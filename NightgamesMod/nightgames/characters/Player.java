package nightgames.characters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nightgames.actions.Action;
import nightgames.actions.Move;
import nightgames.actions.Shortcut;
import nightgames.areas.Area;
import nightgames.areas.Deployable;
import nightgames.characters.body.BodyPart;
import nightgames.combat.Combat;
import nightgames.combat.Encounter;
import nightgames.combat.Result;
import nightgames.global.Global;
import nightgames.gui.GUI;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;
import nightgames.stance.Behind;
import nightgames.stance.Neutral;
import nightgames.stance.Position;
import nightgames.status.Enthralled;
import nightgames.status.Feral;
import nightgames.status.Horny;
import nightgames.status.Masochistic;
import nightgames.status.Status;
import nightgames.status.Stsflag;
import nightgames.trap.Trap;


public class Player extends Character {
	/**
	 * 
	 */
	public GUI gui;
	public String sex;
	private Growth growth;
	public int traitPoints;

	public Player(String name, CharacterSex sex) {
		super(name, 1);
		if (sex == CharacterSex.female || sex == CharacterSex.herm) {
			outfitPlan.add(Clothing.getByID("bra"));
			outfitPlan.add(Clothing.getByID("panties"));
		} else {
			outfitPlan.add(Clothing.getByID("boxers"));
		}
		outfitPlan.add(Clothing.getByID("Tshirt"));
		outfitPlan.add(Clothing.getByID("jeans"));
		outfitPlan.add(Clothing.getByID("socks"));
		outfitPlan.add(Clothing.getByID("sneakers"));

		willpower.setMax(willpower.max());
		change();
		availableAttributePoints=0;
		setTrophy(Item.PlayerTrophy);
		body.finishBody(sex);
		growth = new Growth();
		setGrowth();
	}

	public Player(String name) {
		this(name, CharacterSex.male);
	}

	public void setGrowth() {
		growth.stamina = 2;
		growth.arousal = 4;
		growth.mojo = 2;
		growth.bonusStamina = 1;
		growth.bonusArousal = 2;
		growth.bonusMojo = 1;
	}

	public String describeStatus() {
		StringBuilder b = new StringBuilder();
		body.describeBodyText(b, this, false);
		if (getTraits().size() > 0) {
			b.append("<br>Traits:<br>");
			List<Trait> traits = new ArrayList<>(getTraits());
			traits.sort((first, second) -> first.toString().compareTo(second.toString()));
			b.append(traits.stream().map(Object::toString).collect(Collectors.joining(", ")));
		}
		if (status.size() > 0) {
			b.append("<br><br>Statuses:<br>");
			List<Status> statuses= new ArrayList<>(status);
			statuses.sort((first, second) -> first.name.compareTo(second.name));
			b.append(statuses.stream().map(s -> s.name).collect(Collectors.joining(", ")));
		}
		return b.toString();	
	}

	@Override
	public String describe(int per, Combat c) {
		String description="<i>";
		for(Status s:status){
			description = description+s.describe(c)+"<br>";
		}
		description = description+"</i>";
		description = description + outfit.describe(this);
		if(per>=5 && this.status.size() > 0){
			description += "<br>List of statuses:<br><i>";
			for (Status s : this.status) {
				description += s + ", ";
			}
			description += "</i><br>";
		}
		return description;
	}

	@Override
	public void victory(Combat c, Result flag) {
		if(c.getStance().inserted() && c.getStance().dom(this)){
			getMojo().gain(2);
			if(has(Trait.mojoMaster)){
				getMojo().gain(2);
			}
		}
		if(c.p1.human()){
			c.p2.defeat(c,flag);
		}
		else{
			c.p1.defeat(c,flag);
		}
	}

	@Override
	public void defeat(Combat c, Result flag) {
		c.write("Bad thing");
	}

	@Override
	public void act(Combat c) {
		Character target;
		if(c.p1==this){
			target=c.p2;
		}
		else{
			target=c.p1;
		}
		pickSkillsWithGUI(c, target);
	}

	@Override
	public boolean human() {
		return true;
	}

	@Override
	public void draw(Combat c, Result flag) {
		if(c.getStance().inserted()){
			c.p1.getMojo().gain(3);
			c.p2.getMojo().gain(3);
		}
		if(c.p1.human()){
			c.p2.draw(c,flag);
		}
		else{
			c.p1.draw(c,flag);
		}

	}

	@Override
	public String bbLiner(Combat c) {
		return null;
	}

	@Override
	public String nakedLiner(Combat c) {
		return null;
	}

	@Override
	public String stunLiner(Combat c) {
		return null;
	}

	@Override
	public String taunt(Combat c) {
		return null;
	}

	@Override
	public void detect() {
		for(Area adjacent:location.adjacent){
			if(adjacent.ping(get(Attribute.Perception))){
				Global.gui().message("You hear something in the <b>"+adjacent.name+"</b>.");
			}
		}
	}
	@Override
	public void faceOff(Character opponent, Encounter enc) {
		gui.message("You run into <b>"+opponent.name+"</b> and you both hesitate for a moment, deciding whether to attack or retreat.");
		assessOpponent(opponent);
		gui.promptFF(enc,opponent);
	}

	private void assessOpponent(Character opponent){
		String arousal;
		String stamina;
		if(opponent.state==State.webbed){
			gui.message("She is naked and helpless<br>");
			return;
		}
		if(get(Attribute.Perception)>=6){
			gui.message("She is level "+opponent.getLevel());
		}
		if(get(Attribute.Perception)>=8){
			gui.message("Her Power is "+opponent.get(Attribute.Power)+", her Cunning is "+opponent.get(Attribute.Cunning)+", and her Seduction is "+opponent.get(Attribute.Seduction));
		}
		if(opponent.mostlyNude()||opponent.state==State.shower){
			gui.message("She is completely naked.");
		}
		else{
			gui.message("She is dressed and ready to fight.");
		}
		if(get(Attribute.Perception)>=4){
			if(opponent.getArousal().percent()>70){
				arousal="horny";
			}
			else if(opponent.getArousal().percent()>30){
				arousal="slightly aroused";
			}
			else{
				arousal="composed";
			}
			if(opponent.getStamina().percent()<50){
				stamina="tired";
			}
			else{
				stamina="eager";
			}
			gui.message("She looks "+stamina+" and "+arousal+".");
		}
	}
	@Override
	public void spy(Character opponent, Encounter enc) {
		gui.message("You spot <b>"+opponent.name+"</b> but she hasn't seen you yet. You could probably catch her off guard, or you could remain hidden and hope she doesn't notice you.");
		assessOpponent(opponent);
		gui.promptAmbush(enc, opponent);
	}

	@Override
	public void move() {
		gui.clearCommand();
		if(state==State.combat){
			if(!location.fight.battle()){
				Global.getMatch().resume();
			}
		}
		else if(busy>0){
			busy--;
		}
		else if (this.is(Stsflag.enthralled)) {
			Character master;
			master = ((Enthralled)getStatus(Stsflag.enthralled)).master;
			if(master!=null){
			Move compelled = findPath(master.location());
			gui.message("You feel an irresistible compulsion to head to the <b>"
					+ master.location().name+"</b>");
			if (compelled != null)
				this.gui.addAction(compelled, this);
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
			gui.message("You eventually manage to get an arm free, which you then use to extract yourself from the trap.");
			state=State.ready;
		}
		else if(state==State.masturbating){
			masturbate();
		}
		else{
			gui.message(location.description+"<p>");
			for(Deployable trap: location.env){
				if(trap.owner()==this){
					gui.message("You've set a "+trap.toString()+" here.");
				}
			}
			detect();
			if(!location.encounter(this)){
				for(Area path:location.adjacent){
					gui.addAction(new Move(path),this);
				}
				if (getPure(Attribute.Cunning) >= 28) {
			         for (Area path : this.location.shortcut) {
			           gui.addAction(new Shortcut(path), this);
			         }
			    }
				for(Action act:Global.getActions()){
					if(act.usable(this)){
						gui.addAction(act,this);
					}
				}
			}
		}
	}

	public void ding(){
		level++;
		getStamina().gain(growth.stamina);
		getArousal().gain(growth.arousal);
		getMojo().gain(growth.mojo);
		availableAttributePoints += growth.attributes[rank];
		getMojo().gain(1);
		gui.message("You've gained a Level!<br>Select which attributes to increase.");
		if ((getLevel() % 3 == 0 && level < 10) || ((getLevel() + 1) % 2 == 0 && level > 10)) {
			traitPoints += 1;
		}
		gui.ding();
	}

	public void flee(Area location2) {
		Area[] adjacent = location2.adjacent.toArray(new Area[location2.adjacent.size()]);
		Area destination = adjacent[Global.random(adjacent.length)];
		gui.message("You dash away and escape into the <b>"+destination.name+"</b>");
		travel(destination);
		location2.endEncounter();
	}
	public void bathe(){
		status.clear();
		stamina.fill();
		if(location.name=="Showers"){
			gui.message("You let the hot water wash away your exhaustion and soon you're back to peak condition");
		}
		if(location.name=="Pool"){
			gui.message("The hot water soothes and relaxes your muscles. You feel a bit exposed, skinny-dipping in such an open area. You decide it's time to get moving.");
		}
		if(state==State.lostclothes){
			gui.message("Your clothes aren't where you left them. Someone must have come by and taken them.");
		}
		state=State.ready;
	}
	public void craft(){
		int roll = Global.random(10);
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
			else{
				gui.message("Your concoction turns a sickly color and releases a foul smelling smoke. You trash it before you do any more damage.");
			}
		}
		else{
			if(roll>=7){
				gain(Item.Lubricant);
			}
			else if(roll>=5){
				gain(Item.Sedative);
			}
			else{
				gui.message("Your concoction turns a sickly color and releases a foul smelling smoke. You trash it before you do any more damage.");
			}
		}
		state=State.ready;
	}
	public void search(){
		int roll = Global.random(10);
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
			break;
		default:
			gui.message("You don't find anything useful");
		}
		state=State.ready;
	}
	public void masturbate(){
		gui.message("You hurriedly stroke yourself off, eager to finish before someone catches you. After what seems like an eternity, you ejaculate into a tissue and " +
				"throw it in the trash. Looks like you got away with it.");
		arousal.empty();
		state=State.ready;
	}

	@Override
	public void showerScene(Character target, Encounter encounter) {
		if(target.location().name=="Showers"){
			gui.message("You hear running water coming from the first floor showers. There shouldn't be any residents on this floor right now, so it's likely one " +
					"of your opponents. You peek inside and sure enough, <b>"+target.name()+"</b> is taking a shower and looking quite vulnerable. Do you take advantage " +
					"of her carelessness?");
		}
		else if(target.location().name=="Pool"){
			gui.message("You stumble upon <b>"+target.name+"</b> skinny dipping in the pool. She hasn't noticed you yet. It would be pretty easy to catch her off-guard.");
		}
		assessOpponent(target);
		gui.promptShower(encounter, target);
	}

	@Override
	public void intervene(Encounter enc, Character p1, Character p2) {
		gui.message("You find <b>"+p1.name()+"</b> and <b>"+p2.name()+"</b> fighting too intensely to notice your arrival. If you intervene now, it'll essentially decide the winner.");
		gui.promptIntervene(enc,p1,p2);
	}

	@Override
	public void intervene3p(Combat c, Character target, Character assist) {
		this.gainXP(getAssistXP(target));
		target.defeated(this);
		assist.gainAttraction(this, 1);
		c.write("You take your time, approaching "+target.name()+" and "+assist.name()+" stealthily. "+assist.name()+" notices you first and before her reaction " +
				"gives you away, you quickly lunge and grab "+target.name()+" from behind. She freezes in surprise for just a second, but that's all you need to " +
				"restrain her arms and leave her completely helpless. Both your hands are occupied holding her, so you focus on kissing and licking the " +
				"sensitive nape of her neck.<p>");
	}

	@Override
	public void victory3p(Combat c, Character target, Character assist) {
		this.gainXP(getVictoryXP(target));
		target.gainXP(getDefeatXP(this));
		target.arousal.empty();
		if(target.has(Trait.insatiable)){
			target.arousal.restore((int) (arousal.max()*.2));
		}
		dress(c);
		target.undress(c);
		this.gainTrophy(c, target);
		target.defeated(this);
		if (target.hasDick()) {
		       c.write(String.format("You position yourself between %s's legs, gently "
		       		+ "forcing them open with your knees. %s dick stands erect, fully "
		       		+ "exposed and ready for attention. You grip the needy member and "
		       		+ "start jerking it with a practiced hand. %s moans softly, but seems"
		       		+ " to be able to handle this level of stimulation. You need to turn "
		       		+ "up the heat some more. Well, if you weren't prepared to suck a cock"
		       		+ " or two, you may have joined the wrong competition. You take just "
		       		+ "the glans into your mouth, attacking the most senstitive area with "
		       		+ "your tongue. %s lets out a gasp and shudders. That's a more promising "
		       		+ "reaction.<p>You continue your oral assault until you hear a breathy "
		       		+ "moan, <i>\"I'm gonna cum!\"</i> You hastily remove %s dick out of "
		       		+ "your mouth and pump it rapidly. %s shoots %s load into the air, barely "
		       		+ "missing you.", new Object[] {
		         target.name(), Global.capitalizeFirstLetter(target.possessivePronoun()), target.name(), 
		         Global.capitalizeFirstLetter(target.pronoun()), target.possessivePronoun(), 
		         target.name(), target.possessivePronoun() }));
		     } else {
		c.write(target.name()+"'s arms are firmly pinned, so she tries to kick you ineffectually. You catch her ankles and slowly begin kissing and licking your way " +
				"up her legs while gently, but firmly, forcing them apart. By the time you reach her inner thighs, she's given up trying to resist. Since you no " +
				"longer need to hold her legs, you can focus on her flooded pussy. You pump two fingers in and out of her while licking and sucking her clit. In no " +
				"time at all, she's trembling and moaning in orgasm.");
		     }
		gainAttraction(target,1);
		target.gainAttraction(this,1);
	}
	
	@Override
	public void gain(Item item) {
		Global.gui().message("<b>You've gained "+item.pre()+item.getName()+"</b>");
		super.gain(item);
	}

	@Override
	public String challenge(Character other) {
		return null;
	}

	@Override
	public void promptTrap(Encounter enc,Character target,Trap trap) {
		Global.gui().message("Do you want to take the opportunity to ambush <b>"+target.name()+"</b>?");
		assessOpponent(target);
		gui.promptOpportunity(enc, target, trap);		
	}

	@Override
	public void afterParty() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void counterattack(Character target, Tactics type, Combat c) {
		switch(type){
		case damage:
			c.write(this, "You dodge "+target.name()+"'s slow attack and hit her sensitive tit to stagger her.");
			target.pain(c, 4+Math.min(Global.random(get(Attribute.Power)), 20));
			break;
		case pleasure:
			if(!target.crotchAvailable() || !target.hasPussy()){
				c.write(this, "You pull "+target.name()+" off balance and lick her sensitive ear. She trembles as you nibble on her earlobe.");
				target.body.pleasure(this, body.getRandom("tongue"), target.body.getRandom("ears"), 4+Math.min(Global.random(get(Attribute.Seduction)), 20), c);
			}
			else{
				c.write(this, "You pull "+target.name()+" to you and rub your thigh against her girl parts.");
				target.body.pleasure(this, body.getRandom("feet"), target.body.getRandomPussy(), 4+Math.min(Global.random(get(Attribute.Seduction)), 20), c);
			}
			break;
		case fucking:
			if (c.getStance().sub(this)) {
				Position reverse = c.getStance().reverse(c);
				if (reverse != c.getStance() && !BodyPart.hasOnlyType(reverse.bottomParts(), "strapon")) {
					c.setStance(reverse);
				} else {
					c.write(this, Global.format("{self:NAME-POSSESSIVE} quick wits find a gap in {other:name-possessive} hold and {self:action:slip|slips} away.", this, target));
					c.setStance(new Neutral(this, target));
				}
			} else {
				target.body.pleasure(this, body.getRandom("hands"), target.body.getRandomBreasts(),
						4 + Math.min(Global.random(get(Attribute.Seduction)), 20), c);
				c.write(this,
						Global.format(
								"{self:SUBJECT-ACTION:pinch|pinches} {other:possessive} nipples with {self:possessive} hands as {other:subject-action:try|tries} to fuck {self:direct-object}. "
										+ "While {other:subject-action:yelp|yelps} with surprise, {self:subject-action:take|takes} the chance to pleasure {other:possessive} body",
								this, target));
			}
			break;
		case stripping:
			Clothing clothes = target.stripRandom(c);
			if (clothes != null) {
				c.write(this, "You manage to catch " +target.possessivePronoun() + " hands groping your clothing, and in a swift motion you strip off her " + clothes.getName() + " instead.");
			} else {
				c.write(this, "You manage to dodge " + target.possessivePronoun() + " groping hands and give a retaliating slap in return");
				target.pain(c, 4+Math.min(Global.random(get(Attribute.Power)), 20));
			}
			break;
		case positioning:
			if (c.getStance().dom(this)) {
				c.write(this, "You outmanuever " + target.name() + " and you exhausted her from the struggle.");
				target.weaken(c, 10);
			} else {
				c.write(this, target.name()+" loses her balance while grappling with you. Before she can fall to the floor, you catch her from behind and hold her up.");
				c.setStance(new Behind(this,target));
			}
			break;
		default:
			c.write(this, "You manage to dodge " + target.possessivePronoun() + " attack and give a retaliating slap in return");
			target.pain(c, 4+Math.min(Global.random(get(Attribute.Power)), 20));
		}
	}

	@Override
	public void eot(Combat c, Character opponent, Skill last) {
		super.eot(c, opponent, last);
		if(opponent.pet!=null&&canAct()&&c.getStance().mobile(this)&&!c.getStance().prone(this)){
			if(get(Attribute.Speed)>opponent.pet.ac()*Global.random(20)){
				opponent.pet.caught(c,this);
			}
		}
		if(opponent.has(Trait.pheromones)&&opponent.getArousal().percent()>=20&&opponent.rollPheromones(c)) {
			c.write(opponent,"<br>Whenever you're near "+opponent.name()+", you feel your body heat up. Something in her scent is making you extremely horny.");
			add(c, new Horny(this,opponent.getPheromonePower(),10,opponent.nameOrPossessivePronoun() + " pheromones"));
		}
		if(opponent.has(Trait.smqueen)&&!is(Stsflag.masochism)){
			c.write(Global.capitalizeFirstLetter(String.format("<br>%s seem to shudder in arousal at the thought of pain.", subject())));
			add(c, new Masochistic(this));
		}
		if(has(Trait.RawSexuality)){
			tempt(c, opponent, arousal.max()/25);
			opponent.tempt(c, this, opponent.arousal.max()/25);
		}
		if ((getPure(Attribute.Animism) >= 4) && (getArousal().percent() >= 50) && 
			       (!is(Stsflag.feral))) {
			add(c, new Feral(this));
		}
	}

	@Override
	public String nameDirectObject() {
		return "you";
	}

	@Override
	public String subjectAction(String verb, String pluralverb) {
		return subject() + " " + verb;
	}

	@Override
	public String nameOrPossessivePronoun() {
		return "your";
	}

	@Override
	public String possessivePronoun() {
		return "your";
	}

	@Override
	public String reflectivePronoun() {
		return directObject() + "self";
	}
	@Override
	public String subject() {
		return "you";
	}

	@Override
	public String subjectWas() {
		return subject() + " were";
	}

	@Override
	public String pronoun() {
		return "you";
	}

	@Override
	public String directObject() {
		return "you";
	}

	@Override
	public void emote(Emotion emo, int amt) {
		return;
	}
	@Override
	public String getPortrait(Combat c) {
		return null;
	}
	
	public String action(String firstPerson, String thirdPerson) {
		return firstPerson;
	}
	public Meter getWillpower() {
		return willpower;
	}
	@Override
	public String getType() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean resist3p(Combat c, Character target, Character assist) {
		return has(Trait.cursed);
	}
}
