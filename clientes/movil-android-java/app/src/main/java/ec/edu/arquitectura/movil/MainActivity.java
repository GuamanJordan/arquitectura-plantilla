package ec.edu.arquitectura.movil;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {
    private final String server = "http://10.0.2.2:5100";
    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        Button refresh = new Button(this);
        refresh.setText("Listar productos");
        output = new TextView(this);
        output.setText("Cliente movil Android Java");
        refresh.setOnClickListener(view -> cargarProductos());
        root.addView(refresh);
        ScrollView scroll = new ScrollView(this);
        scroll.addView(output);
        root.addView(scroll);
        setContentView(root);
    }

    private void cargarProductos() {
        new Thread(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(server + "/api/productos").openConnection();
                connection.setRequestMethod("GET");
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder body = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        body.append(line).append('\n');
                    }
                    runOnUiThread(() -> output.setText(body.toString()));
                }
            } catch (Exception ex) {
                runOnUiThread(() -> output.setText(ex.getMessage()));
            }
        }).start();
    }
}
