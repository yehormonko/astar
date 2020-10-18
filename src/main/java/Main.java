import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Main extends Application {

    public static void main(String[] args) {

        Application.launch(args);
    }

    int rowNum = 100;
    int colNum = 100;
    HashMap<String, Node> nodes = new HashMap<String, Node>();
    HashMap<String, Node> lets = new HashMap<String, Node>();
    int mode = 0;
    Node start;
    Node end;
    Color normalColor = Color.rgb(238, 238, 238);
    Color startColor = Color.rgb(116, 223, 77);
    Color endColor = Color.rgb(125, 127, 247);

    public void start(final Stage primaryStage) {
        final GridPane grid = new GridPane();
        final Button drawLet = new Button("Draw let");
        final Button drawStart = new Button("Draw start");
        final Button drawEnd = new Button("Draw end");
        final Button startButton = new Button("Start");
        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "1x1",
                        "2x2"
                );
        final ComboBox size = new ComboBox(options);
        size.getSelectionModel().selectFirst();
        Button restart = new Button("Clear");
        restart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                lets=new HashMap<>();
                nodes = new HashMap<>();
                start = null;
                end = null;
                mode = 0;
                start(primaryStage);
            }
        });
        startButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                try {
                    if (size.getSelectionModel().isSelected(1)) {
                        checkPseudoLets();
                    }
                    startSearch();
                    drawStart.setDisable(true);
                    drawEnd.setDisable(true);
                    drawLet.setDisable(true);
                    startButton.setDisable(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        drawLet.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (mode != 1) {
                    mode = 1;
                    drawLet.setStyle("-fx-background-color: #00ff00");
                    drawStart.setStyle("");
                    drawEnd.setStyle("");
                } else if (mode == 1) {
                    mode = 0;
                    drawLet.setStyle("");
                }
            }
        });
        drawStart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (mode != 2) {
                    mode = 2;
                    drawLet.setStyle("");
                    drawEnd.setStyle("");
                    drawStart.setStyle("-fx-background-color: #00ff00");
                } else if (mode == 2) {
                    mode = 0;
                    drawStart.setStyle("");
                }
            }
        });
        drawEnd.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                if (mode != 3) {
                    mode = 3;
                    drawLet.setStyle("");
                    drawStart.setStyle("");
                    drawEnd.setStyle("-fx-background-color: #00ff00");
                } else if (mode == 3) {
                    mode = 0;
                    drawEnd.setStyle("");
                }
            }
        });
        for (int row = 0; row < rowNum; row++) {
            for (int col = 0; col < colNum; col++) {
                Node rec = new Node();
                rec.setWidth(5);
                rec.setHeight(5);
                rec.setFill(normalColor);
                rec.setStroke(Color.gray(0.6));
                GridPane.setRowIndex(rec, row);
                GridPane.setColumnIndex(rec, col);
                grid.getChildren().addAll(rec);
                rec.setId(row + "x" + col);
                nodes.put(rec.getId(), rec);
                rec.setRow(row);
                rec.setCol(col);
                if (row > 0 & row < rowNum) {
                    Node up = nodes.get((row - 1) + "x" + col);
                    rec.putNearest(up);
                    up.putNearest(rec);
                }
                if (col > 0 & col < colNum) {
                    Node left = nodes.get(row + "x" + (col - 1));
                    rec.putNearest(left);
                    left.putNearest(rec);
                }
                rec.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        Node rectangle = (Node) event.getSource();
                        int r = rectangle.getRow();
                        int c = rectangle.getCol();
                        if (mode == 1) {
                            addLet(rectangle);
                        } else if (mode == 2) {
                            if (start != null) {
                                start.setFill(normalColor);
                            }
                            start = rectangle;
                            rectangle.setFill(startColor);
                        } else if (mode == 3) {
                            if (end != null) {
                                end.setFill(normalColor);
                            }
                            end = rectangle;
                            rectangle.setFill(endColor);
                        }
                    }
                });
            }
        }
        GridPane buttonPane = new GridPane();
        buttonPane.add(drawLet, 0, 0);
        Label sizeLabel = new Label("Size:");
        buttonPane.add(sizeLabel, 0, 1);
        buttonPane.add(size, 0, 2);
        buttonPane.add(drawStart, 0, 3);
        buttonPane.add(drawEnd, 0, 4);
        buttonPane.add(startButton, 0, 5);
        buttonPane.add(restart, 0, 6);
        buttonPane.setHgap(10);
        buttonPane.setVgap(10);
        buttonPane.setPadding(new Insets(0, 10, 0, 10));
        BorderPane root = new BorderPane();
        root.setLeft(grid);
        root.setRight(buttonPane);
        Scene scene = new Scene(root, 700, 650);
        primaryStage.setTitle("Grid");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void addLet(Node node) {
        List<Node> nearest = node.getNearest();
        if (start != null && end != null)
            if (node.getId().equals(start.getId()) | node.getId().equals(end.getId())) return;
        if (node.isLet()) {
            node.setFill(normalColor);
            node.setLet(false);
            for (Node n : nearest) {
                n.putNearest(node);
            }
        } else {
            node.setFill(Color.rgb(137, 137, 137));
            node.setLet(true);
            lets.put(node.getId(), node);
            for (Node n : nearest) {
                List<Node> nn = n.getNearest();
                nn.remove(node);
            }
        }
    }

    public void checkPseudoLets() {
        for (Node node : lets.values()) {
            List<Node> nearest = node.getNearest();
            int col = node.getCol();
            int row = node.getRow();
            for (Node n : nearest) {
                boolean isFound = false;
                for (Node nn : n.getNearest()) {
                    boolean isOther = false;
                    if (nn.getRow() == row || nn.getCol() == col) {
                        isFound = true;
                    }
                }
                if (!isFound) {
                    n.setFill(Color.YELLOW);
                    n.setLet(true);
                    for (Node nd : n.getNearest()) {
                        List<Node> nn = nd.getNearest();
                        nn.remove(n);
                    }
                }
            }
        }
    }


    public void startSearch() throws InterruptedException {
        Task<Void> task = new Task<Void>() {
            protected Void call() throws Exception {
                start.setCost(0);
                Stack<Node> reachable = new Stack<Node>();
                reachable.push(start);
                List<Node> explored = new ArrayList<Node>();
                Node node;
                while (!reachable.isEmpty()) {
                    node = chooseNode(reachable);
                    node.setFill(Color.rgb(166, 255, 199));
                    if (node.getId().equals(end.getId())) {
                        drawPath(node);
                        return null;
                    }
                    reachable.remove(node);
                    explored.add(node);
                    List<Node> newReachable = node.getNearNodes();
                    newReachable.removeAll(explored);
                    for (Node adjacent : newReachable) {
                        if (!reachable.contains(adjacent)) {
                            adjacent.setFill(Color.rgb(218, 244, 249));
                            reachable.push(adjacent);
                        }
                        if (node.getCost() + 1 < adjacent.getCost()) {
                            adjacent.setCost(node.getCost() + 1);
                            adjacent.setPrevious(node);
                        }
                    }
                    Thread.sleep(20);
                }
                return null;
            }
        };
        Thread search = new Thread(task);
        search.setDaemon(true);
        search.start();
    }

    public void drawPath(Node end) {
        Node node = end.getPrevious();
        while (node != null) {
            if (node.getId().equals(start.getId())) {
                start.setFill(startColor);
                end.setFill(endColor);
                return;
            }
            node.setFill(Color.rgb(255, 110, 110));
            node = node.getPrevious();
        }
    }

    public Node chooseNode(Stack<Node> reachable) {
        Stack<Node> toCheck = (Stack<Node>) reachable.clone();
        int minCost = Integer.MAX_VALUE;
        int dd = 0;
        Node bestNode = null;
        for (int i = 0; i < reachable.size(); i++) {
            Node node = toCheck.pop();
            int d = Math.abs(node.getRow() - end.getRow()) + Math.abs(node.getCol() - end.getCol());
            int dist = node.getCost() + d;
            if (dist < minCost) {
                minCost = dist;
                bestNode = node;
                dd = d;
            }
        }
        System.out.println(bestNode.getId() + " " + dd);
        return bestNode;
    }

}