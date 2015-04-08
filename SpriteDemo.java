

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.io.Serializable;

public class SpriteDemo extends JPanel implements Serializable
{

	private static final long serialVersionUID = 2462347245922742374L;

	private JFrame frame;
	private World _world;

	private int spriteLength = 32;

	private boolean[][][] tab;

	Image grassSprite;
	Image treeSprite;
	Image waterSprite;
	Image tsunamiSprite;
	Image lavaSprite;
	Image magmaSprite;
	Image rockSprite;
	Image volcanoSprite;
	Image treelingSprite;
	Image fruitSprite;

	public SpriteDemo(int x, int y, World w)
	{
		try
		{
			grassSprite = ImageIO.read(new File("sprites/grass.png"));
		}
		catch (Exception e)
		{
			System.out.println("Grass : sprite not found");
			System.exit(-1);
		}
		try
		{
			rockSprite = ImageIO.read(new File("sprites/rock.png"));
		}
		catch (Exception e)
		{
			System.out.println("Volcanic Rock : sprite not found");
			System.exit(-1);
		}
		try
		{
			lavaSprite = ImageIO.read(new File("sprites/lava.png"));
		}
		catch (Exception e)
		{
			System.out.println("Lava : sprite not found");
			System.exit(-1);
		}
		try
		{
			magmaSprite = ImageIO.read(new File("sprites/magma.png"));
		}
		catch (Exception e)
		{
			System.out.println("Magmar : sprite not found");
			System.exit(-1);
		}
		try
		{
			tsunamiSprite = ImageIO.read(new File("sprites/tsunami.png"));
		}
		catch (Exception e)
		{
			System.out.println("Tsunami : sprite not found");
			System.exit(-1);
		}
		try
		{
			treeSprite = ImageIO.read(new File("sprites/tree.png"));
		}
		catch (Exception e)
		{
			System.out.println("Tree : sprite not found");
			System.exit(-1);
		}

		try
		{
			waterSprite = ImageIO.read(new File("sprites/water.png"));
		}
		catch (Exception e)
		{
			System.out.println("Water : sprite not found");
			System.exit(-1);
		}

		try
		{
			fruitSprite = ImageIO.read(new File("sprites/fruit.png"));
		}
		catch (Exception e)
		{
			System.out.println("Fruit : sprite not found");
			System.exit(-1);
		}

		try
		{
			treelingSprite = ImageIO.read(new File("sprites/treeling.png"));
		}
		catch (Exception e)
		{
			System.out.println("Treeling : sprite not found");
			System.exit(-1);
		}

		try
		{
			volcanoSprite = ImageIO.read(new File("sprites/volcano.png"));
		}
		catch (Exception e)
		{
			System.out.println("volcano : sprite not found");
			System.exit(-1);
		}

		frame = new JFrame("Ecosysteme");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(this);
		frame.setSize((spriteLength + 2) * x, (spriteLength + 4) * y);
		frame.setVisible(true);

		_world = w;

		tab = new boolean[x][y][10];
	}

	public void update(boolean[][][] src)
	{
		for (int i = 0; i < tab.length; i += 1)
		{
			for (int j = 0; j < tab[0].length; j += 1)
			{
				for (int h = 0; h != 10; h += 1)
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
				{
					g2.drawImage(grassSprite, spriteLength * i, spriteLength * j, spriteLength, spriteLength, frame);
				}
				if (tab[i][j][1] == true)
				{
					g2.drawImage(treeSprite, spriteLength * i, spriteLength * j, spriteLength, spriteLength, frame);
				}
				if (tab[i][j][2] == true)
				{
					g2.drawImage(rockSprite, spriteLength * i, spriteLength * j, spriteLength, spriteLength, frame);
				}
				if (tab[i][j][3] == true)
				{
					g2.drawImage(magmaSprite, spriteLength * i, spriteLength * j, spriteLength, spriteLength, frame);
				}
				if (tab[i][j][4] == true)
				{
					g2.drawImage(waterSprite, spriteLength * i, spriteLength * j, spriteLength, spriteLength, frame);
				}
				if (tab[i][j][5] == true)
				{
					g2.drawImage(lavaSprite, spriteLength * i, spriteLength * j, spriteLength, spriteLength, frame);
				}
				if (tab[i][j][6] == true)
				{
					g2.drawImage(tsunamiSprite, spriteLength * i, spriteLength * j, spriteLength, spriteLength, frame);
				}
				if (tab[i][j][7] == true)
				{
					g2.drawImage(volcanoSprite, spriteLength * i, spriteLength * j, spriteLength, spriteLength, frame);
				}
				if (tab[i][j][8] == true)
				{
					g2.drawImage(treelingSprite, spriteLength * i, spriteLength * j, spriteLength, spriteLength, frame);
				}
				if (tab[i][j][9] == true)
				{
					g2.drawImage(fruitSprite, spriteLength * i, spriteLength * j, spriteLength, spriteLength, frame);
				}
			}
		}
		for ( int i = 0 ; i < _world.agents.size() ; i++ )
		{
			Agent a = _world.agents.get(i);
			g2.drawImage(a.img, spriteLength * (a._x), spriteLength * (a._y), spriteLength, spriteLength, frame);
		}
	}
}


