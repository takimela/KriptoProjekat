package com.example.kriptoprojekat;

import java.io.IOException;

public class OpenSSL {

    private static void runCommand(String command) {
        try {
            // Kreiranje ProcessBuilder objekta za pokretanje Git Bash terminala
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", command);

            // Postavljanje redirectOutput da uhvati izlaz komande
            builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            // Pokretanje procesa
            Process process = builder.start();

            // Čekanje da se proces završi
            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
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
}
