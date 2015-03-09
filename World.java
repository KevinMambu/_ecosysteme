import java.util.ArrayList;


public class World {

	int _dx;
	int _dy;
	
	int Buffer0[][];
	int Buffer1[][];
	
	boolean buffering;
	boolean cloneBuffer; // if buffering, clone buffer after swith
	
	int activeIndex;
	
	ArrayList<Agent> agents;
	SpriteDemo image;	
	public World ( int __dx , int __dy, boolean __buffering, boolean __cloneBuffer )
	{
		_dx = __dx;
		_dy = __dy;

		image = new SpriteDemo(_dx,_dy, this);
		
		buffering = __buffering;
		cloneBuffer = __cloneBuffer;
		
		Buffer0 = new int[_dx][_dy];
		Buffer1 = new int[_dx][_dy];
		activeIndex = 0;
		agents = new ArrayList<Agent>();
		for(int i = 0; i != _dx; i += 1) {
			for(int j = 0; j != _dy; j += 1) {
				Buffer0[i][j] = 9;
				Buffer1[i][j] = 9;
			}
		}
		
	}
	
	public void checkBounds( int __x , int __y )
	{
		if ( __x < 0 || __x > _dx || __y < 0 || __y > _dy )
		{
			System.err.println("[error] out of bounds ("+__x+","+__y+")");
			System.exit(-1);
		}
	}
	
	public int getCellState ( int __x, int __y )
	{
		int res;
		checkBounds (__x,__y);
		
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
	
	public void setCellState (int type, int __x, int __y)
	{
		checkBounds (__x,__y);
		
		if ( buffering == false )
		{
			Buffer0[__x][__y] = type;
		}
		else
		{
			if ( activeIndex == 0 ) // write new buffer
			{
				Buffer0[__x][__y] = type;
			}
			else
			{
				Buffer1[__x][__y] = type;
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
		stepAgents();
		
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
	
	public int[][] getCurrentBuffer()
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
			synchronized ( Buffer0 ) {
			a.step(i);
				}
		}
	}
	
	public void display()
	{
		image.update(this.getCurrentBuffer());
	}
	
}
