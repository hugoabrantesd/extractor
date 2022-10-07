package br.edu.fafic.pp.app;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;

public class Cube {

    private List<String> empresasReincidentes = new ArrayList<>();

    public List<String> getEmpresasReincidentes() {
        return empresasReincidentes;
    }

    public void put(List<String> empresas) throws InterruptedException, BrokenBarrierException {

        if (this.empresasReincidentes.isEmpty()) {
            this.empresasReincidentes = empresas;
        } else {
            // Filtra a lista de empresas mantendo apenas as reincidentes.
            this.empresasReincidentes.retainAll(empresas);
        }

    }
}
