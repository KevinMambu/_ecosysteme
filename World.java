import java.util.ArrayList;


public class World
{
	boolean explosion;
	int lavaIt;
	boolean tsunami;
	boolean volcan;
	int _dx;
	int _dy;

	boolean Buffer0[][][];
	boolean Buffer1[][][];

	boolean buffering;
	boolean cloneBuffer; // if buffering, clone buffer after swith

	int activeIndex;

	int alt[][];

	ArrayList<Agent> agents;
	SpriteDemo image;

	public World ( int __dx , int __dy, boolean __buffering, boolean __cloneBuffer )
	{
		_dx = __dx;
		_dy = __dy;

		image = new SpriteDemo(_dx, _dy, this);

		volcan = false;
		explosion = false;
		tsunami = false;

		buffering = __buffering;
		cloneBuffer = __cloneBuffer;

		Buffer0 = new boolean[_dx][_dy][10];
		Buffer1 = new boolean[_dx][_dy][10];
		alt = new int[_dx][_dy];

		/*0: herbe*/
		/*1: arbre*/
		/*2: roche volcanique*/
		/*3: magma*/
		/*4: eau*/
		/*5: lave*/
		/*6: tsunami*/
		/*7: volcan*/
		/*8: pousse, a implementer*/
		/*9: fruit, a implementer*/

		activeIndex = 0;
		agents = new ArrayList<Agent>();
		for (int i = 0; i != _dx; i += 1)
		{
			for (int j = 0; j != _dy; j += 1)
			{
				alt[i][j] = 0;
				for (int h = 0; h != 5; h += 1)
				{
					Buffer0[i][j][h] = false;
					Buffer1[i][j][h] = false;
				}
			}
		}

		int radius = _dx / 3;
		int x_r = /*(int)(Math.random() * _dx)*/_dx / 2;
		int y_r = /*(int)(Math.random() * _dy)*/_dy / 2;
		int x_rand;
		int y_rand;
		while (radius >= 0)
		{
			traceCircle (0, true, x_r, y_r, radius);
			radius -= 1;
		}
		/*Generation de l'eau, de la terre, des arbres et du volcan*/
		for (int i = 0; i != _dx; i += 1)
		{
			for (int j = 0; j != _dy; j += 1)
			{
				Buffer0[i][j][4] = true;
				Buffer1[i][j][4] = true;

				if (Buffer0[i][j][0])
				{
					setCellState (1, (Math.random() < 0.1 ? true : false), i, j);
					if (volcan == false)
					{
						setCellState (7, (Math.random() < 0.1 ? true : false), i, j);
						volcan = Buffer0[i][j][7];
					}
					Buffer0[i][j][4] = false;
					Buffer1[i][j][4] = false;

				}
				Buffer1[i][j][1] = Buffer0[i][j][1];
			}
		}

		/*géneration de fruits a l'initialisation*/
		for (int i = 0; i != _dx; i += 1)
		{
			for (int j = 0; j != _dy; j += 1)
			{
				if (Buffer0[i][j][1])
				{
					x_rand = (i + (Math.random() < 0.5? 1 : -1) + _dx) % _dx;
					y_rand = (j + (Math.random() < 0.5? 1 : -1) + _dy) % _dy;
					Buffer0[x_rand][y_rand][9] = true;
				}
			}
		}
		setCellState(9,true,13,13);
	}

	public void checkBounds( int __x , int __y )
	{
		if ( __x < 0 || __x > _dx || __y < 0 || __y > _dy )
		{
			System.err.println("[error] out of bounds (" + __x + "," + __y + ")");
			System.exit(-1);
		}
	}

	public boolean[] getCellState ( int __x, int __y )
	{
		boolean[] res;
		checkBounds (__x, __y);

		if ( buffering == false )
		{
			res = Buffer0[__x][__y];
		}
		else
		{
			if ( activeIndex == 1 ) // read old buffer
			{
				res = Buffer0[__x][__y];
			}
			else
			{
				res = Buffer1[__x][__y];
			}
		}

		return res;
	}

