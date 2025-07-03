package Jalon2;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {
  
    // Fonction pour valider la date
    public static boolean validerDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        sdf.setLenient(false);
        
        try {
            Date date = sdf.parse(dateStr);
            return date.after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

  // Liste des jours fériés jusqu'a 2026
    private static final List<String> JOURS_FERIES = Arrays.asList(
        "01-01", "21-04", "01-05", "08-05", "29-05", "08-06", 
        "09-06", "14-07", "15-08", "01-11", "11-11", "25-12",
        "01-01", "06-04", "01-05", "08-05", "14-05", "24-05",
        "25-05", "14-07", "15-08", "01-11", "11-11", "25-12"
    );

    // Fonction pour valider les horaires
    public static boolean validerHoraire(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            Date date = sdf.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            
            int jourSemaine = cal.get(Calendar.DAY_OF_WEEK);
            if (jourSemaine == Calendar.SATURDAY || jourSemaine == Calendar.SUNDAY) {
                return false;
            }
            
            SimpleDateFormat fmt = new SimpleDateFormat("dd-MM");
            if (JOURS_FERIES.contains(fmt.format(date))) {
                return false;
            }
            
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
            if (rdv[5].equals(dateHeure)) { // IDX_DATE_HEURE = 5
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
}