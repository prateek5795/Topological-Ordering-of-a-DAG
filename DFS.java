/*
 * 	Prateek Sarna - pxs180012
 * 	Bharath Rudra - bxr180008
 */

package rbk;

import rbk.Graph.Vertex;
import rbk.Graph.AdjList;
import rbk.Graph.Edge;
import rbk.Graph.GraphAlgorithm;
import rbk.Graph.Factory;
import rbk.Graph.Timer;

import java.io.File;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

public class DFS extends GraphAlgorithm<DFS.DFSVertex> { // Class to implement depth firth search algorithm for
															// topological sorting of a graph

	private static List<Graph.Vertex> finishList; // List to store the vertices visited
	public boolean isCyclic; // boolean variable to check if a particular vertex has cycles

	public static class DFSVertex implements Factory { // class to initialize the values of vertex properties
		int cno;
		public boolean seen; // variable to check if a vertex is visited or not
		public boolean parentSeen; // boolean varibale to check if there are cycles in the graph

		public DFSVertex(Vertex u) { // DFSVertex class constructor
			seen = false; // initially all vertices are set to false as they are unvisited
			parentSeen = false;
		}

		public DFSVertex make(Vertex u) {
			return new DFSVertex(u);
		}
	}

	public DFS(Graph g) { // DFS Class constructor
		super(g, new DFSVertex(null));
		finishList = new LinkedList<Vertex>();
		isCyclic = false;
	}

	public static DFS depthFirstSearch(Graph g) { // method to implement the dfs algorithm for graph g
		DFS d = new DFS(g); // object of the dfs class

		for (Graph.Vertex u : g) {
			DFS_Visit(u, d); // call to DFS_Visit to mark the vertices as visited
		}

		return d;
	}

	public static void DFS_Visit(Vertex u, DFS d) { // method to visit each vertex recursively
		if (d.get(u).parentSeen) { // if parentseen is true the graph has cycles
			d.isCyclic = true;
			finishList = null;
			return;
		}

		if (!d.get(u).seen) {
			d.get(u).seen = true; // Each vertex visited is marked true
			d.get(u).parentSeen = true;

			for (Edge e : d.g.incident(u)) { // iterating through the edges connecting two vertices
				Vertex v = e.otherEnd(u);
				DFS.DFS_Visit(v, d); // visit the adjacent vertices of v
			}

			if (!d.isCyclic)
				((LinkedList<Vertex>) finishList).addFirst(u); // add each vertex visited to the starting of the list to
																// get topological ordering of the list

			d.get(u).parentSeen = false; // making parentSeen of every vertex as false after checking for cycles
		}
	}

	// Member function to find topological order
	public List<Vertex> topologicalOrder1() {
		if (!g.isDirected() || isCyclic) { // checking if the graph is directed or not
			return null; // returns null if the graph is undirected
		}
		depthFirstSearch(g);
		return finishList; // returns the list containing topological ordering of elements

	}

	// Find the number of connected components of the graph g by running dfs.
	// Enter the component number of each vertex u in u.cno.
	// Note that the graph g is available as a class field via GraphAlgorithm.
	public int connectedComponents() {
		return 0;
	}

	// After running the connected components algorithm, the component no of each
	// vertex can be queried.
	public int cno(Vertex u) {
		return get(u).cno;
	}

	// Find topological oder of a DAG using DFS. Returns null if g is not a DAG.
	public static List<Vertex> topologicalOrder1(Graph g) {
		DFS d = new DFS(g);
		return d.topologicalOrder1();
	}

	// Find topological oder of a DAG using the second algorithm. Returns null if g
	// is not a DAG.
	public static List<Vertex> topologicalOrder2(Graph g) {
		return null;
	}

	public static void main(String[] args) throws Exception {
		String string = "4 4	1 2 1	1 3 1	1 4 1	3 4 1 ";
		Scanner in;
		// If there is a command line argument, use it as file from which
		// input is read, otherwise use input from string.
		in = args.length > 0 ? new Scanner(new File(args[0])) : new Scanner(string);

		// Read graph from input
		Graph g = Graph.readDirectedGraph(in);
		g.printGraph(false);

		DFS d = new DFS(g);
		int numcc = d.connectedComponents();
		System.out.println("Number of components: " + numcc + "\nu\tcno");
		for (Vertex u : g) {
			System.out.println(u + "\t" + d.cno(u));
		}
		Timer t = new Timer();
		t.start();
		System.out.println(d.topologicalOrder1(g));
		t.end();
		System.out.println(t);
	}
}
