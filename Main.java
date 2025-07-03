package Jalon2;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String[]> rendezVousListe = new ArrayList<>();
        
        System.out.println("Bienvenue au Cabinet Médical Hypocamp");
        
        
        while (true) {
            System.out.println("\nMenu Principal:");
            System.out.println("1. Lister tous les rendez-vous");
            System.out.println("2. Ajouter un nouveau rendez-vous");
            System.out.println("3. Supprimer un rendez-vous");
            System.out.println("4. Décaler un rendez-vous");
            System.out.println("5. Rechercher un rendez-vous");
            System.out.println("6. Quitter");
            
            System.out.print("Votre choix (1-6): ");
            String choix = scanner.nextLine();
            
            switch (choix) {
                case "1":
                    Utils.afficherRendezVous(rendezVousListe);
                    break;
                case "2":
                    rendezVousListe = Utils.ajouterRendezVous(rendezVousListe, scanner);
                    break;
                case "3":
                    rendezVousListe = Utils.supprimerRendezVous(rendezVousListe, scanner);
                    break;
                case "4":
                    rendezVousListe = Utils.decalerRendezVous(rendezVousListe, scanner);
                    break;
                case "5":
                    Utils.rechercherRendezVous(rendezVousListe, scanner);
                    break;
                case "6":
                    System.out.println("Merci d'avoir utilisé notre système. Au revoir!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez sélectionner une option entre 1 et 6.");
            }
        }
    }
}