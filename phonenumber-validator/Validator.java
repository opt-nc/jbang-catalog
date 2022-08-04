///usr/bin/env jbang "$0" "$@" ; exit $?
//REPOS mavencentral,jitpack
//DEPS info.picocli:picocli:4.5.0
//DEPS com.github.opt-nc:phonenumber-validator:1.2.2
//DEPS com.fasterxml.jackson.core:jackson-databind:2.13.1


import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import nc.opt.telecom.sdk.phonenumber.validator.util.PhoneNumberValidator;

import java.util.concurrent.Callable;

@Command(name = "Validator", version = "Validator 0.1",
        description = "Validator made with jbang")
class Validator implements Callable<Integer> {


     @ArgGroup(exclusive = true)
    private FormatOption formatOption;

    @ArgGroup(exclusive = true)
    private CheckOption checkOption;

    @ArgGroup(exclusive = true)
    private InfoOption infoOption;


    @Parameters(index = "0", description = "Le numéro de téléphone à vérifier")
    private String phoneNumber;


    private static class CheckOption {
        @CommandLine.Option(
                names = {"-c", "--check"},
                description = "Vérification à exécuter sur le numéro de téléphone (is-mobile, is-fixe, is-valid). Le numéro doit être au format E.164.")
        private String check;
    }

    private static class FormatOption {
        @CommandLine.Option(
                names = {"-f", "--format"},
                description = "Format le numéro de téléphone en respectant la norme E.164")
        private boolean formatRequired = false;
    }

    private static class InfoOption {
        @CommandLine.Option(
                names = {"-i", "--info"},
                description = "Retourne les information du numéro de téléphone")
        private boolean infoRequired = false;
    }

    @CommandLine.Option(
            names = {"-o", "--output"},
            description = "Retourne la reponse au format demandané")
    private String output;



    public static void main(String... args) {
        int exitCode = new CommandLine(new Validator()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() throws Exception { // your business logic goes here...

        boolean returnJsonRequired = false;
        if(output != null && output.equals("json"))
            returnJsonRequired = true;
        else if(output != null){
            System.err.println("Format de sortie non supporté : " + output);
            return -1;
        }


        NumberInfo numberInfo = numberInfo = new NumberInfo();
        String result = "";
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);

        if(checkOption != null) {
            switch (checkOption.check) {
                case "is-valid":
                    numberInfo.isValid = PhoneNumberValidator.isPossible(phoneNumber);
                    result = returnJsonRequired ? mapper.writeValueAsString(numberInfo) : String.valueOf(numberInfo.isValid);
                    break;
                case "is-fixe":
                    numberInfo.isFixe = PhoneNumberValidator.isFixe(phoneNumber);
                    result = returnJsonRequired ? mapper.writeValueAsString(numberInfo) : String.valueOf(numberInfo.isFixe);
                    break;
                case "is-mobile":
                    numberInfo.isMobile = PhoneNumberValidator.isMobile(phoneNumber);
                    result = returnJsonRequired ? mapper.writeValueAsString(numberInfo) : String.valueOf(numberInfo.isMobile);
                    break;
                default:
                    System.out.println("Option invalide: " + checkOption.check);
                    System.out.println("Choisissez parmi : is-valid, is-fixe, is-mobile");
                    return -1;
            }
        }
        else if (formatOption != null && formatOption.formatRequired){
            numberInfo.internationalNumber = PhoneNumberValidator.format(phoneNumber);
            result = returnJsonRequired ? mapper.writeValueAsString(numberInfo) : numberInfo.internationalNumber;
        }
        else if (infoOption != null && infoOption.infoRequired){
            numberInfo.type = PhoneNumberValidator.getPhoneType(phoneNumber).name();
            numberInfo.isValid = PhoneNumberValidator.isPossible(phoneNumber);
            result = returnJsonRequired ? mapper.writeValueAsString(numberInfo) : ("Numéro valide : " + numberInfo.isValid + "\nType de numéro : " + numberInfo.type);
        }

        System.out.println(result);

        return 0;
    }


    private class NumberInfo {
        Boolean isMobile;
        Boolean isFixe;
        Boolean isValid;
        String internationalNumber;
        String type;

        public Boolean isMobile() {
            return isMobile;
        }

        public void setMobile(Boolean mobile) {
            isMobile = mobile;
        }

        public Boolean isFixe() {
            return isFixe;
        }

        public void setFixe(Boolean fixe) {
            isFixe = fixe;
        }

        public Boolean isValid() {
            return isValid;
        }

        public void setValid(Boolean valid) {
            isValid = valid;
        }

        public String getInternationalNumber() {
            return internationalNumber;
        }

        public void setInternationalNumber(String internationalNumber) {
            this.internationalNumber = internationalNumber;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
