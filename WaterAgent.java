import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WaterAgent extends Agent
{
	static int nbW = 0;
	int tsunamiIt = 1;

	public WaterAgent(int __x, int __y, World __w)
	{
		super(__x, __y, __w);
		nbW++;
		try
		{
			img = ImageIO.read(new File("sprites/WaterAgent.png"));
		}
		catch (Exception e)
		{
			System.out.println("WaterAgent : sprite not found");
			System.exit(-1);
		}
	}

	public void step(int place)
	{
		if ((PV <= 0) || (age == age_max))
		{
			PV = 0;
			nbW--;
			_alive = false;
		}
		if (_alive)
		{
			age++;
			attaque_alentour(place);
			repere_environement();
			deplacement();

		}
		else
		{
			try
			{
				img = ImageIO.read(new File("sprites/deathwater.png"));
			}
			catch (Exception e)
			{
				System.out.println("deathwater : sprite not found");
				System.exit(-1);
			}
		}
	}

	void repere_environement()
	{
		if (!_alive)
			return;
		if (_world.getCellState(_x, _y)[3] || _world.getCellState(_x, _y)[5])
		{
			_alive = false;
			nbW--;
			return;
		}
		if (_world.getCellState(_x, _y)[9])
		{
			_world.setCellState(9, false, _x, _y);
			PV += 50;
		}

		if ((_world.getCellState(_x, _y)[4]) && _world.explosion)
		{
			_world.tsunami = true;
			_world.waterFlood(_x, _y, tsunamiIt, _world.explosion);
			tsunamiIt++;
		}
		if ( !_world.explosion && (tsunamiIt != 1) )
		{
			_world.tsunami = false;
			tsunamiIt--;
			_world.waterFlood(_x, _y, tsunamiIt, _world.explosion);
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
			if ((a._x == _x) && (a._y == _y) && (a._alive == true))
			{
				if (a instanceof FireAgent)
					if ((float)Math.random() <= 0.15)PV = PV - 10;
				if (a instanceof EarthAgent)
				{
					PV = PV - 20;
					if ((float)Math.random() <= 0.85)PV = PV - 50;
				}
				if ( (a instanceof WaterAgent) && (place != i)
				        &&  (age < 40) && (age >= 10) && (a.age < 40)
				        && (a.age >= 10) && (a.PV > 20) && (PV > 20) && (nbW < 6))
				{
					boolean test = false;
					while ((j < _world.agents.size()) && (test == false))
					{
						Agent b = _world.agents.get(j);
						if ((b instanceof WaterAgent) && !a._alive)
						{
							b = new WaterAgent(_x, _y, _world);
							test = true;
						}
						j++;
					}
					if (!test)
						_world.add(0);
				}
			}
		}

	}
	void deplacement ()
	{
		int n = (_x - 1 + _world.getWidth()) % _world.getWidth();
		int s = (_x + 1 + _world.getWidth()) % _world.getWidth();
		int e = (_y + 1 + _world.getHeight()) % _world.getHeight();
		int w = (_y - 1 + _world.getHeight()) % _world.getHeight();

		if ((!_alive) || (_world.explosion))
		{
			return;
		}

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
		return;
	}

	boolean authorizedMove (int x, int y)
	{
		if (dangerDetected(x, y) || _world.getCellState(x, y)[1])
			return false;
		return true;
	}

	boolean dangerDetected (int x, int y)
	{
		if (_world.getCellState(x, y)[3] || _world.getCellState(x, y)[5] || _world.getCellState(x, y)[7])
			return true;
		return false;
	}
}
