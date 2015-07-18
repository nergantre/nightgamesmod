package items;

import characters.Trait;
import characters.Character;

public enum Clothing implements Loot{
	shirt		("shirt",ClothingType.TOP,5,"a ",Trait.none,Trait.none,75),
	jacket		("jacket",ClothingType.TOPOUTER,2,"a ",Trait.none,Trait.none,400),
	blouse		("blouse",ClothingType.TOP,5,"a ",Trait.none,Trait.none,60),
	bra			("bra",ClothingType.TOPUNDER,5,"a ",Trait.none,Trait.none,200),
	pants		("pants",ClothingType.BOTOUTER,15,"",Trait.none,Trait.none,100),
	skirt		("skirt",ClothingType.BOTOUTER,12,"a ",Trait.none,Trait.flexible,60),
	panties		("panties",ClothingType.UNDERWEAR,15,"",Trait.none,Trait.flexible,250),
	boxers		("boxers",ClothingType.UNDERWEAR,15,"",Trait.none,Trait.flexible,200),
	underwear	("underwear",ClothingType.UNDERWEAR,15,"",Trait.none,Trait.flexible,200),
	briefs		("briefs",ClothingType.UNDERWEAR,15,"",Trait.none,Trait.flexible,200),
	thong		("thong",ClothingType.UNDERWEAR,15,"a ",Trait.none,Trait.skimpy,400),
	tanktop		("tank top",ClothingType.TOP,0,"a ",Trait.none,Trait.none,75),
	Tshirt		("T-shirt",ClothingType.TOP,5,"a ",Trait.none,Trait.none,100),
	shorts		("shorts",ClothingType.BOTOUTER,15,"",Trait.none,Trait.flexible,100),
	jeans		("jeans",ClothingType.BOTOUTER,17,"",Trait.none,Trait.none,200),
	sweatshirt	("sweatshirt",ClothingType.TOP,5,"",Trait.none,Trait.none,50),
	sweatpants	("sweatpants",ClothingType.BOTOUTER,15,"",Trait.none,Trait.none,50),
	sweater		("sweater",ClothingType.TOP,15,"a ",Trait.lame,Trait.none,200),
	miniskirt	("miniskirt",ClothingType.BOTOUTER,12,"a ",Trait.none,Trait.flexible,400),
	strapon		("strap-on dildo",ClothingType.UNDERWEAR,10,"a ",Trait.strapped,Trait.flexible,0),
	bikinitop	("bikini top",ClothingType.TOPUNDER,0,"a ",Trait.none,Trait.skimpy,500),
	bikinibottoms	("bikini bottom",ClothingType.UNDERWEAR,5,"",Trait.none,Trait.skimpy,500),
	cloak		("cloak",ClothingType.TOPOUTER,2,"a ",Trait.mystic,Trait.none,750), 
	labcoat		("lab coat",ClothingType.TOPOUTER,3,"a ",Trait.geeky,Trait.none,750),
	gi			("gi",ClothingType.TOP,5,"a ",Trait.martial,Trait.none,500),
	kungfupants	("kung-fu pants",ClothingType.BOTOUTER,12,"",Trait.martial,Trait.none,600),
	chastitybelt("chastity belt",ClothingType.UNDERWEAR,24,"a ",Trait.armored,Trait.indestructible,0),
	cup			("Groin Protector",ClothingType.UNDERWEAR,3,"a ",Trait.armored,Trait.bulky,1000),
	undershirt	("white undershirt",ClothingType.TOPUNDER,5,"a ",Trait.lame,Trait.none,60),
	trenchcoat	("trenchcoat",ClothingType.TOPOUTER,7,"a ",Trait.none,Trait.bulky,1000),
	warpaint	("war paint",ClothingType.TOPUNDER,0,"",Trait.none,Trait.ineffective,120),
	gothshirt	("goth shirt",ClothingType.TOP,5,"a ",Trait.broody,Trait.none,700),
	gothpants	("goth pants",ClothingType.BOTOUTER,15,"",Trait.broody,Trait.none,800),
	crotchlesspanties("crotchless panties",ClothingType.UNDERWEAR,15,"",Trait.kinky,Trait.ineffective,1200),
	latextop	("latex top",ClothingType.TOPUNDER,4,"",Trait.kinky,Trait.skimpy,1000),
	latexpants	("latex pants",ClothingType.BOTOUTER,15,"",Trait.kinky,Trait.skimpy,1100),
	silkShirt	("silk shirt",ClothingType.TOP,4,"a ",Trait.stylish,Trait.none,1200),
	blazer		("blazer",ClothingType.TOPOUTER,3,"a ",Trait.none,Trait.none,750),
	dresspants	("dress pants",ClothingType.BOTOUTER,15,"",Trait.stylish,Trait.none,900),
	windbreaker	("windbreaker",ClothingType.TOPOUTER,0,"a ",Trait.none,Trait.none,500),
	slimebottom	("slime",ClothingType.UNDERWEAR,0,"",Trait.none,Trait.ineffective,1200),
	slimetop	("slime",ClothingType.TOP,0,"",Trait.none,Trait.ineffective,1200)
	;
	private String name;
	private int dc;
	private Trait buff;
	private String prefix;
	private Trait attribute;
	private int price;
	private ClothingType type;
	
	private Clothing(String name, int dc, String prefix){
		this.name=name;
		this.dc=dc;
		this.prefix=prefix;
	}
	private Clothing(String name, ClothingType type,int dc, String prefix, Trait buff, Trait attribute, int price){
		this(name,dc,prefix);
		this.buff=buff;
		this.attribute=attribute;
		this.price=price;
		this.type = type;
	}
	public String getName(){
		return name;
	}
	public int dc(){
		return dc;
	}
	public String pre(){
		return prefix;
	}
	public boolean adds(Trait test){
		return test==buff;
	}
	public Trait buff() {
		return buff;
	}
	public Trait attribute(){
		return attribute;
	}
	public ClothingType getType(){
		return this.type;
	}
	public int getPrice(){
		return this.price;
	}
	@Override
	public void pickup(Character owner) {
		if(!owner.has(this)){
			owner.gain(this);
		}
	}
}
