package nightgames.daytime;

import java.util.*;

import nightgames.characters.Character;
import nightgames.characters.Trait;
import nightgames.characters.body.*;
import nightgames.daytime.BodyShop.ShopSelection;
import nightgames.global.DebugFlags;
import nightgames.global.Flag;
import nightgames.global.Global;
public class BodyShop extends Activity  {
	List<ShopSelection> selection;
	public BodyShop(Character player) {
		super("Body Shop", player);
		selection = new ArrayList<ShopSelection>();
		populateSelection();
	}

	abstract class ShopSelection {
		String choice;
		int price;
		ShopSelection(String choice, int price) {
			this.choice = choice + " (" + price+ ")";
			this.price = price;
		}
		abstract void buy(Character buyer);
		abstract boolean available(Character buyer);
		double priority(Character buyer) {
			return 5;
		}
		@Override
		public String toString() {
			return choice;
		}
	}

	interface CharacterRequirement {
		boolean isSatisfied(Character character);
	}

	private void addBodyPartMod(String name, final BodyPart part, final BodyPart normal, int growPrice, int removePrice) {
		addBodyPartMod(name, part, normal, growPrice, removePrice, 5, false);
	}

	private void addBodyPartMod(String name, final BodyPart part, final BodyPart normal, int growPrice, int removePrice, final int priority, final boolean onlyReplace) {
		selection.add(new ShopSelection("Body Mod: " + name, growPrice) {
			@Override
			void buy(Character buyer) {
				if (normal == null)
					buyer.body.addReplace(part, 1);
				else {
					buyer.body.addReplace(part, 1);
				}
			}

			@Override
			boolean available(Character buyer) {
				boolean possible = true;
				if (onlyReplace) {
					possible = buyer.body.has(part.getType());
				}
				if (normal == null)
					return possible && !buyer.body.has(part.getType()); // never available
				else
					return possible && !buyer.body.contains(part);
			}

			@Override
			double priority(Character buyer) {
				return priority;
			}
		});

		selection.add(new ShopSelection("Body Mod: Remove " + name, removePrice) {
			@Override
			void buy(Character buyer) {
				if (normal == null) {
					buyer.body.removeOne(part.getType());
				} else {
					buyer.body.remove(part);
					buyer.body.addReplace(normal, 1);
				}
			}

			@Override
			boolean available(Character buyer) {
				if (normal == null)
					return buyer.body.has(part.getType());
				else
					return buyer.body.contains(part);
			}
			
			@Override
			double priority(Character buyer) {
				return 1;
			}
		});
	}
	
	private void addCockMod(String name, CockPart part) {
		selection.add(new ShopSelection("Body Mod: " + name, 2000) {

			@Override
			void buy(Character buyer) {
				buyer.body.addReplace(part, 1);
			}

			@Override
			boolean available(Character buyer) {
				return buyer.body.has("cock") && buyer.body.getRandomCock().isGeneric();
			}
			
			@Override
			double priority(Character buyer) {
				return buyer.dickPreference();
			}
			
		});
		
		selection.add(new ShopSelection("Body Mod: Remove " + name, 2000) {
			@Override
			void buy(Character buyer) {
				buyer.body.addReplace(CockPart.big, 1);
			}

			@Override
			boolean available(Character buyer) {
				return buyer.body.getRandomCock() == part;
			}
			
			@Override
			double priority(Character buyer) {
				return Math.max(0, buyer.pussyPreference() - 7);
			}
		});
	}

	private void addTraitMod(String name, String removeName, final Trait trait, int addPrice, int removePrice, final CharacterRequirement requirement) {
		selection.add(new ShopSelection(name, addPrice) {
			@Override
			void buy(Character buyer) {
				buyer.add(trait);
			}

			@Override
			boolean available(Character buyer) {
				return !buyer.has(trait) && requirement.isSatisfied(buyer);
			}
		});

		selection.add(new ShopSelection(removeName, removePrice) {
			@Override
			void buy(Character buyer) {
				buyer.remove(trait);
			}

			@Override
			boolean available(Character buyer) {
				return buyer.has(trait);
			}

			@Override
			double priority(Character buyer) {
				return 1;
			}
		});
	}

