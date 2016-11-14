package nightgames.combat;

import java.io.Serializable;
import java.util.Optional;

import nightgames.actions.Movement;
import nightgames.areas.Area;
import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.State;
import nightgames.characters.Trait;
import nightgames.global.Encs;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.status.Enthralled;
import nightgames.status.Flatfooted;
import nightgames.status.Hypersensitive;
import nightgames.status.Status;
import nightgames.status.Stsflag;
import nightgames.trap.Spiderweb;
import nightgames.trap.Trap;

public class Encounter implements Serializable, IEncounter {

    private static final long serialVersionUID = 3122246133619156539L;
    protected Character p1;
    protected Character p2;
    protected boolean p1ff;
    protected boolean p2ff;
    protected transient Optional<String> p1Guaranteed;
    protected transient Optional<String> p2Guaranteed;
    protected Area location;
    protected transient Combat fight;
    protected int checkin;
    protected int fightTime;

    public Encounter(Character first, Character second, Area location) {
        this.location = location;
        p1 = first;
        p2 = second;
        checkin = 0;
        fight = null;
        p1Guaranteed = Optional.empty();
        p2Guaranteed = Optional.empty();
        checkEnthrall(p1, p2);
        checkEnthrall(p2, p1);
    }

    protected void checkEnthrall(Character p1, Character p2) {
        Status enthrall = p1.getStatus(Stsflag.enthralled);
        if (enthrall != null) {
            if (((Enthralled) enthrall).master != p2) {
                p1.removelist.add(enthrall);
                p1.add(new Flatfooted(p1, 2));
                p1.add(new Hypersensitive(p1));
                if (p1.human()) {
                    Global.gui()
                          .message("At " + p2.name() + "'s interruption, you break free from the"
                                          + " succubus' hold on your mind. However, the shock all but"
                                          + " short-circuits your brain; you "
                                          + " collapse to the floor, feeling helpless and"
                                          + " strangely oversensitive");
                } else if (p2.human()) {
                    Global.gui()
                          .message(p1.name() + " doesn't appear to notice you at first, but when you "
                                          + "wave your hand close to her face her eyes open wide and"
                                          + " she immediately drops to the floor. Although the display"
                                          + " leaves you somewhat worried about her health, she is"
                                          + " still in a very vulnerable position and you never were"
                                          + " one to let an opportunity pass you by.");
                }
            }
        }
    }

    public boolean spotCheck() {
        if (p1.eligible(p2) && p2.eligible(p1)) {
            if (p1.state == State.shower) {
                p2.showerScene(p1, this);
                return true;
            } else if (p2.state == State.shower) {
                p1.showerScene(p2, this);
                return true;
            } else if (p1.state == State.webbed) {
                spider(p2, p1);
            } else if (p2.state == State.webbed) {
                spider(p1, p2);
            } else if (p1.state == State.crafting || p1.state == State.searching) {
                p2.spy(p1, this);
                return true;
            } else if (p2.state == State.crafting || p2.state == State.searching) {
                p1.spy(p2, this);
                return true;
            }
            if (p1.state == State.shower) {
                p2.showerScene(p1, this);
                return true;
            } else if (p2.state == State.shower) {
                p1.showerScene(p2, this);
                return true;
            } else if (p1.state == State.masturbating) {
                caught(p2, p1);
                return true;
            } else if (p2.state == State.masturbating) {
                caught(p1, p2);
                return true;
            } else if (p1.spotCheck(p2.get(Attribute.Perception))) {
                if (p2.spotCheck(p1.get(Attribute.Perception))) {
                    p1.faceOff(p2, this);
                    p2.faceOff(p1, this);
                } else {
                    p2.spy(p1, this);
                }
            } else {
                if (p2.spotCheck(p1.get(Attribute.Perception))) {
                    p1.spy(p2, this);
                } else {
                    location.endEncounter();
                }
            }
            return true;
        } else {
            if (p1.state == State.masturbating) {
                if (p1.human()) {
                    Global.gui()
                          .message(p2.name()
                                          + " catches you masturbating, but fortunately she's still not allowed to attack you, so she just watches you jerk off with "
                                          + "an amused grin.");
                } else if (p2.human()) {
                    Global.gui()
                          .message("You stumble onto " + p1.name()
                                          + " with her hand between her legs, masturbating. Since you just fought, you still can't touch her, so "
                                          + "you just watch the show until she orgasms.");
                }
            } else if (p2.state == State.masturbating) {
                if (p2.human()) {
                    Global.gui()
                          .message(p1.name()
                                          + " catches you masturbating, but fortunately she's still not allowed to attack you, so she just watches you jerk off with "
                                          + "an amused grin.");
                } else if (p1.human()) {
                    Global.gui()
                          .message("You stumble onto " + p2.name()
                                          + " with her hand between her legs, masturbating. Since you just fought, you still can't touch her, so "
                                          + "you just watch the show until she orgasms.");
                }
            } else if (!p1.eligible(p2) && p1.human()) {
                Global.gui()
                      .message("You encounter " + p2.name()
                                      + ", but you still haven't recovered from your last fight.");
            } else if (p1.human()) {
                Global.gui()
                      .message("You find " + p2.name()
                                      + " still naked from your last encounter, but she's not fair game again until she replaces her clothes.");
            }
            location.endEncounter();
            return false;
        }
    }

