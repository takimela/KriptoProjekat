package com.example.kriptoprojekat;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class FileManager {

    public static boolean isUserExists(String username) {
        String folderPath = "Data/passwords";
        String fileName = username + ".txt";

        File file = new File(folderPath, fileName);

        if (file.exists() && !file.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isCertificateExists(String certificate) {
        String folderPath = "Data/certs";
        String fileName = certificate;

        File file = new File(folderPath, fileName);

        if (file.exists() && !file.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    public static String readPassword(String username) {
        String fileName = "Data/passwords/" + username + ".txt";
        String password = "";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line = br.readLine();
            if (line != null) {
                password = line;
            } else {
                password = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return password;
    }

    public static void writeSimulation(String history, String username, String key)
    {

        String fileName = "Data/simulations/0" + username + "0.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(history);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        OpenSSL.encHisroty(username,key);

        File file = new File(fileName);
        if(file.exists())
            file.delete();
    }

    public static String readSimulations(String username, String key) {
        String fileName = "Data/simulations/" + username + ".txt";

        Path filePath = Paths.get(fileName);
        if (!Files.exists(filePath)) {
            return "";
        }

        return OpenSSL.getHistory(username, key);
    }

}
