package com.weiyi;

/**
 * Created by yuanyi on 15/11/26.
 */
public class Recusive {
    //1 1 2 3 5 8 13
    private int r2(int pre, int next, int count) {
        if (count > 3) {
            return r2(next, pre + next, count - 1);
        } else if (count == 3) {
            return pre + next;
        } else {
            return 1;
        }
    }

    public int find(int count) {
        return r2(1, 1, count);
    }

    public static void main(String[] args) {
        Recusive r = new Recusive();
        for (int i = 1; i < 16; i++)
            System.out.println(r.find(i));
    }
}
