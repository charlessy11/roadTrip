This prject uses Dijkstra’s algorithm to find the shortest route from source vertex to target vertex via any stops inputted by user with fastest possible running time. 
The program produces the locally-optimal choice which results in the shortest possible path.
For instance, assume the user will supply a starting city (San Francisco, for example) and an ending city (eg. Miami). In addition, the user will supply zero, one or more events or places of interest by name, such as the Grand Canyon, Paul Bunyon and Babe the Blue Ox in Minnesota or the Statue of Liberty in New York.
Hence, the program finds a route from the starting city to the ending city in a way that visits each of the events or places of interest while keeping the number of miles driven to a minimum. 
In the example from above, the solution must return a route from San Francisco through the following places, in order:
● Grand Canyon AZ
● Bemidji MN
● New York NY
..., in that order, finally ending in Miami.
