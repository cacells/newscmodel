import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.*;


class CAImagePanel extends JPanel {

	    Image topImg;
	    Image botImg;//second image
	    Graphics backGr,backGr2;
		int columns, rows,columns2,rows2;
		int xScale, yScale,xScale2,yScale2;
		BasicStroke wideStroke = new BasicStroke(5.0f);
		Graphics2D g2;
		float frac = 1.0f;
		boolean twoImage = false;
		int rowstoShow = 100;
		int cind = 7;
		Colour tcolor = new Colour();
		Color Jcolor = tcolor.chooseJavaColour(cind);
		double[] EPScolor = tcolor.chooseEPSColour(cind);
		int border;
		int parentwidth,parentheight;
        BufferedImage bi;
        Graphics2D bigraphics2d;
        ImageObserver IOb;
        static 	DecimalFormat twoPlaces = new DecimalFormat("0000");
        static int Iw=100,Ih = 100;

		public CAImagePanel(int ww, int  wh){
			parentwidth = ww;
			parentheight = wh;
	        bi = new BufferedImage(ww, wh, BufferedImage.TYPE_INT_RGB);
	        bigraphics2d = bi.createGraphics();
			bigraphics2d.setColor(Color.white);
			bigraphics2d.fillRect(0,0,ww,wh);
		}
		public void setScale(int noColumns,int noRows, int scale)//just one image
		{
			columns = noColumns;
			rows = noRows;
			xScale = scale;
			yScale = scale;
			//System.out.println("here"+xScale*columns+" "+yScale*rows);
			topImg= createImage(xScale*columns,yScale*rows);
			//topImg= createImage(20*64,2000);
			//System.out.println("here"+topImg);
			backGr= topImg.getGraphics();
			g2 = (Graphics2D) backGr;
			g2.setStroke(wideStroke);
		}
		public void setScale(int noColumns,int noRows,int scale,int noColumns2,int noRows2, int scale2)
		{
			columns = noColumns;
			rows = noRows;
			xScale = scale;
			yScale = scale;
			topImg= createImage(xScale*columns,yScale*rows);
			backGr= topImg.getGraphics();

			//now for the second image
			columns2 = noColumns2;
			rows2 = noRows2;
			xScale2 = scale2;
			yScale2 = scale2;
			botImg= createImage(xScale2*columns2,yScale2*rows2);
			backGr2= botImg.getGraphics();
			g2 = (Graphics2D) backGr2;
			g2.setStroke(wideStroke);
			//frac = (float)(xScale*rows)/(float)(xScale*rows + xScale2*rows2);
			frac = (float)(xScale*columns+2*border)/(float)(xScale*columns + xScale2*columns2+3*border);
			twoImage = true;
			//System.out.println("height" + this.getSize().height);
		}
		
		public void clearCAPanel()
		{
				backGr.setColor(Color.orange);
				backGr.fillRect(0,0,this.columns*xScale,rows*yScale);
				//backGr.fillRect(0,0,20*64,2000);
		}
		public void clearCAPanel(int panelNum)
		{

			if (panelNum == 1){
				backGr.setColor(Color.white);
				backGr.fillRect(0,0,xScale*columns,yScale*rows);
			}
			if (panelNum == 2){
				//backGr2.setColor(Jcolor);
				backGr2.setColor(Color.white);
				backGr2.fillRect(0,0,xScale2*columns,yScale2*rows2);
			}
		}
		public void clearParent()
		{
			Graphics g = this.getGraphics();
			g.setColor(Color.white);
			g.fillRect(0,0,parentwidth,parentheight);
		}
		public void setBorder(int border){
			this.border = border;
		}
		
