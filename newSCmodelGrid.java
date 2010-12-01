/*
 * This is the main part of the simulation of the SPC version of the model 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import javax.swing.JOptionPane;
 
public class newSCmodelGrid {

	public ArrayList <newSCCell> tissue;// List of cells that make up the tissue
	private Random rand = new Random();
	public static int maxlineage, nneighbours = 8,stages = 5;//probably static is  unnecessary
	public int gsize;
	public boolean writetogrid = false,precheck=true;
	public static boolean smallcolony=false,forcinggrow=false,othercells = true,random=true,counting = false;
	public int [] SCs,cellclonecount,clonesizecount;
	public int SCcount=0,ncells=0,PMBcount = 0,noncyclingcount = 0;
	public int countcolonies=0,countviable=0;
	double log2 = Math.log(2.0);


	public static int iterspercycle,startcount = 100;// = (int) (1.0/newSCCell.scRate);
	static double gridscrate = newSCCell.scRate;
	public static double cyclingprob = 0.22;//
	public static double otherfrac = .05,scfrac = cyclingprob*gridscrate,type4frac=0.0;
	
	boolean traced = false;
	int iteration = 0;
	newSCCell tracedcell;



	
	public newSCmodelGrid(int size, int maxC, double frac) {//beth: this is the constructor
		//maxc unused but could use for CycleLength
		gridscrate = newSCCell.scRate;
		cyclingprob = frac;
		iterspercycle = (int) (1.0/gridscrate);
		newSCCell.CycleLength = iterspercycle;
		
		// Create new instance of simulation with size of grid maximum SPC cycle and fraction of stem cells 
	    gsize = size;
		SPCBoxStatic[][] grid = new SPCBoxStatic[size][size];
		stages=newSCCell.CycleLength;
        //beth: matrix of dimensions sizexsize containing homes, and called 'grid'
		// Temporary 2D array to hold boxes in Cartesian grid so that connections can be made

		tissue = new ArrayList<newSCCell>();// Creates the list structure for the cells that constitute the tissue
        fillgrid(grid);
        
        findfractions();
        
        fillwithCells(true);
 /*       if (smallcolony) {
        	if (!othercells) makegroupofCells(grid);
        	else makegroupofCells(grid,true);
        }
        else {
        	if (!othercells) fillwithCells();
        	else fillwithCells(true);
        }*/
        //note grid contents will be changed by method bcos I am passing a pointer to the grid 
        //although the grid pointer can't be changed (not that I want to!)
		//beth: at this point there are no holes (type 0 cells).
