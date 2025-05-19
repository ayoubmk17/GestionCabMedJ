package cabinet;

import java.util.Scanner;

public class Payment {
    private int id;
    private double montant;
    private String datePaiement;
    private Patient patient;
    private Rdv rdv;
    private String modePaiement;

    public Payment(int id, double montant, String datePaiement, Patient patient, Rdv rdv, String modePaiement) {
        this.id = id;
        this.montant = montant;
        this.datePaiement = datePaiement;
        this.patient = patient;
        this.rdv = rdv;
        this.modePaiement = modePaiement;
    }

    public static void traiterPaiement(Scanner scanner, CabinetMedica cabinet) {
        System.out.print("Nom du patient : ");
        String nomPatient = scanner.nextLine();
        Patient patient = cabinet.getListePatient().stream()
                .filter(p -> p.getNom().equalsIgnoreCase(nomPatient))
                .findFirst()
                .orElse(null);
        if (patient == null) {
            System.out.println("Patient introuvable.");
            return;
        }

        System.out.print("ID du rendez-vous : ");
        int idRdv = scanner.nextInt();
        scanner.nextLine();
        Rdv rdv = cabinet.getListeRdv().stream()
                .filter(r -> r.getId() == idRdv)
                .findFirst()
                .orElse(null);
        if (rdv == null) {
            System.out.println("Rendez-vous introuvable.");
            return;
        }

        System.out.print("Montant (€) : ");
        double montant = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Date paiement (yyyy-MM-dd) : ");
        String datePaiement = scanner.nextLine();

        System.out.print("Mode de paiement (Carte, Espèces...) : ");
        String mode = scanner.nextLine();

        cabinet.ajouterPaiement(montant, datePaiement, patient, rdv, mode);
    }

    @Override
    public String toString() {
        return String.format("Paiement #%d - %s€ le %s par %s %s (RDV #%d) - Mode: %s",
                id, montant, datePaiement, patient.getNom(), patient.getPrenom(), rdv.getId(), modePaiement);
    }
}