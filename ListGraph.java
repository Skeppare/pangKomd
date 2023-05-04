import java.util.*;

public class ListGraph<T> implements Graph<T> {
    private final Map<T, Set<Edge<T>>> nodes = new HashMap<>();


    public void add(T node) {
        nodes.putIfAbsent(node, new HashSet<>());
    }

    @Override
    public void connect(T node1, T node2, String name, int weight) {
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException("One or both nodes not found in graph.");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cant be negative.");
        }
        if (getEdgeBetween(node1, node2) != null) {
            throw new IllegalStateException("Nodes already have a connection.");
        }
        add(node1);
        add(node2);

        Set<Edge<T>> a = nodes.get(node1);
        Set<Edge<T>> b = nodes.get(node2);

        a.add(new Edge(node2, name, weight));
        b.add(new Edge(node1, name, weight));
    }

    @Override
    public void setConnectionWeight(T node1, T node2, int weight) {
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException("One or both nodes not found in graph.");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Weight cant be negative.");
        }
        getEdgeBetween(node1, node2).setWeight(weight);
        getEdgeBetween(node2, node1).setWeight(weight);
    }

    @Override
    public Set<T> getNodes() {
        return nodes.keySet();
    }

    @Override
    public Collection<Edge<T>> getEdgesFrom(T node) {
        Set<Edge<T>> edges = nodes.get(node);
        if (edges == null) {
            throw new NoSuchElementException("Node not in graph.");
        }
        return edges;
    }

    @Override
    public Edge<T> getEdgeBetween(T node1, T node2) {

        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException("One or both nodes not found in graph.");
        }
        for (Edge<T> edge : nodes.get(node1)) {
            if (edge.getDestination().equals(node2)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public void disconnect(T node1, T node2) {
        if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
            throw new NoSuchElementException("One or both nodes not found in graph.");
        }
        if(getEdgeBetween(node1, node2) == null){
            throw new IllegalStateException("No edge between nodes.");
        }
        Set<Edge<T>> edge1 = nodes.get(node1);
        Set<Edge<T>> edge2 = nodes.get(node2);

        for(Edge<T> edge: edge1){
            if(edge.getDestination().equals(node2)){
                edge1.remove(edge);
                break;
            }
        }
        for(Edge<T> edge: edge2){
            if(edge.getDestination().equals(node1)){
                edge2.remove(edge);
                break;
            }
        }


    }

    //    public void remove(T node) throws NoSuchElementException {
//        if(!nodes.containsKey(node)){
//            throw new NoSuchElementException("The node" + node + " doesn't exist!");
//        }
//
//        //Ta bort noden och dess kanter
//        Set<Edge<T>> edgesToRemove = nodes.get(node);
//        nodes.remove(node);
//
//        // Ta bort nodens referenser från de andra nodernas kanter
//        for (T otherNode : nodes.keySet()) {
//            if (otherNode.equals(node)) {
//                continue; // Hoppa över själva noden som redan är borttagen
//            }
//
//            Set<Edge<T>> edges = nodes.get(otherNode);
//            edges.removeAll(edgesToRemove);
//        }
//    }
    @Override
    public void remove(T node) {
        if (!nodes.containsKey(node)) {
            throw new NoSuchElementException("Node not found in graph");
        }
        for (Edge<T> edge : nodes.get(node)) {
            nodes.get(edge.getDestination()).removeIf(e -> e.getDestination().equals(node));
        }
        nodes.remove(node);
    }


    @Override
    public boolean pathExists(T a, T b){
        if(!nodes.containsKey(a) || !nodes.containsKey(b)){
            return false;
        }
        Set<T> visited = new HashSet<>();
        dfs(a, b, visited);
        return visited.contains(b);
    }

    private void dfs(T current, T searchedFor, Set<T> visited) {
        visited.add(current);
        if(current.equals(searchedFor)) {
            System.out.println("Found!");
            // return;
        }
        for (Edge<T> edge : nodes.get(current)) {
            if (!visited.contains(edge.getDestination())) {
                dfs(edge.getDestination(), searchedFor, visited);
            }
        }
    }

    @Override
    public List<Edge<T>> getPath(T from, T to) {
        if(!pathExists(from, to)){
            return null;
        }
        Map<T, T> connections = new HashMap<>();
        LinkedList<T> queue = new LinkedList<>();

        connections.put(from, null);
        queue.add(from);

        while (!queue.isEmpty()) {
            T node = queue.pollFirst();
            for (Edge<T> edge : nodes.get(node)) {
                T destination = edge.getDestination();
                if (!connections.containsKey(destination)) {
                    connections.put(destination, node);
                    queue.add(destination);
                }
            }
        }
        if (!connections.containsKey(to)) {
            throw new IllegalStateException("no connection");
        }
        return gatherPath(from, to, connections);
    }

    private List<Edge<T>> gatherPath(T from, T to, Map<T, T> connections) {
        LinkedList<Edge<T>> path = new LinkedList<>();
        T current = to;

        while (!current.equals(from)) {
            T next = connections.get(current);
            Edge edge = getEdgeBetween(next, current);
            path.addFirst(edge);
            current = next;
        }
        return path;
    }

    private Stack<T> depthFirstSearch(T current, T searchedFor, Set<T> visited, Stack<T> pathSoFar) {
        visited.add(current);
        if (current.equals(searchedFor)) {
            //pathSoFar.add(current);
            return pathSoFar;
        }
        for (Edge edge : nodes.get(current)) {
            T n = (T) edge.getDestination();
            if (!visited.contains(n)) {
                pathSoFar.push(n);
                Stack<T> p = depthFirstSearch(n, searchedFor, visited, pathSoFar);
                if (!p.isEmpty()) {
                    return p;
                } else {
                    pathSoFar.pop();
                }
            }
        }
        return new Stack<T>();
    }
    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < nodes.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append("till ");
            sb.append("");
            sb.append(" med ");
            sb.append(nodes);
            sb.append(" tar ");
            sb.append(nodes);
        }

        sb.append("]");

        return sb.toString();
    }

}
