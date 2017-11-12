package tianma.learn.ds.Launcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UsingSingleThreadExecutor {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService execService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < Config.StringMatchSample.source.length; i++) {

            execService.execute(new TracePrint(Config.StringMatchSample.source[i], Config.StringMatchSample.pattern[i], i));
        }
        execService.shutdown();
    }
}
