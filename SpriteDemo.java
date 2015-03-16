

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SpriteDemo extends JPanel
{

	private static final long serialVerionUID = 149;

	private JFrame frame;
	private World _world;

	private int spriteLength = 32;

	private boolean[][][] tab;

	Image grassSprite;
	Image treeSprite;

	public SpriteDemo(int x, int y, World w)
	{
		try
		{
			grassSprite = ImageIO.read(new File("grass.png"));
			treeSprite = ImageIO.read(new File("tree.png"));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(-1);
		}

		frame = new JFrame("World of Sprite");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.setSize(800, 800);
		frame.setVisible(true);

		_world = w;

		tab = new boolean[x][y][6];
	}

	public void update(boolean[][][] src)
	{
		for (int i = 0; i < tab.length; i += 1)
		{
			for (int j = 0; j < tab[0].length; j += 1)
			{
				for (int h = 0; h != 6; h += 1)
				{
					tab[i][j][h] = src[i][j][h];
				}
			}
		}
	}


	public void paint(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;

		this.update(_world.getCurrentBuffer());

		for (int i = 0; i != tab.length; i += 1)
		{
			for (int j = 0; j != tab[0].length; j += 1)
			{
				if (tab[i][j][0] == true)
					g2.drawImage(grassSprite, spriteLength * i, spriteLength * j, spriteLength, spriteLength, frame);
				if (tab[i][j][1] == true)
					g2.drawImage(treeSprite, spriteLength * i, spriteLength * j, spriteLength, spriteLength, frame);
			}
		}
		for ( int i = 0 ; i < _world.agents.size() ; i++ )
		{
			Agent a = _world.agents.get(i);
			g2.drawImage(a.img, spriteLength * (a._x), spriteLength * (a._y), spriteLength, spriteLength, frame);
		}
	}
}


