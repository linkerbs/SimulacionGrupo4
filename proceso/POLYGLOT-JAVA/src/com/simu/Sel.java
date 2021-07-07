package com.simu;

import com.simu.classes.Condition;
import com.simu.classes.Element;
import com.simu.classes.Mesh;
import com.simu.classes.Node;
import com.simu.enums.Parameters;
import com.simu.enums.Sizes;

import java.util.ArrayList;

import static com.simu.MathTools.*;
import static java.lang.Math.pow;

public class Sel {
    public static void showMatrix(ArrayList<ArrayList<Float>> K) {
        for(int i = 0; i < K.get(0).size(); i++) {
            System.out.print("[\t");
            for(int j = 0; j < K.size(); j++) {
                System.out.print(K.get(i).get(j) + "\t");
            }
            System.out.println("]");
        }
    }
    public static void showKs(ArrayList<ArrayList<ArrayList<Float>>> Ks) {
        for(int i = 0; i < Ks.size(); i++) {
            System.out.println("K del elemento " + (i + 1) + ":");
            showMatrix(Ks.get(i));
            System.out.println("*************************************");
        }
    }

    public static void showVector(ArrayList<Float> b) {
        System.out.print("[\t");
        for(int i = 0; i < b.size(); i++) {
            System.out.print(b.get(i) + "\t");
        }
        System.out.println("]");
    }

    public static void showBs(ArrayList<ArrayList<Float>> bs) {
        for(int i = 0; i < bs.size(); i++) {
            System.out.println("b del elemento " + (i+1));
            showVector(bs.get(i));
            System.out.println("*************************************");
        }
    }

    private static void calculateKWithMiu(ArrayList<ArrayList<Float>> temp, ArrayList<ArrayList<Float>> miu) {
        for(int i = 0; i < temp.size(); i++){
            for(int j = 0; j < temp.size(); j++){
                if(i < 10 && j < 10 || i >= 10 && i < 20 && j >= 10 && j < 20 || i >=20 && j >= 20) {
                    temp.get(i).set(j, miu.get(i % 10).get(j % 10));
                }
            }
        }
    }

    private static float calculatec1(float x1, float x2) {
        float zeroAprox = (float) 0.0000000001;
        if(x2 - x1 == 0)
            return (float) (1/ pow(zeroAprox, 2));
        else
            return (float) (1/ pow(x2-x1, 2));
    }

    private static float calculatec2(float x1, float x2, float x8) {
        float zeroAprox = (float) 0.0000000001;
        float v = 4 * x1 + 4 * x2 - 8 * x8;
        float r = (1/(x2 - x1)) * v;

        if(x2 - x1 == 0 || v == 0)
            return (1/zeroAprox) * zeroAprox;
        else
            return r;
    }

    private static float calculateA (int i, Mesh m) {
        Element e = m.getElement(i);

        Node node1 = m.getNode(e.getNode1() - 1);
        Node node2 = m.getNode(e.getNode2() - 1);
        Node node8 = m.getNode(e.getNode8() - 1);

        float c1 = calculatec1(node1.getX(), node2.getX());
        float c2 = calculatec2(node1.getX(), node2.getX(), node8.getX());

        return (float) ((-1/ pow(c2,2)*192) * pow(4*c1-c2, 4) -
                (1/c2*24) * pow(4*c1-c2, 3) -
                (1/ pow(c2,3)*3840) * pow(4*c1-c2, 5) +
                (1/ pow(c2,3)*3840) * pow(4*c1+3*c2, 5));
    }

    private static float calculateB (int i, Mesh m) {
        Element e = m.getElement(i);

        Node node1 = m.getNode(e.getNode1() - 1);
        Node node2 = m.getNode(e.getNode2() - 1);
        Node node8 = m.getNode(e.getNode8() - 1);

        float c1 = calculatec1(node1.getX(), node2.getX());
        float c2 = calculatec2(node1.getX(), node2.getX(), node8.getX());

        return (float) ((-1/ pow(c2,2)*192) * pow(4*c1+c2, 4) +
                (1/c2*24) * pow(4*c1+c2, 3) +
                (1/ pow(c2,3)*3840) * pow(4*c1+c2, 5) -
                (1/ pow(c2,3)*3840) * pow(4*c1-3*c2, 5));
    }

