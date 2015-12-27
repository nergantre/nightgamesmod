package nightgames.actions;

public enum Movement {
	quad(" head outside, toward the quad."),
	kitchen(" move into the kitchen."),
	dorm(" move to the first floor of the dorm."),
	shower(" run into the showers."),
	storage(" enter the storage room."),
	dining(" head to the dining hall."),
	laundry(" move to the laundry room."),
	tunnel(" move into the tunnel."),
	bridge(" move to the bridge."),
	engineering(" head to the first floor of the engineering building."),
	workshop(" enter a workshop."),
	lab(" enter one of the chemistry labs."),
	la(" move to the liberal arts building."),
	library(" enter the library."),
	pool(" move to the indoor pool."),
	union(" head toward the student union."),
	courtyard(" head toward the courtyard."),
	hide(" disappear into a hiding place."),
	trap(" start rigging up something weird, probably a trap."),
	bathe(" start bathing in the nude, not bothered by your presence."),
	scavenge(" begin scrounging through some boxes in the corner."),
	craft(" start mixing various liquids. Whatever it is doesn't look healthy."),
	wait(" loitering nearby"),
	resupply(
			" heads for one of the safe rooms, probably to get a change of clothes."),
	oil(" rubbing body oil on her every inch of her skin. Wow, you wouldn't mind watching that again."),
	enerydrink(" opening an energy drink and downing the whole thing."),
	beer(" opening a beer and downing the whole thing."),
	recharge(" plugging a battery pack into a nearby charging station."),
	locating(
			" is holding someone's underwear in her hands and breathing deeply. Strange."),
	masturbate(
			" starts to pleasure herself, while trying not to make much noise. It's quite a show."),
	mana(" doing something with a large book. When she's finished, you can see a sort of aura coming from her."),
	retire(" has left the match."), 
	ftcNorthBase(" head to the north camp."), 
	ftcWestBase(" move to the west camp."), 
	ftcSouthBase(" go to the south camp."), 
	ftcEastBase(" walk to the east camp."),
	ftcCenter(" head to the central clearing."),
	ftcPond(" wade through the bushes to the pool."),
	ftcGlade(" head into the shaded glade."),
	ftcCabin(" walk into the cabin."),
	ftcTrail(" move to the trail."),
	ftcLodge(" head into the lodge."),
	ftcHill(" climb up the small hill."),
	ftcPath(" head down the path."),
	ftcOak(" move towards the tall oak."),
	ftcPass(" head into the narrow pass."),
	ftcWaterfall(" head to the waterfall."),
	ftcMonument(" go to the stone monument."),
	ftcDump(" walk to the dumpsite."),
	ftcTreeAmbush(" climb up a tree."),
	ftcBushAmbush(" dive into some bushes."),
	ftcPassAmbush(" slip into an alcove.");

	private String desc;

	/**
	 * @return the Item name
	 */
	public String describe() {
		return desc;
	}

	private Movement(String desc) {
		this.desc = desc;
	}
}
