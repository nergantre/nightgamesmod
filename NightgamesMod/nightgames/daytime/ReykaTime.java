package nightgames.daytime;

import static nightgames.requirements.RequirementShortcuts.bodypart;
import static nightgames.requirements.RequirementShortcuts.not;

import java.util.ArrayList;
import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.BasicCockPart;
import nightgames.characters.body.BodyPart;
import nightgames.characters.body.CockMod;
import nightgames.characters.body.CockPart;
import nightgames.characters.body.EarPart;
import nightgames.characters.body.ModdedCockPart;
import nightgames.characters.body.PussyPart;
import nightgames.characters.body.TailPart;
import nightgames.characters.body.WingsPart;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.requirements.BodyPartRequirement;
import nightgames.requirements.NotRequirement;
import nightgames.requirements.RequirementShortcuts;
import nightgames.status.addiction.Addiction;
import nightgames.status.addiction.AddictionType;

public class ReykaTime extends BaseNPCTime {
    public ReykaTime(Character player) {
        super(player, Global.getNPC("Reyka"));
        knownFlag = "Reyka";
        giftedString = "\"Awww thanks!\"";
        giftString = "\"A present? You shouldn't have!\"";
        transformationOptionString = "Demonic Rituals";
        loveIntro = "[Placeholder]<br>You head down to the chapel basement. Reyka shows you a cute smile when she realizes that you've come to visit.";
        transformationIntro = "<br>Reyka seems a bit shocked at your request. <i>You want to become a demon?</i> "
                        + "She looks a bit worried when she responds. You quickly reassure her that you don't necessarily want the whole package, corrupted soul and all. "
                        + "You just want some of the fun bits that she had made such good use of. Seeing the conversation directed back to sex, Reyka quickly recovers her saucy grin, "
                        + "<i>\"You've been having some fun I see? Alright, I can do that for you, but you have to bring me some ingredients. I can't do everything myself! "
                        + "I'll write down what I can do for you. One thing though, you'll have to provide me a lot of semen as power for the rituals. I'll give you these magic bottles, so make sure "
                        + "you catch it in here the next time you jack off\"</i>";
        transformationFlag = "masturbationSemen";
    }

