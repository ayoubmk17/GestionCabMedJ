package cabinet;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Medecin extends Personne {
    private String specialite;
    private List<Patient> listePatient;
    private CabinetMedica cabinet;

    public Medecin(String nom, String prenom, String email, String specialite) {
        super(nom, prenom, email);
        if (specialite == null || specialite.trim().isEmpty()) {
            throw new IllegalArgumentException("La spécialité ne peut pas être vide");
        }
        this.specialite = specialite.trim();
        this.listePatient = new ArrayList<>();
    }

    public void setCabinet(CabinetMedica cabinet) {
        this.cabinet = cabinet;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Medecin medecin = (Medecin) obj;
        if (!super.equals(obj)) return false;
        return getNom().equals(medecin.getNom()) &&
                getPrenom().equals(medecin.getPrenom()) &&
                getEmail().equals(medecin.getEmail());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (specialite != null ? specialite.hashCode() : 0);
        return result;
    }

    public void consulterPlanning() {
        if (cabinet == null) {
            throw new IllegalStateException("Erreur: Cabinet non défini");
        }

        List<Rdv> rdvs = cabinet.getRdvMedecin(this);

        System.out.println("\nPlanning du Dr. " + getNom() + " " + getPrenom());
        System.out.println("Spécialité: " + specialite);
        System.out.println("----------------------------------------");

        if (rdvs.isEmpty()) {
            System.out.println("Aucun rendez-vous programmé");
        } else {
            // Grouper les rendez-vous par date
            String dateActuelle = null;
            for (Rdv rdv : rdvs) {
                if (!rdv.getDate().equals(dateActuelle)) {
                    dateActuelle = rdv.getDate();
                    // Formater la date pour l'affichage
                    LocalDate date = LocalDate.parse(dateActuelle);
                    System.out.printf("\nDate: %s\n",
                            date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
                }
                System.out.printf("- %s : Patient %s %s\n",
                        rdv.getHeure(),
                        rdv.getPatient().getNom(),
                        rdv.getPatient().getPrenom());
            }
        }
        System.out.println("----------------------------------------");
    }

    public void consulterPlanningDate(String date) {
        if (cabinet == null) {
            throw new IllegalStateException("Erreur: Cabinet non défini");
        }

        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez AAAA-MM-JJ");
        }

        List<Rdv> rdvs = cabinet.getRdvMedecin(this);

        System.out.printf("\nPlanning du Dr. %s %s pour le %s\n",
                getNom(), getPrenom(), date);
        System.out.println("----------------------------------------");

        boolean aDesRdv = false;
        for (Rdv rdv : rdvs) {
            if (rdv.getDate().equals(date)) {
                System.out.printf("%s - Patient: %s %s\n",
                        rdv.getHeure(),
                        rdv.getPatient().getNom(),
                        rdv.getPatient().getPrenom());
                aDesRdv = true;
            }
        }

        if (!aDesRdv) {
            System.out.println("Aucun rendez-vous programmé pour cette date");
        }
        System.out.println("----------------------------------------");
    }

    public void verifierHistorique(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Le patient ne peut pas être null");
        }
        System.out.printf("Historique médical de %s %s: %s\n",
                patient.getNom(),
                patient.getPrenom(),
                patient.getHistorique());
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        if (specialite == null || specialite.trim().isEmpty()) {
            throw new IllegalArgumentException("La spécialité ne peut pas être vide");
        }
        this.specialite = specialite.trim();
    }

    public List<Patient> getListePatient() {
        return new ArrayList<>(listePatient);
    }

    public void setListePatient(List<Patient> listePatient) {
        this.listePatient = new ArrayList<>(listePatient);
    }

    public void ajouterPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Le patient ne peut pas être null");
        }
        if (!listePatient.contains(patient)) {
            listePatient.add(patient);
        }
    }

    public boolean estDisponible(String date, String heure) {
        if (cabinet == null) {
            throw new IllegalStateException("Cabinet non défini");
        }

        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez AAAA-MM-JJ");
        }

        for (Rdv rdv : cabinet.getListeRdv()) {
            if (rdv.getMedecin().equals(this) &&
                    rdv.getDate().equals(date) &&
                    rdv.getHeure().equals(heure)) {
                return false;
            }
        }
        return true;
    }

    public static Medecin creerMedecin(Scanner scanner) {
        System.out.println("\n--- Ajout d'un nouveau médecin ---");
        try {
            System.out.print("Nom : ");
            String nom = scanner.nextLine().trim();
            if (nom.isEmpty()) {
                throw new IllegalArgumentException("Le nom ne peut pas être vide");
            }

            System.out.print("Prénom : ");
            String prenom = scanner.nextLine().trim();
            if (prenom.isEmpty()) {
                throw new IllegalArgumentException("Le prénom ne peut pas être vide");
            }

            System.out.print("Email : ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty() || !email.contains("@")) {
                throw new IllegalArgumentException("Email invalide");
            }

            System.out.print("Spécialité : ");
            String specialite = scanner.nextLine().trim();
            if (specialite.isEmpty()) {
                throw new IllegalArgumentException("La spécialité ne peut pas être vide");
            }

            return new Medecin(nom, prenom, email, specialite);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
            return null;
        }
    }

    public static void consulterPlanning(Scanner scanner, CabinetMedica cabinet) {
        System.out.println("\n--- Consultation planning médecin ---");
        List<Medecin> medecins = cabinet.getListeMed();

        if (medecins.isEmpty()) {
            System.out.println("Aucun médecin disponible.");
            return;
        }

        // Afficher la liste des médecins
        System.out.println("\nListe des médecins :");
        for (int i = 0; i < medecins.size(); i++) {
            Medecin med = medecins.get(i);
            System.out.printf("%d. Dr. %s %s (%s)\n",
                    i + 1, med.getNom(), med.getPrenom(), med.getSpecialite());
        }

        // Sélection du médecin
        int choix = -1;
        while (choix == -1) {
            System.out.print("\nChoisir un médecin (numéro) : ");
            try {
                choix = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (choix < 0 || choix >= medecins.size()) {
                    System.out.println("Numéro invalide.");
                    choix = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
        }

        // Afficher le planning
        Medecin medecinChoisi = medecins.get(choix);
        if (medecinChoisi != null) {
            List<Rdv> rdvsMedecin = cabinet.getRdvMedecin(medecinChoisi);
            if (rdvsMedecin.isEmpty()) {
                System.out.println("\nAucun rendez-vous programmé pour ce médecin.");
            } else {
                System.out.println("\nPlanning du Dr. " + medecinChoisi.getNom() + " " + medecinChoisi.getPrenom());
                System.out.println("----------------------------------------");
                for (Rdv rdv : rdvsMedecin) {
                    System.out.printf("Le %s à %s - %s %s\n",
                            rdv.getDate(), rdv.getHeure(),
                            rdv.getPatient().getNom(),
                            rdv.getPatient().getPrenom());
                }
                System.out.println("----------------------------------------");
            }
        }
    }
}