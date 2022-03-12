package GUI;

import FileReader.Parser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class InnerWindow implements DesignScheme{

    private final WindowFrame gui;
    private final int ID;

    private ImageIcon lockIcon;
    private Parser parser;
    private JFrame frame;

    private final JPanel root = new JPanel(new GridLayout(1, 0));
    private final JPanel startPanel = new JPanel(new GridLayout(4, 0));

    private String saveExt = "txt";

    InnerWindow(WindowFrame gui, int ID){
        this.gui = gui;
        this.ID = ID;

        setupStartPanel();
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
        encryptButton.setBackground(off_white);
        JLabel orLabel = new JLabel("or");
        JButton decryptButton = new JButton("Decrypt");
        decryptButton.setBackground(off_white);

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
                setupEncryptPanel();
            }
        }
        encryptButton.addActionListener(new removeEncrypt());

        class removeDecrypt implements ActionListener{
            public void actionPerformed(ActionEvent e){
                setupDecryptPanel();
            }
        }
        decryptButton.addActionListener(new removeDecrypt());
    }

    private void setupEncryptPanel(){
        new EncryptionBranch(this);
    }

    private void setupDecryptPanel(){
        new DecryptionBranch(this);
    }

    /**
     * Other helpful methods
     * */
    WindowFrame gui(){return gui;}

    JFrame frame(){return frame;}

    public JPanel root(){return root;}

    public JPanel startPanel(){return startPanel;}

    String saveExt(){return saveExt;}
    void setSaveExt(String s){saveExt = s;}

    Parser parser(){return parser;}

    int ID(){return ID;}

    ImageIcon lockIcon(){return lockIcon;}

    String chooseFile(){
        return gui.chooseFile();
    }

    String readFromAndTruncate(String directory){
        return gui.readFromAndTruncate(directory);
    }

    void toEncrypt(String directory, ImageIcon icon){
        gui.toEncrypt(directory, icon);
    }

    void toDecrypt(String directory){
        gui.toDecrypt(directory);
    }

    void refresh(){
        gui.refresh();
    }

    void openFileWithTextEditor(){
        gui.openFileWithTextEditor();
    }

    void saveAs(String saveExt){
        gui.saveAs(saveExt);
    }

    void setTabText(String text){
        gui.setTabText(text, ID);
    }

    void setTabIcon(ImageIcon icon){
        gui.setTabIcon(icon, ID);
    }

}


