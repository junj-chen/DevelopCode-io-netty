import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @title: Test
 * @Description //TODO
 * @Author Chen
 * @Date: 2021/9/5 23:15
 * @Version 1.0
 */
public class Test {

    private static ThreadLocal<AtomicInteger> sqequer = new ThreadLocal<AtomicInteger>(){
        @Override
        protected AtomicInteger initialValue() {
            return new AtomicInteger(0);
        }

    };

    static class BarTask implements Runnable{
        @Override
        public void run() {
            AtomicInteger atomicInteger = sqequer.get();
            int andIncrement = atomicInteger.getAndIncrement();

            System.out.println(andIncrement);
        }
    }

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(new BarTask());
        executorService.execute(new BarTask());
        executorService.execute(new BarTask());
        executorService.execute(new BarTask());

        executorService.shutdown();


    }
}