	private void populateSelection() {
		CharacterRequirement noRequirement = new CharacterRequirement() {
			@Override
			public boolean isSatisfied(Character character) {
				return true;
			}
		};

		selection.add(new ShopSelection("Breast Expansion", 1500) {
			@Override
			void buy(Character buyer) {
				BreastsPart target = buyer.body.getBreastsBelow(BreastsPart.maximumSize().size);
				assert(target != null);
				buyer.body.remove(target);
				buyer.body.addReplace(target.upgrade(), 1);
			}
			@Override
			boolean available(Character buyer) {
				BreastsPart target = buyer.body.getBreastsBelow(BreastsPart.maximumSize().size);
				return target != null;
			}
			
			@Override
			double priority(Character buyer) {
				return 10;
			}
		});

		selection.add(new ShopSelection("Breast Reduction", 1500) {
			@Override
			void buy(Character buyer) {
				BreastsPart target = buyer.body.getBreastsAbove(BreastsPart.flat.size);
				assert(target != null);
				buyer.body.remove(target);
				buyer.body.addReplace(target.downgrade(), 1);
			}
			@Override
			boolean available(Character buyer) {
				BreastsPart target = buyer.body.getBreastsAbove(BreastsPart.flat.size);
				return target != null;
			}
			
			@Override
			double priority(Character buyer) {
				return 5;
			}
		});

		selection.add(new ShopSelection("Grow Cock", 2500) {
			@Override
			void buy(Character buyer) {
				buyer.body.addReplace(CockPart.tiny, 1);
			}
			@Override
			boolean available(Character buyer) {
				return !buyer.hasDick();
			}
			@Override
			double priority(Character buyer) {
				return buyer.dickPreference();
			}
		});

		selection.add(new ShopSelection("Remove Cock", 2500) {
			@Override
			void buy(Character buyer) {
				buyer.body.removeAll("cock");
			}
			@Override
			boolean available(Character buyer) {
				return buyer.hasDick();
			}
			@Override
			double priority(Character buyer) {
				return Math.max(0, buyer.pussyPreference() - 7);
			}
		});

		selection.add(new ShopSelection("Grow Balls", 1000) {
			@Override
			void buy(Character buyer) {
				buyer.body.addReplace(new GenericBodyPart("balls", 0, 1.0, 1.5, "balls", ""), 1);
			}
			@Override
			boolean available(Character buyer) {
				return !buyer.hasBalls();
			}
			@Override
			double priority(Character buyer) {
				return 0;
			}
		});

		selection.add(new ShopSelection("Remove Balls", 1000) {
			@Override
			void buy(Character buyer) {
				buyer.body.removeAll("balls");
			}
			@Override
			boolean available(Character buyer) {
				return buyer.hasBalls();
			}
			@Override
			double priority(Character buyer) {
				return 0;
			}
		});

		selection.add(new ShopSelection("Grow Pussy", 2500) {
			@Override
			void buy(Character buyer) {
				buyer.body.addReplace(PussyPart.normal, 1);
			}
			@Override
			boolean available(Character buyer) {
				return !buyer.hasPussy();
			}
			@Override
			double priority(Character buyer) {
				return buyer.pussyPreference();
			}
		});

		selection.add(new ShopSelection("Cock Expansion", 1500) {
			@Override
			void buy(Character buyer) {
				CockPart target = buyer.body.getCockBelow(CockPart.maximumSize().size);
				assert(target != null);
				buyer.body.remove(target);
				buyer.body.addReplace(target.upgrade(), 1);
			}
			@Override
			boolean available(Character buyer) {
				CockPart target = buyer.body.getCockBelow(CockPart.maximumSize().size);
				return target != null;
			}

			@Override
			double priority(Character buyer) {
				CockPart part = buyer.body.getRandomCock();
				if (part != null)
					return CockPart.big.size > part.size ? 10 : 3;
				return 0;
			}
		});

		selection.add(new ShopSelection("Cock Reduction", 1500) {
			@Override
			void buy(Character buyer) {
				CockPart target = buyer.body.getCockAbove(CockPart.tiny.size);
				assert(target != null);
				buyer.body.remove(target);
				buyer.body.addReplace(target.downgrade(), 1);
			}
			@Override
			boolean available(Character buyer) {
				CockPart target = buyer.body.getCockAbove(CockPart.maximumSize().size);
				return target != null;
			}
			
			@Override
			double priority(Character buyer) {
				CockPart part = buyer.body.getRandomCock();
				if (part != null)
					return CockPart.small.size < part.size ? 3 : 0;
				return 0;
			}
		});

		addTraitMod("Vaginal Mod: Grow Tongue", "Vaginal Mod: Remove Tongue", Trait.vaginaltongue, 10000, 10000, new CharacterRequirement() {
			@Override
			public boolean isSatisfied(Character character) {
				return character.hasPussy();
			}
		});
		addTraitMod("Fluids Mod: Laced Juices", "Fluids Mod: Remove Laced Juices", Trait.lacedjuices, 1000, 1000, noRequirement);
		addTraitMod("Breast Mod: Permanent Lactation", "Breast Mod: Stop Lactating", Trait.lactating, 1000, 1000, noRequirement);
		addTraitMod("Scent Mod: Pheromones", "Scent Mod: Remove Pheromones", Trait.augmentedPheromones, 1500, 1500, noRequirement);
		addBodyPartMod("Wings", WingsPart.normal, null, 1500, 1500);
		addBodyPartMod("Tail", TailPart.normal, null, 1000, 1000);
		addBodyPartMod("Fused Boots", new GenericBodyPart(
				"Fused Boots",
				"{self:name-possessive} legs are wrapped in a shiny black material that look fused on.",
				.3, 1.5, .7, true, "feet", ""),
				new GenericBodyPart("feet", 0, 1, 1, "feet", ""), 1000, 1000);
		addBodyPartMod("Anal Pussy", AnalPussyPart.generic, 
				AssPart.generic, 2000, 2000);
		addBodyPartMod("Succubus Pussy", PussyPart.succubus, 
				PussyPart.normal, 2000, 2000, -1, true);
		addBodyPartMod("Cybernetic Pussy", PussyPart.cybernetic, 
				PussyPart.normal, 2000, 2000, -1, true);
		addBodyPartMod("Feral Pussy", PussyPart.feral,
				PussyPart.normal, 2000, 2000, -1, true);
		addBodyPartMod("Fiery Pussy", PussyPart.fiery,
				PussyPart.normal, 2000, 2000, -1, true);
		addBodyPartMod("Arcane Pussy", PussyPart.arcane,
				PussyPart.normal, 2000, 2000, -1, true);
		addCockMod("Incubus Cock", CockPart.incubus);
		addCockMod("Primal Cock", CockPart.primal);
		addCockMod("Bionic Cock", CockPart.bionic);
		addCockMod("Blessed Cock", CockPart.blessed);
		addCockMod("Enlightened Cock", CockPart.enlightened);
		addBodyPartMod("Fused Gloves", new GenericBodyPart(
				"Fused Gloves",
				"{self:name-possessive} arms and hands are wrapped in a shiny black material that look fused on.",
				.2, 1.5, .7, true, "hands", ""),
				new GenericBodyPart("hands", 0, 1, 1, "hands", ""), 1000, 1000);
		addBodyPartMod("Cat ears", EarPart.cat, EarPart.normal, 1000, 1000);
		addBodyPartMod("Cat tail", TailPart.cat, TailPart.normal, 2500, 2500);
		addBodyPartMod("Pointed ears", EarPart.pointed, EarPart.normal, 1000, 1000);
		addBodyPartMod("Demonic Wings", WingsPart.demonic, WingsPart.normal, 2500, 2500);
		addBodyPartMod("Demonic tail", TailPart.demonic, TailPart.normal, 2500, 2500);
	}

