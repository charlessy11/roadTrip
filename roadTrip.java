import java.util.*;
import java.io.BufferedReader;
import java.io.*;

public class roadTrip extends graph
{
    Hashtable<String, String> location;
    Hashtable<String, Boolean> isVisited;
    Hashtable<String, String> prevLocation;
    Hashtable<String, Integer> distance;

    HashSet<String> cityList; //list that stores places
    int totDistance; //total distance traveled
    int totTime; //total time traveled
    graph graph; //contains edges and the vertices for the adjacent list.

    public roadTrip() {
        location = new Hashtable<>();
        isVisited = new Hashtable<>();
        prevLocation = new Hashtable<>();
        distance = new Hashtable<>();
        cityList = new HashSet<>();
        graph = new graph();
        totDistance = 0;
        totTime = 0;
    }
    //function that reads and parses 'roads.csv' and stores places to cityList
    public void roads(String roadFile) throws Exception {
        String roads = roadFile;
        String roadContent = "";

        try (BufferedReader read = new BufferedReader(new FileReader(roads))) {
            while ((roadContent = read.readLine()) != null) {
                String[] line = roadContent.split(",");
                Integer distance = Integer.parseInt(line[2]);
                //check for errors
                if (line[3].equals("10a")) {
                    line[3] = "100";
                }
                Integer time = Integer.parseInt(line[3]);
                if (line[0] != null && line[1] != null) {
                    //construct graph using adjacency list
                    graph.addEdge(line[0], line[1], distance, time);
                    //add places to cityList hash set
                    cityList.add(line[0]);
                    cityList.add(line[1]);
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: File not found.");
        }
    }
    //function that reads 'attractions.csv'
    public void Attractions(String attractionFile) throws Exception {
        String attraction = attractionFile;
        String attractionContent = "";

        try (BufferedReader read = new BufferedReader(new FileReader(attraction))) {
            while ((attractionContent = read.readLine()) != null) {
                String[] line = attractionContent.split(",");
                location.put(line[0],line[1]);
            }
        } catch (Exception e) {
            System.out.println("ERROR: File not found.");
            System.exit(0);
        }
        location.remove("Attraction");
    }
    //function that takes the user-supplied starting city, ending city and list of attractions
    //this function returns a list representing the (shortest route) the user should take
    List<String> route(String starting_city, String ending_city, List<String> attractions)
    {
        ArrayList<String> path = new ArrayList<>();
        graph.addEdge(starting_city,starting_city,0,0);
        Iterator<String> cityIndex = cityList.iterator();

        //use dijkstra's algorithm to find shortest path
        while(cityIndex.hasNext())
        {
            String city = cityIndex.next();
            if(city!=null)
            {
                isVisited.put(city,false);
                distance.put(city,Integer.MAX_VALUE);
            }
        }
        distance.put(starting_city, 0);

        visit(cityList);

        ArrayList<Integer> sortAttractions = new ArrayList<>(); //list that sorts the attractions based on distance
        Hashtable<Integer, String> estimator = new Hashtable<>(); //hashtable that estimates distance from one attraction to another
        Iterator<String> attractionList = attractions.iterator();
        ArrayList<String> sortedList = new ArrayList<>(); //list that stores sorted cities
        //loop through list of attractions
        while(attractionList.hasNext())
        {
            String attractionIndex = attractionList.next();
            sortAttractions.add(distance.get(location.get(attractionIndex)));
            estimator.put(distance.get(location.get(attractionIndex)),attractionIndex);
        }

        Collections.sort(sortAttractions);

        for(int index : sortAttractions)
        {
            sortedList.add(location.get(estimator.get(index)));
        }

        sortedList.add(0,starting_city);
        //check if last attraction is at ending location
        if(sortedList.contains(ending_city)) {
            sortedList.remove(ending_city);
            sortedList.add(ending_city);
        }
        else {
            sortedList.add(ending_city);
        }
        //create stack
        Stack locationList = new Stack();

        for(int i = 0;i < sortedList.size()-1; i++)
        {
            String currentAttraction = sortedList.get(i);
            String nextAttraction = sortedList.get(i+1);
            String temp = sortedList.get(i+1);

            locationList.push(nextAttraction);

            while(!currentAttraction.equals(nextAttraction))
            {
                String prevAttraction = prevLocation.get(nextAttraction);
                totDistance += getDistance(nextAttraction,prevAttraction);
                totTime += getTime(nextAttraction,prevAttraction);
                locationList.add(prevAttraction);
                nextAttraction = prevLocation.get(nextAttraction);
            }

            while(!locationList.isEmpty())
            {
                path.add((String)locationList.pop());
            }

            isVisited = new Hashtable<>();
            prevLocation = new Hashtable<>();
            distance = new Hashtable<>();
            for(String cities: cityList)
            {
                if(cities!= null)
                {
                    isVisited.put(cities,false);
                    distance.put(cities,Integer.MAX_VALUE);
                }
            }
            distance.put(temp,0);
            visit(cityList);
        }
        return path;
    }
    //function that finds the smallest unknown vertex (for dijkstra's algorithm)
    private String smallestUnknownVertex()
    {
        String vertex ="";
        int smallest = Integer.MAX_VALUE;

        for (String cities:cityList)
        {
            if(!isVisited.get(cities) && distance.get(cities) <= smallest)
            {
                smallest = distance.get(cities);
                vertex = cities;
            }
        }
        return vertex;
    }
    //function that sets location to 'known' if visited already (for dijkstra's algorithm)
    private void knownLocation(String vertex)
    {
        if(vertex!= null)
        {
            isVisited.put(vertex, true);
        }
    }
    //function that returns the distance (weight) from one vertex to another
    private int getDistance(String vertex1, String vertex2)
    {
        int distance = 0;
        for(edge edgePoint: graph.edgeCases)
        {
            if(edgePoint.pointA.equals(vertex1)&&edgePoint.pointB.equals(vertex2))
            {
                return edgePoint.weight;
            }
            else if (edgePoint.pointA.equals(vertex2)&&edgePoint.pointB.equals(vertex1))
            {
                return edgePoint.weight;
            }
        }
        return distance;
    }
    //function that returns the amount of time taken from one vertex to another
    private int getTime(String vertex1, String vertex2)
    {
        int time = 0;
        for(edge edgePoint: graph.edgeCases)
        {
            if(edgePoint.pointA.equals(vertex1)&&edgePoint.pointB.equals(vertex2))
            {
                return edgePoint.minutes;
            }
            else if (edgePoint.pointA.equals(vertex2)&&edgePoint.pointB.equals(vertex1))
            {
                return edgePoint.minutes;
            }
        }
        return time;
    }
    //function that visits cityList and updates hashtable respectively
    private void visit(HashSet<String> cityList)
    {
        for(String cities: cityList)
        {
            while(!isVisited.get(cities))
            {
                String vertex = smallestUnknownVertex();
                knownLocation(vertex);
                for(String value: graph.adjacentList.get(vertex))
                {
                    int weight = getDistance(vertex,value);
                    if(distance.get(value)>distance.get(vertex)+weight&&!value.equals(vertex))
                    {
                        distance.put(value,distance.get(vertex)+weight);
                        prevLocation.put(value, vertex);
                    }
                }
            }
        }
    }
    //function that prints shortest path, total distance, and total time
    public void printRoads(List<String> routes)
    {
        System.out.println("Shortest Path: "+routes.toString());
        System.out.println("Total Distance: "+ totDistance+ " miles");
        System.out.println("Total Time: "+ totTime+ " minutes");
    }
    public static void main(String[] args) throws Exception {
        String roadsFile = args[0]; //roads.csv
        String attractionsFile = args[1]; //attractions.csv
        roadTrip plan = new roadTrip();
        plan.Attractions(attractionsFile);
        plan.roads(roadsFile);
        List<String> attractionPlans= new ArrayList<>();
        attractionPlans.add("Statue of Liberty");
        List<String> path = plan.route("Miami FL", "Boston MA",attractionPlans);
        plan.printRoads(path);
    }
}