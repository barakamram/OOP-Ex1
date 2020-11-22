package ex1.tests;

import ex1.src.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class WGraphAlgoTest {
    @Test
    void isConnected() {
        WGraph_DS g = new WGraph_DS(); // create a new graph
        g.addNode(1);
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.connect(1,2,2);
        g.connect(1,3,6);
        g.connect(2,4,4.5);
        WGraph_Algo ag = new WGraph_Algo(); // create a graph algo
        ag.init(g);
        assertTrue(ag.isConnected());
        g.removeNode(1);
        assertFalse(ag.isConnected());
        g.connect(2,3,13);
        assertTrue(ag.isConnected());
    }

    @Test
    void shortestPathDist() {
        weighted_graph g0 = smallGraphCreator();
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());
        assertEquals(ag0.shortestPathDist(1,10), 19.5);
        assertEquals(ag0.shortestPathDist(2,10), 2);
        assertEquals(ag0.shortestPathDist(4,3), 8);
    }
    @Test
    void shortestPath() {
        weighted_graph g = smallGraphCreator();
        weighted_graph_algorithms ag = new WGraph_Algo();
        ag.init(g);
        List<node_info> sp = ag.shortestPath(1,10);
        int[] arr = {1,4,6,8,3,9,2,10};
        int i = 0;
        for(node_info n: sp) {
            assertEquals(n.getKey(), arr[i]);
            i++;
        }
    }

    @Test
    void save_load() {
        weighted_graph g0 = WGraphDSTest.graphCreator(10,23,1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        String str = "g0.obj";
        ag0.save(str);
        weighted_graph g1 = WGraphDSTest.graphCreator(10,23,1);
        ag0.load(str);
        assertEquals(g0,g1);
        g0.removeNode(7);
        assertNotEquals(g0,g1);
    }

    private weighted_graph smallGraphCreator() {
        weighted_graph graph = new WGraph_DS();
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(graph);
        for (int i = 1; i <= 10; i++) {
            graph.addNode(i);
        }
        System.out.println(graph.toString());
        graph.connect(4, 1, 3);
        graph.connect(4, 3, 11);
        graph.connect(4, 5, 1);
        graph.connect(4, 6, 2);
        graph.connect(6, 8, 5);
        graph.connect(8, 3, 1);
        graph.connect(7, 3, 5);
        graph.connect(3, 2, 7);
        graph.connect(9, 2, 4.5);
        graph.connect(9, 3, 2);
        graph.connect(2, 10, 2);

        return graph;
    }
}