    @Override
    public void buildTransformationPool() {
        options = new ArrayList<>();
        {
            TransformationOption growCock = new TransformationOption();
            growCock.ingredients.put(Item.PriapusDraft, 1);
            growCock.ingredients.put(Item.Talisman, 1);
            growCock.requirements.add(RequirementShortcuts.rev(new NotRequirement(new BodyPartRequirement("cock"))));
            growCock.additionalRequirements = "";
            growCock.option = "Reyka: Grow a cock";
            growCock.scene = "[Placeholder]<br>Reyka downs the bottle of the priapus draft after channeling her dark magic into the talisman and attaching it to her clitoris. "
                            + "The two of you wait, and soon enough, a large demonic cock sprouts out under the talisman, ripping it off from her body.";
            growCock.effect = (c, self, other) -> {
                other.body.add(new ModdedCockPart(BasicCockPart.big, CockMod.incubus));
                return true;
            };
            options.add(growCock);
        }
        {
            TransformationOption removeCock = new TransformationOption();
            removeCock.ingredients.put(Item.FemDraft, 3);
            removeCock.requirements.add(RequirementShortcuts.rev(new BodyPartRequirement("cock")));
            removeCock.additionalRequirements = "";
            removeCock.option = "Reyka: Remove her cock";
            removeCock.scene = "<br>Reyka doesn't seem extremely pleased with your request to remove her new found maleness, but complies anyways with your wishes. "
                            + "Taking the FemDrafts you offer her, she drinks them one after another and waits with her cock still proudly erect. "
                            + "Soon enough though, the organ starts shrinking back into her body as if being absorbed by her lower lips. "
                            + "Finally the cock head retreats into her fleshy hood, becoming indistinguishable from her old clitoris.";
            removeCock.effect = (c, self, other) -> {
                other.body.removeAll("cock");
                return true;
            };
            options.add(removeCock);
        }
        TransformationOption incubusCock = new TransformationOption();
        incubusCock.ingredients.put(Item.PriapusDraft, 10);
        incubusCock.ingredients.put(Item.SuccubusDraft, 20);
        incubusCock.ingredients.put(Item.semen, 5);
        incubusCock.requirements.add(new BodyPartRequirement("cock"));
        incubusCock.requirements.add((c, self, other) -> {
            return self.body.get("cock")
                            .stream()
                            .anyMatch(cock -> ((CockPart) cock).isGeneric(self));
        });
        incubusCock.additionalRequirements = "A normal cock";
        incubusCock.option = "Incubus Cock";
        incubusCock.scene = "{self:subject} smiles when she sees that you have brought her the ingredients. "
                        + "<i>\"{other:name}, honey, just lie down on the couch over there and I'll fix you right up.\"</i> "
                        + "To be completely honest, you're kind of worried that this may go completely and horribly wrong "
                        + "now that you've decided to do this. However it's a bit too late in the game to back out now. "
                        + "Nervously, you lie down on the couch in the demoness' chapel. A few minutes later, Reyka returns, "
                        + "completely naked. Reyka glances at you, and coos <i>\"Aww, don't be nervous! I promise this wont "
                        + "hurt... too much.\"</i> The succubus saunters over to you and fetches the bottles you prepared with "
                        + "her tail. She pours the thick liquids on her hands and mixes them together. She applies half of the "
                        + "mixture on your cock (which almost makes you cum due to her ministrations), and massages the rest "
                        + "into her cunt. Finally, she draws a familiar rune on your lower belly that you recognize as the orgasm "
                        + "seal she uses in the night games.<br><br>"
                        + "As Reyka finishes with her preparations, you finally realize what she's about to do. Remembering the last "
                        + "time she fucked you within an inch of your life, you start to scramble off of her couch, in an mad attempt "
                        + "to escape. As if she read your mind, Reyka unfurls her wings and jumps on you as you almost reach the stairs "
                        + "back up to the main chapel area. The temptress flaps her wings once and uses the upwards force to swiftly "
                        + "mount you. Grinning devilishly, she purrs <i>\"Oh no {other:name}, we'll be having none "
                        + "of that. You paid for this yourself, and it would be SUCH a waste for us to stop now. Don't you agree "
                        + "{other:name}?\"</i> Resigned to your fate, you can only nod obediently as she rubs her goopy slit against "
                        + "your rock hard erection<br><br>"
                        + "After 6 hours of messy sex in which Reyka fucked you unconscious 17 times, you wake up cuddled in Reyka's "
                        + "bountiful bosoms. You quickly notice that Reyka has been awake and smiling at your groggy expression while you wake up. "
                        + "As you somehow start to get hard again when you smell Reyka's intoxicating scent, you realize that your penis feels very different. "
                        + "Reyka sees you glancing at your own crotch and tells you, <i>\"So if you haven't noticed yet, your cock has been transformed into "
                        + "that of an incubus. You'll find that you have a bit more staying power than usual, and you will be able to drain some "
                        + "of your partner's strengths whil you're fucking. Finally, you'll find that your cum has some... special properties. "
                        + "If you cum inside a girl, she'll be temporarily enthralled to your will. These effects are stronger the more instinctive "
                        + "your opponent is. However, it wont work much against that high tech gizmo that you humans have now. Anyway, now that "
                        + "we're done, I have to get some rest. Even if it doesn't look like it, that ritual took a lot out of me.\" "
                        + "Recognizing that you are being shooed away, you profusely thank Reyka and leave her chapel with your new incubus cock.</i>";
        incubusCock.effect = (c, self, other) -> {
            Optional<BodyPart> optPart = self.body.get("cock")
                                                  .stream()
                                                  .filter(cock -> ((CockPart) cock).isGeneric(self))
                                                  .findAny();
            BasicCockPart target = (BasicCockPart) optPart.get();
            self.body.remove(target);
            self.body.add(new ModdedCockPart(target, CockMod.incubus));
            return true;
        };
        options.add(incubusCock);
        TransformationOption demonWings = new TransformationOption();
        demonWings.ingredients.put(Item.SuccubusDraft, 20);
        demonWings.ingredients.put(Item.semen, 5);
        demonWings.requirements.add(not(bodypart("wings")));
        demonWings.option = "Demonic Wings";
        demonWings.scene =
                        "Reyka smiles and crushes the ingredients together and draws a magic formation on your back and shoulders. "
                                        + "After telling you to sit down across from her, she starts masturbating. Dumbfounded at her sudden action, you start getting up from your chair. "
                                        + "You quick fall off balance though when the markings on your back grow hot. Black demonic wings tears through your back and settles behind you. "
                                        + "Engrossed with exploring your new wings, you don't even notice when Reyka approaches you and pushes you down. You realize that the ritual must have "
                                        + "taken a lot out of her, and she looks particularly famished now. You resign yourself to be the demoness' partner and dinner as payment for your new wings.";
        demonWings.effect = (c, self, other) -> {
            self.body.addReplace(WingsPart.demonic, 1);
            return true;
        };
        options.add(demonWings);
        TransformationOption demonTail = new TransformationOption();
        demonTail.ingredients.put(Item.SuccubusDraft, 20);
        demonTail.ingredients.put(Item.semen, 5);
        demonTail.requirements.add(not(bodypart("tail")));
        demonTail.requirements.add((c, self, other) -> {
            return self.body.get("tail")
                            .stream()
                            .anyMatch(part -> part != TailPart.demonic) || !self.body.has("tail");
        });
        demonTail.option = "Spade Tail";
        demonTail.scene =
                        "[Placeholder]<br>Reyka marks the top of you ass with a magic symbol and fingers your ass until you grow a demonic tail.";
        demonTail.effect = (c, self, other) -> {
            self.body.addReplace(TailPart.demonic, 1);
            return true;
        };
        options.add(demonTail);
        TransformationOption pointedEars = new TransformationOption();
        pointedEars.ingredients.put(Item.SuccubusDraft, 20);
        pointedEars.ingredients.put(Item.semen, 5);
        pointedEars.requirements.add(new BodyPartRequirement("ears"));
        pointedEars.requirements.add((c, self, other) -> {
            return self.body.get("ears")
                            .stream()
                            .anyMatch(part -> part != EarPart.cat) || !self.body.has("ears");
        });
        pointedEars.option = "Pointed Ears";
        pointedEars.scene =
                        "Reyka fetches the ingredients from your pockets with her tail and mixes them together with her palm. She mutters something unintelligible under her breath and suddenly a "
                                        + "comfortable-looking massage table appears in front of you. She motions for you to lie down, to which you promptly comply. She hovers above you and uses her mixture coated hands to "
                                        + "massage your ears. As her hands make contact with your sensitive ears, you feel a burning sensation in them as if they're are melting. "
                                        + "Reyka notices your fearful expression and coos, <i>\"Don't worry honey, just bear with it for now.\"</i> while continuing to massage and pull at your ears. After a "
                                        + "good twenty minutes of pinching and pulling on them, Reyka lets you know that she's done and gives you a mirror. You confirm that yes, you are now a proud owner of a set of "
                                        + "pointed fey-looking ears!";
        pointedEars.effect = (c, self, other) -> {
            self.body.addReplace(EarPart.pointed, 1);
            return true;
        };
        options.add(pointedEars);
        TransformationOption succubusPussy = new TransformationOption();
        succubusPussy.ingredients.put(Item.SuccubusDraft, 20);
        succubusPussy.ingredients.put(Item.BewitchingDraught, 20);
        succubusPussy.ingredients.put(Item.FemDraft, 20);
        succubusPussy.ingredients.put(Item.semen, 5);
        succubusPussy.requirements.add(new BodyPartRequirement("pussy"));
        succubusPussy.requirements.add((c, self, other) -> {
            return self.body.get("pussy")
                            .stream()
                            .anyMatch(pussy -> pussy == PussyPart.normal);
        });
        succubusPussy.additionalRequirements = "A normal pussy";
        succubusPussy.option = "Succubus Pussy";
        succubusPussy.scene =
                        "[Placeholder]<br>Reyka mixes the potions together with her tail and fucks you thoroughly with it, turning your once-human slit into a pulsating cock-hungry succubus pussy.";
        succubusPussy.effect = (c, self, other) -> {
            self.body.addReplace(PussyPart.succubus, 1);
            return true;
        };
        options.add(succubusPussy);
    }

