///usr/bin/env jbang "$0" "$@" ; exit $?
//REPOS mavencentral,jitpack
//DEPS info.picocli:picocli:4.5.0
//DEPS com.github.opt-nc:phonenumber-validator:1.2.2


import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import nc.opt.telecom.sdk.phonenumber.validator.util.PhoneNumberValidator;

import java.util.concurrent.Callable;

@Command(name = "Validator", mixinStandardHelpOptions = true, version = "Validator 0.1",
        description = "Validator made with jbang")
class Validator implements Callable<Integer> {

    @CommandLine.Option(
            names = {"-c", "--check"},
            description = "Vérification à exécuter sur le numéro de téléphone (is-mobile, is-fixe, is-valid, info). Le numéro doit être au format E.164.")
    private String check;

    @CommandLine.Option(
            names = {"-f", "--format"},
            description = "Format le numéro de téléphone en respectant la norme E.164")
    private Boolean formatRequired;

    @Parameters(index = "0", description = "Le numéro de téléphone à vérifier")
    private String phoneNumber;

    public static void main(String... args) {
        int exitCode = new CommandLine(new Validator()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception { // your business logic goes here...
        if(check != null) {
            switch (check) {
                case "is-valid":
                    System.out.println(PhoneNumberValidator.isPossible(phoneNumber) ? "oui" : "non");
                    break;
                case "is-fixe":
                    System.out.println(PhoneNumberValidator.isFixe(phoneNumber) ? "oui" : "non");
                    break;
                case "is-mobile":
                    System.out.println(PhoneNumberValidator.isMobile(phoneNumber) ? "oui" : "non");
                    break;
                case "info":
                    System.out.println("Numéro valide : " + (PhoneNumberValidator.isPossible(phoneNumber) ? "oui" : "non"));
                    System.out.println("Type de numéro : " + PhoneNumberValidator.getPhoneType(phoneNumber).name());
                    break;
                default:
                    System.out.println("Invalid option : " + check);
                    System.out.println("Choose in : is-valid, is-fixe, is-mobile, info");
                    break;
            }
        }
        else if (formatRequired){
            System.out.println(PhoneNumberValidator.format(phoneNumber));
        }

        return 0;
    }
}
