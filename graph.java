import java.util.*;
public class graph
{
    Hashtable<String,List<String>> adjacentList;
    List<edge> weightedEdge;

    public graph()
    {
        adjacentList = new Hashtable<>();
        weightedEdge = new ArrayList();
    }
    public class edge
    {
        String locA;
        String locB;
        int distance;
        int time;
        public edge( String locA, String locB, int distance, int time)
        {
            this.locA = locA;
            this.locB = locB;
            this.distance = distance;
            this.time = time;
        }
    }
    public void addNode(String location)
    {
        adjacentList.putIfAbsent(location,new ArrayList<>());
    }
    public void addEdge(String locA, String locB, int distance, int time)
    {
        addNode(locA);
        addNode(locB);
        adjacentList.get(locA).add(locB);
        adjacentList.get(locB).add(locA);
        weightedEdge.add(new edge(locA,locB,distance,time));
    }
    public void showConnections()
    {
        String connections ="";
        for(edge edge: weightedEdge)
        {
            connections += edge.locA+ "--->"+edge.locB+" "+edge.distance+ " "+edge.time+"\n";
        }
        System.out.println(connections);
    }
}
