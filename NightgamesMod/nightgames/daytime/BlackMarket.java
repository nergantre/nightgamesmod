package nightgames.daytime;

import java.util.Map;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;

public class BlackMarket extends Store {
    private boolean trained;

    public BlackMarket(Character player) {
        super("Black Market", player);
        add(Item.Aphrodisiac);
        add(Item.SPotion);
        add(Item.DisSol);
        add(Item.Beer);
        add(Item.PriapusDraft);
        add(Item.BustDraft);
        add(Item.FemDraft);
        add(Item.TinyDraft);
        add(Item.SuccubusDraft);
        add(Item.TentacleTonic);
        add(Item.BewitchingDraught);
        add(Item.JuggernautJuice);
        add(Item.TinkersMix);
        add(Item.Totem);
    }

    @Override
    public boolean known() {
        return Global.checkFlag(Flag.blackMarket);
    }

    @Override
    public void visit(String choice) {
        if (choice.equals("Start")) {
            acted = false;
            trained = false;
        }
        Global.gui().clearText();
        Global.gui().clearCommand();
        if (choice.equals("Leave")) {
            done(acted);
            return;
        }
        checkSale(choice);
        if (player.human()) {
            if (Global.checkFlag(Flag.blackMarketPlus)) {
                if (!Global.checkFlag(Flag.metRin)) {
                    Global.gui().message(
                                    "You knock on the door to the black market. When Ridley answers, you tell him that you're here to see his premium goods on behalf of "
                                                    + "Callisto. Ridley glances back into the room for a moment and then walks past you without saying anything. You stand there confused, until you see the "
                                                    + "girl on the couch stand up and approach you with a smile. <i>\"Hello "
                                                    + player.name()
                                                    + ",\"</i> she says while extending her hand. <i>\"I'm Rin Callisto. You shouldn't "
                                                    + "be surprised I know who you are, you've been putting on a good show lately.\"</i> You've seen her here before, but you've never taken a good look at her. She "
                                                    + "has elegant features, shoulder length black hair, and looks a couple years older than you. She's very pretty, but you overlooked her because you assumed she "
                                                    + "was Ridley's girlfriend. <i>\"Mike isn't the most pleasant company, but he's a good middleman. He keeps his mouth shut and he doesn't ask questions.\"</i> He must "
                                                    + "not be reliable enough to earn her trust, otherwise she wouldn't feel the need to keep an eye on him all the time. <i>\"Aesop sold you my name, right? I'll have "
                                                    + "to collect my share from him later. I have many items you won't find anywhere else; items that will give you an edge in the games. Not all of them are completely "
                                                    + "safe, but I think you knew that when you came looking for the black market.\"</i>");
                    Global.flag(Flag.metRin);
                } else if (choice.startsWith("Cursed Artifacts")) {
                    Global.gui().message(
                                    "You ask Rin about the assorted tomes and unpleasant looking idols she's laid out. <i>\"You should be careful with those, they're all "
                                                    + "cursed. None of them will kill you unless you're unusually susceptible to such things, but the effects would not be pleasant.\"</i> Ok, it sounds "
                                                    + "like these aren't what you're looking for. You aren't looking to get cursed, no matter how useful the artifacts are. <i>\"The items aren't very "
                                                    + "valuable themselves, otherwise I'd have sold them to collectors. A skilled spiritualist could refine the curse to bestow an unholy boon. Fortunately, "
                                                    + "I have the training to do so.\"</i> An unholy boon? That sounds more than a bit worrying. <i>\"Are you religious? In my experience, demons are just another "
                                                    + "flavor of spirit. It's still your choice, do what you like.\"</i>");
                    Global.flag(Flag.darkness);
                    acted = true;
                } else if (choice.startsWith("Dark Power")) {
                    if (player.money >= 1000 * (player.getPure(Attribute.Dark) + 1)) {
                        player.money -= 1000 * (player.getPure(Attribute.Dark) + 1);
                        Global.gui().message(
                                        "Rin lights some incense and has you lie down on the couch with one of the cursed artifacts on your chest. As she performs a lengthy "
                                                        + "ritual, you feel your body heat up and an overwhelming sense of danger flood through you. You're certain something powerful is trying to take control "
                                                        + "of your soul. Rin chants softly and wraps a talisman around a wooden rod. The presence inside you looms and seems ready to devour you, when suddenly she "
                                                        + "strikes the artifact with the rod. A shock runs through your consciousness, and the sense of danger disappears. The dark power is still present, but it "
                                                        + "seems tame now, willing to obey your command should you call for it. <i>\"The ritual is complete. You can keep the artifact as a souvenir, but all its "
                                                        + "power is in you now.\"</i><br/><br/>");
                        player.mod(Attribute.Dark, 1);
                        acted = true;
                        trained = true;
                    } else {
                        Global.gui().message("You can't afford the artifacts.");
                    }
                } else if (choice.equals("S&M Gear")) {
                    Global.gui().message(
                                    "Rin opens a case containing a variety of S&M toys and bondage gear. <i>\"You probably shouldn't touch those,\"</i> she says as you reach for them. "
                                                    + "<i>\"I didn't pick these up at a sex shop, they have potent enchantments on them.\"</i> Enchanted sex toys? At this point, you'll believe just about anything. How "
                                                    + "useful are they? <i>\"They won't do you much good to use them during the match, but if you use them now, their power will transfer to you. Mostly they'll give "
                                                    + "you fetishes that you'll be able to share with your opponents. It's a high risk, high reward style of sex-fighting.\"</i> Rin smiles and reclines on the couch. "
                                                    + "<i>\"If you buy something, I don't mind helping you try it out. No extra charge of course, I'm not a prostitute.\"</i>");
                    Global.flag(Flag.fetishism);
                    acted = true;
                } else if (choice.startsWith("Fetishism")) {
                    if (player.money >= 1000 * (player.getPure(Attribute.Fetish) + 1)) {
                        player.money -= 1000 * (player.getPure(Attribute.Fetish) + 1);
                        Global.gui().message(
                                        "You select one of the S&M toys and pay Rin for it. She picks up the toy - a leather riding crop - and gives you a small wicked smile. <i>\"Good "
                                                        + "choice. This one will grant you the masochism fetish if used properly. I'm quite good with these. Get undressed and we'll get started.\"</i> Your trepidation "
                                                        + "must show on your face, because her smiles becomes slightly more reassuring. <i>\"Sometimes the path to power can be painful. The gifts in these items may "
                                                        + "bring you victory, but there's no easy way to unlock them. Besides, you'll enjoy it before we're through. That's the whole point after all.\"</i><br/><br/>");
                        player.mod(Attribute.Fetish, 1);
                        acted = true;
                        trained = true;
                    } else {
                        Global.gui().message("You can't afford that.");
                    }
                } else {
                    Global.gui().message(
                                    "Ridley leaves the black market as soon as he sees you. It seems you're a preferred client now, and you can deal with Rin directly. Rin gives "
                                                    + "you a polite smile and stands up. <i>\"Welcome back. What can I do for you?\"</i>");
                }
                if (!trained) {
                    if (!Global.checkFlag(Flag.darkness)) {
                        Global.gui().choose(this, "Cursed Artifacts");
                    } else {
                        Global.gui().choose(this, "Dark Power: $" + 1000 * (player.getPure(Attribute.Dark) + 1));
                    }
                    if (!Global.checkFlag(Flag.fetishism)) {
                        Global.gui().choose(this, "S&M Gear");
                    } else {
                        Global.gui().choose(this, "Fetishism: $" + 1000 * (player.getPure(Attribute.Fetish) + 1));
                    }
                }
            } else {
                Global.gui().message(
                                "You knock on the door to the room Aesop pointed you to. A somewhat overweight man, a few years older than you, looks you over for a moment "
                                                + "before letting you in. The dorm room is tidier than its occupant, whose clothes are noticeably stained and smell faintly of old beer and weed. There's a bong on "
                                                + "the nearby table and an attractive girl on the couch who makes no indication that she noticed you enter. <i>\"Don't mind the bitch,\"</i> says Ridley, noticing your "
                                                + "attention. <i>\"She doesn't care who you are and neither do I. What are you looking for?\"</i>");
            }
            Map<Item, Integer> MyInventory = this.player.getInventory();
            for (Item i : stock.keySet()) {
                if (MyInventory.get(i) == null || MyInventory.get(i) == 0) {
                    Global.gui().message(i.getName() + ": $" + i.getPrice());
                } else {
                    Global.gui().message(
                                    i.getName() + ": $" + i.getPrice() + " (you have: " + MyInventory.get(i) + ")");
                }
            }
            Global.gui().message("You have : $" + player.money + " to spend.");
            displayGoods();
            Global.gui().choose(this, "Leave");
        }
    }