//		for (int x = 0; x < size; x++) { //  Loop through all the boxes in the grid 
//			for (int y = 0; y < size; y++) {
//		        for (int xx = x - 1; xx <= x + 1; xx++) {
//			        for (int yy = y - 1; yy <= y + 1; yy++) {
//						if((y!=yy)||(x!=xx)) // Form links with their 8 immediate neighbours
//			            grid[x][y].addNeighbour(grid[bounds(xx,size)][bounds(yy,size)]);
//						//This maintains the cartesian relationship between each of the boxes without having to maintain the array
//			        }
//			    }
//			}
//	    } 
		neighbourhood(grid,size,8);//8 neighbours
		//reset the values in the static cell counting arrays
		newSCCell.resetstaticcounters();
       if (writetogrid) {
    	   writeGrid();
    	   writetogrid = false;
       }
       SCs= new int[maxlineage];
       cellclonecount = new int[maxlineage];
       clonesizecount = new int[7];
       newSCCell.counting = counting;
        //setGrowArray();
	}//beth: end of constructor
	

	public newSCmodelGrid(int size, int maxC, double frac,String fname) {//constructor to read from file
		//THIS WON'T WORK BECAUSE RESTSTAGE HASN"T BEEN ADDED YET
		// Create new instance of simulation with size of grid maximum SPC cycle and fraction of stem cells 
		gridscrate = newSCCell.scRate;
		cyclingprob=frac;
		iterspercycle = (int) (1.0/gridscrate);
		stages=newSCCell.CycleLength;
		//maxc unused but could use to set cyclelength
	    gsize = size;
		SPCBoxStatic[][] grid = new SPCBoxStatic[size][size];
        //beth: matrix of dimensions sizexsize containing homes, and called 'grid'
		// Temporary 2D array to hold boxes in Cartesian grid so that connections can be made

		tissue = new ArrayList<newSCCell>();// Creates the list structure for the cells that constitute the tissue
        readGrid(grid,fname);
        //note grid contents will be changed by method bcos I am passing a pointer to the grid 
        //although the grid pointer can't be changed (not that I want to!)
		//beth: at this point there are no holes (type 0 cells).
//		for (int x = 0; x < size; x++) { //  Loop through all the boxes in the grid 
//			for (int y = 0; y < size; y++) {
//		        for (int xx = x - 1; xx <= x + 1; xx++) {
//			        for (int yy = y - 1; yy <= y + 1; yy++) {
//						if((y!=yy)||(x!=xx)) // Form links with their 8 immediate neighbours
//			            grid[x][y].addNeighbour(grid[bounds(xx,size)][bounds(yy,size)]);
//						//This maintains the cartesian relationship between each of the boxes without having to maintain the array
//			        }
//			    }
//			}
//	    } 
		neighbourhood(grid,size,8);//8 neighbours
		//reset the values in the static cell counting arrays
		newSCCell.resetstaticcounters();
		SCs= new int[maxlineage];
	       cellclonecount = new int[maxlineage];
	       clonesizecount = new int[7];
	      // setGrowArray();
	}
	
	public void neighbourhood(SPCBoxStatic[][] grid,int size,int n){
		newSCCell.setneighbourarray(n);
		if (n==8){
		for (int x = 0; x < size; x++) { //  Loop through all the boxes in the grid 
			for (int y = 0; y < size; y++) {
		        for (int xx = x - 1; xx <= x + 1; xx++) {
			        for (int yy = y - 1; yy <= y + 1; yy++) {
						if((y!=yy)||(x!=xx)) // Form links with their 8 immediate neighbours
			            grid[x][y].addNeighbour(grid[bounds(xx,size)][bounds(yy,size)]);
						//This maintains the cartesian relationship between each of the boxes without having to maintain the array
			        }
			    }
			}
	    } 
		}
		else{//then assuming 4 neighbours
			for (int x = 0; x < size; x++) { //  Loop through all the boxes in the grid 
				for (int y = 0; y < size; y++) {
		            grid[x][y].addNeighbour(grid[bounds(x-1,size)][bounds(y,size)]);
		            grid[x][y].addNeighbour(grid[bounds(x+1,size)][bounds(y,size)]);
		            grid[x][y].addNeighbour(grid[bounds(x,size)][bounds(y-1,size)]);
		            grid[x][y].addNeighbour(grid[bounds(x,size)][bounds(y+1,size)]);
				}
			}
		}
		
	}
	
/*	private void setGrowArray(){//set up the permanent grow array, in order

			for (newSCCell c : tissue) { 
				if (c.cycleStage==(stages-1) ) {
					growArray.add(c);
					c.isavail=false;
				}
			}
		for (int i=(stages-2);i>=0;i--){
			for (newSCCell c : tissue) { 
				if ((c.type==1) && (c.cycleStage==i )) {
					growArray.add(c);
				}
			}
		}
		//System.out.println("ga size "+growArray.size());
	}*/
	


	
	private void fillgrid(SPCBoxStatic[][] grid){
		//fill the grid with spaces of lineage 0.
		int x,y,k,lineage=0;
		newSCCell cell;
		for (k = 0; k < gsize; k++) {
			x = k;
        	for(y=0;y<k;y++){
			    grid[x][y] = new SPCBoxStatic(x,y);// New instance of SPCBox created and added to 2D grid
				cell = new newSCCell(grid[x][y],lineage);// New instance of newSCCell created and given unique lineage id
				grid[x][y].occupant = cell;// The new cell is added to the SPCBox
				tissue.add(cell);// Add new cell to list of cells that constitute the tissue
				//lineage++;//uncomment to lineage all
			    grid[y][x] = new SPCBoxStatic(y,x);// New instance of SPCBox created and added to 2D grid
				cell = new newSCCell(grid[y][x],lineage);// New instance of newSCCell created and given unique lineage id
				grid[y][x].occupant = cell;// The new cell is added to the SPCBox
				tissue.add(cell);// Add new cell to list of cells that constitute the tissue
				//lineage++;//uncomment to lineage all
			}
		    grid[k][k] = new SPCBoxStatic(k,k);// New instance of SPCBox created and added to 2D grid
			cell = new newSCCell(grid[k][k],lineage);// New instance of newSCCell created and given unique lineage id
			grid[k][k].occupant = cell;// The new cell is added to the SPCBox
			tissue.add(cell);// Add new cell to list of cells that constitute the tissue
			//lineage++;//uncomment to lineage all
		}
		ncells = gsize*gsize;
	}
	
