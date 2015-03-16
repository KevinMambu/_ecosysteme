
public class MyEcosystem_predprey
{


	public static void main(String[] args)
	{

		// initialisation generale

		if(args.length != 5) {
			System.out.println("java MyEcosystem_predprey nbWater nbEarth nbFire nbWind size");
			System.exit(-1);
		}

		int dx = Integer.parseInt(args[4]);
		int dy = Integer.parseInt(args[4]);

		//int reprodWater = 6;
		//int reprodEarth = 10;

		int nbWater = Integer.parseInt(args[0]);
		int nbEarth = Integer.parseInt(args[1]);
		int nbFire = Integer.parseInt(args[2]);
		int nbWind = Integer.parseInt(args[3]);


		int displayWidth = dx;  // 200
		int displayHeight = dy; // 200

		int delai = 200;//100; // -- delay before refreshing display -- program is hold during delay, even if no screen update was requested. USE WITH CARE.
		int nombreDePasMaximum = Integer.MAX_VALUE;
		int it = 0;

		// initialise l'ecosysteme

		World world = new World(dx, dy, true, true);

		for ( int i = 0 ; i != nbWater ; i++ )
		{
			world.add(new WaterAgent((int)(Math.random() * dx), (int)(Math.random() * dy), world));
		}
		for ( int i = 0 ; i != nbEarth ; i++ )
		{
			world.add(new EarthAgent((int)(Math.random() * dx), (int)(Math.random() * dy), world));
		}
		for ( int i = 0 ; i != nbFire ; i++ )
			world.add(new FireAgent((int)(Math.random() * dx), (int)(Math.random() * dy), world));
		for ( int i = 0 ; i != nbWind ; i++ )
			world.add(new WindAgent((int)(Math.random() * dx), (int)(Math.random() * dy), world));

		world.setCellState(1, true, 0, 0);
		world.setCellState(1, true, 1, 0);
		world.setCellState(1, true, 0, 1);

		world.Status();

		// mise a jour de l'etat du monde
		while ( it != nombreDePasMaximum )
		{
			// 1 - display

			// 2 - update

			world.step();

			// 3 - iterate

			it++;
			
			if(it % 40 == 0)
				world.Status();

			try
			{
				Thread.sleep(delai);
			}
			catch (InterruptedException e)
			{
			}
		}

	}

}
