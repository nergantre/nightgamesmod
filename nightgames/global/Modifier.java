package nightgames.global;

public enum Modifier {
	normal	(0),		//No challenge
	underwearonly (100),		//Only get boxers when resupplying
	nudist	(125),		//No clothes
	norecovery (100),	//Arousal only empties on loss or draw, no masturbation
	vibration (75), 	//Small arousal gain in upkeep
	vulnerable (75),	//Permanent Hypersensitive effect
	pacifist (100),		//Unable to use pain attacks
	notoys (50),		//Unable to use toys
	noitems (50),		//Unable to use consumables
	ftc (0),			//Fuck the Carrier
	;
	private int bonus;
	private Modifier(int bonus){
		this.bonus=bonus;
	}
	public int bonus(){
		return this.bonus;
	}
}