    @Override
    public void subVisitIntro(String choice) {
        if (npc.getAffection(player) > 0) {
            Global.gui()
                  .message("You go over to the chapel, wondering if Her Demonic Highness would deign to "
                                  + "see you. As you enter, the priest notices you and quickly shuffles away, apparently "
                                  + "a little skittish around anyone who would want to visit a demon. You walk towards "
                                  + "the back and descend the basement stairs. Strangely, Reyka isn't there, it's just "
                                  + "the creepy decor greeting you. You look around for a moment, but decide not to pry, "
                                  + "there are probably a few dozen things in here that could kill you. If not, Reyka probably "
                                  + "will. As you turn to go back up, Reyka appears in the doorway. <i>\"Oh hello "
                                  + player.name()
                                  + ", how nice of you to come visit me! I was just out getting some... supplies.\"</i> She "
                                  + "gives the room a once-over and looks at you darkly, seeming to bore straight into your "
                                  + "soul with her menacing eyes: <i>\"You didn't touch anything, did you?\"</i> "
                                  + "Quickly shaking your head, you emphatically declare your innocence. You're only here to spend some time "
                                  + "with your favorite demoness. <i>\"Is that so, and what might you be planning then?\"</i>");
            Global.gui()
                  .choose(this, "Games");
            Global.gui()
                  .choose(this, "Sparring");
            Global.gui()
                  .choose(this, "Sex");
            if (Global.getPlayer().checkAddiction(AddictionType.CORRUPTION)) {
                Global.gui().choose(this, "Strengthen Corruption");
            }
        } else if (Global.getPlayer()
                         .checkAddiction(AddictionType.CORRUPTION)) {
            Global.gui()
                  .message("Reyka low-affection corruption intro");
            if (npc.getAttraction(player) < 15) {
                npc.gainAttraction(player, 2);
                player.gainAttraction(npc, 2);
            } else {
                npc.gainAffection(player, 1);
                player.gainAffection(npc, 1);
                Global.gui()
                      .choose(this, "Games");
                Global.gui()
                      .choose(this, "Sparring");
                Global.gui()
                      .choose(this, "Sex");
            }
            Global.gui()
                  .choose(this, "Strengthen Corruption");
        } else if (npc.getAttraction(player) < 10) {
            Global.gui()
                  .message("You were going to ask Aesop where to find Reyka, but while on your way "
                                  + "there you noticed a dim pink haze protruding from a window leading into "
                                  + "the campus chapel's basement. The irony of a demon living beneath a church "
                                  + "is definitely Reyka's style, so you go over to investigate.<br/><br/>"
                                  + "You go to the back of the chapel, looking for the priest. You find him "
                                  + "setting a pot of tea and gently tap him on the shoulder. <i>\"Oh! You scared me, my son! "
                                  + "I'm sorry but I'm a bit jumpy these days... Anyway, what can I do for you?\"</i> You "
                                  + "inquire with as much subtlety as you can muster whether the church has any long term housing, perhaps in the basement. "
                                  + "The priest's eyes go wide in shock as you say this. Trembling, he slowly "
                                  + "raises his arm, finger outstretched, pointing towards a narrow door. Soon "
                                  + "after, he briskly walks away, leaving you alone facing the basement door.<br/><br/>"
                                  + "Taking care to be as quiet as possible, you walk down the stairs. There "
                                  + "is more of that haze here, it smells mildly like strawberries. "
                                  + "When you reach the bottom, you take a look around. There is a large pile "
                                  + "of junk in one corner, probably the original contents of the basement. "
                                  + "The rest of the space is mostly empty; except for some intricate drawings "
                                  + "on the floor, there is only a bed on which Reyka lies. Lay would be a better "
                                  + "word, though, as she sat up faster than you could blink and she looks rather "
                                  + "displeased. <i>\"Would you like it if I barged into your room like that? No, wait, "
                                  + "don't answer that, of course you would. Still, a lady deserves her privacy, yes?\"</i> "
                                  + "She bats a hand at you dismissively as if swatting away a fly and suddenly you are flying back "
                                  + "up the stairs, through the open door which promptly slams shut. She probably "
                                  + "doesn't want to see you...");
            player.gainAttraction(npc, 2);
            npc.gainAttraction(player, 2);
        } else {
            player.gainAffection(npc, 1);
            npc.gainAffection(player, 1);
            Global.gui()
                  .message("Deciding you'd rather have lunch instead, you head for the cafeteria. Halfway there, "
                                  + "you are pushed to the wall by a surprisingly wing-less but still strikingly beautiful Reyka. "
                                  + "\"So listen, I love playing around at night and all, but the days do tend to get a bit dull. "
                                  + "Usually, I'd go and have a little fun with some random coeds, nothing serious of course. "
                                  + "I just go around discreetly getting people all hot and bothered in a variety of ways and "
                                  + "observe the results. It breaks a few relationships here and there and even creates a few "
                                  + "new ones, I'm a regular Cupid. Then I thought, 'well, " + player.name()
                                  + " is pretty nice, perhaps "
                                  + "I could have some fun with him.', so here I am. Are you interested?\"<br/><br/>"
                                  + "You are frozen for a moment, staring into those deep eyes of hers. 'Pretty nice'? "
                                  + "What's that supposed to mean? As you ponder this, she apparantly gets slightly annoyed by "
                                  + "your inaction and presses herself into you even harder. She leans over and whispers in you ear: "
                                  + "<i>\"We really could have some very... good... fun... you know....\"</i> Those last words are punctuated "
                                  + "by her gently squeezing your crotch. You maintain enough composure to agree. She immediately jumps back, smiling. "
                                  + "<i>\"Excellent. Now you never really had a choice of course, but as a reward for coming willingly, "
                                  + "I'll let you choose what we are going to do! Who knows, I might even let you into my home.\"</i> "
                                  + "You are not sure whether to be relieved or worried about this, but you have a choice to make.");
            Global.gui()
                  .choose(this, "Games");
            Global.gui()
                  .choose(this, "Sparring");
            Global.gui()
                  .choose(this, "Sex");
        }
        Global.gui()
              .choose(this, "Leave");
    }

