package GUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

class WindowOrganizer {
    private final WindowFrame gui;
    int tabNum = 0;
    int openTabs = 0;
    private final int defaultCols = 7;
    final JPanel bar = new JPanel(new GridLayout(1, defaultCols));
    final JPanel space = new JPanel(new BorderLayout());

    private final Color barColor = new Color(110, 110, 108,255);
    private final Color unselectedColor = new Color(138, 138, 134,255);
    private final Color selectedColor = new Color(171, 171, 166,255);

    private final LinkedList<JButton> tabs = new LinkedList<>();

    WindowOrganizer(WindowFrame gui){
        this.gui = gui;
    }

    void tabBar(){
        //this establishes the dimensions of the tab and its delete button
        JButton tab = new JButton("Tab "+tabNum);
        tab.setName("Tab "+tabNum);
        tab.setBackground(unselectedColor);
        tabs.add(tab);

        JPanel buttonBacking = new JPanel(new BorderLayout());
        JButton x = new JButton();
        ImageIcon xImg = gui.makeIconFrom("src/images/x_button.png", 5, 5);
        x.setIcon(xImg);
        x.setName("x "+tabNum);
        x.setBackground(unselectedColor);
        buttonBacking.setPreferredSize(new Dimension(10,20));
        buttonBacking.add(x, BorderLayout.CENTER);

        Dimension upDownBuff = new Dimension(30, 5);

        JPanel northBuffer = new JPanel();
        northBuffer.setPreferredSize(upDownBuff);
        northBuffer.setBackground(unselectedColor);

        JPanel southBuffer = new JPanel();
        southBuffer.setPreferredSize(upDownBuff);
        southBuffer.setBackground(unselectedColor);


        buttonBacking.add(northBuffer, BorderLayout.NORTH);
        buttonBacking.add(southBuffer, BorderLayout.SOUTH);


        JPanel container = new JPanel(new BorderLayout());
        container.setName("Tab "+tabNum);
        container.setBorder(new LineBorder(Color.BLACK, 1, true));
        container.add(tab, BorderLayout.CENTER);
        container.add(buttonBacking, BorderLayout.LINE_END);


        //Organizational and visual logic: How to keep the tabs on the left side
        LinkedList<Component> tabList = new LinkedList<>();
        tabList.add(container);

        //if there are panels in the bar then remove all panels that don't contain the word Tab
        if(bar.getComponents().length  > 0) {
            for (Component c : bar.getComponents()) {
                if (!c.getName().contains("Tab")) bar.remove(c);
            }
        }

        //add new tabs to bar from linked list
        for (Component c : tabList) {
            bar.add(c);
        }

        //add blanks to fill in the space
        char sig = 'a';
        for(int i = tabList.size(); i <= defaultCols - openTabs; i++){
            JPanel blank = new JPanel();
            blank.setName("blank-"+sig);
            blank.setBackground(barColor);
            bar.add(blank);
            sig++;
        }

        gui.refresh();

        class remove implements ActionListener {
            public void actionPerformed(ActionEvent e){
                delete(container, tab);
            }
        }
        x.addActionListener(new remove());

        class select implements ActionListener{
            public void actionPerformed(ActionEvent e){
                choose(tab, e);
            }
        }
        tab.addActionListener(new select());
    }

    private void delete(JPanel tabContainer, JButton tab){
        bar.remove(tabContainer);

        //removes the selected tab by name
        for(Component c : space.getComponents()){
            if(c.getName().equals(tab.getName())){
                space.remove(c);
            }
        }

        //if no tab is selected then set the last window in space to visible and its tab to selected
        Component[] list = space.getComponents();
        if(list.length > 0 && tab.getBackground().equals(selectedColor)){
            list[list.length-1].setVisible(true);
        }

        //adjust count of open tabs
        openTabs--;

        //fill in the blank spots once the number of tabs gets below the number of default columns
        if(openTabs-1 < defaultCols){
            char sig = 'a';
            JPanel blank = new JPanel();
            blank.setName("blank-"+sig);
            blank.setBackground(barColor);
            bar.add(blank);
        }


        gui.refresh();
    }

    private void choose(JButton tab, ActionEvent e){
        tab.setBackground(selectedColor);
        JButton button = (JButton) e.getSource();

        for(Component pair : bar.getComponents()){
            if(!pair.getName().equals(button.getName()))  {
                Component c = pair.getComponentAt(1,1);
                if(c.getName().contains("Tab")) {
                    c.setBackground(unselectedColor);
                }
            }
        }

        for(Component panel : space.getComponents()){
            panel.setVisible(panel.getName().equals(tab.getName()));
        }

        gui.refresh();
    }

    int currentTabNum = -1;
    void window(){
        currentTabNum = tabNum;

        InnerWindow inner = new InnerWindow(gui, currentTabNum);

        JPanel window = inner.root();

        JPanel windowContainer = new JPanel(new BorderLayout());

        windowContainer.setName("Tab "+currentTabNum);
        windowContainer.add(window);

        addSpace(windowContainer);

        gui.refresh();
    }

    void addSpace(JPanel window){
        space.add(window);
    }

    void setTabText(String text, int ID){
        for(JButton tab : tabs){
            if(tab.getName().equals("Tab "+ID)){
                tab.setText(text);
            }
        }
    }

    void setTabIcon(ImageIcon icon, int ID){
        for(JButton tab : tabs){
            if(tab.getName().equals("Tab "+ID)){
                tab.setIcon(icon);
            }
        }
    }

}
