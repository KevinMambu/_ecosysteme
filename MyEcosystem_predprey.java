
public class MyEcosystem_predprey {


	public static void main(String[] args) {

		// initialisation generale
	    
		int dx = 8;
		int dy = 8;
		
		//int reprodWater = 6;
		//int reprodEarth = 10;
		
		int nbWater = 1;
		int nbEarth = 1;
		int nbFire = 1;
		int nbWind = 1;


		int displayWidth = dx;  // 200
		int displayHeight = dy; // 200

		int delai = 200;//100; // -- delay before refreshing display -- program is hold during delay, even if no screen update was requested. USE WITH CARE. 
		int nombreDePasMaximum = Integer.MAX_VALUE;
		int it = 0;
		
	    // initialise l'ecosysteme
	    
		World world = new World(dx,dy,true,true);
		
		for ( int i = 0 ; i != nbWater ; i++ ) {
			world.add(new WaterAgent((int)(Math.random()*dx),(int)(Math.random()*dy),world));
			System.out.println("Prey: "+world.agents.get(world.agents.size() - 1)._x + "," + world.agents.get(world.agents.size() - 1)._y);
		}
		for ( int i = 0 ; i != nbEarth ; i++ ) {
			world.add(new EarthAgent((int)(Math.random()*dx),(int)(Math.random()*dy),world));
			System.out.println("Predator: "+world.agents.get(world.agents.size() - 1)._x + "," + world.agents.get(world.agents.size() - 1)._y);
		}
		for ( int i = 0 ; i != nbFire ; i++ ) 
			world.add(new FireAgent((int)(Math.random()*dx),(int)(Math.random()*dy),world));
		for ( int i = 0 ; i != nbWind ; i++ ) 
			world.add(new WindAgent((int)(Math.random()*dx),(int)(Math.random()*dy),world));

		world.setCellState(0,0,0);
		world.setCellState(0,1,0);
		world.setCellState(0,0,1);


	    // mise a jour de l'etat du monde	
		while ( it != nombreDePasMaximum )
		{
			// 1 - display

			// 2 - update
						
			world.step();
						
			// 3 - iterate
			
			it++;
			
			try {
				Thread.sleep(delai);
			} catch (InterruptedException e) 
			{
			}
		}
		
	}

}
