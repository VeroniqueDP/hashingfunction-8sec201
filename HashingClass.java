 /**
 * Nom				: Duchesne
 * Prenom			: Simon
 * code permanent	: DUCS
 *
 * Nom				: Du Paul
 * Prenom			: Veronique
 * code permanent	: DUPV05518306
 * 
 * Cours			: Cybersécurité défensive : vulnérabilité et incidents
 * Session			: hiver 2023
 * Travail pratique : 1
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;


public class HashingClass {

    // Fonction permettant de traduire un fichier en une matrice de byte
    // La fonction retourne la matrice de byte
    public static byte[] FileToBytes(File file) throws IOException {
        FileInputStream fl = new FileInputStream(file);

        byte[] arr = new byte[(int) file.length()]; // initialisation d'une matrice de même taille que notre fichier

        System.out.println("Nombre de bytes lus : " + fl.read(arr));
        fl.close(); // Nous pouvons fermer le fichier maintenant qu'il est traduit dans la matrice

        return arr; // Nous retournons la matrice de
    }


    public static void main(String[] args)
            throws IOException {

        //Création d'une variable File https://docs.oracle.com/javase/7/docs/api/java/io/File.html
        File path = new File("tparendre.txt" );

        // Appel de la méthode nous permettant de traduire notre fichier en byte
        byte[] byteArray = FileToBytes(path);

        // Déterminer combien de tableau nous aurons besoin
        int columnNumber = 16;
        int lineNumber = 100;

        float entryByArray = columnNumber * lineNumber;
        int minimumArrayRequired = (int) Math.ceil(byteArray.length / entryByArray);


        // QUESTION 1 : décomposer le fichier en matrices de byte de taille 16x100
        // Nous ferons ici un seul tableau, mais celui-ci sera de 3 dimensions.
        // La 3ième dimension représente simplement le nombre de tableau 2 dimensions que nous avons
        byte[][][] tableauxDeBytes = new byte[minimumArrayRequired][lineNumber][columnNumber];
        int readerNumber = 0;
        for (int i = 0; i < minimumArrayRequired; i++) {
            for (int j = 0; j < lineNumber; j++) {
                for (int k = 0; k < columnNumber; k++) {
                    if (readerNumber < byteArray.length) {
                        tableauxDeBytes[i][j][k] = byteArray[readerNumber];
                        readerNumber++;
                    } else {
                        tableauxDeBytes[i][j][k] = 0;
                    }
                }
            }
        }

        // Afficher tous les tableaux 100x16
        System.out.println();
        System.out.println("QUESTION 1 : décomposer le fichier en matrices de byte de taille 16x100");
        for (int i = 0; i < minimumArrayRequired && i < 10; i++) { // limite de 10 tableaux pour les fichiers trop longs
            System.out.println("Tableau [" + i + "]: ");
            for (int j = 0; j < lineNumber; j++)
                System.out.println(Arrays.toString(tableauxDeBytes[i][j]));
            System.out.println();
        }

        // QUESTION 2 : calculer le xor de chaque colonne dans un tableau hash de byte de taille 16
        // Pour chaque colonnes de chaque tableau, on fait le xor de la ligne 0 avec la ligne j,
        // le résultat précédant étant toujours stocké à la ligne 0 (la ligne 0 representant le tableau hash)
        for (int i = 0; i < minimumArrayRequired; i++) {
            for (int j = 1; j < lineNumber; j++) { // initialise a 1 puisque la ligne 0 serviral toujours de référence et sera utilisé dans tous les calculs
                for (int k = 0; k < columnNumber; k++) {
                    tableauxDeBytes[i][0][k] = (byte) (tableauxDeBytes[i][0][k] ^ tableauxDeBytes[i][j][k]);
                }
            }
        }

        System.out.println("QUESTION 2 : calculer le xor de chaque colonne dans un tableau hash de byte de taille 16");
        System.out.print("Résultats du XOR des colonnes du tableau 0 : ");
        System.out.println(Arrays.toString(tableauxDeBytes[0][0]));

        // QUESTION 3 : Faire les additions des résultats de XOR
        // **** Ici, on ne gère pas les retenues. Donc si on a l'addition de -126 + -57, plutôt que d'avoir un ****
        // **** débordement à -183, on ne va considérer que les 8 derniers bits soit 0100 1001 ce qui donne 73 ****
        for (int i = 1; i < minimumArrayRequired; i++) {// initialise a 1 puisque le tableau 0 servira toujours de référence et sera utilisé dans tous les calculs
            // afficher la ligne 0 de chaque matrice qui contient le résultat du xor de chaque colonne
            System.out.print("Résultats du XOR des colonnes du tableau " + i + " : ");
            System.out.println(Arrays.toString(tableauxDeBytes[i][0]));

            // Comme on se sert seulement de la 1ere ligne de chaque tableau, on itere seulement sur les colonnes
            for (int k = 0; k < columnNumber; k++) {
                tableauxDeBytes[0][0][k] = (byte) (tableauxDeBytes[0][0][k] + tableauxDeBytes[i][0][k]);
            }
        }
        System.out.println();

        // Isolation du hash
        byte[] hash = new byte[columnNumber];
        System.arraycopy(tableauxDeBytes[0][0], 0, hash, 0, columnNumber);


        // Sortie de toutes les informations trouvées lors de l'exécution du programme
        // Normalement, nous n'aurions seulement besoin que du Hash final
        // À des fins pratique pour le travail, vous pouvez y trouver plus d'informations

        System.out.println("Nombre de tableau " + columnNumber + " X " + lineNumber + " :");
        System.out.println(Arrays.toString(new float[]{minimumArrayRequired}));
        System.out.println(System.lineSeparator());

        System.out.println("QUESTION 3 : résultat de la sommation dans hash");
        System.out.println("Hash (somme des résultats du XOR de tous les tableaux):");
        System.out.println(Arrays.toString(hash));
    }
}
