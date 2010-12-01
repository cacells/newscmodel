import java.awt.Color;

public class Colour{
static double[][] vals={
{1.0,1.0,1.0},
{1.0,0.0,0.0},
{0.75,0.75,0.75},
{0.625,0.625,0.625},
{0.5,0.5,0.5},
{0.0,0.0,0.0},
{0.800000,0.800000,0.800000},
{0.930000,0.950000,0.980000},
{0.439216,0.858824,0.576471},
{0.623520,0.372549,0.623529},
{0.647059,0.164706,0.164706},
{0.372549,0.623529,0.623529},
{1.000000,0.498039,0.000000},
{0.258824,0.258824,0.435294},
{0.184314,0.309804,0.184314},
{0.309804,0.309804,0.184314},
{0.600000,0.196078,0.800000},
{0.119608,0.137255,0.556863},
{0.184314,0.309804,0.309804},
{0.184314,0.309804,0.309804},
{0.439216,0.576471,0.858824},
{0.556863,0.137255,0.137255},
{0.137255,0.556863,0.137255},
{0.800000,0.498039,0.196078},
{0.858824,0.858824,0.439216},
{0.576471,0.858824,0.439216},
{0.309804,0.184314,0.184314},
{0.623529,0.623529,0.372549},
{0.749020,0.847059,0.847059},
{0.560784,0.560784,0.737255},
{0.196078,0.800000,0.196078},
{0.556863,0.137255,0.419608},
{0.196078,0.800000,0.600000},
{0.196078,0.196078,0.800000},
{0.419608,0.556863,0.137255},
{0.917647,0.917647,0.678431},
{0.576471,0.439216,0.858824},
{0.258824,0.435294,0.258824},
{0.498039,0.000000,1.000000},
{0.498039,1.000000,0.000000},
{0.439216,0.858824,0.858824},
{0.858824,0.439216,0.576471},
{0.184314,0.184314,0.309804},
{0.137255,0.137255,0.556863},
{0.137255,0.137255,0.556863},
{1.000000,0.500000,0.000000},
{1.000000,0.250000,0.000000},
{0.858824,0.439216,0.858824},
{0.560784,0.737255,0.560784},
{0.737255,0.560784,0.560784},
{0.917647,0.678431,0.917647},
{0.435294,0.258824,0.258824},
{0.137255,0.556863,0.419608},
{0.556863,0.419608,0.137255},
{0.196078,0.600000,0.800000},
{0.498039,0.000000,1.000000},
{0.000000,1.000000,0.498039},
{0.137255,0.419608,0.556863},
{0.858824,0.576471,0.439216},
{0.847059,0.749020,0.847059},
{0.678431,0.917647,0.917647},
{0.309804,0.184314,0.309804},
{0.800000,0.196078,0.600000},
{0.847059,0.847059,0.749020},
{0.600000,0.800000,0.196078},
{0.220000,0.690000,0.870000},
{0.350000,0.350000,0.670000},
{0.710000,0.650000,0.260000},
{0.720000,0.450000,0.200000},
{0.550000,0.470000,0.140000},
{0.650000,0.490000,0.240000},
{0.900000,0.910000,0.980000},
{0.850000,0.850000,0.100000},
{0.810000,0.710000,0.230000},
{0.820000,0.570000,0.460000},
{0.850000,0.850000,0.950000},
{1.000000,0.430000,0.780000},
{0.530000,0.120000,0.470000},
{0.300000,0.300000,1.000000},
{0.850000,0.530000,0.100000},
{0.890000,0.470000,0.200000},
{0.910000,0.760000,0.650000},
{0.650000,0.500000,0.390000},
{0.520000,0.370000,0.260000},
{1.000000,0.110000,0.680000},
{0.420000,0.260000,0.150000},
{0.360000,0.200000,0.090000},
{0.960000,0.800000,0.690000},
{0.920000,0.780000,0.620000},
{0.000000,0.000000,0.610000},
{0.350000,0.160000,0.140000},
{0.360000,0.250000,0.200000},
{0.590000,0.410000,0.310000},
{0.320000,0.490000,0.460000},
{0.290000,0.460000,0.430000},
{0.520000,0.390000,0.390000},
{0.130000,0.370000,0.310000},
{0.550000,0.090000,0.090000},
{0.730000,0.160000,0.960000},
{0.870000,0.580000,0.980000},
{0.940000,0.810000,0.990000},
{0.900000,0.930000,0.950000},
};

public Color chooseJavaColour(int index){
	Color chosen;
	int r,g,b;
	r = (int)(255*vals[index][0]);
	g = (int)(255*vals[index][1]);
	b = (int)(255*vals[index][2]);
	chosen = new Color(r,g,b);
	//System.out.println(r+" "+g+" "+b);
	return chosen;
}
public Color Javagrey(double stain){
	Color chosen = null;
	int grey;
	grey = (int)(255.0*(1.0-stain));
	chosen = new Color(grey,grey,grey);
	//System.out.println(r+" "+g+" "+b);
	return chosen;
}
public Color Javashades(double stain){
	Color chosen = null;
	int grey;
	grey = (int)(255.0*(1.0-stain));
	//chosen = new Color(grey,grey,grey);
	chosen = new Color(chosen.HSBtoRGB((float) (194.0/360.0), (float) stain,(float) (1.0-stain/2.0)));
	//System.out.println(r+" "+g+" "+b);
	return chosen;
}

public double[] chooseEPSColour(int index){
	double[] chosen = {vals[index][0],vals[index][1],vals[index][2]};
	return chosen;
}

}
/*
0 DimGray {0.329412,0.329412,0.329412}
replaced by white
1 DimGrey {0.329412,0.329412,0.329412}
replaced by red
2 Gray {0.752941,0.752941,0.752941}
replaced by 0.75 gray
3 Grey {0.752941,0.752941,0.752941}
replaced by 0.625 gray
4 LightGray {0.658824,0.658824,0.658824}
replaced by 0,5 gray
5 LightGrey {0.658824,0.658824,0.658824}
replaced by black
6 VLightGray {0.800000,0.800000,0.800000}
7 #declare VLightGrey = color red 0.80 green 0.80 blue 0.80;
8 #declare Aquamarine = color red 0.439216 green 0.858824 blue 0.576471;
9 #declare BlueViolet = color red 0.62352 green 0.372549 blue 0.623529;
10 #declare Brown = color red 0.647059 green 0.164706 blue 0.164706;
11 #declare CadetBlue = color red 0.372549 green 0.623529 blue 0.623529;
12 #declare Coral = color red 1.0 green 0.498039 blue 0.0;
13 #declare CornflowerBlue = color red 0.258824 green 0.258824 blue 0.435294;
14 #declare DarkGreen = color red 0.184314 green 0.309804 blue 0.184314;
15 #declare DarkOliveGreen = color red 0.309804 green 0.309804 blue 0.184314;
16 #declare DarkOrchid = color red 0.6 green 0.196078 blue 0.8;
17 #declare DarkSlateBlue = color red 0.119608 green 0.137255 blue 0.556863;
18 #declare DarkSlateGray = color red 0.184314 green 0.309804 blue 0.309804;
19 #declare DarkSlateGrey = color red 0.184314 green 0.309804 blue 0.309804;
20 #declare DarkTurquoise = color red 0.439216 green 0.576471 blue 0.858824;
21 #declare Firebrick = color red 0.556863 green 0.137255 blue 0.137255;
22 #declare ForestGreen = color red 0.137255 green 0.556863 blue 0.137255;
23 #declare Gold = color red 0.8 green 0.498039 blue 0.196078;
24 #declare Goldenrod = color red 0.858824 green 0.858824 blue 0.439216;
25 #declare GreenYellow = color red 0.576471 green 0.858824 blue 0.439216;
26 #declare IndianRed = color red 0.309804 green 0.184314 blue 0.184314;
27 #declare Khaki = color red 0.623529 green 0.623529 blue 0.372549;
28 #declare LightBlue = color red 0.74902 green 0.847059 blue 0.847059;
29 #declare LightSteelBlue = color red 0.560784 green 0.560784 blue 0.737255;
30 #declare LimeGreen = color red 0.196078 green 0.8 blue 0.196078;
31 #declare Maroon = color red 0.556863 green 0.137255 blue 0.419608;
32 #declare MediumAquamarine = color red 0.196078 green 0.8 blue 0.6;
33 #declare MediumBlue = color red 0.196078 green 0.196078 blue 0.8;
34 #declare MediumForestGreen = color red 0.419608 green 0.556863 blue 0.137255;
35 #declare MediumGoldenrod = color red 0.917647 green 0.917647 blue 0.678431;
36 #declare MediumOrchid = color red 0.576471 green 0.439216 blue 0.858824;
37 #declare MediumSeaGreen = color red 0.258824 green 0.435294 blue 0.258824;
38 #declare MediumSlateBlue = color red 0.498039 green 0.0 blue 1.0;
39 #declare MediumSpringGreen = color red 0.498039 green 1.0 blue 0.0;
40 #declare MediumTurquoise = color red 0.439216 green 0.858824 blue 0.858824;
41 #declare MediumVioletRed = color red 0.858824 green 0.439216 blue 0.576471;
42 #declare MidnightBlue = color red 0.184314 green 0.184314 blue 0.309804;
43 #declare Navy = color red 0.137255 green 0.137255 blue 0.556863;
44 #declare NavyBlue = color red 0.137255 green 0.137255 blue 0.556863;
45 #declare Orange = color red 1 green 0.5 blue 0.0;
46 #declare OrangeRed = color red 1.0 green 0.25 blue 0.0;
47 #declare Orchid = color red 0.858824 green 0.439216 blue 0.858824;
48 #declare PaleGreen = color red 0.560784 green 0.737255 blue 0.560784;
49 #declare Pink = color red 0.737255 green 0.560784 blue 0.560784;
50 #declare Plum = color red 0.917647 green 0.678431 blue 0.917647;
51 #declare Salmon = color red 0.435294 green 0.258824 blue 0.258824;
52 #declare SeaGreen = color red 0.137255 green 0.556863 blue 0.419608;
53 #declare Sienna = color red 0.556863 green 0.419608 blue 0.137255;
54 #declare SkyBlue = color red 0.196078 green 0.6 blue 0.8;
55 #declare SlateBlue = color red 0.498039 green 0.0 blue 1.0;
56 #declare SpringGreen = color red 0.0 green 1.0 blue 0.498039;
57 #declare SteelBlue = color red 0.137255 green 0.419608 blue 0.556863;
58 #declare Tan = color red 0.858824 green 0.576471 blue 0.439216;
59 #declare Thistle = color red 0.847059 green 0.74902 blue 0.847059;
60 #declare Turquoise = color red 0.678431 green 0.917647 blue 0.917647;
61 #declare Violet = color red 0.309804 green 0.184314 blue 0.309804;
62 #declare VioletRed = color red 0.8 green 0.196078 blue 0.6;
63 #declare Wheat = color red 0.847059 green 0.847059 blue 0.74902;
64 #declare YellowGreen = color red 0.6 green 0.8 blue 0.196078;
65 #declare SummerSky = color red 0.22 green 0.69 blue 0.87;
66 #declare RichBlue = color red 0.35 green 0.35 blue 0.67;
67 #declare Brass =  color red 0.71 green 0.65 blue 0.26;
68 #declare Copper = color red 0.72 green 0.45 blue 0.20;
69 #declare Bronze = color red 0.55 green 0.47 blue 0.14;
70 #declare Bronze2 = color red 0.65 green 0.49 blue 0.24;
71 #declare Silver = color red 0.90 green 0.91 blue 0.98;
72 #declare BrightGold = color red 0.85 green 0.85 blue 0.10;
73 #declare OldGold =  color red 0.81 green 0.71 blue 0.23;
74 #declare Feldspar = color red 0.82 green 0.57 blue 0.46;
75 #declare Quartz = color red 0.85 green 0.85 blue 0.95;
76 #declare NeonPink = color red 1.00 green 0.43 blue 0.78;
77 #declare DarkPurple = color red 0.53 green 0.12 blue 0.47;
78 #declare NeonBlue = color red 0.30 green 0.30 blue 1.00;
79 #declare CoolCopper = color red 0.85 green 0.53 blue 0.10;
80 #declare MandarinOrange = color red 0.89 green 0.47 blue 0.20;
81 #declare LightWood = color red 0.91 green 0.76 blue 0.65;
82 #declare MediumWood = color red 0.65 green 0.50 blue 0.39;
83 #declare DarkWood = color red 0.52 green 0.37 blue 0.26;
84 #declare SpicyPink = color red 1.00 green 0.11 blue 0.68;
85 #declare SemiSweetChoc = color red 0.42 green 0.26 blue 0.15;
86 #declare BakersChoc = color red 0.36 green 0.20 blue 0.09;
87 #declare Flesh = color red 0.96 green 0.80 blue 0.69;
88 #declare NewTan = color red 0.92 green 0.78 blue 0.62;
89 #declare NewMidnightBlue = color red 0.00 green 0.00 blue 0.61;
90 #declare VeryDarkBrown = color red 0.35 green 0.16 blue 0.14;
91 #declare DarkBrown = color red 0.36 green 0.25 blue 0.20;
92 #declare DarkTan = color red 0.59 green 0.41 blue 0.31;
93 #declare GreenCopper = color red 0.32 green 0.49 blue 0.46;
94 #declare DkGreenCopper = color red 0.29 green 0.46 blue 0.43;
95 #declare DustyRose = color red 0.52 green 0.39 blue 0.39;
96 #declare HuntersGreen = color red 0.13 green 0.37 blue 0.31;
97 #declare Scarlet = color red 0.55 green 0.09 blue 0.09;
98 #declare Med_Purple =  color red 0.73 green 0.16 blue 0.96;
99 #declare Light_Purple = color red 0.87 green 0.58 blue 0.98;
100 #declare Very_Light_Purple = color red 0.94 green 0.81 blue 0.99;
101 Not Sure {0.90000,0.930000,0.950000}
*/
