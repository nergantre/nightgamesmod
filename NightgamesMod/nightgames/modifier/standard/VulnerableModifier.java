package nightgames.modifier.standard;

import nightgames.modifier.BaseModifier;
import nightgames.modifier.StatusModifier;
import nightgames.status.Hypersensitive;

public class VulnerableModifier extends BaseModifier {

	public VulnerableModifier() {
		status = new StatusModifier(new Hypersensitive(null));
	}

	@Override
	public int bonus() {
		return 75;
	}

	@Override
	public String name() {
		return "vulnerable";
	}

	@Override
	public String intro() {
		return "<i>\"I've got a simple handicap for you tonight. You've probably come across some sensitization potions that temporarily enhance your sense of touch, right? "
				+ "There's a cream that has basically the same effect, but it'll last for several hours. The deal is that I'll rub the cream into your penis, making you much "
				+ "more vulnerable during the match, and you'll get an extra $" + bonus()
				+ " per victory. Interested?\"</i>";
	}

	@Override
	public String acceptance() {
		return "Lilly leads you into the men's bathroom to apply the hypersensitivity cream. She removes your pants and boxers and starts to rub the cream onto your dick. The stuff works "
				+ "fast and you can't help letting out a quiet moan at her soft touch. She treats the process very clinically and seems almost bored to be handling your manhood. <i>\"I hope "
				+ "you don't take my lack of interest personally,\"</i> she says, as if reading your mind. <i>\"You seem nice, and I guess you're reasonably good looking. You just happen to be "
				+ "the wrong gender.\"</i> Ah, that explains a bit. That must make this more awkward for her than it would otherwise be. She shrugs. <i>\"I don't mind. I'm used to competing against "
				+ "men, and I have some pride in my technique.\"</i> As she says this, her hand motions turn into smooth, pleasurable strokes. <i>\"Besides, with the typical gender ratio in these "
				+ "games, I'm better off than the straight girls. Are you feeling any effect yet?\"</i> You certainly are. Between the cream and her skilled handjob, you can barely stay standing. "
				+ "She continues stroking you until you shoot your load into her hands. <i>\"That was quick. I'm going to assume the cream was effective rather than you having a fetish for girls "
				+ "who aren't attracted to you.\"</i>";
	}

}