    private static float calculateC (int i, Mesh m) {
        Element e = m.getElement(i);

        Node node1 = m.getNode(e.getNode1() - 1);
        Node node2 = m.getNode(e.getNode2() - 1);
        Node node8 = m.getNode(e.getNode8() - 1);

        float c2 = calculatec2(node1.getX(), node2.getX(), node8.getX());

        return (float) ((4/15) * pow(c2,2));
    }

    private static float calculateD (int i, Mesh m) {
        Element e = m.getElement(i);

        Node node1 = m.getNode(e.getNode1() - 1);
        Node node2 = m.getNode(e.getNode2() - 1);
        Node node8 = m.getNode(e.getNode8() - 1);

        float c1 = calculatec1(node1.getX(), node2.getX());
        float c2 = calculatec2(node1.getX(), node2.getX(), node8.getX());

        return (float) ((1/ pow(c2,2)*192)* pow(4*c2-c1, 4) - (1/ pow(c2,3)*3840)* pow(4*c2-c1, 5) +
                (1/ pow(c2,3)*7680)* pow(4*c2+8*c1, 5) - (7/ pow(c2,3)*7680)* pow(4*c2-8*c1, 5) +
                (1/ pow(c2,3)*768)* pow(-8*c1,5) - (c1/ pow(c2,3)*96)* pow(4*c2-8*c1,4) +
                ((2*c1-1)/ pow(c2,3)*192)* pow(-8*c1,4));
    }

    private static float calculateE (int i, Mesh m) {
        Element e = m.getElement(i);

        Node node1 = m.getNode(e.getNode1() - 1);
        Node node2 = m.getNode(e.getNode2() - 1);
        Node node8 = m.getNode(e.getNode8() - 1);

        float c1 = calculatec1(node1.getX(), node2.getX());
        float c2 = calculatec2(node1.getX(), node2.getX(), node8.getX());

        return (float) ((8/3)* pow(c1,2) + (1/30)* pow(c2,2));
    }

    private static float calculateF (int i, Mesh m) {
        Element e = m.getElement(i);

        Node node1 = m.getNode(e.getNode1() - 1);
        Node node2 = m.getNode(e.getNode2() - 1);
        Node node8 = m.getNode(e.getNode8() - 1);

        float c1 = calculatec1(node1.getX(), node2.getX());
        float c2 = calculatec2(node1.getX(), node2.getX(), node8.getX());

        return (float) ((2/3)*c1*c2 - (1/30)* pow(c2,2));
    }

    private static float calculateG (int i, Mesh m) {
        Element e = m.getElement(i);

        Node node1 = m.getNode(e.getNode1() - 1);
        Node node2 = m.getNode(e.getNode2() - 1);
        Node node8 = m.getNode(e.getNode8() - 1);

        float c1 = calculatec1(node1.getX(), node2.getX());
        float c2 = calculatec2(node1.getX(), node2.getX(), node8.getX());

        return (float) ((-16/3)* pow(c1,2) - (4/3)*c1*c2 - (2/15)* pow(c2,2));
    }

    private static float calculateH (int i, Mesh m) {
        Element e = m.getElement(i);

        Node node1 = m.getNode(e.getNode1() - 1);
        Node node2 = m.getNode(e.getNode2() - 1);
        Node node8 = m.getNode(e.getNode8() - 1);

        float c1 = calculatec1(node1.getX(), node2.getX());
        float c2 = calculatec2(node1.getX(), node2.getX(), node8.getX());

        return (float) ((2/3)*c1*c2 + (1/30)* pow(c2,2));
    }

    private static float calculateI (int i, Mesh m) {
        Element e = m.getElement(i);

        Node node1 = m.getNode(e.getNode1() - 1);
        Node node2 = m.getNode(e.getNode2() - 1);
        Node node8 = m.getNode(e.getNode8() - 1);

        float c1 = calculatec1(node1.getX(), node2.getX());
        float c2 = calculatec2(node1.getX(), node2.getX(), node8.getX());

        return (float) ((-16/3)*pow(c1,2) - (2/3)*pow(c2,2));
    }

