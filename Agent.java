
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class Agent {

	World _world;
	boolean _alive;	
	static int redId   = 0;
	static int greenId = 1;
	static int blueId  = 2;
	
	int PV;
	int reproduction;
	int mode;
	Image img;	
	int 	_x;
	int 	_y;
	int 	_orient;
	//int 	_etat;
	int reproduce_it;
	int 	_redValue;
	int 	_greenValue;
	int 	_blueValue;
	
	public Agent( int __x, int __y, World __w)
	{
		_alive = true;
		_x = __x;
		_y = __y;
		_world = __w;
		
		_redValue = 255;
		_greenValue = 0;
		_blueValue = 0;
		
		//reproduction=0;
		PV=100;
		mode=0;
		_orient = 0;
	}
	
	abstract public void step(int place );
	
}
