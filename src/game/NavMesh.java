package game;

import java.util.ArrayList;

public class NavMesh {

    ArrayList<Node> nodes;

    public NavMesh() {
        nodes = new ArrayList<Node>();
    }

    public void addNode(Node n) {
        nodes.add(n);
    }

    public void removeNode(Node n) {
        nodes.remove(n);
        for (Node i: n.neighbours) {
            i.neighbours.remove(n);
        }
    }

    public Node getNode (int x, int y) {
        for (Node n: nodes) {
            if (n.x == x && n.y == y) {
                return n;
            }
        }
        return null;
    }

    public boolean findNode (int x, int y) {
        if (getNode(x,y) == null) return false;
        return true;
    }

    public int dist (Node n1, Node n2) {
        return (int)(Math.sqrt((n1.x - n2.x)*(n1.x - n2.x) + (n1.y - n2.y)*(n1.y - n2.y)));
    }

    public ArrayList<Node> getRoute (Node source, Node dest) {
        ArrayList<Node> unexpNodes = new ArrayList<Node>();

        for (Node n: nodes) {
            n.route.clear();
            n.cost = 1;
            n.visited = false;
        }
        unexpNodes.add(source);
        while (unexpNodes.size() != 0) {

            //search for lowest cost item of unexpNodes
            Node lowestCost = unexpNodes.get(0);
            for (Node n:unexpNodes) {
                if (n.cost + dist(source, n) < lowestCost.cost + dist(source,lowestCost)) {
                    lowestCost = n;
                }
            }

            //remove the lowest cost item from the list of unexpanded nodes and mark it as visited
            unexpNodes.remove(lowestCost);
            lowestCost.visited = true;

            //check if the destination has been reached and break the while loop if true
            if (lowestCost.x == dest.x && lowestCost.y == dest.y) {
                break;
            }
            else {
                //expand the node if it isn't the destination (by finding the node's neighbours)
                ArrayList<Node> lowestNeighbours = lowestCost.neighbours;

                for (Node n : lowestNeighbours) {
                    //check if the node hasn't been visited, store the new found route to it from the source town
                    //calculate the cost it took to get there, add it to the unexpanded nodes list and mark it
                    //as visited
                    if (!n.visited) {
                        n.cost += lowestCost.cost;
                        n.visited = true;
                        unexpNodes.add(n);
                        n.route.addAll(lowestCost.route);
                        n.route.add(lowestCost);
                    }
                    else {
                        // if the node has already been visited
                        // check if the new found route to node n is shorter than the previous one
                        // replace the route and cost if it is
                        if (n.cost > (lowestCost.cost + 1)) {
                            n.route.clear();
                            n.cost += lowestCost.cost;
                            unexpNodes.add(n);
                            n.route.addAll(lowestCost.route);
                            n.route.add(lowestCost);
                        }
                    }
                }
            }
        }

        //add the destination to the final route
        if (!dest.route.isEmpty()) {
            dest.route.add(dest);
        }

        return dest.route;
    }

    public String toString() {
        return "" + nodes.size();
    }
}
