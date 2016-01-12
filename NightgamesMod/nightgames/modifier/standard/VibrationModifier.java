package nightgames.modifier.standard;

import nightgames.global.Global;
import nightgames.modifier.BaseModifier;

public class VibrationModifier extends BaseModifier {

	public VibrationModifier() {
		custom = (c, m) -> c.tempt(5);
	}

	@Override
	public int bonus() {
		return 75;
	}

	@Override
	public String name() {
		return "vibration";
	}

	@Override
	public String intro() {
		if (Global.getPlayer().hasDick()) {
			return "<i>\"Do you like toys, " + Global.getPlayer().name()
					+ "? I thought of a way to make your matches harder that you'll still enjoy.\"</i> She holds up a small plastic ring. "
					+ "<i>\"Vibrating cock-ring,\"</i> she explains. <i>\"I'll give you a $" + bonus()
					+ " for each fight you win while this little fellow keeps you horny and ready to " + "burst.\"</i>";
		} else if (Global.getPlayer().hasPussy()) {
			return "<i>\"Do you like toys, " + Global.getPlayer().name()
					+ "? I thought of a way to make your matches harder that you'll still enjoy.\"</i> She holds up a small plastic ring. "
					+ "<i>\"Vibrating clit-ring,\"</i> she explains. <i>\"I'll give you a $" + bonus()
					+ " for each fight you win while this little fellow keeps you horny and ready to " + "burst.\"</i>";
		}
		return null;
	}

	@Override
	public String acceptance() {
		if (Global.getPlayer().hasDick()) {
			return "You agree to Lilly's rule and reach out to take the cock-ring, but she shakes her head. <i>\"I need to put it on you to make sure it's positioned correctly. Don't worry, "
					+ "you don't need to undress.\"</i> She steps close to you and slips her hand down the front of your pants and underwear. Her fingers dexterously manipulate your dick as she "
					+ "manuevers the ring onto it. From her expression, it looks like she's concentrating on her task rather than trying to entice you, but her closeness and her touch still "
					+ "have an effect on you. <i>\"Don't feel embarrassed,\"</i> she says with a reassuring smile. <i>\"It's actually easier to put this on when you're a little hard.\"</i> She fits the "
					+ "cock-ring in place and removes her hand. <i>\"Time to test.\"</i> She holds up a small remote and switches it on. Your hips jerk at the intense sensation on your cock. You "
					+ "have to endure this for three hours? <i>\"The intensity will automatically modulate to keep you from going numb, but after a few minutes, you'll partially adapt to it. "
					+ "I'll hang onto the remote during the match.\"</i> She hits the button again and the vibration stops. <i>\"If this ends up making you cum, I won't be offended if you think "
					+ "about me.\"</i>";
		} else if (Global.getPlayer().hasPussy()) {
			return "You agree to Lilly's rule and reach out to take the clit-ring, but she shakes her head. <i>\"I need to put it on you to make sure it's positioned correctly. Don't worry, "
					+ "you don't need to undress.\"</i> She steps close to you and slips her hand down the front of your waistband and underwear. Her fingers dexterously teases your nub as she "
					+ "manuevers the ring onto it. From her expression, it looks like she's concentrating on her task rather than trying to entice you, but her closeness and her touch still "
					+ "have an effect on you. <i>\"Don't feel embarrassed,\"</i> she says with a reassuring smile. <i>\"I have to get it a bit hard a bit before putting it on.\"</i> She fits the "
					+ "clit-ring in place and removes her hand. <i>\"Time to test.\"</i> She holds up a small remote and switches it on. Your hips jerk at the intense sensation on your sensitive nub. You "
					+ "have to endure this for three hours? <i>\"The intensity will automatically modulate to keep you from going numb, but after a few minutes, you'll partially adapt to it. "
					+ "I'll hang onto the remote during the match.\"</i> She hits the button again and the vibration stops. <i>\"If this ends up making you cum, I won't be offended if you think "
					+ "about me.\"</i>";
		}
		return null;
	}

	@Override
	public boolean isApplicable() {
		return Global.getPlayer().hasDick() || Global.getPlayer().hasPussy();
	}
}
