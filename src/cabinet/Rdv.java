package cabinet;

import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Rdv implements Comparable<Rdv> {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    
    private int id;
    private String date;
    private String heure;
    private Medecin medecin;
    private Patient patient;

    public Rdv(int id, String date, String heure, Medecin medecin, Patient patient) {
        if (medecin == null) {
            throw new IllegalArgumentException("Le médecin ne peut pas être null");
        }
        if (patient == null) {
            throw new IllegalArgumentException("Le patient ne peut pas être null");
        }

        // Validation du format de la date
        try {
            LocalDate.parse(date, DATE_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez AAAA-MM-JJ");
        }

        // Validation du format de l'heure
        try {
            LocalTime.parse(heure, TIME_FORMATTER);
        } catch (Exception e) {
            throw new IllegalArgumentException("Format d'heure invalide. Utilisez HH:MM");
        }

        this.id = id;
        this.date = date;
        this.heure = heure;
        this.medecin = medecin;
        this.patient = patient;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rdv rdv = (Rdv) obj;
        return id == rdv.id &&
                date.equals(rdv.date) &&
                heure.equals(rdv.heure) &&
                medecin.equals(rdv.medecin) &&
                patient.equals(rdv.patient);
    }

    @Override
    public int compareTo(Rdv other) {
        int dateComparison = this.date.compareTo(other.date);
        if (dateComparison != 0) {
            return dateComparison;
        }
        return this.heure.compareTo(other.heure);
    }

    @Override
    public String toString() {
        return String.format("RDV #%d - %s à %s - Dr. %s avec %s %s",
                id, date, heure, medecin.getNom(), patient.getNom(), patient.getPrenom());
    }

    // Getters et Setters avec validation
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        try {
            LocalDate.parse(date, DATE_FORMATTER);
            this.date = date;
        } catch (Exception e) {
            throw new IllegalArgumentException("Format de date invalide. Utilisez AAAA-MM-JJ");
        }
    }

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
        try {
            LocalTime.parse(heure, TIME_FORMATTER);
            this.heure = heure;
        } catch (Exception e) {
            throw new IllegalArgumentException("Format d'heure invalide. Utilisez HH:MM");
        }
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public void setMedecin(Medecin medecin) {
        if (medecin == null) {
            throw new IllegalArgumentException("Le médecin ne peut pas être null");
        }
        this.medecin = medecin;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Le patient ne peut pas être null");
        }
        this.patient = patient;
    }

    public LocalDate getLocalDate() {
        return LocalDate.parse(date, DATE_FORMATTER);
    }

    public LocalTime getLocalTime() {
        return LocalTime.parse(heure, TIME_FORMATTER);
    }

    public static void programmerRdv(Scanner scanner, CabinetMedica cabinet) {
        System.out.println("\n--- Programmation d'un rendez-vous ---");

        // Vérifier s'il y a des patients et des médecins
        List<Patient> patients = cabinet.getListePatient();
        List<Medecin> medecins = cabinet.getListeMed();

        if (patients.isEmpty()) {
            System.out.println("Aucun patient disponible. Veuillez d'abord ajouter un patient.");
            return;
        }
        if (medecins.isEmpty()) {
            System.out.println("Aucun médecin disponible. Veuillez d'abord ajouter un médecin.");
            return;
        }

        try {
            // Sélection du patient
            System.out.println("\nPatients disponibles :");
            for (int i = 0; i < patients.size(); i++) {
                Patient p = patients.get(i);
                System.out.printf("%d. %s %s\n", i + 1, p.getNom(), p.getPrenom());
            }

            int choixPatient = -1;
            while (choixPatient == -1) {
                System.out.print("Choisir un patient (numéro) : ");
                try {
                    choixPatient = Integer.parseInt(scanner.nextLine().trim()) - 1;
                    if (choixPatient < 0 || choixPatient >= patients.size()) {
                        System.out.println("Numéro invalide.");
                        choixPatient = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez entrer un nombre valide.");
                }
            }

            // Sélection du médecin
            System.out.println("\nMédecins disponibles :");
            for (int i = 0; i < medecins.size(); i++) {
                Medecin m = medecins.get(i);
                System.out.printf("%d. Dr. %s %s (%s)\n",
                        i + 1, m.getNom(), m.getPrenom(), m.getSpecialite());
            }

            int choixMedecin = -1;
            while (choixMedecin == -1) {
                System.out.print("Choisir un médecin (numéro) : ");
                try {
                    choixMedecin = Integer.parseInt(scanner.nextLine().trim()) - 1;
                    if (choixMedecin < 0 || choixMedecin >= medecins.size()) {
                        System.out.println("Numéro invalide.");
                        choixMedecin = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez entrer un nombre valide.");
                }
            }

            // Saisie de la date et l'heure
            String date = null;
            while (date == null) {
                System.out.print("Date (AAAA-MM-JJ) : ");
                try {
                    date = scanner.nextLine().trim();
                    LocalDate.parse(date, DATE_FORMATTER);
                } catch (Exception e) {
                    System.out.println("Format de date invalide. Utilisez AAAA-MM-JJ");
                    date = null;
                }
            }

            String heure = null;
            while (heure == null) {
                System.out.print("Heure (HH:MM) : ");
                try {
                    heure = scanner.nextLine().trim();
                    LocalTime.parse(heure, TIME_FORMATTER);
                } catch (Exception e) {
                    System.out.println("Format d'heure invalide. Utilisez HH:MM");
                    heure = null;
                }
            }

            // Création du rendez-vous
            Patient patient = patients.get(choixPatient);
            Medecin medecin = medecins.get(choixMedecin);
            Secretaire secretaire = new Secretaire("Secrétaire", "Cabinet",
                    "secretariat@cabinet.com", "0102030405", cabinet);

            secretaire.programmerRdv(patient, medecin, date, heure);
            System.out.println("Rendez-vous programmé avec succès !");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    public static void modifierRdv(Scanner scanner, CabinetMedica cabinet) {
        System.out.println("\n--- Modification de rendez-vous ---");
        List<Rdv> rdvs = cabinet.getListeRdv();

        if (rdvs.isEmpty()) {
            System.out.println("Aucun rendez-vous à modifier.");
            return;
        }

        try {
            // Afficher la liste des rendez-vous
            System.out.println("\nRendez-vous disponibles :");
            for (Rdv rdv : rdvs) {
                System.out.printf("RDV #%d - Le %s à %s\n",
                        rdv.getId(), rdv.getDate(), rdv.getHeure());
                System.out.printf("  Dr. %s avec %s %s\n",
                        rdv.getMedecin().getNom(),
                        rdv.getPatient().getNom(),
                        rdv.getPatient().getPrenom());
            }

            // Sélection du rendez-vous
            Rdv rdvToModify = null;
            while (rdvToModify == null) {
                System.out.print("\nEntrez l'ID du rendez-vous à modifier : ");
                try {
                    final int idSaisi = Integer.parseInt(scanner.nextLine().trim());
                    rdvToModify = rdvs.stream()
                            .filter(r -> r.getId() == idSaisi)
                            .findFirst()
                            .orElse(null);

                    if (rdvToModify == null) {
                        System.out.println("ID de rendez-vous invalide. Veuillez réessayer.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez entrer un nombre valide.");
                }
            }

            // Saisie de la nouvelle date et heure
            String nouvelleDate = null;
            while (nouvelleDate == null) {
                System.out.print("Nouvelle date (AAAA-MM-JJ) : ");
                try {
                    nouvelleDate = scanner.nextLine().trim();
                    LocalDate.parse(nouvelleDate, DATE_FORMATTER);
                } catch (Exception e) {
                    System.out.println("Format de date invalide. Utilisez AAAA-MM-JJ");
                    nouvelleDate = null;
                }
            }

            String nouvelleHeure = null;
            while (nouvelleHeure == null) {
                System.out.print("Nouvelle heure (HH:MM) : ");
                try {
                    nouvelleHeure = scanner.nextLine().trim();
                    LocalTime.parse(nouvelleHeure, TIME_FORMATTER);
                } catch (Exception e) {
                    System.out.println("Format d'heure invalide. Utilisez HH:MM");
                    nouvelleHeure = null;
                }
            }

            // Vérifier si le médecin est disponible à cette nouvelle date/heure
            if (!cabinet.estDisponible(rdvToModify.getMedecin(), nouvelleDate, nouvelleHeure)) {
                throw new IllegalStateException("Le médecin n'est pas disponible à ce créneau");
            }

            cabinet.modifierRdv(rdvToModify.getId(), nouvelleDate, nouvelleHeure);
            System.out.println("Rendez-vous modifié avec succès !");

        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}