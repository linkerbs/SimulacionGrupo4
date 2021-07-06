package com.simu;

import com.simu.classes.Condition;
import com.simu.classes.Item;
import com.simu.classes.Mesh;
import com.simu.classes.Item;
import com.simu.enums.Types;
import com.simu.enums.Sizes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static com.simu.enums.Indicators.NOTHING;

public class Tools {

    private static void obtenerDatos(Scanner file, String nlines, int n, String mode, Item[] itemList, String type) {
        file.nextLine();
        file.nextLine();
        if (nlines == "DOUBLELINE") file.nextLine();

        for (int i = 0; i < n; i++) {
            switch (mode) {
                case "INT_FLOAT_DIRICH":
                    String[] intFloatLineDirich = file.nextLine().split("\\s+");
                    int e0Dirich = Integer.parseInt(intFloatLineDirich[0]);
                    float r0Dirich = Float.parseFloat(intFloatLineDirich[1]);
                    itemList[i].setValues(NOTHING.ordinal(), NOTHING.ordinal(), NOTHING.ordinal(),
                            NOTHING.ordinal(), e0Dirich, NOTHING.ordinal(), NOTHING.ordinal(), NOTHING.ordinal(),
                            NOTHING.ordinal(), NOTHING.ordinal(), NOTHING.ordinal(), NOTHING.ordinal(),
                            NOTHING.ordinal(), NOTHING.ordinal(), r0Dirich);
                case "INT_FLOAT":
                        String[] intFloatLine = file.nextLine().split("\\s+");
                        int e0 = Integer.parseInt(intFloatLine[0]);
                        float r0 = Float.parseFloat(intFloatLine[1]);
                        itemList[i].setValues(NOTHING.ordinal(), NOTHING.ordinal(), NOTHING.ordinal(),
                                NOTHING.ordinal(), e0, NOTHING.ordinal(), NOTHING.ordinal(), NOTHING.ordinal(),
                                NOTHING.ordinal(), NOTHING.ordinal(), NOTHING.ordinal(), NOTHING.ordinal(),
                                NOTHING.ordinal(), NOTHING.ordinal(), r0);
                        /*if (type == Sizes.DIRICHLET_X.ordinal()) System.out.println("X: " + e0 + " " + r0);
                        else System.out.println("N: " + e0 + " " + r0);*/
                    break;
                case "INT_FLOAT_FLOAT_FLOAT":
                    String[] intFloatFloatFloatLine = file.nextLine().split("\\s+");
                    int e = Integer.parseInt(intFloatFloatFloatLine[0]);
                    float r = Float.parseFloat(intFloatFloatFloatLine[1]);
                    float rr = Float.parseFloat(intFloatFloatFloatLine[2]);
                    float rrr = Float.parseFloat(intFloatFloatFloatLine[3]);
                    itemList[i].setValues(e, r, rr, rrr, NOTHING.ordinal(), NOTHING.ordinal(), NOTHING.ordinal(),
                            NOTHING.ordinal(), NOTHING.ordinal(),  NOTHING.ordinal(),  NOTHING.ordinal(),
                            NOTHING.ordinal(),  NOTHING.ordinal(),  NOTHING.ordinal(),  NOTHING.ordinal());
                    //System.out.println(e + " " + r + " " + rr + " " + rrr);
                    break;
                case "INT_10":
                    String[] intIntIntIntIntLine = file.nextLine().split("\\s+");
                    int e1 = Integer.parseInt(intIntIntIntIntLine[0]);
                    int e2 = Integer.parseInt(intIntIntIntIntLine[1]);
                    int e3 = Integer.parseInt(intIntIntIntIntLine[2]);
                    int e4 = Integer.parseInt(intIntIntIntIntLine[3]);
                    int e5 = Integer.parseInt(intIntIntIntIntLine[4]);
                    int e6 = Integer.parseInt(intIntIntIntIntLine[5]);
                    int e7 = Integer.parseInt(intIntIntIntIntLine[6]);
                    int e8 = Integer.parseInt(intIntIntIntIntLine[7]);
                    int e9 = Integer.parseInt(intIntIntIntIntLine[8]);
                    int e10 = Integer.parseInt(intIntIntIntIntLine[9]);
                    int e11 = Integer.parseInt(intIntIntIntIntLine[10]);
                    itemList[i].setValues(e1, NOTHING.ordinal(), NOTHING.ordinal(), NOTHING.ordinal(), e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, NOTHING.ordinal());
                    //System.out.println(e1 + " " + e2 + " " + e3 + " " + e4 + " " + e5);
                    break;
            }
        }
    }

