package GUI;

import EncryptionMethods.Encryption;
import EncryptionMethods.MoorePenrose;
import EncryptionMethods.PseudoInverse2;
import EncryptionMethods.SquareMatrix;
import FileReader.Parser;
import NumberTypes.RootFraction;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WindowFrame {

    final int WIDTH = 1000;
    final int HEIGHT = 500;
    final int vShift = 25;

    final JFrame frame = new JFrame("Encryption Application");

    final Parser parser = new Parser();
    final String s = parser.getDestinationPathToFile(new File("EncryptionAlgorithms").getAbsolutePath());
    String textDirectory = s+"src\\text";
    String currentExt = "txt";
    String content = "intentionally empty";

    final ImageIcon lockIcon = makeIconFrom(s+"src\\images\\lock.png", 120, 120);
    final ImageIcon dlIcon = makeIconFrom(s+"src\\images\\download.png", 70, 70);

    boolean canSaveAs = false;

    final WindowOrganizer organizer;
    final TextEditor editor;

    public WindowFrame(){
        organizer = new WindowOrganizer(this);
        editor = new TextEditor(this);

        setupFrame();

        setupMenuBar();

        customLayout();

        organizer.tabNum++;
        organizer.openTabs++;
    }

    private void setupFrame(){
        frame.setVisible(true);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(lockIcon.getImage());
    }


    private void setupMenuBar(){
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);

        JMenu file = new JMenu("File");
        menubar.add(file);

        JMenuItem newTab = new JMenuItem("New Tab");
        file.add(newTab);

        JMenuItem saveAs = new JMenuItem("Save As...");
        file.add(saveAs);

        JMenuItem open = new JMenuItem("Open");
        file.add(open);

        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);


        JMenu edit = new JMenu("Edit");
        menubar.add(edit);

        JMenuItem editFile = new JMenuItem("Edit File...");
        edit.add(editFile);

        JMenuItem newTextFile = new JMenuItem("Text Editor");
        edit.add(newTextFile);

        JMenuItem changeDirectory = new JMenuItem("Change Save Directory...");
        edit.add(changeDirectory);


        JMenu defaults = new JMenu("Default");
        menubar.add(defaults);

        JMenuItem oldSaveDirectory = new JMenuItem("Save Directory");
        defaults.add(oldSaveDirectory);


        JMenu help = new JMenu("Help");
        menubar.add(help);

        JMenuItem refresh = new JMenuItem("Refresh");
        help.add(refresh);

        JMenuItem about = new JMenuItem("About");
        help.add(about);


        if(organizer.tabNum == 0){
            organizer.tabNum++;
            organizer.openTabs++;
            organizer.tabBar();
            organizer.window();
        }

        refresh();

        fileBehavior(saveAs, exit, open, newTab);

        editBehavior(editFile, newTextFile, changeDirectory);

        defaultsBehavior(oldSaveDirectory);

        helpBehavior(about, refresh);

    }

    private void fileBehavior(JMenuItem saveAs, JMenuItem exit, JMenuItem open, JMenuItem newTab){

        class saver implements ActionListener{
            public void actionPerformed(ActionEvent e){
                if(!canSaveAs) saveAs();
                else saveAs(currentExt);
            }
        }
        saveAs.addActionListener(new saver());

        class exitaction implements ActionListener {
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        }
        exit.addActionListener(new exitaction());

        class opener implements ActionListener{
            public void actionPerformed(ActionEvent e){
                if(e.getSource() == open) {
                    openThis();
                }
            }
        }
        open.addActionListener(new opener());

        class addTab implements ActionListener{
            public void actionPerformed(ActionEvent e){
                organizer.tabBar();
                organizer.window();
                organizer.tabNum++;
                organizer.openTabs++;
            }
        }
        newTab.addActionListener(new addTab());
    }

    private void editBehavior(JMenuItem editFile, JMenuItem newTextFile, JMenuItem changeDirectory){
        class editExistingFile implements ActionListener{

            public void actionPerformed(ActionEvent e){
                editor.openFileWithTextEditor();
            }
        }
        editFile.addActionListener(new editExistingFile());

        class editNewFile implements ActionListener{
            public void actionPerformed(ActionEvent e){
                editor.openEmptyTextEditor();
            }
        }
        newTextFile.addActionListener(new editNewFile());

        class changeDir implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                changeDirectory();
            }
        }
        changeDirectory.addActionListener(new changeDir());
    }

    private void helpBehavior(JMenuItem about, JMenuItem refresh){
        class aboutism implements ActionListener{
            public void actionPerformed(ActionEvent e){
                showAbout();
            }
        }
        about.addActionListener(new aboutism());


        class refresher implements ActionListener{
            public void actionPerformed(ActionEvent e){
                refresh();
            }
        }
        refresh.addActionListener(new refresher());
    }

    private void defaultsBehavior(JMenuItem oldSaveDirectory){
        class oldDir implements ActionListener{
            @Override
            public void actionPerformed(ActionEvent e) {
                textDirectory = s+"src\\text";
            }
        }
        oldSaveDirectory.addActionListener(new oldDir());
    }

    private void customLayout(){
        Color barColor = new Color(97, 95, 95,255);
        Color spaceColor = new Color(75, 73, 73,255);

        JPanel pane = (JPanel) frame.getContentPane();

        organizer.keyStrokeMaps(pane);

        organizer.bar.setPreferredSize(new Dimension(WIDTH, vShift));
        organizer.bar.setBackground(barColor);

        organizer.space.setPreferredSize(new Dimension(WIDTH, HEIGHT-3*vShift));
        organizer.space.setBackground(spaceColor);

        pane.add(organizer.bar, BorderLayout.NORTH);
        pane.add(organizer.space, BorderLayout.SOUTH);

        refresh();
    }

    private void changeDirectory(){
        String directory = JOptionPane.showInputDialog(frame,
                "Input:",
                "New Save Directory",
                JOptionPane.PLAIN_MESSAGE);

        if(directory == null) return;

        boolean valid = directory.startsWith("C:\\");

        if(valid) {
            textDirectory = directory;
        }else{
            JOptionPane.showMessageDialog(frame,
                    "This path isn't absolute",
                    "Invalid Path",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }

    ImageIcon makeIconFrom(String directory, int pixelWidth, int pixelHeight){
        ImageIcon icon = new ImageIcon(directory);
        Image image = icon.getImage();
        Image smallerImage = image.getScaledInstance(pixelWidth, pixelHeight, Image.SCALE_AREA_AVERAGING);
        icon.setImage(smallerImage);
        return icon;
    }

    void saveAs(){
        saveAs("txt");
    }

    void saveAs(String ext){

        String directory = saveFile();

        if(directory == null) return;
        if(directory.equals("")) return;
        if(directory.equals(" ")) return;

        adjustableFileType(directory, ext);
    }

    private void adjustableFileType(String directory, String ext){
        try{
            FileWriter fw = new FileWriter(directory+"."+ext);
            fw.write(content);
            fw.close();
            String filename = parser.getDestinationFileName(directory);
            JOptionPane.showMessageDialog(frame,
                    "''"+filename+"."+ext+"'' saved successfully",
                    "Successful",
                    JOptionPane.PLAIN_MESSAGE,
                    dlIcon);

        }catch (IOException exception){
            exception.printStackTrace();
        }
    }

    String chooseFile() {
        String directory = "";
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Square MatrixStructures.Matrix", "esm"));
        fc.setFileFilter(new FileNameExtensionFilter("Moore Penrose", "emp"));
        fc.setFileFilter(new FileNameExtensionFilter("Pseudo Inverse", "epi"));
        fc.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
        fc.setCurrentDirectory(new File(textDirectory));

        int response = fc.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File file = new File(fc.getSelectedFile().getAbsolutePath());
            directory = file.getAbsolutePath();
        }
        return directory;
    }

    private String saveFile(){
        String directory = "";
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(textDirectory));
        int response = fc.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            File file = new File(fc.getSelectedFile().getAbsolutePath());
            directory = file.getAbsolutePath();
        }
        return directory;
    }

    private void openThis(){
        String directory = chooseFile();

        if(directory == null) return;
        if(directory.equals("")) return;
        if(directory.equals(" ")) return;

        String truncate = readFromAndTruncate(directory);

        String[] options = {"Encrypt", "Decrypt", "Neither"};
        int n = JOptionPane.showOptionDialog(frame,
                "Would you like to encrypt or decrypt the file: "+ directory +"\ncontaining:\n"+truncate+"...",
                "Encrypt or Decrypt?",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                lockIcon,
                options,
                options[2]);
        if(n == 0) {
            toEncrypt(directory, lockIcon);
        }
        if(n == 1){
            toDecrypt(directory);
        }
    }

    String readFromAndTruncate(String filepath){

        content = parser.read(filepath);

        StringBuilder sb = new StringBuilder();
        int count = 0;
        for(int i = 0; i < content.length(); i++){
            if(count == 4) break;
            if(content.charAt(i) == '\n') count++;
            sb.append(content.charAt(i));
        }
        return sb.toString();
    }

    void toEncrypt(String filename, ImageIcon icon){
        Object[] possibilities = {"esm", "emp", "epi"};
        String ext = (String) JOptionPane.showInputDialog(frame,
                "Choose file type for "+filename,
                "File Type",
                JOptionPane.PLAIN_MESSAGE,
                icon,
                possibilities,
                possibilities[2]);

        String plaintext = content;

        switch (ext) {
            case "esm" -> encryptAndReformat(new SquareMatrix<>(new RootFraction()), parser, plaintext, filename, ext);
            case "emp" -> encryptAndReformat(new MoorePenrose<>(new RootFraction()), parser, plaintext, filename, ext);
            case "epi" -> encryptAndReformat(new PseudoInverse2<>(new RootFraction()), parser, plaintext, filename, ext);
        }
    }

    void toDecrypt(String filename){
        String ciphertext = content;
        String ext = filename.substring(filename.length()-3);

        switch (ext) {
            case "esm" -> {
                decryptAndReformat(new SquareMatrix<>(new RootFraction()), parser, ciphertext, filename);
                return;
            }
            case "emp" -> {
                decryptAndReformat(new MoorePenrose<>(new RootFraction()), parser, ciphertext, filename);
                return;
            }
            case "epi" -> {
                decryptAndReformat(new PseudoInverse2<>(new RootFraction()), parser, ciphertext, filename);
                return;
            }
        }

        JOptionPane.showMessageDialog(frame, "Cannot decrypt a ."+ext+" file");

    }

    private void encryptAndReformat(Encryption<RootFraction> e, Parser p, String plaintext, String directory, String ext){
        String ciphertext = e.encrypt(p.noNewLines(plaintext));
        String filename = p.getDestinationFileName(directory), filepath = p.getDestinationPathToFile(directory);
        String encDirectory = filepath+"enc_"+filename;
        JOptionPane.showMessageDialog(frame,
                "Encryption of ''"+filename+"'' Successful",
                "Successful",
                JOptionPane.PLAIN_MESSAGE,
                lockIcon
        );
        p.reformatCustomFile(encDirectory.substring(0, encDirectory.length()-4), ciphertext, ext);
    }

    private void decryptAndReformat(Encryption<RootFraction> e, Parser p, String ciphertext, String directory){
        String plaintext = e.decrypt(p.noNewLines(ciphertext));
        String filename = p.getDestinationFileName(directory);
        JOptionPane.showMessageDialog(frame,
                "Decryption of ''"+filename+"'' Successful",
                "Successful",
                JOptionPane.PLAIN_MESSAGE,
                lockIcon);
        p.reformatTextFile(directory, plaintext);
    }

    private void showAbout(){
        String message = parser.read("src/text/about_message.txt");

        JOptionPane.showOptionDialog(frame,
                message,
                "About My Encryption Project",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                lockIcon,
                null,
                null);
    }

    void refresh(){
        frame.setSize(WIDTH+1, HEIGHT);
        frame.setSize(WIDTH, HEIGHT);
    }

    public void setContent(String s){
        content = s;
    }

    public void setCanSaveAs(boolean b){
        canSaveAs = b;
    }

    public void setCurrentExt(String s){
        currentExt = s;
    }

    void openFileWithTextEditor(){
        editor.openFileWithTextEditor();
    }

    void setTabText(String text, int ID){
        organizer.setTabText(text, ID);
    }

    void setTabIcon(ImageIcon icon, int ID){
        organizer.setTabIcon(icon, ID);
    }

}