	public void setCellState (int type, boolean bool, int __x, int __y)
	{
		//checkBounds (__x, __y);

		if ( buffering == false )
		{
			Buffer0[__x][__y][type] = bool;
		}
		else
		{
			if ( activeIndex == 0 ) // write new buffer
			{
				Buffer0[__x][__y][type] = bool;
			}
			else
			{
				Buffer1[__x][__y][type] = bool;
			}
		}
	}


	/**
	 * Update the world state and return an array for the current world state (may be used for display)
	 * @return
	 */
	public void step ( )
	{
		analyze ();
		stepWorld ();
		stepAgents ();

		if ( buffering && cloneBuffer )
		{
			if ( activeIndex == 0 )
				for ( int x = 0 ; x != _dx ; x++ )
					for ( int y = 0 ; y != _dy ; y++ )
					{
						Buffer1[x][y] = Buffer0[x][y];
					}
			else
				for ( int x = 0 ; x != _dx ; x++ )
					for ( int y = 0 ; y != _dy ; y++ )
					{
						Buffer0[x][y] = Buffer1[x][y];
					}

			activeIndex = (activeIndex + 1 ) % 2; // switch buffer index
		}

	}

	void analyze ()
	{
		for (int i = 0; i < _dx; i += 1)
		{
			for (int j = 0; j < _dy; j += 1)
			{

				if (getCellState(i, j)[4] && getCellState(i, j)[5])
				{
					setCellState (4, false, i, j);
					setCellState (5, false, i, j);
					setCellState (2, true, i, j);
				}

				if (getCellState(i, j)[1] && getCellState(i, j)[5])
				{
					setCellState (1, false, i, j);
				}

				if (getCellState(i, j)[3] && getCellState(i, j)[4])
				{
					setCellState (3, false, i, j);
					setCellState (4, false, i, j);
					setCellState (2, true, i, j);
				}

				if (getCellState(i, j)[3])
				{
					setCellState(3, false, i, j);
					setCellState(2, true, i, j);
				}

				if (getCellState(i, j)[5])
				{
					setCellState(5, false, i, j);
					setCellState(3, true, i, j);
				}

				if (getCellState(i, j)[0] && getCellState(i, j)[4])
				{
					setCellState(4, false, i, j);
				}

				if (getCellState(i, j)[2] && getCellState(i, j)[4])
				{
					setCellState(4, false, i, j);
				}

				if (getCellState(i, j)[7] && getCellState(i, j)[1])
				{
					setCellState(1, false, i, j);
				}

				if ((Math.random() < 0.01) && getCellState(i, j)[0] && !getCellState(i, j)[2] && !getCellState(i, j)[3] && !getCellState(i, j)[5] && !getCellState(i, j)[6])
				{

				}

			}
		}
	}

	public boolean[][][] getCurrentBuffer()
	{
		if ( activeIndex == 0 || buffering == false )
			return Buffer0;
		else
			return Buffer1;
	}

	public int getWidth()
	{
		return _dx;
	}

	public int getHeight()
	{
		return _dy;
	}

	public void add (int agent)
	{
		// earth agent : 1
		//fire 2
		//water 0
		//wind 3
		int x;
		int y;
		int center = _dx / 2;
		int rayon = _dx / 3;
		switch (agent)
		{
		case 0:
			x = (int)(Math.random () * (2 * rayon) + rayon);
			y = (int)(Math.random () * (2 * rayon) + rayon);
			if (getCellState(x, y)[1])
			{
				if (!getCellState(x, y - 1)[1])
					y -= 1;
				if (!getCellState(x, y + 1)[1])
					y += 1;
				if (!getCellState(x - 1, y)[1])
					x -= 1;
				if (!getCellState(x + 1, y)[1])
					x += 1;
			}
			agents.add(new WaterAgent(x, y, this));
			break;

		case 1:
			x = (int)(Math.random () * (2 * rayon) + rayon)-2;
			y = (int)(Math.random () * (2 * rayon) + rayon)-2;
			agents.add(new EarthAgent(x, y, this));
			break;

		case 2:
			x = (int)(Math.random () * (2 * rayon) + rayon)-2;
			y = (int)(Math.random () * (2 * rayon) + rayon)-2;
			agents.add(new FireAgent(x, y, this));
			break;

		case 3:
			x = (int)(Math.random () * (2 * rayon) + rayon)-2;
			y = (int)(Math.random () * (2 * rayon) + rayon)-2;
			agents.add(new WindAgent(x, y, this));
			break;

		default:
			System.out.println ("World.add : Input Error");
			System.exit (-1);
		}
	}

