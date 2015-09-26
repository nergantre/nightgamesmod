package nightgames.gui;

import nightgames.characters.Attribute;
import nightgames.characters.CharacterSex;
import nightgames.characters.Player;
import nightgames.characters.Trait;
import nightgames.global.Flag;
import nightgames.global.Global;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JRadioButton;
import javax.swing.Box;
import javax.swing.JComboBox;
import java.awt.Dimension;
import java.awt.Component;

public class CreationGUI extends JPanel{
	private JTextField powerfield;
	private JTextField seductionfield;
	private JTextField cunningfield;
	private JTextField attPoints;
	private JTextField namefield;
	private int power;
	private int seduction;
	private int cunning;
	private int remaining;
	private JButton btnPowMin;
	private JButton btnPowPlus;
	private JButton btnSedMin;
	private JButton btnSedPlus;
	private JButton btnCunMin;
	private JButton btnCunPlus;
	private JSeparator separator;
	private JTextPane textPane;
	private JScrollPane scrollPane;
	private JRadioButton rdbtnNormal;
	private JRadioButton rdbtnDumb;
	private JRadioButton rdbtnEasy;
	private JCheckBox rdbtnHard;
	private JSeparator separator_1;
	private Box verticalBox;
	private Box horizontalBox;
	private JLabel lblStrength;
	private JComboBox StrengthBox;
	private JTextPane StrengthDescription;
	private JSeparator separator_2;
	private JLabel lblWeakness;
	private JComboBox WeaknessBox;
	private JTextPane WeaknessDescription;
	private JPanel panel_1;
	private JPanel panel_4;
	private JPanel panel_5;
	private JPanel panel_6;
	private JPanel panel_7;
	private JPanel panel_8;
	private JPanel panel_9;
	private JPanel panel_10;
	private JPanel panel_11;
	private JPanel panel_12;
	public CreationGUI() {
		setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);

		JLabel lblName = new JLabel("Name");
		lblName.setFont(new Font("Sylfaen", Font.BOLD, 15));
		panel.add(lblName);
		
		namefield = new JTextField();
		namefield.setFont(new Font("Sylfaen", Font.BOLD, 15));
		panel.add(namefield);
		namefield.setColumns(10);

		JComboBox<CharacterSex> sexBox = new JComboBox<>();
		Arrays.stream(CharacterSex.values()).forEach(s -> sexBox.addItem(s));
		panel.add(sexBox);
	
