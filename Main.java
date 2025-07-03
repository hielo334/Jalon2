package Jalon2;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String[]> rendezVousListe = new ArrayList<>();
        
        System.out.println("Bienvenue au Cabinet Médical Hypocamp");
        
    // Menu principal
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
            
        // Gestion des choix utilisateur
        switch (choix) {
            case "1":
                // Afficher tous les rendez-vous
                Methods.afficherRendezVous(rendezVousListe);
                break;
                    
            case "2":
                // Ajouter un nouveau rendez-vous
                rendezVousListe = Methods.ajouterRendezVous(rendezVousListe, scanner);
                break;
                    
            case "3":
                // Supprimer un rendez-vous existant
                rendezVousListe = Methods.supprimerRendezVous(rendezVousListe, scanner);
                break;
                    
            case "4":
                // Décaler un rendez-vous
                rendezVousListe = Methods.decalerRendezVous(rendezVousListe, scanner);
                break;
                    
                case "5":
                // Rechercher des rendez-vous
                Methods.rechercherRendezVous(rendezVousListe, scanner);
                break;
                    
            case "6":
                // Quitter l'application
                System.out.println("Merci d'avoir utilisé notre système. Au revoir!");
                scanner.close();
                return;
                    
            default:
                System.out.println("Choix invalide. Veuillez sélectionner une option entre 1 et 6.");
        }
    }
    }
}