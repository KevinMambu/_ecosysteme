import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class EarthAgent extends Agent
{
	boolean enterre;
	static int nbE = 0;

	public EarthAgent(int __x, int __y, World __w)
	{
		super(__x, __y, __w);
		enterre = false;
		nbE++;
		try
		{
			img = ImageIO.read(new File("sprites/EarthAgent.png"));
		}
		catch (Exception e)
		{
			System.out.println("Earth Agent : sprite not found");
			System.exit(-1);
		}
	}

	public void step(int place)
	{
		if ((PV <= 0) || (age == age_max))
		{
			PV = 0;
			_alive = false;
			nbE--;
		}
		if (enterre && _alive)
		{
			deterre();
			return;
		}
		else
		{
			if (_alive)
			{
				age++;
				deplacement();
				repere_environement();
				attaque_alentour(place);
			}
			else
			{
				try
				{
					img = ImageIO.read(new File("sprites/deathearth.png"));
				}
				catch (Exception e)
				{
					System.out.println("deathearth : sprite not found");
					System.exit(-1);
				}
			}
		}
	}

	void repere_environement()
	{
		if (!_alive)
			return;
		if ((_world.getCellState(_x, _y)[4]) || _world.getCellState(_x, _y)[3] || _world.getCellState(_x, _y)[5])
		{
			PV = 0;
			_alive = false;
			nbE--;
			return;
		}

		if (_world.getCellState(_x, _y)[9])
		{
			_world.setCellState(9, false, _x, _y);
			PV += 50;
		}

		int n = (_x - 1 + _world.getWidth()) % _world.getWidth();
		int s = (_x + 1 + _world.getWidth()) % _world.getWidth();
		int e = (_y + 1 + _world.getHeight()) % _world.getHeight();
		int w = (_y - 1 + _world.getHeight()) % _world.getHeight();

		if (_world.explosion)
		{
			if (dangerDetected(_x, e) || dangerDetected(_x, w) || dangerDetected(n, _y) || dangerDetected(s, _y))
			{
				enterre = true;
				try
				{
					img = ImageIO.read(new File("sprites/earthunderground.png"));
				}
				catch (Exception ex)
				{
					System.out.println("earth underground : sprite not found");
					System.exit(-1);
				}
			}
		}
		if (_world.getCellState(_x, _y)[2])
		{
			_world.setCellState(2, false, _x, _y);
			_world.setCellState(0, true, _x, _y);
		}
		return;
	}

	void deterre()
	{
		int n = (_x - 1 + _world.getWidth()) % _world.getWidth();
		int s = (_x + 1 + _world.getWidth()) % _world.getWidth();
		int e = (_y + 1 + _world.getHeight()) % _world.getHeight();
		int w = (_y - 1 + _world.getHeight()) % _world.getHeight();
		if (!dangerDetected(n, _y) || !dangerDetected(s, _y) || !dangerDetected(_x, e) || !dangerDetected(_x, w))
		{
			enterre = false;
			try
			{
				img = ImageIO.read(new File("sprites/EarthAgent.png"));
			}
			catch (Exception ex)
			{
				System.out.println("Earth Agent : sprite not found");
				System.exit(-1);
			}
			return;
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
			if ((a._x == _x) && (a._y == _y) && (a instanceof WaterAgent) && (a._alive == true) )
			{
				if (a instanceof WaterAgent)
					if ((float)Math.random() <= 0.15)
						PV = PV - 10;
				if  (a instanceof WindAgent)
				{
					if ((float)Math.random() <= 0.85)
						PV = PV - 50;
					else
						PV = PV - 20;
				}
				if  (a instanceof FireAgent)
					if ((float)Math.random() <= 0.50)
						PV = PV - 30;
				// si un agent de terre se trouve au meme endroit que lui, que
				if ((a instanceof EarthAgent) && (place != i) && (a.PV > 20) && (nbE < 6))
				{
					boolean test = false;
					while ((j < _world.agents.size()) && (test == false))
					{
						Agent b = _world.agents.get(j);
						if ((b instanceof EarthAgent) && !a._alive)
						{
							b = new EarthAgent(_x - (int)(Math.random() * 3 - 1), _y, _world);
							test = true;
						}
						j++;
					}
					if (!test)
						_world.add(1);
				}
			}
		}
	}

	boolean dangerDetected (int x, int y)
	{
		if (_world.getCellState(x, y)[3] || _world.getCellState(x, y)[5] || _world.getCellState(x, y)[4])
			return true;
		return false;
	}
	boolean authorizedMove (int x, int y)
	{
		if (dangerDetected(x, y) || _world.getCellState(x, y)[1])
			return false;
		return true;
	}

	void deplacement ()
	{
		int n = (_x - 1 + _world.getWidth()) % _world.getWidth();
		int s = (_x + 1 + _world.getWidth()) % _world.getWidth();
		int e = (_y + 1 + _world.getHeight()) % _world.getHeight();
		int w = (_y - 1 + _world.getHeight()) % _world.getHeight();
		if (!_alive)
			return;
		_orient = (int)(Math.random() * 4);
		switch ( _orient )
		{
		case 0: // nord
			if (!authorizedMove(n, _y))
				_x = s;
			else
				_x = n;
			break;


		case 1: // est
			if (!authorizedMove(_x, e))
				_y = w;
			else
				_y = e;
			break;


		case 2: // sud
			if (!authorizedMove(s, _y))
				_x = n;
			else
				_x = s;
			break;


		case 3: // ouest
			if (!authorizedMove(_x, w))
				_y = e;
			else
				_y = w;
			break;
		}
	}
}
