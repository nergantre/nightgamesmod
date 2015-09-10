package nightgames.daytime;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import nightgames.characters.Attribute;
import nightgames.characters.Character;
import nightgames.characters.NPC;
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
import nightgames.characters.custom.effect.CustomEffect;
import nightgames.characters.custom.requirement.BodyPartRequirement;
import nightgames.characters.custom.requirement.CustomRequirement;
import nightgames.characters.custom.requirement.NotRequirement;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.items.Item;
import nightgames.items.Loot;
import nightgames.items.clothing.Clothing;

public abstract class BaseNPCTime extends Activity {
	protected NPC npc;
	String knownFlag = "";
	String noRequestedItems = "{self:SUBJECT} frowns when {self:pronoun} sees that you don't have the requested items.";
	String giftedString = "\"Awww thanks!\"";
	String giftString = "\"A present? You shouldn't have!\"";
	String transformationOptionString = "Transformations";
	String loveIntro = "[Placeholder]<br>LoveIntro";
	String transformationIntro = "[Placeholder]<br>TransformationIntro";
	String transformationFlag = "";
	Trait advTrait = null;

	public BaseNPCTime(Character player, NPC npc) {
		super(npc.getName(), player);
		this.npc = npc;
		buildTransformationPool();
	}

	@Override
	public boolean known() {
		return knownFlag.isEmpty() || Global.checkFlag(knownFlag);
	}
	List<TransformationOption> options;
	
	public abstract void buildTransformationPool();

	public List<Loot> getGiftables() {
		List<Loot> giftables = new ArrayList<>();
		player.closet.stream().filter(article -> !npc.has(article)).forEach(article -> giftables.add(article));
		return giftables;
	}
	public abstract void subVisit(String choice);
	public abstract void subVisitIntro(String choice);

	@Override
	public void visit(String choice) {
		Global.gui().clearText();
		Global.gui().clearCommand();
		List<Loot> giftables = getGiftables();

		Optional<TransformationOption> optionalOption = options.stream().filter(opt -> choice.equals(opt.option)).findFirst();
		Optional<Loot> optionalGiftOption = giftables.stream().filter(gift -> choice.equals(Global.capitalizeFirstLetter(gift.getName()))).findFirst();

		if (optionalOption.isPresent()) {
			TransformationOption option = optionalOption.get();
			boolean hasAll = option.ingredients.entrySet().stream().allMatch(entry -> player.has(entry.getKey(), entry.getValue()));
			if (hasAll) {
				Global.gui().message(Global.format(option.scene, npc, player));
				option.ingredients.entrySet().stream().forEach(entry -> player.consume(entry.getKey(), entry.getValue(), false));
				option.effect.execute(null, player, npc);
				Global.gui().choose(this, "Leave");
			} else {
				Global.gui().message(Global.format(noRequestedItems, npc, player));
				Global.gui().choose(this, "Back");
			}
		} else if (optionalGiftOption.isPresent()) {
			Global.gui().message(Global.format(giftedString, npc, player));
			if (optionalGiftOption.get() instanceof Clothing) {
				if (player.closet.contains(optionalGiftOption.get())) {
					player.closet.remove(optionalGiftOption.get());
				}
				npc.closet.add((Clothing) optionalGiftOption.get());
			}
			player.gainAffection(npc, 2);
			npc.gainAffection(player, 2);
			Global.gui().choose(this, "Back");
		} else if (choice.equals("Gift")) {
			Global.gui().message(Global.format(giftString, npc, player));
			giftables.stream().forEach(loot -> Global.gui().choose(this, Global.capitalizeFirstLetter(loot.getName())));
			Global.gui().choose(this, "Back");
		} else if (choice.equals("Change Outfit")) {
			Global.gui().changeClothes(npc, this, "Back");
		} else if (choice.equals(transformationOptionString)) {
			Global.gui().message(Global.format(transformationIntro, npc, player));
			if (transformationFlag != "")
				Global.flag(transformationFlag);
			options.forEach(opt -> {
				Global.gui().message(opt.option + ":");
				opt.ingredients.entrySet().forEach((entry) -> {
					Global.gui().message(entry.getValue() + " " + entry.getKey().getName());					
				});
				if (!opt.additionalRequirements.isEmpty()) {
					Global.gui().message(opt.additionalRequirements);
				}
				Global.gui().message("<br>");
			});
			options.forEach(opt -> {
				if (opt.requirements.stream().allMatch(req -> req.meets(null, player, npc))) {
					Global.gui().choose(this, opt.option);
				}
			});
			Global.gui().choose(this, "Back");
		} else if (choice.equals("Start") || choice.equals("Back") && (advTrait == null || npc.has(advTrait))) {
			if (npc.getAffection(player) > 25) {
				Global.gui().message(Global.format(loveIntro, npc, player));
				Global.gui().choose(this, "Games");
				Global.gui().choose(this, "Sparring");
				Global.gui().choose(this, "Sex");
				if (!options.isEmpty())
					Global.gui().choose(this, transformationOptionString);
				if (npc.getAffection(player) > 30) {
					Global.gui().choose(this, "Gift");
				}
				if (npc.getAffection(player) > 35) {
					Global.gui().choose(this, "Change Outfit");
				}
				Global.gui().choose(this, "Leave");
			} else {
				subVisitIntro(choice);
			}
		} else if (choice.equals("Leave"))
			done(true);
		else {
			subVisit(choice);
		}
	}

	@Override
	public void shop(Character paramCharacter, int paramInt) {
		paramCharacter.gainAffection(npc, 1);
		npc.gainAffection(paramCharacter, 1);

	}

}
