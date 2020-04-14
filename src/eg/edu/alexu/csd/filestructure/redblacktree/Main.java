package eg.edu.alexu.csd.filestructure.redblacktree;

public class Main {
    public static void main(String[] args) {
        IRedBlackTree<Integer, String> myTree = new RedBlackTree<>();
        myTree.insert(1058, "1058");
        myTree.insert(1685, "1685");
        myTree.insert(6997, "6997");
        myTree.insert(8438, "8438");
        myTree.insert(2776, "2776");
        myTree.insert(1129, "1129");
        myTree.insert(7913, "7913");
        myTree.insert(7515, "7515");
        myTree.delete(1058);
        myTree.delete(1685);
        myTree.delete(6997);
        myTree.delete(8438);
        myTree.delete(2776);
        myTree.delete(1129);
        myTree.delete(7913);
       // myTree.delete(7515);
        System.out.println(myTree.getRoot().getKey());
        System.out.println(myTree.getRoot().getValue());

    }
}