	public void stepWorld() // world THEN agents
	{
		/*this.display();*/
		image.repaint();
	}

	public void stepAgents() // world THEN agents
	{
		for ( int i = 0 ; i != agents.size() ; i++ )
		{
			Agent a = agents.get(i);
			synchronized ( Buffer0 )
			{
				a.step(i);
			}
		}
	}

	public void display()
	{
		image.update(this.getCurrentBuffer());
	}

	public void Status()
	{
		System.out.println("Status:");
		System.out.println("Size: " + _dx + "x" + _dy);
		System.out.println("\nAgents:");

		Agent a;
		String s = "";

		for (int i = 0; i != agents.size(); i += 1)
		{
			a = agents.get(i);
			if (a instanceof EarthAgent)
				s += "EarthAgent, ";
			else if (a instanceof FireAgent)
				s += "FireAgent, ";
			else if (a instanceof WaterAgent)
				s += "WaterAgent, ";
			else
				s += "WindAgent, ";

			s += a._x + "x" + a._y + ", ";
			s += (a._alive ? "Alive" : "Dead");
			s += ", " + a.PV + " HP, ";
			s += a.age + " yrs.";
			System.out.println(s);
			s = "";
		}

		System.out.println("Arraylist length: " + agents.size());
		System.out.println("\n\n");
	}

	void traceCircle (int type, boolean bool, int x_0, int y_0, int r)
	/*Inspire de l'algorithme de trace de cercle d'Andres*/
	/*Pseudo-code sur Wikipedia*/
	{
		int x = 0;
		int y = r;
		int d = r - 1;

		while (y >= x)
		{
			x = x % _dx;
			y = y % _dy;
			setCellState(type, bool, (x_0 + x + _dx) % _dx, (y_0 + y + _dy) % _dy);
			setCellState(type, bool, (x_0 + y + _dx) % _dx, (y_0 + x + _dy) % _dy);
			setCellState(type, bool, (x_0 - x + _dx) % _dx, (y_0 + y + _dy) % _dy);
			setCellState(type, bool, (x_0 - y + _dx) % _dx, (y_0 + x + _dy) % _dy);
			setCellState(type, bool, (x_0 + x + _dx) % _dx, (y_0 - y + _dy) % _dy);
			setCellState(type, bool, (x_0 + y + _dx) % _dx, (y_0 - x + _dy) % _dy);
			setCellState(type, bool, (x_0 - x + _dx) % _dx, (y_0 - y + _dy) % _dy);
			setCellState(type, bool, (x_0 - y + _dx) % _dx, (y_0 - x + _dy) % _dy);

			if (d >= 2 * x)
			{
				d -= 2 * x + 1;
				x = (x + 1) % _dx;
				x = x % _dx;
			}
			else if (d < 2 * (r - y))
			{
				d += 2 * y - 1;
				y = (y - 1) % _dy;
				y = y % _dy;
			}
			else
			{
				d += 2 * (y - x - 1);
				y = (y - 1) % _dy;
				y = y % _dy;
				x = (x + 1) % _dx;
				x = x % _dx;
			}
		}
	}

	void lavaFlood (int x, int y, int r)
	{
		traceCircle (5, true, x, y, r);
		return;
	}

	void waterFlood (int x, int y, int r, boolean bool)
	{
		traceCircle (6, bool, x, y, r);
		return;
	}
}
