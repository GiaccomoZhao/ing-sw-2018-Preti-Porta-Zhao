package porprezha.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Logger;

public class ImageButton extends JPanel implements MouseListener, MouseMotionListener {
    private JLabel label;
    private boolean bClickedDown = false;
    private Point size;  // default image size
    private Point position;
    private String imageSource = "1x5.jpg";

    static Logger logger = Logger.getLogger(ImageButton.class.getName());

/*
    public ImageButton(JLabel label) {
        this.label = label;
    }
*/
    public ImageButton() {
        ImageIcon icon = createImageIcon(imageSource, "red dice number 1");
        this.label = new JLabel("Image text", icon, JLabel.CENTER);
        label.setToolTipText("This is a ImageIcon with text");
        position = new Point(10, 10);
        size = new Point(10, 10);
        this.setPreferredSize(new Dimension(size.x, size.y));

        label.setOpaque(true);
        label.setBackground(Color.BLACK);

        this.add(label);
        addMouseMotionListener(this);
        addMouseListener(this);
        logger.info("ImageButton initialized");
    }


    // TODO: drag picture -> mousedown to pick up, mousemove picture follows, mouseup to place it at nearest designated position
    // TODO: pickup -> set smaller size or add colored border
    @Override
    public void mouseClicked(MouseEvent e) {
        // PICK UP or PLACE DOWN
        bClickedDown = !bClickedDown;
        if(bClickedDown) {
            // Pick Up, set smaller size

        } else {
            // Place Down, return to default size

        }
        logger.info("Clicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // pick up
        logger.info("pressed");
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // move Image
        logger.info("dragged");
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        // place down
        logger.info("released");
    }


    // do nothing
    @Override
    public void mouseMoved(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }



    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path,
                                               String description) {
        java.net.URL imgURL = ImageButton.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ImageButtonTest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new ImageButton());

        //Display the window.
        frame.pack();
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
