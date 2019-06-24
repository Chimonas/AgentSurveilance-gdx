package com.mygdx.game.worldAttributes.agents;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.worldAttributes.areas.*;
import com.mygdx.game.worldAttributes.agents.guard.Guard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;

public class AStarAI{

    public ArrayList<Area> knownAreas;

    public ArrayList<Pair<Vector2, Float>> nodes; //Pair of the position of the node and of its weight
    public float[][] weights; //Matrix of edge weights between the nodes that are connected (0 if not)
    public ArrayList<Pair<Vector2, Integer>> path; //The location and the index of that location in nodes
    public ArrayList<Pair<Vector2, Integer>> crossedPath;

    public Vector2 goal;
    public Vector2 start;
    public Agent agent;
    private float weightGuardInSight = 100.0f;
    private float RADIUS_AROUND_GUARD = 3;

    public AStarAI(Agent agent, ArrayList<Area> knownAreas) {
        this.agent = agent;
        this.knownAreas = knownAreas;
        this.goal = null;
        this.start = agent.position.cpy();
        this.path = new ArrayList<Pair<Vector2, Integer>>();
        this.crossedPath = new ArrayList<Pair<Vector2, Integer>>();
    }

    public void createGraph(ArrayList<Vector2> specialWeightNode){
            this.nodes = new ArrayList<>();
            createNodesAccordingToAreas();
            connectNodes(specialWeightNode);
    }

    public void applyPath(){

        if(agent.position.dst(goal) < 0.1){
            agent.ai.newVelocity = 0.0f;
            agent.updatePosition();
        } else if(this.path.isEmpty()){
            agent.ai.newAngle = agent.getAngleBetweenTwoPos(agent.position, goal);
            agent.updatePosition();
        }
        else {
            if(agent.position.dst(path.get(path.size()-1).getLeft()) < 0.1){
                agent.ai.newAngle = agent.getAngleBetweenTwoPos(agent.position, path.get(path.size()-2).getLeft());
                crossedPath.add(path.get(path.size()-1));
                path.remove(path.size()- 1);
            }

            //If the agent is currently turning, set his velocity to 0, if not, set it to the max velocity
            if(Math.abs((this.agent.ai.newAngle%360.0f + 360.0f)%360.0f - this.agent.angleFacing) <= 0.1f)
                this.agent.ai.newVelocity = this.agent.MAX_VELOCITY;
            else this.agent.ai.newVelocity = 0.0f;

            agent.updatePosition();
        }
    }

    public void createNodesAccordingToAreas(){

        nodes.add(new Pair<Vector2, Float>(agent.position, agent.position.dst(goal)));
        for(Area a: knownAreas){
            if(a instanceof Shade || a instanceof Target){
                Vector2 node = new Vector2(a.getTopLeft().x + a.getWidth()/2,a.getBottomRight().y + a.getHeight()/2);
                Pair<Vector2, Float> tuple = new Pair<Vector2, Float>(node, node.dst(goal));
                nodes.add(tuple);
                this.agent.getWorld().addAStarGraphNode(node);
            } else {
                //Creating nodes around the corners of an area
                Vector2 node1, node2, node3, node4;
                node1 = new Vector2(a.getTopLeft().x - 0.1f, a.getTopLeft().y + 0.1f);
                node2 = new Vector2(a.getBottomRight().x + 0.1f, a.getTopLeft().y + 0.1f);
                node3 = new Vector2(a.getBottomRight().x + 0.1f, a.getBottomRight().y - 0.1f);
                node4 = new Vector2(a.getTopLeft().x - 0.1f, a.getBottomRight().y - 0.1f);
                Pair<Vector2, Float> tuple1 = new Pair<Vector2, Float>(node1, node1.dst(goal));
                Pair<Vector2, Float> tuple2 = new Pair<Vector2, Float>(node2, node2.dst(goal));
                Pair<Vector2, Float> tuple3 = new Pair<Vector2, Float>(node3, node3.dst(goal));
                Pair<Vector2, Float> tuple4 = new Pair<Vector2, Float>(node4, node4.dst(goal));
                nodes.add(tuple1);
                nodes.add(tuple2);
                nodes.add(tuple3);
                nodes.add(tuple4);
                this.agent.getWorld().addAStarGraphNode(node1);
                this.agent.getWorld().addAStarGraphNode(node2);
                this.agent.getWorld().addAStarGraphNode(node3);
                this.agent.getWorld().addAStarGraphNode(node4);
            }
        }
        weights = new float[nodes.size()][nodes.size()];
    }

    public void connectNodes(ArrayList<Vector2> specialWeightNode){

        for(int i = 0; i < nodes.size(); i++){
            for(int j = i+1; j < nodes.size(); j++){

                boolean noStraightConnection = false;
                float edgeWeight = 0;

                for(Area a:knownAreas)
                    if((a instanceof Structure || a instanceof SentryTower)&&
                        a.intersects(nodes.get(i).left, nodes.get(j).left))
                        noStraightConnection = true;

                if(!noStraightConnection){
                    edgeWeight = nodes.get(i).left.dst(nodes.get(j).left);

                    if(specialWeightNode != null)
                        for(Vector2 node:specialWeightNode)
                            if(nodes.get(i).getLeft().dst(node) < 2 || nodes.get(j).getLeft().dst(node) < 2)
                                edgeWeight += weightGuardInSight;

                    //For Visualization
   //                 this.agent.getWorld().addAStarGraphEdge(new Pair<>(nodes.get(i).left, nodes.get(j).left));
                }
                weights[j][i] = edgeWeight;
                weights[i][j] = edgeWeight;
            }
        }
    }