    private static float calculateJ (int i, Mesh m) {
        Element e = m.getElement(i);

        Node node1 = m.getNode(e.getNode1() - 1);
        Node node2 = m.getNode(e.getNode2() - 1);
        Node node8 = m.getNode(e.getNode8() - 1);

        float c1 = calculatec1(node1.getX(), node2.getX());
        float c2 = calculatec2(node1.getX(), node2.getX(), node8.getX());

        return (float) ((2/15)*pow(c2,2));
    }

    private static float calculateK (int i, Mesh m) {
        Element e = m.getElement(i);

        Node node1 = m.getNode(e.getNode1() - 1);
        Node node2 = m.getNode(e.getNode2() - 1);
        Node node8 = m.getNode(e.getNode8() - 1);

        float c1 = calculatec1(node1.getX(), node2.getX());
        float c2 = calculatec2(node1.getX(), node2.getX(), node8.getX());

        return (float) ((-4/3)*c1*c2);
    }

    private static void fillMiuMatrix (ArrayList<ArrayList<Float>> miu, float A, float B,
                                       float C, float D, float E, float F, float G, float H,
                                       float I, float J, float K)
    {

        miu.get(0).set(0, A);
        miu.get(0).set(1, E);
        miu.get(0).set(4, -F);
        miu.get(0).set(6, -F);
        miu.get(0).set(7, G);
        miu.get(0).set(8, F);
        miu.get(0).set(9, F);

        miu.get(1).set(0, E);
        miu.get(1).set(1, B);
        miu.get(1).set(4, -H);
        miu.get(1).set(6, -H);
        miu.get(1).set(7, I);
        miu.get(1).set(8, H);
        miu.get(1).set(9, H);

        miu.get(4).set(0, -F);
        miu.get(4).set(1, -H);
        miu.get(4).set(4, C);
        miu.get(4).set(6, J);
        miu.get(4).set(7, -K);
        miu.get(4).set(8, -C);
        miu.get(4).set(9, -J);

        miu.get(6).set(0, -F);
        miu.get(6).set(1, -H);
        miu.get(6).set(4, J);
        miu.get(6).set(6, C);
        miu.get(6).set(7, -K);
        miu.get(6).set(8, -J);
        miu.get(6).set(9, -C);

        miu.get(7).set(0, G);
        miu.get(7).set(1, I);
        miu.get(7).set(4, -K);
        miu.get(7).set(6, -K);
        miu.get(7).set(7, D);
        miu.get(7).set(8, K);
        miu.get(7).set(9, K);

        miu.get(8).set(0, F);
        miu.get(8).set(1, H);
        miu.get(8).set(4, -C);
        miu.get(8).set(6, -J);
        miu.get(8).set(7, K);
        miu.get(8).set(8, C);
        miu.get(8).set(9, J);

        miu.get(9).set(0, F);
        miu.get(9).set(1, H);
        miu.get(9).set(4, -J);
        miu.get(9).set(6, -C);
        miu.get(9).set(7, K);
        miu.get(9).set(8, J);
        miu.get(9).set(9, C);

    }

    private static void calculateLocalMiu(int el, Mesh m, ArrayList<ArrayList<Float>> miuMatrix) {
        float A = calculateA(el, m);
        float B = calculateB(el, m);
        float C = calculateC(el, m);
        float D = calculateD(el, m);
        float E = calculateE(el, m);
        float F = calculateF(el, m);
        float G = calculateG(el, m);
        float H = calculateH(el, m);
        float I = calculateI(el, m);
        float J = calculateJ(el, m);
        float K = calculateK(el, m);

        fillMiuMatrix(miuMatrix, A, B, C, D, E, F, G, H, I, J, K);
    }

    public static ArrayList<ArrayList<Float>> createLocalK(int element,Mesh m){
        float J = calculateLocalJ(element, m);
        float EI = m.getParameter(Parameters.EI.ordinal());

        ArrayList<ArrayList<Float>> K = new ArrayList<>();
        ArrayList<ArrayList<Float>> miuMatrix = new ArrayList<>();
        ArrayList<ArrayList<Float>> KWithMiu = new ArrayList<>();

        zeroes(miuMatrix,10);
        calculateLocalMiu(element, m, miuMatrix);

        zeroes(KWithMiu, 30);
        calculateKWithMiu(KWithMiu, miuMatrix);

        //System.out.println(KWithMiu.size() + " " + KWithMiu.get(0).size());

        productRealMatrix(EI * J, KWithMiu, K);

        return K;
    }

