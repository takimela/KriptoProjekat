package com.example.kriptoprojekat;

import java.io.*;

public class OpenSSL {

    private static void runCommand(String command) {
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);

            builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            Process process = builder.start();

            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String runCommandWithOutput(String command) {
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);

            builder.redirectOutput(ProcessBuilder.Redirect.PIPE);

            Process process = builder.start();

            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();
            process.destroy();

            return output.toString();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void genRsa(String username, String password) {
        String command = "openssl genrsa -out Data/private/" + username + ".key -aes256 -passout pass:" + password;
        runCommand(command);
    }

    public static void newRequest(String username, String password, String info) {
        String command = "cd Data && openssl req -new -key private/" + username+ ".key -config openssl.cnf -out requests/" + username+ ".csr -subj " + info + " -passin pass:" + password;
        runCommand(command);
    }

    public static void signRequest(String username) {
        String command = "cd Data && openssl ca -batch -in requests/" + username + ".csr -out certs/" + username + ".crt -config openssl.cnf -passin pass:sigurnost";
        runCommand(command);
    }

    public static void createUser(String username, String password) {
        String command ="cd Data/passwords && openssl passwd -5 -salt " + username + " " + password + "> " + username + ".txt";
        runCommand(command);
    }

    public static boolean isValidCertTime(String certificate){
        String command ="cd Data/certs && openssl x509 -checkend 86400 -noout -in " + certificate;
        String output = runCommandWithOutput(command);
        output = output.trim();

        if(output.equals("Certificate will not expire"))
            return true;
        else
            return false;
    }

    public static boolean isValidCertificate(String certificate){
        String command ="cd Data && openssl verify -crl_check -CAfile rootca.pem -CRLfile crl/crllist.crl certs/" + certificate;
        String output = runCommandWithOutput(command);
        output = output.trim();

        if(output.endsWith("OK"))
            return true;
        else
            return false;
    }

    public static boolean isValidPasswort(String username, String password){
        String passwordHash = FileManager.readPassword(username);
        String passwordHash2 = runCommandWithOutput("openssl passwd -5 -salt " + username + " " + password);

        passwordHash2 = passwordHash2.trim();
        if(passwordHash.equals(passwordHash2))
            return true;
        else
            return false;
    }

    public static String getHistory(String username, String password){
        String command ="cd Data/simulations && openssl enc -d -aes256 -in " + username + ".txt -pass pass:" + password + " -pbkdf2";
        return runCommandWithOutput(command);
    }

    public static void encHisroty(String username, String password){
        String command ="cd Data/simulations && openssl enc -aes256 -in 0" + username + "0.txt -out " + username + ".txt -pass pass:" + password + " -pbkdf2";
        runCommand(command);

        command ="cd Data && openssl dgst -sign private/" + username + ".key -sha256 -passin pass:" + password + " -out signatures/" + username + ".sign simulations/" + username + ".txt";
        runCommand(command);
    }

    public static boolean verifySignature(String username, String password){

        File file = new File("Data/signatures/" + username + ".sign");
        if (!(file.exists() && !file.isDirectory())) {
            return true;
        }

        String command ="cd Data && openssl dgst -prverify private/" + username + ".key -passin pass:" + password + " -signature signatures/" + username + ".sign simulations/" + username + ".txt";
        String output = runCommandWithOutput(command).trim();

        if(output.endsWith("OK"))
            return true;
        else
            return false;

    }
}
