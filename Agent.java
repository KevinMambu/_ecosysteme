import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class Agent{

	World _world;
	boolean _alive;
	int PV;
	int mode;
	Image img;
	int     _x;
	int     _y;
	int     _orient;
	int		age;
	int age_max;

	public Agent( int __x, int __y, World __w)
	{
		_alive = true;
		_x = __x;
		_y = __y;
		_world = __w;

		
		age=0;
		age_max = 100000;
		PV = 100;
		mode = 0;
		_orient = 0;
	}

	abstract public void step(int place );
}

