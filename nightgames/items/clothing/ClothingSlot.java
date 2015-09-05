package nightgames.items.clothing;

public enum ClothingSlot {
	top(5),
	bottom(5),
	legs(3),
	feet(1),
	arms(1),
	head(2);
	private double exposureWeight;
	ClothingSlot(double exposureWeight) {
		this.exposureWeight = exposureWeight;
	}
	
	public double getExposureWeight() {return exposureWeight;}
}