	@Override
	public boolean known() {
		
		
		return Global.checkFlag(Flag.bodyShop);
	}

	private void displaySelection() {
		Global.gui().message("You have :$"+player.money+" to spend.");
		for (ShopSelection s: selection) {
			if (s.available(player) && player.money >= s.price) {
				Global.gui().choose(this, s.choice);
			}
		}
		Global.gui().choose(this,"Leave");
	}
	@Override
	public void visit(String choice) {
		if (choice.equals("Start")) {
			Global.gui().clearText();
			Global.gui().clearCommand();
			Global.gui().message("While wondering why you're even here, you walk into the rundown shack named \"The Body Shop\". The proprietor looks at you strangely then mutely points to the sign.");
			displaySelection();
			return;
		}
		for (ShopSelection s : selection) {
			if (s.choice.equals(choice)) {
				Global.gui().message("<br>You've selected " + s.choice + ". While wondering if this was such a great idea, you follow the proprietor into the back room...");
				s.buy(player);
				player.money -= s.price;
				done(true);
				return;
			}
		}
		Global.gui().message("<br>You have some second thoughts about letting some stranger play with your body. You think up some excuse and quickly leave the shack.");
		done(false);
	}

	@Override
	public void shop(Character npc, int budget) {
		int chance = 100;
		while(budget > 0) {
			if (Global.random(100) > chance) {
				break;
			}
			chance /= 3;
			List<ShopSelection> avail = new ArrayList<ShopSelection>();
			for (int i = 0; i < 10; i ++) {
				avail.add(new ShopSelection("none" + i, 0) {
					@Override
					void buy(Character buyer) {
						
					}
					@Override
					boolean available(Character buyer) {
						return true;
					}
				});
			}
			for (ShopSelection s: selection) {
				if (s.available(npc) && budget >= s.price) {
					for (int i = 0; i < s.priority(npc); i++) {
						avail.add(s);
					}
				}
			}

			if (avail.size() == 0) { return; }
			int randomindex = Global.random(avail.size());
			ShopSelection choice = avail.get(randomindex);
			npc.money -= choice.price;
			budget -= choice.price;
			choice.buy(npc);
			if (Global.isDebugOn(DebugFlags.DEBUG_PLANNING) && !choice.choice.contains("none")) {
				System.out.println(npc.name() + " purchased " + choice.choice);
			}
		}
	}
}
