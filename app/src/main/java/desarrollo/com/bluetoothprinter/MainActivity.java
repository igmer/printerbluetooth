package desarrollo.com.bluetoothprinter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.RawPrintable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PrintingCallback {
    Printing printing;
    Button btnPrint, btnPair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Printooth.INSTANCE.init(this);
        if (Printooth.INSTANCE.hasPairedPrinter())
            printing = Printooth.INSTANCE.printer();


        intiView();

    }

    private void intiView() {
        btnPair = findViewById(R.id.emparejar);
        btnPrint = findViewById(R.id.imprimir);

        if (printing != null) {
        printing.setPrintingCallback(this);
        }
        btnPair.setOnClickListener(v -> {
            if (Printooth.INSTANCE.hasPairedPrinter()){
                Printooth.INSTANCE.removeCurrentPrinter();
            }
            else{
                startActivityForResult(new Intent(MainActivity.this, ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
                changePairAndUpair();
            }

        });
        btnPrint.setOnClickListener(v ->{
            if (!Printooth.INSTANCE.hasPairedPrinter()){
                startActivityForResult(new Intent(MainActivity.this,ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
            }else{
                printText();
            }
        });

    }

    private void printText() {
        ArrayList<Printable> printables = new ArrayList<>();
        printables.add(new RawPrintable.Builder(new byte[]{27,100,4}).build());
        printables.add(new TextPrintable.Builder()
        .setText("Producto    cantidad   precio")
        .setNewLinesAfter(1)
        .build());
        printables.add(new TextPrintable.Builder()
        .setText("Producto    cantidad   precio")
        .setNewLinesAfter(1)
        .build());
        printables.add(new TextPrintable.Builder()
        .setText("Producto    cantidad   precio")
        .setNewLinesAfter(1)
        .build());
        printing.print(printables);
    }

    private void changePairAndUpair() {
        if (Printooth.INSTANCE.hasPairedPrinter()){
            btnPair.setText(new StringBuilder("Unpair").append(Printooth.INSTANCE.getPairedPrinter().getName().toString()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void connectingWithPrinter() {
        Toast.makeText(this, "Conectando con el impresor", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void connectionFailed(String s) {
        Toast.makeText(this, "Fallo "+s, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onError(String s) {
        Toast.makeText(this, "Error "+s, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onMessage(String s) {
        Toast.makeText(this, "Mensaje: "+s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void printingOrderSentSuccessfully() {
        Toast.makeText(this, "Se ienvio con exito el texto", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK){
            initPrinting();
        }
    }

    private void initPrinting() {
        if (!Printooth.INSTANCE.hasPairedPrinter()){
            printing= Printooth.INSTANCE.printer();
        }
        if (printing != null){
            printing.setPrintingCallback(this);
        }
    }
}
