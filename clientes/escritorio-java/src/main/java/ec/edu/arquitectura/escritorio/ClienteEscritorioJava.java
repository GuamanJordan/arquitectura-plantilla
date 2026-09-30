package ec.edu.arquitectura.escritorio;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClienteEscritorioJava {
    private final String server;
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"Respuesta REST"}, 0);
    private final HttpClient client = HttpClient.newHttpClient();

    public ClienteEscritorioJava(String server) {
        this.server = server;
    }

    public static void main(String[] args) {
        String server = args.length > 0 ? args[0] : "http://localhost:8082/jakarta-rest-glassfish";
        SwingUtilities.invokeLater(() -> new ClienteEscritorioJava(server).mostrar());
    }

    private void mostrar() {
        JFrame frame = new JFrame("Cliente escritorio Java");
        JButton refrescar = new JButton("Refrescar productos");
        refrescar.addActionListener(event -> cargarProductos());
        frame.add(refrescar, BorderLayout.NORTH);
        frame.add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
        frame.setSize(720, 420);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        cargarProductos();
    }

    private void cargarProductos() {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(server + "/api/productos")).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            model.setRowCount(0);
            model.addRow(new Object[]{response.body()});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