    @Override
    public void subVisit(String choice) {
        if (choice.equals("Strengthen Corruption")) {
            if (npc.getAffection(player) == 0) {
                Global.gui()
                      .message("Reyka has you perform 'evil' acts to strengthen your corruption. This"
                                      + " is a placeholder. It still increases your addiction, though. You"
                                      + " won't suffer withdrawal effects tonight.");
            } else {
                Global.gui()
                      .message("You tell Reyka about the darkness you've been feeling inside. The thing she put there."
                                      + " <i>\"Oh, left a little mark, did I? Well, don't worry, you'll be fine. As long as "
                                      + "you keep it fed, of course.\"</i> You most certainly do not like the sound of that. "
                                      + "<i>\"Don't fret so much dear, it's unseemly. It's really quite simple: it got there"
                                      + " because of what I am, so it wants you to be a little like me as well. You know,"
                                      + " demonic and all.\"</i> What!? She's turning you into a demon? <i>\"No, no, it's not "
                                      + "that strong. Not yet at least. No, for now, you just need to act the part a bit."
                                      + " And what could be a better way to do so than some particularly naughty sex?\" "
                                      + "</i>That's more your language, so you follow Reyka as she leads you to the wonderful"
                                      + " irony that is her home.\n\nWhen a demon promises to do naughty things to you, "
                                      + "that should probably worry you. It doesn't. Something about Reyka puts you at "
                                      + "ease despite her imperious manner. Her wicked smile in the pale light of her "
                                      + "basement room however, does not. <i>\"So tell me, what do you think would be some "
                                      + "delightfully bad things for us to stoke that fire of yours with?\"</i> She glances "
                                      + "meaningfully downward as if to look around you at something... Oh, oh. Butt "
                                      + "stuff? <i>\"Butt stuff! That always seems to get under some people's nerves. But "
                                      + "still tame enough for a neophyte such as yourself. So what are you waiting for? "
                                      + "Get naked.\"</i> You do as she tells you, and she does the same. Your efforts are "
                                      + "hampered a bit as she reveals her curvy physique, but you manage to get it all "
                                      + "off anyway. <i>\"I see there is little need to get you ready, \"</i> nope, already "
                                      + "rock-hard, <i>\"so we just have to take care of lubrication.\"</i> You look around for"
                                      + " a bottle, but there doesn't seem to be any. <i>\"No, dear. Not that artificial "
                                      + "nonsense. Your saliva will work perfectly well.\"</i> She gets on her bed on all "
                                      + "fours and looks back at you. <i>\"Don't worry, I wash myself. Usually. Well? You "
                                      + "may commence!\"</i> She does look kind of inviting... You hesitantly walk towards "
                                      + "her and kneel before the bed, looking right at her rosy hole. You grab her "
                                      + "luscious asscheeks with both hands and give her a massage. <i>\"Mmm, good start. "
                                      + "But that's not what we're here for, is it?\"</i> Steeling your nerves, you lean in."
                                      + " Smells nice, too. The first lick is very tentative, but it does uncover that "
                                      + "she doesn't taste nearly as bad as you feared. In fact, it seems quite pleasant."
                                      + " Reyka seems to agree, if the little shiver up her spine is any indication. So "
                                      + "you step up your game, really getting to work on her asshole. Reyka's hips seem"
                                      + " to take on a life of their own, rotating and grinding back against your face. "
                                      + "The scents wafting up from her wet slit do not help your concentration at all,"
                                      + " and you find yourself travelling down every once in a while to have a test. It"
                                      + " seems to taste better every time, and the lubrication helps as well. <i>\"That'll"
                                      + " do for now, love, I need something more substantial right now.\"</i> What a "
                                      + "coincidence. You've got just the thing. You push her forward a bit and take your"
                                      + " position behind her on the bed. It's a pretty steep angle to line yourself up "
                                      + "properly, but you are well-motivated. Before giving yourself a chance to have "
                                      + "second thoughts, you sink all the way down into her. <i>\"Ooooh! It's been too long"
                                      + " since I had a stud give me a good pounding back there! You'll make up for lost "
                                      + "time, won't you?\"</i> Oh, fucking yes you will! Not bothering with starting off "
                                      + "slowly, you immediately begin hammering her tight ass as hard as you can, "
                                      + "eliciting all kinds of sounds from the demon beneath you. Her tail wraps itself"
                                      + " around your neck, but luckily doesn't constrict. Just to make sure you don't "
                                      + "leave, probably. Not that there was any risk of that happening. You don't hold "
                                      + "up long under the ferocious tempo, and you soon feel you're about to cum. When "
                                      + "you do, it feels like something snaps inside of you. The orgasm feels even more"
                                      + " powerful than usual, and there is a distinct sense of something besides cum "
                                      + "being pumped into Reyka. Her tail, still around your neck, suddenly opens up and"
                                      + " covers your mouth. Before you can even process what's going on, it blows "
                                      + "something into you. You break away and start coughing, causing black smoke to "
                                      + "flow out of your mouth. You can still feel it though. Not in your lungs, "
                                      + "someplace deeper. <i>\"Willing sacrifices are always more potent that those taken"
                                      + " by force. Keep this up, and you'll make quite the handsome semi-incubus at some"
                                      + " point. Are you alright, dear? You look a little pale.\"</i> You feel a little "
                                      + "pale. You tell her that it's perhaps best if you rested for a while before "
                                      + "continuing... Whatever it was she just did. She agrees, waving you off. It "
                                      + "seems a dismissive gesture, but her eyes betray something else. Is that actual"
                                      + " concern? Pondering the theological implications of a demon showing affection,"
                                      + " you head home. ");
            }
            Global.gui()
                  .choose(this, "Leave");
            Global.getPlayer()
                  .addict(AddictionType.CORRUPTION, npc, Addiction.MED_INCREASE);
            Global.getPlayer().getAddiction(AddictionType.CORRUPTION).ifPresent(Addiction::flagDaytime);
        } else if (choice.equals("Sex")) {
            if (npc.getAffection(player) >= 8 && (!player.has(Trait.desensitized) || Global.random(2) == 1)) {
                Global.gui()
                      .message("You hesitated for a bit too long, and again Reyka's eyes flash red. Again, your mind fogs over, "
                                      + "ready to do whatever your mistress wants. Again, Reyka, hauls you off. You don't seem to "
                                      + "be able to focus on anything; everything around you is distorted and blurry, except for Reyka. "
                                      + "She is casually walking in front of you, not even bothering to look back and see if you "
                                      + "are still with her. You wouldn't dream of leaving, of course: She is your one buoy in a sea "
                                      + "of vagueness. Eventually, you arrive at her place, ready for another round of her surreptitiously "
                                      + "feeding on you. No. Not this time. You muster up all your willpower, all your strength, and "
                                      + "you break free from the shroud around your mind. Instantly the fog clears, the demonic hidey-hole "
                                      + "that is Reyka's room snapping into focus. Reyka knows it too. She spins around, a hint of surprise "
                                      + "in her expression. Not wanting to give her a chance to enthrall you again, you tackle her onto the "
                                      + "bed. You know you'll never make it to the door. Then again, you did want to have sex with her. You "
                                      + "just wanted it without being mentally restrained and helpless the entire time. Seeing little other options "
                                      + "in your current position, you lean in and kiss her. You've obviously kissed her plenty of times already "
                                      + "during the fights, some of them even voluntarily, but this one is different. This is a kiss with "
                                      + "emotion behind it and Reyka senses this too. She seems quite startled of a moment, but soon returns "
                                      + "your kiss passionately. Her tongue forces it's way into your mouth and twists and turns around your own.<br/><br/>"
                                      + "The aphrodisiacs in her saliva are already starting to get to you, so you decide it's probably best "
                                      + "to act quickly, while your mind is still your own. You hurry downward and pull up the miniskirt she "
                                      + "always seems to be wearing. Also as per usual, she has forgone any further means of covering up, not "
                                      + "wanting anything to get in the way should she have a sudden need for a snack. Feeling quite in the mood "
                                      + "for a snack yourself, you move in and start licking her gently. Reyka is clearly not used to this. Usually "
                                      + "when she compels her 'partners' she makes them work hard; the slow pace and meticulous placement of each "
                                      + "lick and caress means she feels every detail. She locks her thighs around your head and grabs your hair, "
                                      + "trying to hump your face. You manage to keep your pace though, fully aware of the dangerous position "
                                      + "you are in given how her juices have had somewhat disquieting effects on you in the past. Reyka by now "
                                      + "is desperate for more stimulation, and has no problem screaming out profanities to that effect. It's quite "
                                      + "possible any people upstairs may hear her; that'd be something to see. She's getting close; you can tell "
                                      + "by her wings and tail popping into existence as she can no longer summon the will to hide them.<br/><br/>"
                                      + "While her juices have not taken control of you, they've certainly had other effects as you are rock "
                                      + "hard. You move up her body and deeply kiss her once more. Simultaneously, you position your dick at "
                                      + "her entrance and thrust all the way in in one go. The sudden invasion sends her over the edge. Her "
                                      + "inner muscles are always hard at work when they have a guest over, but right now it's simply insane. "
                                      + "They are twisting and pulling more dextrously than you have ever felt anyone with their hands, and "
                                      + "you've felt Mara at work. The sensations would have been enough to set you off instantly, but you "
                                      + "are in some kind of trance-like state where you feel all the incredible pleasure, yet feel no need "
                                      + "to cum. You continue pistoning into her furiously for several minutes, Reyka seemingly in a state of "
                                      + "perpetual orgasm. It's a good thing she doesn't actually need to breathe or she would have passed "
                                      + "out by now. As she continues to moan and squirm, the more demonic features of her anatomy once again "
                                      + "disappear. You close your eyes and just focus on keeping the thrusting going. Not a minute later, she "
                                      + "grabs your face and pulls it off of her own. When you open your eyes, you see a pair of deep blue "
                                      + "eyes staring back at you. Her skin becomes less pale and her hair shifts from black to blonde. "
                                      + "As you stare at her in amazement, ceasing your thrusts, she pulls you back in and gives you a "
                                      + "loving kiss. After all the internal milking you've endured it seems strange to you that a kiss "
                                      + "should send you over the edge, but send you over it does. You thrust in one final time and pour "
                                      + "a deluge of cum into her. Strangely, though, you don't feel any of the tiredness you've come to expect "
                                      + "from Reyka; it just feels like an incredible orgasm. When you finally finish you roll off to the side, "
                                      + "catching your breath. Reyka rolls onto her side, putting her arms around your neck and a leg across your "
                                      + "waist.<br/><br/>"
                                      + "After a few minutes you ask her what the transformation thing is all about. <i>\"Normally when succubi have "
                                      + "sex, it feels good, but in the same way you feel good having a really nice meal. What we just did "
                                      + "had true emotion in it, from both of us. Demons have souls, did you know that? They're just a bit... "
                                      + "emptier than mortals'. That kind of... making love, it fills them up for a while, turning us into what "
                                      + "we would look like if we were mortal. It feels different too, needing to breathe and the constant "
                                      + "thumping of a heart in your chest seemed like chores to me before, but now... I don't know.\"</i> She's quiet "
                                      + "after that, so you are too. You just hold her as her hair is beginning to darken at the roots and "
                                      + "her irises are taking on a pinkish hue. Eventually, you get up and get dressed. Reyka, her old self "
                                      + "again, does the same and you share a final kiss before you leave.");
                if (!player.has(Trait.desensitized)) {
                    Global.gui()
                          .message("<br/><br/><b>Having been in the maelstrom that is Reyka's pussy while she was orgasming, you are sure "
                                          + "nothing else will ever come close to those feelings. Your sexual endurance has permanently increased.</b>");
                    player.add(Trait.desensitized);
                    npc.getGrowth().addTrait(0, Trait.desensitized);
                }
            } else {
                Global.gui()
                      .message("Feeling a tad squeamish, you are about to suggest going out to get something to eat. As you turn "
                                      + "to Reyka to say this, however,  you see a quick flash of red in her eyes. Your mind fogs over for "
                                      + "a moment. When you try to recall what you where saying, Reyka instead whispers in your ear: <i>\"How about "
                                      + "we go and have a little private fun somewhere?\"</i> This sounds like an excellent idea you, so you accept "
                                      + "and follow her as she leads you over to that wonderfully cozy basement room of hers.<br/><br/>"
                                      + "You reach the room without incident, save for the sad look the priest gave you on your way in. "
                                      + "As soon as you reach the bottom of the stairs, Reyka pulls your arm, hard. The force of the pull "
                                      + "flings you onto the bed, which stands at the center of the intricate pattern of symbols drawn on the "
                                      + "floor. Reyka looks you over possessively, grinning impishly. She speaks some words your mind doesn't "
                                      + "seem to be able to process and a bright  ball of swirling purple light appears in her hands. She spreads "
                                      + "her arms wide and the ball seems to explode,  blinding you.  After only a second your vision returns, "
                                      + "revealing both of you to be naked. Reyka settles in between your legs and takes your surprisingly hard "
                                      + "member into her mouth. The feeling is amazing; she doesn't waste any time and starts sucking hard while "
                                      + "her tongue swirls around your head. Wherever she is not touching, her saliva feels tingly on your flesh. "
                                      + "It doesn't take long for you to get close to orgasm, but Reyka has other plans. With a long-nailed finger "
                                      + "she draws an arcane symbol just above your cock and you immediately feel the oncoming waves draw back into "
                                      + "your balls. She stops sucking and with a flick of her wrist, she sends you sprawling further onto the bed. "
                                      + "She immediately proceeds to mount you, taking you fully inside her fully lubricated entrance. It feels as if "
                                      + "her insides are an entirely separate organism, squirming and squeezing and pulling and tightening and "
                                      + "SUCKING, man the suction is powerful. If you were at all able, you would have cum already, probably twice, "
                                      + "but whatever spell she cast on you is keeping a tight lid on things. The sensory overload is driving you "
                                      + "mad and you start bucking back fervently, desperate for that small push over the edge. This continues "
                                      + "for minutes, every second feeling like an eternity. At long last, she decides you've had enough. She "
                                      + "settles down, impaling herself fully, brushes a hand over the symbol she previously drew and with "
                                      + "a final smile and squeeze, sends you blasting over the edge. It is beyond anything you have ever felt. "
                                      + "It feels like all your energy is drained out of you, replaced by pure ecstasy. After what "
                                      + "feels like hours the feeling dissipates, leaving only weariness in its wake.<br/><br/>"
                                      + "Mustering all your strength, you look up to see Reyka already dressed. While brushing her hair, "
                                      + "she asks: <i>\"You remember that moment right before I let you cum? That's when I'd usually ask you "
                                      + "to give up your soul. As you'll understand, they never refuse at that point. But given all of our "
                                      + "nighttime fun, it would have been a terrible shame to do that to you. Now go and get cleaned up; "
                                      + "you won't be any fun if you're crawling across the Quad all night. Actually, now that I think about it, "
                                      + "if I could get my hands on one of those strap-ons, you might be...\"</i> taking this worrying prospect as "
                                      + "your cue, you quickly stumble up the stairs. After taking a quick breather and cup of coffee in the "
                                      + "chapel's kitchen, you walk back home, still drowsy. You probably shouldn't do that too often, "
                                      + "lest she changes her mind about your value to her.<br>"
                                      + "Then again, you realize you didn't have any choice from the start. ");
            }
            Global.gui()
                  .choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Seduction);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Sparring")) {
            if (npc.getAffection(player) >= 12 && (!player.has(Trait.clairvoyance) || Global.random(2) == 1)) {
                if (!player.has(Trait.clairvoyance)) {
                    Global.gui()
                          .message("<i>\"Are you that eager to get your ass... kicked, shall we say... again? I must "
                                          + "say I admire your courage. Tell you what, if you do a little something for me, "
                                          + "I'll teach you a little trick, give you a fighting chance. Come with me.\"</i> "
                                          + "You are certainly intrigued and the prospect of learning something is more appealing "
                                          + "than waking up sore again in places you really shouldn't be sore, so you follow her.<br/><br/>"
                                          + "You end up at the library, of all places, and Reyka leads you to the back. In front of "
                                          + "an old limestone archway leading to a secluded section, she stops. <i>\"See this?\"</i> she asks, pointing "
                                          + "towards a small sigil carved into the stone. <i>\"I can't pass by that, or touch it. So you "
                                          + "just have to scratch it out with this.\"</i> She holds out a knife and you hesitantly take it. "
                                          + "You've never seen the sigil before, it just looks like a random scribble, but you figure "
                                          + "demonic 'tricks' are worth removing a scribble. You press the knife into the limestone "
                                          + "and scratch over it. Soon enough, Reyka pats you on the shoulder and walks past you into "
                                          + "the aisle of books. She quickly returns, dusty tome in hand, and gestures you to follow.<br/><br/>"
                                          + "This time, she led you to her basement room beneath the chapel. Only slightly feeling like "
                                          + "a sacrificial lamb, you watch as she puts the book away and turns to face you. <i>\"You are probably "
                                          + "curious as to what I'll teach you, but first there is a little something to take care of.\"</i> After "
                                          + "she finishes saying this, the room lights up in purple fire. It doesn't burn you, but it sure "
                                          + "does something. You feel tingly all over, and not the pleasant kind you've come to expect from Reyka. "
                                          + "A few seconds later, the flames die down. You don't feel any different, but Reyka is looking at "
                                          + "you in a way that makes you pretty sure it worked, whatever it was. <i>\"Now, just close your eyes, "
                                          + "concentrate for a moment, and tell me if you feel any different.\"</i>  You do as she says, but at "
                                          + "first there is nothing. Just as you are about to tell her so, you notice some soft music, "
                                          + "seemingly far away. She must have seen you cringe, as she says: <i>\"Good, focus on that. Hear "
                                          + "it right in your soul and watch for any changes.\"</i> You listen carefully, and suddenly the "
                                          + "music spikes, followed quickly by a light punch to your face. <i>\"Now you know what to listen."
                                          + "Let me explain: I have given you the ability to tap into the minds of others. It's not "
                                          + "strong enough to hear their thoughts or anything like that, but you'll notice when they are "
                                          + "planning something. With practice, you might be able to tell just what they're going to do and "
                                          + "then do something about it. Useful right? Now, about those rules; Creepy Voice told me they were made "
                                          + "because none of the other competitors could do what I do. Keep things at least somewhat fair, you know? "
                                          + "With you having this ability now, I can use it in the fights too, so we both win! But for now, you "
                                          + "just need some practice, so let's start!\"</i>.<br/><br/>"
                                          + "You spend another half hour in the basement, trying to distinguish between the mild "
                                          + "musical hints preceding all kinds of slaps and punches to your face, intermixed with caresses "
                                          + "and licks everywhere else. You still need more practice, but <b>you have learned to tune in to "
                                          + "your opponents' minds, making it easier to evade and counter their attacks</b>.");
                    player.add(Trait.clairvoyance);
                    npc.getGrowth().addTrait(0, Trait.clairvoyance);
                } else {
                    Global.gui()
                          .message("<i>\"Ah, good. By now, you may have figured out that there really isn't that much room to grow, "
                                          + "I can only do so much for a mortal such as yourself. Still, it's always fun getting a "
                                          + "little practice in, so let's go to the gym.\"</i><br/><br/>"
                                          + "In the gym, you go to the same room you've already gotten your ass kicked a couple times. "
                                          + "This time, though, you feel a lot more confident. You get into your starting position, "
                                          + "and concentrate on the faint music. It's so soft you don't even notice it unless you "
                                          + "really try, doing it while fighting is a hefty challenge. Soon, the match is underway. "
                                          + "Unlike previously, Reyka doesn't stand idly, waiting for you to strike, but gets some "
                                          + "attacks of her own in as well. To the clueless observer it would look pretty boring; "
                                          + "neither of you is even touching the other. You are very evenly matched, it seems, you "
                                          + "feel you might actually have a chance at beating her! Those hopes are soon crushed, "
                                          + "maybe or maybe not along with one of your ribs, as Reyka delivers a lightning-quick jab "
                                          + "to your chest. The wind knocked out of you, you fall to your knees gasping for air. "
                                          + "When you look up, you see Reyka with an almost cute smile, holding out her hand. "
                                          + "<i>\"I just wanted to make sure you remember who's boss here, but you did well.\"</i> You latch "
                                          + "on to her hand and she pulls you up, dusting of your shoulders. <i>\"I'll give you a few minutes "
                                          + "to catch your breath and then we'll go about defending against some... different moves.\"</i><br/><br/>"
                                          + "When you have recovered from Reyka's disproportionately sharp punch, you retake your "
                                          + "position opposite her and wait for her to explain the rules of this second round. "
                                          + "So here's the deal, I am not going to go all out on you, that wouldn't be any sport. "
                                          + "Instead, I'll be performing at the level I think you can reach. I will attempt to touch "
                                          + "you, and you will evade. Every time I succeed, you take off a piece of clothing and when "
                                          + "you're naked, I'll try to get you to cum. When you do, I'll get to have a little fun with you. "
                                          + "If you haven't cum ten minutes after the match starts, you can do anything you want to me. Deal?\" "
                                          + "You think it would be terribly unwise to argue with her now that she's gotten a bit into it, so "
                                          + "you immediately accept. The first two minutes of the match are uneventful as you manage to evade "
                                          + "every lunge she makes. Once, you even managed to maneuver around her to a position in which, in "
                                          + "a regular match, you could easily have taken her down. This is rewarded by an appreciative, if "
                                          + "somewhat surprised glance over her shoulder. In response to this, she picks up the pace and you're "
                                          + "losing clothes left and right. At the six-minute mark, she starts sprinting circles around you "
                                          + "inhumanly fast before grabbing you from behind. She holds you in place with one arm across your chest, "
                                          + "while she uses her other hand to fondle your already rising dick. <i>\"Yum, playtime.\"</i> With that, she pulls "
                                          + "down your boxers and lets you go. <i>\"Next time, " + Global.getPlayer().boyOrGirl() + ", you're mine!\"</i> As you start the final round, three "
                                          + "minutes remain on the clock. You aren't trying anymore fancy moves, instead opting to stay as far away "
                                          + "as possible. She gets close a couple times though, slapping your ass a few times. Your escapes are "
                                          + "hindered somewhat by the protrusion in your groin, but you make it to eight and a half minutes, "
                                          + "nevertheless. Aware that it's almost time, Reyka again intensifies her assault, working you "
                                          + "to the ground in no time and mounting you in a 69. <i>\"Think you can last a minute? I don't.\"</i> "
                                          + "As she starts giving you an expert blowjob, you slip your hands under her skirt and massage "
                                          + "her ass. It won't make her cum, you're sure of that, but it might distract her enough to buy you enough "
                                          + "time. It didn't, however, as with only eight seconds left before your salvation, you are forced "
                                          + "to give in to her perfect technique. After properly draining you of all you had to offer, she looks "
                                          + "back at you, licking a last drop from her upper lip. <i>\"You did very well. I couldn't just let you win, "
                                          + "of course. That would be a waste of a nice meal. As a reward, though, I'll let you off easy today.\"</i> "
                                          + "After recuperating from the intense orgasm, you get up, get dressed and leave the gym. Reyka has "
                                          + "already left, probably off to work out her pent up frustrations on some poor unsuspecting frat guy, "
                                          + "you think.");
                }
            } else {
                Global.gui()
                      .message("<i>\"So basically, you want to practice sexfighting, just more the fighting than "
                                      + "the sex, yes?\"</i> Reyka asks you. <i>\"How dull, but I'm sure "
                                      + "we can make it fun!\"</i> Soon you are in a relatively small practice room in the far "
                                      + "corner of the gym with nothing but a mat in it. Reyka spins around to face you, her "
                                      + "wings suddenly materializing behind her. <i>\"Here's how this is going to work: You are going "
                                      + "to try to work me to the ground, you'll fail, and then you'll be my pet until nightfall. "
                                      + "How does that sound?\"</i> She's quite cocky given how well you've kept up with her at night. "
                                      + "<i>\"That's because there are some additional rules during the games for me, "
                                      + "courtesy of some creepy arrogant guy, which make things slightly trickier. "
                                      + "Here, I can do what I want, and I want you.\"</i> You're not convinced. After all, demons are known for "
                                      + "their incessant lying. But you will need some terms of your own: If you do bring her down, she has to "
                                      + "follow your commands instead. Reyka seems to have to struggle not to burst out laughing at the "
                                      + "suggestion, but agrees to your proposal.<br/><br/>"
                                      + "After a quick warming-up, you stand at one edge of the mat, looking straight at Reyka on the opposite side. "
                                      + "One brief countdown later, you start cautiously approaching Reyka. She has taken up a defensive "
                                      + "posture, but her wry smile tells you she is not worried in the least. Eager to take advantage of "
                                      + "her overconfidence, you suddenly swing your arm at her, hoping to disorient her. "
                                      + "She certainly has good reflexes, though, as she starts moving out of the way the same instant "
                                      + "you start your swing. The miss has left you horribly imbalanced, but she doesn't press her "
                                      + "advantage; instead she just keeps smiling. The next ten minutes are filled with repetitions of this "
                                      + "first attack: you attempt to make a move and she dodges impossibly quickly. Finally, she decides "
                                      + "she's had enough fun for now and latches onto your outstretched arm, effortlessly throwing you down. "
                                      + "The back of your head slams down on the mat hard, and the world begins to fade. Just before you pass out, "
                                      + "you see Reyka standing over you with a hungry look in her eyes and a menacing smile.<br/><br/>"
                                      + "You wake up in your own bed. Your alarm clock tells you it has been a surprisingly short time since "
                                      + "you got beaten by Reyka. Given her nature, though, a short time is probably all she needs to do some damage. "
                                      + "As you groggily get up, you become aware of the fact your entire body feels sore, particularly "
                                      + "your groin. You decide a quick, hot shower is in order before you get into any more fights. "
                                      + "While giving yourself a quick rinse, you notice marks all over your body. Upon closer inspection, "
                                      + "they're bite marks. You don't remember anything since falling to the ground in the gym, but between "
                                      + "the marks and your unerringly sore ass, it's probably better that way.");
            }
            Global.gui()
                  .choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Power);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Games")) {
            if (npc.getAffection(player) >= 16 && (!player.has(Trait.locator) || Global.random(2) == 1)) {

                if (!player.has(Trait.locator)) {
                    Global.gui()
                          .message("<i>\"Another game? Great! I liked the way the last one turned out!\"</i> She winks as she says this, "
                                          + "stirring memories you'd rather be bygones. You quickly suggest to play something else, saying you need all your "
                                          + "faculties in good condition if you are to study later. <i>\"Hey, you came to me! But alright, I guess we can "
                                          + "swap roles this time. I'll even level the playing field for you.\"</i> Unbidden images of wings sprouting "
                                          + "from your back come to mind, and you waste no time making clear to her you don't want them. <i>\"That's, "
                                          + "not what I meant, you silly " + Global.getPlayer().boyOrGirl() + ". That would require at least five souls and I'm not allowed to harvest "
                                          + "any here. No, I meant the hunting instincts, you know, so you can track my movements?\"</i> Come again? "
                                          + "That kind of defeats the point of the game, but it sure sounds interesting... You ask her to elaborate. "
                                          + "<i>\"Well, it's quite simple really: I weave a little spell, and you gain a seventh sense!\"</i> You are too "
                                          + "stunned by the prospect of a demon rummaging in your mind to notice the word 'seventh' where you "
                                          + "would have expected 'sixth'. You probably wouldn't want to know, anyway. <i>\"It works like this: if you're "
                                          + "intimate enough with someone, there is a natural connection between you. All you need to do is force that connection "
                                          + "open with some outside help. I'm pretty practiced, but you may need a catalyst like a talisman in order to do it. "
                                          + "It wouldn't be perfect, of course, and the recoil from forcing it open may make you a bit 'hot', but it'll give you an "
                                          + "idea of where to look.\"</i> You still aren't too thrilled about all this, but the possibilities are tempting. "
                                          + "You agree to let her work her magic. Smiling, she holds her hand against your head, and pulls it away again "
                                          + "after a few seconds. <i>\"What? Were you expecting fireworks? Let's go and give it a try.\"</i>");
                    player.add(Trait.locator);
                }
                Global.gui()
                      .message("Reyka pulls you out towards the dorms. A lot of people, men and women alike, look your way, "
                                      + "jealous at the " + Global.getPlayer().guyOrGirl() + " who is getting tugged towards the dorms by a stunningly beautiful woman "
                                      + "with a devilish grin on her face. You are not so sure if you are all that lucky, but hey, it "
                                      + "could be worse. You just hope she won't drag you to a family dinner some time, that brother of "
                                      + "hers doesn't sound like the type of guy you would get along with. That, and the cuisine would "
                                      + "probably not be to your liking.<br/><br/>"
                                      + "Arriving at the dorm, Reyka says the rules will be the same as last time, only she wont fly "
                                      + "this time. She hands you a ring, presumably hers although you haven't seen it before. <i>\"Make sure "
                                      + "to be somewhere private when you try and trace me, would you? We wouldn't want to draw any attention.\"</i> "
                                      + "She briskly walks off to her own starting point. You'll have to intercept her before "
                                      + "she reaches the showers. You thought it would be too easy that way, given how you are close to them. "
                                      + "Then again, it's very busy hear during the daytime, and it would be easy to get lost in the crowds. It's "
                                      + "probably best if you try to catch her before she reaches the dorms. First, however, you need to find a "
                                      + "secluded spot so you can 'see' where she'll be coming from. Walking into the bathroom, you duck into a stall "
                                      + "and pull out the ring Reyka gave you. You hold it in your clenched fist and concentrate on a mental image "
                                      + "of her face. It's not hard to create that image, you committed every detail of it to memory the first time "
                                      + "you laid eyes on her. Suddenly, an image of the library flashes in you mind. It's only for a second, but "
                                      + "at least you now where she's starting. When your own vision returns, your fist is ablaze in purple flames. "
                                      + "Shocked, you let go of the ring, but all that's left is a ball of smoke which makes it's way right up "
                                      + "your nostrils. It smells... different. You are somewhat worried about what you just breathed in, but "
                                      + "at least the flames are gone. You remember you should be trying to get to Reyka, she'll be on her way "
                                      + "over here by now. You walk towards the exit, only to see Reyka already walking by the doorway. "
                                      + "How did she get here so quickly, especially without the use of her wings...<br/><br/>"
                                      + "Seeing you, Reyka quickly ducks into a hallway to the side and you run after her. At this rate, she'll beat "
                                      + "you to the showers, so you try to find a different route and cut her off. While running, you notice "
                                      + "a stirring in your groin. Images of previous encounters with Reyka come to mind, but you cast them aside"
                                      + "and keep going. As you make your final turn, you can see the door to the women's showers right in front "
                                      + "of you. You hope you've gotten there in time, but when you are only a few steps away, Reyka crashes into "
                                      + "you from the side. You get back up and stumble towards the door, reaching it seconds before she does, "
                                      + "blocking her way. As she turns to look at you, you now have a full-on erection tenting your pants. "
                                      + "Reyka sees this and licks her lips. <i>\"I probably should have told you, but that smoke is quite a potent "
                                      + "aphrodisiac. Well, anyway, you won and now you can do anything you like to me. No one is taking a "
                                      + "shower right now anyway.\"</i> You should have known she'd set you up. From the 'accidental' omission of "
                                      + "some rather important information to the choice of destination; she planned for this all along. "
                                      + "You want her, you want her badly, but you are also quite pissed at her. So as punishment, you'll not "
                                      + "give her what she wants. You shake off the temptation and promise yourself a good jerk off session later. "
                                      + "For now, you intent to use your prize to get some answers. The first of those being just how she got "
                                      + "here that fast. Even at a dead run, it should have taken her three times longer to get here from the "
                                      + "library. <i>\"Who ever said that ring belonged to me? I 'loaned' it from a girl who was visiting the chapel. "
                                      + "The rules did not stipulate that I had to give you something of mine, you just assumed I did.\"</i> This answer "
                                      + "does not so much good for your temper, and you angrily ask her if there's anything else you should know "
                                      + "about this thing she made you able to do. <i>\"You already know about the smoke... Oh! You can't spy at anyone "
                                      + "on consecrated grounds; it would just give you a headache if you tried. And don't try to use it on yourself: "
                                      + "it's pointless and the feedback loop is not something you want to experience. Apart from that, you know "
                                      + "all there is to know, so are we going to do something about that problem of yours?\"</i> She runs her hand over "
                                      + "your crotch for those last few words, but you quickly back away muttering something about studying and "
                                      + "run off straight to your room. For once, you arrived back from a visit to Reyka fully intact, just the raging "
                                      + "boner to worry about. Ah, well. You did voluntarily visit a succubus, what did you expect?");
            } else {
                Global.gui()
                      .message("Seeing Reyka confused is certainly a change, she is usually the very image of confidence. "
                                      + "<i>\"A game? You want to play a game? An actual, no-sex game?\"</i> You nod affirmatively, "
                                      + "wary of what she might do next. <i>\"Well... I don't know any games. At least, none that do "
                                      + "not involve any of the tortured souls Father used to keep around the house. What do you "
                                      + "propose we do?\"</i> After a moment of careful deliberation, you decide to keep things close to "
                                      + "her nature at first. You ask her what those games she used to play, hoping you might "
                                      + "adapt one into a non-violent, non-sexual, non-torturery variant. <i>\"Oh you know, the usual. "
                                      + "Scaring them, planting all kinds of traps to decapitate, dismember or otherwise mutilate them. "
                                      + "They always come back anyway, the already dead can't die again. You see, my brother is a "
                                      + "demon of rage, so he's into that kind of stuff. And my powers only came with physical maturity, "
                                      + "so I tagged along with him. My favorite was always making them think they had a chance at escape; they "
                                      + "never seem to figure out there isn't any. Later, when I reached adulthood I much preferred going out "
                                      + "on my own, usually up here somewhere, getting some... experience in. Along with other things.\"</i> "
                                      + "You aren't sure what bothers you most, what she had said, or the casual tone in which she had "
                                      + "said it. Still, you think you can play in on those predatory instincts of hers. So, demonic "
                                      + "hide and seek it is. You explain the rules to her. <i>\"That won't cut it. I have to exert myself "
                                      + "finding you and not get to have a little snack? Oh, no. Not happening.\"</i> Ah, well, baby steps.<br/><br/>"
                                      + "So it's going to be a good old stalk: You start at your dorm, Reyka in the student union. At "
                                      + "a predetermined time, you will try to get to the engineering department and she will try to catch "
                                      + "you. If she does, you'll have to go with her to her place and get the life sucked out of you. "
                                      + "Well, some of it at least... You hope this will open the way to some more innocuous games.<br/><br/>"
                                      + "You quickly get to your starting position and when the moment comes, you start running. "
                                      + "You figure you'll be easy to catch in the Quad, so you decide to go through the tunnel and "
                                      + "the cafeteria. There, you can take the back exit - off-limits during the night - and make your way "
                                      + "around to the engineering department. You reach the cafeteria without incident, save for people giving "
                                      + "you some strange looks, and locate the door. After looking around to make sure no one is watching, you "
                                      + "open the door, check the alley and, finding it clear, step outside. As you walk towards the access road "
                                      + "which runs behind both the cafeteria and the engineering building for deliveries, you hear a faint "
                                      + "rustling above you. You turn around just in time to see Reyka, wings outstretched, swooping down on you. "
                                      + "When she collides with you, you fall flat on your back with her on top. <i>\"Found you! Normally I'd "
                                      + "take you right here, but a deal is a deal.\"</i> She gives you a pat on your head and lets you up. When you "
                                      + "look at her again, her wings are gone. You ask why she was allowed to use them here in the first place, "
                                      + "since you thought she was not allowed to display any demonic traits in public. <i>\"I'm not, but no one "
                                      + "ever comes back here. Except for deliveries and shady deals, neither of which take place at this hour. "
                                      + "Now come along!\"</i><br/><br/>"
                                      + "When you return to your room, you feel like you got run over by a train. Reyka exercises some restraint "
                                      + "with her feeding when she beats you at night, since it makes it all the more fun for her should you run into "
                                      + "her again. She didn't do so just now. Still, after a quick meal and a hot shower, you are ready to go back "
                                      + "out.");
            }
            Global.gui()
                  .choose(this, "Leave");
            Daytime.train(player, npc, Attribute.Cunning);
            npc.gainAffection(player, 1);
            player.gainAffection(npc, 1);
        } else if (choice.equals("Leave")) {
            done(true);
        } else {
            done(false);
        }
    }
    
    @Override
    public Optional<String> getAddictionOption() {
        return Global.getPlayer().checkAddiction(AddictionType.CORRUPTION) ? Optional.of("Strengthen Corruption") : Optional.empty();
    }
}
