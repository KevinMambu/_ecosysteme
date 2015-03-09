

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SpriteDemo extends JPanel {

	private static final long serialVerionUID = 149;

	private JFrame frame;
	private World _world;	
	
	private int spriteLength = 32;
	
	private int[][] tab;

	Image grassSprite;
	Image treeSprite;

	public SpriteDemo(int x, int y, World w)
	{
		try
		{
			grassSprite = ImageIO.read(new File("grass.png"));
			treeSprite = ImageIO.read(new File("tree.png"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}

		frame = new JFrame("World of Sprite");
		frame.add(this);
		frame.setSize(800,800);
		frame.setVisible(true);
		
		_world = w;

		tab = new int[x][y];
	}

	public void update(int[][] src) {
		for(int i = 0; i < tab.length; i += 1) {
			for(int j = 0; j < tab[0].length; j += 1) {
				tab[i][j] = src[i][j];
			}
		}
	}

	
	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		this.update(_world.getCurrentBuffer());

		for(int i = 0; i != tab.length; i += 1) {
			for(int j = 0; j != tab[0].length; j += 1) {
					g2.drawImage(grassSprite,spriteLength*i,spriteLength*j,spriteLength,spriteLength, frame);
					if(tab[i][j] == 0)
						g2.drawImage(treeSprite,spriteLength*i,spriteLength*j,spriteLength,spriteLength, frame);
			}
		}
		for ( int i = 0 ; i < _world.agents.size() ; i++ ) {
			Agent a = _world.agents.get(i);
			g2.drawImage(a.img,spriteLength*(a._x),spriteLength*(a._y),spriteLength,spriteLength, frame);
		}
	}
}


