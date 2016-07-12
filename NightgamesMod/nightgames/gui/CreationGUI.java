package nightgames.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.*;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import nightgames.characters.Attribute;
import nightgames.characters.CharacterSex;
import nightgames.characters.Trait;
import nightgames.global.Flag;
import nightgames.global.Global;
import nightgames.start.StartConfiguration;

public class CreationGUI extends JPanel {
    /**
     * 
     */
    private static final long serialVersionUID = -101675245609325067L;
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
    @SuppressWarnings("unused")
    private JRadioButton rdbtnEasy;
    private JCheckBox rdbtnHard;
    private JSeparator separator_1;
    private Box verticalBox;
    private Box horizontalBox;
    private JLabel lblStrength;
    private JComboBox<Trait> StrengthBox;
    private JTextPane StrengthDescription;
    private JSeparator separator_2;
    private JLabel lblWeakness;
    private JComboBox<Trait> WeaknessBox;
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
    private JPanel topPanel;
    private JComboBox<CharacterSex> sexBox;
    private JComboBox<StartConfiguration> configs;
    private JButton btnStart;
    private JButton btnAdvStart;

    public CreationGUI() {
        setLayout(new BorderLayout(0, 0));

        topPanel = new JPanel();
        add(topPanel, BorderLayout.NORTH);
        topPanel.setBackground(new Color(0, 10, 30));

        JLabel lblName = new JLabel("Name");

        lblName.setForeground(new Color(240, 240, 255));
        lblName.setFont(new Font("Sylfaen", Font.BOLD, 15));
        topPanel.add(lblName);

        namefield = new JTextField();
        namefield.setFont(new Font("Sylfaen", Font.BOLD, 15));
        topPanel.add(namefield);
        namefield.setColumns(10);

        sexBox = new JComboBox<>();
        Arrays.stream(CharacterSex.values())
              .forEach(s -> sexBox.addItem(s));
        topPanel.add(sexBox);

        separator = new JSeparator();
        topPanel.add(separator);

        btnStart = new JButton("Start");
        btnStart.setFont(new Font("Verdana", Font.BOLD, 12));
        topPanel.add(btnStart);
        btnStart.addActionListener(e -> makeGame());

        btnAdvStart = new JButton("Advanced Start");
        btnAdvStart.setFont(new Font("Verdana", Font.BOLD, 12));
        topPanel.add(btnAdvStart);
        btnAdvStart.addActionListener(this::advancedStart);

        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        textPane = new JTextPane();
        scrollPane.setViewportView(textPane);
        textPane.setForeground(new Color(240, 240, 255));

        textPane.setBackground(new Color(18, 30, 49));
        textPane.setFont(new Font("Baskerville Old Face", Font.PLAIN, 22));
        textPane.setEditable(false);
        textPane.setText(Global.getIntro());

        JPanel panel_2 = new JPanel();
        add(panel_2, BorderLayout.SOUTH);
        panel_2.setLayout(new GridLayout(0, 12, 0, 0));
        panel_2.setForeground(new Color(240, 240, 255));
        panel_2.setBackground(new Color(19, 30, 49));

        JLabel lblPower = new JLabel("Power");
        lblPower.setForeground(new Color(240, 240, 255));
        lblPower.setFont(new Font("Sylfaen", Font.BOLD, 15));
        panel_2.add(lblPower);


        powerfield = new JTextField();
        powerfield.setFont(new Font("Verdana", Font.BOLD, 15));
        powerfield.setEditable(false);
        powerfield.setBackground(new Color(18, 30, 49));
        powerfield.setForeground(new Color(240, 240, 255));
        panel_2.add(powerfield);
        powerfield.setColumns(10);

        panel_4 = new JPanel();
        panel_4.setBackground(new Color(0, 10, 30));
        panel_2.add(panel_4);

        JLabel lblSeduction = new JLabel("Seduction");
        lblSeduction.setFont(new Font("Verdana", Font.BOLD, 15));
        panel_2.add(lblSeduction);

        seductionfield = new JTextField();
        seductionfield.setFont(new Font("Verdana", Font.BOLD, 15));
        seductionfield.setEditable(false);
        seductionfield.setBackground(new Color(18, 30, 49));
        seductionfield.setForeground(new Color(240, 240, 255));
        panel_2.add(seductionfield);
        seductionfield.setColumns(10);

        panel_5 = new JPanel();
        panel_5.setBackground(new Color(0, 10, 30));
        panel_2.add(panel_5);

        JLabel lblCunning = new JLabel("Cunning");
        lblCunning.setFont(new Font("Verdana", Font.BOLD, 15));
        panel_2.add(lblCunning);

        cunningfield = new JTextField();
        cunningfield.setFont(new Font("Verdana", Font.BOLD, 15));
        cunningfield.setEditable(false);
        cunningfield.setBackground(new Color(18, 30, 49));
        cunningfield.setForeground(new Color(240, 240, 255));
        panel_2.add(cunningfield);
        cunningfield.setColumns(10);

        panel_6 = new JPanel();
        panel_6.setBackground(new Color(0, 10, 30));
        panel_2.add(panel_6);

        horizontalBox = Box.createHorizontalBox();
        panel_2.add(horizontalBox);
        horizontalBox.setAlignmentY(Component.BOTTOM_ALIGNMENT);

        JLabel lblAttributePoints = new JLabel("Remaining");
        horizontalBox.add(lblAttributePoints);
        lblAttributePoints.setFont(new Font("Sylfaen", Font.PLAIN, 15));

        attPoints = new JTextField();
        horizontalBox.add(attPoints);
        attPoints.setFont(new Font("Verdana", Font.PLAIN, 15));
        attPoints.setEditable(false);
        attPoints.setBackground(new Color(18, 30, 49));
        attPoints.setForeground(new Color(240, 240, 255));
        attPoints.setColumns(2);

        panel_7 = new JPanel();
        horizontalBox.add(panel_7);

        panel_11 = new JPanel();
        panel_2.add(panel_11);
        panel_11.setBackground(new Color(0, 10, 30));

        rdbtnNormal = new JRadioButton("Normal");
        rdbtnNormal.setSelected(true);
        rdbtnNormal.setForeground(new Color(240, 240, 255));
        rdbtnNormal.setBackground(new Color(0, 10, 30));
        panel_2.add(rdbtnNormal);

        btnPowMin = new JButton("-");
        btnPowMin.setFont(new Font("Sylfaen", Font.BOLD, 15));
        btnPowMin.setForeground(new Color(240, 240, 255));
        btnPowMin.setBackground(new Color(85, 98, 112));
        panel_2.add(btnPowMin);
        btnPowMin.addActionListener(arg0 -> {
            power--;
            remaining++;
            refresh();
        });

        btnPowPlus = new JButton("+");
        btnPowPlus.setFont(new Font("Sylfaen", Font.BOLD, 15));
        btnPowPlus.setForeground(new Color(240, 240, 255));
        btnPowPlus.setBackground(new Color(85, 98, 112));
        panel_2.add(btnPowPlus);
        btnPowPlus.addActionListener(arg0 -> {
            power++;
            remaining--;
            refresh();
        });

        panel_8 = new JPanel();
        panel_2.add(panel_8);
        panel_8.setBackground(new Color(0, 10, 30));

        btnSedMin = new JButton("-");
        btnSedMin.setFont(new Font("Sylfaen", Font.BOLD, 15));
        btnSedMin.setForeground(new Color(240, 240, 255));
        btnSedMin.setBackground(new Color(85, 98, 112));
        panel_2.add(btnSedMin);
        btnSedMin.addActionListener(arg0 -> {
            seduction--;
            remaining++;
            refresh();
        });

        btnSedPlus = new JButton("+");
        btnSedPlus.setFont(new Font("Sylfaen", Font.BOLD, 15));
        btnSedPlus.setForeground(new Color(240, 240, 255));
        btnSedPlus.setBackground(new Color(85, 98, 112));
        panel_2.add(btnSedPlus);
        btnSedPlus.addActionListener(arg0 -> {
            seduction++;
            remaining--;
            refresh();
        });

        panel_9 = new JPanel();
        panel_2.add(panel_9);
        panel_9.setBackground(new Color(0, 10, 30));

        btnCunMin = new JButton("-");
        btnCunMin.setFont(new Font("Sylfaen", Font.BOLD, 15));
        btnCunMin.setForeground(new Color(240, 240, 255));
        btnCunMin.setBackground(new Color(85, 98, 112));
        panel_2.add(btnCunMin);
        btnCunMin.addActionListener(arg0 -> {
            cunning--;
            remaining++;
            refresh();
        });

        btnCunPlus = new JButton("+");
        btnCunPlus.setFont(new Font("Sylfaen", Font.BOLD, 15));
        btnCunPlus.setForeground(new Color(240, 240, 255));
        btnCunPlus.setBackground(new Color(85, 98, 112));
        panel_2.add(btnCunPlus);

        panel_10 = new JPanel();
        panel_2.add(panel_10);
        panel_10.setBackground(new Color(0, 10, 30));

        panel_1 = new JPanel();
        panel_2.add(panel_1);
        panel_1.setBackground(new Color(0, 10, 30));

        panel_12 = new JPanel();
        panel_2.add(panel_12);
        panel_12.setBackground(new Color(0, 10, 30));

        rdbtnDumb = new JRadioButton("Easy Mode");
        rdbtnDumb.setForeground(new Color(240, 240, 240));
        rdbtnDumb.setBackground(new Color(0, 10, 30));
        panel_2.add(rdbtnDumb);
        for (int i = 0; i < 11; i++) {
            panel_2.add(new JPanel());
        }

        rdbtnHard = new JCheckBox("NPC Bonuses");
        rdbtnHard.setForeground(new Color(240, 240, 255));
        rdbtnHard.setBackground(new Color(0, 10, 30));
        panel_2.add(rdbtnHard);
        btnCunPlus.addActionListener(arg0 -> {
            cunning++;
            remaining--;
            refresh();
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

        panel_3.setBackground(new Color(0, 10, 30));

        lblStrength = new JLabel("Strength");

        lblStrength.setBackground(new Color(0, 10, 30));
        lblStrength.setForeground(new Color(240, 240, 255));
        verticalBox.add(lblStrength);

        StrengthBox = new JComboBox<Trait>();
        StrengthBox.setBackground(new Color(0, 10, 30));
        StrengthBox.setForeground(new Color(240, 240, 255));
        StrengthBox.addItem(Trait.exhibitionist);
        StrengthBox.addItem(Trait.romantic);
        StrengthBox.addItem(Trait.dexterous);
        StrengthBox.addItem(Trait.experienced);
        StrengthBox.addItem(Trait.wrassler);
        StrengthBox.addItem(Trait.pimphand);
        StrengthBox.addItem(Trait.stableform);
        StrengthBox.addItem(Trait.brassballs);
        StrengthBox.addItem(Trait.bramaster);
        StrengthBox.addItem(Trait.pantymaster);
        StrengthBox.addActionListener(
                        arg0 -> StrengthDescription.setText(((Trait) StrengthBox.getSelectedItem()).getDesc()));
        verticalBox.add(StrengthBox);

        StrengthDescription = new JTextPane();
        StrengthDescription.setPreferredSize(new Dimension(100, 100));
        StrengthDescription.setEditable(false);
        StrengthDescription.setBackground(new Color(18, 30, 49));
        StrengthDescription.setForeground(new Color(240, 240, 255));
        StrengthDescription.setText(((Trait) StrengthBox.getSelectedItem()).getDesc());
        verticalBox.add(StrengthDescription);

        separator_2 = new JSeparator();
        verticalBox.add(separator_2);

        lblWeakness = new JLabel("Weakness");

        lblWeakness.setBackground(new Color(0, 10, 30));
        lblWeakness.setForeground(new Color(240, 240, 255));
        verticalBox.add(lblWeakness);

        WeaknessBox = new JComboBox<Trait>();
        WeaknessBox.setBackground(new Color(0, 10, 30));
        WeaknessBox.setForeground(new Color(240, 240, 255));
        WeaknessBox.addItem(Trait.insatiable);
        WeaknessBox.addItem(Trait.imagination);
        WeaknessBox.addItem(Trait.achilles);
        WeaknessBox.addItem(Trait.ticklish);
        WeaknessBox.addItem(Trait.lickable);
        WeaknessBox.addItem(Trait.naive);
        WeaknessBox.addItem(Trait.footfetishist);
        WeaknessBox.addItem(Trait.hairtrigger);
        WeaknessBox.addActionListener(
                        arg0 -> WeaknessDescription.setText(((Trait) WeaknessBox.getSelectedItem()).getDesc()));
        verticalBox.add(WeaknessBox);

        WeaknessDescription = new JTextPane();
        WeaknessDescription.setBackground(new Color(18, 30, 49));
        WeaknessDescription.setForeground(new Color(240, 240, 255));
        WeaknessDescription.setPreferredSize(new Dimension(100, 100));
        WeaknessDescription.setEditable(false);
        WeaknessDescription.setText(((Trait) WeaknessBox.getSelectedItem()).getDesc());
        verticalBox.add(WeaknessDescription);

        separator_1 = new JSeparator();
        verticalBox.add(separator_1);
        power = 3;
        seduction = 3;
        cunning = 3;
        remaining = 20 - power - seduction - cunning;
        refresh();

    }

    private void advancedStart(ActionEvent evt) {
        Collection<StartConfiguration> starts = StartConfiguration.loadConfigurations();
        configs = new JComboBox<>();
        StringBuilder sb = new StringBuilder();
        starts.stream()
              .filter(StartConfiguration::isEnabled)
              .forEach(cfg -> {
                  sb.append(cfg.getName());
                  sb.append("\n");
                  sb.append(cfg.getSummary());
                  sb.append("\n\n");
                  configs.addItem(cfg);
              });
        StartConfiguration firstCfg;
        Optional<StartConfiguration> def = starts.stream()
                                                 .filter(s -> s.getName()
                                                               .equals("Default"))
                                                 .findAny();
        if (def.isPresent()) {
            firstCfg = def.get();
            configs.setSelectedItem(firstCfg);
        } else {
            firstCfg = (StartConfiguration) configs.getSelectedItem();
        }
        if (firstCfg != null) {
            setupConfig(firstCfg);
        }
        topPanel.remove(btnAdvStart);
        topPanel.add(configs);
        topPanel.revalidate();
        textPane.setText(sb.toString());
        configs.addItemListener(e -> setupConfig((StartConfiguration) e.getItem()));
        Arrays.stream(btnStart.getActionListeners())
              .forEach(btnStart::removeActionListener);
        btnStart.addActionListener(e -> makeGame(Optional.of((StartConfiguration) configs.getSelectedItem())));
    }

    private void makeGame() {
        makeGame(Optional.empty());
    }

    private boolean playerCanChooseTraits(Optional<StartConfiguration> startConfig) {
        boolean allowed = true;
        if (startConfig.isPresent()) {
            allowed = startConfig.get().playerCanChooseTraits();
        }
        return allowed;
    }


    private void makeGame(Optional<StartConfiguration> startConfig) {
        if (!namefield.getText()
                        .isEmpty()) {
            String name = namefield.getText();
            CharacterSex sex = (CharacterSex) sexBox.getSelectedItem();
            List<Trait> traits = Collections.emptyList();
            if (playerCanChooseTraits(startConfig)) {
                traits = Arrays.asList((Trait) StrengthBox.getSelectedItem(), (Trait) WeaknessBox.getSelectedItem());
            }
            if (rdbtnDumb.isSelected()) {
                Global.flag(Flag.dumbmode);
            }
            if (rdbtnHard.isSelected()) {
                Global.flag(Flag.hardmode);
            }
            Map<Attribute, Integer> selectedAttributes = new HashMap<Attribute, Integer>();
            selectedAttributes.put(Attribute.Power, power);
            selectedAttributes.put(Attribute.Seduction, seduction);
            selectedAttributes.put(Attribute.Cunning, cunning);
            Global.newGame(name, startConfig, traits, sex, selectedAttributes);
            Global.startMatch();
        }
    }

    private void setupConfig(StartConfiguration cfg) {
        sexBox.setEnabled(cfg.playerCanChooseGender());
        StrengthBox.setEnabled(cfg.playerCanChooseTraits());
        WeaknessBox.setEnabled(cfg.playerCanChooseTraits());
        if (!cfg.playerCanChooseGender()) {
            sexBox.setSelectedItem(cfg.chosenPlayerGender());
        }
        Map<Attribute, Integer> cfgAttributes = cfg.playerAttributes();
        int points = cfg.availableAttributePoints();
        power = cfgAttributes.getOrDefault(Attribute.Power, 3);
        seduction = cfgAttributes.getOrDefault(Attribute.Seduction, 3);
        cunning = cfgAttributes.getOrDefault(Attribute.Cunning, 3);
        remaining = points;
        refresh();
    }

    private void refresh() {
        powerfield.setText("" + power);
        seductionfield.setText("" + seduction);
        cunningfield.setText("" + cunning);
        attPoints.setText("" + remaining);
        if (remaining <= 0) {
            btnPowPlus.setEnabled(false);
            btnSedPlus.setEnabled(false);
            btnCunPlus.setEnabled(false);
        } else {
            btnPowPlus.setEnabled(true);
            btnSedPlus.setEnabled(true);
            btnCunPlus.setEnabled(true);
        }

        if (power <= 1) {
            btnPowMin.setEnabled(false);
        } else {
            btnPowMin.setEnabled(true);
        }
        if (seduction <= 1) {
            btnSedMin.setEnabled(false);
        } else {
            btnSedMin.setEnabled(true);
        }
        if (cunning <= 1) {
            btnCunMin.setEnabled(false);
        } else {
            btnCunMin.setEnabled(true);
        }
    }
}
