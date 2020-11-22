package ex1.src;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
/**
 * This interface represents an undirectional weighted graph.
 * It should support a large number of nodes (over 10^6, with average degree of 10).
 * The implementation should be based on an efficient compact representation
 * (should NOT be based on a n*n matrix).
 *
 */
public class WGraph_DS implements weighted_graph,java.io.Serializable {
    private int MC;
    private HashMap<Integer, node_info> myWeightedGraph;
    private int numOfNodes;
    private int numOfEdges;
    //copy constructor
    public WGraph_DS(){
        this.myWeightedGraph=new HashMap<>();
        numOfNodes = 0;
        numOfEdges = 0;
        MC = 0;
    }
    /**
     * return the node_data by the node_id,
     * @param key - the node_id
     * @return the node_data by the node_id, null if none.
     */
    @Override
    public node_info getNode(int key) {
        return myWeightedGraph.get(key);
    }
    /**
     * return true iff (if and only if) there is an edge between node1 and node2
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        boolean hasEdge = false;
        if (myWeightedGraph.containsKey(node1) && myWeightedGraph.containsKey(node2)) {
            if (((NodeInfo) myWeightedGraph.get(node1)).hashNi.values().contains(myWeightedGraph.get(node2))) hasEdge = true;
        }
        return hasEdge;
    }
    /**
     * return the weight if the edge (node1, node1). In case
     * there is no such edge - should return -1
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public double getEdge(int node1, int node2) {
        double w = -1;
        NodeInfo nd= new NodeInfo((NodeInfo) myWeightedGraph.get(node1));
        if (node1==node2) w=0;
        if(hasEdge(node1,node2))
            w=nd.weightOfEdges.get(node2);
        return w;
    }
    /**
     * add a new node to the graph with the given key.
     * Note: this method should run in O(1) time.
     * Note2: if there is already a node with such a key -> no action should be performed.
     * @param key
     */
    @Override
    public void addNode(int key) {
        if (!myWeightedGraph.containsKey(key)){
            NodeInfo nd= new NodeInfo(key);
            nd.hashNi = new HashMap<>();
            nd.weightOfEdges = new HashMap<>();
            NodeInfo n= new NodeInfo(nd);
            myWeightedGraph.put(key,n);
            numOfNodes++;
            MC++;
        }
    }
    /**
     * Connect an edge between node1 and node2, with an edge with weight >=0.
     * Note: this method should run in O(1) time.
     * Note2: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     */
    @Override
    public void connect(int node1, int node2, double w) {
        WGraph_DS.NodeInfo nd1 = (NodeInfo) myWeightedGraph.get(node1);
        WGraph_DS.NodeInfo nd2 = (NodeInfo) myWeightedGraph.get(node2);
        if (hasEdge(node1,node2) && w!=nd1.weightOfEdges.get(nd2.getKey())) {
            nd1.weightOfEdges.remove(node2);
            nd2.weightOfEdges.remove(node1);
            nd1.weightOfEdges.put(node2,w);
            nd2.weightOfEdges.put(node1,w);
            MC++;
        }

        if (!hasEdge(node1,node2) && node1!=node2) {
            if (w>=0) {
                nd1.hashNi.put(node2,nd2);
                nd2.hashNi.put(node1,nd1);
                nd1.weightOfEdges.put(node2,w);
                nd2.weightOfEdges.put(node1,w);
                numOfEdges++;
                MC++;
            }
        }
    }
    /**
     * This method return a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * Note: this method should run in O(1) tim
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV() {
        return myWeightedGraph.values();
    }
    /**
     *
     * This method returns a Collection containing all the
     * nodes connected to node_id
     * Note: this method can run in O(k) time, k - being the degree of node_id.
     * @return Collection<node_data>
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        NodeInfo node= (NodeInfo) myWeightedGraph.get(node_id);
        if (node.hashNi==null) return null;
        else return node.hashNi.values();
    }
    /**
     * Delete the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(n), |V|=n, as all the edges should be removed.
     * @return the data of the removed node (null if none).
     * @param key
     */
    @Override
    public node_info removeNode(int key) {
        if (myWeightedGraph.containsKey(key)) {
            NodeInfo node = (NodeInfo) myWeightedGraph.get(key);
            numOfNodes--;
            MC++;
            int Ni_size = node.hashNi.size();
            numOfEdges = numOfEdges - Ni_size;
            node_info[] niNodes = node.hashNi.values().toArray(new node_info[Ni_size]);
            NodeInfo nd;
            for (node_info n: niNodes) {
                nd = (NodeInfo) n;
                nd.hashNi.remove(node.getKey());
                nd.weightOfEdges.remove(node.getKey());
                MC++;
            }
            myWeightedGraph.remove(key);
        }
        return myWeightedGraph.get(key);
    }
    /**
     * Delete the edge from the graph,
     * Note: this method should run in O(1) time.
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (hasEdge(node1,node2)) {
            NodeInfo nd1= (NodeInfo) myWeightedGraph.get(node1);
            NodeInfo nd2= (NodeInfo) myWeightedGraph.get(node2);
            nd1.hashNi.remove(node2);
            nd2.hashNi.remove(node1);
            nd1.weightOfEdges.remove(node2);
            nd2.weightOfEdges.remove(node1);
            numOfEdges--;
            MC++;
        }
    }
    /** return the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int nodeSize() {
        return numOfNodes;
    }
    /**
     * return the number of edges (undirectional graph).
     * Note: this method should run in O(1) time.
     * @return
     */
    @Override
    public int edgeSize() {
        return numOfEdges;
    }
    /**
     * return the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph should cause an increment in the ModeCount
     * @return
     */
    @Override
    public int getMC() {
        return MC;
    }

