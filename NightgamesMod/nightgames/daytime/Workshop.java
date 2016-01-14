package nightgames.daytime;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.clothing.Clothing;

public class Workshop extends Activity {
    private boolean acted;

    public Workshop(Character player) {
        super("Workshop", player);
        acted = false;
    }

    @Override
    public boolean known() {
        return Global.checkFlag(Flag.workshop);
    }

    @Override
    public void visit(String choice) {
        Global.gui().clearText();
        Global.gui().clearCommand();
        if (!Global.checkFlag(Flag.metJett)) {
            Global.gui().message(
                            "You head to Jett's workshop. Apparently he has an implicit claim on this workshop in the same way that Mara does on her computer room. "
                                            + "When you enter the room, he's busy machining an unidentifiable metal component. When he finishes the part, he walks over to greet you, but doesn't "
                                            + "bother removing his safety glasses. He's a bit short and skinny. You can't help thinking of him as a stereotypical science nerd. <i>\"You're "
                                            + player.name() + "? "
                                            + "You don't need to nod, that was a rhetorical question. I've seen your fights.\"</i> His tone is not quite hostile, but he's clearly not interested in making "
                                            + "friends. <i>\"If you're looking to upgrade your toys, I'm your man. It's not cheap, but my work is well worth the money.\"</i> Aesop made it sound like Jett "
                                            + "could make some more versitile equipment. What all is for sale? Jett gives you a distainful look. <i>\"Nothing. I'm not selling anything potentially dangerous "
                                            + "to someone who doesn't know how to maintain and operate it safely. I'm not risking you sodding up one of my inventions in a way that gets a girl hurt. What "
                                            + "I am willing to do is teach you how I put my equipment together. If you're a quick study and can understand how my toys work, I'll help you build your own. Oh, "
                                            + "and I'm not helping you for free. No offence, but I'm rooting for your opponents. Male solidarity is fine and well, but they're a lot more attractive than you.\"</i>");
            Global.flag(Flag.metJett);
            acted = true;
            Global.gui().choose(this, "Lecture: $" + 1000 * (player.getPure(Attribute.Science) + 1));
        } else if (choice.equals("Start")) {
            Global.gui().message(
                            "You head to Jett's workshop. He sets down the parts he was working on and turns to face you. <i>\"You need something? I hope you brought your "
                                            + "wallet.\"</i>");
            Global.gui().choose(this, "Lecture: $" + 1000 * (player.getPure(Attribute.Science) + 1));
            acted = false;
        } else if (choice.equals("Upgrade Dildo: $2000")) {
            if (player.money >= Item.Dildo2.getPrice()) {
                Global.gui().message(
                                "You hand over the dildo and ask Jett how he plans to upgrade it. <i>\"If I hollow out a small space inside it, there will be enough "
                                                + "room for a compact oscillating motor.\"</i> That's it? He's just going to put a motor in it? You could have just bought a vibrating dildo at the sex "
                                                + "shop. <i>\"No. Nonono... no, you don't want to do that. That's a classic beginner's mistake. Crude store bought vibrators just overstimulate nerve "
                                                + "endings and cause numbness. This magnetic motor will allow me to tune the vibration to the ideal frequency to maximize pleasure without reducing "
                                                + "sensation over time. It's not cheap, but you wouldn't be coming to me if you weren't willing to shell out a bit.\"</i><p>After a few minutes, he sets "
                                                + "the dildo on the table in front of you. <i>\"It's on, try touching it.\"</i> What does he mean it's on? The dildo is completely silent and motionless. You "
                                                + "pick up the dildo, but immediately drop it in surprise. It's vibrating, but that doesn't feel like vibration you've ever experienced, it's almost "
                                                + "like a current running through your arm. Jett clearly knows what he's doing. You're not going to question his pricing again.");
                player.money -= Item.Dildo2.getPrice();
                player.consume(Item.Dildo, 1, false);
                player.gain(Item.Dildo2);
                acted = true;
            } else {
                Global.gui().message("You can't afford that upgrade");
            }
        } else if (choice.equals("Upgrade Tickler: $3000")) {
            if (player.money >= Item.Tickler2.getPrice()) {
                Global.gui().message(
                                "Jett rummages around in the back of his workshop for awhile before returning with a small box of feathers. <i>\"Arm,\"</i> he demands bluntly. "
                                                + "You extend your arm and he touches it with one of the feathers. It feels like a normal feather, but goosebumps appear where it touched. Your skin feels "
                                                + "more sensitive where it touched. <i>\"The feathers come from a bird which secretes an oil that can increase sensitivity in primates. There's no evolutionary "
                                                + "reason for this, it's just a quirk of nature.\"</i> He inserts a few of these into your tickler and returns it. <i>\"That's sufficient. I once made a tickler "
                                                + "entirely out of these. It wasn't a good idea.\"</i>");
                player.money -= Item.Tickler2.getPrice();
                player.consume(Item.Tickler, 1, false);
                player.gain(Item.Tickler2);
                acted = true;
            } else {
                Global.gui().message("You can't afford that upgrade");
            }
        } else if (choice.equals("Upgrade Riding Crop: $1500")) {
            if (player.money >= Item.Crop2.getPrice()) {
                Global.gui().message(
                                "Jett works on the riding crop for a few minutes and when he returns it, there's a short length of cord attached to the end. you feel the "
                                                + "cord. It's flexible and softer than leather, but still has some resilience. <i>\"I call it the 'Treasure Hunter.' It's a painful, yet elegant attachment that's "
                                                + "the right length and size to hit extremely sensitive areas. It's effective against both male and female anatomy. Simple enough to accomplish, but the real "
                                                + "trick was finding the ideal material. My solution is quite painful on impact, but has no risk of breaking the skin or leaving unpleasant welts.\"</i> You take "
                                                + "the crop with great care. It suddenly feels quite dangerous.");
                player.money -= Item.Crop2.getPrice();
                player.consume(Item.Crop, 1, false);
                player.gain(Item.Crop2);
                acted = true;
            } else {
                Global.gui().message("You can't afford that upgrade");
            }
        } else if (choice.equals("Upgrade Onahole: $3000")) {
            if (player.money >= Item.Onahole2.getPrice()) {
                Global.gui().message(
                                "Jett reluctantly takes the Onahole from you. <i>\"You wash this, right?\"</i> He takes it to the back room and is gone for a while. He returns "
                                                + "and hands you the cock sleeve which is not visibly different. <i>\"Put your finger inside.\"</i> It's hot and wet, almost indistinguishable from the real thing. "
                                                + "<i>\"It's self lubricating and maintains a temperature slightly above a normal human body.\"</i> Quite impressive, and there's no obvious heating mechanism or "
                                                + "liquid supply. How does it work? <i>\"Trade secret. Sorry mate, but until the patent is finalized, I can't reveal the magic trick.\"</i>");
                player.money -= Item.Onahole2.getPrice();
                player.consume(Item.Onahole, 1, false);
                player.gain(Item.Onahole2);
                acted = true;
            } else {
                Global.gui().message("You can't afford that upgrade");
            }
        } else if (choice.equals("Upgrade Strapon: $2500")) {
            if (player.money >= Item.Strapon2.getPrice()) {
                Global.gui().message(
                                "[Placeholder]<br>Jett upgrades your strapon with a more flexible body and a vibration feature.");
                player.money -= Item.Strapon2.getPrice();
                player.consume(Item.Strapon, 1, false);
                player.gain(Item.Strapon2);
                acted = true;
            } else {
                Global.gui().message("You can't afford that upgrade");
            }
        } else if (choice.equals("Leave")) {
            done(acted);
            return;
        } else if (choice.startsWith("Lecture")) {
            if (player.money >= 1000 * (player.getPure(Attribute.Science) + 1)) {
                Global.gui().message(
                                "They say that geniuses make poor teachers, but Jett disproves that theory. He explains the principles behind his work in a way that you "
                                                + "can easily follow. Despite his unfriendly demeanor, he answers any questions you have without complaint. After about an hour of intense lecture, you "
                                                + "feel like you've gotten the benefits of a week of classes.<p><i>\"Some of this equipment is likely to consume battery power rapidly. If you need to "
                                                + "recharge during a match, there are a few compatible charging stations in the Mechanical Engineering workshops.\"</i>");
                player.money -= 1000 * (player.getPure(Attribute.Science) + 1);
                player.mod(Attribute.Science, 1);
                if (!player.has(Item.ShockGlove)) {
                    player.gain(Item.ShockGlove);
                }
                if (!player.has(Clothing.getByID("labcoat"))) {
                    player.gain(Clothing.getByID("labcoat"));
                }
                if (player.getPure(Attribute.Science) >= 4 && !player.has(Item.Aersolizer)) {
                    player.gain(Item.Aersolizer);
                }
                acted = true;
            } else {
                Global.gui().message("You don't have enough money for Jett's lecture.");
            }
            acted = true;
        }
        if (player.has(Item.Dildo)) {
            Global.gui().choose(this, "Upgrade Dildo: $2000");
        }
        if (player.has(Item.Tickler)) {
            Global.gui().choose(this, "Upgrade Tickler: $3000");
        }
        if (player.has(Item.Crop)) {
            Global.gui().choose(this, "Upgrade Riding Crop: $1500");
        }
        if (player.has(Item.Onahole)) {
            Global.gui().choose(this, "Upgrade Onahole: $3000");
        }
        if (player.has(Item.Strapon)) {
            Global.gui().choose(this, "Upgrade Strapon: $2500");
        }
        Global.gui().choose(this, "Leave");

    }

