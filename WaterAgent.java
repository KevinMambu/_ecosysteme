import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class WaterAgent extends Agent{



	public WaterAgent(int __x, int __y, World __w) {
		super(__x, __y, __w);
		// TODO Auto-generated constructor stub
		_redValue = 0;
		_greenValue = 0;
		_blueValue = 255;
		
		try {
			img = ImageIO.read(new File("WaterAgent.png"));
		} catch(Exception e) {
			System.out.println("image introuvable");
		}
	}
	
	 public void step(int place){
		 if(_alive){
		 PV=PV-1;
		if (reproduction==20)reproduction=0;
		else if (reproduction>=1)reproduction++;
		 attaque_alentour(place);
		 if (PV<=0)_alive=false;
		repere_environement();
		deplacementRandom();
		 }
	 }
	
	 void repere_environement(){ 
	 }

	 void attaque_alentour (int place){
		 int j=0;
		 for(int i = 0; i != _world.agents.size(); i += 1) {
				Agent a = _world.agents.get(i);
				if((a._x == _x) && (a._y == _y) && (a instanceof FireAgent) && (a._alive=true))
					if((float)Math.random()<=0.15)PV=PV-10;
				/*if((a._x == _x) && (a._y == _y) && (a instanceof WindAgent) && (a._alive=true)) 
					if((float)Math.random()<=0.75)PV=PV-30;*/
				if((a._x == _x) && (a._y == _y) && (a instanceof EarthAgent)&& (a._alive=true) ) {
					PV=PV-20;
					if((float)Math.random()<=0.85)PV=PV-50;
				}
				if((a._x == _x) && (a._y == _y) && (a instanceof WaterAgent)&&(place!=i)&& (a._alive=true) && (reproduction==0)){ 
					reproduction=1;
					boolean test=false;
					while((j < _world.agents.size())&&(test==false)) {
						Agent b= _world.agents.get(j);
						if((b instanceof WaterAgent) && !a._alive){
							b = new WaterAgent(_x,_y,_world);
							test=true;
						}
						j++;
					}
					if(!test)
						_world.add(new WaterAgent(_x,_y,_world));
				}
		}
	
	}

	 void deplacementRandom (){
		_orient= (int)(Math.random()*4);
		switch ( _orient ) 
		{
	   	case 0: // nord	
				_y = ( _y - 1 + _world.getHeight() ) % _world.getHeight();
	   		break;
     	case 1:	// est
		 		_x = ( _x + 1 + _world.getWidth() ) % _world.getWidth();
				break;
	   	case 2:	// sud
    		_y = ( _y + 1 + _world.getHeight() ) % _world.getHeight();
				break;
			case 3:	// ouest
				_x = ( _x - 1 + _world.getWidth() ) % _world.getWidth();
			break;
			}
		
	 }

}