    protected void fightOrFlight(Character p, boolean fight, Optional<String> guaranteed) {
        if (p == p1) {
            p1ff = fight;
            p1Guaranteed = guaranteed;
            checkin++;
        } else {
            p2ff = fight;
            p2Guaranteed = guaranteed;
            checkin++;
        }
        if (checkin >= 2) {
            if (p1ff && p2ff) {
                startFightTimer();
                if (p1.human() || p2.human()) {
                    if (p1.human()) {
                        Global.gui()
                              .message(p2.challenge(p1));
                    } else {
                        Global.gui()
                              .message(p1.challenge(p2));
                    }
                    this.fight = Global.gui()
                                       .beginCombat(p1, p2);
                } else {
                    // this.fight=new NullGUI().beginCombat(p1,p2);
                    this.fight = new Combat(p1, p2, location);
                }
            } else if (p1ff) {
                if (p1Guaranteed.isPresent() && !p2Guaranteed.isPresent()) {
                    if (p1.human() || p2.human())
                        Global.gui().message(p1Guaranteed.get());
                    startFightTimer();
                    Global.gui().refresh();
                    this.fight = Global.gui().beginCombat(p1, p2);
                } else if (p2Guaranteed.isPresent()) {
                    if (p1.human() || p2.human())
                        Global.gui().message(p2Guaranteed.get());
                    p2.flee(location);
                } else if (p2.check(Attribute.Speed, 10 + p1.get(Attribute.Speed) + (p1.has(Trait.sprinter) ? 5 : 0)
                                + (p2.has(Trait.sprinter) ? -5 : 0))) {
                    if (p1.human()) {
                        Global.gui()
                              .message(p2.name() + " dashes away before you can move.");
                    }
                    p2.flee(location);
                } else {
                    startFightTimer();
                    if (p1.human() || p2.human()) {
                        if (p1.human()) {
                            Global.gui()
                                  .message(p2.name() + " tries to run, but you stay right on her heels and catch her.");
                        } else {
                            Global.gui()
                                  .message("You quickly try to escape, but " + p1.name()
                                                  + " is quicker. She corners you and attacks.");
                        }
                        Global.gui()
                              .refresh();
                        this.fight = Global.gui()
                                           .beginCombat(p1, p2);
                    } else {

                        // this.fight=new NullGUI().beginCombat(p1,p2);
                        this.fight = new Combat(p1, p2, location);
                    }
                }
            } else if (p2ff) {
                if (p2Guaranteed.isPresent() && !p1Guaranteed.isPresent()) {
                    if (p1.human() || p2.human())
                        Global.gui().message(p2Guaranteed.get());
                    startFightTimer();
                    Global.gui().refresh();
                    this.fight = Global.gui().beginCombat(p1, p2);
                } else if (p1Guaranteed.isPresent()) {
                    if (p1.human() || p2.human())
                        Global.gui().message(p1Guaranteed.get());
                    p1.flee(location);
                } else if (p1.check(Attribute.Speed, 10 + p2.get(Attribute.Speed) + (p1.has(Trait.sprinter) ? -5 : 0)
                                + (p2.has(Trait.sprinter) ? 5 : 0))) {
                    if (p2.human()) {
                        Global.gui()
                              .message(p1.name() + " dashes away before you can move.");
                    }
                    p1.flee(location);
                } else {
                    startFightTimer();
                    if (p1.human() || p2.human()) {
                        if (p2.human()) {
                            Global.gui()
                                  .message(p1.name() + " tries to run, but you stay right on her heels and catch her.");
                        } else {
                            Global.gui()
                                  .message("You quickly try to escape, but " + p2.name()
                                                  + " is quicker. She corners you and attacks.");
                        }
                        this.fight = Global.gui()
                                           .beginCombat(p1, p2);
                    } else {
                        // this.fight=new NullGUI().beginCombat(p1,p2);
                        this.fight = new Combat(p1, p2, location);
                    }
                }
            } else {
                if (p1.get(Attribute.Speed) + Global.random(10) >= p2.get(Attribute.Speed) + Global.random(10)) {
                    if (p2.human()) {
                        Global.gui()
                              .message(p1.name() + " dashes away before you can move.");
                    }
                    p1.flee(location);
                } else {
                    if (p1.human()) {
                        Global.gui()
                              .message(p2.name() + " dashes away before you can move.");
                    }
                    p2.flee(location);
                }
            }
        }
    }

