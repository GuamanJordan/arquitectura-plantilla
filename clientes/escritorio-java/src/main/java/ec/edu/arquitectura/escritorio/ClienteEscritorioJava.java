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
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
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
    private final JLabel resumen = new JLabel("0 productos");
    private final JTextArea rawResponse = new JTextArea();

    public ClienteEscritorioJava(String server) {
        this.serverField = new JTextField(server);
    }

    public static void main(String[] args) {
        String server = args.length > 0 ? args[0] : "http://localhost:5100";
        SwingUtilities.invokeLater(() -> new ClienteEscritorioJava(server).mostrar());
    }

    private void mostrar() {
        UIManager.put("Button.arc", 8);
        UIManager.put("Component.arc", 8);

        JFrame frame = new JFrame("Cliente escritorio Java");
        frame.getContentPane().setBackground(new Color(245, 247, 251));
        frame.setLayout(new BorderLayout(12, 12));

        JPanel top = new JPanel(new GridBagLayout());
        top.setBackground(new Color(245, 247, 251));
        top.setBorder(BorderFactory.createEmptyBorder(18, 18, 6, 18));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel serverLabel = new JLabel("Servidor REST");
        serverLabel.setFont(serverLabel.getFont().deriveFont(Font.BOLD, 17f));
        serverLabel.setForeground(new Color(23, 32, 51));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        top.add(serverLabel, gbc);

        serverField.setFont(serverField.getFont().deriveFont(16f));
        serverField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(198, 210, 225)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridx = 1;
        gbc.weightx = 1;
        top.add(serverField, gbc);

        JButton refrescar = new JButton("Refrescar productos");
        refrescar.setFont(refrescar.getFont().deriveFont(Font.BOLD, 16f));
        refrescar.setBackground(new Color(15, 118, 110));
        refrescar.setForeground(Color.WHITE);
        refrescar.setFocusPainted(false);
        refrescar.addActionListener(event -> cargarProductos());
        gbc.gridx = 2;
        gbc.weightx = 0;
        top.add(refrescar, gbc);

        status.setFont(status.getFont().deriveFont(14f));
        status.setForeground(new Color(71, 84, 103));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        top.add(status, gbc);

        resumen.setFont(resumen.getFont().deriveFont(Font.BOLD, 14f));
        resumen.setForeground(new Color(15, 118, 110));
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        top.add(resumen, gbc);

        JTable table = new JTable(model);
        table.setFont(table.getFont().deriveFont(16f));
        table.getTableHeader().setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD, 16f));
        table.getTableHeader().setBackground(new Color(248, 250, 252));
        table.getTableHeader().setForeground(new Color(71, 84, 103));
        table.setRowHeight(34);
        table.setGridColor(new Color(226, 232, 240));
        table.setSelectionBackground(new Color(230, 244, 241));
        table.setSelectionForeground(new Color(23, 32, 51));

        rawResponse.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        rawResponse.setRows(6);
        rawResponse.setEditable(false);
        rawResponse.setBackground(new Color(248, 250, 252));
        rawResponse.setBorder(BorderFactory.createTitledBorder("Respuesta cruda"));

        frame.add(top, BorderLayout.NORTH);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 18));
        frame.add(tableScroll, BorderLayout.CENTER);
        JScrollPane rawScroll = new JScrollPane(rawResponse);
        rawScroll.setBorder(BorderFactory.createEmptyBorder(0, 18, 18, 18));
        frame.add(rawScroll, BorderLayout.SOUTH);
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
            resumen.setText(model.getRowCount() + " productos");
        } catch (Exception ex) {
            status.setText("Error: " + ex.getMessage());
            resumen.setText("Sin datos");
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
