package porprezhas.view;

import com.sun.deploy.panel.JavaPanel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;

public class ClientGame {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new GameFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Display the window.
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);

                createAndShowGUI();
            }
        });
    }
}

class GameFrame extends JFrame {
    final private int WIDTH = 780;
    final private int HEIGHT= 500;
    public GameFrame() {
        //Set window size
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
//        frame.setPreferredSize(new Dimension(screenWidth, screenHeight));
        setSize(new Dimension(WIDTH, HEIGHT));
        setResizable(true);

        this.setTitle("Sagrada family");

        //Add content to the window.
        JPanel toolBar = new JPanel();
        toolBar.setBackground(Color.gray);
        toolBar.setPreferredSize(new Dimension(WIDTH-200, 48));
        Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED,
                new Color(45, 92, 162),
                new Color(120, 200, 162),
                new Color(0, 92, 0),
                new Color(125, 0, 0));
        toolBar.setBorder(border);



        JPanel actionPanel = new JPanel();
        actionPanel.setBackground(Color.black);
        actionPanel.setPreferredSize(new Dimension(160, HEIGHT));

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.darkGray);
        contentPanel.setPreferredSize(new Dimension(620, 380));

        getContentPane().add(actionPanel, BorderLayout.EAST);
        getContentPane().add(toolBar, BorderLayout.NORTH);
        getContentPane().add(contentPanel, BorderLayout.CENTER);
    }
}