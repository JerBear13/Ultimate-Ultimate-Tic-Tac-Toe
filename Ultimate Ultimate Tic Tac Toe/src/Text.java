import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class Text {

    /*
     * creats a string of text as an object that can be drawn on to the JPanel
     * fontSize: is the size of the font
     * Color: is the color of the text to be displayed
     * String: the text that you want to have show up
     * x: the x positon for the string to be drawn
     * y: the y position for the string to be drawn
     */
	public void draw(Graphics2D g2, int fontSize, Color color, String Text, int x, int y) {
		Font font = new Font("Roboto", Font.PLAIN, fontSize);
		g2.setColor(color);
		g2.setFont(font);
		g2.drawString(Text, x, y);

	}
}
