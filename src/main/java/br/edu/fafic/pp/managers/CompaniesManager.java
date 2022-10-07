package br.edu.fafic.pp.managers;

import br.edu.fafic.pp.app.Cube;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.stream.Collectors;

public class CompaniesManager extends Thread {

    private final String path;
    private final Cube cube;

    public CompaniesManager(String path, Cube cube) {
        this.path = path;
        this.cube = cube;
    }

    /**
     * Retorna a lista de empresas sem repetição
     */
    private List<String> getCompanies() {
        CSVReader csvReader;

        try {
            csvReader = new CSVReader(new FileReader(this.path));
            String[] nextLine;
            List<String> companies = new ArrayList<>();

            while ((nextLine = csvReader.readNext()) != null) {
                if (!nextLine[0].contains("DENOM_CIA")) {
                    companies.add(nextLine[0].split(";")[3]);
                }
            }

            return companies.stream().distinct().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("===== ERRO AO LER ARQUIVO CSV =====");
        }
        return new ArrayList<>();
    }

    @Override
    public void run() {
        List<String> companies = this.getCompanies();

        synchronized (CompaniesManager.class) {
            try {
                cube.put(companies);
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
        super.run();
    }
}