    public static float calculateLocalJ(int ind, Mesh m){
        float J,a,b,c,d,e,f,g,h,i;

        Element el = m.getElement(ind);

        Node n1 = m.getNode(el.getNode1()-1);
        Node n2 = m.getNode(el.getNode2()-1);
        Node n3 = m.getNode(el.getNode3()-1);
        Node n4 = m.getNode(el.getNode4()-1);

        a=n2.getX()-n1.getX();b=n3.getX()-n1.getX();c=n4.getX()-n1.getX();
        d=n2.getY()-n1.getY();e=n3.getY()-n1.getY();f=n4.getY()-n1.getY();
        g=n2.getZ()-n1.getZ();h=n3.getZ()-n1.getZ();i=n4.getZ()-n1.getZ();
        //Se calcula el determinante de esta matriz utilizando
        //la Regla de Sarrus.
        J = a*e*i+d*h*c+g*b*f-g*e*c-a*h*f-d*b*i;

        return J;
    }

    private static void calculateTauMatrixWithTau (ArrayList<ArrayList<Float>> tauMatrix, ArrayList<Float> tau) {
        for(int i = 0; i < tauMatrix.size(); i++){
            for(int j = 0; j < tauMatrix.get(0).size(); j++){
                if(i < 10 && j == 0 || i >= 10 && i < 20 && j == 1 || i >=20 && j == 2) {
                    tauMatrix.get(i).set(j, tau.get(i % 10));
                }
            }
        }
    }

    public static ArrayList<Float> createLocalb(int element, Mesh m){
        ArrayList<Float> b = new ArrayList<Float>();
        ArrayList<Float> f = new ArrayList<Float>();
        ArrayList<Float> tau = new ArrayList<Float>();
        ArrayList<ArrayList<Float>> tauMatrix = new ArrayList<>();
        ArrayList<ArrayList<Float>> BxB_i = new ArrayList<>();

        float f_x = m.getParameter(Parameters.FX.ordinal());
        float f_y = m.getParameter(Parameters.FY.ordinal());
        float f_z = m.getParameter(Parameters.FZ.ordinal());

        float J, b_i;

        zeroes(tauMatrix, 30, 3);
        zeroesVector(b, 30);
        zeroesVector(tau, 10);

        f.add(f_x);
        f.add(f_y);
        f.add(f_z);

        J = calculateLocalJ(element, m);
        b_i = (float) (J / 120.0);

        tau.set(0, 59F);
        tau.set(1, (float) -1);
        tau.set(2, (float) -1);
        tau.set(3, (float) -1);
        tau.set(4, 4F);
        tau.set(5, 4F);
        tau.set(6, 4F);
        tau.set(7, 4F);
        tau.set(8, 4F);
        tau.set(9, 4F);

        calculateTauMatrixWithTau(tauMatrix, tau);

        productRealMatrix(b_i, tauMatrix, BxB_i);

        productMatrixVector(BxB_i, f, b);

        return b;
    }

    public static void crearSistemasLocales(Mesh m, ArrayList<ArrayList<ArrayList<Float>>> localKs, ArrayList<ArrayList<Float>> localbs){
        for(int i = 0; i < m.getSize(Sizes.ELEMENTS.ordinal()); i++){
            localKs.add(createLocalK(i,m));
            localbs.add(createLocalb(i,m));
        }
    }

