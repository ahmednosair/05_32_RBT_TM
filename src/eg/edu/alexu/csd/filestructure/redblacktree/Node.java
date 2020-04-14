package eg.edu.alexu.csd.filestructure.redblacktree;

public class Node<T extends Comparable<T>,V> implements INode<T, V>{
    static final boolean RED   = true;
    static final boolean BLACK = false;
    private T key;
    private V value;
    private boolean color;
    private INode<T,V> parent;
    private INode<T,V> leftChild;
    private INode<T,V> rightChild;
    public Node(T key,V value,boolean color,INode<T,V> parent,INode<T,V> leftChild,INode<T,V> rightChild){
        this.parent=parent;
        this.leftChild=leftChild;
        this.rightChild=rightChild;
        this.key=key;
        this.value= value;
        if(color)
            this.color=RED;
        else
            this.color=BLACK;
    }


    public void setParent(INode<T, V> parent){
        this.parent= parent;
    }

    public INode<T, V> getParent(){
        return parent;
    }

    public void setLeftChild(INode<T, V> leftChild){
        this.leftChild=leftChild;
    }

    public INode<T, V> getLeftChild(){
        return leftChild;
    }

    public void setRightChild(INode<T, V> rightChild){
        this.rightChild=rightChild;
    }

    public INode<T, V> getRightChild(){
        return rightChild;
    }

    public T getKey(){
        return key;
    }
    public void setKey(T key){
        this.key=key;
    }

    public V getValue(){
        return value;
    }
    public void setValue(V value){
        this.value=value;
    }

    public boolean getColor(){
        return color;
    }
    public void setColor(boolean color){
        if(color)
            this.color=RED;
        else
            this.color=BLACK;
    }

    public boolean isNull(){
        if(key==null){
            return true;
        }else {
            return false;
        }
    }
}
