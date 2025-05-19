package cabinet;

import java.util.Scanner;

public class Patient extends Personne {
    private int age;
    private String historique;

    public Patient(String nom, String prenom, String email, int age, String historique) {
        super(nom, prenom, email);
        this.age = age;
        this.historique = historique;
    }

    // Méthode pour enregistrer les informations personnelles
    public void enregistrerInfo(String historique) {
        this.historique = historique;
        System.out.println("Informations mises à jour pour le patient " + getNom());
    }

    // Méthode pour demander un changement (nécessite une validation)
    public void demanderChangement(String nouveauHistorique) {
        System.out.println("Demande de changement d'historique envoyée pour " + getNom());
    }

    // Getters et Setters
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHistorique() {
        return historique;
    }

    public void setHistorique(String historique) {
        this.historique = historique;
    }

    public static Patient creerPatient(Scanner scanner) {
        System.out.println("\n--- Ajout d'un nouveau patient ---");
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

            int age = -1;
            while (age == -1) {
                System.out.print("Age : ");
                try {
                    age = Integer.parseInt(scanner.nextLine().trim());
                    if (age <= 0 || age > 150) {
                        System.out.println("L'âge doit être compris entre 1 et 150 ans.");
                        age = -1;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Veuillez entrer un nombre valide.");
                }
            }

            System.out.print("Historique médical : ");
            String historique = scanner.nextLine().trim();

            return new Patient(nom, prenom, email, age, historique);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur : " + e.getMessage());
            return null;
        }
    }
}