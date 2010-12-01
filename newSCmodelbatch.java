/*
 * This is a class that contains main and
 * will call the SPCGrid simulation and graphically display results.
 * usage: noSPC scfrac scrate maxiters smallcolony forcegrow readgrid
 * typical usage: noSPC 0.22 0.2 100 small blank new
 * for small group of cells, 0.22 frac SC, 5 iters per SC prolif, 100 iters total, 
 * don't force the grow, and generate a new grid
 * SPC is for Single Progenitor Cell
 */


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.util.*;
 
public class newSCmodelbatch  {

    newSCmodelGrid experiment;
    Random rand = new Random();    

	int iterations;
	int gSize;
 
    //Color[] colours = {Color.black,Color.white,Color.green,Color.blue,Color.yellow,Color.red,Color.pink};
    boolean writeImages = false,writetoGrid = false;
    static int maxIters = 1000;
	int lin=64*64;//max number of different cell lines equals cells in grid
	double dlin = (double) lin;
	public static boolean readgrid = false;
	BufferedWriter bufGrid;
	int maxage = (int) (maxIters*newSCCell.scRate);
	
    Colour palette = new Colour();
	int[] colorindices = {45,1,54};//{0,1,2,54,4,5};
	int nnw = colorindices.length-1;
//    Color[] colours = {Color.white,Color.black,Color.green,Color.blue,Color.yellow,Color.red,Color.pink};
    Color[] javaColours;
    double[][] epsColours;
    //Color[] colours = {Color.black,Color.white,Color.green,Color.blue,Color.yellow,Color.red,Color.pink};

	int countcolonies, countviable;

	public int[] colonies,SCs;
	int SCcount;

	
	


	public newSCmodelbatch(int size, int maxC, double frac) {
	    gSize=size;
		    experiment = new newSCmodelGrid(size, maxC, frac);

		lin = newSCmodelGrid.maxlineage;//in case the grid is not full
		dlin = (double) lin;
		maxage = (int) (maxIters*newSCCell.scRate*1.5);//1.5 times expected
		//System.out.println("max age = "+maxage);
		
		colonies = new int[lin];
		SCs = new int[lin];

			//drawCALineage();

			for(iterations=0; iterations<maxIters; iterations++){

				experiment.iterateandcount(true);

				/*if (iterations%2==0){
					drawCALineage();
				}*/
				newSCCell.resetstaticcounters();
				//if((iterations%5)==0)postscriptPrint("SPC"+iterations+".eps");
				// This will produce a postscript output of the tissue
			}		
	}
	//new ones
	
	
	


	
	public void drawCALineage() {

  	    SCcount = 0;
		for(int ii=0;ii<lin;ii++)colonies[ii] = 0;
		for(int ii=0;ii<lin;ii++)SCs[ii] = 0;
		for (newSCCell c : experiment.tissue){
			colonies[c.lineage] = colonies[c.lineage] | 1;
			if (c.type==1) {
				SCs[c.lineage] = SCs[c.lineage] | 1;
				SCcount++;
				//System.out.println("yo");
			}
		}
		countcolonies = 0;
		countviable = 0;
		for(int ii=0;ii<lin;ii++){
			countcolonies = countcolonies+ colonies[ii];
			countviable = countviable+ SCs[ii];
		}
		System.out.println(" "+countviable+"  "+countcolonies+"  "+SCcount+"  "+newSCCell.totalproliferations);

	}

	public void writeGrid(){
		try{

	        for (newSCCell c : experiment.tissue) { // loop through the tissue (ArrayList of cells)
				bufGrid.write(c.home.x+" "+c.home.y+" "+c.type+" "+c.age+" "+c.lineage);
				bufGrid.newLine();	
		    }


		}
		catch(IOException e){
		}
	}





