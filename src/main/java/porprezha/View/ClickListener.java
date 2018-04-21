package porprezha.view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClickListener implements ActionListener{
    private JPanel panel;
    public ClickListener(JPanel panel) {
        this.panel = panel;
    }

    // TODO: drag picture -> mousedown to pick up, mousemove picture follows, mouseup to place it at nearest designated position
    // TODO: click -> move to default position

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());   // text contained in the button
        System.out.println(e.getSource());
    }
}
