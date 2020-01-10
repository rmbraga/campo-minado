package com.braga.model;

import com.braga.exception.ExplosaoException;

import java.util.ArrayList;
import java.util.List;

public class Campo {

    private final int linha;
    private final int coluna;

    private boolean aberto;
    private boolean minado;
    private boolean marcado;

    private List<Campo> vizinhos = new ArrayList<>();

    Campo(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    boolean adicionarVizinho(Campo candidatoAVizinho) {
        /**
         * Variables to know if the current line/column
         * is different from the candidate neighbor line/column...
         * And also to know the diagonal line
         */
        boolean linhaDiferente = linha != candidatoAVizinho.linha;
        boolean colunaDiferente = coluna != candidatoAVizinho.coluna;
        boolean diagonal = linhaDiferente && colunaDiferente;

        int deltaLinha = Math.abs(linha - candidatoAVizinho.linha);
        int deltaColuna = Math.abs(coluna - candidatoAVizinho.coluna);
        int deltaGeral = deltaColuna + deltaLinha;

        // Calculate if we have neghbors lines/columns
        if (deltaGeral == 1 && !diagonal) {
            vizinhos.add(candidatoAVizinho);
            return true;
        } else if (deltaGeral == 2 && diagonal) {
            vizinhos.add(candidatoAVizinho);
            return true;
        } else {
            return false;
        }
    }

    void alternarMarcacao() {
        if (!aberto) {
            marcado = !marcado;
        }
    }

    boolean abrir() {
        if (!aberto && !marcado) {
            aberto = true;

            if (minado) {
                throw new ExplosaoException();
            }

            if (vizinhancaSegura()) {
                vizinhos.forEach(v -> v.abrir());
            }
            return true;
        } else {
            return false;
        }
    }

    boolean vizinhancaSegura() {
        return vizinhos.stream().noneMatch(v -> v.minado);
    }

    public boolean isMinado() {
        return minado;
    }

    void setAberto(boolean aberto) {
        this.aberto = aberto;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public boolean isAberto() {
        return aberto;
    }

    public boolean isFechado() {
        return !isAberto();
    }

    void minar() {
        if (!minado) {
            minado = true;
        }
    }

    public int getLinha() {
        return linha;
    }

    public int getColuna() {
        return coluna;
    }

    boolean objetivoAlcancado() {
        boolean desvendado = !minado && aberto;
        boolean protegido = minado && marcado;
        return desvendado || protegido;
    }

    long minasNaVizinhanca() {
        return vizinhos.stream().filter(v -> v.minado).count();
    }

    void reiniciar() {
        aberto = false;
        minado = false;
        marcado = false;
    }

    public String toString() {
        if (marcado) {
            return "x";
        } else if (aberto && minado) {
            return "*";
        } else if (aberto && minasNaVizinhanca() > 0) {
            return Long.toString(minasNaVizinhanca());
        } else if (aberto) {
            return " ";
        } else {
            return "?";
        }
    }
}