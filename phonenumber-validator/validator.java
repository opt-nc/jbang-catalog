///usr/bin/env jbang "$0" "$@" ; exit $?
//REPOS mavencentral,jitpack
//DEPS info.picocli:picocli:4.5.0
//DEPS com.github.opt-nc:phonenumber-validator:1.2.2


import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import nc.opt.telecom.sdk.phonenumber.validator.util.PhoneNumberValidator;

import java.util.concurrent.Callable;

@Command(name = "validator", mixinStandardHelpOptions = true, version = "validator 0.1",
        description = "validator made with jbang")
class validator implements Callable<Integer> {

    @CommandLine.Option(
            names = {"-c", "--check"},
            description = "Vérification à exécuter sur le numéro en paramètre", required=true)
    private String check;
    @Parameters(index = "0", description = "Le numéro de téléphone à vérifier")
    private String phoneNumber;

    public static void main(String... args) {
        int exitCode = new CommandLine(new validator()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception { // your business logic goes here...
        switch (check){
            case "is-valid":
                System.out.println(PhoneNumberValidator.isPossible(phoneNumber));
                break;
            case "is-fixe":
                System.out.println(PhoneNumberValidator.isFixe(phoneNumber));
                break;
            case "is-mobile":
                System.out.println(PhoneNumberValidator.isMobile(phoneNumber));
                break;
            default:
                System.out.println("Invalid option : " + check);
                System.out.println("Choose in : is-valid, is-fixe, is-mobile");
            break;
        }

        return 0;
    }
}