    private ArrayList<Pair<Vector2, Integer>> reconstructPath(ArrayList<Integer> cameFrom, int current) {

        ArrayList<Pair<Vector2, Integer>> p = new ArrayList<Pair<Vector2, Integer>>();
        p.add(new Pair<Vector2, Integer>(goal, current));
        while(!nodes.get(cameFrom.get(current)).getLeft().equals(start)) {
            p.add(new Pair<Vector2, Integer>(nodes.get(cameFrom.get(current)).getLeft(), cameFrom.get(current)));
            current = cameFrom.get(current);
        }
        p.add(new Pair<Vector2, Integer>(start, 0));
        return p;
    }

    public void aStar(){
        ArrayList<Vector2> closedSet = new ArrayList<>(); //nodes that have been evaluated
        ArrayList<Vector2> openSet = new ArrayList<>();  // discovered nodes but not evaluated yet
        ArrayList<Integer> cameFrom = new ArrayList<>(); //For each node, the most efficient node from where it can come
        float[] gScore = new float[nodes.size()]; //For each node, the cost of getting to that node from the start
        float[] fScore = new float[nodes.size()]; //For each node, the cost of going from the start to the goal by passing by that node

        for(int i=0; i<nodes.size(); i++) {
            fScore[i] = 1000000; //aka Infinity
            gScore[i] = 1000000;
            cameFrom.add(null);
        }
        gScore[0] = 0;
        fScore[0] = nodes.get(0).getRight(); //The heuristic is the euclidian distance
        openSet.add(start);

        while(!openSet.isEmpty()){
            float min = 1000000;
            int current = 0;

            //to be perfectionned
            for(int i = 0; i<nodes.size(); i++) {
                if (fScore[i] <= min && openSet.contains(nodes.get(i).left)){
                    min = fScore[i];
                    current = i;
                }
            }

            if(nodes.get(current).left.equals(goal)) {
                this.path = reconstructPath(cameFrom, current);
                break;
            }
            openSet.remove(nodes.get(current).left);
            closedSet.add(nodes.get(current).left);

            //Find neighbours of current through the weights matrix
            ArrayList<Integer> neighbours = new ArrayList<Integer>();
            for(int i = 0; i<nodes.size(); i++)
                if(weights[current][i] != 0)
                    neighbours.add(i);


            for(Integer neighbour: neighbours){

                if(closedSet.contains(nodes.get(neighbour).left)) continue; //Ignore the node that has been already evaluated

                float tentative_gScore = gScore[current] + weights[current][neighbour];

                if(!openSet.contains(nodes.get(neighbour).left)) openSet.add(nodes.get(neighbour).left); //not sure
                else if(tentative_gScore >= gScore[neighbour]) continue;

                cameFrom.set(neighbour, current);
                gScore[neighbour] = tentative_gScore;
                fScore[neighbour] = gScore[neighbour] + nodes.get(neighbour).getRight();
            }

        }

    }

    public void setGoal(Vector2 goal){
        this.goal = goal;
    }

    public Vector2 getGoal() {
        return goal;
    }


    //Taken from https://stackoverflow.com/questions/521171/a-java-collection-of-value-pairs-tuples
    public static class Pair<L,R> {

        private final L left;
        private final R right;

        public Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }

        public L getLeft() { return left; }
        public R getRight() { return right; }

        @Override
        public int hashCode() { return left.hashCode() ^ right.hashCode(); }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Pair)) return false;
            Pair pairo = (Pair) o;
            return this.left.equals(pairo.getLeft()) &&
                    this.right.equals(pairo.getRight());
        }

    }

    public boolean checkDeviations(){
        if(!agent.getVisibleGuards().isEmpty()){
            start = agent.position;
//            Vector2 dest = this.path.get(path.size()-1).getLeft(); //Current destination
            createGraph(getNodesAroundGuard());
            return true;
        }
        return false;

    }


    //TODO: visible bounds to be checke dout
    public ArrayList<Vector2> getNodesAroundGuard(){

        ArrayList<Vector2> n = new ArrayList<Vector2>();

        for(Guard guard: this.agent.getVisibleGuards())
            for(Pair<Vector2, Float> node: nodes)
                    if(guard.getPosition().dst(node.getLeft()) < RADIUS_AROUND_GUARD) {
                        n.add(node.getLeft());
                        System.out.println("Node: " + node.getLeft());
                        System.out.println("Gposition: " + guard.getPosition());
                    }


        return n;
    }

    public ArrayList<Pair<Vector2, Float>> getNodes() {
        return nodes;
    }

}
