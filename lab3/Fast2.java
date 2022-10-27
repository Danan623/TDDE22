package lab3;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.function.ToDoubleFunction;

/**
 * Brute force solution. To run: java brute.java < input.txt
 *
 * @author Magnus Nielsen Largely based on existing C++-laborations by Tommy
 *         Olsson and Filip StrÃ¶mbÃ¤ck.
 */
public class Fast2 {
	/**
	 * Clear the window and paint all the Points in the plane.
	 *
	 * @param frame  - The window / frame.
	 * @param points - The points to render.
	 */
	private static void render(JFrame frame, ArrayList<Point> points) {
		frame.removeAll();
		frame.setVisible(true);

		for (Point p : points) {
			p.paintComponent(frame.getGraphics(), frame.getWidth(), frame.getHeight());
		}
	}

	/**
	 * Draw a line between two points in the window / frame.
	 *
	 * @param frame - The frame / window in which you wish to draw the line.
	 * @param p1    - The first Point.
	 * @param p2    - The second Point.
	 */
	private static void renderLine(JFrame frame, Point p1, Point p2) {
		p1.lineTo(p2, frame.getGraphics(), frame.getWidth(), frame.getHeight());
	}

	/**
	 * Read all the points from the buffer in the input scanner.
	 *
	 * @param input - Scanner containing a buffer from which to read the points.
	 * @return ArrayList<Point> containing all points defined in the file / buffer.
	 */
	private static ArrayList<Point> getPoints(Scanner input) {
		int count = input.nextInt();
		ArrayList<Point> res = new ArrayList<>();
		for (int i = 0; i < count; ++i) {
			res.add(new Point(input.nextInt(), input.nextInt())); // Point(X, Y) ..
		}

		return res;
	}

