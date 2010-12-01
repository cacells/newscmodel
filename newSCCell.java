/*
 * This contains the classes for the Cell object for the SPC version of the model
 */

import java.util.*;
 
class newSCCell{
	public static int cellTypes = 3; // number of different cell types
	public static Random rand = new Random();
	public static double rval = 0.5;//.5;//usually 0.08
	public static double TAFraction = 1.0 - 2.0*rval;// The probability of a producing EP and PMB
	public static int CycleLength = 5;
	static Integer[] neighbours;//set using setneighbours from gridstatic

	public boolean canGrow,proliferated,isavail;
	public int cycleStage;
	public int type; // 0 = space, 1 = EP, 2=PMB
	public SPCBoxStatic home;// The box the cell sits in
	public int age;
	public static double scRate=0.2;// Relative SC proliferation rate if scRate = 0.5 SC proliferation rate would be half SPC rate
	public int lineage;
	
	public static int[] cellcounts=new int[cellTypes+1];
	public static int[] prolifcounts=new int[cellTypes+1];
	public static double[] agesums=new double[cellTypes+1];
	public static int totalproliferations=0;
	public static int nmature = 0;
	public static boolean recycle = false,type4=true,counting = false;
	public static double recycleprob = 0.0;
	
	public newSCCell(SPCBoxStatic home,int lin){
		this.home=home;
		lineage = lin;
		canGrow=false;
		proliferated=false;
		cycleStage = 0;//reset during gris set-up
		isavail = false;//could calc it here but why waste time?(cycleStage==(restTime-1));
		age = 0;
		type = 0;//defaults to a space
	}
	
	public static void setneighbourarray(int n){
		neighbours = new Integer[n];
		for (int i=0;i<n;i++) neighbours[i] = i;
	}
	
	public static void resetstaticcounters(){
		for (int i=0;i<cellTypes+1;i++){
			cellcounts[i]=0;
			prolifcounts[i]=0;
			agesums[i]=0.0;
			totalproliferations = 0;
			nmature = 0;
		}
	}
	
	
	public void maintainandcount(){// Determines if a Cell can detach or grow and counts
		cellcounts[type]++;
		agesums[type] = agesums[type] + age;
		//canDetach=(type==cellTypes);// For standard SPC model only PMB can detach
		switch(type){
		case 0: //spaces
			canGrow=false;
			proliferated=false;
			isavail = true;//this space may be taken
			//age = 0; not using age at the moment	
			break;
		case 1: //cycling cells
			if (cycleStage >= (CycleLength-1)){
				canGrow = true; 
			}
			else canGrow = false;
			proliferated=false;
			isavail = false;//this space may not be taken
			//age = 0; not using age at the moment	
			break;
		case 2: //not currently cycling
			canGrow = false;
			proliferated=false;
			isavail = true;//this space may be taken
			//age = 0; not using age at the moment	
			break;			
		default://Langerhans cells etc
			canGrow=false;
			proliferated=false;
			isavail = false;//this space may not be taken	
		}

	}
	
	public int ageCell(){
		if (type==1){
			cycleStage = cycleStage + 1;
			if (cycleStage >= (CycleLength-1)) {
				canGrow = true;
				nmature++;
			}
			else canGrow = false;
		}
		return nmature;
		//cycleStage = (cycleStage + 1)%restTime;		
	}

	public void growthandcount(newSCCell cHold){
		//cHold is the SC, and this is the migrating cell
		if (counting){
			totalproliferations++;
			prolifcounts[cHold.type]++;
		}
		cycleStage = 0;
		lineage = cHold.lineage;//new clone ID
		age = 0;//not using age
		isavail = false;//new cell: not available: this is important	
		canGrow=false;// can't proliferate
		if (recycle && (rand.nextDouble() < recycleprob)){
			type = 1;//go straight back to being a cycling cell
		}
		else{
			type = 2;//not a cycling cell
		}
		cHold.canGrow=false;// Proliferating cell will not proliferate again in this iteration
		cHold.proliferated=true;// The proliferating cell has proliferated
		cHold.age = age;// not using age
		cHold.cycleStage = 0;//have a rest now that you have proliferated
		cHold.isavail = false;//The proliferating cell isn't available this iteration either???
		cHold.type = 2;
		if (type4 && rand.nextDouble()< rval) {
			cHold.type = 4;
		}
		/*
		if (type4){
			if (rand.nextDouble()< TAFraction) {
				type = 2;
				cHold.type = 4;
			}
			else{
				if (rand.nextDouble()< 0.5){
					type = 4;
					cHold.type = 4;
				}
				else{
					type = 2;
					cHold.type=2;//cycling becomes non-cycling
				}
			}
		}*/

	}
	

//	public boolean migrateandcountprecheck(boolean forcinggrow){
//		int sizeA = neighbours.length;
//		ArrayList<Integer> nlist = new ArrayList<Integer>(Arrays.asList(neighbours));//initialise nlist
//		int a,b;
//		newSCCell cHold;
//
//			//find an available neighbour
//			for(int i=0;i<sizeA;i++){ // Loop from starting point through list of neighbours
//				a = rand.nextInt(nlist.size());//pick random list index
//				b = nlist.remove(a);//use the value at that index and make the list smaller
//				cHold = home.getNeighbour(b);
//				if(cHold.canGrow){ // If neighbour is available for prolif
//					growthandcount(cHold);// neighbour takes over
//                    return true;
//				}
//			}
//
//		return false;// Return false if no proliferating cell can be found 
//	}
	public boolean growandcount(boolean forcinggrow){
		//grow if you find an available neighbour
		int sizeA = neighbours.length;
		ArrayList<Integer> nlist = new ArrayList<Integer>(Arrays.asList(neighbours));//initialise nlist
		int a,b;
		newSCCell cHold;

			//find an available neighbour
			for(int i=0;i<sizeA;i++){ // Loop from starting point through list of neighbours
				a = rand.nextInt(nlist.size());//pick random list index
				b = nlist.remove(a);//use the value at that index and make the list smaller
				cHold = home.getNeighbour(b);
				if(cHold.isavail){ // If neighbour is available for takeover
					cHold.growthandcount(this);// this takes over the neighbour
                    return true;
				}
			}
        //what should be done if no neighbour is found?
		return false;// Return false if no space can be found 
	}
	public newSCCell findneighbour(boolean forcinggrow){
		//grow if you find an available neighbour
		int sizeA = neighbours.length;
		ArrayList<Integer> nlist = new ArrayList<Integer>(Arrays.asList(neighbours));//initialise nlist
		int a,b;
		newSCCell cHold;

			//find an available neighbour
			for(int i=0;i<sizeA;i++){ // Loop from starting point through list of neighbours
				a = rand.nextInt(nlist.size());//pick random list index
				b = nlist.remove(a);//use the value at that index and make the list smaller
				cHold = home.getNeighbour(b);
				if(cHold.isavail||(cHold.type==4)){ // If neighbour is available for takeover
                    return cHold;
				}
			}
        //what should be done if no neighbour is found?
		return null;// Return null if no available neighbour 
	}

}
