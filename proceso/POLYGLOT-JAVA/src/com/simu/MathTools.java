package com.simu;

import java.util.ArrayList;
import java.util.Collections;

public class MathTools {
    public static void zeroes(ArrayList<ArrayList<Float>> M, int n){
        for(int i = 0; i < n; i++){
            ArrayList<Float> row = new ArrayList<>(Collections.nCopies(n, 0.0F));
            M.add(row);
        }
    }

    public static void zeroes(ArrayList<ArrayList<Float>> M, int n, int m){
        for(int i = 0; i < n; i++){
            ArrayList<Float> row = new ArrayList<>(Collections.nCopies(m, 0.0F));
            //System.out.println(row.toString());
            M.add(row);
        }
    }

    public static void zeroesVector(ArrayList<Float> v,int n){
        for(int i = 0; i < n; i++){
            v.add(0.0F);
        }
    }

    public static void copyMatrix(ArrayList<ArrayList<Float>> A, ArrayList<ArrayList<Float>> copy){
        zeroes(copy, A.size());
        for (int i = 0; i < A.size(); i++)
            for (int j = 0; j < A.get(0).size(); j++)
                copy.get(i).add(j, A.get(i).get(j));
    }

    public static float calculateMember(int i, int j, int r, ArrayList<ArrayList<Float>> A, ArrayList<ArrayList<Float>> B){
        float member = 0;
        for(int k=0;k<r;k++)
            member += A.get(i).get(k)*B.get(k).get(j);
        return member;
    }

    public static ArrayList<ArrayList<Float>> productMatrixMatrix(ArrayList<ArrayList<Float>> A, ArrayList<ArrayList<Float>> B,int n,int r,int m){
        ArrayList<ArrayList<Float>> R = new ArrayList<>();
        zeroes(R,n,m);
        //System.out.println(R.toString());
        for(int i = 0; i < n; i++)
            for(int j = 0; j < m; j++)
                R.get(i).set(j, calculateMember(i,j,r,A,B));

        return R;
    }

    public static void productMatrixVector(ArrayList<ArrayList<Float>> A, ArrayList<Float> v, ArrayList<Float> R){
        for(int f = 0; f < A.size(); f++){
            float cell = (float)0.0;
            for(int c = 0; c < v.size(); c++){
                cell += A.get(f).get(c) * v.get(c);
            }
            R.set(f, R.get(f) + cell);
        }
    }

    public static void productRealMatrix(float real, ArrayList<ArrayList<Float>> M, ArrayList<ArrayList<Float>> R){
        zeroes(R, M.size());
        for(int i=0;i<M.size();i++)
            for(int j=0;j<M.get(0).size();j++)
                R.get(i).set(j, real*M.get(i).get(j));
    }

    public static void getMinor(ArrayList<ArrayList<Float>> M, int ind, int jind){
        M.remove(ind);
        for(int i = 0; i < M.size(); i++)
            M.get(i).remove(jind);
    }

    public static float determinant(ArrayList<ArrayList<Float>> M){
        if(M.size() == 1) return M.get(0).get(0);
        else{
            float det = (float)0.0;
            for(int i = 0; i < M.get(0).size(); i++){
                ArrayList<ArrayList<Float>> minor = new ArrayList<>();
                copyMatrix(M, minor);
                getMinor(minor,0,i);
                det += Math.pow(-1,i) * M.get(0).get(i) * determinant(minor);
            }
            return det;
        }
    }

    public static void cofactors(ArrayList<ArrayList<Float>> M, ArrayList<ArrayList<Float>> Cof){
        zeroes(Cof, M.size());
        for(int i = 0; i < M.size(); i++){
            for(int j = 0; j < M.get(0).size(); j++){
                //cout << "Calculando cofactor ("<<i+1<<","<<j+1<<")...\n";
                ArrayList<ArrayList<Float>> minor = new ArrayList<>();
                copyMatrix(M, minor);
                getMinor(minor,i,j);
                Cof.get(i).set(j, (float)(Math.pow(-1,i+j) * determinant(minor)));
            }
        }
    }

    public static void transpose(ArrayList<ArrayList<Float>> M, ArrayList<ArrayList<Float>> T){
        zeroes(T,M.get(0).size(),M.size());
        for(int i = 0; i < M.size(); i++)
            for(int j = 0; j < M.get(0).size(); j++)
                T.get(j).add(i, M.get(i).get(j));
    }

    public static void inverseMatrix(ArrayList<ArrayList<Float>> M, ArrayList<ArrayList<Float>> Minv){
        System.out.println("Iniciando calculo de inversa...");
        ArrayList<ArrayList<Float>> Cof = new ArrayList<>();
        ArrayList<ArrayList<Float>> Adj = new ArrayList<>();
        System.out.println("Calculo de determinante...");
        float det = determinant(M);
        if(det == 0) System.exit(1);
        System.out.println("Iniciando calculo de cofactores...");
        cofactors(M,Cof);
        System.out.println("Calculo de adjunta...");
        transpose(Cof,Adj);
        System.out.println("Calculo de inversa...");
        productRealMatrix(1/det, Adj, Minv);
    }

}
