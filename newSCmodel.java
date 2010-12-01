/*
 * This is a class that contains main and
 * will call the SPCGrid simulation and graphically display results.
 * usage: noSPCgrowprob scfrac scrate maxiters smallcolony forcegrow readgrid
 * typical usage: noSPCgrowprob 0.22 0.2 100 small blank new
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
 
public class newSCmodel extends JFrame implements Runnable {

    newSCmodelGrid experiment;
    Random rand = new Random();    
	Thread runner;
    Container mainWindow;
	CAImagePanel CApicture;
	Image backImg1;
	Graphics backGr1;
	Image backImg2;
	Graphics backGr2;
	JProgressBar progressBar;
	JTextArea msgBtn,msgBtn2,msgBtn3;
	JPanel buttonHolderhigh;
	int scale = 20;//beth: could set to 1. Makes the colour transitions better?
	int border = 20;
	int iterations;
	int gSize;
    Colour palette = new Colour();
	int[] colorindices = {45,1,54,23,59};//{0,1,2,54,4,5};
	int nnw = colorindices.length-1;
//    Color[] colours = {Color.white,Color.black,Color.green,Color.blue,Color.yellow,Color.red,Color.pink};
    Color[] javaColours;
    double[][] epsColours;
    //Color[] colours = {Color.black,Color.white,Color.green,Color.blue,Color.yellow,Color.red,Color.pink};
    boolean writeImages = false,writetoGrid = false;
    static int maxIters = 200;
	int lin=64*64;//max number of different cell lines equals cells in grid
	double dlin = (double) lin;
	public static boolean readgrid = false;
	BufferedWriter bufGrid;
	int maxage = (int) (maxIters*newSCCell.scRate);


	public newSCmodel(int size, int maxC, double frac) {
	    gSize=size;
	    if (readgrid)
			experiment = new newSCmodelGrid(size, maxC, frac,"grid.dat");
	    else
		    experiment = new newSCmodelGrid(size, maxC, frac);

		lin = newSCmodelGrid.maxlineage;//in case the grid is not full
		dlin = (double) lin;
		maxage = (int) (maxIters*newSCCell.scRate*1.5);//1.5 times expected
		//System.out.println("max age = "+maxage);

		setVisible(true);
		backImg1 = createImage(scale * size, scale * size);
		backGr1 = backImg1.getGraphics();
		backImg2 = createImage(scale * size, scale * size);
		backGr2 = backImg2.getGraphics();
		setpalette();
		
	    int wscale = 6;//scale for main panel
	    int btnHeight = 480-384;//found by trial and error - must be a better way!
	    //although no buttons yet
	    int wh = (gSize*1)*wscale + 2*border;// +btnHeight;//mainWindow height
	    int ww = (gSize*2)*wscale + 3*border;//mainWindow width   
	    
		mainWindow = getContentPane();
		mainWindow.setLayout(new BorderLayout());
		setSize(ww,wh);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
        CApicture = new CAImagePanel(ww,wh);
        CApicture.setBorder(border);
        CApicture.rowstoShow = gSize;
        mainWindow.add(CApicture,BorderLayout.CENTER);
		setVisible(true);
		
		buttonHolderhigh = new JPanel();
		buttonHolderhigh.setLayout(new GridLayout(1,4));
		
        msgBtn = new JTextArea("   Colony count: "+0);
        msgBtn.setEditable(false);
        buttonHolderhigh.add(msgBtn);
        msgBtn2 = new JTextArea("   Viable Colony count: "+0);
        msgBtn2.setEditable(false);
        buttonHolderhigh.add(msgBtn2);
        msgBtn3 = new JTextArea("   Stem Cell count: "+0);
        msgBtn3.setEditable(false);
        buttonHolderhigh.add(msgBtn3);
		
	    progressBar = new JProgressBar(JProgressBar.HORIZONTAL,0,maxIters-1);
	    progressBar.setValue(0);
	    progressBar.setStringPainted(true);
	    
	    buttonHolderhigh.add(progressBar);	    
	    mainWindow.add(buttonHolderhigh, BorderLayout.SOUTH);
		setVisible(true);
		

		
	}
	//new ones
	public void drawCA() {

		int a;
		for (newSCCell c : experiment.tissue){
			a = c.type;
			CApicture.drawCircleAt(c.home.x, c.home.y, javaColours[a], 1);
		}
	    CApicture.updateGraphic();
	}
	public void drawCAage(int panelnum) {
		double cage;
  	    CApicture.clearCAPanel(panelnum);
		for (newSCCell c : experiment.tissue){
			//if ((c.age < minage) && (c.type>0)) minage = c.age;
			cage = 1.0 - (double) (c.age)/(double)(maxage);
			if (c.type==1) 
				CApicture.drawCircleAt2(c.home.x, c.home.y, palette.Javashades(cage), panelnum,c.proliferated);
			else 
				CApicture.drawCircleAt(c.home.x, c.home.y, palette.Javashades(cage), panelnum);
		}
	    //outputImage();
	    CApicture.updateGraphic();
	}
	public void drawCALineage() {
		double celllin;
  	    CApicture.clearCAPanel(2);
  	    Color Lineagecolour;
  	    

  	    for (newSCCell c : experiment.tissue){
  	    	celllin = (double)c.lineage/(dlin+1.0);
  	    	switch(c.type){
  	    	case(0):
  	    		CApicture.drawCircleAt(c.home.x, c.home.y, javaColours[0], 2);
  	    	break;
  	    	case(1):
  	    		CApicture.drawCircleAt2(c.home.x, c.home.y, palette.Javagrey(celllin), 2,c.proliferated);
  	    	break;
  	    	case(2):
  	    		CApicture.drawCircleAt2(c.home.x, c.home.y, palette.Javagrey(celllin), 2,c.proliferated);
  	    	break;
  	    	default:
  	    		CApicture.drawCircleAt(c.home.x, c.home.y, javaColours[2], 2);	
  	    		//if ((c.age < minage) && (c.type>0)) minage = c.age;

  	    	}
  	    }

		//System.out.println("v "+countviable+" c "+countcolonies+" s "+SCcount+" p "+newSCCell.totalproliferations);
	    //outputImage();
	    CApicture.updateGraphic();
	}
	public void drawCALineage(int lintodraw) {
		double celllin;
  	    CApicture.clearCAPanel(2);
  	    Color Lineagecolour=javaColours[2];
		celllin = (double)lintodraw/(dlin+1.0);
		for (newSCCell c : experiment.tissue){

			//if ((c.age < minage) && (c.type>0)) minage = c.age;
			if (c.type==0){
				CApicture.drawCircleAt(c.home.x, c.home.y, javaColours[0], 2);
			}
			else{
				if (experiment.SCs[c.lineage]==1){
					if (c.lineage == lintodraw) Lineagecolour = palette.Javagrey(celllin);
					else Lineagecolour = javaColours[3];
					if (c.type==1) {
						CApicture.drawCircleAt2(c.home.x, c.home.y, Lineagecolour, 2,c.proliferated);
					}
					else 
						CApicture.drawCircleAt(c.home.x, c.home.y, Lineagecolour, 2);
				}
				else{//draw it blue - not viable
					if (c.lineage == lintodraw) Lineagecolour =  javaColours[4];
					else Lineagecolour = javaColours[3];
					if (c.type==1) {
						CApicture.drawCircleAt2(c.home.x, c.home.y, Lineagecolour, 2,c.proliferated);
					}
					else 
						CApicture.drawCircleAt(c.home.x, c.home.y, Lineagecolour, 2);
				}
			}
		}

		//System.out.println("v "+countviable+" c "+countcolonies+" s "+SCcount+" p "+newSCCell.totalproliferations);
	    //outputImage();
	    CApicture.updateGraphic();
	}
	public void openoutputfile(){
		try{
			bufGrid = new BufferedWriter(new FileWriter("allgrid.dat"));
		}
		catch(IOException e){
			}
	}
	public void closeoutputfile(){
		try{
			bufGrid.close();
			System.out.println("Finished writing grid");
		}
		catch(IOException e){
			}
	}
	public void writeGrid(){
		try{

	        for (newSCCell c : experiment.tissue) { // loop through the tissue (ArrayList of cells)
				bufGrid.write(c.home.x+" "+c.home.y+" "+c.type+" "+c.cycleStage+" "+c.lineage);
				bufGrid.newLine();	
		    }


		}
		catch(IOException e){
		}
	}
	public void initialise(){
			CApicture.setScale(gSize,gSize,scale,gSize,gSize,scale);
      	    CApicture.clearCAPanel(1);
      	    CApicture.clearCAPanel(2);
      	    CApicture.clearParent();
		    iterations=0;
	}
	
	
	public void start() {
		initialise();
		if (runner == null) {
			runner = new Thread(this);
		}
		runner.start();
	}


	public void run() {
		if (runner == Thread.currentThread()) {
			drawCAage(1);
			//either draw colonies:
			drawCALineage();
			//newSCCell.scRate = 0.1;
			System.out.println("scRate "+newSCCell.scRate);
			if (writeImages) CApicture.writeImage(0);
			openoutputfile();
			for(iterations=0; iterations<maxIters; iterations++){
				//if(iterations==0)experiment.age();// age all cells at start
				experiment.iterateandcount(false);
				progressBar.setValue(iterations);
				if (iterations%10==0)writeGrid();
				if (iterations%2==0){
					drawCAage(1);
					//either draw colonies:
					drawCALineage();
					//or draw age:
					//drawCAage();
					msgBtn.setText("   Colony count: "+ experiment.countcolonies);
					msgBtn2.setText("   Viable Colony count: "+ experiment.countviable);
					msgBtn3.setText("   Stem Cell count: "+experiment.SCcount);
					if (writeImages) CApicture.writeImage(iterations+2);
				}
				newSCCell.resetstaticcounters();
				//if((iterations%5)==0)postscriptPrint("SPC"+iterations+".eps");
				// This will produce a postscript output of the tissue
			}
			closeoutputfile();
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
		maxIters = 500;
		newSCmodel s;
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
			s = new newSCmodel(64, 1, initalSeed);
			break;
		default:
			//maxiters set above, scrate set to 1 in newSCCell,force and small set in gridstatic.
			s = new newSCmodel(64, 1, 0.22);
		}
		s.start();
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
//			noSPCgrowprob s = new noSPCgrowprob(64, 1, initalSeed);
//			s.start();
//		}else{
//			noSPCgrowprob s = new noSPCgrowprob(64, 1, 0.22);
//			s.start();
//		}
		
	}
}

