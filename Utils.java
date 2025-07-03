package Jalon2;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Utils {
    // Accéder aux éléments du tableau de rendez-vous (valeur attribué immuable)
    private static final int IDX_NOM = 0;
    private static final int IDX_PRENOM = 1;
    private static final int IDX_AGE = 2;
    private static final int IDX_CODE_CONSULT = 3;
    private static final int IDX_TYPE_CONSULT = 4;
    private static final int IDX_DATE_HEURE = 5;
    private static final int IDX_CODE_REF = 6;
    private static final int IDX_PRIX_INIT = 7;
    private static final int IDX_PRISE_CHARGE = 8;
    private static final int IDX_PRIX_CLIENT = 9;

    // Liste des jours fériés (valeur attribué immuable)
    private static final List<String> JOURS_FERIES = Arrays.asList(
        "01-01", "21-04", "01-05", "08-05", "29-05", "08-06", 
        "09-06", "14-07", "15-08", "01-11", "11-11", "25-12",
        // 2026
        "01-01", "06-04", "01-05", "08-05", "14-05", "24-05",
        "25-05", "14-07", "15-08", "01-11", "11-11", "25-12"
    );

    // Fonction pour valider la date
    public static boolean validerDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-yyyy HH:mm");
        sdf.setLenient(false);
        
        try {
            Date date = sdf.parse(dateStr);
            return date.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Fonction pour valider les horaires
    public static boolean validerHoraire(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("DD-MM-yyyy HH:mm");
            Date date = sdf.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            
            // Vérifier weekend
            int jourSemaine = cal.get(Calendar.DAY_OF_WEEK);
            if (jourSemaine == Calendar.SATURDAY || jourSemaine == Calendar.SUNDAY) {
                return false;
            }
            
            // Vérifier jours fériés
            SimpleDateFormat fmt = new SimpleDateFormat("DD-MM");
            if (JOURS_FERIES.contains(fmt.format(date))) {
                return false;
            }
            
            // Vérifier horaires
            int heures = cal.get(Calendar.HOUR_OF_DAY);
            int minutes = cal.get(Calendar.MINUTE);
            
            boolean matin = (heures >= 8 && heures < 12) || (heures == 12 && minutes == 0);
            boolean apresMidi = (heures >= 14 && heures < 17) || (heures == 17 && minutes == 0);
            
            return matin || apresMidi;
        } catch (Exception e) {
            return false;
        }
    }

    // Fonction pour vérifier conflits
    public static boolean verifierConflit(List<String[]> rendezVousListe, String dateHeure) {
        for (String[] rdv : rendezVousListe) {
            if (rdv[IDX_DATE_HEURE].equals(dateHeure)) {
                return true;
            }
        }
        return false;
    }

    // Fonction pour générer le code
    public static String genererCodeReference(String nom, String prenom, String dateHeure, String codeConsult) {
        String initiales = (prenom.substring(0, 1) + nom.substring(0, 1)).toUpperCase();
        String datePart = dateHeure.replace("-", "").replace(" ", "").replace(":", "");
        return initiales + datePart + codeConsult;
    }

    // Fonction pour calculer les prix
    public static double[] calculerPrix(int age, String codeConsult) {
        double prixInitial = 0;
        
        switch (codeConsult) {
            case "BS": prixInitial = 120; break;
            case "CD": prixInitial = 200; break;
            case "VC": prixInitial = 0; break;
            case "CM": prixInitial = 100; break;
            case "GN": prixInitial = 70; break;
            case "SM": prixInitial = 60; break;
        }
        
        double priseEnCharge = (age < 18 || age >= 60) ? prixInitial * 0.8 : prixInitial * 0.6;
        double prixClient = prixInitial - priseEnCharge;
        
        return new double[]{prixInitial, priseEnCharge, prixClient};
    }




    //* */ (1): Fonction pour afficher les rendez-vous !

    public static void afficherRendezVous(List<String[]> rendezVousListe) {
        if (rendezVousListe.isEmpty()) {
            System.out.println("Aucun rendez-vous programmé.");
            return;
        }
        
        System.out.println("\nListe des rendez-vous:");
        
        for (String[] rdv : rendezVousListe) {
            System.out.println("Réf: " + rdv[IDX_CODE_REF]);
            System.out.println("Patient: " + rdv[IDX_PRENOM] + " " + rdv[IDX_NOM] + ", " + rdv[IDX_AGE] + " ans");
            System.out.println("Consultation: " + rdv[IDX_TYPE_CONSULT] + " (" + rdv[IDX_CODE_CONSULT] + ")");
            System.out.println("Date/Heure: " + rdv[IDX_DATE_HEURE]);
            System.out.printf("Prix: %.2f EUR (Prise en charge: %.2f EUR)\n", 
                Double.parseDouble(rdv[IDX_PRIX_INIT]), 
                Double.parseDouble(rdv[IDX_PRISE_CHARGE]));
            System.out.printf("À payer: %.2f EUR\n", Double.parseDouble(rdv[IDX_PRIX_CLIENT]));
        }
    }




    //* */ (2) Fonction pour ajouter un rendez-vous !

    public static List<String[]> ajouterRendezVous(List<String[]> rendezVousListe, Scanner scanner) {
        System.out.println("\nAjout d'un nouveau rendez-vous");
        
        // Saisie des informations
        System.out.print("Nom du patient: ");
        String nom = scanner.nextLine();
        
        System.out.print("Prénom du patient: ");
        String prenom = scanner.nextLine();
        
        System.out.print("Âge du patient: ");
        int age = Integer.parseInt(scanner.nextLine());
        
        // Affichage des types de consultation
        System.out.println("\nTypes de consultation disponibles:");
        System.out.println("BS - Bilan de santé");
        System.out.println("CD - Cardiologie");
        System.out.println("VC - Vaccinations");
        System.out.println("CM - Certification médicale");
        System.out.println("GN - Général");
        System.out.println("SM - Suivi médical");
        
        System.out.print("Code de consultation (BS/CD/VC/CM/GN/SM): ");
        String codeConsult = scanner.nextLine().toUpperCase();
        
        if (!Arrays.asList("BS", "CD", "VC", "CM", "GN", "SM").contains(codeConsult)) {
            System.out.println("Code de consultation invalide.");
            return rendezVousListe;
        }
        
        // Saisie de la date et heure
        String dateHeure;
        while (true) {
            System.out.print("Date et heure (DD-MM-YYYY HH:MM): ");
            dateHeure = scanner.nextLine();
            
            if (!validerDate(dateHeure)) {
                System.out.println("Date invalide ou antérieure.");
                continue;
            }
            
            if (!validerHoraire(dateHeure)) {
                System.out.println("Le cabinet est fermé.");
                continue;
            }
            
            if (verifierConflit(rendezVousListe, dateHeure)) {
                System.out.println("Un rendez-vous existe déjà.");
                continue;
            }
            
            break;
        }
        
        // Génération du code
        String codeRef = genererCodeReference(nom, prenom, dateHeure, codeConsult);
        
        // Calcul des prix
        double[] prix = calculerPrix(age, codeConsult);
        
        // Création du tableau représentant le rendez-vous
        String[] nouveauRdv = {
            nom,
            prenom,
            String.valueOf(age),
            codeConsult,
            getTypeConsultation(codeConsult),
            dateHeure,
            codeRef,
            String.valueOf(prix[0]), //convertit les types de valeurs en chaine
            String.valueOf(prix[1]),
            String.valueOf(prix[2])
        };
        
        // Ajout à la liste
        List<String[]> nouvelleListe = new ArrayList<>(rendezVousListe);
        nouvelleListe.add(nouveauRdv);
        System.out.println("Rendez-vous ajouté avec succès. Référence: " + codeRef);
        
        return nouvelleListe;
    }



     // Fonction utilitaire pour obtenir le type de consultation écrit en entier et non BS pour bilan de santé par ex
     //Cette fonction est déclarée private car elle n'est utilisée qu'en interne par la classe Utils
    private static String getTypeConsultation(String code) {
        switch (code) {
            case "BS": return "Bilan de santé";
            case "CD": return "Cardiologie";
            case "VC": return "Vaccinations";
            case "CM": return "Certification médicale";
            case "GN": return "Général";
            case "SM": return "Suivi médical";
            default: return "Inconnu";
        }
    }



    //* */ (3) Fonction pour supprimer un rendez-vous !

    public static List<String[]> supprimerRendezVous(List<String[]> rendezVousListe, Scanner scanner) {
        if (rendezVousListe.isEmpty()) {
            System.out.println("Aucun rendez-vous à supprimer.");
            return rendezVousListe;
        }
        
        System.out.print("Entrez le code de référence (ou une partie): ");
        String codeRef = scanner.nextLine().toUpperCase();
        
        List<String[]> resultats = new ArrayList<>();
        for (String[] rdv : rendezVousListe) {
            if (rdv[IDX_CODE_REF].contains(codeRef)) {
                resultats.add(rdv);
            }
        }
        
        if (resultats.isEmpty()) {
            System.out.println("Aucun rendez-vous trouvé.");
            return rendezVousListe;
        }
        
        if (resultats.size() > 1) {
            System.out.println("Plusieurs rendez-vous trouvés:");
            afficherRendezVous(resultats);
            return rendezVousListe;
        }
        
        String[] rdvASupprimer = resultats.get(0);
        System.out.print("Supprimer le rendez-vous " + rdvASupprimer[IDX_CODE_REF] + "? (O/N): ");
        String confirmation = scanner.nextLine().toUpperCase();
        
        if (confirmation.equals("O")) {
            List<String[]> nouvelleListe = new ArrayList<>(rendezVousListe);
            nouvelleListe.remove(rdvASupprimer);
            System.out.println("Rendez-vous supprimé avec succès.");
            return nouvelleListe;
        }
        
        System.out.println("Suppression annulée.");
        return rendezVousListe;
    }



    //* */ (4) Fonction pour décaler un rendez-vous !

    public static List<String[]> decalerRendezVous(List<String[]> rendezVousListe, Scanner scanner) {
        if (rendezVousListe.isEmpty()) {
            System.out.println("Aucun rendez-vous à décaler.");
            return rendezVousListe;
        }
        
        System.out.print("Entrez le code de référence (ou une partie): ");
        String codeRef = scanner.nextLine().toUpperCase();
        
        List<String[]> resultats = new ArrayList<>();
        for (String[] rdv : rendezVousListe) {
            if (rdv[IDX_CODE_REF].contains(codeRef)) {
                resultats.add(rdv);
            }
        }
        
        if (resultats.isEmpty()) {
            System.out.println("Aucun rendez-vous trouvé.");
            return rendezVousListe;
        }
        
        if (resultats.size() > 1) {
            System.out.println("Plusieurs rendez-vous trouvés:");
            afficherRendezVous(resultats);
            return rendezVousListe;
        }
        
        String[] rdvADecaler = resultats.get(0);
        System.out.println("Rendez-vous actuel: " + rdvADecaler[IDX_DATE_HEURE]);
        
        // Saisie nouvelle date
        String nouvelleDate;
        while (true) {
            System.out.print("Nouvelle date et heure (DD-MM-YYYY HH:MM): ");
            nouvelleDate = scanner.nextLine();
            
            if (!validerDate(nouvelleDate)) {
                System.out.println("Date invalide ou antérieure.");
                continue;
            }
            
            if (!validerHoraire(nouvelleDate)) {
                System.out.println("Le cabinet est fermé.");
                continue;
            }
            
            if (verifierConflit(rendezVousListe, nouvelleDate)) {
                System.out.println("Un rendez-vous existe déjà.");
                continue;
            }
            
            break;
        }
        
        // Génération nouveau code
        String nouveauCode = genererCodeReference(
            rdvADecaler[IDX_NOM],
            rdvADecaler[IDX_PRENOM],
            nouvelleDate,
            rdvADecaler[IDX_CODE_CONSULT]
        );
        
        // Création nouveau rendez-vous
        String[] rdvMisAJour = Arrays.copyOf(rdvADecaler, rdvADecaler.length);
        rdvMisAJour[IDX_DATE_HEURE] = nouvelleDate;
        rdvMisAJour[IDX_CODE_REF] = nouveauCode;
        
        // Mise à jour de la liste
        List<String[]> nouvelleListe = new ArrayList<>();
        for (String[] rdv : rendezVousListe) {
            if (rdv[IDX_CODE_REF].equals(rdvADecaler[IDX_CODE_REF])) {
                nouvelleListe.add(rdvMisAJour);
            } else {
                nouvelleListe.add(rdv);
            }
        }
        
        System.out.println("Rendez-vous décalé ! Nouvelle référence: " + nouveauCode);
        return nouvelleListe;
    }



    //* */ (5) Fonction pour rechercher des rendez-vous !

    public static void rechercherRendezVous(List<String[]> rendezVousListe, Scanner scanner) {
        if (rendezVousListe.isEmpty()) {
            System.out.println("Aucun rendez-vous à rechercher.");
            return;
        }
        
        System.out.print("Entrez un critère de recherche: ");
        String critere = scanner.nextLine().toUpperCase();
        
        List<String[]> resultats = new ArrayList<>();
        for (String[] rdv : rendezVousListe) {
            if (rdv[IDX_CODE_REF].contains(critere) ||
                rdv[IDX_NOM].toUpperCase().contains(critere) ||
                rdv[IDX_PRENOM].toUpperCase().contains(critere) ||
                rdv[IDX_CODE_CONSULT].contains(critere)) {
                resultats.add(rdv);
            }
        }
        
        if (resultats.isEmpty()) {
            System.out.println("Aucun rendez-vous trouvé.");
            return;
        }
        
        System.out.println("\n" + resultats.size() + " rendez-vous trouvés:");
        afficherRendezVous(resultats);
    }

   
}