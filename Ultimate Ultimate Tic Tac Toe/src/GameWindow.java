import javax.swing.JFrame;

public class GameWindow extends JFrame{

    /*
     * creats an instance of GameLoop 
     * sets the size of the window
     * makes it visible
     * makes the program close when the window is closed
     * makes the window unable to change size
     */
    public GameWindow() {
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().add(new GameLoop());
        pack();
    }

    public static void main(String[] args) {
        new GameWindow();
    }
}
