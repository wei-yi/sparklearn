package com.weiyi;

import scala.reflect.api.Trees;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * Created by yuanyi on 15/11/25.
 */
public class TreeBuilder {

    public static void stack() {
        Stack<String> stack = new Stack();
        stack.push("one");
        stack.push("two");
        stack.push("three");
        System.out.println(stack.peek() + " : " + stack.size());
        System.out.println(stack.pop() + " : "+ stack.size());
        System.out.println(stack.pop() + " : " + stack.size());
    }

    public static  List<Tree> treeDeepFirstRoot(Tree t) {
          List<Tree> firstscan = new LinkedList<Tree>();
          Stack<Tree> stack = new Stack<Tree>();
        if(t == null) {
            return firstscan;
        }else {
            stack.push(t);
          while(stack.size() !=0 ) {
              Tree tree = stack.pop();
              if(tree.getrChild() != null)
                  stack.push(tree.getrChild());
              if(tree.getlChile() != null)
                  stack.push(tree.getlChile());
              firstscan.add(tree);
          }
        }
        return firstscan;
    }

    public static List<Tree> recusive(Tree t) {
        List<Tree> nodes = new LinkedList<Tree>();
        if (t == null)
            return null;
        else {
            nodes.add(t);
            if (t.getlChile() != null) {
                nodes.addAll(recusive(t.getlChile()));
            }
            if(t.getrChild() != null) {
                nodes.addAll(recusive(t.getrChild()));
            }
//           nodes.add(t);
//            Tree c = null;
//            if((c = t.getlChile()) != null) {
//                return recusive(c);
//            }
        }
            return nodes;
    }

    public static  void main(String[] args) {
        Tree t1 = new Tree("l1", null, null);
        Tree t2 = new Tree("r1", null, null);
        Tree t3 = new Tree("p1", t1, t2);
        Tree t4 = new Tree("p2", t1, t2);
        Tree t5 = new Tree("p3", t3, t4);

        List<Tree> trees = treeDeepFirstRoot(t5);
        for(Tree t: trees) {
            System.out.println(t.toString());
        }
        System.out.println("---------------");
        List<Tree> trees1 = recusive(t5);
        for(Tree t: trees1) {
            System.out.println(t.toString());
        }
    }

}


class Tree {
    private String data;
    public Tree rChild;
    private Tree lChile;

    public Tree(String data, Tree lChile, Tree rChild) {
        this.data = data;
        this.lChile = lChile;
        this.rChild = rChild;
    }

    public Tree getrChild() {
        return rChild;
    }

    public Tree getlChile() {
        return lChile;
    }

    public String getData() {
      return data;
    }

    @Override
    public String toString() {
        return data;
    }
}