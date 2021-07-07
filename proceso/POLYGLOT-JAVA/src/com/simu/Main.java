package com.simu;

import com.simu.classes.Mesh;
import com.simu.enums.Sizes;

import java.io.IOException;
import java.util.ArrayList;

import static com.simu.MathTools.zeroes;
import static com.simu.MathTools.zeroesVector;
import static com.simu.Sel.*;
import static com.simu.Tools.leerMallayCondiciones;
import static com.simu.Tools.writeResults;
//import static com.simu.Tools.writeResults;

public class Main {

    public static void main(String[] args) {
        String filename = args[0];

        ArrayList<ArrayList<ArrayList<Float>>> localKs = new ArrayList<>();
        ArrayList<ArrayList<Float>> localbs = new ArrayList<>();
        ArrayList<ArrayList<Float>> K = new ArrayList<>();
        ArrayList<Float> b = new ArrayList<>();
        ArrayList<Float> T = new ArrayList<>();
        Mesh m = new Mesh();

        System.out.print("IMPLEMENTACION DEL METODO DE LOS ELEMENTOS FINITOS\n" +
                "\t- TRANSFERENCIA DE CALOR\n\t- 3 DIMENSIONES\n" +
                "\t- FUNCIONES DE FORMA LINEALES\n\t- PESOS DE GALERKIN\n" +
                "\t- ELEMENTOS TETRAHEDROS\n" +
                "*********************************************************************************\n\n");

        try {
            leerMallayCondiciones(m, filename);
        } catch(IOException e) {
            System.out.println(e);
        }

        System.out.println("Datos obtenidos correctamente\n********************");

        crearSistemasLocales(m, localKs, localbs);
        //showKs(localKs);
        //showBs(localbs);
        System.out.println("******************************");

        zeroes(K, m.getSize(Sizes.NODES.ordinal()));
        zeroesVector(b, m.getSize(Sizes.NODES.ordinal()));
        ensamblaje(m, localKs, localbs, K, b);
        //showMatrix(K);
        //showVector(b);
        System.out.println("******************************");
        //System.out.println(K.size() + "-" + K.get(0).size());
        //System.out.println(b.size());

        applyNeumann(m, b);
        //showMatrix(K);
        //showVector(b);
        System.out.println("******************************");
        //System.out.println(K.size() + "-" + K.get(0).size());
        //System.out.println(b.size());

        applyDirichlet(m, K, b);
        //showMatrix(K);
        //showVector(b);
        System.out.println("******************************");
        //System.out.println(K.size() + "-" + K.get(0).size());
        //System.out.println(b.size());


        zeroesVector(T, b.size());
        calculate(K, b, T);

        //System.out.println("La respuesta es: ");
        //showVector(T);

        writeResults(m, T, filename);
    }
}
