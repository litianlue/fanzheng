package fanzhen.first.com.fanzhen.external;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zjj on 2018/1/29.
 */
public class SequenceUtil {
    public static List<Double> BubbleSort(List<Double> list){

        double[] arr = new double[list.size()];
        List<Double> list1 = new ArrayList<>();
        double temp;//临时变量
        boolean flag;//是否交换的标志
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        for(int i=0; i<arr.length-1; i++){   //表示趟数，一共arr.length-1次。

            flag = false;
            for(int j=arr.length-1; j>i; j--){

                if(arr[j] < arr[j-1]){
                    temp = arr[j];
                    arr[j] = arr[j-1];
                    arr[j-1] = temp;
                    flag = true;
                }
            }
            if(!flag) break;
        }
        for (int i = 0; i < arr.length; i++) {
            list1.add(arr[i]);
        }
        return list1;
    }

}
