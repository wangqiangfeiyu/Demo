package thread;

import java.util.concurrent.RecursiveAction;

/**
 * @author wangqiang
 * @version C10 2018年06月15日
 * @since SDP V300R003C10
 */

public class SortTask extends RecursiveAction
{
    private static final long serialVersionUID = -1738015707066879398L;
    public final String[] words;
    final int lo;
    final int hi;

    public SortTask(String[] array) {
        this.words = array;
        this.lo = 0;
        this.hi = array.length - 1;
    }

    public SortTask(String[] array, int lo, int hi) {
        this.words = array;
        this.lo = lo;
        this.hi = hi;
    }

    @Override
    protected void compute() {
        if (hi - lo > 0) {
            int pivot = partition(words, lo, hi);
            SortTask left = new SortTask(words, lo, pivot - 1);
            SortTask right = new SortTask(words, pivot + 1, hi);
            invokeAll(left, right);
        }
    }

    /**
     * 对数组进行分区操作，并返回中间元素位置
     */
    private int partition(String[] array, int lo, int hi) {
        String x = array[hi];
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (array[j].compareTo(x) <= 0) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, hi);
        return i + 1;
    }

    private void swap(String[] array, int i, int j) {
        if (i != j) {
            String temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }
}