    private void startFightTimer() {
        fightTime = 2;
    }

    protected void ambush(Character attacker, Character target) {
        startFightTimer();
        target.add(new Flatfooted(target, 3));
        if (p1.human() || p2.human()) {
            fight = Global.gui()
                          .beginCombat(attacker, target, 0);
            if (target.human()) {
                Global.gui()
                      .message(attacker.name() + " catches you by surprise and attacks!");
            }
        } else {
            // this.fight=new NullGUI().beginCombat(p1,p2);
            Global.gui()
                  .refresh();
            fight = new Combat(attacker, target, location, 0);
        }
    }

    protected void showerambush(Character attacker, Character target) {
        startFightTimer();
        if (target.human()) {
            if (location.id() == Movement.shower) {
                Global.gui()
                      .message("You aren't in the shower long before you realize you're not alone. Before you can turn around, a soft hand grabs your exposed penis. "
                                      + attacker.name() + " has the drop on you.");
            } else if (location.id() == Movement.pool) {
                Global.gui()
                      .message("The relaxing water causes you to lower your guard a bit, so you don't notice "
                                      + attacker.name()
                                      + " until she's standing over you. There's no chance to escape, you'll have to face her nude.");
            }
        } else if (attacker.human()) {
            if (location.id() == Movement.shower) {
                Global.gui()
                      .message("You stealthily walk up behind " + target.name()
                                      + ", enjoying the view of her wet naked body. When you stroke her smooth butt, "
                                      + "she jumps and lets out a surprised yelp. Before she can recover from her surprise, you pounce!");
            } else if (location.id() == Movement.pool) {
                Global.gui()
                      .message("You creep up to the jacuzzi where " + target.name()
                                      + " is soaking comfortably. As you get close, you notice that her eyes are "
                                      + "closed and she may well be sleeping. You crouch by the edge of the jacuzzi for a few seconds and just admire her nude body with her breasts "
                                      + "just above the surface. You lean down and give her a light kiss on the forehead to wake her up. She opens her eyes and swears under her breath "
                                      + "when she sees you. She scrambles out of the tub, but you easily catch her before she can get away.");
            }
        }
        if (p1.human() || p2.human()) {
            Global.gui()
                  .refresh();
            fight = Global.gui()
                          .beginCombat(p1, p2, 1);
        } else {
            // this.fight=new NullGUI().beginCombat(p1,p2);
            fight = new Combat(p1, p2, location, 0);
        }
    }

