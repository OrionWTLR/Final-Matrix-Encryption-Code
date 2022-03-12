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

public class DecryptionBranch implements DesignScheme{
    private final InnerWindow inner;
    private final JPanel root;
    private final JPanel startPanel;

    private final ImageIcon lockIcon;

    DecryptionBranch(InnerWindow inner){
        this.inner = inner;
        root = inner.root();
        startPanel = inner.startPanel();
        lockIcon = inner.lockIcon();
        setupDecryptPanel();
    }

    public void refresh(){
        inner.refresh();
    }

    /**
     * Decryption Path
     * */
    private void setupDecryptPanel(){
        root.remove(startPanel);
        refresh();

        JPanel blankPanel = new JPanel();
        blankPanel.add(new JLabel());

        JLabel textLabel = new JLabel("Enter Text You Want To Decrypt Here: ");
        JTextField textField = new JTextField(40);

        JPanel inputPanel = new JPanel(new GridLayout(3, 1));
        inputPanel.add(textLabel);
        inputPanel.add(textField);

        JPanel openPanel = openPanel(inner,"Decrypt", "decrypt", lockIcon);
        inputPanel.add(openPanel);

        JPanel encryptPanel = new JPanel(new GridLayout(5, 0));
        encryptPanel.add(blankPanel);
        encryptPanel.add(inputPanel);
        JPanel backing = standardBorderLayout(inner, encryptPanel);

        root.add(backing);

        refresh();

        inner.setTabText("Decrypt");

        class onClickEnter implements ActionListener {
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
        JRadioButton moore = new JRadioButton("Moore-Penrose Encryption");
        JRadioButton pseudo2 = new JRadioButton("Pseudo-Inverse Encryption");

        JPanel backing = new JPanel();
        setupChoicesPanel(inner, new JRadioButton[]{square, moore, pseudo2}, constraints, choices, backing);

        class onSelection implements ActionListener{
            public void actionPerformed(ActionEvent e){
                JRadioButton button = (JRadioButton) e.getSource();
                String plaintext = "this is a test phrase";
                if(button == square){
                    setRestFalse(moore, pseudo2);
                    SquareMatrix<Fraction> esm = new SquareMatrix<>(new Fraction());
                    plaintext = esm.decrypt(ciphertext);
                    inner.setSaveExt("esm");
                }else if(button == moore){
                    setRestFalse(square, pseudo2);
                    MoorePenrose<RootFraction> emp = new MoorePenrose<>(new RootFraction());
                    plaintext = emp.decrypt(ciphertext);
                    inner.setSaveExt("emp");
                }if(button == pseudo2){
                    setRestFalse(square, moore);
                    PseudoInverse2<RootFraction> epi2 = new PseudoInverse2<>(new RootFraction());
                    plaintext = epi2.decrypt(ciphertext);
                    inner.setSaveExt("epi");
                }

                root.remove(backing);

                saveFromTextField(inner, plaintext, "Here Is The Plaintext You Decrypted");
            }
        }
        square.addActionListener(new onSelection());
        moore.addActionListener(new onSelection());
        pseudo2.addActionListener(new onSelection());
    }
}
