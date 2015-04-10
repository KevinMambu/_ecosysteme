import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class WindAgent extends Agent
{

	static int nbWind = 0;

	public WindAgent(int __x, int __y, World __w)
	{
		super(__x, __y, __w);
		nbWind++;
		try
		{
			img = ImageIO.read(new File("sprites/WindAgent.png"));
		}
		catch (Exception e)
		{
			System.out.println("WindAgent : sprite not found");
			System.exit(-1);
		}
	}

	public void step (int place)
	{
		if ((PV <= 0) || (age == age_max))
		{
			PV = 0;
			_alive = false;
			nbWind--;
		}

		if (_alive)
		{
			age++;
			attaque_alentour(place);
			repere_environement();
			deplacementChasse();
		}
		else
		{
			try
			{
				img = ImageIO.read(new File("sprites/deathwind.png"));
			}
			catch (Exception e)
			{
				System.out.println("deathwind : sprite not found");
				System.exit(-1);
			}
		}
	}

	void repere_environement()
	{
		if (!_alive)
			return;
		if (_world.getCellState(_x, _y)[9])
		{
			_world.setCellState(9, false, _x, _y);
			PV += 50;
		}
	}


	void attaque_alentour (int place)
	{
		if (!_alive)
			return;
		int j = 0;
		for (int i = 0; i != _world.agents.size(); i += 1)
		{
			Agent a = _world.agents.get(i);
			if ((a._x == _x) && (a._y == _y) &&  (a._alive == true)){
			if  (a instanceof FireAgent)
			{
				PV = PV - 20;
				if ((float)Math.random() <= 0.85)PV = PV - 50;
			}
			if (a instanceof EarthAgent) 
				if ((float)Math.random() <= 0.15)PV = PV - 10;
			if ((a instanceof WindAgent) && (place != i) &&/* (age < 40) && (age >= 10) &&(a.age < 40)
					&& (a.age >= 10) &&*/ (a.PV > 20)&&(PV > 20)&&(nbWind<6))
			{
				boolean test = false;
				while ((j < _world.agents.size()) && (test == false))
				{
					Agent b = _world.agents.get(j);
					if ((b instanceof WindAgent) && !a._alive)
					{
						b = new WindAgent(_x, _y, _world);
						test = true;
					}
					j++;
				}
				if ((!test) && (nbWind >= 5))
					_world.add(3);
			}
		}
		}

	}


	void deplacement ()
	{
		if (!_alive)
			return;


		_orient = (int)(Math.random() * 4);
		switch ( _orient )
		{

		case 0: // nord
			_y = ( _y - 1 + _world.getHeight() ) % _world.getHeight();
			break;

		case 1: // est
			_x = ( _x + 1 + _world.getWidth() ) % _world.getWidth();
			break;

		case 2: // sud
			_y = ( _y + 1 + _world.getHeight() ) % _world.getHeight();
			break;

		case 3: // ouest
			_x = ( _x - 1 + _world.getWidth() ) % _world.getWidth();
			break;
		}

	}

	void deplacementChasse()
	{
		if (!_alive)
			return;

		int x = 1000;
		int y = 1000;
		Agent b = null;

		for (int i = 0; i != _world.agents.size(); i += 1)
		{
			Agent a = _world.agents.get(i);
			if ((a instanceof FireAgent) && (a._alive) && (Math.abs(x - _x) >= Math.abs(a._x - _x)) && (Math.abs(y - _y) >= Math.abs(a._y - _y)))
			{
				b = a;
				x = a._x;
				y = a._y;
			}
		}


		if (b != null)
		{
			_orient = b._orient;
			if (b._x < _x)
				_x = _x - 1;
			else if (b._y < _y)
				_y = _y - 1;
			else if (b._x > _x)
				_x = _x + 1;
			else if (b._y > _y)
				_y = _y + 1;

		}
		else
		{
			_orient = (int)(Math.random() * 4);
			switch ( _orient )
			{
			case 0: // nord
				_y = ( _y - 1 + _world.getHeight() ) % _world.getHeight();
				break;
			case 1: // est
				_x = ( _x + 1 + _world.getWidth() ) % _world.getWidth();
				break;
			case 2: // sud
				_y = ( _y + 1 + _world.getHeight() ) % _world.getHeight();
				break;
			case 3: // ouest
				_x = ( _x - 1 + _world.getWidth() ) % _world.getWidth();
				break;
			}
		}

	}
}
