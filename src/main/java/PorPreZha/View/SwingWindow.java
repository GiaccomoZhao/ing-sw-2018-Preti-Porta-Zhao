package PorPreZha.View;

import javax.swing.*;
import java.awt.*;

public class SwingWindow {
    private final String desctiption = "";
    JComponent c;
    SwingWindow() {
        c.setToolTipText(desctiption);
    }
    private void createAndShowGUI() {
        JFrame frame = new JFrame();
        JLabel label = new JLabel("hello");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize( new Dimension(300, 300));

        //        frame.getContentPane().add(label);

        frame.setLayout(new BorderLayout());
        frame.add(label, BorderLayout.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        frame.pack();
        frame.setVisible(true);
    }
}
