package  com.weiyi;

public class TreeSort {

    public static void headAdjust(int[] num, int adjustPosition, int limited) {
        int tmp = num[adjustPosition];
        for(int j = adjustPosition * 2; j< limited; j*=2) {
            if (j < limited) {
                if(j+1 < limited && num[j] < num[++j]) {
                  j+=1;
                }
                if (num[j] > tmp) {
                     num[adjustPosition] = num[j];
                    adjustPosition = j;
                }
            }
        }
        num[adjustPosition] = tmp ;
    }

    public static void treeSort(int[] array) {
     for(int i = (array.length-1)/2; i >0; i--) {
         headAdjust(array, i, array.length-1);
     }
        for(int i :array){
            System.out.print(i);
            System.out.print(",");
        }


        for(int i =1; i< array.length-1; i++ ) {
            int tmp = array[array.length-i];
            array[array.length-i] = array[1];
            headAdjust(array, 1, array.length-i-1);
        }
        System.out.print("-----------");
        for(int i :array) {
            System.out.print(i);
            System.out.print(",");
        }

    }

    public static void main(String [] args) {
        int[] aa = {0,1,3,2,10,2,3,1,7,6};
        treeSort(aa);

        int j =0 ;
        System.out.println("j :" +j );

    }
}