package lab2;
import org.w3c.dom.Node;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.Scanner;
import java.io.*;

/**
 * VisualizeAVL is a visualization tool partially based on old tree
 * visualization tools from earlier iterations of the course. You do
 * not need to study or understand this class, it's solely a utility
 * for you to use.
 *
 * @author Magnus Nielsen, Tommy Färnqvist.
 */
public class VisualizeAVL extends JPanel implements ActionListener {

    public static final long serialVersionUID = 3L;
    private AVLTree tree = null;
    private HashMap<AVLTreeNode, Rectangle> nodeLocations = null;
    private HashMap<AVLTreeNode, Dimension> subtreeSizes = null;
    static private boolean dirty = true;
    private int parent2child = 20, child2child = 30;
    private Dimension empty = new Dimension(0, 0);
    private FontMetrics fm = null;

    public VisualizeAVL(AVLTree tree) {
        this.tree = tree;
        nodeLocations = new HashMap<AVLTreeNode, Rectangle>();
        subtreeSizes = new HashMap<AVLTreeNode, Dimension>();
    }

    /**
     * Repaints the tree when an action has been performed (insert or
     * remove).
     */ 
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    /**
     * Calculates the node locations.
     */
    private void calculateLocations() {
        nodeLocations.clear();
        subtreeSizes.clear();
        AVLTreeNode root = tree.getRoot();
        if (root != null) {
            calculateSubtreeSize(root);
            calculateLocation(root, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
        }
    }

    /**
     * Calculate the size of a subtree rooted at n
     *
     * @param n - root of subtree (AVLTreeNode)
     * @return - returns the dimension of the sub tree (Dimension, Swing lib) 
     */
    private Dimension calculateSubtreeSize(AVLTreeNode n) {
        if (n == null)
            return new Dimension(0, 0);
        Dimension ld = calculateSubtreeSize(n.getLeft());
        Dimension rd = calculateSubtreeSize(n.getRight());
        int h = fm.getHeight() + parent2child + Math.max(ld.height, rd.height);
        int w = ld.width + child2child + rd.width;
        Dimension d = new Dimension(w, h);
        subtreeSizes.put(n, d);
        return d;
    }

    /**
     * Calculate the location of the nodes in the subtree rooted at n
     *
     * @param n - root of subtree (AVLTreeNode)
     * @param left - used to calculate how far to the left to place the sub tree (int)
     * @param right - used to calculate how far to the right to place the sub tree (int)
     * @param top - used to calculate how far from the top to place the sub tree (int)
     */
    private void calculateLocation(AVLTreeNode n, int left, int right, int top) {
        if (n == null)
            return;
        Dimension ld = (Dimension) subtreeSizes.get(n.getLeft());
        if (ld == null)
            ld = empty;
        Dimension rd = (Dimension) subtreeSizes.get(n.getRight());
        if (rd == null)
            rd = empty;
        int center = 0;
        if (right != Integer.MAX_VALUE)
            center = right - rd.width - child2child / 2;
        else if (left != Integer.MAX_VALUE)
            center = left + ld.width + child2child / 2;
        int width = fm.stringWidth("" + n.getElement());
        Rectangle r = new Rectangle(center - width / 2 - 3, top, width + 6, fm
				    .getHeight());
        nodeLocations.put(n, r);
        calculateLocation(n.getLeft(), Integer.MAX_VALUE, center - child2child / 2,
			  top + fm.getHeight() + parent2child);
        calculateLocation(n.getRight(), center + child2child / 2, Integer.MAX_VALUE,
			  top + fm.getHeight() + parent2child);
    }

    /**
     * Draw the tree using the pre-calculated locations.
     * 
     * @param g - graphics object from Swing library (Graphics2D)
     * @param n - root of subtree (AVLTreeNode)
     * @param px - position on the x axis (int)
     * @param py - position on the y axis (int)
     * @param yoffs - offset on the y axis (int)
     */
    private void drawTree(Graphics2D g, AVLTreeNode n, int px, int py, int yoffs) {
        if (n == null)
            return;
        Rectangle r = (Rectangle) nodeLocations.get(n);
        g.draw(r);
        g.drawString("" + n.getElement(), r.x + 3, r.y + yoffs);
        if (px != Integer.MAX_VALUE)
            g.drawLine(px, py, r.x + r.width / 2, r.y);
        drawTree(g, n.getLeft(), r.x + r.width / 2, r.y + r.height, yoffs);
        drawTree(g, n.getRight(), r.x + r.width / 2, r.y + r.height, yoffs);
    }

    /**
     * Paints the entire tree. 
     *
     * @param g - graphics object from Swing library (Graphics2D)
     */
    public void paint(Graphics g) {
        super.paint(g);
        fm = g.getFontMetrics();
        // if node locations not calculated
        if (dirty) {
            calculateLocations();
            dirty = false;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(getWidth() / 2, parent2child);
        drawTree(g2d, tree.getRoot(), Integer.MAX_VALUE, Integer.MAX_VALUE, fm
		 .getLeading()
		 + fm.getAscent());
        fm = null;
    }

    /**
     * Print a menu for the user.
     */
    static void printMenu() {
        final String newline = System.getProperty("line.separator");

        String strMenu = "+--- Binary trees ---" + newline;
        strMenu += "r : Reset tree" + newline;
        strMenu += "i : Insert key" + newline;
        strMenu += "f : Find key" + newline;
        strMenu += "d : Delete key" + newline;
        strMenu += "h : Height of key" + newline;
        strMenu += "q : Quit program" + newline;
        strMenu += "x : show this text" + newline;
        System.out.print(strMenu);
    }

    /**
     * Get the next char from the scanner.
     *
     * @param in - scanner object linked to a data stream (Scanner, java.util)
     * @return - char containing the first char in the scanner stream.
     */
    static char getChar(Scanner in) {
        return in.next().charAt(0);
    }

    /**
     * Get the next int from the scanner.
     *
     * @param in - scanner object linked to a data stream (Scanner, java.util)
     * @return - int containing the first int in the scanner stream.
     */
    static int getInt(Scanner in) {
	return in.nextInt();
    }

    /**
     * Get the next string (line) from the scanner.
     *
     * @param in - scanner object linked to a data stream (Scanner, java.util)
     * @return - string containing the first line in the scanner stream.
     */    
    static String getString(Scanner in) {
        return in.nextLine();
    }

    /**
     * Fetch the entire contents of a text file, and return it in a String.
     *
     * @param aFile - The file from which to extract the contents (File)
     * @return - string object containing the contents from the file (String)
     */
    static public String getContents(File aFile) {

        StringBuilder contents = new StringBuilder();

        try {
            // use buffering, reading one line at a time

            BufferedReader input = new BufferedReader(new FileReader(aFile));
            try {
                String line = null; // not declared within while loop

                while ((line = input.readLine()) != null) {
                    contents.append(line);
                    contents.append(System.getProperty("line.separator"));
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return contents.toString();
    }

    public static void main(String[] args) {
	boolean textMode = false;
	if (args.length == 1 && args[0].equals("--text-mode")) {
	    textMode = true;
	}

	AVLTree<Integer> tree = new AVLTree<>();

	JFrame f = new JFrame("AVL");
	f.getContentPane().add(new VisualizeAVL(tree));
	// create and add an event handler for window closing event
	f.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
	f.setBounds(50, 50, 600, 400);
	if (!textMode) {
	    f.setVisible(true);
	}

	char c;
	int k;
	String v;
	Scanner in = new Scanner(System.in);
	printMenu();
	for (;;) {
	    System.out.print("lab > ");
	    System.out.flush();
	    c = getChar(in);

	    switch (c) {
	    case 'r':
	    case '6':
		tree = new AVLTree<>();
		f.dispose();
		f = new JFrame("AVL");
		f.getContentPane().add(new VisualizeAVL(tree));
		// create and add an event handler for window closing event
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
			    System.exit(0);
			}
		    });
		f.setBounds(50, 50, 600, 400);
		if (!textMode) {
		    f.setVisible(true);
		}
		break;
	    case 'h':
		System.out.println("Height of key (int): ");
		System.out.flush();
		k = getInt(in);
		System.out.println("The node: " + k + " has the height: " + tree.getNodeHeight(k));
		break;
	    case 'i':
	    case '1':
		System.out.print("Insert key (int): ");
		System.out.flush();
		k = getInt(in);
		tree.insert(k);
		System.out
		    .print("Inserted key=" + k + "\n");
		System.out.flush();
		dirty = true;
		f.repaint();
		break;
	    case 'd':
	    case '2':
		System.out.print("Delete key (int): ");
		System.out.flush();
		k = getInt(in);
		tree.remove(k);
		dirty = true;
		f.repaint();
		break;
	    case 'f':
		System.out.print("Find key (int): ");
		System.out.flush();
		k = getInt(in);
		Integer str = tree.find(k);
		if (str != null)
		    System.out.print("Found key:" + k + "\n");
		else
		    System.out.print("Key:" + k + " not found\n");
		break;
	    case 'p':
	    case '8':
		tree.print();
		break;
	    case 'q':
	    case '0':
		System.out.println("Program terminated.");
		System.exit(0);
		break;
	    case 'x':
		printMenu();
		break;
	    default:
		System.out.print("**** Sorry, No menu item named '");
		System.out.print(c + "'\n");
		System.out.flush();
	    }
	}
    }
}