/*	private void makegroupofCells(SPCBoxStatic[][] grid){
		int x,y,k,radiussq = 10*10,cradiussq;
		int lineage =0;
		newSCCell cell;//just a name to use for each cell as it is placed in a home in the grid


		for (x = 0; x < gsize; x++) {
        	for(y=0;y<gsize;y++){
        		cradiussq = (x-31)*(x-31) + (y-31)*(y-31);
        		if (cradiussq < radiussq) {
        			ncells++;
        			lineage++;
        			cell = grid[x][y].occupant;
        			if (rand.nextDouble()<cyclingprob){
        				cell.type = 1;//cycling
        			    cell.cycleStage = rand.nextInt(stages);
        			}
        			else{
    				    cell.type=2;//not cycling
        			}
    				cell.lineage = lineage;
        		}
        	}
		}
		maxlineage = lineage+1;//or should it be just lineage???
		
	}
	
	private void makegroupofCells(SPCBoxStatic[][] grid,boolean withothers){
		int x,y,k,radiussq = 10*10,cradiussq;
		int lineage =0;
		newSCCell cell;

		double celltype,noncyclingfrac = otherfrac+cyclingprob;

		for (x = 0; x < gsize; x++) {
			for(y=0;y<gsize;y++){
				cradiussq = (x-31)*(x-31) + (y-31)*(y-31);
				if (cradiussq < radiussq) {
					ncells++;
					cell = grid[x][y].occupant;
					celltype = rand.nextDouble();
					if (celltype < otherfrac){//it is a Langerhans etc cell
						cell.type=3;
					}
					else if (celltype >= noncyclingfrac){//non cycling cell
						cell.type = 2;
						lineage++;
						cell.lineage = lineage;
					}
					else{
						cell.type = 1;//cycling
						lineage++;
						cell.lineage = lineage;
						cell.cycleStage = rand.nextInt(stages);
					}

				}
			}
		}
		maxlineage = lineage+1;//or should it be just lineage???

	}
	
	
	private void fillwithCells(){
		int x,y,k;
		int lineage =0;

		//int sc = (int)(gsize*gsize*frac);
		ncells = gsize*gsize;
		double dncells = (double) ncells;
		
        for (newSCCell cell:tissue){
        	lineage++;
			if (rand.nextDouble()<cyclingprob){
				cell.type = 1;//cycling
			    cell.cycleStage = rand.nextInt(stages);
			}
			else{
			    cell.type=2;//not cycling
			}
			cell.lineage = lineage;  	
        }
        maxlineage = lineage+1;
		
	}
	
	*/
	private void findfractions(){
		
	}
	
	private void fillwithCells(boolean withOthers){
		
		int lineage =0,stage=0;
		double celltype,noncyclingfrac = otherfrac+cyclingprob,pmbfrac = 1.0-cyclingprob*newSCCell.rval;
		

		//int sc = (int)(gsize*gsize*frac);

		int ncycling = 0;
		
		for (newSCCell cell:tissue){
			celltype = rand.nextDouble();
			if (celltype < otherfrac){//it is a Langerhans etc cell
				cell.type=3;//lineage will be 0
			}
			else if (newSCCell.type4 && (celltype > pmbfrac)){
				cell.type = 4;
				lineage++;
				cell.lineage = lineage;				
			}
			else if (celltype >= noncyclingfrac){//non cycling cell
				cell.type = 2;
				lineage++;
				cell.lineage = lineage;
				//nonCyclingList.add(cell);
			}
			else{
				ncycling++;
				cell.type = 1;//cycling
				lineage++;
				cell.lineage = lineage;
				stage = rand.nextInt(stages);
				cell.cycleStage = stage;
				if (stage == (stages-1)) cell.canGrow = true;
			}
		}
        maxlineage = lineage+1;
		//System.out.println("ncycling "+ncycling);
	}


	private int bounds(int a,int size) {  // Creates the toroidal links between top and bottom and left and right
		if (a < 0) return size + a;
		if (a >= size) return a - size;
		return a;
	}
	

	public void iterateandcount(boolean printit) {
		//beth: 
		int[] colonies = new int[maxlineage];
		int logval,csc;
		boolean needstogrow;
		ArrayList<newSCCell> growArray = new ArrayList<newSCCell>();
		ArrayList<newSCCell> nonCyclingList = new ArrayList<newSCCell>();

		//int numtocycle = (int) ((cyclingprob/(1.0-otherfrac-cyclingprob))*(double)ncells*newSCCell.scRate);//0.2*ncells*0.22
		int numtocycle = (int) ((1.0-newSCCell.recycleprob)*cyclingprob*(double)ncells*newSCCell.scRate);//0.2*ncells*0.22
		//System.out.println(" ga size "+growArray.size());

		newSCCell cHold,noncyclingcell;
		// Create a list to hold cells that are spaces or have the capacity to detach

		if (!counting){
			if (iteration==startcount) {
				counting = true;
				newSCCell.counting = true;
				relineage();
			}
			iteration++;
		}
		else {
			SCcount=0;
			PMBcount = 0;
			noncyclingcount = 0;
			countcolonies=0;
			countviable=0;
			for (int ii=0;ii<maxlineage;ii++) {
				SCs[ii] = 0;
				cellclonecount[ii] = 0;
			}
			for (int ii=0;ii<7;ii++)clonesizecount[ii] = 0;
		}

		for (newSCCell c : tissue) { // loop through the tissue (ArrayList of cells)
			if (counting){
				colonies[c.lineage] = colonies[c.lineage] | 1;
				cellclonecount[c.lineage]++;
				// in this model all are type 1//if (c.type==1) {
				switch(c.type){
					case 1:
						SCcount ++;
						SCs[c.lineage] = SCs[c.lineage] | 1;
						break;
					case 2:
						noncyclingcount++;
						SCs[c.lineage] = SCs[c.lineage] | 1;
						break;
					case 4:
						PMBcount++;
				}
				/*
				if ((c.type==1)||(c.type==2)) {
					SCs[c.lineage] = SCs[c.lineage] | 1;
				    SCcount++;
				}
				else if (c.type==4) PMBcount++;*/
			}
			//System.out.println("yo");
			//see above }     	
			//c.maintainandcount(); // Calls each cell to maintain its state re: detach and/or grow
			//if(c.type==1)growArray.add(c); // If cell is an SC, add to grow list
			//if(c.canDetach)growArray.add(c);// If cell can detach add to grow list
			if (c.type==2) {
				c.isavail = true;
				nonCyclingList.add(c);
			}
			c.proliferated = false;
			if (c.canGrow) {
				growArray.add(c);
			}
		}
		//System.out.println("gasize "+growArray.size());

		//System.out.println(" grow array size should match nmature "+growArray.size());
		if (counting){
			for(int ii=1;ii<maxlineage;ii++){
				countcolonies = countcolonies+ colonies[ii];
				countviable = countviable+ SCs[ii];
				csc = cellclonecount[ii];
				if (csc > 1){
					logval = (int) (Math.log(csc-1)/log2);
					if (logval > 6) logval = 6;
					clonesizecount[logval]++;
				}
			}
			if (printit) {
				System.out.print(" "+countviable+"  "+countcolonies+"  "+SCcount);
				System.out.print(" "+noncyclingcount);
			}
			//beth: go through the list and see if anything grows into those spots
		}

		//for (int m=0;m<nmigrate;m++){//migrate a fraction of the SC, randomly


		int ndiv = 0,nblocked = 0;
		while(growArray.size()>0){ // loop through the SC ready to divide
			cHold=growArray.remove(rand.nextInt(growArray.size()));
			//randomly choose a noncycling cell to replace
			/*			if (!random) {
				noncyclingcell = cHold.findneighbour(false);
			    if (noncyclingcell != null){
			    	nonCyclingList.remove(noncyclingcell);
					noncyclingcell.growthandcount(cHold);			    	
			    }
			}
			else
			{
				noncyclingcell = nonCyclingList.remove(rand.nextInt(nonCyclingList.size()));
				noncyclingcell.growthandcount(cHold);
			}*/

			noncyclingcell = cHold.findneighbour(false);
			if (noncyclingcell != null){
				noncyclingcell.growthandcount(cHold);
				//nonCyclingList.add(cHold);
				//cHold.isavail = true;
				ndiv++;
			}
			else{
				//blocked
				//System.out.println("here");

				cHold.canGrow=false;// Proliferating cell will not proliferate again in this iteration
				cHold.proliferated=true;// The proliferating cell has proliferated
				cHold.cycleStage = 0;//have a rest now that you have proliferated
				cHold.isavail = false;//The proliferating cell isn't available this iteration either???
				cHold.type=2;//cycling becomes non-cycling
				//nonCyclingList.add(cHold);
				//cHold.isavail = true;

			}

		}


		//System.out.println("ndiv "+ndiv);

		//randomly get more noncycling to join the cycling cells
		newSCCell.nmature = 0;
		for (newSCCell c : tissue) {
			c.ageCell();
		}
		//System.out.println("nmature "+newSCCell.nmature);

		//now change some non-cycling ones to be cycling
		//System.out.println("numtocycle "+numtocycle+" noncycling size "+nonCyclingList.size());
		int ncls = nonCyclingList.size();
		//System.out.println("num to cycle "+numtocycle);
		while(numtocycle>0){
			cHold = nonCyclingList.remove(rand.nextInt(nonCyclingList.size()));
			//			if (!traced){
			//				traced = true;
			//				tracedcell = cHold;
			//			}
			if (cHold.isavail){
				nblocked++;
				cHold.type=1;
				cHold.cycleStage=0;
				cHold.isavail = false;
				cHold.canGrow = false;
				numtocycle--;
			}
		}
		//		if (traced){
		//			System.out.println("traced age "+tracedcell.cycleStage);
		//		}

		//System.out.println("non cycling count "+nonCyclingList.size()+" numtocycle "+" divisions "+ndiv+" removed "+nblocked);


		//System.out.println("nmature "+newSCCell.nmature);
		if (counting){
			if (printit) {
				System.out.print("  "+newSCCell.totalproliferations);

				for (int ii=0;ii<7;ii++){
					System.out.print(" "+clonesizecount[ii]);
				}
				System.out.println();
			}
		}
	}

	public void relineage(){
		int lineage = 1;
		for (newSCCell c:tissue){
			//if (c.type ==1){
			if ((c.type==1)||(c.type==2)){
				c.lineage = lineage++;
			}
			else{
				c.lineage = 0;
			}
		}
		maxlineage = lineage;
	}

