package com.simu.classes;

import com.simu.enums.*;
import org.w3c.dom.NodeList;

import java.util.Arrays;

public class Mesh {
    private float[] parameters = new float[4];
    private int[] sizes = new int[4];
    Node[] nodeList;
    Element[] elementList;
    int[] indicesDirich;
    Condition[] dirichletList;
    Condition[] neumannList;

    public void setParameters(float EI, float f_x, float f_y, float f_z) {
        parameters[Parameters.EI.ordinal()] = EI;
        parameters[Parameters.FX.ordinal()] = f_x;
        parameters[Parameters.FY.ordinal()] = f_y;
        parameters[Parameters.FZ.ordinal()] = f_z;
    }

    public void setSizes(int nnodes, int neltos, int ndirich, int nneu) {
        sizes[Sizes.NODES.ordinal()] = nnodes;
        sizes[Sizes.ELEMENTS.ordinal()] = neltos;
        sizes[Sizes.DIRICHLET.ordinal()] = ndirich;
        sizes[Sizes.NEUMANN.ordinal()] = nneu;
    }

    public int getSize(int s) {
        return sizes[s];
    }

    public float getParameter(int p) {
        return parameters[p];
    }

    public void createData() {
        nodeList = new Node[sizes[Sizes.NODES.ordinal()]];
        Arrays.fill(nodeList, null);
        for(int i = 0; i < nodeList.length; i++) {
            nodeList[i] = new Node();
        }

        elementList = new Element[sizes[Sizes.ELEMENTS.ordinal()]];
        //System.out.println("Sizes: " + sizes[Sizes.ELEMENTS.ordinal()]);
        Arrays.fill(elementList, null);
        for(int i = 0; i < elementList.length; i++) {
            elementList[i] = new Element();
        }

        indicesDirich = new int[sizes[Sizes.DIRICHLET.ordinal()]];

        dirichletList = new Condition[sizes[Sizes.DIRICHLET.ordinal()]];
        Arrays.fill(dirichletList, null);
        for(int i = 0; i < dirichletList.length; i++) {
            dirichletList[i] = new Condition();
        }
        //System.out.println("a ver: " + dirichletList[0]);

        neumannList = new Condition[sizes[Sizes.NEUMANN.ordinal()]];
        Arrays.fill(neumannList, null);
        for(int i = 0; i < neumannList.length; i++) {
            neumannList[i] = new Condition();
        }
    }

    public Node[] getNodes() {
        return nodeList;
    }

    public Element[] getElementList() {
        return elementList;
    }

    public int[] getIndicesDirich() {
        return indicesDirich;
    }

    public Condition[] getDirichletList() {
        return dirichletList;
    }

    public Condition[] getNeumannList() {
        return neumannList;
    }

    public Node getNode(int i) {
        return nodeList[i];
    }

    public Element getElement(int i) {
        return elementList[i];
    }

    public Condition getCondition(int i, int type) {
        if(type == Sizes.DIRICHLET.ordinal()) return dirichletList[i];
        else return neumannList[i];
    }
}