    public String toString(){ return String.valueOf(this.myWeightedGraph.values()); }

    public boolean equals(Object g) {
        if (this == g) return true;
        if (g == null || getClass() != g.getClass()) return false;
        WGraph_DS graph = (WGraph_DS) g;
        return myWeightedGraph.equals(graph.myWeightedGraph);
    }

    public int hashCode() {
        return Objects.hash(myWeightedGraph, MC);
    }


    /**
     *
     *
     *
     */
    private class NodeInfo implements node_info,java.io.Serializable {
        private int key_id;
        private double tag;
        private String info;
        private HashMap<Integer, Double> weightOfEdges;
        private HashMap<Integer, node_info> hashNi;
        //copy constructor
        public NodeInfo(NodeInfo node) {
            this.key_id = node.key_id;
            this.info = node.info;
            this.tag = node.tag;
            this.hashNi = node.hashNi;
            this.weightOfEdges = node.weightOfEdges;
        }
        //copy constructor by key
        public NodeInfo(int key) {
            setKey(key);
        }

        public void setKey(int key) {
            this.key_id = key;
        }
        /**
         * Return the key (id) associated with this node.
         * Note: each node_data should have a unique key.
         * @return
         */
        @Override
        public int getKey() {
            return this.key_id;
        }
        /**
         * return the remark (meta data) associated with this node.
         * @return
         */
        @Override
        public String getInfo() {
            return this.info;
        }
        /**
         * Allows changing the remark (meta data) associated with this node.
         * @param s
         */
        @Override
        public void setInfo(String s) {
            this.info = s;
        }
        /**
         * Temporal data (aka distance, color, or state)
         * which can be used be algorithms
         * @return
         */
        @Override
        public double getTag() {
            return this.tag;
        }
        /**
         * Allow setting the "tag" value for temporal marking an node - common
         * practice for marking by algorithms.
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        public String toString() {
            return String.valueOf(this.getKey());
        }

        public boolean equals(Object n) {
            if (this == n) return true;
            if (n == null || getClass() != n.getClass()) return false;
            NodeInfo node= (NodeInfo) n;
            return key_id == node.key_id;
        }

        public int hashCode() {
            return Objects.hash(key_id);
        }
    }

}
