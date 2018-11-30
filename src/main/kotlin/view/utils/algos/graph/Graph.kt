package view.utils.algos.graph

/**
 * Used for topological sorting and detection of circular dependencies.
 *
 * One usage of this class is for detecting circular dependencies between positioning rule of RelativeLayout children
 * and figuring which child to render first.
 */
class Graph<T: Any> {

    companion object {

        fun <T: Any> fromEdges(edges: List<Pair<T, T>>): Graph<T> {
            val graph = Graph<T>()
            edges.forEach { graph.addEdge(it.first, it.second) }
            return graph
        }
    }

    private val graph: MutableList<MutableList<Int>> = mutableListOf()
    private val nodeIndexMapping = mutableMapOf<Int, T>()
    private var maxNodeIndex = 0

    fun addEdge(from: T, to: T) {
        val fromIndex = labelToIndex(from)
        val toIndex = labelToIndex(to)
        if (fromIndex > graph.size-1) graph.add(mutableListOf()) // new node
        if (toIndex > graph.size-1) graph.add(mutableListOf()) // new node
        if (!graph[fromIndex].contains(toIndex)) graph[fromIndex].add(toIndex) // new edge
    }

    fun topologicalSort(): List<T> {
        val inDegree = Array(graph.size) { 0 }
        for (i in 0 until graph.size) {
            for (j in 0 until graph[i].size) {
                inDegree[graph[i][j]]++
            }
        }

        var visited = 0
        val queue = (0 until graph.size).filter { inDegree[it] == 0 }.toMutableList()
        val sorted = mutableListOf<Int>()
        while (!queue.isEmpty()) {
            val node = queue.removeAt(0)
            sorted += node
            for (neighbour in graph[node]) {
                inDegree[neighbour]--
                if (inDegree[neighbour] == 0) {
                    queue.add(neighbour)
                }
            }
            visited++
        }

        if (visited != graph.size) {
            throw IllegalStateException("Graph is not a DAG")
        }

        val sortedLabels = sorted.map { indexToLabel(it) }
        return sortedLabels
    }

    private fun indexToLabel(index: Int) = nodeIndexMapping[index] ?: throw NoSuchElementException()

    private fun labelToIndex(label: T): Int {
        return if (nodeIndexMapping.containsValue(label)) {
            nodeIndexMapping.keys.first { nodeIndexMapping[it] == label }
        } else {
            val index = maxNodeIndex
            nodeIndexMapping[index] = label
            maxNodeIndex++
            index
        }
    }
}