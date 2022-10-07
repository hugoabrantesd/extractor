package br.edu.fafic.pp.managers;

import net.lingala.zip4j.ZipFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FilesManager extends Thread {

    private final String path;
    private final String destPath;

    public FilesManager(String path, String destPath) {
        this.path = path;
        this.destPath = destPath;
    }

    public static void downloadAllFiles(int initRange, int endRange)
            throws MalformedURLException, InterruptedException {
        URL url;

        for (int i = initRange; i <= endRange; i++) {
            url = new URL(String.format("http://dados.cvm.gov.br/dados/CIA_ABERTA/DOC/DFP/DADOS/dfp_cia_aberta_%s%s", i, ".zip"));

            String directory = "src/downloads/dfp_cia_aberta_" + i;
            System.out.println("=== Baixando " + "dfp_cia_aberta_" + i + ".zip");
            downloadFiles(url, directory, "dfp_cia_aberta_" + i + ".zip");
        }
    }

    private static void downloadFiles(URL url, String directory, String fileName) {

        File f = new File(directory);

        if (f.mkdir()) {
            try (InputStream is = url.openStream()) {
                ReadableByteChannel rbc = Channels.newChannel(is);
                FileOutputStream fos = new FileOutputStream(directory + "/" + fileName);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void unzip(String path, String destPath) throws IOException {
        ZipFile zipFile = new ZipFile(path);
        zipFile.extractAll(destPath);
    }

    @Override
    public void run() {
        try {
            unzip(this.path, this.destPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.run();
    }
}
