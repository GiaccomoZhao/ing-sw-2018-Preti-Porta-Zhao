package PorPreZha.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClickListener implements ActionListener{
    private JPanel panel;
    public ClickListener(JPanel panel) {
        this.panel = panel;
    }

    // TODO: drag picture -> mousedown to pick, mouseup to release, mousemove picture follows
    // TODO: click -> move to set pos

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());   // text contained in the button
        System.out.println(e.getSource());
    }
}