    @Override
    public void shop(Character npc, int budget) {
        int remaining = budget;
        if (npc.getPure(Attribute.Science) > 0 && remaining >= 1000) {
            npc.money -= 1000;
            remaining -= 1000;
            npc.mod(Attribute.Science, 1);
            if (!npc.has(Item.ShockGlove)) {
                npc.gain(Item.ShockGlove);
            }
        }
        if (npc.has(Item.Onahole) && remaining >= Item.Onahole2.getPrice()) {
            npc.money -= Item.Onahole2.getPrice();
            remaining -= Item.Onahole2.getPrice();
            npc.consume(Item.Onahole, 1, false);
            npc.gain(Item.Onahole2);
        }
        if (npc.has(Item.Dildo) && remaining >= Item.Dildo2.getPrice()) {
            npc.money -= Item.Dildo2.getPrice();
            remaining -= Item.Dildo2.getPrice();
            npc.consume(Item.Dildo, 1, false);
            npc.gain(Item.Dildo2);
        }
        if (npc.has(Item.Tickler) && remaining >= Item.Tickler2.getPrice()) {
            npc.money -= Item.Tickler2.getPrice();
            remaining -= Item.Tickler2.getPrice();
            npc.consume(Item.Tickler, 1, false);
            npc.gain(Item.Tickler2);
        }
        if (npc.has(Item.Crop) && remaining >= Item.Crop2.getPrice()) {
            npc.money -= Item.Crop2.getPrice();
            remaining -= Item.Crop2.getPrice();
            npc.consume(Item.Crop, 1, false);
            npc.gain(Item.Crop2);
        }
        if (npc.has(Item.Strapon2) && remaining >= Item.Strapon2.getPrice()) {
            npc.money -= Item.Strapon2.getPrice();
            remaining -= Item.Strapon2.getPrice();
            npc.consume(Item.Strapon, 1, false);
            npc.gain(Item.Strapon2);
        }
        if (npc.getPure(Attribute.Science) > 0 && remaining >= 1000) {
            npc.money -= 1000;
            remaining -= 1000;
            npc.mod(Attribute.Science, 1);
            if (!npc.has(Item.ShockGlove)) {
                npc.gain(Item.ShockGlove);
            }
        }
    }

}