    protected void aphrodisiactrick(Character attacker, Character target) {
        attacker.consume(Item.Aphrodisiac, 1);
        attacker.gainXP(attacker.getVictoryXP(target));
        target.gainXP(target.getDefeatXP(attacker));
        if (target.human()) {
            if (location.id() == Movement.shower) {
                Global.gui()
                      .message("The hot shower takes your fatigue away, but you can't seem to calm down. Your cock is almost painfully hard. You need to deal with this while "
                                      + "you have the chance. You jerk off quickly, hoping to finish before someone stumbles onto you. Right before you cum, you are suddenly grabbed from behind and "
                                      + "spun around. " + attacker.name()
                                      + " has caught you at your most vulnerable and, based on her expression, may have been waiting for this moment. She kisses you and "
                                      + "firmly grasps your twitching dick. In just a few strokes, you cum so hard it's almost painful.\n");
            } else if (location.id() == Movement.pool) {
                Global.gui()
                      .message("As you relax in the jacuzzi, you start to feel extremely horny. Your cock is in your hand before you're even aware of it. You stroke yourself "
                                      + "off underwater and you're just about ready to cum when you hear nearby footsteps. Oh shit, you'd almost completely forgotten you were in the middle of a "
                                      + "match. The footsteps are from " + attacker.name()
                                      + ", who sits down at the edge of the jacuzzi while smiling confidently. You look for a way to escape, but it's "
                                      + "hopeless. You were so close to finishing you just need to cum now. "
                                      + attacker.name()
                                      + " seems to be thinking the same thing, as she dips her bare feet into the "
                                      + "water and grasps your penis between them. She pumps you with her feet and you shoot your load into the water in seconds.\n");
            }
        } else if (attacker.human()) {
            if (location.id() == Movement.shower) {
                Global.gui()
                      .message("You empty the bottle of aphrodisiac onto the shower floor, letting the heat from the shower turn it to steam. You watch "
                                      + target.name() + " and wait "
                                      + "for a reaction. Just when you start to worry that it was all washed down the drain, you see her hand slip between her legs. Her fingers go to work pleasuring herself "
                                      + "and soon she's completely engrossed in her masturbation, allowing you to safely get closer without being noticed. She's completely unreserved, assuming she's alone "
                                      + "and you feel a voyeuristic thrill at the show. You can't just remain an observer though. For this to count as a victory, you need to be in physical contact with her "
                                      + "when she orgasms. When you judge that she's in the home stretch, you embrace her from behind and kiss her neck. She freezes in surprise and you move your hand between "
                                      + "her legs to replace her own. Her pussy is hot, wet, and trembling with need. You stick two fingers into her and rub her clit with your thumb. She climaxes almost "
                                      + "immediately. You give her a kiss on the cheek and leave while she's still too dazed to realize what happened. You're feeling pretty horny, but after a show like that "
                                      + "it's hardly surprising.\n");
            } else if (location.id() == Movement.pool) {
                Global.gui()
                      .message("You sneak up to the jacuzzi, and empty the aphrodisiac into the water without "
                                      + target.name() + " noticing. You slip away and find a hiding spot. In a "
                                      + "couple minutes, you notice her stir. She glances around, but fails to see you and then closes her eyes and relaxes again. There's something different now though and "
                                      + "her soft moan confirms it. You grin and quietly approach again. You can see her hand moving under the surface of the water as she enjoys herself tremendously. Her moans "
                                      + "rise in volume and frequency. Now's the right moment. You lean down and kiss her on the lips. Her masturbation stops immediately, but you reach underwater and finger "
                                      + "her to orgasm. When she recovers, she glares at you for your unsportsmanlike trick, but she can't manage to get really mad in the afterglow of her climax. You're "
                                      + "pretty turned on by the encounter, but you can chalk this up as a win.\n");
            }
        }
        if (!target.mostlyNude()) {
            attacker.gain(target.getTrophy());
        }
        target.nudify();
        target.defeated(attacker);
        target.getArousal()
              .empty();
        attacker.tempt(20);
        Global.getMatch()
              .score(attacker, target.has(Trait.event) ? 5 : 1);
        attacker.state = State.ready;
        target.state = State.ready;
        location.endEncounter();
    }

