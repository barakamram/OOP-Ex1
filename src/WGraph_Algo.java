package ex1.src;

import java.io.*;
import java.util.*;
/**
 * This interface represents an Undirected (positive) Weighted Graph Theory algorithms including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected();
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file);
 * 6. Load(file);
 *
 * @author boaz.benmoshe
 *
 */
public class WGraph_Algo implements weighted_graph_algorithms,java.io.Serializable {
    private weighted_graph myWeightedGraphAlgo;

    public WGraph_Algo() {
        myWeightedGraphAlgo = new WGraph_DS();
    }

    public WGraph_Algo(weighted_graph g0) {
        myWeightedGraphAlgo = g0;
    }
    /**
     * Init the graph on which this set of algorithms operates on.
     * @param g
     */
    @Override
    public void init(weighted_graph g) {
        if (g==null) myWeightedGraphAlgo =new WGraph_DS();
        this.myWeightedGraphAlgo = g;
    }
    /**
     * Return the underlying graph of which this class works.
     * @return
     */
    @Override
    public weighted_graph getGraph() {
        return myWeightedGraphAlgo;
    }
    /**
     * Compute a deep copy of this weighted graph.
     * @return
     */
    @Override
    public weighted_graph copy() {
        weighted_graph g = new WGraph_DS();
        for (node_info nd:myWeightedGraphAlgo.getV()) {
            g.addNode(nd.getKey());
            for (node_info node: myWeightedGraphAlgo.getV(nd.getKey())) {
                g.addNode(node.getKey());
                double w = myWeightedGraphAlgo.getEdge(nd.getKey(),node.getKey());
                    g.connect(nd.getKey(), node.getKey(), w);
                }
            }
        return g;
    }
    /**
     * Returns true if and only if (iff) there is a valid path from EVREY node to each
     * other node. NOTE: assume ubdirectional graph.
     * @return
     */
    @Override
    public boolean isConnected() {
        boolean isConnected = true;
        if (myWeightedGraphAlgo.getV().isEmpty()) return true;
        Iterator<node_info> i=myWeightedGraphAlgo.getV().iterator();
        node_info node=i.next();
        dijkstras(myWeightedGraphAlgo, node.getKey());
        while (i.hasNext()) {
            node_info n = i.next();
            if (n.getTag()==-1) isConnected = false;
        }
        return isConnected;
    }
    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        double path_Dist;
        if (myWeightedGraphAlgo.getNode(src) == null || myWeightedGraphAlgo.getNode(dest) == null) return -1;
        if (src==dest) path_Dist = 0;
        else {
            if (myWeightedGraphAlgo.getV(src).isEmpty() || myWeightedGraphAlgo.getV(dest).isEmpty()) path_Dist = -1;
            else {
                dijkstras(myWeightedGraphAlgo, src);
                if (myWeightedGraphAlgo.getNode(dest).getTag() == -1) path_Dist = -1;
                else path_Dist = myWeightedGraphAlgo.getNode(dest).getTag();
            }
        }
        return path_Dist;
    }
    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        LinkedList<node_info> queue = new LinkedList<>();
        double x= myWeightedGraphAlgo.nodeSize();
        node_info node = myWeightedGraphAlgo.getNode(dest);
        if (myWeightedGraphAlgo.getNode(src) == null || node == null) return null;

        queue.push(node);
        if (src==dest) return queue;
        HashMap<node_info, node_info> prevNode= dijkstras(myWeightedGraphAlgo, src);
        for (int i = 0; i < x; i++) {
            node = prevNode.get(node);
            if (node==null) return null;
            queue.push(node);
            if (node.getKey() == src) break;
        }
        if (node.getKey()!=src) return null;
        if (queue.size()==1) return null;
        return queue;
    }
    /**
     * Saves this weighted (undirected) graph to the given
     * file name
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this.myWeightedGraphAlgo);
            fileOutputStream.close();
            objectOutputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            FileInputStream fileInputStream=new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            weighted_graph g = (weighted_graph) objectInputStream.readObject();

            fileInputStream.close();
            objectInputStream.close();
            myWeightedGraphAlgo=g;
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * This method helps to the other functions in this graph algorithm.
     * @param g - our graph
     * @param src - start node
     * @return hashmap of the parents of the nodes,
     * every node has a paren while the src is the root
     * (this for the shortestPath function)
     */
     public HashMap<node_info,node_info> dijkstras(weighted_graph g, int src) {
        LinkedList<Integer> queue = new LinkedList();
        node_info[] nodes = g.getV().toArray(new node_info[g.nodeSize()]);
        HashMap<node_info,node_info> prevNode=new HashMap<>();
        for (node_info n : nodes){
            n.setTag(-1);
            n.setInfo("white");
            prevNode.put(n,null);
        }
        queue.add(src);
        myWeightedGraphAlgo.getNode(src).setTag(0);
        while (queue.size() != 0) {
            int u = queue.poll();
            g.getNode(u).setInfo("black");
            for (node_info n : g.getV(u)) {

                if (!n.getInfo().equals("black")) {
                    if(!queue.contains(n.getKey())) queue.add(n.getKey());
                    if(n.getTag() > g.getNode(u).getTag()+ g.getEdge(u,n.getKey()) || n.getTag() ==-1) {
                        n.setTag(g.getNode(u).getTag() + g.getEdge(u, n.getKey()));
                        prevNode.replace(n,g.getNode(u));
                    }
                }
                if(n.getTag() > g.getNode(u).getTag()+ g.getEdge(u,n.getKey())) {
                    n.setTag(g.getNode(u).getTag() + g.getEdge(u, n.getKey()));
                    prevNode.replace(n,g.getNode(u));
                    if (!queue.contains(n.getKey())) queue.add(n.getKey());
                }
            }
        }
        return prevNode;
    }

    public boolean equals(Object ga) {
        if (this == ga) return true;
        if (ga == null || getClass() != ga.getClass()) return false;
        WGraph_Algo graph = (WGraph_Algo) ga;
        return myWeightedGraphAlgo.equals(graph.myWeightedGraphAlgo);
    }

    public int hashCode() {
        return Objects.hash(myWeightedGraphAlgo);
    }
}

