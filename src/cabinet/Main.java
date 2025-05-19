package cabinet;

import java.util.Scanner;

public class Main {
    private static Scanner scanner;
    private static CabinetMedica cabinet = new CabinetMedica();
    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            try {
                afficherMenu();
                int choix = lireChoixMenu();

                switch (choix) {
                    case 1:
                        cabinet.ajouterMed(Medecin.creerMedecin(scanner));
                        break;
                    case 2:
                        cabinet.ajouterPatient(Patient.creerPatient(scanner));
                        break;
                    case 3:
                        Rdv.programmerRdv(scanner, cabinet);
                        break;
                    case 4:
                        Rdv.modifierRdv(scanner, cabinet);
                        break;
                    case 5:
                        cabinet.afficherRdvs();
                        break;
                    case 6:
                        Medecin.consulterPlanning(scanner, cabinet);
                        break;
                    case 7:
                        cabinet.afficherStatistiques();
                        break;
                    case 8:
                        Payment.traiterPaiement(scanner, cabinet);
                        break;
                    case 9:
                        running = false;
                        System.out.println("Au revoir !");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
                System.out.println("Appuyez sur Entrée pour continuer...");
                scanner.nextLine();
            }
        }
    }

    private static void afficherMenu() {
        System.out.println("\n=== Système de Gestion Médicale ===");
        System.out.println("1. Ajouter un médecin");
        System.out.println("2. Ajouter un patient");
        System.out.println("3. Programmer un rendez-vous");
        System.out.println("4. Modifier un rendez-vous");
        System.out.println("5. Afficher tous les rendez-vous");
        System.out.println("6. Consulter le planning d'un médecin");
        System.out.println("7. Afficher les statistiques du cabinet");
        System.out.println("8. Ajouter un paiement");
        System.out.println("9. Quitter");
        System.out.print("Votre choix : ");
    }

    private static int lireChoixMenu() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                int choix = Integer.parseInt(input);
                if (choix >= 1 && choix <= 9) {
                    return choix;
                }
                System.out.println("Veuillez entrer un nombre entre 1 et 9");
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer un nombre valide.");
            }
            System.out.print("Votre choix : ");
        }
    }
}