    protected void caught(Character attacker, Character target) {
        attacker.gainXP(attacker.getVictoryXP(target));
        target.gainXP(target.getDefeatXP(attacker));
        if (target.human()) {
            Global.gui()
                  .message("You jerk off frantically, trying to finish as fast as possible. Just as you feel the familiar sensation of imminent orgasm, you're grabbed from behind. "
                                  + "You freeze, cock still in hand. As you turn your head to look at your attacker, "
                                  + attacker.name()
                                  + " kisses you on the lips and rubs the head of your penis with her "
                                  + "palm. You were so close to the edge that just you cum instantly.");
            if (!target.mostlyNude()) {
                Global.gui()
                      .message("You groan in resignation and reluctantly strip off your clothes and hand them over.");
            }
        } else if (attacker.human()) {
            Global.gui()
                  .message("You spot " + target.name()
                                  + " leaning against the wall with her hand working excitedly between her legs. She is mostly, but not completely successful at "
                                  + "stifling her moans. She hasn't noticed you yet, and as best as you can judge, she's pretty close to the end. It'll be an easy victory for you as long as you work fast. "
                                  + "You sneak up and hug her from behind while kissing the nape of her neck. She moans and shudders in your arms, but doesn't stop fingering herself. She probably realizes "
                                  + "she has no chance of winning even if she fights back. You help her along by licking her neck and fondling her breasts as she hits her climax.");
        }
        if (!target.mostlyNude()) {
            attacker.gain(target.getTrophy());
        }
        target.nudify();
        target.defeated(attacker);
        target.getArousal()
              .empty();
        attacker.tempt(20);
        Global.getMatch()
              .score(attacker, target.has(Trait.event) ? 5 : 1);
        attacker.state = State.ready;
        target.state = State.ready;
        location.endEncounter();
    }

    protected void spider(Character attacker, Character target) {
        attacker.gainXP(attacker.getVictoryXP(target));
        target.gainXP(target.getDefeatXP(attacker));
        if (attacker.human()) {
            Global.gui()
                  .message(target.name()
                                  + " is naked and helpless in the giant rope web. You approach slowly, taking in the lovely view of her body. You trail your fingers "
                                  + "down her front, settling between her legs to tease her sensitive pussy lips. She moans and squirms, but is completely unable to do anything in her own defense. "
                                  + "You are going to make her cum, that's just a given. If you weren't such a nice guy, you would leave her in that trap afterward to be everyone else's prey "
                                  + "instead of helping her down. You kiss and lick her neck, turning her on further. Her entrance is wet enough that you can easily work two fingers into her "
                                  + "and begin pumping. You gradually lick your way down her body, lingering at her nipples and bellybutton, until you find yourself eye level with her groin. "
                                  + "You can see her clitoris, swollen with arousal, practically begging to be touched. You trap the sensitive bud between your lips and attack it with your tongue. "
                                  + "The intense stimulation, coupled with your fingers inside her, quickly brings her to orgasm. While she's trying to regain her strength, you untie the ropes "
                                  + "binding her hands and feet and ease her out of the web.");
        } else if (target.human()) {
            Global.gui()
                  .message("You're trying to figure out a way to free yourself, when you see " + attacker.name()
                                  + " approach. You groan in resignation. There's no way you're "
                                  + "going to get free before she finishes you off. She smiles as she enjoys your vulnerable state. She grabs your dangling penis and puts it in her mouth, licking "
                                  + "and sucking it until it's completely hard. Then the teasing starts. She strokes you, rubs you, and licks the head of your dick. She uses every technique to "
                                  + "pleasure you, but stops just short of letting you ejaculate. It's maddening. Finally you have to swallow your pride and beg to cum. She pumps you dick in earnest "
                                  + "now and fondles your balls. When you cum, you shoot your load onto her face and chest. You hang in the rope web, literally and figuratively drained. "
                                  + attacker.name() + " " + "graciously unties you and helps you down.");
        }
        if (!target.mostlyNude()) {
            attacker.gain(target.getTrophy());
        }
        target.nudify();
        target.defeated(attacker);
        target.getArousal()
              .empty();
        attacker.tempt(20);
        Global.getMatch()
              .score(attacker, target.has(Trait.event) ? 5 : 1);
        attacker.state = State.ready;
        target.state = State.ready;
        location.endEncounter();
        location.remove(location.get(Spiderweb.class));
    }

