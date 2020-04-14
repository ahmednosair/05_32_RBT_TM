package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;

public class RedBlackTree<T extends Comparable<T>, V> implements IRedBlackTree<T, V> {
    INode<T, V> root;

    RedBlackTree() {
        root = new Node<T, V>(null, null, INode.BLACK, null, null, null);
        root.setRightChild(new Node<T, V>(null, null, INode.BLACK, root, null, null));
        root.setLeftChild(new Node<T, V>(null, null, INode.BLACK, root, null, null));
    }

    /**
     * return the root of the given Red black tree.
     *
     * @return root.
     */
    public INode<T, V> getRoot() {
        return root;
    }

    /**
     * return whether the given tree isEmpty or not.
     *
     * @return boolean represent the state of the tree.
     */
    public boolean isEmpty() {
        return (root.isNull());
    }

    /**
     * Clear all keys in the given tree.
     */
    public void clear() {
        root = new Node<T, V>(null, null, INode.BLACK, null, null, null);
        root.setRightChild(new Node<T, V>(null, null, INode.BLACK, root, null, null));
        root.setLeftChild(new Node<T, V>(null, null, INode.BLACK, root, null, null));
    }

    /**
     * return the value associated with the given key or null if no value is found.
     *
     * @param key to search.
     * @return value associated with this key.
     */
    public V search(T key) {
        if (key == null) {
            throw new RuntimeErrorException(new Error("Can't search for a null key!"));
        }
        if (root.isNull()) {
            return null;
        }
        INode<T, V> current = root;
        while (!current.isNull()) {
            if (current.getKey().compareTo(key) == 0) {
                return current.getValue();
            } else if (current.getKey().compareTo(key) < 0) {
                current = current.getRightChild();
            } else {
                current = current.getLeftChild();
            }
        }
        return null;
    }

    /**
     * return true if the tree contains the given key and false otherwise.
     *
     * @param key to search.
     * @return found key in tree or not.
     */
    public boolean contains(T key) {
        if (key == null) {
            throw new RuntimeErrorException(new Error("Can't check for a null key"));
        }
        INode<T, V> current = root;
        if (root.isNull()) {
            return false;
        }
        while (!current.isNull()) {
            if (current.getKey().compareTo(key) == 0) {
                return true;
            } else if (current.getKey().compareTo(key) < 0) {
                current = current.getRightChild();
            } else {
                current = current.getLeftChild();
            }
        }
        return false;
    }

    /**
     * Insert the given key in the tree while maintaining the red black tree properties.
     * If the key is already present in the tree, update its value.
     *
     * @param key   to be inserted
     * @param value the associated value with the given key
     */
    public void insert(T key, V value) {
        if (key == null) {
            throw new RuntimeErrorException(new Error("Can't insert a value with null key"));
        }
        if (value == null) {
            throw new RuntimeErrorException(new Error("Can't insert a null value"));
        }
        if (root.isNull()) {
            root = new Node<>(key, value, false, null, null, null);
            root.setLeftChild(new Node<>(null, null, false, root, null, null));
            root.setRightChild(new Node<>(null, null, false, root, null, null));
            return;
        }
        INode<T, V> current = root;
        while (!current.isNull()) {
            if (current.getKey().compareTo(key) == 0) {
                break;
            } else if (current.getKey().compareTo(key) < 0) {
                current = current.getRightChild();
            } else {
                current = current.getLeftChild();
            }
        }
        current.setValue(value);
        if (current.isNull()) {
            current.setColor(true);
            current.setKey(key);
            current.setLeftChild(new Node<>(null, null, false, current, null, null));
            current.setRightChild(new Node<>(null, null, false, current, null, null));
            reColor(current);
        }
    }

    /**
     * Delete the node associated with the given key.
     * Return true in case of success and false otherwise.
     *
     * @param key to be deleted.
     * @return true in case of success and false otherwise.
     */
    public boolean delete(T key) {
        if (key == null) {
            throw new RuntimeErrorException(new Error("Can't delete a null key!"));
        }
        INode<T, V> toBeDeleted = getNodeWithKey(key);
        if (toBeDeleted == null) {
            return false;
        }
        if (!toBeDeleted.getLeftChild().isNull() && !toBeDeleted.getRightChild().isNull()) {
            INode<T, V> successor = getSuccessor(toBeDeleted);
            T swapTmpKey = successor.getKey();
            V swapTmpValue = successor.getValue();
            successor.setKey(toBeDeleted.getKey());
            successor.setValue(toBeDeleted.getValue());
            toBeDeleted.setKey(swapTmpKey);
            toBeDeleted.setValue(swapTmpValue);
            toBeDeleted = successor;
        }
        if (!toBeDeleted.getLeftChild().isNull()) {
            INode<T, V> replacement = toBeDeleted.getLeftChild();
            if (toBeDeleted.getColor() == INode.RED || toBeDeleted.getLeftChild().getColor() == INode.RED) {
                leftDelete(toBeDeleted);
            } else {
                leftDelete(toBeDeleted);
                fixDoubleBlack(replacement);
            }
        } else if (!toBeDeleted.getRightChild().isNull()) {
            INode<T, V> replacement = toBeDeleted.getRightChild();
            if (toBeDeleted.getColor() == INode.RED || toBeDeleted.getRightChild().getColor() == INode.RED) {
                rightDelete(toBeDeleted);
            } else {
                rightDelete(toBeDeleted);
                fixDoubleBlack(replacement);
            }
        } else {
            INode<T, V> replacement = new Node<>(null, null, INode.BLACK, toBeDeleted.getParent(), null, null);
            if (toBeDeleted.getColor() == INode.RED) {
                leafDelete(toBeDeleted, replacement);
            } else {
                leafDelete(toBeDeleted, replacement);
                fixDoubleBlack(replacement);
            }
        }
        return true;
    }