    public static void assemblyK(Element e, ArrayList<ArrayList<Float>> localK, ArrayList<ArrayList<Float>> K){
        int index1 = e.getNode1() - 1;
        int index2 = e.getNode2() - 1;
        int index3 = e.getNode3() - 1;
        int index4 = e.getNode4() - 1;
        int index5 = e.getNode5() - 1;
        int index6 = e.getNode6() - 1;
        int index7 = e.getNode7() - 1;
        int index8 = e.getNode8() - 1;
        int index9 = e.getNode9() - 1;
        int index10 = e.getNode10() - 1;

        K.get(index1).set(index1, K.get(index1).get(index1) + localK.get(0).get(0));
        K.get(index1).set(index2, K.get(index1).get(index2) + localK.get(0).get(1));
        K.get(index1).set(index3, K.get(index1).get(index3) + localK.get(0).get(2));
        K.get(index1).set(index4, K.get(index1).get(index4) + localK.get(0).get(3));
        K.get(index1).set(index5, K.get(index1).get(index5) + localK.get(0).get(4));
        K.get(index1).set(index6, K.get(index1).get(index6) + localK.get(0).get(5));
        K.get(index1).set(index7, K.get(index1).get(index7) + localK.get(0).get(6));
        K.get(index1).set(index8, K.get(index1).get(index8) + localK.get(0).get(7));
        K.get(index1).set(index9, K.get(index1).get(index9) + localK.get(0).get(8));
        K.get(index1).set(index10, K.get(index1).get(index10) + localK.get(0).get(9));

        K.get(index2).set(index1, K.get(index2).get(index1) + localK.get(1).get(0));
        K.get(index2).set(index2, K.get(index2).get(index2) + localK.get(1).get(1));
        K.get(index2).set(index3, K.get(index2).get(index3) + localK.get(1).get(2));
        K.get(index2).set(index4, K.get(index2).get(index4) + localK.get(1).get(3));
        K.get(index2).set(index5, K.get(index2).get(index5) + localK.get(1).get(4));
        K.get(index2).set(index6, K.get(index2).get(index6) + localK.get(1).get(5));
        K.get(index2).set(index7, K.get(index2).get(index7) + localK.get(1).get(6));
        K.get(index2).set(index8, K.get(index2).get(index8) + localK.get(1).get(7));
        K.get(index2).set(index9, K.get(index2).get(index9) + localK.get(1).get(8));
        K.get(index2).set(index10, K.get(index2).get(index10) + localK.get(1).get(9));

        K.get(index3).set(index1, K.get(index3).get(index1) + localK.get(2).get(0));
        K.get(index3).set(index2, K.get(index3).get(index2) + localK.get(2).get(1));
        K.get(index3).set(index3, K.get(index3).get(index3) + localK.get(2).get(2));
        K.get(index3).set(index4, K.get(index3).get(index4) + localK.get(2).get(3));
        K.get(index3).set(index5, K.get(index3).get(index5) + localK.get(2).get(4));
        K.get(index3).set(index6, K.get(index3).get(index6) + localK.get(2).get(5));
        K.get(index3).set(index7, K.get(index3).get(index7) + localK.get(2).get(6));
        K.get(index3).set(index8, K.get(index3).get(index8) + localK.get(2).get(7));
        K.get(index3).set(index9, K.get(index3).get(index9) + localK.get(2).get(8));
        K.get(index3).set(index10, K.get(index3).get(index10) + localK.get(2).get(9));

        K.get(index4).set(index1, K.get(index4).get(index1) + localK.get(3).get(0));
        K.get(index4).set(index2, K.get(index4).get(index2) + localK.get(3).get(1));
        K.get(index4).set(index3, K.get(index4).get(index3) + localK.get(3).get(2));
        K.get(index4).set(index4, K.get(index4).get(index4) + localK.get(3).get(3));
        K.get(index4).set(index5, K.get(index4).get(index5) + localK.get(3).get(4));
        K.get(index4).set(index6, K.get(index4).get(index6) + localK.get(3).get(5));
        K.get(index4).set(index7, K.get(index4).get(index7) + localK.get(3).get(6));
        K.get(index4).set(index8, K.get(index4).get(index8) + localK.get(3).get(7));
        K.get(index4).set(index9, K.get(index4).get(index9) + localK.get(3).get(8));
        K.get(index4).set(index10, K.get(index4).get(index10) + localK.get(3).get(9));

        K.get(index5).set(index1, K.get(index5).get(index1) + localK.get(4).get(0));
        K.get(index5).set(index2, K.get(index5).get(index2) + localK.get(4).get(1));
        K.get(index5).set(index3, K.get(index5).get(index3) + localK.get(4).get(2));
        K.get(index5).set(index4, K.get(index5).get(index4) + localK.get(4).get(3));
        K.get(index5).set(index5, K.get(index5).get(index5) + localK.get(4).get(4));
        K.get(index5).set(index6, K.get(index5).get(index6) + localK.get(4).get(5));
        K.get(index5).set(index7, K.get(index5).get(index7) + localK.get(4).get(6));
        K.get(index5).set(index8, K.get(index5).get(index8) + localK.get(4).get(7));
        K.get(index5).set(index9, K.get(index5).get(index9) + localK.get(4).get(8));
        K.get(index5).set(index10, K.get(index5).get(index10) + localK.get(4).get(9));

        K.get(index6).set(index1, K.get(index6).get(index1) + localK.get(5).get(0));
        K.get(index6).set(index2, K.get(index6).get(index2) + localK.get(5).get(1));
        K.get(index6).set(index3, K.get(index6).get(index3) + localK.get(5).get(2));
        K.get(index6).set(index4, K.get(index6).get(index4) + localK.get(5).get(3));
        K.get(index6).set(index5, K.get(index6).get(index5) + localK.get(5).get(4));
        K.get(index6).set(index6, K.get(index6).get(index6) + localK.get(5).get(5));
        K.get(index6).set(index7, K.get(index6).get(index7) + localK.get(5).get(6));
        K.get(index6).set(index8, K.get(index6).get(index8) + localK.get(5).get(7));
        K.get(index6).set(index9, K.get(index6).get(index9) + localK.get(5).get(8));
        K.get(index6).set(index10, K.get(index6).get(index10) + localK.get(5).get(9));

        K.get(index7).set(index1, K.get(index7).get(index1) + localK.get(6).get(0));
        K.get(index7).set(index2, K.get(index7).get(index2) + localK.get(6).get(1));
        K.get(index7).set(index3, K.get(index7).get(index3) + localK.get(6).get(2));
        K.get(index7).set(index4, K.get(index7).get(index4) + localK.get(6).get(3));
        K.get(index7).set(index5, K.get(index7).get(index5) + localK.get(6).get(4));
        K.get(index7).set(index6, K.get(index7).get(index6) + localK.get(6).get(5));
        K.get(index7).set(index7, K.get(index7).get(index7) + localK.get(6).get(6));
        K.get(index7).set(index8, K.get(index7).get(index8) + localK.get(6).get(7));
        K.get(index7).set(index9, K.get(index7).get(index9) + localK.get(6).get(8));
        K.get(index7).set(index10, K.get(index7).get(index10) + localK.get(6).get(9));

        K.get(index8).set(index1, K.get(index8).get(index1) + localK.get(7).get(0));
        K.get(index8).set(index2, K.get(index8).get(index2) + localK.get(7).get(1));
        K.get(index8).set(index3, K.get(index8).get(index3) + localK.get(7).get(2));
        K.get(index8).set(index4, K.get(index8).get(index4) + localK.get(7).get(3));
        K.get(index8).set(index5, K.get(index8).get(index5) + localK.get(7).get(4));
        K.get(index8).set(index6, K.get(index8).get(index6) + localK.get(7).get(5));
        K.get(index8).set(index7, K.get(index8).get(index7) + localK.get(7).get(6));
        K.get(index8).set(index8, K.get(index8).get(index8) + localK.get(7).get(7));
        K.get(index8).set(index9, K.get(index8).get(index9) + localK.get(7).get(8));
        K.get(index8).set(index10, K.get(index8).get(index10) + localK.get(7).get(9));

        K.get(index9).set(index1, K.get(index9).get(index1) + localK.get(8).get(0));
        K.get(index9).set(index2, K.get(index9).get(index2) + localK.get(8).get(1));
        K.get(index9).set(index3, K.get(index9).get(index3) + localK.get(8).get(2));
        K.get(index9).set(index4, K.get(index9).get(index4) + localK.get(8).get(3));
        K.get(index9).set(index5, K.get(index9).get(index5) + localK.get(8).get(4));
        K.get(index9).set(index6, K.get(index9).get(index6) + localK.get(8).get(5));
        K.get(index9).set(index7, K.get(index9).get(index7) + localK.get(8).get(6));
        K.get(index9).set(index8, K.get(index9).get(index8) + localK.get(8).get(7));
        K.get(index9).set(index9, K.get(index9).get(index9) + localK.get(8).get(8));
        K.get(index9).set(index10, K.get(index9).get(index10) + localK.get(8).get(9));

        K.get(index10).set(index1, K.get(index10).get(index1) + localK.get(9).get(0));
        K.get(index10).set(index2, K.get(index10).get(index2) + localK.get(9).get(1));
        K.get(index10).set(index3, K.get(index10).get(index3) + localK.get(9).get(2));
        K.get(index10).set(index4, K.get(index10).get(index4) + localK.get(9).get(3));
        K.get(index10).set(index5, K.get(index10).get(index5) + localK.get(9).get(4));
        K.get(index10).set(index6, K.get(index10).get(index6) + localK.get(9).get(5));
        K.get(index10).set(index7, K.get(index10).get(index7) + localK.get(9).get(6));
        K.get(index10).set(index8, K.get(index10).get(index8) + localK.get(9).get(7));
        K.get(index10).set(index9, K.get(index10).get(index9) + localK.get(9).get(8));
        K.get(index10).set(index10, K.get(index10).get(index10) + localK.get(9).get(9));
    }

