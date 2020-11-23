An implemantion of undirectional weighted graph in Java. 
the algorithm is used to copy any graph, to retrieve if the graph is connected or not, to calculate and retrieve the shortest path from any vertice on the graph to an other vertice.

*****************************************************************************************************


myWeightedGraph displays the construction of the graph.

* `addNode(node_data n)` a method that adds a new node to the graph with the given node.

* `removeNode(int key)` a method that deletes this node from the graph and removes all edges which starts or ends at this node.

* `connect(int node1, int node2)` a method that connects an edge between node1 and node2.

* `removeEdge(int node1, int node2)` a method delete the edge between node1 and node2 from the graph.

* `hasEdge(int node1, int node2)` a boolean method that returns true if there is an edge between node1 and node2.

* `getNode (int node1, int node2)` a method that returns the weight of the edge between node 1 and node2.

* `nodeSize()` a method  that returns the number of nodes (vertices) in the graph.

* `edgeSize()` a method that return the number of edges in the graph.

* `getV()` a method that returns a collection of all nodes (vertices) in the graph.

* `getV(int node_id)` a method that returns a collection of all neighbors of the node.


myWeightedGraphAlgo represents the graph that the algorithm is used on.

* `init(graph g)` a function that initilize the algorithm on given graph.

* `copy()` a function to make a deep copy of the graph included the edges.

* `isConnected()` a boolean function to check if the function is connected. it's meaning that every node in the graph is reachable to each other node in the graph.

* `shortestPathDist`  a function that inkove Dijkstra algorithm, the function return the length of the shortest path between the source and the destination. 

* `shortestPath` a function that invoke Dijkstra algorithm, the function return the shortest path by list of nodes found between the source and the destination.

* `save(file)` a function that save the graph to a file and return true if the graph successfully saved and false if the graph unsuccessfully saved

* `load(file)` a function that load a file and build a graph and return true if the graph successfully loaded and false if the graph unsuccessfully loaded



