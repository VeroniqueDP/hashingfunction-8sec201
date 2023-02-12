package com.hash;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;


public class HashingClass {

    // Fonction permettant de traduire un fichier en une matrice de byte
    // La fonction retourne la matrice de byte
    public static byte[] FileToBytes(File file) throws IOException {
        FileInputStream fl = new FileInputStream(file);
        byte[] arr = new byte[(int)file.length()]; // initialisation d'une matrice de même taille que notre fichier

        fl.read(arr);
        fl.close(); // Nous pouvons fermer le fichier maintenant qu'il est traduit dans la matrice
        return arr; // Nous retournons la matrice de
    }


    public static void main(String[] args)
            throws IOException
    {

        //Création d'une variable File https://docs.oracle.com/javase/7/docs/api/java/io/File.html
        File path = new File(
                "/Users/berlingott/Desktop/Modélisation et dev objet/Projet/hash/tparendre.pdf");

        // Appel de la méthode nous permettant de traduire notre fichier en byte
        byte[] ByteArray = FileToBytes(path);

        // Déterminer combien de tableau nous aurons besoin
        int ColumnNumber = 16;
        int LineNumber = 100;

        float EntryByArray = ColumnNumber*LineNumber;
        int MinimumArrayRequired = ByteArray.length/(LineNumber*ColumnNumber) ;


        //Convertir notre array en plusieurs tables de bytes
        // Nous ferons ici un seul tableau, mais celui-ci sera de 3 dimensions.
        // La 3ième dimension représente simplement le nombre de tableau 2 dimensions que nous avons
        byte[][][] TableauxDeBytes = new byte[MinimumArrayRequired+1][LineNumber][ColumnNumber];

        int ReaderNumber=0;
        for (int i = 0 ; i <= MinimumArrayRequired; i++){
            for (int j = 0; j < LineNumber; j++){
                for(int k = 0; k < ColumnNumber; k++ ){
                    if(ReaderNumber < ByteArray.length) {
                        TableauxDeBytes[i][j][k] = ByteArray[ReaderNumber];
                        ReaderNumber++;
                    }
                    else{
                        TableauxDeBytes[i][j][k] = 0;
                    }
                }
            }
        }

        //Appliquer tous les XOR a chacune des lignes

        for (int i = 0 ; i <= MinimumArrayRequired; i++){
            for( int j = 1; j < LineNumber; j++) { // initialise a 1 puisque la ligne 0 serviral toujours de référence et sera utilisé dans tous les calculs
                for(int k = 0; k < ColumnNumber; k++){
                    TableauxDeBytes[i][0][k] = (byte) (TableauxDeBytes[i][0][k]^TableauxDeBytes[i][j][k]);
                }
            }
        }

        //Fait les additions des résultats de XOR
        for (int i = 0 ; i <= MinimumArrayRequired; i++){
                for(int k = 0; k < ColumnNumber; k++){
                    TableauxDeBytes[0][0][k] = (byte) (TableauxDeBytes[0][0][k]+TableauxDeBytes[i][0][k]);
                }
        }
        // Isolation du hash
        byte[] hash = new byte[ColumnNumber];

        for(int k = 0; k < ColumnNumber; k++) {
            hash[k] = TableauxDeBytes[0][0][k] ;
        }


        // Sortie de toutes les informations trouvées lors de l'exécution du programme
        // Normalement, nous n'aurions seulement besoin que du Hash final
        // À des fins pratique pour le travail, vous pouvez y trouver plus d'informations
        System.out.print("Tous les bytes que représente le fichier:");
        System.out.print(Arrays.toString(ByteArray));
        System.out.print(System.lineSeparator());


        System.out.print("Nombre de tableau " + ColumnNumber + " X " + LineNumber + " :");
        System.out.print(Arrays.toString(new float[]{MinimumArrayRequired}));
        System.out.print(System.lineSeparator());


        System.out.print("Hash:");
        System.out.print(Arrays.toString(hash));
    }
}