package PorPreZha.View;

import javax.swing.*;

public class Gui {
    private JFrame frame;
    private JButton button;
    private JTextField username, token, post;
    private JTextArea feed;

    public void init() {
        frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        button = new JButton("button1");
        username = new JTextField();
        token = new JTextField();
        post = new JTextField();
        feed = new JTextArea();

        JPanel panel = new JPanel();
        panel.add(username);
        panel.add(token);
    }
}
