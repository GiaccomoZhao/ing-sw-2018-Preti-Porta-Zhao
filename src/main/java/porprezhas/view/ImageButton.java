package porprezhas.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

public class ImageButton extends JPanel implements MouseListener, MouseMotionListener {
    static private ImageButton testButton;
//    static private JPanel gamePanel;
    private JLabel label;
    private String imageSource = "TestImage.jpg";
    private boolean bClickedUp = false;
//    private Point size;  // default image size
//    private Point position;
//    private List<Point> defaultPositionList;
    private Point defaultPosition;
    static final int posizionateArea = 30;


    static Logger logger = Logger.getLogger(ImageButton.class.getName());

/*
    public ImageButton(JLabel label) {
        this.label = label;
    }
*/
    public ImageButton(Rectangle rectangle) {
//        panel = new JPanel();
        this.defaultPosition = new Point(rectangle.x, rectangle.y);
//        this.currentPosition = defaultPosition;
        ImageIcon icon = createImageIcon(imageSource, "red dice number 1");
        if(rectangle.width > 0 && rectangle.height > 0) {
            icon.setImage(getScaledImage(icon.getImage(), rectangle.width, rectangle.height));
        }
        label = new JLabel(icon);//("Image text", iconId, JLabel.CENTER);
        label.setToolTipText("This is a ImageIcon with text");
//        position = new Point(10, 10);
//        size = new Point(100, 100);
//        this.setPreferredSize(new Dimension(size.x, size.y));

        label.setOpaque(true);
        label.setBackground(Color.BLACK);
//        panel.add(label);
        this.add(label);

        label.addMouseMotionListener(this);
        label.addMouseListener(this);

        label.setLocation(defaultPosition);
//        screenToClientPosition = new Point();
    }

    void repositionate() {
        label.setLocation(defaultPosition);
    }

    Point location;
    MouseEvent pressed;

    public void mousePressed(MouseEvent e)
    {
        pressed = e;
    }

    public void mouseDragged(MouseEvent e)
    {
        Component component = e.getComponent();
        location = component.getLocation(location);
        int x = location.x - pressed.getX() + e.getX();
        int y = location.y - pressed.getY() + e.getY();
        component.setLocation(x, y);
    }
/*
//    private Point clickPosition;
    private Point viewPositionOnClient;
    private Point screenToClientPosition;   // = window position + title bar height;
//    static private int titleHeight;

    @Override
    public void mousePressed(MouseEvent e) {
        // pick up
        viewPositionOnClient =  SwingUtilities.convertPoint(e.getComponent(), new Point(0,0), this);
        System.out.println(viewPositionOnClient);
        screenToClientPosition.x = -( e.getXOnScreen() - viewPositionOnClient.x);
        screenToClientPosition.y = -( e.getYOnScreen() - viewPositionOnClient.y);
//        clickPosition.setLocation(e.getLocationOnScreen().getX() - e.getXOnScreen() + e.getPoint().getX(),
//                                          e.getLocationOnScreen().getY() - e.getYOnScreen() + e.getPoint().getY());
        logger.info("Pressed at x = " + viewPositionOnClient.getX() + "\t y = " + viewPositionOnClient.getY());
        logger.info("Window  at x = " + screenToClientPosition.getX() + "\t y = " + screenToClientPosition.getY());
//        System.out.println(label.getInsets());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // move Image
        logger.info("Dragged to cx = " + (e.getXOnScreen() + screenToClientPosition.x) +
                                  "\tcy = " + (e.getYOnScreen() + screenToClientPosition.y));
        label.setLocation(e.getXOnScreen() + screenToClientPosition.x,
                          e.getYOnScreen() + screenToClientPosition.y);
    }
*/

    @Override
    public void mouseReleased(MouseEvent e) {
        // place down
        Component component = e.getComponent();
        Point position = component.getLocation();
//        logger.info("Released at x = " + e.getY() + "\t y = " + e.getY());
        if(   Math.abs( position.x - defaultPosition.x ) <= posizionateArea
           && Math.abs( position.y - defaultPosition.y ) <= posizionateArea)
            repositionate();
    }


    // TODO: drag picture -> mousedown to pick up, mousemove picture follows, mouseup to place it at nearest designated position
    // TODO: pickup -> set smaller size or add colored border
    @Override
    public void mouseClicked(MouseEvent e) {
        // PICK UP or PLACE DOWN
        bClickedUp = !bClickedUp;
        if(bClickedUp) {
            // Pick Up, set smaller size

        } else {
            // Place Down, return to default size

        }
//        clickPosition = e.getPoint();
//        logger.info("Pressed at x = " + clickPosition.getX() + "\t y = " + clickPosition.getY());
        repositionate();
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


    /**
     * Creates an ImageIcon if the path is valid.
     * @param path :String - resource path
     * @param description :String - description of the file
     */
    protected ImageIcon createImageIcon(String path,
                                        String description) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     * @param srcImg - source image to scale
     * @param w - desired width
     * @param h - desired height
     * @return - the new resized image
     */
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
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

        //Set window size
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        frame.setPreferredSize(new Dimension(screenWidth/2, screenHeight/2));

        //Add content to the window.
//        gamePanel = new JPanel();
        testButton = new ImageButton(new Rectangle(200, 100, 48, 48));
        frame.add(testButton);

        //Display the window.
        frame.pack();
        frame.setVisible(true);

        System.out.println(frame.getInsets());
//        titleHeight = frame.getInsets().top - 0;
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