    @Override
    public void shop(Character npc, int budget) {
        int remaining = budget;
        if (npc.getPure(Attribute.Dark) > 0 && remaining >= 1000 * (npc.getPure(Attribute.Dark) + 1)) {
            if (remaining >= 2000 * (npc.getPure(Attribute.Dark) + 2)) {
                npc.money -= 1000 * (npc.getPure(Attribute.Dark) + 1);
                remaining -= 1000 * (npc.getPure(Attribute.Dark) + 1);
                npc.mod(Attribute.Dark, 1);
            }
            npc.money -= 1000 * (npc.get(Attribute.Dark) + 1);
            remaining -= 1000 * (npc.get(Attribute.Dark) + 1);
            npc.mod(Attribute.Dark, 1);
        }
        if (npc.getPure(Attribute.Fetish) > 0 && remaining >= 1000 * (npc.getPure(Attribute.Fetish) + 1)) {
            if (remaining >= 2000 * (npc.getPure(Attribute.Fetish) + 2)) {
                npc.money -= 1000 * (npc.getPure(Attribute.Fetish) + 1);
                remaining -= 1000 * (npc.getPure(Attribute.Fetish) + 1);
                npc.mod(Attribute.Fetish, 1);
            }
            npc.money -= 1000 * (npc.getPure(Attribute.Fetish) + 1);
            remaining -= 1000 * (npc.getPure(Attribute.Fetish) + 1);
            npc.mod(Attribute.Fetish, 1);
        }
        int bored = 0;
        while (remaining > 25 && bored < 5) {
            for (Item i : stock.keySet()) {
                int max = 10;
                if (i.equals(Item.PriapusDraft)) {
                    if (Global.random(10) > Global.getValue(Flag.malePref)) {
                        bored++;
                        continue;
                    }
                    max = 1;
                }
                if (i.equals(Item.FemDraft)) {
                    if (Global.random(10) < Global.getValue(Flag.malePref)) {
                        bored++;
                        continue;
                    }
                    max = 1;
                }
                if (remaining > i.getPrice() && !npc.has(i, max)) {
                    npc.gain(i);
                    npc.money -= i.getPrice();
                    remaining -= i.getPrice();
                    continue;
                }
                bored++;
            }
        }
    }
}