    public void intrude(Character intruder, Character assist) {
        fight.intervene(intruder, assist);
    }

    public boolean battle() {
        fightTime--;
        if (fightTime <= 0 && !fight.isEnded()) {
            fight.go();
            return true;
        } else {
            return false;
        }
    }

    public Combat getCombat() {
        return fight;
    }

    public Character getPlayer(int i) {
        if (i == 1) {
            return p1;
        } else {
            return p2;
        }
    }

    protected void steal(Character thief, Character target) {
        if (thief.human()) {
            Global.gui()
                  .message("You quietly swipe " + target.name()
                                  + "'s clothes while she's occupied. It's a little underhanded, but you can still turn them in for cash just as if you defeated her.");
        }
        thief.gain(target.getTrophy());
        target.nudify();
        target.state = State.lostclothes;
        location.endEncounter();
    }

    public void trap(Character opportunist, Character target, Trap trap) {
        if (opportunist.human()) {
            Global.gui()
                  .message("You leap out of cover and catch " + target.name() + " by surprise.");
        } else if (target.human()) {
            Global.gui()
                  .message("Before you have a chance to recover, " + opportunist.name() + " pounces on you.");
        }
        Global.gui()
              .refresh();
        trap.capitalize(opportunist, target, this);
    }

    public void engage(Combat fight) {
        this.fight = fight;
        if (fight.p1.human() || fight.p2.human()) {
            Global.gui()
                  .watchCombat(fight);
        }
    }

    public void parse(Encs choice, Character self, Character target) {
        switch (choice) {
            case ambush:
                ambush(self, target);
                break;
            case showerattack:
                showerambush(self, target);
                break;
            case aphrodisiactrick:
                aphrodisiactrick(self, target);
                break;
            case stealclothes:
                steal(self, target);
                break;
            case fight:
                fightOrFlight(self, true, Optional.empty());
                break;
            case flee:
                fightOrFlight(self, false, Optional.empty());
                break;
            case smoke:
                fightOrFlight(self, false, Optional.of(smokeMessage(self)));
                break;
            default:
                return;
        }
    }

    public void parse(Encs choice, Character self, Character target, Trap trap) {
        switch (choice) {
            case ambush:
                ambush(self, target);
                break;
            case capitalize:
                trap(self, target, trap);
                break;
            case showerattack:
                showerambush(self, target);
                break;
            case aphrodisiactrick:
                aphrodisiactrick(self, target);
                break;
            case stealclothes:
                steal(Global.getPlayer(), target);
                break;
            case fight:
                fightOrFlight(self, true, Optional.empty());
                break;
            case flee:
                fightOrFlight(self, false, Optional.empty());
                break;
            case smoke:
                fightOrFlight(self, false, Optional.of(smokeMessage(self)));
                break;
            default:
                return;
        }
    }
    
    private String smokeMessage(Character c) {
        return String.format("%s a smoke bomb and %s.", 
                        Global.capitalizeFirstLetter(c.subjectAction("drop", "drops"))
                        , c.action("disappear", "disappears"));
    }

    @Override
    public boolean checkIntrude(Character c) {
        return fight != null && !c.equals(p1) && !c.equals(p2);
    }

    @Override
    public void watch() {
        Global.gui().watchCombat(fight);
        fight.go();
    }
}
