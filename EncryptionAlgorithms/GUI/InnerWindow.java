package GUI;

import EncryptionMethods.MoorePenrose;
import EncryptionMethods.PseudoInverse2;
import EncryptionMethods.SquareMatrix;
import FileReader.Parser;
import NumberTypes.Fraction;
import NumberTypes.RootFraction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class InnerWindow {

    private final WindowFrame gui;

    private ImageIcon lockIcon;
    private Parser parser;
    private JFrame frame;

    private final JPanel root = new JPanel(new GridLayout(1, 0));
    private final JPanel startPanel = new JPanel(new GridLayout(4, 0));

    private final Color standardButtonColor = new Color(187, 187, 184,255);

    private final int ID;

    InnerWindow(WindowFrame gui, int ID){
        this.gui = gui;
        this.ID = ID;
        setupStartPanel();
    }

    public JPanel root(){
        return root;
    }

    private void setupStartPanel(){

        parser = gui.parser;
        lockIcon = gui.lockIcon;
        frame = gui.frame;

        int WIDTH = gui.WIDTH;
        int HEIGHT = gui.HEIGHT;
        int vShift = gui.vShift;

        startPanel.setSize(WIDTH, HEIGHT -6* vShift);

        JLabel headLabel = new JLabel("Encryption Algorithm");
        JButton encryptButton = new JButton("Encrypt");
        encryptButton.setBackground(standardButtonColor);
        JLabel orLabel = new JLabel("or");
        JButton decryptButton = new JButton("Decrypt");
        decryptButton.setBackground(standardButtonColor);

        headLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headLabel.setFont(new Font("Serif", Font.BOLD, 30));

        orLabel.setHorizontalAlignment(SwingConstants.CENTER);
        orLabel.setVerticalAlignment(SwingConstants.NORTH);
        orLabel.setFont(new Font("Serif", Font.PLAIN, 20));

        JPanel encPanel = new JPanel();
        encPanel.add(encryptButton);

        JPanel decPanel = new JPanel();
        decPanel.add(decryptButton);

        startPanel.add(headLabel);
        startPanel.add(encPanel);
        startPanel.add(orLabel);
        startPanel.add(decPanel);

        root.add(startPanel);
        refresh();

        setTabText("Home");

        class removeEncrypt implements ActionListener {
            public void actionPerformed(ActionEvent e){
                root.remove(startPanel);
                refresh();
                setupEncryptPanel();
            }
        }
        encryptButton.addActionListener(new removeEncrypt());

        class removeDecrypt implements ActionListener{
            public void actionPerformed(ActionEvent e){
                root.remove(startPanel);
                refresh();
                setupDecryptPanel();
            }
        }
        decryptButton.addActionListener(new removeDecrypt());
    }


    private void setupChoicesPanel(JRadioButton[] buttons, GridBagConstraints constraints, JPanel choices, JPanel backing){
        buttons[0].setHorizontalAlignment(SwingConstants.LEADING);
        buttons[1].setHorizontalAlignment(SwingConstants.LEADING);
        buttons[2].setHorizontalAlignment(SwingConstants.LEADING);

        constraints.gridy = 0;
        choices.add(buttons[0], constraints);

        constraints.gridy = 1;
        choices.add(buttons[1], constraints);

        constraints.gridy = 2;
        choices.add(buttons[2], constraints);

        backing = standardBorderLayout(choices);

        root.add(backing);
        refresh();
    }

    /**
     * Encryption Path
     * */
    private void setupEncryptPanel(){
        JPanel blankPanel = new JPanel();
        blankPanel.add(new JLabel());

        JLabel textLabel = new JLabel("Enter Text You Want To Encrypt Here: ");
        JTextField textField = new JTextField(40);

        JPanel inputFieldAndOpen = new JPanel(new GridLayout(3, 1));
        inputFieldAndOpen.add(textLabel);
        inputFieldAndOpen.add(textField);

        JLabel message = new JLabel("Choose a word with more than 8 characters");
        message.setVisible(false);
        message.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel openPanel = openPanel("Encrypt", "encrypt");
        JButton openTextEditor = new JButton("Edit Text");
        openTextEditor.setBackground(standardButtonColor);
        openPanel.add(openTextEditor);

        inputFieldAndOpen.add(openPanel);

        JPanel messagePanel = new JPanel(new GridLayout(2, 0));
        messagePanel.add(message);

        JPanel encryptPanel = new JPanel(new GridLayout(5, 0));
        encryptPanel.add(blankPanel);
        encryptPanel.add(inputFieldAndOpen);
        encryptPanel.add(messagePanel);

        JPanel backing = standardBorderLayout(encryptPanel);

        root.add(backing);

        refresh();

        setTabText("Encrypt");

        class onClickEnter implements ActionListener{
            private int clickCount = 0;
            public void actionPerformed(ActionEvent e){
                clickCount++;
                String text = textField.getText();

                if(text.length() > 8) {
                    root.remove(backing);
                    refresh();
                    displayCiphertext(text);
                }
                else{
                    if(clickCount == 1){
                        message.setVisible(true);
                        refresh();
                    }
                }
            }
        }
        textField.addActionListener(new onClickEnter());


        class openAFileToEdit implements ActionListener{
            public void actionPerformed(ActionEvent e){
                openFileWithTextEditor();
            }
        }
        openTextEditor.addActionListener(new openAFileToEdit());

    }

    private String saveExt = "txt";
    private void displayCiphertext(String plaintext){
        JPanel choices = new JPanel(new GridLayout(6, 1));
        JLabel choose = new JLabel("Choose An Encryption Method");
        GridBagConstraints constraints = new GridBagConstraints();

        choose.setHorizontalAlignment(SwingConstants.CENTER);
        choose.setFont(new Font("SansSerif", Font.PLAIN, 20));
        choices.add(choose);

        JRadioButton square = new JRadioButton("Square Matrix Encryption");
        JRadioButton moore = new JRadioButton("Moore-Penrose EEncryption");
        JRadioButton pseudo2 = new JRadioButton("Pseudo-Inverse Encryption");

        JPanel backing = new JPanel();
        setupChoicesPanel(new JRadioButton[]{square, moore, pseudo2}, constraints, choices, backing);


        class onSelection implements ActionListener{
            public void actionPerformed(ActionEvent e){
                JRadioButton button = (JRadioButton) e.getSource();
                String ciphertext = "this is a test phrase";
                if(button == square){
                    setRestFalse(moore, pseudo2);
                    SquareMatrix<Fraction> esm = new SquareMatrix<>(new Fraction());
                    ciphertext = esm.encrypt(plaintext);
                    saveExt = "esm";
                }else if(button == moore){
                    setRestFalse(square, pseudo2);
                    MoorePenrose<RootFraction> emp = new MoorePenrose<>(new RootFraction());
                    ciphertext = emp.encrypt(plaintext);
                    saveExt = "emp";
                }if(button == pseudo2){
                    setRestFalse(square, moore);
                    PseudoInverse2<RootFraction> epi2 = new PseudoInverse2<>(new RootFraction());
                    ciphertext = epi2.encrypt(plaintext);
                    saveExt = "epi";
                }

                root.remove(backing);

                saveFromTextField(ciphertext, "Here Is The Ciphertext You Encrypted");

            }
        }

        square.addActionListener(new onSelection());
        moore.addActionListener(new onSelection());
        pseudo2.addActionListener(new onSelection());

    }

    private void saveFromTextField(String ciphertext, String title){

        JLabel titleCT = new JLabel(title);
        titleCT.setFont(new Font("Serif", Font.BOLD, 25));
        titleCT.setHorizontalAlignment(SwingConstants.CENTER);

        JTextField textFieldCT = new JTextField(ciphertext, ciphertext.length());
        JPanel textFieldPanel = new JPanel();
        textFieldPanel.add(textFieldCT);

        JPanel savePanel = savePanel(textFieldCT.getText());

        JPanel showCT = new JPanel(new GridLayout(3,1));
        showCT.add(titleCT);
        showCT.add(textFieldPanel);
        showCT.add(savePanel);

        JPanel backing = standardBorderLayout(showCT);

        root.add(backing);

        refresh();

    }

    /**
     * Decryption Path
     * */
    private void setupDecryptPanel(){
        JPanel blankPanel = new JPanel();
        blankPanel.add(new JLabel());

        JLabel textLabel = new JLabel("Enter Text You Want To Decrypt Here: ");
        JTextField textField = new JTextField(40);

        JPanel inputPanel = new JPanel(new GridLayout(3, 1));
        inputPanel.add(textLabel);
        inputPanel.add(textField);

        JPanel openPanel = openPanel("Decrypt", "decrypt");
        inputPanel.add(openPanel);

        JPanel encryptPanel = new JPanel(new GridLayout(5, 0));
        encryptPanel.add(blankPanel);
        encryptPanel.add(inputPanel);
        JPanel backing = standardBorderLayout(encryptPanel);

        root.add(backing);

        refresh();

        setTabText("Decrypt");

        class onClickEnter implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String text = textField.getText();
                root.remove(backing);
                refresh();
                displayPlaintext(text);
            }
        }
        textField.addActionListener(new onClickEnter());
    }

    private void displayPlaintext(String ciphertext){
        JPanel choices = new JPanel(new GridLayout(6, 1));
        JLabel choose = new JLabel("Choose A Decryption Method");
        GridBagConstraints constraints = new GridBagConstraints();

        choose.setHorizontalAlignment(SwingConstants.CENTER);
        choose.setFont(new Font("SansSerif", Font.PLAIN, 20));
        choices.add(choose);

        JRadioButton square = new JRadioButton("Square Matrix Encryption");
        JRadioButton moore = new JRadioButton("Moore-PenroseEncryption");
        JRadioButton pseudo2 = new JRadioButton("Pseudo-Inverse Encryption");

        JPanel backing = new JPanel();
        setupChoicesPanel(new JRadioButton[]{square, moore, pseudo2}, constraints, choices, backing);

        class onSelection implements ActionListener{
            public void actionPerformed(ActionEvent e){
                JRadioButton button = (JRadioButton) e.getSource();
                String plaintext = "this is a test phrase";
                if(button == square){
                    setRestFalse(moore, pseudo2);
                    SquareMatrix<Fraction> esm = new SquareMatrix<>(new Fraction());
                    plaintext = esm.decrypt(ciphertext);
                    saveExt = "esm";
                }else if(button == moore){
                    setRestFalse(square, pseudo2);
                    MoorePenrose<RootFraction> emp = new MoorePenrose<>(new RootFraction());
                    plaintext = emp.decrypt(ciphertext);
                    saveExt = "emp";
                }if(button == pseudo2){
                    setRestFalse(square, moore);
                    PseudoInverse2<RootFraction> epi2 = new PseudoInverse2<>(new RootFraction());
                    plaintext = epi2.decrypt(ciphertext);
                    saveExt = "epi";
                }

                root.remove(backing);

                saveFromTextField(plaintext, "Here Is The Plaintext You Decrypted");
            }
        }
        square.addActionListener(new onSelection());
        moore.addActionListener(new onSelection());
        pseudo2.addActionListener(new onSelection());
    }

    /**
     * Other helpful methods
     * */


    private JPanel openPanel(String menuItem, String sentenceItem){
        JPanel open = new JPanel();
        JButton button = new JButton("Open File");
        button.setBackground(standardButtonColor);

        open.add(button);

        class openFromWindow implements ActionListener{
            public void actionPerformed(ActionEvent e){
                String directory = chooseFile();
                int n = actionsWithFile(menuItem, sentenceItem, directory, lockIcon);
                if(menuItem.equals("Encrypt")){
                    if(n == 0) {
                        toEncrypt(directory, lockIcon);
                    }
                }else if(menuItem.equals("Decrypt")){
                    if(n == 0) {
                        toDecrypt(directory);
                    }
                }
            }
        }
        button.addActionListener(new openFromWindow());
        return open;
    }

    private JPanel savePanel(String savedContent){
        JButton saveButton = new JButton("Save As...");
        saveButton.setBackground(standardButtonColor);
        JPanel savePanel = new JPanel();
        savePanel.add(saveButton);

        class saveFromWindow implements ActionListener{
            public void actionPerformed(ActionEvent e){
                gui.setContent(savedContent);
                gui.setCanSaveAs(true);
                gui.setCurrentExt(saveExt);
                saveAs(saveExt);
            }
        }
        saveButton.addActionListener(new saveFromWindow());

        return savePanel;
    }



    private int actionsWithFile(String menuItem, String sentenceItem, String directory, ImageIcon icon){

        if(directory == null) return -1;
        if(directory.equals("")) return -1;
        if(directory.equals(" ")) return -1;

        String truncate = readFromAndTruncate(directory);

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




    private void setRestFalse(JRadioButton b1, JRadioButton b2){
        b1.setSelected(false);
        b2.setSelected(false);
    }

    private void backToStartPanel(JPanel startContainerPanel, JPanel removeThisBasePanel){
        JButton backToStartButton = new JButton("Return to Start");
        JPanel backToStartPanel = new JPanel();
        backToStartButton.setBackground(standardButtonColor);
        backToStartPanel.add(backToStartButton);
        startContainerPanel.add(backToStartPanel);

        class backToStart implements ActionListener{
            public void actionPerformed(ActionEvent e){
                root.remove(removeThisBasePanel);
                refresh();
                root.add(startPanel);

                setTabText("Home");
            }
        }
        backToStartButton.addActionListener(new backToStart());
    }


    private JPanel standardBorderLayout(Component c){
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

        backToStartPanel(south, backing);

        return backing;
    }

    void refresh(){
        gui.refresh();
    }

    void openFileWithTextEditor(){
        gui.openFileWithTextEditor();
    }

    String chooseFile(){
        return gui.chooseFile();
    }

    void toEncrypt(String directory, ImageIcon icon){
        gui.toEncrypt(directory, icon);
    }

    void toDecrypt(String directory){
        gui.toDecrypt(directory);
    }

    void saveAs(String saveExt){
        gui.saveAs(saveExt);
    }

    String readFromAndTruncate(String directory){
        return gui.readFromAndTruncate(directory);
    }

    void setTabText(String text){
        gui.setTabText(text, ID);
    }

    void setTabIcon(ImageIcon icon){
        gui.setTabIcon(icon, ID);
    }

}


