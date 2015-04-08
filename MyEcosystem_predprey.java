
public class MyEcosystem_predprey
{


	public static void main(String[] args)
	{

		// initialisation generale

		/*if (args.length != 5)
		{
			System.out.println("java MyEcosystem_predprey nbWater nbEarth nbFire nbWind size");
			System.exit(-1);
		}*/

		int dx = 20; //Integer.parseInt(args[4]);
		int dy = 20;//Integer.parseInt(args[4]);

		//int reprodWater = 6;
		//int reprodEarth = 10;

		int nbWater =3; //Integer.parseInt(args[0]);
		int nbEarth =3; //Integer.parseInt(args[1]);
		int nbFire =3; //Integer.parseInt(args[2]);
		int nbWind = 3;//Integer.parseInt(args[3]);



		int displayWidth = dx;  // 200
		int displayHeight = dy; // 200

		int delai = 100;//100; // -- delay before refreshing display -- program is hold during delay, even if no screen update was requested. USE WITH CARE.
		int nombreDePasMaximum = Integer.MAX_VALUE;
		int it = 0;

		// initialise l'ecosysteme

		World world = new World(dx, dy, true, true);

		for ( int i = 0 ; i != nbWater ; i++ )
			world.add(0);
		for ( int i = 0 ; i != nbEarth ; i++ )
			world.add(1);
		for ( int i = 0 ; i != nbFire ; i++ )
			world.add(2);
		for ( int i = 0 ; i != nbWind ; i++ )
			world.add(3);

		world.Status();
		// mise a jour de l'etat du monde
		while ( it != nombreDePasMaximum )
		{
			// 1 - display

			// 2 - update

			world.step();

			// 3 - iterate

			it++;

			if (it % 40 == 0)
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
