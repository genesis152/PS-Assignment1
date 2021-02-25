package Controller;

import Model.Parcel;
import View.PostmanView;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.orthogonal.mxOrthogonalLayout;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.*;
import com.mxgraph.view.mxStylesheet;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.HamiltonianCycleAlgorithm;
import org.jgrapht.alg.tour.HeldKarpTSP;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.VertexSetListener;
import org.jgrapht.generate.CompleteGraphGenerator;
import org.jgrapht.*;
import Controller.JGraphXAdapter;
import org.jgrapht.graph.*;
import org.jgrapht.graph.builder.GraphBuilder;
import org.jgrapht.traverse.DepthFirstIterator;
import com.mxgraph.layout.*;
import com.mxgraph.swing.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;

public class GraphController {
    private Graph<Parcel, WeightedEdge> graph;
    private Supplier<String> vertexSupplier;
    private PostmanView postmanView;

    public static class WeightedEdge extends DefaultWeightedEdge {
        @Override
        public String toString() {
            return String.format("%.2f",getWeight());
        }
    }

    public GraphController(){
        emptyGraph();
    }

    public GraphController(Map<Integer, Parcel> parcelMap){
        emptyGraph();
        for (Parcel parcel : parcelMap.values()){
            addParcelToCompleteGraph(parcel);
        }
    }

    public void emptyGraph(){
        this.graph = new DefaultUndirectedWeightedGraph<Parcel, WeightedEdge>(WeightedEdge.class);
    }

    private double distance(Point p1, Point p2){
        return Math.sqrt((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) *  (p1.y - p2.y));
    }

    public void printGraph(){
        DepthFirstIterator depthFirstIterator = new DepthFirstIterator<Parcel, WeightedEdge>(graph);
        List<Parcel> parcels = new ArrayList<Parcel>();
        while(depthFirstIterator.hasNext()) {
            Parcel currentParcel = (Parcel)depthFirstIterator.next();
            parcels.add(currentParcel);
            System.out.println(currentParcel.toString());
        }
        Iterator it = graph.edgeSet().iterator();
        while(it.hasNext()){
            WeightedEdge currentEdge = (WeightedEdge)it.next();
            System.out.println(
                    graph.getEdgeSource(currentEdge).getID() + " - " +
                    graph.getEdgeTarget(currentEdge).getID() + ". cost - " +
                    Double.toString(graph.getEdgeWeight(currentEdge)));
        }

    }

    public void removeParcelFromGraph(Parcel parcel){
        graph.removeVertex(parcel);
    }

    public void updateParcelInGraph(Parcel parcel){
        graph.removeVertex(parcel);
        addParcelToCompleteGraph(parcel);
    }

    private Graph convertGraph(){
        GraphPath<Parcel,WeightedEdge> hamiltonianPath = new HeldKarpTSP<Parcel,WeightedEdge>().getTour(graph);
        DefaultUndirectedWeightedGraph<Parcel, WeightedEdge> auxGraph = new DefaultUndirectedWeightedGraph<Parcel, WeightedEdge>(WeightedEdge.class);
        List<Parcel> parcelList = hamiltonianPath.getVertexList();
        List<WeightedEdge> edgesList = hamiltonianPath.getEdgeList();
        for(Parcel parcel : parcelList){
            auxGraph.addVertex(parcel);
        }
        for(WeightedEdge edge : edgesList){
            WeightedEdge auxEdge = auxGraph.addEdge(graph.getEdgeSource(edge), graph.getEdgeTarget(edge));
            auxGraph.setEdgeWeight(auxEdge, graph.getEdgeWeight(edge));
        }
        ListenableGraph<Parcel, WeightedEdge> g =
                new DefaultListenableGraph<Parcel, WeightedEdge>(auxGraph);

        return g;
    }

    public Object saveGraphAsLayout(){
        Graph convertedGraph = convertGraph();
        return createContainer(convertedGraph);
    }

    public Object createContainer(Graph graph){
        JGraphXAdapter<Parcel, WeightedEdge> graphAdapter =
                new JGraphXAdapter<Parcel,WeightedEdge>(graph);

//        graphAdapter.setCellsMovable(false);
//        graphAdapter.setEdgeLabelsMovable(false);
//        graphAdapter.setCellsDeletable(false);
//        graphAdapter.setCellsDisconnectable(false);
//        graphAdapter.setCellsResizable(false);
//        graphAdapter.setCellsBendable(false);

        JFrame frame = new JFrame("Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mxGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        mxGraphComponent graphComponent = new mxGraphComponent(graphAdapter);
        mxGraphModel graphModel  = (mxGraphModel)graphComponent.getGraph().getModel();
        Collection<Object> cells =  graphModel.getCells().values();
        mxUtils.setCellStyles(graphComponent.getGraph().getModel(),
                cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);


        frame.getContentPane().add(graphComponent,BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        return frame.getContentPane();
    }

    public void addParcelToCompleteGraph(Parcel newVertex){
        this.graph.addVertex(newVertex);
        DepthFirstIterator depthFirstIterator = new DepthFirstIterator<Parcel, WeightedEdge>(graph);
        while(depthFirstIterator.hasNext()){
            Parcel currentVertex = (Parcel)depthFirstIterator.next();
            if(!currentVertex.equals(newVertex)) {
                WeightedEdge edge = graph.addEdge(currentVertex, newVertex);
                graph.setEdgeWeight(edge, distance(currentVertex.getCoordinates(), newVertex.getCoordinates()));
            }
        }
    }
}
