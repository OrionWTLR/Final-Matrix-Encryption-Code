package GUI;

import FileReader.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public interface DesignScheme {
    Color off_white = new Color(187, 187, 184,255);
    Color dark_gray = new Color(51, 49, 49);

    default JPanel openPanel(InnerWindow inner, String menuItem, String sentenceItem, ImageIcon lockIcon){
        JPanel open = new JPanel();
        JButton button = new JButton("Open File");
        button.setBackground(off_white);

        open.add(button);

        class openFromWindow implements ActionListener {
            public void actionPerformed(ActionEvent e){
                String directory = inner.chooseFile();
                int n = actionsWithFile(inner, menuItem, sentenceItem, directory, lockIcon);
                if(menuItem.equals("Encrypt")){
                    if(n == 0) {
                        inner.toEncrypt(directory, lockIcon);
                    }
                }else if(menuItem.equals("Decrypt")){
                    if(n == 0) {
                        inner.toDecrypt(directory);
                    }
                }
            }
        }
        button.addActionListener(new openFromWindow());
        return open;
    }

    default int actionsWithFile(InnerWindow inner, String menuItem, String sentenceItem, String directory, ImageIcon icon){
        JFrame frame = inner.frame();
        Parser parser = inner.parser();

        if(directory == null) return -1;
        if(directory.equals("")) return -1;
        if(directory.equals(" ")) return -1;

        String truncate = inner.readFromAndTruncate(directory);

        String filename = parser.getDestinationFileName(directory);

        String[] options = {menuItem, "Exit"};
        return JOptionPane.showOptionDialog(frame,
                "Would you like to "+sentenceItem+" this file: "+ filename +"\ncontaining:\n"+truncate+"...",
                menuItem,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                icon,
                options,
                options[1]);
    }

    default JPanel savePanel(InnerWindow inner, String savedContent, String saveExt){
        WindowFrame gui = inner.gui();

        JButton saveButton = new JButton("Save As...");
        saveButton.setBackground(off_white);
        JPanel savePanel = new JPanel();
        savePanel.add(saveButton);

        class saveFromWindow implements ActionListener{
            public void actionPerformed(ActionEvent e){
                gui.setContent(savedContent);
                gui.setCanSaveAs(true);
                gui.setCurrentExt(saveExt);
                inner.saveAs(saveExt);
            }
        }
        saveButton.addActionListener(new saveFromWindow());

        return savePanel;
    }

    default void setRestFalse(JRadioButton b1, JRadioButton b2){
        b1.setSelected(false);
        b2.setSelected(false);
    }

    default void backToStartPanel(InnerWindow inner, JPanel startContainerPanel, JPanel removeThisBasePanel){
        JPanel root = inner.root();
        JPanel startPanel = inner.startPanel();

        JButton backToStartButton = new JButton("Return to Start");
        JPanel backToStartPanel = new JPanel();
        backToStartButton.setBackground(off_white);
        backToStartPanel.add(backToStartButton);
        startContainerPanel.add(backToStartPanel);

        class backToStart implements ActionListener{
            public void actionPerformed(ActionEvent e){
                root.remove(removeThisBasePanel);
                inner.refresh();
                root.add(startPanel);

                inner.setTabText("Home");
            }
        }
        backToStartButton.addActionListener(new backToStart());
    }

    default JPanel standardBorderLayout(InnerWindow inner, Component c){
        JPanel backing = new JPanel(new BorderLayout());
        JPanel center = new JPanel();
        JPanel west = new JPanel();
        JPanel east = new JPanel();
        JPanel north = new JPanel();
        JPanel south = new JPanel();

        backing.add(center, BorderLayout.CENTER);
        backing.add(west, BorderLayout.WEST);
        backing.add(east, BorderLayout.EAST);
        backing.add(north, BorderLayout.NORTH);
        backing.add(south, BorderLayout.SOUTH);

        if(c != null) center.add(c);

        backToStartPanel(inner, south, backing);

        return backing;
    }


    default void saveFromTextField(InnerWindow inner, String ciphertext, String title){
        JPanel root = inner.root();

        String saveExt = inner.saveExt();

        JLabel titleCT = new JLabel(title);
        titleCT.setFont(new Font("Serif", Font.BOLD, 25));
        titleCT.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField textFieldCT = new JTextField(ciphertext, ciphertext.length());
        JPanel textFieldPanel = new JPanel();
        textFieldPanel.add(textFieldCT);

        JPanel savePanel = savePanel(inner, textFieldCT.getText(), saveExt);

        JPanel showCT = new JPanel(new GridLayout(3,1));
        showCT.add(titleCT);
        showCT.add(textFieldPanel);
        showCT.add(savePanel);

        JPanel backing = standardBorderLayout(inner, showCT);

        root.add(backing);

        inner.refresh();
    }


    default void setupChoicesPanel(InnerWindow inner, JRadioButton[] buttons, GridBagConstraints constraints, JPanel choices, JPanel backing){
        buttons[0].setHorizontalAlignment(SwingConstants.LEADING);
        buttons[1].setHorizontalAlignment(SwingConstants.LEADING);
        buttons[2].setHorizontalAlignment(SwingConstants.LEADING);

        constraints.gridy = 0;
        choices.add(buttons[0], constraints);

        constraints.gridy = 1;
        choices.add(buttons[1], constraints);

        constraints.gridy = 2;
        choices.add(buttons[2], constraints);

        backing = standardBorderLayout(inner, choices);

        inner.root().add(backing);
        inner.refresh();
    }

}