    public static void assemblyb(Element e,ArrayList<Float> localb, ArrayList<Float> b){
        int index1 = e.getNode1() - 1;
        int index2 = e.getNode2() - 1;
        int index3 = e.getNode3() - 1;
        int index4 = e.getNode4() - 1;
        int index5 = e.getNode5() - 1;
        int index6 = e.getNode6() - 1;
        int index7 = e.getNode7() - 1;
        int index8 = e.getNode8() - 1;
        int index9 = e.getNode9() - 1;
        int index10 = e.getNode10() - 1;

        b.set(index1, b.get(index1) + localb.get(0));
        b.set(index2, b.get(index2) + localb.get(1));
        b.set(index3, b.get(index3) + localb.get(2));
        b.set(index4, b.get(index4) + localb.get(3));
        b.set(index5, b.get(index5) + localb.get(4));
        b.set(index6, b.get(index6) + localb.get(5));
        b.set(index7, b.get(index7) + localb.get(6));
        b.set(index8, b.get(index8) + localb.get(7));
        b.set(index9, b.get(index9) + localb.get(8));
        b.set(index10, b.get(index10) + localb.get(9));
    }

    public static void ensamblaje(Mesh m,ArrayList<ArrayList<ArrayList<Float>>> localKs, ArrayList<ArrayList<Float>> localbs, ArrayList<ArrayList<Float>> K, ArrayList<Float> b){
        for(int i = 0; i < m.getSize(Sizes.ELEMENTS.ordinal()); i++){
            Element e = m.getElement(i);
            assemblyK(e,localKs.get(i), K);
            assemblyb(e,localbs.get(i), b);
        }
    }

