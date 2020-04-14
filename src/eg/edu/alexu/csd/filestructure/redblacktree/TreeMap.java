package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;
import java.util.*;

public class TreeMap<T extends Comparable<T>, V> implements ITreeMap<T, V> {

    private static class EntriesComparator<G extends Comparable<G>, H> implements Comparator<Map.Entry<G, H>> {

        @Override
        public int compare(Map.Entry<G, H> o1, Map.Entry<G, H> o2) {
            return o1.getKey().compareTo(o2.getKey());
        }
    }

    private static class MapEntry<Q extends Comparable<Q>, W> implements Map.Entry<Q, W> {
        private Q key;
        private W value;

        public MapEntry(Q key, W value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public Q getKey() {
            return key;
        }

        @Override
        public W getValue() {
            return value;
        }

        @Override
        public W setValue(W value) {
            this.value = value;
            return this.value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Map.Entry)) return false;
            return ((Map.Entry<?, ?>) o).getKey().equals(this.key) && ((Map.Entry<?, ?>) o).getValue().equals(this.value);
        }

    }

    private IRedBlackTree<T, V> tree;
    private int size;

    public TreeMap() {
        tree = new RedBlackTree<>();
        size = 0;
    }

    public Map.Entry<T, V> ceilingEntry(T key) {
        if (key == null) {
            throw new RuntimeErrorException(new Error("Can't find a ceil for null key"));
        }
        Map.Entry<T, V> ceil = null;
        INode<T, V> current = tree.getRoot();
        while (!current.isNull()) {
            if (current.getKey().compareTo(key) == 0) {
                ceil = new MapEntry<>(current.getKey(), current.getValue());
                break;
            } else if (current.getKey().compareTo(key) < 0) {
                current = current.getRightChild();
            } else {
                ceil = new MapEntry<>(current.getKey(), current.getValue());
                current = current.getLeftChild();

            }
        }
        return ceil;
    }


    public T ceilingKey(T key) {

        Map.Entry<T, V> ceilingEntry = ceilingEntry(key);
        if (ceilingEntry == null) {
            return null;
        }
        return ceilingEntry.getKey();
    }

    public void clear() {
        tree.clear();
        size = 0;
    }


    public boolean containsKey(T key) {
        return tree.contains(key);
    }


    public boolean containsValue(V value) {
        if (value == null) {
            throw new RuntimeErrorException(new Error("Can't find a null value!"));
        }
        Stack<INode<T, V>> dfs = new Stack<>();
        if (tree.getRoot() == null) {
            return false;
        }
        dfs.push(tree.getRoot());
        INode<T, V> tmp;
        while (!dfs.empty()) {
            tmp = dfs.pop();
            if (tmp.getValue().equals(value)) {
                return true;
            }
            if (!tmp.getRightChild().isNull()) {
                dfs.push(tmp.getRightChild());
            }
            if (!tmp.getLeftChild().isNull()) {
                dfs.push(tmp.getLeftChild());
            }
        }
        return false;
    }

    public Set<Map.Entry<T, V>> entrySet() {
        Set<Map.Entry<T, V>> result = new TreeSet<>(new EntriesComparator<>());
        Stack<INode<T, V>> dfs = new Stack<>();
        if (tree.getRoot() == null) {
            return result;
        }
        dfs.push(tree.getRoot());
        INode<T, V> tmp;
        while (!dfs.empty()) {
            tmp = dfs.pop();
            result.add(new MapEntry<>(tmp.getKey(), tmp.getValue()));
            if (!tmp.getRightChild().isNull()) {
                dfs.push(tmp.getRightChild());
            }
            if (!tmp.getLeftChild().isNull()) {
                dfs.push(tmp.getLeftChild());
            }
        }
        return result;
    }

    public Map.Entry<T, V> firstEntry() {
        if (size == 0) {
            return null;
        }
        INode<T, V> current = tree.getRoot();
        while (!current.getLeftChild().isNull()) {
            current = current.getLeftChild();
        }
        return new MapEntry<>(current.getKey(), current.getValue());
    }


    public T firstKey() {
        if (size == 0) {
            return null;
        }
        INode<T, V> current = tree.getRoot();
        while (!current.getLeftChild().isNull()) {
            current = current.getLeftChild();
        }
        return current.getKey();
    }


    public Map.Entry<T, V> floorEntry(T key) {
        if (key == null) {
            throw new RuntimeErrorException(new Error("Can't find a floor for null key"));
        }
        Map.Entry<T, V> floor = null;
        INode<T, V> current = tree.getRoot();
        while (!current.isNull()) {
            if (current.getKey().compareTo(key) == 0) {
                floor = new MapEntry<>(current.getKey(), current.getValue());
                break;
            } else if (current.getKey().compareTo(key) < 0) {
                current = current.getRightChild();
                floor = new MapEntry<>(current.getKey(), current.getValue());
            } else {
                current = current.getLeftChild();

            }
        }
        return floor;
    }


    public T floorKey(T key) {
        Map.Entry<T, V> floorEntry = floorEntry(key);
        if (floorEntry == null) {
            return null;
        }
        return floorEntry.getKey();
    }


    public V get(T key) {
        return tree.search(key);
    }


    public ArrayList<Map.Entry<T, V>> headMap(T toKey) {
        return headMap(toKey, false);
    }

    public ArrayList<Map.Entry<T, V>> headMap(T toKey, boolean inclusive) {

        INode<T, V> current = tree.getRoot();
        ArrayList<Map.Entry<T, V>> list = new ArrayList<>();
        Stack<INode<T, V>> recStack = new Stack<>();
        while (!current.isNull() || !recStack.empty()) {
            while (!current.isNull()) {
                recStack.push(current);
                current = current.getLeftChild();
            }
            current = recStack.pop();
            if (inclusive) {
                if (current.getKey().compareTo(toKey) <= 0) {
                    list.add(new MapEntry<>(current.getKey(), current.getValue()));
                } else {
                    break;
                }
            } else {
                if (current.getKey().compareTo(toKey) < 0) {
                    list.add(new MapEntry<>(current.getKey(), current.getValue()));
                }else {
                    break;
                }
            }
            current = current.getRightChild();
        }
        return list;
    }


    public Set<T> keySet() {
        Set<T> result = new TreeSet<>();
        Stack<INode<T, V>> dfs = new Stack<>();
        if (tree.getRoot() == null) {
            return result;
        }
        dfs.push(tree.getRoot());
        INode<T, V> tmp;
        while (!dfs.empty()) {
            tmp = dfs.pop();
            result.add(tmp.getKey());
            if (!tmp.getRightChild().isNull()) {
                dfs.push(tmp.getRightChild());
            }
            if (!tmp.getLeftChild().isNull()) {
                dfs.push(tmp.getLeftChild());
            }
        }
        return result;
    }

    public Map.Entry<T, V> lastEntry() {
        if (size == 0) {
            return null;
        }
        INode<T, V> current = tree.getRoot();
        while (!current.getRightChild().isNull()) {
            current = current.getRightChild();
        }
        return new MapEntry<>(current.getKey(), current.getValue());
    }


    public T lastKey() {
        if (size == 0) {
            return null;
        }
        INode<T, V> current = tree.getRoot();
        while (!current.getRightChild().isNull()) {
            current = current.getRightChild();
        }
        return current.getKey();
    }


    public Map.Entry<T, V> pollFirstEntry() {
        if (size == 0) {
            return null;
        }
        INode<T, V> current = tree.getRoot();
        while (!current.getLeftChild().isNull()) {
            current = current.getLeftChild();
        }
        remove(current.getKey());
        return new MapEntry<>(current.getKey(), current.getValue());
    }

    public Map.Entry<T, V> pollLastEntry() {
        if (size == 0) {
            return null;
        }
        INode<T, V> current = tree.getRoot();
        while (!current.getRightChild().isNull()) {
            current = current.getRightChild();
        }
        remove(current.getKey());
        return new MapEntry<>(current.getKey(), current.getValue());
    }


    public void put(T key, V value) {
        boolean increaseSize = tree.contains(key);
        tree.insert(key, value);
        if (!increaseSize) {
            size++;
        }
    }


    public void putAll(Map<T, V> map) {
        if (map == null) {
            throw new RuntimeErrorException(new Error("Can't add a keys-values from a null map"));
        }

        for (Map.Entry<T, V> entry : map.entrySet()) {
            this.put(entry.getKey(), entry.getValue());

        }
    }


    public boolean remove(T key) {
        boolean result = tree.delete(key);
        if (result) {
            size--;
            return true;
        }
        return false;

    }


    public int size() {
        return size;
    }

    public Collection<V> values() {
        return inOrderTraversal(tree.getRoot());
    }

    private Collection<V> inOrderTraversal(INode<T, V> root) {
        ArrayList<V> list = new ArrayList<>();
        INode<T, V> current = root;
        Stack<INode<T, V>> recStack = new Stack<>();
        while (!current.isNull() || !recStack.empty()) {
            while (!current.isNull()) {
                recStack.push(current);
                current = current.getLeftChild();
            }
            current = recStack.pop();
            list.add(current.getValue());
            current = current.getRightChild();
        }
        return list;
    }

}