package nightgames.gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import nightgames.combat.Combat;
import nightgames.global.Global;
import nightgames.skills.Skill;
import nightgames.skills.Tactics;


public class SkillButton extends JPanel {
	protected Skill action;
	protected Combat combat;
	private JButton button;
	private int tint(int s, int t, float a) {
		return Math.max(Math.min(Math.round(s + (t - s) * a), 255), 0);
	}
	private Color tint(Color source, Color tint, float alpha)
	{
		return new Color(tint(source.getRed(), tint.getRed(), alpha),
				tint(source.getGreen(), tint.getGreen(), alpha),
				tint(source.getBlue(), tint.getBlue(), alpha));
	}
	
	public SkillButton(final Skill action, Combat c){
		super();
		button = new JButton(action.getLabel(c));
		button.setBorderPainted(false);
		button.setOpaque(true);
		button.setFont(new Font("Baskerville Old Face", Font.PLAIN, 18));
		this.action=action;
		String text = "<html>" + action.describe(c);
		if(action.type(c)==Tactics.damage){
			button.setBackground(new Color(150,0,0));
			button.setForeground(Color.WHITE);
		}
		else if(action.type(c)==Tactics.pleasure){
			button.setBackground(Color.PINK);
		}
		else if(action.type(c)==Tactics.fucking){
			button.setBackground(new Color(255,100,200));
		}
		else if(action.type(c)==Tactics.positioning){
			button.setBackground(new Color(0,100,0));
			button.setForeground(Color.WHITE);
		}
		else if(action.type(c)==Tactics.stripping){
			button.setBackground(new Color(0,100,0));
			button.setForeground(Color.WHITE);
		}
		else if(action.type(c)==Tactics.debuff){
			button.setBackground(Color.CYAN);
		}
		else if(action.type(c)==Tactics.recovery||action.type(c)==Tactics.calming){
			button.setBackground(Color.WHITE);
		}
		else if(action.type(c)==Tactics.summoning){
			button.setBackground(Color.YELLOW);
		}
		else{
			button.setBackground(new Color(200,200,200));
		}

		if (action.getMojoCost(c) > 0) {
			setBorder(new LineBorder(Color.RED, 3));
			text += "<br>Mojo cost: " + action.getMojoCost(c);
		} else if (action.getMojoBuilt(c) > 0){
			setBorder(new LineBorder(new Color(53,201,255), 3));
			text += "<br>Mojo generated: " + action.getMojoBuilt(c) + "%";
		} else {
			setBorder(new LineBorder(button.getBackground(), 3));
		}
		if (!action.user().cooldownAvailable(action)) {
			button.setEnabled(false);
			text += String.format("<br>Remaining Cooldown: %d turns", action.user().getCooldown(action));
			button.setForeground(Color.WHITE);
			button.setBackground(getBackground().darker());
		}

		text += "</html>";
		button.setToolTipText(text);
		this.combat=c;
		button.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (action.subChoices().size() == 0) {
					SkillButton.this.combat.act(SkillButton.this.action.user(), SkillButton.this.action, "");
				} else {
					Global.gui().commandPanel.removeAll();
					for (String choice : action.subChoices()) {
						Global.gui().commandPanel.add(new SubSkillButton(action, choice, SkillButton.this.combat));
					}
					Global.gui().commandPanel.repaint();
					Global.gui().commandPanel.revalidate();
				}
			}
		});
		setLayout(new BorderLayout());
		add(button);
	}
}
