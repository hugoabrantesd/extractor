package br.edu.fafic.pp.app;

import br.edu.fafic.pp.managers.CompaniesManager;
import br.edu.fafic.pp.managers.FilesManager;

import java.io.File;
import java.net.MalformedURLException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class App {

    private static final int startYear = 2010;
    private static final int endYear = 2020;
    public static CyclicBarrier cyclicBarrier = new CyclicBarrier(12);
    private static final Cube CUBE = new Cube();

    public static void executarAtividade01() throws MalformedURLException, InterruptedException {
        File f = new File("src/downloads");

        boolean isCreated = f.mkdir();
        if (isCreated) {
            FilesManager.downloadAllFiles(startYear, endYear);

            System.out.println();
            for (int i = startYear; i <= endYear; i++) {
                System.out.println("=== Extraindo " + "dfp_cia_aberta_" + i + ".zip");
                String directory = "src/downloads/dfp_cia_aberta_" + i;
                new FilesManager(directory + "/dfp_cia_aberta_" + i + ".zip", directory).start();
            }
        }
    }

    public static void getCompanies() {
        long startTime = System.currentTimeMillis();
        System.out.println("Carregando...");
        for (int i = startYear; i <= endYear; i++) {
            String path = "src/downloads/dfp_cia_aberta_" + i + "/dfp_cia_aberta_DRE_con_" + i + ".csv";

            CompaniesManager companiesManager = new CompaniesManager(path, CUBE);
            new Thread(companiesManager).start();
        }

        try {
            cyclicBarrier.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException | BrokenBarrierException | TimeoutException e) {
            System.out.print("");
        }

        long timePassed = System.currentTimeMillis() - startTime;
        long secondsPassed = timePassed / 1000;

        System.out.println("Quantidade de empresas reincidentes: " + CUBE.getEmpresasReincidentes().size());
        System.out.println("Tempo decorrido (segundos): " + secondsPassed);
    }

}
