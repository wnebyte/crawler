package com.github.wnebyte.crawler.struct;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Node<T> {

    private Node<T> parent;

    private final List<Node<T>> children = new ArrayList<>();

    private final T value;

    public Node(final T value) {
        this.value = value;
    }

    @SafeVarargs
    public Node(final T value, final Node<T>... children) {
        this(value);
        addChildren(children);
    }

    @SafeVarargs
    public final void addChildren(final Node<T>... children) {
        if ((children != null) && (children.length != 0)) {
            for (Node<T> node : children) {
                addChild(node);
            }
        }
    }

    public void addChild(final Node<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void setParent(final Node<T> parent) {
        this.parent = parent;
    }

    public Node<T> getParent() {
        return parent;
    }

    public boolean isRoot() {
        return (getParent() == null);
    }

    public boolean isLeaf() {
        return (getChildren().isEmpty());
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public T getValue() {
        return value;
    }

    public int getDepth() {
        if (this.isRoot()) {
            return 0;
        }
        int depth = 0;
        depth = Math.max(depth, this.getParent().getDepth());
        return depth + 1;
    }

    public List<Node<T>> leafNodes()
    {
        List<Node<T>> leafNodes = new ArrayList<Node<T>>();
        if (this.isLeaf()) {
            leafNodes.add(this);
        } else {
            for (Node<T> child : this.getChildren()) {
                leafNodes.addAll(child.leafNodes());
            }
        }
        return leafNodes;
    }

    public void consume(final Consumer<Node<T>> consumer) {
        consume(this, consumer);
    }

    public static <T> void consume(final Node<T> node, final Consumer<Node<T>> consumer) {
        consumer.accept(node);
        if (node.isLeaf()) {
            return;
        }
        for (Node<T> child : node.getChildren()) {
            consume(child, consumer);
        }
    }
}
