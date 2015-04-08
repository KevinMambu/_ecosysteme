import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class FireAgent extends Agent
{
	int VolcanoIt = 1;
	int VolcanoRadius = 5;
	static int nbF = 0;


	public FireAgent(int __x, int __y, World __w)
	{
		super(__x, __y, __w);
		nbF++;

		try
		{
			img = ImageIO.read(new File("sprites/FireAgent.png"));
		}
		catch (Exception e)
		{
			System.out.println("FireAgent : sprite not found");
			System.exit(-1);
		}
	}

	public void step(int place)
	{
		if ((PV <= 0) || (age == age_max))
		{
			PV = 0;
			_alive = false;
			nbF--;
		}

		if ((_world.getCellState(_x, _y)[7]) && (_alive))
		{
			_world.explosion = true;
		}

		if (_world.explosion && (VolcanoIt <= VolcanoRadius) && _world.getCellState(_x, _y)[7])
		{
			_world.lavaFlood(_x, _y, VolcanoIt);
			VolcanoIt++;
		}

		if (_world.explosion && (VolcanoIt == VolcanoRadius))
		{
			_world.explosion = false;
			VolcanoIt = 1;
		}

		if (_alive)
		{
			age++;
			attaque_alentour(place);
			deplacement();
			repere_environement();
		}
		else
		{
			try
			{
				img = ImageIO.read(new File("sprites/deathfire.png"));
			}
			catch (Exception e)
			{
				System.out.println("deathfire : sprite not found");
				System.exit(-1);
			}
		}
	}

	void repere_environement()
	{
		if (!_alive)
		{
			return;
		}
		if (_world.getCellState(_x, _y)[4]||_world.getCellState(_x, _y)[6])
		{
			_alive = false;
			nbF--;
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
			if ((a._x == _x) && (a._y == _y) &&(a._alive == true)){
			if (a instanceof WaterAgent) 
			{
				PV = PV - 20;
				if ((float)Math.random() <= 0.85)PV = PV - 50;
			}
			if (a instanceof EarthAgent)
				if ((float)Math.random() <= 0.75)PV = PV - 30;
			if  (a instanceof WindAgent) 
				if ((float)Math.random() <= 0.15)PV = PV - 10;
			if ((a instanceof FireAgent) && (place != i) /*&& (a.age < 40) && (a.age >= 10) && (a.PV > 20)
			    && (age < 40) && (age >= 10)*/ && (PV > 20) && (nbF < 6))
			{
				boolean test = false;
				while ((j < _world.agents.size()) && (test == false))
				{
					Agent b = _world.agents.get(j);
					if ((b instanceof FireAgent) && !a._alive)
					{
						b = new FireAgent(_x, _y, _world);
						test = true;
					}
					j++;
				}
				if (!test)
					_world.add(2);
			}
		}
		}

	}

	void deplacement ()
	{
		int tour = 0;
		if ((!_alive) || (_world.explosion))
			return;
		_orient = (int)(Math.random() * 4);
		while (tour < 2)
		{
			switch ( _orient )
			{
			case 0: // nord

				if ((_world.getCellState(_x, ( _y - 1 + _world.getHeight() ) % _world.getHeight())[1] == true)
				        || (_world.getCellState(_x, ( _y - 1 + _world.getHeight() ) % _world.getHeight())[4] == true)
				        || (_world.getCellState(_x, ( _y - 1 + _world.getHeight() ) % _world.getHeight())[6] == true))
				{
					break;
				}

				else
				{
					_y = ( _y - 1 + _world.getHeight() ) % _world.getHeight();
					return;
				}

			case 1: // est

				if ((_world.getCellState( ( _x + 1 + _world.getHeight() ) % _world.getHeight(), _y)[1] == true)
				        || (_world.getCellState(( _x + 1 + _world.getHeight() ) % _world.getHeight(), _y)[4] == true)
				        || (_world.getCellState(( _x + 1 + _world.getHeight() ) % _world.getHeight(), _y)[6] == true))
				{
					break;
				}

				else
				{
					_x = ( _x + 1 + _world.getWidth() ) % _world.getWidth();
					return;
				}

			case 2: // sud

				if ((_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[1] == true)
				        || (_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[4] == true)
				        || (_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[6] == true))
				{
					break;
				}

				else
				{
					_y = ( _y + 1 + _world.getHeight() ) % _world.getHeight();
					return;
				}

			case 3: // ouest

				if ((_world.getCellState( ( _x - 1 + _world.getHeight() ) % _world.getHeight(), _y)[1] == true)
				        || (_world.getCellState(( _x - 1 + _world.getHeight() ) % _world.getHeight(), _y)[4] == true)
				        || (_world.getCellState(( _x - 1 + _world.getHeight() ) % _world.getHeight(), _y)[6] == true))
				{
					break;
				}

				else
				{
					_x = ( _x - 1 + _world.getWidth() ) % _world.getWidth();
					return;
				}
			}
			tour++;


		}
	}
}
