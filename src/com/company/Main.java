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
            System.out.print(String.format("%10.3f", elem));
        }
        System.out.println();
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
        for(int i = 0; i < n - 1; i++){
            for(int k = i + 1; k < n; k++){
                c = matrix[k][i]/matrix[i][i];
                matrix[k][i] = 0;
                for(int j = i + 1; j < n; j++)
                    matrix[k][j] = matrix[k][j] - c * matrix[i][j];
                vector[k] = vector[k] - c * vector[i];
            }
        }
        x[n - 1] = vector[n - 1]/matrix[n - 1][n - 1];
        for(int i = n - 1; i > 0; i--){
            s = 0;
            for(int j = i + 1; j < n; j++)
                s = s + matrix[i][j] * x[j];
            x[i] = (vector[i] - s)/matrix[i][i];
        }
        return x;
    }

    public static double[][] CholetskyMethodSolve(double[][] matrix, double[] vector){ // поменять на double[], изменено для тестирования
        // решение СЛАУ методом Холецкого, НЕ РАБОТАЕТ!!!
        ToSymmetric(matrix, vector); // приведение к симметричному виду, ТОЧНО РАБОТАЕТ!!!
        System.out.println("Матрица A:");
        OutputMatrix(matrix);
        System.out.println("Вектор B:");
        OutputVector(vector);

        int n = vector.length;
        double s;
        for(int k = 0; k < n; k++){
            for(int i = 0; i < k; i++){
                s = 0;
                for(int j = 0; j < i; j++) {
                    s = s + matrix[i][j]*matrix[k][j];
                }
                matrix[k][i] = (matrix[k][i] - s)/matrix[i][i];
            }
            System.out.println();
            s = 0;
            for(int j = 0; j < k; j++)
                s = s + Math.pow(matrix[k][j],2);
            matrix[k][k] = Math.sqrt(matrix[k][k] - s);
        }
        // алгоритм приведения к двум треугольным матрицам завершен
        // на этом моменте нам нужно решить две системы уравнений, каким образом - непонятно
        // по идее, у нас уже есть некий x[3] или x[1]
        return matrix;
    }

    public static void main(String[] args) {
        /*int m, n; // размерность матрицы
        Scanner in = new Scanner(System.in);
        System.out.print("Введите m: ");
        m = in.nextInt();
        System.out.print("Введите n: ");
        n = in.nextInt();
        double [][] matrixA = new double[m][n];
        System.out.println("Заполните матрицу:");
        InputMatrix(matrixA);
        System.out.println("Матрица:");
        OutputMatrix(matrixA);
        System.out.println("Заполните вектор:");
        in.nextLine();
        double [] vectorB = LineToArr(in.nextLine());
        OutputVector(vectorB);*/


        double [][] matrixA = {{3.40, 3.26, 2.90}, {2.64, 2.39, 1.96}, {4.64, 4.32, 3.85}};
        double [] vectorB = {13.05, 10.30, 17.89};
        System.out.println("Матрица A:");
        OutputMatrix(matrixA);
        System.out.println("Вектор B:");
        OutputVector(vectorB);

        /*System.out.println("Решение методом Гаусса:");
        OutputVector(GaussMethodSolve(matrixA, vectorB));*/

        System.out.println("Преобразование матрицы методом Холецкого...");
        System.out.println("Матрица:");
        OutputMatrix(CholetskyMethodSolve(matrixA, vectorB));
    }
}
