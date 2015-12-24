package nightgames.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import nightgames.actions.Action;
import nightgames.characters.Character;
import nightgames.global.Global;

public class ActionButton extends JButton{
	protected Action action;
	protected Character user;
	public ActionButton(Action action,Character user){
		super(action.toString());
		setFont(new Font("Baskerville Old Face", Font.PLAIN, 18));
		this.action=action;
		this.user=user;
		addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Global.gui().clearText();
				ActionButton.this.action.execute(ActionButton.this.user);
				if(!ActionButton.this.action.freeAction())
					Global.getMatch().resume();
			}			
		});
	}
}