    public static void applyNeumann(Mesh m, ArrayList<Float> b){
        for(int i = 0; i < m.getSize(Sizes.NEUMANN.ordinal()); i++){
            Condition c = m.getCondition(i, Sizes.NEUMANN.ordinal());
            b.set(c.getNode1()-1, b.get(c.getNode1()-1) + c.getValue());
        }
    }

    public static void applyDirichlet(Mesh m, ArrayList<ArrayList<Float>> K, ArrayList<Float> b){
        for(int i = 0; i < m.getSize(Sizes.DIRICHLET.ordinal()); i++){
            Condition c = m.getCondition(i, Sizes.DIRICHLET.ordinal());
            int index = c.getNode1();
            if(index != 0)
                index = index - 1;

            K.remove(index);
            b.remove(index);

            for(int row = 0; row < K.size(); row++){
                float cell = K.get(row).get(index);
                K.get(row).remove(index);
                b.set(row, b.get(row) + (-1*c.getValue()*cell));
            }
        }
    }

    public static void calculate(ArrayList<ArrayList<Float>> K, ArrayList<Float> b, ArrayList<Float> T){
        System.out.println("Iniciando calculo de respuesta...");
        ArrayList<ArrayList<Float>> Kinv = new ArrayList<>();
        System.out.println("Calculo de inversa...");
        inverseMatrix(K, Kinv);
        System.out.println("Calculo de respuesta...");
        productMatrixVector(Kinv,b,T);
    }
}
