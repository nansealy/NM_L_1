package com.company;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static double[] LineToArr(String line) {
        // преобразование строки в массив double
        String[] strArr = line.split(" ");
        double[] doubleArr = new double[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            doubleArr[i] = Double.parseDouble(strArr[i]);
        }
        return doubleArr;
    }

    public static void InputMatrix(double[][] matrix) {
        //ввод матрицы
        Scanner in = new Scanner(System.in);
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = LineToArr(in.nextLine());
        }

    }

    public static void OutputMatrix(double[][] matrix) {
        // вывод двумерной матрицы
        for (double[] matrLine : matrix) {
            for (double elem : matrLine) {
                System.out.print(String.format("%10.3f", elem));
            }
            System.out.println();
        }
    }

    public static void OutputVector(double[] vector) {
        // вывод вектора
        for (double elem : vector) {
            System.out.print(String.format("%15.3f", elem));
        }
        System.out.println();
    }


    public static double[][] MatrixCopy(double[][] matrix){
        // копирование данных в новую матрицу
        double[][] matrixCopy = new double[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++){
            for (int j = 0; j < matrix[0].length; j++){
                matrixCopy[i][j] = matrix[i][j];
            }
        }
        return matrixCopy;
    }

    public static double[][] MatrixTranspose(double[][] matrix){
        // транспонирование матрицы
        double[][] resMatrix = new double[matrix.length][matrix[0].length];
        for(int i = 0; i < matrix.length; i++)
            for(int j = 0; j < matrix[i].length; j++){
                resMatrix[i][j] = matrix[j][i];
            }
        return resMatrix;
    }

    public static double[][] MatrixInverse(double [][] matrix){
        // получение обратной матрицы
        int n = matrix.length;
        double buf;
        double[][] unitMatrix = new double[n][n]; // единичная матрица
        double[][] invMatrix = new double[n][n]; // результирующая матрица
        System.arraycopy(matrix, 0, invMatrix, 0, n);

        for(int i = 0; i < n; i++)
            unitMatrix[i][i] = 1; // заполнение главной диагонали единицами

        for(int k = 0; k < n; k++){
            buf = invMatrix[k][k];
            for(int j = 0; j < n; j++){
                invMatrix[k][j] /= buf;
                unitMatrix[k][j] /= buf;
            }
            for(int i = k + 1; i < n; i++){
                buf = invMatrix[i][k];
                for(int j = 0; j < n; j++){
                    invMatrix[i][j] -= invMatrix[k][j] * buf;
                    unitMatrix[i][j] -= unitMatrix[k][j] * buf;
                }
            }
        }

        for (int k = n - 1; k > 0; k--) {
            for (int i = k - 1; i >= 0; i--) {
                buf = invMatrix[i][k];
                for (int j = 0; j < n; j++) {
                    invMatrix[i][j] -= invMatrix[k][j] * buf;
                    unitMatrix[i][j] -= unitMatrix[k][j] * buf;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                invMatrix[i][j] = unitMatrix[i][j];
            }
        }

        return invMatrix;
    }

    public static double[][] MatrixMultiply(double[][] matrixA, double [][] matrixB) throws IllegalArgumentException {
        // умножение матриц
        if(matrixA[0].length == matrixB.length){
            double[][] resMatrix = new double[matrixA.length][matrixB[0].length];
            for(int i = 0; i < matrixA.length; i++) {
                for (int j = 0; j < matrixB[0].length; j++) {
                    for (int k = 0; k < matrixA[0].length; k++) {
                        resMatrix[i][j] += matrixA[i][k] * matrixB[k][j];
                    }
                }
            }
            return resMatrix;
        }
        else {
            throw new IllegalArgumentException("Невозможно получить произведение данных матриц!");
        }
    }

    public static double[] MatrixVectorMultiply(double[][] matrixA, double [] vectorB) throws IllegalArgumentException {
        // умножение матрицы на вектор
        if(matrixA[0].length == vectorB.length){
            double[] resMatrix = new double[matrixA.length];
            for(int i = 0; i < matrixA.length; i++) {
                    for (int k = 0; k < matrixA[0].length; k++) {
                        resMatrix[i] += matrixA[i][k] * vectorB[k];
                    }
            }
            return resMatrix;
        }
        else {
            throw new IllegalArgumentException("Невозможно получить произведение данных матриц!");
        }
    }

    public static void ToSymmetric(double[][] matrix, double[] vector){
        // приведение СЛАУ к симметричному виду
        double[][] transposeMatrix = MatrixTranspose(matrix);
        System.arraycopy(MatrixMultiply(transposeMatrix, matrix), 0, matrix, 0, matrix.length);
        System.arraycopy(MatrixVectorMultiply(transposeMatrix, vector), 0, vector, 0, vector.length);
    }

    public static double[] GaussMethodSolve(double[][] matrix, double[] vector){
        // решение СЛАУ методом Гаусса

        int n = vector.length;
        double c, s;
        double[] x = new double[n];

        // создание копий массивов:
        double[][] matrixCopy = MatrixCopy(matrix);
        double[] vectorCopy = Arrays.copyOf(vector, n);

        // прямой ход:
        for(int i = 0; i < n - 1; i++){
            for(int k = i + 1; k < n; k++){
                c = matrixCopy[k][i]/matrixCopy[i][i];
                matrixCopy[k][i] = 0;
                for(int j = i + 1; j < n; j++)
                    matrixCopy[k][j] = matrixCopy[k][j] - c * matrixCopy[i][j];
                vectorCopy[k] = vectorCopy[k] - c * vectorCopy[i];
            }
        }

        // обратный ход:
        x[n - 1] = vectorCopy[n - 1]/matrixCopy[n - 1][n - 1];
        for(int i = n - 1; i >= 0; i--){
            s = 0;
            for(int j = i + 1; j < n; j++)
                s += matrixCopy[i][j] * x[j];
            x[i] = (vectorCopy[i] - s)/matrixCopy[i][i];
        }
        return x;
    }

    public static double[] CholetskyMethodSolve(double[][] matrix, double[] vector){
        // решение СЛАУ методом Холецкого

        int n = vector.length;
        double s;

        // создание копий массивов:
        double[][] matrixCopy = MatrixCopy(matrix);
        double[] vectorCopy = Arrays.copyOf(vector, n);

        double[][] l_matrix = new double[n][n];
        double[] y = new double[n];
        double[] x;

        ToSymmetric(matrixCopy, vectorCopy); // приведение к симметричному виду

        for(int k = 0; k < n; k++){
            for(int i = 0; i < k; i++){
                s = 0;
                for(int j = 0; j < i; j++) {
                    s = s + matrixCopy[i][j] * matrixCopy[k][j];
                }
                matrixCopy[k][i] = (matrixCopy[k][i] - s)/matrixCopy[i][i]; // вычисление значений под главной диагональю
            }
            s = 0;
            for(int j = 0; j < k; j++)
                s = s + Math.pow(matrixCopy[k][j],2);
            matrixCopy[k][k] = Math.sqrt(matrixCopy[k][k] - s); // вычисление диагональных значений
        }

        for(int i = 0; i < n; i++){ // копирование треугольной матрицы
            for (int j = 0; j <= i; j++){
                l_matrix[i][j] = matrixCopy[i][j];
            }
        }

        // алгоритм приведения к треугольной матрице завершен

        y[0] = vectorCopy[0]/l_matrix[0][0]; // вычисление y0
        for(int i = 1; i < n; i++){
            s = 0;
            for(int j = i - 1; j >= 0; j--)
                s += l_matrix[i][j] * y[j];
            y[i] = (vectorCopy[i] - s)/l_matrix[i][i]; // последовательное вычисление остальных y
        }

        x = MatrixVectorMultiply(MatrixInverse(MatrixTranspose(l_matrix)), y); // решение уравнение LT*x=y

        return x;
    }

    public static void main(String[] args) {

        double [][] matrixA = {{3.40, 3.26, 2.90}, {2.64, 2.39, 1.96}, {4.64, 4.32, 3.85}};
        double [] vectorB = {13.05, 10.30, 17.89};
        System.out.println("Матрица A:");
        OutputMatrix(matrixA);
        System.out.println("Вектор B:");
        OutputVector(vectorB);

        System.out.println("Решение методом Гаусса:");
        OutputVector(GaussMethodSolve(matrixA, vectorB));

        System.out.println("Матрица A:");
        OutputMatrix(matrixA);
        System.out.println("Вектор B:");
        OutputVector(vectorB);

        System.out.println("Решение методом Холецкого: ");
        OutputVector(CholetskyMethodSolve(matrixA, vectorB));

        System.out.println("Проверка решения методом Гаусса: ");
        OutputVector(MatrixVectorMultiply(matrixA, GaussMethodSolve(matrixA, vectorB)));

        System.out.println("Проверка решения методом Холецкого: ");
        OutputVector(MatrixVectorMultiply(matrixA, CholetskyMethodSolve(matrixA, vectorB)));

        System.out.println("Вектор B:");
        OutputVector(vectorB);
    }
}