public void writeGrid(){
	try{
		BufferedWriter bufGrid = new BufferedWriter(new FileWriter("grid.dat"));
		
        newSCCell cHold;
        for (newSCCell c : tissue) { // loop through the tissue (ArrayList of cells)
			bufGrid.write(c.home.x+" "+c.home.y+" "+c.type+" "+c.lineage);
			bufGrid.newLine();	
	    }

		bufGrid.close();
		System.out.println("Finished writing grid");
	}
	catch(IOException e){
	}
}


public void readGrid(SPCBoxStatic[][] grid, String fname){
	      try{
		  		// Open the file
		  		FileInputStream fstream = new FileInputStream(fname);
		  		// Get the object of DataInputStream
		  		DataInputStream in = new DataInputStream(fstream);
		          BufferedReader br = new BufferedReader(new InputStreamReader(in));
		  		String strLine;
		  		//Read File Line By Line
		  		int i1,i2,i3,i4;
				int fi = 0,li;
				int len;
				int count = 0;
		        newSCCell cell;
                maxlineage = 0;
                
		  		while ((strLine = br.readLine()) != null) 	{
		  			// Print the content on the console
		  			fi = 0;
		  			//System.out.println (strLine);
		  			len = strLine.length();
						li = strLine.indexOf(' ');
						i1 = Integer.parseInt(strLine.substring(fi,li));
						fi = li+1;
						li = strLine.indexOf(' ',fi);
						i2 = Integer.parseInt(strLine.substring(fi,li));
						fi = li+1;
						li = strLine.indexOf(' ',fi);
						i3 = Integer.parseInt(strLine.substring(fi,li));
						fi = li+1;
						i4 = Integer.parseInt(strLine.substring(fi,len));
						grid[i1][i2] = new SPCBoxStatic(i1,i2);
						cell = new newSCCell(grid[i1][i2],i4);
						if (maxlineage < i4) maxlineage = i4;
						grid[i1][i2].occupant = cell;
						cell.type = i3;
						tissue.add(cell);// Add new cell to list of cells that constitute the tissue
		  		}
		  		maxlineage++;
		  		//System.out.println("max lin "+maxlineage);
		  		//Close the input stream
		  		in.close();
		  		}catch (Exception e){//Catch exception if any
		  			System.err.println("Error: " + e.getMessage());
		  		}

}

}//end SPCGridStatic