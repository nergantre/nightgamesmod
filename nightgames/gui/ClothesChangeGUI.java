package nightgames.gui;
import nightgames.characters.Character;
import nightgames.daytime.Activity;
import nightgames.global.Global;
import nightgames.global.Modifier;
import nightgames.items.clothing.Clothing;

import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.Box;

import java.awt.Font;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClothesChangeGUI extends JPanel {
	private Character player;
	private Activity resume;
	private ArrayList<Clothing> TopOut;
	private ArrayList<Clothing> TopMid;
	private ArrayList<Clothing> TopIn;
	private ArrayList<Clothing> BotOut;
	private ArrayList<Clothing> BotIn;
	private String noneString = "";
	private JComboBox TOBox;
	private JComboBox TMBox;
	private JComboBox TIBox;
	private JComboBox BOBox;
	private JComboBox BIBox;
	
	public ClothesChangeGUI(Character player, Activity event){
		this.player = player;
		this.resume = event;
		setLayout(new GridLayout(0, 1, 0, 0));
		
		TopOut = new ArrayList<Clothing>();
		TopMid = new ArrayList<Clothing>();
		TopIn = new ArrayList<Clothing>();
		BotOut = new ArrayList<Clothing>();
		BotIn = new ArrayList<Clothing>();
		
		for(Clothing article: player.closet){
			if (!player.hasPussy() && Clothing.femaleOnlyClothing.contains(article)) {
				continue;
			}
		}
		
		JSeparator separator_1 = new JSeparator();
		add(separator_1);
		
		JLabel lblTop = new JLabel("Top");
		lblTop.setHorizontalAlignment(SwingConstants.CENTER);
		lblTop.setFont(new Font("Sylfaen", Font.PLAIN, 20));
		add(lblTop);
		
		TOBox = new JComboBox(TopOut.toArray());
		TOBox.setFont(new Font("Sylfaen", Font.PLAIN, 22));
		add(TOBox);
		TOBox.addItem(noneString);
		TOBox.setSelectedItem(noneString);
		
		TMBox = new JComboBox(TopMid.toArray());
		TMBox.setFont(new Font("Sylfaen", Font.PLAIN, 22));
		add(TMBox);
		TMBox.addItem(noneString);
		TMBox.setSelectedItem(noneString);
		
		TIBox = new JComboBox(TopIn.toArray());
		TIBox.setFont(new Font("Sylfaen", Font.PLAIN, 22));
		add(TIBox);
		TIBox.addItem(noneString);
		TIBox.setSelectedItem(noneString);
		
		JSeparator separator = new JSeparator();
		add(separator);
		
		JLabel lblBottom = new JLabel("Bottom");
		lblBottom.setHorizontalAlignment(SwingConstants.CENTER);
		lblBottom.setFont(new Font("Sylfaen", Font.PLAIN, 20));
		add(lblBottom);
		
		BOBox = new JComboBox(BotOut.toArray());
		BOBox.setFont(new Font("Sylfaen", Font.PLAIN, 22));
		add(BOBox);
		BOBox.addItem(noneString);
		BOBox.setSelectedItem(noneString);
		
		BIBox = new JComboBox(BotIn.toArray());
		BIBox.setFont(new Font("Sylfaen", Font.PLAIN, 22));
		add(BIBox);
		BIBox.addItem(noneString);
		BIBox.setSelectedItem(noneString);
		
		JSeparator separator_2 = new JSeparator();
		add(separator_2);
		
		Box horizontalBox_2 = Box.createHorizontalBox();
		add(horizontalBox_2);
		
		Component horizontalStrut = Box.createHorizontalStrut(200);
		horizontalBox_2.add(horizontalStrut);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ClothesChangeGUI.this.player.change(Modifier.normal);
				Global.gui().removeClosetGUI();
				ClothesChangeGUI.this.resume.visit("Leave");
			}
		});
		btnOk.setFont(new Font("Sylfaen", Font.PLAIN, 24));
		horizontalBox_2.add(btnOk);
		// TODO EVERYTHING!
	}
}
