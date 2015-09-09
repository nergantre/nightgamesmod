package nightgames.items.clothing;

public enum ClothingSlot {
	head(2),
	top(5),
	arms(1),
	bottom(5),
	legs(3),
	feet(1);
	private double exposureWeight;
	ClothingSlot(double exposureWeight) {
		this.exposureWeight = exposureWeight;
	}
	
	public double getExposureWeight() {return exposureWeight;}
}