    private static void correctConditions(int n, int nx, int ny, int nz, Condition[] list, int[] indices, int nnodes) {
        //System.out.println(list.length);
        //System.out.println(indices.length);
        for (int i = 0; i < n; i++)
            indices[i] = list[i].getNode1();

        for (int i = 0; i < n - 1; i++) {
            int pivot = list[i].getNode1();
            for (int j = i; j < n; j++) {
                if (list[j].getNode1() > pivot)
                    list[j].setNode1(list[j].getNode1() - 1);
            }
        }
    }

    public static void leerMallayCondiciones(Mesh m, String filename) throws IOException {
        File file = new File(filename + ".dat");
        Scanner scanner = new Scanner(file);

        String firstLine = scanner.nextLine();
        String secondLine = scanner.nextLine();

        float EI = Float.parseFloat(firstLine.split(" ")[0]);
        float f_x = Float.parseFloat(firstLine.split(" ")[1]);
        float f_y = Float.parseFloat(firstLine.split(" ")[2]);
        float f_z = Float.parseFloat(firstLine.split(" ")[3]);
        int nnodes = Integer.parseInt(secondLine.split(" ")[0]);
        int neltos = Integer.parseInt(secondLine.split(" ")[1]);
        int ndirich_x = Integer.parseInt(secondLine.split(" ")[2]);
        int ndirich_y = Integer.parseInt(secondLine.split(" ")[3]);
        int ndirich_z = Integer.parseInt(secondLine.split(" ")[4]);
        int ndirich = ndirich_x + ndirich_y + ndirich_z;
        int nneu = Integer.parseInt(secondLine.split(" ")[5]);

        //System.out.println(" " + EI + " " + f_x + " " + f_y + " " + f_z);
        //System.out.println(nnodes + " " + neltos + " " + ndirich_x + " " + ndirich_y + " " + ndirich_z + " " + nneu);

        m.setParameters(EI, f_x, f_y, f_z);
        m.setSizes(nnodes, neltos, ndirich, nneu);
        m.createData();

        obtenerDatos(scanner, "SINGLELINE", nnodes, "INT_FLOAT_FLOAT_FLOAT", m.getNodes(), Sizes.NODES.name());
        obtenerDatos(scanner, "DOUBLELINE", neltos, "INT_10", m.getElementList(), Sizes.ELEMENTS.name());
        obtenerDatos(scanner, "DOUBLELINE", ndirich_x, "INT_FLOAT_DIRICH", m.getDirichletList(), Types.DIRICHLET_X.name());
        obtenerDatos(scanner, "DOUBLELINE", ndirich_y, "INT_FLOAT_DIRICH", m.getDirichletList(), Types.DIRICHLET_Y.name());
        obtenerDatos(scanner, "DOUBLELINE", ndirich_z, "INT_FLOAT_DIRICH", m.getDirichletList(), Types.DIRICHLET_Z.name());
        obtenerDatos(scanner, "DOUBLELINE", nneu, "INT_FLOAT", m.getNeumannList(), Sizes.NEUMANN.name());

        scanner.close();

        for(int i = 0; i < m.getDirichletList().length; i++)
            System.out.println("dirich: " + m.getDirichletList()[i].getNode1());

        correctConditions(ndirich, ndirich_x, ndirich_y, ndirich_z, m.getDirichletList(), m.getIndicesDirich(), m.getSize(Sizes.NODES.ordinal()));

        for(int i = 0; i < m.getDirichletList().length; i++)
            System.out.println("dirich2: " + m.getDirichletList()[i].getNode1());
    }

    private static boolean findIndex(int v, int s, int[] arr) {
        for (int i = 0; i < s; i++)
            if (arr[i] == v)
                return true;
        return false;
    }

    /*public static void writeResults(Mesh m, ArrayList<Float> T, String filename) {
        int[] dirichIndices = m.getIndicesDirich();
        Condition[] dirich = m.getDirichletList();
        String outputFileName = filename + ".post.res";

        String outputFileData = "GiD Post Results File 1.0\n" +
                "Result \"Temperature\" \"Load Case 1\" 1 Scalar OnNodes\nComponentNames \"T\"\nValues\n";

        int Tpos = 0;
        int Dpos = 0;
        int n = m.getSize(Sizes.NODES.ordinal());
        int nd = m.getSize(Sizes.DIRICHLET.ordinal());

        for(int i = 0; i < n; i++) {
            if (findIndex(i + 1, nd, dirichIndices)) {
                outputFileData += i + 1 + " " + dirich[Dpos].getValue() + "\n";
                Dpos++;
            } else {
                outputFileData += i + 1 + " " + T.get(Tpos) + "\n";
                Tpos++;
            }
        }
        outputFileData += "End values\n";
        try {
            FileWriter myWriter = new FileWriter(outputFileName);
            myWriter.write(outputFileData);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }*/
}
