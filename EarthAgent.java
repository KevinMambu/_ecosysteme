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
	static int nbE=0;
	
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
		if (_world.explosion)
			if ((_world.getCellState(_x, (_y - 1) % _world.getHeight())[1] || _world.getCellState(_x, (_y - 1) % _world.getHeight())[3]
			        		|| _world.getCellState(_x, (_y - 1) % _world.getHeight())[4] || _world.getCellState(_x, (_y - 1) % _world.getHeight())[5]
			        		|| _world.getCellState(_x, (_y - 1) % _world.getHeight())[6])
			        && (_world.getCellState(_x, (_y + 1) % _world.getHeight())[1] || _world.getCellState(_x, (_y + 1) % _world.getHeight())[3]
			        		|| _world.getCellState(_x, (_y + 1) % _world.getHeight())[4] || _world.getCellState(_x, (_y + 1) % _world.getHeight())[5]
			        		|| _world.getCellState(_x, (_y + 1) % _world.getHeight())[6])
			        && (_world.getCellState((_x - 1) % _world.getHeight(), _y)[1] || _world.getCellState((_x - 1) % _world.getHeight(), _y)[3]
			        		|| _world.getCellState((_x - 1) % _world.getHeight(), _y)[4] || _world.getCellState((_x - 1) % _world.getHeight(), _y)[5]
			        		|| _world.getCellState((_x - 1) % _world.getHeight(), _y)[6])
			        && (_world.getCellState((_x + 1) % _world.getHeight(), _y)[1] || _world.getCellState((_x + 1) % _world.getHeight(), _y)[3]
			        		|| _world.getCellState((_x + 1) % _world.getHeight(), _y)[4] || _world.getCellState((_x + 1) % _world.getHeight(), _y)[5]
			        		|| _world.getCellState((_x + 1) % _world.getHeight(), _y)[6]))
			{
				enterre = true;
				try
				{
					img = ImageIO.read(new File("earthunderground.png"));
				}
				catch (Exception e)
				{
					System.out.println("earth underground : sprite not found");
					System.exit(-1);
				}
				return;
			}

		if (_world.getCellState(_x, _y)[2])
		{
			_world.setCellState(2, false, _x, _y);
			_world.setCellState(0, true, _x, _y);

		}

	}

	void deterre()
	{
		if (!((_world.getCellState(_x, _y - 1)[1] || _world.getCellState(_x, _y - 1)[3]
		        || _world.getCellState(_x, _y - 1)[4] || _world.getCellState(_x, _y - 1)[5]|| _world.getCellState(_x, _y - 1)[6])
		        && (_world.getCellState(_x, _y + 1)[1] || _world.getCellState(_x, _y + 1)[3]
		            || _world.getCellState(_x, _y + 1)[4] || _world.getCellState(_x, _y + 1)[5]|| _world.getCellState(_x, _y + 1)[6])
		        && (_world.getCellState(_x - 1, _y)[1] || _world.getCellState(_x - 1, _y)[3]
		            || _world.getCellState(_x - 1, _y)[4] || _world.getCellState(_x - 1, _y)[5]|| _world.getCellState(_x - 1, _y)[6])
		        && (_world.getCellState(_x + 1, _y)[1] || _world.getCellState(_x + 1, _y)[3]
		            || _world.getCellState(_x + 1, _y)[4] || _world.getCellState(_x + 1, _y)[5]|| _world.getCellState(_x + 1, _y)[6])

		     ))
		{
			enterre = false;
			try
			{
				img = ImageIO.read(new File("EarthAgent.png"));
			}
			catch (Exception e)
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
			if ((a._x == _x) && (a._y == _y) && (a instanceof WaterAgent) && (a._alive == true) ){
				if(a instanceof WaterAgent)
				if ((float)Math.random() <= 0.15)
					PV = PV - 10;
			if  (a instanceof WindAgent)
			{
				PV = PV - 20;
				if ((float)Math.random() <= 0.85)
					PV = PV - 50;
			}
			if  (a instanceof FireAgent) 
				if ((float)Math.random() <= 0.50)
					PV = PV - 30;
			// si un agent de terre se trouve au meme endroit que lui, que
			if ((a instanceof EarthAgent) && (place != i)/* && (age < 40) && (age >= 10) && (PV > 20)&&
					(a.age < 40) && (a.age >= 10)*/ && (a.PV > 20)&&(nbE<6))
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

	void deplacement ()
	{
		if (!_alive)
			return;
		_orient = (int)(Math.random() * 4);
		switch ( _orient )
		{
		case 0: // nord
			if ((_world.getCellState(_x, ( _y - 1 + _world.getHeight() ) % _world.getHeight())[1] == true)
			        || (_world.getCellState(_x, ( _y - 1 + _world.getHeight() ) % _world.getHeight())[3] == true)
			        || (_world.getCellState(_x, ( _y - 1 + _world.getHeight() ) % _world.getHeight())[4] == true)
			        || (_world.getCellState(_x, ( _y - 1 + _world.getHeight() ) % _world.getHeight())[6] == true)
			        || (_world.getCellState(_x, ( _y - 1 + _world.getHeight() ) % _world.getHeight())[7] == true)
			        || (_world.getCellState(_x, ( _y - 1 + _world.getHeight() ) % _world.getHeight())[5] == true))
				break;

			_y = ( _y - 1 + _world.getHeight() ) % _world.getHeight();
			break;


		case 1: // est
			if ((_world.getCellState( ( _x + 1 + _world.getHeight() ) % _world.getHeight(), _y)[1] == true)
			        || (_world.getCellState( ( _x + 1 + _world.getHeight() ) % _world.getHeight(), _y)[3] == true)
			        || (_world.getCellState(( _x + 1 + _world.getHeight() ) % _world.getHeight(), _y)[4] == true)
			        || (_world.getCellState(( _x + 1 + _world.getHeight() ) % _world.getHeight(), _y)[6] == true)
			        || (_world.getCellState(( _x + 1 + _world.getHeight() ) % _world.getHeight(), _y)[7] == true)
			        || (_world.getCellState( ( _x + 1 + _world.getHeight() ) % _world.getHeight(), _y)[5] == true))
				break;

			_x = ( _x + 1 + _world.getWidth() ) % _world.getWidth();
			break;


		case 2: // sud
				if ((_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[1] == true)
			        || (_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[3] == true)
			        || (_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[4] == true)
			        || (_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[6] == true)
			        || (_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[7] == true)
			        || (_world.getCellState(_x, ( _y + 1 + _world.getHeight() ) % _world.getHeight())[5] == true))
				break;

			_y = ( _y + 1 + _world.getHeight() ) % _world.getHeight();
			break;


		case 3: // ouest
			if ((_world.getCellState( ( _x - 1 + _world.getHeight() ) % _world.getHeight(), _y)[1] == true)
			        || (_world.getCellState( ( _x - 1 + _world.getHeight() ) % _world.getHeight(), _y)[3] == true)
			        || (_world.getCellState(( _x - 1 + _world.getHeight() ) % _world.getHeight(), _y)[4] == true)
			        || (_world.getCellState(( _x - 1 + _world.getHeight() ) % _world.getHeight(), _y)[6] == true)
			        || (_world.getCellState(( _x - 1 + _world.getHeight() ) % _world.getHeight(), _y)[7] == true)
			        || (_world.getCellState( ( _x - 1 + _world.getHeight() ) % _world.getHeight(), _y)[5] == true))
				break;

			_x = ( _x - 1 + _world.getWidth() ) % _world.getWidth();
			break;
		}
	}
}
