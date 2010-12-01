/*
 * This contains the classes for the Box object for the SPC version of the model
 * beth: the boxes are called 'home'
 */

import java.util.*;



public class SPCBoxStatic {
	public static Random rand = new Random();//beth: not sure this is used
	
	public newSCCell occupant; // The cell in the box
	public int x,y; // Cartesian coordinates of the box this is only used for graphical display
	public ArrayList<SPCBoxStatic> neighbours;// list of neighbouring Boxes
	
	public SPCBoxStatic(int x, int y) { // Create new instance of the box
		this.x=x;
		this.y=y;
		neighbours  = new java.util.ArrayList<SPCBoxStatic>();//beth: could remove java.util?
	}

	public newSCCell getNeighbour(int n){
		return neighbours.get(n).occupant;// Returns a neighbour of this box
	}

	public void addNeighbour(SPCBoxStatic newNeighbour) {
		this.neighbours.add(newNeighbour);// Adds neighbour to list (see SPCGridStatic)
	}
}