    private INode<T, V> getSuccessor(INode<T, V> node) {
        node = node.getRightChild();
        while (!node.getLeftChild().isNull()) {
            node = node.getLeftChild();
        }
        return node;
    }

    private void reColor(INode<T, V> current) {
        while (current != root) {
            if (current.getParent().getColor() == INode.RED) {
                INode<T, V> uncle;
                if (current.getParent() == current.getParent().getParent().getLeftChild()) {
                    uncle = current.getParent().getParent().getRightChild();
                } else {
                    uncle = current.getParent().getParent().getLeftChild();
                }
                if (uncle.getColor() == INode.RED) {
                    current.getParent().setColor(INode.BLACK);
                    uncle.setColor(INode.BLACK);
                    uncle.getParent().setColor(INode.RED);
                    current = uncle.getParent();
                } else {
                    if (current.getParent().getParent().getLeftChild() == current.getParent()) {
                        if (current.getParent().getLeftChild() == current) {
                            rightRotate(current.getParent().getParent());
                            boolean swapTmp = current.getParent().getColor();
                            current.getParent().setColor(current.getParent().getRightChild().getColor());
                            current.getParent().getRightChild().setColor(swapTmp);
                        } else {
                            leftRotate(current.getParent());
                            current = current.getLeftChild();
                            rightRotate(current.getParent().getParent());
                            boolean swapTmp = current.getParent().getColor();
                            current.getParent().setColor(current.getParent().getRightChild().getColor());
                            current.getParent().getRightChild().setColor(swapTmp);
                        }
                    } else {
                        if (current.getParent().getLeftChild() == current) {
                            rightRotate(current.getParent());
                            current = current.getRightChild();
                            leftRotate(current.getParent().getParent());
                            boolean swapTmp = current.getParent().getColor();
                            current.getParent().setColor(current.getParent().getLeftChild().getColor());
                            current.getParent().getLeftChild().setColor(swapTmp);
                        } else {
                            leftRotate(current.getParent().getParent());
                            boolean swapTmp = current.getParent().getColor();
                            current.getParent().setColor(current.getParent().getLeftChild().getColor());
                            current.getParent().getLeftChild().setColor(swapTmp);
                        }
                    }
                    break;
                }
            } else {
                break;
            }
        }
        if (current == root) {
            current.setColor(INode.BLACK);
        }
    }

    private void rightRotate(INode<T, V> current) {
        INode<T, V> temp = current.getLeftChild().getRightChild();
        if (current.getParent() == null) {
            root = current.getLeftChild();
        } else {
            if (current.getParent().getLeftChild() == current) {
                current.getParent().setLeftChild(current.getLeftChild());
            } else {
                current.getParent().setRightChild(current.getLeftChild());
            }
        }
        current.getLeftChild().setParent(current.getParent());
        temp.getParent().setRightChild(current);
        current.setParent(temp.getParent());
        current.setLeftChild(temp);
        temp.setParent(current);
    }

    private void leftRotate(INode<T, V> current) {
        INode<T, V> temp = current.getRightChild().getLeftChild();
        if (current.getParent() == null) {
            root = current.getRightChild();
        } else {
            if (current.getParent().getLeftChild() == current) {
                current.getParent().setLeftChild(current.getRightChild());
            } else {
                current.getParent().setRightChild(current.getRightChild());
            }
        }
        current.getRightChild().setParent(current.getParent());
        temp.getParent().setLeftChild(current);
        current.setParent(temp.getParent());
        current.setRightChild(temp);
        temp.setParent(current);
    }

    private INode<T, V> getNodeWithKey(T key) {
        INode<T, V> current = root;
        while (!current.isNull()) {
            if (current.getKey().compareTo(key) == 0) {
                return current;
            } else if (current.getKey().compareTo(key) < 0) {
                current = current.getRightChild();
            } else {
                current = current.getLeftChild();
            }
        }
        return null;
    }