	public void postscriptPrint(String fileName) {
		int xx;
		int yy;
		int state;
		boolean flag;
		try {
			java.io.FileWriter file = new java.io.FileWriter(fileName);
			java.io.BufferedWriter buffer = new java.io.BufferedWriter(file);
			System.out.println(fileName);
			buffer.write("%!PS-Adobe-2.0 EPSF-2.0");
			buffer.newLine();
			buffer.write("%%Title: test.eps");
			buffer.newLine();
			buffer.write("%%Creator: gnuplot 4.2 patchlevel 4");
			buffer.newLine();
			buffer.write("%%CreationDate: Thu Jun  4 14:16:00 2009");
			buffer.newLine();
			buffer.write("%%DocumentFonts: (atend)");
			buffer.newLine();
			buffer.write("%%BoundingBox: 0 0 300 300");
			buffer.newLine();
			buffer.write("%%EndComments");
			buffer.newLine();
			for (newSCCell c : experiment.tissue){
				if(c.type>0){
					xx = (c.home.x * 4) + 20;
					yy = (c.home.y * 4) + 20;
					if (c.proliferated) {
						buffer.write("newpath " + xx + " " + yy + " 1.5 0 360 arc fill\n");
						buffer.write("0 setgray\n");
						buffer.write("newpath " + xx + " " + yy + " 1.5 0 360 arc  stroke\n");
					} else {
						buffer.write("0.75 setgray\n");
						buffer.write("newpath " + xx + " " + yy + " 1.5 0 360 arc fill\n");
					}
				}
			}
			buffer.write("showpage");
			buffer.newLine();
			buffer.write("%%Trailer");
			buffer.newLine();
			buffer.write("%%DocumentFonts: Helvetica");
			buffer.newLine();
			buffer.close();
		} catch (java.io.IOException e) {
			System.out.println(e.toString());
		}
	}
    public void setpalette(){
    	int ind = colorindices.length;
    	javaColours = new Color[ind];
    	epsColours = new double[ind][3];
    	for (int i=0;i<ind;i++){
    		//System.out.println("color index "+colorindices[i]);
    		javaColours[i] = palette.chooseJavaColour(colorindices[i]);
    		epsColours[i] = palette.chooseEPSColour(colorindices[i]);
    	}
    }

	public static void main(String args[]) {
		double initalSeed = 0.1;
		int arglen = args.length;
		maxIters = 1000;
		newSCmodelbatch s;
		switch(arglen){
		case 6:
			if (args[5].contains("read")) readgrid = true;
		case 5:
			if (args[4].contains("force")) newSCmodelGrid.forcinggrow = true;
		case 4:
			if (args[3].contains("small")) {
				newSCmodelGrid.smallcolony = true;
				readgrid = false;
			}
		case 3:
			maxIters = Integer.parseInt(args[2]);
		case 2:
			newSCCell.scRate = Double.parseDouble(args[1]);
            if (newSCCell.scRate > 1.0) newSCCell.scRate = 1.0;
		case 1:
			initalSeed = Double.parseDouble(args[0]);
			s = new newSCmodelbatch(64, 1, initalSeed);
			break;
		default:
			//maxiters set above, scrate set to 1 in newSCCell,force and small set in gridstatic.
			s = new newSCmodelbatch(64, 1, 0.22);
		}
//		if(arglen>0){
//			initalSeed = Double.parseDouble(args[0]);
//			if (arglen > 1) {
//				newSCCell.scRate = Double.parseDouble(args[1]);
//                if (newSCCell.scRate > 1.0) newSCCell.scRate = 1.0;
//			}
//			else newSCCell.scRate = 1.0;
//			if (arglen > 2) {
//				maxIters = Integer.parseInt(args[2]);
//			}
//			else maxIters = 100;
//			//System.out.println("scRate "+newSCCell.scRate);
//			noSPC s = new noSPC(64, 1, initalSeed);
//			s.start();
//		}else{
//			noSPC s = new noSPC(64, 1, 0.22);
//			s.start();
//		}
		
	}
}