		separator = new JSeparator();
		panel.add(separator);

		
		JButton btnStart = new JButton("Start");
		btnStart.setFont(new Font("Sylfaen", Font.BOLD, 15));
		panel.add(btnStart);
		btnStart.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!CreationGUI.this.namefield.getText().isEmpty()){
					String name = CreationGUI.this.namefield.getText();
					CharacterSex sex = (CharacterSex) sexBox.getSelectedItem();
					Player one = new Player(name, sex);
					one.set(Attribute.Power, CreationGUI.this.power);
					one.set(Attribute.Seduction, CreationGUI.this.seduction);
					one.set(Attribute.Cunning, CreationGUI.this.cunning);
					one.add((Trait)StrengthBox.getSelectedItem());
					one.add((Trait)WeaknessBox.getSelectedItem());
					if(rdbtnDumb.isSelected()){
						Global.flag(Flag.dumbmode);
					}
					if(rdbtnHard.isSelected()){
						Global.flag(Flag.hardmode);
					}
					Global.newGame(one);
					Global.startMatch();
				}
			}
		});
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane, BorderLayout.CENTER);
		
		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		textPane.setForeground(new Color(240, 240, 255));
		textPane.setBackground(new Color(25, 25, 50));
		textPane.setFont(new Font("Baskerville Old Face", Font.PLAIN, 22));
		textPane.setEditable(false);
		textPane.setText(Global.getIntro());
		
		JPanel panel_2 = new JPanel();
		add(panel_2, BorderLayout.SOUTH);
		panel_2.setLayout(new GridLayout(0, 12, 0, 0));
		
		JLabel lblPower = new JLabel("Power");
		lblPower.setFont(new Font("Sylfaen", Font.BOLD, 15));
		panel_2.add(lblPower);
		
		powerfield = new JTextField();
		powerfield.setFont(new Font("Sylfaen", Font.BOLD, 15));
		powerfield.setEditable(false);
		panel_2.add(powerfield);
		powerfield.setColumns(10);
		
		panel_4 = new JPanel();
		panel_2.add(panel_4);
		
		JLabel lblSeduction = new JLabel("Seduction");
		lblSeduction.setFont(new Font("Sylfaen", Font.BOLD, 15));
		panel_2.add(lblSeduction);
		
		seductionfield = new JTextField();
		seductionfield.setFont(new Font("Sylfaen", Font.BOLD, 15));
		seductionfield.setEditable(false);
		panel_2.add(seductionfield);
		seductionfield.setColumns(10);
		
		panel_5 = new JPanel();
		panel_2.add(panel_5);
		
		JLabel lblCunning = new JLabel("Cunning");
		lblCunning.setFont(new Font("Sylfaen", Font.BOLD, 15));
		panel_2.add(lblCunning);
		
		cunningfield = new JTextField();
		cunningfield.setFont(new Font("Sylfaen", Font.BOLD, 15));
		cunningfield.setEditable(false);
		panel_2.add(cunningfield);
		cunningfield.setColumns(10);
		
		panel_6 = new JPanel();
		panel_2.add(panel_6);
		
		horizontalBox = Box.createHorizontalBox();
		panel_2.add(horizontalBox);
		horizontalBox.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		
		JLabel lblAttributePoints = new JLabel("Remaining");
		horizontalBox.add(lblAttributePoints);
		lblAttributePoints.setFont(new Font("Sylfaen", Font.PLAIN, 15));
		
		attPoints = new JTextField();
		horizontalBox.add(attPoints);
		attPoints.setFont(new Font("Sylfaen", Font.PLAIN, 15));
		attPoints.setEditable(false);
		attPoints.setColumns(2);
		
		panel_7 = new JPanel();
		horizontalBox.add(panel_7);
		
		panel_11 = new JPanel();
		panel_2.add(panel_11);
		
		rdbtnNormal = new JRadioButton("Normal");
		rdbtnNormal.setSelected(true);
		panel_2.add(rdbtnNormal);
		
		btnPowMin = new JButton("-");
		btnPowMin.setFont(new Font("Sylfaen", Font.BOLD, 15));
		panel_2.add(btnPowMin);
		btnPowMin.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				power--;
				remaining++;
				refresh();
			}
		});
		
		btnPowPlus = new JButton("+");
		btnPowPlus.setFont(new Font("Sylfaen", Font.BOLD, 15));
		panel_2.add(btnPowPlus);
		btnPowPlus.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				power++;
				remaining--;
				refresh();
			}
		});
		
		panel_8 = new JPanel();
		panel_2.add(panel_8);
		
		btnSedMin = new JButton("-");
		btnSedMin.setFont(new Font("Sylfaen", Font.BOLD, 15));
		panel_2.add(btnSedMin);
		btnSedMin.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				seduction--;
				remaining++;
				refresh();
			}
		});
		
		btnSedPlus = new JButton("+");
		btnSedPlus.setFont(new Font("Sylfaen", Font.BOLD, 15));
		panel_2.add(btnSedPlus);
		btnSedPlus.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				seduction++;
				remaining--;
				refresh();
			}
		});
		
		panel_9 = new JPanel();
		panel_2.add(panel_9);
		
		btnCunMin = new JButton("-");
		btnCunMin.setFont(new Font("Sylfaen", Font.BOLD, 15));
		panel_2.add(btnCunMin);
		btnCunMin.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cunning--;
				remaining++;
				refresh();
			}
		});
		
		btnCunPlus = new JButton("+");
		btnCunPlus.setFont(new Font("Sylfaen", Font.BOLD, 15));
		panel_2.add(btnCunPlus);
		
		panel_10 = new JPanel();
		panel_2.add(panel_10);
		
		panel_1 = new JPanel();
		panel_2.add(panel_1);
		
		panel_12 = new JPanel();
		panel_2.add(panel_12);
		
		rdbtnDumb = new JRadioButton("Old AI");
		panel_2.add(rdbtnDumb);
		for (int i = 0; i < 11; i++) {
			panel_2.add(new JPanel());
		}

		rdbtnHard = new JCheckBox("Hard Mode");
		panel_2.add(rdbtnHard);
		btnCunPlus.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				cunning++;
				remaining--;
				refresh();
			}
		});
		ButtonGroup difficulty = new ButtonGroup();
		difficulty.add(rdbtnNormal);
		difficulty.add(rdbtnDumb);
		JPanel panel_3 = new JPanel();
		panel_3.setMinimumSize(new Dimension(100, 10));
		panel_3.setMaximumSize(new Dimension(100, 32767));
		add(panel_3, BorderLayout.EAST);
		panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		verticalBox = Box.createVerticalBox();
		panel_3.add(verticalBox);
		
		lblStrength = new JLabel("Strength");
		verticalBox.add(lblStrength);
		
		StrengthBox = new JComboBox();
		StrengthBox.addItem(Trait.exhibitionist);
		StrengthBox.addItem(Trait.romantic);
		StrengthBox.addItem(Trait.dexterous);
		StrengthBox.addItem(Trait.experienced);
		StrengthBox.addItem(Trait.wrassler);
		StrengthBox.addItem(Trait.pimphand);
		StrengthBox.addItem(Trait.stableform);
		StrengthBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				StrengthDescription.setText(((Trait)StrengthBox.getSelectedItem()).getDesc());
			}
		});
		verticalBox.add(StrengthBox);
		
		StrengthDescription = new JTextPane();
		StrengthDescription.setPreferredSize(new Dimension(100, 100));
		StrengthDescription.setEditable(false);
		StrengthDescription.setForeground(Color.black);
		StrengthDescription.setText(((Trait)StrengthBox.getSelectedItem()).getDesc());
		verticalBox.add(StrengthDescription);
		
		separator_2 = new JSeparator();
		verticalBox.add(separator_2);
		
		lblWeakness = new JLabel("Weakness");
		verticalBox.add(lblWeakness);
		
		WeaknessBox = new JComboBox();
		WeaknessBox.addItem(Trait.insatiable);
		WeaknessBox.addItem(Trait.imagination);
		WeaknessBox.addItem(Trait.achilles);
		WeaknessBox.addItem(Trait.ticklish);
		WeaknessBox.addItem(Trait.lickable);
		WeaknessBox.addItem(Trait.naive);
		WeaknessBox.addItem(Trait.footfetishist);
		WeaknessBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				WeaknessDescription.setText(((Trait)WeaknessBox.getSelectedItem()).getDesc());
			}
		});
		verticalBox.add(WeaknessBox);
		
		WeaknessDescription = new JTextPane();
		WeaknessDescription.setPreferredSize(new Dimension(100, 100));
		WeaknessDescription.setEditable(false);
		WeaknessDescription.setForeground(Color.black);
		WeaknessDescription.setText(((Trait)WeaknessBox.getSelectedItem()).getDesc());
		verticalBox.add(WeaknessDescription);
		
		separator_1 = new JSeparator();
		verticalBox.add(separator_1);
		power = 3;
		seduction = 3;
		cunning = 3;
		remaining = 20 - power - seduction - cunning;
		refresh();
	}
	private void refresh(){
		powerfield.setText(""+power);
		seductionfield.setText(""+seduction);
		cunningfield.setText(""+cunning);
		attPoints.setText(""+remaining);
		if(remaining <= 0){
			btnPowPlus.setEnabled(false);
			btnSedPlus.setEnabled(false);
			btnCunPlus.setEnabled(false);
		}
		else{
			btnPowPlus.setEnabled(true);
			btnSedPlus.setEnabled(true);
			btnCunPlus.setEnabled(true);
		}
		if(power <= 1){
			btnPowMin.setEnabled(false);
		}
		else{
			btnPowMin.setEnabled(true);
		}
		if(seduction <= 1){
			btnSedMin.setEnabled(false);
		}
		else{
			btnSedMin.setEnabled(true);
		}
		if(cunning <= 1){
			btnCunMin.setEnabled(false);
		}
		else{
			btnCunMin.setEnabled(true);
		}
	}

}
