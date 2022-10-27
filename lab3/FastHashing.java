package lab3;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 
 *
 * @author Magnus Nielsen Largely based on existing C++-laborations by Tommy
 *         Olsson and Filip Strömbäck.
 */
public class FastHashing {
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

		if (args.length != 1) {
			System.out.println("Usage: java Brute <input.txt>\n"
					+ "Replace <input.txt> with your input file of preference, and possibly the path.\n"
					+ "Ex: java Brute data/input1000.txt");
			System.exit(0);
		}

		// Opening the file containing the points.
		f = new File(args[0]);

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

		Collections.sort(points, new NaturalOrderComparator());
		// Collections.sort(points, new SlopeComparator(points.get(0)));
		// Sorting points by natural order (lexicographic order). Makes finding end
		// points of line segments easy.
		long start = System.currentTimeMillis();
		/**
		 * @author Daniel
		 * @param If >= 4 points with same slope exists: Draw a line between the points
		 *           no need to sort the slopes with this algorithm.
		 */
		double tmpD;
		int count;
		HashMap<Double, Integer> sneakySlope = new HashMap<Double, Integer>(); // unsynchronized map

		/*
		 * left "pointer": compare one point at a time with the rest to its right
		 * (right-pointer)
		 * 
		 */
		for (int i = 0; i < points.size() - 1; i++) {
			/*
			 * right-pointer: from left + 1 to end
			 */
			for (int j = i + 1; j < points.size(); j++) {

				tmpD = points.get(i).slopeTo(points.get(j));
				/*
				 * If slope exists
				 */
				if (sneakySlope.containsKey(tmpD)) {
					count = sneakySlope.get(tmpD) + 1;
					sneakySlope.put(tmpD, count);
					/*
					 * If two pairs of points have the same slope
					 */
					if (count > 2) {
						/*
						 * print line
						 */
						renderLine(frame, points.get(i), points.get(j));
					}
				} else { // init
					count = 1;
					sneakySlope.put(tmpD, count);
				}

			}
			sneakySlope.clear(); // to minimize the space in memo
		}

		long end = System.currentTimeMillis();
		System.out.println("Computing all the line segments took: " + (end - start) + " milliseconds.");

	}

	/**
	 * 
	 * @Daniel
	 * @param comparator class: sort the slopes of random points in 2D space
	 *                   compared from a fixed point
	 * @return 1 - if slope to point a > slope to point b. -1 if slope to point a <
	 *         slope to point b. 0 if equal slopes
	 */
	private static class SlopeComparator implements Comparator<Point> {
		private Point origo;

		public SlopeComparator(Point origo) {
			this.origo = origo;
		}

		public int compare(Point a, Point b) {

			return Double.compare(origo.slopeTo(a), origo.slopeTo(b));
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