	public static void main(String[] args) throws InterruptedException {
		JFrame frame;
		Scanner input = null;
		File f;
		ArrayList<Point> points;
		/*
		 * if (args.length != 1) { System.out.println("Usage: java Brute <input.txt>\n"
		 * +
		 * "Replace <input.txt> with your input file of preference, and possibly the path.\n"
		 * + "Ex: java Brute data/input1000.txt"); System.exit(0); }
		 */
		// Opening the file containing the points.
		// f = new File(args[0]);
		f = new File("C:\\Users\\Daniel\\eclipse-workspace\\tdde22\\src\\lab3\\data\\grid5x5.txt"); // egen
																									// anordning
		try {
			input = new Scanner(f);
		} catch (FileNotFoundException e) {
			System.err.println("Failed to open file. Try giving a correct file / file path.");
		}

		// Creating frame for painting.
		frame = new JFrame();
		frame.setMinimumSize(new Dimension(512, 512));
		frame.setPreferredSize(new Dimension(512, 512));

		// Getting the points and painting them in the window.
		points = getPoints(input);
		render(frame, points);

		// Sorting points by natural order (lexicographic order). Makes finding end
		// points of line segments easy.
		Collections.sort(points, new NaturalOrderComparator());

		// long start = System.currentTimeMillis();
		ArrayList<Point> tmp = new ArrayList<>(points); // kopierar pointsArrayen
//		int c = 0;
//		for (Point a : tmp) {
//			c++;
//			System.out.println(c + ". " + " x = " + a.getx() + " y = " + a.gety());
//		}
//		c = 0;
//
//		System.out.println();
////		for (Point pointer : points) {
////
////			Collections.sort(tmp, new SlopeComparator(pointer));
////
////		}
//		Collections.sort(tmp, new SlopeComparator(points.get(1)));
//		Collections.sort(tmp, new NaturalOrderComparator());
//		
//		for (Point b : tmp) {
//			c++;
//			System.out.println(c + ". " + " x = " + b.getx() + " y = " + b.gety());
//	}
		long start = System.currentTimeMillis();
		double tmpSlope;
		double check = -1;
		//int flag;
		for (int i = 0; i < tmp.size(); i++) {
	
			Collections.sort(tmp, new SlopeComparator(tmp.get(i)));// sorterar efter en punkt i taget
			System.out.println("#---------------------------------------------------------#" + i);
			//HashMap<Double,Integer> tmpD = new HashMap<Double,Integer>(); // arraylist double
			int flag = 0;
			for (int j = 1; j < tmp.size(); j++) {

				tmpSlope = tmp.get(0).slopeTo(tmp.get(j));// gör en tillfällig tmp variabel
//				if(flag == -1) {
//					check = tmpSlope; // initiera första kontroll
//					flag = 0;
//				}
				if(check == tmpSlope) {
					flag++; // hittat lika lutning
					
					if(flag > 1) {			
						renderLine(frame, tmp.get(0), tmp.get(j));
					}
				}else {
					check = tmpSlope;
					flag = 0;		}	
				
//				if (tmpD.containsKey(tmpSlope)) {
//					count = tmpD.get(tmpSlope) + 1;
//					tmpD.put(tmp.get(0).slopeTo(tmp.get(j)), count); // addera varje element i arraylist tmp
//					//count++;
//					if (count > 2) {
//						renderLine(frame, tmp.get(0), tmp.get(j));
//					}
//				} else {
//					count = 1;
//					tmpD.put(tmp.get(0).slopeTo(tmp.get(j)), count); // addera varje element i arraylist tmp
//					
//				}

				System.out.println(tmpSlope);
			}
			System.out.println("Pointer på index " + i + "\n");
		}

		// System.out.println(tmp);

//		double tmpD;
//		int count;
//		for (int i = 0; i < points.size() - 1; i++) {// jmf från första till nästsista element. "Vänsterpekare"
//			HashMap<Double, Integer> sneakySlope = new HashMap<Double, Integer>(); // hashmap är snabbare.
//			// Hashtable<Double, Integer> sneakySlope = new Hashtable<Double, Integer>(); //
//			// för varje nytt "i" skapar vi nytt hashtable
//			for (int j = i + 1; j < points.size(); j++) { // jmf från andra- till sista element. "Högerpekare"
//
//				tmpD = points.get(i).slopeTo(points.get(j));
//
//				if (sneakySlope.containsKey(tmpD)) {// om lutningen finns, kommer endast finnas ett m värde för varje
//													// lutning (ty en punkt i taget) "m = Y - dy/dx * X"
//					count = sneakySlope.get(tmpD) + 1; // count = value + 1
//					sneakySlope.put(tmpD, count); // put(key,value)
//
//					if (count > 2) { // 4 i rad (eller fler^^): (P1 jmf med P2) => count = 1, P1->P2->P3 => count = 2
//										// osv
//						renderLine(frame, points.get(i), points.get(j)); // Pi -> Pj + k : ritar ut linjen
//					}
//				} else {// initialisera ny lutning
//					count = 1;
//					sneakySlope.put(tmpD, count); // initialvärde, count = antal element av samma sort
//				}
//
//				// System.out.println("i = " + i + " lutning = " + tmpD);
//			}
//
//		}

		//////////////////////////////////////////////////////////////////////
		// Your code goes here. Draw any lines you find using the function //
		// renderLine. Look at Brute.java if you are unsure if how to use //
		// any functions. Good luck! //
		//////////////////////////////////////////////////////////////////////

		long end = System.currentTimeMillis();
		System.out.println("Computing all the line segments took: " + (end - start) + " milliseconds.");

	}

	private static class SlopeComparator implements Comparator<Point> {
		private Point pointer;

		public SlopeComparator(Point pointer) {
			this.pointer = pointer;
		}

		public int compare(Point a, Point b) {
			return Double.compare(pointer.slopeTo(a), pointer.slopeTo(b));
		}

	}

	/**
	 * Comparator class. Used to tell Collections.sort how to compare objects of a
	 * non standard class. When you make your own Comparator, keep in mind that this
	 * is a class like any other, and can contain data and other methods if you deem
	 * it useful.
	 */
	private static class NaturalOrderComparator implements Comparator<Point> {
		public int compare(Point a, Point b) {
			if (a.greaterThan(b)) {
				// a is "greater" than b.
				return 1;
			} else if (a.lessThan(b)) {
				// a is "less" than b.
				return -1;
			}
			// our two points are equal.
			return 0;

		}

	}
}
