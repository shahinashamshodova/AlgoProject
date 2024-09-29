# AlgoProject

The aim of this project was to apply graph algorithms to find shortest paths in a transport network of Vancouver. 
### Shortest Path Functionality 

I decided to use Dijkstra with Adjacency Matrix Graph Representation.

I used Adjacency Matrix because there is around 8000 stops in the input and file stop_times has 1.7 million entries (plus 5000 entries from transfers). Size of Adjacency Matrix is 8000*8000 = 64 million. We see that number of edges (1.7 million + 5000) and number vertices squared only differ by approximately one order of magnitude ( ~ 10ˆ7/10ˆ6 = 10). Hence, the graph is relatively dense. Though ideally, we would want E = O(Vˆ2) with a lower constant (in our case it is 64/1.7). 

Another benefit of Adjacency matrix implementation is that in the long run, when transport network becomes more dense it will become more efficient.

Moreover, we can add/delete edges in O(1) time which works well with previous point and also allows us to parse files quicker once memory for matrix was allocated.

If later on we wanted to add a new vertex this would require O(Vˆ2) time, however we can consider such updates to the network as one-off operations. The reason is that adding a new edge means simply extending trip of a bus or connecting 2 trips together or even creating a brand new bus trip, while adding a new vertex (stop) is equivalent to building a new stop and then changing some trips to pass through it. In real life, trips can be changed daily, while new bus stops are built not so regularly.

I decided to use Dijkstra, because:

Floyd-Warshall solves all pairs shortest path problem, but bus routes change often, so precomputing data regularly using Floyd-Warshall (O(Vˆ3)) and putting it into some sort of cache may result in many caches entries being invalid.

Bellman-Ford solves same problem as dijkstra, but can also work with negative edges. However, given the context of the problem there cannot be negative edges between stops.

My implementation of Dijkstra is using array as priority queue, so the complexity is O(Vˆ2). With min binary heap, we could get O(V + ELogV) and assuming E >> V, O(ELogV). 

### Bus Stop Search Functionality 

Ternary search tree is a most common algorithm used in autocomplete searching. Finding the last character in user's input allows us to get all of the matches. 

Time complexity for TST: O(log n + k) for lookup, insertion and deletion, where k is the string length to be looked up, inserted or deleted and n is the number of nodes in TST.

### Trips with arrival time lookup functionality

ArrayList was used to store the data about trips. Each time a query arrives linear search is performed on the data. The filtered data points are then sorted. Hence time complexity is O(N) + O(KlogK) = O(N + KlogK), where N is the total number of trips and K is the number of trips with matching arrival time. 

This could be improved by sorting the full data first. When data is sorted, binary search can be perfomed on it to find all of the relevant entries O(LogN). Extracting all of the relevant elements and sorting them by trip_id would required O(KlogK) still. Hence time complexity can be O(LogN + KLogK) with O(NlogN) precompute complexity.

Since there are many data fields for each trip, we use the in-built java sort, which is based on MergeSort. Hence, any prior sorting present in the data would not be lost due to stability.

### UI

I used simple command line application as UI, it allows to query all of the 3 functionalities described above as well as provides error handling.