    private void fixDoubleBlack(INode<T, V> doubleBlack) {
        INode<T, V> sibling, redChild;
        while (doubleBlack != root) {
            if (doubleBlack == doubleBlack.getParent().getLeftChild()) {
                sibling = doubleBlack.getParent().getRightChild();
            } else {
                sibling = doubleBlack.getParent().getLeftChild();
            }
            if (sibling.isNull()) {
                doubleBlack = doubleBlack.getParent();
                continue;
            }
            if (sibling.getColor() == INode.BLACK) {
                if (sibling.getLeftChild().getColor() == INode.RED || sibling.getRightChild().getColor() == INode.RED) {
                    if (sibling == sibling.getParent().getLeftChild()) {
                        redChild = sibling.getLeftChild().getColor() == INode.RED ? sibling.getLeftChild() : sibling.getRightChild();
                        if (redChild == redChild.getParent().getLeftChild()) {
                            //left  left
                            redChild.setColor(sibling.getColor());
                            sibling.setColor(doubleBlack.getParent().getColor());
                        } else {
                            //left right
                            redChild.setColor(doubleBlack.getParent().getColor());
                            leftRotate(sibling);
                        }
                        rightRotate(doubleBlack.getParent());
                    } else {
                        redChild = sibling.getRightChild().getColor() == INode.RED ? sibling.getRightChild() : sibling.getLeftChild();
                        if (redChild == redChild.getParent().getLeftChild()) {
                            //right left
                            redChild.setColor(doubleBlack.getParent().getColor());
                            rightRotate(sibling);

                        } else {
                            //right right
                            redChild.setColor(sibling.getColor());
                            sibling.setColor(doubleBlack.getParent().getColor());
                        }
                        leftRotate(doubleBlack.getParent());
                    }
                    doubleBlack.getParent().setColor(INode.BLACK);
                    break;
                } else {
                    //b
                    sibling.setColor(INode.RED);
                    if (doubleBlack.getParent().getColor() == INode.BLACK) {
                        doubleBlack = doubleBlack.getParent();
                    } else {
                        doubleBlack.getParent().setColor(INode.BLACK);
                        break;
                    }
                }
            } else {
                //c
                sibling.setColor(INode.BLACK);
                sibling.getParent().setColor(INode.RED);
                if (sibling == sibling.getParent().getRightChild()) {
                    leftRotate(doubleBlack.getParent());
                } else {
                    rightRotate(doubleBlack.getParent());
                }
            }
        }
    }

    private void leftDelete(INode<T, V> toBeDeleted) {
        if (toBeDeleted != root) {
            if (toBeDeleted == toBeDeleted.getParent().getLeftChild()) {
                toBeDeleted.getParent().setLeftChild(toBeDeleted.getLeftChild());
            } else {
                toBeDeleted.getParent().setRightChild(toBeDeleted.getLeftChild());
            }
            toBeDeleted.getLeftChild().setParent(toBeDeleted.getParent());
            toBeDeleted.getLeftChild().setColor(INode.BLACK);
            toBeDeleted.setParent(null);

        } else {
            root = toBeDeleted.getLeftChild();
            toBeDeleted.getLeftChild().setParent(null);
            toBeDeleted.getLeftChild().setColor(INode.BLACK);
        }
        toBeDeleted.setLeftChild(null);
        toBeDeleted.setRightChild(null);
    }

    private void rightDelete(INode<T, V> toBeDeleted) {
        if (toBeDeleted != root) {
            if (toBeDeleted == toBeDeleted.getParent().getLeftChild()) {
                toBeDeleted.getParent().setLeftChild(toBeDeleted.getRightChild());
            } else {
                toBeDeleted.getParent().setRightChild(toBeDeleted.getRightChild());
            }
            toBeDeleted.getRightChild().setParent(toBeDeleted.getParent());

        } else {
            root = toBeDeleted.getRightChild();
            toBeDeleted.getRightChild().setParent(null);
        }
        toBeDeleted.getRightChild().setColor(INode.BLACK);
        toBeDeleted.setParent(null);
        toBeDeleted.setLeftChild(null);
        toBeDeleted.setRightChild(null);
    }

    private void leafDelete(INode<T, V> toBeDeleted, INode<T, V> replacement) {
        if (toBeDeleted != root) {
            if (toBeDeleted == toBeDeleted.getParent().getLeftChild()) {
                toBeDeleted.getParent().setLeftChild(replacement);
            } else {
                toBeDeleted.getParent().setRightChild(replacement);
            }
        } else {
            root = replacement;
        }
        toBeDeleted.setParent(null);
        toBeDeleted.setLeftChild(null);
        toBeDeleted.setRightChild(null);
    }
}
