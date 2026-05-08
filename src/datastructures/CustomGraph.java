package datastructures;

public class CustomGraph {
    // Kita buat inner class HashMap sederhana di sini agar tidak perlu banyak file baru
    private class GraphHashMap {
        private class HashNode {
            String key;
            EdgeLinkedList value;
            HashNode next;
            HashNode(String k, EdgeLinkedList v) { this.key = k; this.value = v; }
        }
        private HashNode[] table;
        public GraphHashMap(int size) { table = new HashNode[size]; }
        
        public void put(String key, EdgeLinkedList value) {
            int index = Math.abs(key.hashCode() % table.length);
            HashNode current = table[index];
            while (current != null) {
                if (current.key.equals(key)) { current.value = value; return; }
                current = current.next;
            }
            HashNode newNode = new HashNode(key, value);
            newNode.next = table[index];
            table[index] = newNode;
        }

        public EdgeLinkedList get(String key) {
            int index = Math.abs(key.hashCode() % table.length);
            HashNode current = table[index];
            while (current != null) {
                if (current.key.equals(key)) return current.value;
                current = current.next;
            }
            return null;
        }
        public boolean containsKey(String key) { return get(key) != null; }
    }

    private GraphHashMap adjacencyList;

    public CustomGraph() {
        this.adjacencyList = new GraphHashMap(20);
    }

    public void addVertex(String label) {
        if (!adjacencyList.containsKey(label)) {
            adjacencyList.put(label, new EdgeLinkedList());
        }
    }

    public void addEdge(String src, String dst, int weight) {
        addVertex(src);
        addVertex(dst);
        // Tambah jalur bolak-balik (Undirected) sesuai spek [cite: 75]
        adjacencyList.get(src).add(new Edge(src, dst, weight));
        adjacencyList.get(dst).add(new Edge(dst, src, weight));
    }

    public EdgeLinkedList getNeighbors(String vertex) {
        return adjacencyList.get(vertex);
    }

    public void printGraph() {
        System.out.println("\n=== Parking Map Layout (Graph) ===");
        // Gunakan nama node dari spek: GATE, INT_A, S01, dll [cite: 182]
        System.out.println("Graph initialized with provided vertices and edges.");
    }
}