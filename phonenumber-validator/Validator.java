///usr/bin/env jbang "$0" "$@" ; exit $?
//REPOS mavencentral,jitpack
//DEPS info.picocli:picocli:4.5.0
//DEPS com.github.opt-nc:phonenumber-validator:1.4.0
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
                description = "Vérification à exécuter sur le numéro de téléphone (is-mobile, is-fixe, is-valid, is-special). Le numéro doit être au format E.164.")
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
            description = "Retourne la réponse au format demandé")
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


        NumberInfo numberInfo = new NumberInfo();
        String result = "";
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(Include.NON_NULL);


        if(checkOption != null) {
            try{
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
                    case "is-special":
                        numberInfo.isMobile = PhoneNumberValidator.isSpecial(phoneNumber);
                        result = returnJsonRequired ? mapper.writeValueAsString(numberInfo) : String.valueOf(numberInfo.isSpecial);
                        break;
                    default:
                        System.out.println("Option invalide: " + checkOption.check);
                        System.out.println("Choisissez parmi : is-valid, is-fixe, is-mobile, is-special");
                        return -1;
                }

            }catch (IllegalArgumentException e){
                System.err.println(e.getMessage());
            }
        }
        else if (formatOption != null && formatOption.formatRequired){
            try {
                numberInfo.internationalNumber = PhoneNumberValidator.format(phoneNumber);
                result = returnJsonRequired ? mapper.writeValueAsString(numberInfo) : numberInfo.internationalNumber;
            }catch (IllegalArgumentException e){
                System.err.println(e.getMessage());
            }
        }
        else if (infoOption != null && infoOption.infoRequired){
            try{
                numberInfo.type = PhoneNumberValidator.getPhoneType(phoneNumber);
                numberInfo.isValid = PhoneNumberValidator.isPossible(phoneNumber);
                if(PhoneNumberValidator.isSpecial(phoneNumber))
                    numberInfo.attribue = PhoneNumberValidator.getSpecialNumberLabel(phoneNumber);

                result = returnJsonRequired ? mapper.writeValueAsString(numberInfo) : ("Numéro valide : " + numberInfo.isValid
                        + "\nType de numéro : " + numberInfo.type + (PhoneNumberValidator.isSpecial(phoneNumber) ? "\nAttribué: " + PhoneNumberValidator.getSpecialNumberLabel(phoneNumber) : ""));
            }catch (IllegalArgumentException e){
                System.err.println(e.getMessage());
            }
        }

        System.out.println(result);

        return 0;
    }


    private class NumberInfo {
        Boolean isMobile;
        Boolean isFixe;
        Boolean isValid;
        Boolean isSpecial;
        String internationalNumber;
        String type;
        String attribue;

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

        public Boolean getIsSpecial() {
            return isSpecial;
        }

        public void setSpecial(Boolean special) {
            isSpecial = special;
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

        public String getAttribue() {
            return attribue;
        }

        public void setAttribue(String attribue) {
            this.attribue = attribue;
        }
    }
}
