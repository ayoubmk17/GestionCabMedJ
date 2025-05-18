package cabinet;

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

    public String toString() {
        return String.format("Paiement #%d - %.2f € le %s | Mode: %s | Patient: %s | RDV #%d",
                id, montant, datePaiement, modePaiement,
                patient.getNom(), rdv.getId());
    }
}