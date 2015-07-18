package global;

import gui.SaveButton;
import gui.SceneButton;
import items.Clothing;
import items.Item;

import java.util.ArrayList;

import javax.swing.JButton;

import status.Hypersensitive;
import characters.Airi;
import characters.Player;
import characters.Reyka;

public class Prematch implements Scene{
	private Modifier type;
	private Player player;
	public Prematch(Player player){
		this.player=player;
		Global.current=this;
		ArrayList<JButton> choice = new ArrayList<JButton>();		
		String message = "";
		if(player.getLevel()<5){
			message+= "You arrive at the student union a few minutes before the start of the match. " +
				"You have enough time to check in and make idle chat with your opponents before " +
				"you head to your assigned starting point and wait. At exactly 10:00, the match is on.";
			type=Modifier.normal;
			choice.add(new SceneButton("Start The Match"));
		}
		else if(!Global.checkFlag(Flag.metLilly)){
			message+= "You get to the student union a little earlier than usual. Cassie and Jewel are there already and you spend a few minutes talking with them while " +
					"you wait for the other girls to show up. A few people are still rushing around making preparations, but it's not clear exactly what they're doing. " +
					"Other than making sure there are enough spare clothes to change into, there shouldn't be too much setup required. Maybe they're responsible for " +
					"making sure the game area is clear of normal students, but you can't imagine how a couple students could pull that off.<p>A girl who appears to be " +
					"in charge calls you over. She has straight, red hair, split into two simple pigtails. Her features are unremarkable except for the freckles lightly " +
					"spotting her face, but you could reasonably describe her as cute. She mentioned her name once. It was a kind of flower... Lilly? Yeah, " +
					"that sounds right, Lilly Quinn.<p>Lilly gives you a thin smile that's somewhere between polite and friendly. <i>\"You seem to be doing alright for a beginner. Not " +
					"everyone takes to this sort of competition so naturally. In fact, if you think you can handle a bit of a handicap, I've been authorized to offer " +
					"a small bonus.\"</i> Well, you're not going to complain about some extra spending money. It's worth at least hearing her out. <i>\"Sometimes our Benefactor " +
					"offers some extra prize money, but I can't just give it away for free.\"</i> You think you see a touch of malice enter her smile. <i>\"I came up with some " +
					"additional rules to make the game a little more interesting. If you accept my rule, then the extra money will be added as a bonus to each point you " +
					"score tonight.\"</i><p>It's an interesting offer, but it begs the question of why she's extending it to you specifically. Lilly smirks and brushes one of " +
					"her pigtails over her shoulder. <i>\"Don't worry, I'm not giving you preferential treatment. You're very much not my type. On the other hand, I do like " +
					"watching your opponents win, and by giving you a handicap I make that more likely to happen. I don't intend to unfairly pick on you though. Fortunately, " +
					"you'll make more money for every fight you do win, " +
					"so it's better for everyone.\"</i> That's.... You're not entirely sure how to respond to that. <i>\"For the first rule, I'll start with something simple: for " +
					"tonight's match, you're only allowed to wear your boxers. Even when you come back here for a change of clothes, you'll only get your underwear. If you " +
					"agree to this, I'll throw an extra $"+Modifier.pantsman.bonus()+" on top of your normal prize for each point you score. Interested?\"</i>";
			type=Modifier.pantsman;
			Global.flag(Flag.metLilly);
			choice.add(new SceneButton("Do it"));
			choice.add(new SceneButton("Not interested"));
		}
		else{
			message+="You arrive at the student union with about 10 minutes to spare before the start of the match. You greet each of the girls and make some idle chatter with " +
					"them before you check in with Lilly to see if she has any custom rules for you.<p>";
			if (player.getRank() > 0) {
				message+= "Before you have a chance to ask though, Lilly mentions to you that there is a new competitor. However, when you ask her for details, she only mentions that her "
						+ "name is Airi, and that she's a Biology student, while holding a visible smirk. Your instincts tells you something is wrong, but you decide to ignore it for now.<p>"
						+ "<b>Airi has entered the games.</b><p>";
				Global.newChallenger(new Airi());
				Global.flag(Flag.Airi);
			}
			type=offer(player);
			if(type==Modifier.normal){
				message+="<i>\"Sorry "+player.name()+", there's no bonus money available tonight. Our Benefactor doesn't always give us the extra budget.\"<i/> She shrugs casually and " +
					"brushes her pigtail over her shoulder. <i>\"There's nothing wrong with having a normal match. You don't want to get so caught up in gimmicks that you forget " +
					"your fundamentals.\"</i> You give her a fairly neutral shrug and spend a few more minutes talking with her before the match starts. She's surprisingly easy to " +
					"talk to. You eventually head to your start point and arrive just before 10:00.";
				choice.add(new SceneButton("Start The Match"));
			}
			else{
				switch(type){
				case pantsman:
					message+="<i>\"So, "+player.name()+", what would you say to another match in your underwear? For some reason, that just amuses the hell out of me. " +
							"The bonus is still $"+type.bonus()+" per point.\"</i> ";
					break;
				case nudist:
					message+="<i>\"Funny thing "+player.name()+", me and the other girls were just talking about you.\"</i> There's no way that's good. <i>\"I asked them all what their least "+
								"favorite thing about you is.\"</i> Nope. Definitely not good. <i>\"After some discussion they all agreed that your worst quality is your insistence on " +
								"so frequently wearing clothing. So, I think you should spend the match naked and see how well you do. I'm willing to offer a $"+type.bonus()+" bonus to " +
								"motivate you. What do you say?\"</i>";
					break;
				case norecovery:
					message+="Lilly waits until you approach and holds up a small metal... something. <i>\"This unique accessory just fell into my lap, and it made me think of a new " +
							"handicap for you. It's peculiar little toy that's designed to inhibit a man's ejaculation, but it's not so effective that it would prevent an opponent " +
							"from getting you off. In theory, it should keep you from orgasming from masturbation or when you win. You'll have to fight much more defensively or get " +
							"good at forcing a draw.\"</i> She shrugs. <i>\"That's the theory at least. You'll be my guinea pig for this. If you agree, the bonus will be $"+type.bonus()+".\"</i>";
					break;
				case vibration:
					message+="<i>\"Do you like toys, "+player.name()+"? I thought of a way to make your matches harder that you'll still enjoy.\"</i> She holds up a small plastic ring. " +
							"<i>\"Vibrating cock-ring,\"</i> she explains. <i>\"I'll give you a $"+type.bonus()+" for each fight you win while this little fellow keeps you horny and ready to " +
							"burst.\"</i>";
					break;
				case vulnerable:
					message+="<i>\"I've got a simple handicap for you tonight. You've probably come across some sensitization potions that temporarily enhance your sense of touch, right? " +
							"There's a cream that has basically the same effect, but it'll last for several hours. The deal is that I'll rub the cream into your penis, making you much " +
							"more vulnerable during the match, and you'll get an extra $"+type.bonus()+" per victory. Interested?\"</i>";
					break;
				case pacifist:
					message+="Lilly gives you a long, appraising look. <i>\"I'm trying to decide what sort of man you are. You strike me as a good guy, probably not the type " +
							"to hit a girl outside a match. I propose you try being a perfect gentleman by refusing to hit anyone during tonight's match too. So no slapping, " +
							"kicking, anything intended to purely cause pain. If you agree, I'll add $"+type.bonus()+" to each point. What do you say?\"</i>";
					break;
				case notoys:
					message+="<i>\"I've only got a small bonus available tonight, so I have a simple little handicap for you. Leave your sex toys under the bed tonight. You're better off " +
							"getting some practice with your fingers, tongue, or whatever other body parts you like sticking into girls. Liquids, traps, any consumables are fine, only " +
							"toys are off limits. The bonus is only $"+type.bonus()+", but it " +
							"shouldn't give you much trouble.\"</i>";
					break;
				case noitems:
					message+="<i>\"Tell me "+player.name()+", are you the sort of player who spends all his winnings on disposable toys and traps to give yourself the edge? You'd " +
							"probably be better off saving the money and relying more on your own abilities. If you can go the entire night without using any consumable items, I'll " +
							"toss you a $"+type.bonus()+" bonus.\"</i>";
					break;
				}
				choice.add(new SceneButton("Do it"));
				choice.add(new SceneButton("Not interested"));
			}
		}
		
		choice.add(new SaveButton());
		Global.gui().prompt(message, choice);
	}
	private Modifier offer(Player player){
		ArrayList<Modifier> available = new ArrayList<Modifier>();
		if(Global.random(10)>=4){
			return Modifier.normal;
		}
		else{
			available.add(Modifier.normal);
			available.add(Modifier.pantsman);
			available.add(Modifier.nudist);
			available.add(Modifier.norecovery);
			available.add(Modifier.vibration);
			available.add(Modifier.vulnerable);
			available.add(Modifier.pacifist);
			if(player.has(Item.Dildo)||player.has(Item.Dildo2)||player.has(Item.Tickler)||player.has(Item.Tickler2)){
				available.add(Modifier.notoys);
			}
			available.add(Modifier.noitems);
		}
		return available.get(Global.random(available.size()));
	}
	@Override
	public void respond(String response) {
		String message = "";
		ArrayList<JButton> choice = new ArrayList<JButton>();		
		if(response.startsWith("Start")){
			Global.dusk(type);
		}
		else if(response.startsWith("Not")){
			type=Modifier.normal;
			Global.dusk(type);
		}
		else if(response.startsWith("Do")){
			switch(type){
			case pantsman:
				player.top.clear();
				player.bottom.clear();
				player.bottom.add(Clothing.boxers);
				message+="Lilly smiles with her hands on her hips. <i>\"Glad to hear it. We'll hang on to the rest of your clothes until the match is over. Boxers-only starts " +
						"now.\"</i> She wants you to undress here before the match even starts? You hesitate as you realize your opponents are all watching you curiously. Some " +
						"of Lilly's assistants are still around too. She laughs when she notices your reluctance. <i>\"Are you seriously getting embarrassed about this? As if " +
						"anyone in this room hasn't seen you naked on a regular basis.\"</i> She does have a point. You quickly strip off your shirt and pants and prepare for " +
						"the match.";
				break;
			case nudist:
				player.nudify();
				message+="You agree to Lilly's rule and start to strip off your clothes. You try to appear nonchalant about it, but you can't help reddening a bit when your " +
						"opponents start cheering you on. Lilly stiffles a laugh as you hand over your clothes. <i>\"You see? You're more popular already.\"</i>";
				break;
			case norecovery:
				message+="<i>\"Come on,\"</i> Lilly says as she leads you into a nearby room. <i>\"I need to handle your naughty bits and I figured you would prefer some privacy.\"</i> It's " +
						"true, but you're a little surprised by her consideration. She generally seems to enjoy making you uncomfortable. <i>\"Ok, take off your pants and underwear. " +
						"Don't be shy, you have nothing I haven't seen before, and nothing I'm interested in.\"</i> You bare your lower half and Lilly fixes the metal ring onto the " +
						"base of your penis. It's snug, but not uncomfortable, though it presses against the base of your scotum in a way that feels weird.<p><i>\"Now, this may be " +
						"a little awkward for both of us, but I need you to try to masturbate to completion so we can verify that it works as intended.\"</i> This explains the privacy. " +
						"Is she just doing this to screw with you? She shakes her head with a serious expression. <i>\"If it turns out that accessory actually gives you an unfair advantage, " +
						"I can't let you wear it during the match. I do take my job seriously.\"</i> You feel a little bad for doubting her, so you start jerking off without complaining.<p>It " +
						"takes you some time to get hard under Lilly's scrutinizing stare, but you eventually make yourself fully erect and masturbate in earnest. At full mast, the " +
						"metal ring creates an oppressive tightness. No matter how much you try, you find yourself completely unable to cum. Eventually you notice Lilly trying to hide " +
						"her expression, her shoulders shaking with mirth. <i>\"I'm sorry,\"</i> she says between giggles. <i>\"I know it's really rude to laugh, but your expression is just too " +
						"funny. I'm really sorry.\"</i> She manages to calm down enough to gesture for you to stop. <i>\"Ok, I'm convinced the accessory effectively prevents masturbation. The " +
						"more important test is whether you can still be made to orgasm.\"</i> She grasps your dick and begins to stroke you skillfully. You immediately feel the pleasure start " +
						"to build in your frustrated cock and in seconds she brings you to a spurting climax. <i>\"That worked much better than I expected,\"</i> Lilly comments as she " +
						"pulls a tissue out of her pocket to clean up. <i>\"Hopefully you're not too worn out. The match hasn't even started yet.\"</i>";
				break;
			case vibration:
				message+="You agree to Lilly's rule and reach out to take the cock-ring, but she shakes her head. <i>\"I need to put it on you to make sure it's positioned correctly. Don't worry, " +
						"you don't need to undress.\"</i> She steps close to you and slips her hand down the front of your pants and underwear. Her fingers dexterously manipulate your dick as she " +
						"manuevers the ring onto it. From her expression, it looks like she's concentrating on her task rather than trying to entice you, but her closeness and her touch still " +
						"have an effect on you. <i>\"Don't feel embarrassed,\"</i> she says with a reassuring smile. <i>\"It's actually easier to put this on when you're a little hard.\"</i> She fits the " +
						"cock-ring in place and removes her hand. <i>\"Time to test.\"</i> She holds up a small remote and switches it on. Your hips jerk at the intense sensation on your cock. You " +
						"have to endure this for three hours? <i>\"The intensity will automatically modulate to keep you from going numb, but after a few minutes, you'll partially adapt to it. " +
						"I'll hang onto the remote during the match.\"</i> She hits the button again and the vibration stops. <i>\"If this ends up making you cum, I won't be offended if you think " +
						"about me.\"</i>";
				break;
			case vulnerable:
				player.add(new Hypersensitive(player));
				message+="Lilly leads you into the men's bathroom to apply the hypersensitivity cream. She removes your pants and boxers and starts to rub the cream onto your dick. The stuff works " +
						"fast and you can't help letting out a quiet moan at her soft touch. She treats the process very clinically and seems almost bored to be handling your manhood. <i>\"I hope " +
						"you don't take my lack of interest personally,\"</i> she says, as if reading your mind. <i>\"You seem nice, and I guess you're reasonably good looking. You just happen to be " +
						"the wrong gender.\"</i> Ah, that explains a bit. That must make this more awkward for her than it would otherwise be. She shrugs. <i>\"I don't mind. I'm used to competing against " +
						"men, and I have some pride in my technique.\"</i> As she says this, her hand motions turn into smooth, pleasurable strokes. <i>\"Besides, with the typical gender ratio in these " +
						"games, I'm better off than the straight girls. Are you feeling any effect yet?\"</i> You certainly are. Between the cream and her skilled handjob, you can barely stay standing. " +
						"She continues stroking you until you shoot your load into her hands. <i>\"That was quick. I'm going to assume the cream was effective rather than you having a fetish for girls " +
						"who aren't attracted to you.\"</i>";
				break;
			case pacifist:
				message+="Lilly flashes you a broad grin and slaps you on the back uncomfortably hard. <i>\"Just so everyone's aware,\"</i> she calls out to your opponents, <i>\""+player.name()+
						" has sworn that he won't hurt any girls tonight. So no matter how much anyone taunts him, whips him, or kicks him in the balls, he can't retaliate in " +
						"any way.\"</i> As you try to ignore a growing sense of dread, she leans close to your ear and whispers, <i>\"Good luck.\"</i>";
				break;
			case notoys:
				message+="You agree to Lilly's terms and hand over all the sex toys you have on you. She carefully looks over each of the devices, which makes you feel awkward for reasons " +
						"you can't quite explain. <i>\"Is this really the best you have? You're going to need to up your game soon if you want to be competitive. These don't even match up " +
						"to the toys I have for personal use.\"</i>";
				break;
			case noitems:
				message+="Lilly nods, satisfied with your answer. <i>\"Excellent. Traps and items are so impersonal. Besides, who do you think has to clean them up at the end of the night? " +
						"You're better off without them.\"</i>";
				break;
			}
			choice.add(new SceneButton("Start The Match"));
			Global.gui().prompt(message, choice);
		}
	}
}
