package GUI;

import FileReader.Parser;

import javax.sound.sampled.Line;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class TextEditor implements DesignScheme{

    private final WindowFrame gui;
    private final Parser parser = new Parser();
    private final ImageIcon writing;
    String textDirectory;
    private String currentTextEditorDirectory;

    TextEditor(WindowFrame gui){
        this.gui = gui;
        String s = parser.getDestinationPathToFile(new File("EncryptionAlgorithms").getAbsolutePath());
        writing = gui.makeIconFrom(s +"src\\images\\writing.png", 100, 100);
        textDirectory = s+"src\\text";
        currentTextEditorDirectory = textDirectory;
    }


    void openFileWithTextEditor(){
        String directory = gui.chooseFile();
        if(directory == null) return;
        if(directory.equals("")) return;
        if(directory.equals(" ")) return;

        String text = parser.read(directory);
        String filename = parser.getDestinationFileName(directory);
        JTextArea area = setupTextArea();
        area.setText(text);

        textEditorFrame(area, " - "+filename);
    }

    void openEmptyTextEditor(){
        JTextArea area = setupTextArea();
        textEditorFrame(area, "");
    }

    void textEditorFrame(JTextArea area, String filename){
        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setPreferredSize(new Dimension(600, 700));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        JFrame textEditorFrame = new JFrame();
        textEditorFrame.setTitle("Text Editor"+filename);
        textEditorFrame.setVisible(true);
        textEditorFrame.setSize(600, 700);
        textEditorFrame.setIconImage(writing.getImage());

        JMenuBar menubar = new JMenuBar();
        textEditorFrame.setJMenuBar(menubar);

        JMenu file = new JMenu("File");
        menubar.add(file);
        JMenuItem win = new JMenuItem("New Window");
        file.add(win);
        JMenuItem open = new JMenuItem("Open...");
        file.add(open);
        JMenuItem saveProgress = new JMenuItem("Save");
        file.add(saveProgress);
        JMenuItem saveAs = new JMenuItem("Save As...");
        file.add(saveAs);


        addToFrameWithBorderLayout(scrollPane, textEditorFrame);

        class window implements ActionListener {
            public void actionPerformed(ActionEvent e){
                openEmptyTextEditor();
            }
        }
        win.addActionListener(new window());


        class saveFile implements ActionListener{
            public void actionPerformed(ActionEvent e){

                parser.writeToTextFile(area.getText(),
                        currentTextEditorDirectory.substring(0, currentTextEditorDirectory.length()-4));
            }
        }
        saveProgress.addActionListener(new saveFile());

        class saveFileAs implements ActionListener{
            public void actionPerformed(ActionEvent e){
                gui.content = area.getText();
                gui.canSaveAs = true;
                gui.currentExt = "txt";
                gui.saveAs();
            }
        }
        saveAs.addActionListener(new saveFileAs());

        class opener implements ActionListener{
            public void actionPerformed(ActionEvent e){
                if(e.getSource() == open) {
                    String directory = gui.chooseFile();
                    if(directory == null) return;
                    if(directory.equals("")) return;
                    if(directory.equals(" ")) return;

                    textEditorFrame.setTitle("Text Editor - "+ parser.getDestinationFileName(directory));

                    String text = parser.read(directory);

                    currentTextEditorDirectory = directory;

                    area.setText(text);
                }
            }
        }
        open.addActionListener(new opener());
    }



    private JTextArea setupTextArea(){
        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Serif", Font.PLAIN, 20));
        return area;
    }

    private void addToFrameWithBorderLayout(JComponent component, JFrame textEditorFrame){
        JPanel backing = borderLayout();
        backing.add(component, BorderLayout.CENTER);
        textEditorFrame.add(backing);
    }

    JPanel borderLayout(){

        JPanel backing = new JPanel(new BorderLayout());

        JPanel center = new JPanel();
        backing.add(center, BorderLayout.CENTER);
        center.setBorder(new LineBorder(Color.BLACK));

        borderPanelSetting(backing, BorderLayout.WEST);
        borderPanelSetting(backing, BorderLayout.EAST);
        borderPanelSetting(backing, BorderLayout.NORTH);
        borderPanelSetting(backing, BorderLayout.SOUTH);

        return backing;
    }

    private void borderPanelSetting(JPanel backing, String layout){
        JPanel panel = new JPanel();
        panel.setBackground(dark_gray);
        //panel.setBorder(new LineBorder(Color.BLACK));
        backing.add(panel, layout);
    }





}