		public void drawCircleAt(int x, int y, Color colour)
		{
			backGr.setColor(colour);
			backGr.fillOval(x*xScale,y*yScale,xScale,yScale);

		}
		public void drawCircleAt(int x, int y, Color colour,int panelNum)
		{
			if (panelNum == 1){
			backGr.setColor(colour);
			backGr.fillOval(x*xScale,y*yScale,xScale,yScale);
			}
			if (panelNum == 2){
				backGr2.setColor(colour);
				backGr2.fillOval(x*xScale2,y*yScale2,xScale2,yScale2);
			}
		}
		public void drawCircleAt2(int x, int y, Color colour,int panelNum, boolean hasprolifd)
		{
			if (panelNum == 1){
			backGr.setColor(colour);
			backGr.fillOval(x*xScale,y*yScale,xScale,yScale);
			}
			if (panelNum == 2){
				backGr2.setColor(colour);
				backGr2.fillOval(x*xScale2,y*yScale2,xScale2,yScale2);
				if (hasprolifd) backGr2.setColor(Color.yellow);
				else backGr2.setColor(Color.red);
				backGr2.drawOval(x*xScale2,y*yScale2,xScale2,yScale2);
			}
		}
		public void drawSC(int x, int y, Color colour, int panelNum)
		{
			backGr2.setColor(colour);
			switch(panelNum){
			case 1:
				backGr.drawOval(x*xScale2,y*yScale2,xScale2,yScale2);
				break;
			case 2:
				backGr2.drawOval(x*xScale2,y*yScale2,xScale2,yScale2);
				break;
			}
		}
		public void drawProgress(int val, int maxval, Color colour,int panelNum)
		//draw a progress bar in the corner	
		{
			int sizex = 5;
			int xval1,xval2;
			if (panelNum == 1){
				xval1 = (sizex*val*xScale)/maxval;
				xval2 = sizex*xScale - xval1;
				backGr.setColor(colour);
				backGr.fillRect(10,0,xval1,yScale);
				backGr.setColor(Color.lightGray);
				backGr.fillRect(xval1+10,0,xval2,yScale);		
			}
			if (panelNum == 2){
				xval1 = (sizex*val*xScale2)/maxval;
				xval2 = sizex*xScale2-xval1;
				backGr.setColor(colour);
				backGr.fillRect(10,0,xval1,yScale2);
				backGr.setColor(Color.white);
				backGr.fillRect(xval1+10,0,xval2,yScale2);	
			}
		}
		
		public void drawALine(int x1, int y1, int x2, int y2,Color colour)
		{
			backGr.setColor(colour);
			backGr.drawLine(x1*xScale,y1*yScale,x2*xScale,y2*yScale);
		}
		
		public void drawALine(int x1, int y1, int x2, int y2,Color colour,int panelNum)
		{
			if (panelNum == 1){
			backGr.setColor(colour);
			backGr.drawLine(x1*xScale,y1*yScale,x2*xScale,y2*yScale);
			}
			if (panelNum == 2){
				backGr2.setColor(colour);
				backGr2.drawLine(x1*xScale2,y1*yScale2,x2*xScale2,y2*yScale2);
			}
		}

		public void updateGraphic() {
		        Graphics g = this.getGraphics();
		        if ((topImg != null) && (g != null)) {		        	
		            g.drawImage(topImg, border, border,  (int)(((float)this.getSize().width)*frac)-border, this.getSize().height-border, 0, 0, (int) (xScale * (columns)), (int) (yScale * (rows)), this);
		            if (twoImage)
		            	g.drawImage(botImg, (int)(((float)this.getSize().width)*frac),border, this.getSize().width-border, this.getSize().height-border, 0, 0, (int) (xScale2 * (columns2)), (int) (yScale2 * (rows2)), this);
	            	    //g.drawImage(botImg, 0, (int)(((float)this.getSize().height)*frac), this.getSize().width, this.getSize().height, 0, 0, (int) (xScale2 * (columns2)), (int) (yScale2 * (rowstoShow)), this);
		        }
        
		    }

		
		    public void writeImage(int iter){
		    	//System.out.println("iteration "+iter);
	            bigraphics2d.drawImage(topImg, border, border,  (int)(parentwidth*frac) - border, parentheight-border, 0, 0, (int) (xScale * (columns)), (int) (yScale * (rows)), this);
		        bigraphics2d.drawImage(botImg, (int)(parentwidth*frac),border, parentwidth-border, parentheight-border, 0, 0, (int) (xScale2 * (columns2)), (int) (yScale2 * (rows2)), this);
 	        //paint(g2);	        
		        File f = new File("images/image"+twoPlaces.format(iter)+".png");
		        try {
		                // png is an image format (like gif or jpg)
		                ImageIO.write(bi, "png", f);
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }	
		    }

		    @Override
		    public void paintComponent(Graphics g) {
		        updateGraphic();
		    }

		    @Override
		    public void paint(Graphics g) {
		        updateGraphic();
		    }

	}