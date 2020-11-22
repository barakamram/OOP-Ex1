package ex1.tests;

import ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class WGraphDSTest {
    @Test
    void nodeSizeAndEdgeSize() {
        weighted_graph g = new WGraph_DS();
        g.addNode(0);
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(3);//same
        assertEquals(4, g.nodeSize());
        g.connect(0, 1, 1);
        g.connect(1, 2, 2);
        g.connect(0, 3, 3);
        g.connect(0, 1, 1);//same
        assertEquals(3, g.edgeSize());
        assertEquals(g.getEdge(0, 3), g.getEdge(3, 0));
        assertEquals(3, g.getEdge(0, 3));
        assertEquals(1, g.getEdge(0, 1));
        g.connect(0, 1, 2);//update the edge
        assertEquals(2, g.getEdge(0, 1));
        assertEquals(3, g.edgeSize());
    }

    @Test
    void hasEdgeAndGetEdge() {
        weighted_graph g = smallGraphCreator();
        assertTrue(g.hasEdge(3, 2));
        assertTrue(g.hasEdge(0, 1));
        assertFalse(g.hasEdge(9, 18));//node does not exist
        assertFalse(g.hasEdge(0, 4));//no edge between nodes
        assertFalse(g.hasEdge(-8, 3));//node does not exist
        assertEquals(-1, g.getEdge(0, 4));
        assertEquals(0, g.getEdge(4, 4));//edge between node to him self
        assertEquals(0.5, g.getEdge(1, 3));
        assertEquals(0.5, g.getEdge(1, 2));
        g.connect(1, 2, 1);
        assertEquals(1, g.getEdge(1, 2));
    }

    @Test
    void AddAndRemoveNodesAndEdges() {
        weighted_graph g = smallGraphCreator();
        g.removeNode(2);
        g.removeEdge(1, 3);
        boolean flag = true;
        for (node_info n : g.getV()) {
            if (g.hasEdge(n.getKey(), 2)) flag = false;
        }
        Assertions.assertEquals(4, g.nodeSize());
        Assertions.assertEquals(2, g.edgeSize());
        Assertions.assertTrue(flag);
        g.addNode(2);
        g.addNode(2);//same
        g.connect(2, 4, 0.5);
        g.connect(2, 4, 0.7);//reconnect
        g.connect(2, 0, 0.5);
        g.connect(1, 0, 0.5);//same
        Assertions.assertEquals(0.7, g.getEdge(2, 4));//check update from 0.3->0.7
        Assertions.assertEquals(5, g.nodeSize());
        Assertions.assertEquals(4, g.edgeSize());
    }

    @Test
    void MC() {
        weighted_graph g = smallGraphCreator();
        Assertions.assertEquals(10, g.getMC());
        g.removeEdge(3, 0);
        Assertions.assertEquals(10, g.getMC());//check if mc update
        g.removeNode(1);//check if mc updates when removing Node
        Assertions.assertEquals(14, g.getMC());
        g.connect(0, 3, 2);////check if mc updates
        Assertions.assertEquals(15, g.getMC());
        g.connect(0, 3, 1.3);//check if mc update
        Assertions.assertEquals(16, g.getMC());
        g.connect(0, 3, 1.3);//check if mc does not update
        Assertions.assertEquals(16, g.getMC());
    }

    @Test
    void getV() {
        weighted_graph g0 = smallGraphCreator();
        weighted_graph g1 = smallGraphCreator();
        assertTrue(g0.getV().containsAll(g1.getV()) && g1.getV().containsAll(g0.getV()));
    }

    public static weighted_graph smallGraphCreator() {
        weighted_graph g = new WGraph_DS();
        for (int i = 0; i < 5; i++) {
            g.addNode(i);
        }
        double w = 0.5;
        g.connect(0, 1, w);
        g.connect(1, 2, w);
        g.connect(1, 3, w);
        g.connect(2, 3, w);
        g.connect(3, 4, w);
        return g;
    }

    private static Random _rnd = null;

    public static weighted_graph graphCreator(int v_size, int e_size, int seed) {
        weighted_graph g = new WGraph_DS();
        _rnd = new Random(seed);
        for (int i = 0; i < v_size; i++) {
            g.addNode(i);
        }
        int[] nodes = nodes(g);
        while (g.edgeSize() < e_size) {
            int a = nextRnd(0, v_size);
            int b = nextRnd(0, v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = _rnd.nextDouble();
            g.connect(i, j, w);
        }
        return g;
    }

    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0 + min, (double) max);
        int ans = (int) v;
        return ans;
    }

    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max - min;
        double ans = d * dx + min;
        return ans;
    }

    private static int[] nodes(weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] nodes = new node_info[size];
        V.toArray(nodes);
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }

}

