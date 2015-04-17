package areas;
import characters.Character;

public interface Deployable {
	public void resolve(Character active);
	public Character owner();
}
