///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS info.picocli:picocli:4.5.0


import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

@Command(name = "hello-optnc", mixinStandardHelpOptions = true, version = "hello-optnc 0.1",
        description = "HelloOptNc made with jbang")
class HelloOptNc implements Callable<Integer> {

    @Parameters(index = "0", description = "The greeting to print", defaultValue = "from OPT-NC!")
    private String greeting;

    public static void main(String... args) {
        int exitCode = new CommandLine(new HelloOptNc()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception { // your business logic goes here...
        System.out.println("Hello World " + greeting);
        return 0;
    }
}
