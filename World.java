import java.util.ArrayList;


public class World
{

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

		buffering = __buffering;
		cloneBuffer = __cloneBuffer;

		Buffer0 = new boolean[_dx][_dy][6];
		Buffer1 = new boolean[_dx][_dy][6];
		alt = new int[_dx][_dy];
		/*0: herbe*/
		/*1: arbre*/
		/*2: roche volcanique*/
		/*3: magma*/
		/*4: eau*/
		/*5: lave*/

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
		while (radius >= 0)
		{
			traceCircle (_dx / 2, _dy / 2, radius);
			radius -= 1;
		}

		for (int i = 0; i != _dx; i += 1)
		{
			for (int j = 0; j != _dy; j += 1)
			{
				Buffer0[i][j][4] = true;
				Buffer1[i][j][4] = true;
				if(Buffer0[i][j][0])
					setCellState (1,(Math.random() < 0.1? true : false),i,j);
				Buffer1[i][j][1] = Buffer0[i][j][1];
					
			}
		}
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
		checkBounds (__x, __y);

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
		stepWorld();
		//stepAgents();

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

	public void add (Agent agent)
	{
		agents.add(agent);
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
			System.out.println(s);
			s = "";
		}

		System.out.println("Arraylist length: " + agents.size());
		System.out.println("\n\n");
	}

	void traceCircle (int x_0, int y_0, int r)
	/*Inspire de l'algorithme de trace de cercle d'Andres*/
	/*Pseudo-code sur Wikipedia*/
	{
		int x = 0;
		int y = r;
		int d = r - 1;

		while (y >= x)
		{
			setCellState(0, true, x_0 + x, y_0 + y);
			setCellState(0, true, x_0 + y, y_0 + x);
			setCellState(0, true, x_0 - x, y_0 + y);
			setCellState(0, true, x_0 - y, y_0 + x);
			setCellState(0, true, x_0 + x, y_0 - y);
			setCellState(0, true, x_0 + y, y_0 - x);
			setCellState(0, true, x_0 - x, y_0 - y);
			setCellState(0, true, x_0 - y, y_0 - x);

			if (d >= 2 * x)
			{
				d -= 2 * x + 1;
				x += 1;
			}
			else if (d < 2 * (r - y))
			{
				d += 2 * y - 1;
				y -= 1;
			}
			else
			{
				d += 2 * (y - x - 1);
				y -= 1;
				x += 1;
			}
		}
	}

}
