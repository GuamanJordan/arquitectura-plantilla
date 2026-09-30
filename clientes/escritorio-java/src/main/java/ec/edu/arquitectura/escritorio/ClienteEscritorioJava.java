package ec.edu.arquitectura.escritorio;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClienteEscritorioJava {
    private final DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock"}, 0);
    private final HttpClient client = HttpClient.newHttpClient();
    private final JTextField serverField;
    private final JLabel status = new JLabel("Listo");
    private final JTextArea rawResponse = new JTextArea();

    public ClienteEscritorioJava(String server) {
        this.serverField = new JTextField(server);
    }

    public static void main(String[] args) {
        String server = args.length > 0 ? args[0] : "http://localhost:5100";
        SwingUtilities.invokeLater(() -> new ClienteEscritorioJava(server).mostrar());
    }

    private void mostrar() {
        JFrame frame = new JFrame("Cliente escritorio Java");
        frame.setLayout(new BorderLayout(12, 12));

        JPanel top = new JPanel(new GridBagLayout());
        top.setBorder(BorderFactory.createEmptyBorder(16, 16, 8, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel serverLabel = new JLabel("Servidor REST");
        serverLabel.setFont(serverLabel.getFont().deriveFont(Font.BOLD, 16f));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        top.add(serverLabel, gbc);

        serverField.setFont(serverField.getFont().deriveFont(16f));
        gbc.gridx = 1;
        gbc.weightx = 1;
        top.add(serverField, gbc);

        JButton refrescar = new JButton("Refrescar productos");
        refrescar.setFont(refrescar.getFont().deriveFont(Font.BOLD, 16f));
        refrescar.addActionListener(event -> cargarProductos());
        gbc.gridx = 2;
        gbc.weightx = 0;
        top.add(refrescar, gbc);

        status.setFont(status.getFont().deriveFont(14f));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        top.add(status, gbc);

        JTable table = new JTable(model);
        table.setFont(table.getFont().deriveFont(16f));
        table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD, 16f));
        table.setRowHeight(30);

        rawResponse.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        rawResponse.setRows(6);
        rawResponse.setEditable(false);
        rawResponse.setBorder(BorderFactory.createTitledBorder("Respuesta cruda"));

        frame.add(top, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(new JScrollPane(rawResponse), BorderLayout.SOUTH);
        frame.setSize(1100, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        cargarProductos();
    }

    private void cargarProductos() {
        try {
            String server = serverField.getText().trim();
            HttpRequest request = HttpRequest.newBuilder(URI.create(server + "/api/productos")).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            rawResponse.setText(response.body());
            model.setRowCount(0);
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                status.setText("Error HTTP " + response.statusCode() + " al consultar " + server + "/api/productos");
                return;
            }
            cargarTabla(response.body());
            status.setText("Productos cargados desde " + server + "/api/productos");
        } catch (Exception ex) {
            status.setText("Error: " + ex.getMessage());
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla(String json) {
        Pattern objectPattern = Pattern.compile("\\{([^}]*)}");
        Matcher objects = objectPattern.matcher(json);
        while (objects.find()) {
            String item = objects.group(1);
            model.addRow(new Object[]{
                    extraer(item, "id"),
                    extraer(item, "nombre"),
                    extraer(item, "precio"),
                    extraer(item, "stock")
            });
        }
    }

    private static String extraer(String item, String field) {
        Pattern stringPattern = Pattern.compile("\"" + field + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher stringMatcher = stringPattern.matcher(item);
        if (stringMatcher.find()) {
            return stringMatcher.group(1);
        }
        Pattern numberPattern = Pattern.compile("\"" + field + "\"\\s*:\\s*([^,]+)");
        Matcher numberMatcher = numberPattern.matcher(item);
        return numberMatcher.find() ? numberMatcher.group(1).trim() : "";
    }
}
