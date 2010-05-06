package edu.washington.cs.rse.transit.common.offline;

import edu.washington.cs.rse.collections.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph<T> {

  private Map<T, Set<T>> _outboundEdges = new HashMap<T, Set<T>>();

  private Map<T, Set<T>> _inboundEdges = new HashMap<T, Set<T>>();

  public Graph() {

  }

  public Graph(Graph<T> graph) {
    for (T node : graph.getNodes())
      addNode(node);
    for (Pair<T> edge : graph.getEdges())
      addEdge(edge.getFirst(), edge.getSecond());
  }

  public Set<T> getNodes() {
    Set<T> nodes = new HashSet<T>();
    nodes.addAll(_outboundEdges.keySet());
    nodes.addAll(_inboundEdges.keySet());
    return nodes;
  }

  public Set<Pair<T>> getEdges() {
    Set<Pair<T>> edges = new HashSet<Pair<T>>();
    for (T from : _outboundEdges.keySet()) {
      for (T to : _outboundEdges.get(from))
        edges.add(Pair.createPair(from, to));
    }
    return edges;
  }

  public Set<T> getInboundNodes(T node) {
    return get(_inboundEdges, node, false);
  }

  public boolean isConnected(T from, T to) {

    if (from.equals(to))
      return true;

    for (T next : get(_outboundEdges, from, false)) {
      if (isConnected(next, to))
        return true;
    }

    return false;
  }

  public void addNode(T node) {
    get(_outboundEdges, node, true);
    get(_inboundEdges, node, true);
  }

  public void addEdge(T from, T to) {
    get(_outboundEdges, from, true).add(to);
    get(_inboundEdges, to, true).add(from);
  }

  public void removeEdge(T from, T to) {
    get(_outboundEdges, from, false).remove(to);
    get(_inboundEdges, to, false).remove(from);
  }

  private void removeNode(T node) {

    for (T from : get(_inboundEdges, node, false))
      get(_outboundEdges, from, false).remove(node);
    _inboundEdges.remove(node);

    for (T to : get(_outboundEdges, node, false))
      get(_inboundEdges, to, false).remove(node);
    _outboundEdges.remove(node);
  }

  public List<T> getTopologicalSort(Comparator<T> tieBreaker) {

    List<T> order = new ArrayList<T>();
    Graph<T> g = new Graph<T>(this);

    while (true) {
      Set<T> nodes = g.getNodes();

      if (nodes.isEmpty())
        return order;

      List<T> noInbound = new ArrayList<T>();

      for (T node : nodes) {
        if (g.getInboundNodes(node).isEmpty())
          noInbound.add(node);
      }

      if (noInbound.isEmpty())
        throw new IllegalStateException("cycle");

      if (tieBreaker != null)
        Collections.sort(noInbound, tieBreaker);

      order.addAll(noInbound);
      for (T node : noInbound)
        g.removeNode(node);
    }
  }

  private Set<T> get(Map<T, Set<T>> edges, T key, boolean create) {
    Set<T> set = edges.get(key);
    if (set == null) {
      set = new HashSet<T>();
      if (create)
        edges.put(key, set);
    }
    return set;
  }
}