package thread;

import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

/**
 * @author wangqiang
 * @version C10 2018年06月15日
 * @since SDP V300R003C10
 */

public class SortTask extends RecursiveAction
{
    /**
     * 需要排序的数组
     */
    private final String[] array;

    /**
     * 起始位置
     */
    private final int start;

    /**
     * 其实位置
     */
    private final int end;

    /**
     * 是否需要多线程排序的游标
     */
    private static final int THRESHOLD = 100000;

    @Override
    protected void compute()
    {
        if (end - start < THRESHOLD)
        {
            Arrays.sort(array, start, end + 1);
        }
        else
        {
            int pivot = partition(stringToLong(array), start, end);
            SortTask left = null;
            SortTask right = null;
            if (start < pivot - 1)
            {
                left = new SortTask(array, start, pivot - 1);
            }
            if (pivot + 1 < end)
            {
                right = new SortTask(array, pivot + 1, end);
            }
            if (left != null)
            {
                left.fork();
            }
            if (right != null)
            {
                right.fork();
            }
        }
    }
    public static long[] stringToLong(String stringArray[]) {
        if (stringArray == null || stringArray.length < 1) {
            return null;
        }
        long longArray[] = new long[stringArray.length];
        for (int i = 0; i < longArray.length; i++) {
            try {
                longArray[i] = Long.valueOf(stringArray[i]);
            } catch (NumberFormatException e) {
                longArray[i] = 0;
                continue;
            }
        }
        return longArray;
    }
    /**
     * 获取当前值所在区域
     *
     * @param array 数组
     * @param start 起始位置
     * @param end 结束位置
     * @return 下标
     */
    private int partition(long[] array, int start, int end)
    {
        int i = start;
        int j = end;
        if (j - i > 2)
        {
            if ((array[i] < array[j - i] && array[j - i] < array[j])
                || (array[j] < array[j - i] && array[j - i] < array[i]))
            {
                long t = array[i];
                array[i] = array[j - i];
                array[j - i] = t;
            }
            else
            {
                if ((array[i] < array[j] && array[j] < array[j - i])
                    || (array[j - i] < array[j] && array[j] < array[i]))
                {
                    long t = array[i];
                    array[i] = array[j];
                    array[j] = t;
                }
            }
        }
        long pivot = array[i];
        while (i < j)
        {
            while (i < j && array[j] > pivot)
            {
                j--;
            }
            if (i < j)
            {
                array[i++] = array[j];
            }
            while (i < j && array[i] < pivot)
            {
                i++;
            }
            if (i < j)
            {
                array[j--] = array[i];
            }
        }
        array[i] = pivot;
        return i;
    }

    public SortTask(String[] array, int start, int end)
    {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    public SortTask(String[] array)
    {
        this.array = array;
        this.start = 0;
        this.end = array.length - 1;
    }
}
