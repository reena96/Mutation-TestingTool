package tianma.learn.ds.Launcher;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class UsingSingleThreadExecutor {

    public static void main(String[] args) throws InterruptedException {
        String INPUT_FILE_PATH = "./src/main/java/tianma/learn/ds/Launcher/ExecutionTrace_";

        ExecutorService execService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < Config.StringMatchSample.sequence.length; i++) {

            execService.execute(new TracePrint(INPUT_FILE_PATH,Config.StringMatchSample.sequence[i], Config.StringMatchSample.pattern[i], i+""));
        }
        execService.shutdown();
    }
}