import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Node extends Rectangle {
    private int col;
    private int row;
    private boolean isLet;
    private int cost = Integer.MAX_VALUE;
    private Node previous;
    private List<Node> nearest = new ArrayList<Node>();

    public void putNearest(Node node){
        nearest.add(node);
    }
    public List<Node> getNearest() {
        return nearest;
    }

    public void setNearest(List<Node> nearest) {
        this.nearest = nearest;
    }

    public Node getPrevious() {
        return previous;
    }

    public void setPrevious(Node previous) {
        this.previous = previous;
    }

    public List<Node> getNearNodes(){
        List<Node> nodes = new ArrayList<Node>();
        if(up!=null) nodes.add(up);
        if(down!=null) nodes.add(down);
        if(left!=null) nodes.add(left);
        if(right!=null) nodes.add(right);
        return nearest;
    }
    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean isLet() {
        return isLet;
    }

    public void setLet(boolean let) {
        isLet = let;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    private Node up;
    private Node down;
    private Node left;
    private Node right;

//    public Node getUp() {
//        return up;
//    }
//
//    public void setUp(Node up) {
//        this.up = up;
//    }
//
//    public Node getDown() {
//        return down;
//    }
//
//    public void setDown(Node down) {
//        this.down = down;
//    }
//
//    public Node getLeft() {
//        return left;
//    }
//
//    public void setLeft(Node left) {
//        this.left = left;
//    }
//
//    public Node getRight() {
//        return right;
//    }
//
//    public void setRight(Node right) {
//        this.right = right;
//    }
}
