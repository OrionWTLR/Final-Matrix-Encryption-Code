package GUI;

import EncryptionMethods.MoorePenrose;
import EncryptionMethods.PseudoInverse2;
import EncryptionMethods.SquareMatrix;
import NumberTypes.Fraction;
import NumberTypes.RootFraction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EncryptionBranch implements DesignScheme{

    private final InnerWindow inner;
    private final JPanel root;
    private final JPanel startPanel;

    private final ImageIcon lockIcon;


    EncryptionBranch(InnerWindow inner){
        this.inner = inner;
        root = inner.root();
        startPanel = inner.startPanel();
        lockIcon = inner.lockIcon();
        setupEncryptPanel(inner);
    }

    public void refresh(){
        inner.refresh();
    }

    /**
     * Encryption Path
     * */
    private void setupEncryptPanel(InnerWindow inner){
        root.remove(startPanel);
        refresh();

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

        JPanel openPanel = openPanel(inner,"Encrypt", "encrypt", lockIcon);
        JButton openTextEditor = new JButton("Edit Text");
        openTextEditor.setBackground(off_white);
        openPanel.add(openTextEditor);

        inputFieldAndOpen.add(openPanel);

        JPanel messagePanel = new JPanel(new GridLayout(2, 0));
        messagePanel.add(message);

        JPanel encryptPanel = new JPanel(new GridLayout(5, 0));
        encryptPanel.add(blankPanel);
        encryptPanel.add(inputFieldAndOpen);
        encryptPanel.add(messagePanel);

        JPanel backing = standardBorderLayout(inner, encryptPanel);

        root.add(backing);

        refresh();

        inner.setTabText("Encrypt");

        class onClickEnter implements ActionListener{
            private int clickCount = 0;
            public void actionPerformed(ActionEvent e){
                clickCount++;
                String text = textField.getText();

                if(text.length() > 8) {
                    root.remove(backing);
                    refresh();
                    displayCiphertext(text);
                }else{
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
                inner.openFileWithTextEditor();
            }
        }
        openTextEditor.addActionListener(new openAFileToEdit());

    }


    private void displayCiphertext(String plaintext){
        JPanel choices = new JPanel(new GridLayout(6, 1));
        JLabel choose = new JLabel("Choose An Encryption Method");
        GridBagConstraints constraints = new GridBagConstraints();

        choose.setHorizontalAlignment(SwingConstants.CENTER);
        choose.setFont(new Font("SansSerif", Font.PLAIN, 20));
        choices.add(choose);

        JRadioButton square = new JRadioButton("Square Matrix Encryption");
        JRadioButton moore = new JRadioButton("Moore-Penrose Encryption");
        JRadioButton pseudo2 = new JRadioButton("Pseudo-Inverse Encryption");

        JPanel backing = new JPanel();
        setupChoicesPanel(inner, new JRadioButton[]{square, moore, pseudo2}, constraints, choices, backing);

        class onSelection implements ActionListener{
            public void actionPerformed(ActionEvent e){
                JRadioButton button = (JRadioButton) e.getSource();
                String ciphertext = "this is a test phrase";
                if(button == square){
                    setRestFalse(moore, pseudo2);
                    SquareMatrix<Fraction> esm = new SquareMatrix<>(new Fraction());
                    ciphertext = esm.encrypt(plaintext);
                    inner.setSaveExt("esm");
                }else if(button == moore){
                    setRestFalse(square, pseudo2);
                    MoorePenrose<RootFraction> emp = new MoorePenrose<>(new RootFraction());
                    ciphertext = emp.encrypt(plaintext);
                    inner.setSaveExt("emp");
                }if(button == pseudo2){
                    setRestFalse(square, moore);
                    PseudoInverse2<RootFraction> epi2 = new PseudoInverse2<>(new RootFraction());
                    ciphertext = epi2.encrypt(plaintext);
                    inner.setSaveExt("epi");
                }

                JPanel panel = (JPanel) root.getComponents()[0];

                root.remove(panel);
                refresh();

                saveFromTextField(inner, ciphertext, "Here Is The Ciphertext You Encrypted");

            }
        }

        square.addActionListener(new onSelection());
        moore.addActionListener(new onSelection());
        pseudo2.addActionListener(new onSelection());
    }


}
