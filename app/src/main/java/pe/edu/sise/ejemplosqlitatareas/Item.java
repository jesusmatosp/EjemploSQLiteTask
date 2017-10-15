package pe.edu.sise.ejemplosqlitatareas;

import android.database.SQLException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Item extends AppCompatActivity {

    TextView item = null;
    TextView place = null;
    TextView description = null;
    TextView importance = null;

    Integer rowId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Button saveBtn = (Button) findViewById(R.id.add);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                saveData();
                finish();
            }
        });
        // obtener referencias
        item = (TextView) findViewById(R.id.item);
        place = (TextView) findViewById(R.id.plaece);
        description = (TextView) findViewById(R.id.description);
        importance = (TextView) findViewById(R.id.importance);
    }

    protected void saveData() {
        String itemText = item.getText().toString();
        String placeText = place.getText().toString();
        String descriptionText = description.getText().toString();
        String importanceText = importance.getText().toString();

        try{
            MainActivity.mDbHelper.open();
            MainActivity.mDbHelper.insertItem(itemText, placeText, descriptionText,
                    Integer.parseInt(importanceText));
            MainActivity.mDbHelper.close();
        }catch (SQLException e){
            e.printStackTrace();
            showMessage(e.getLocalizedMessage());
        }
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG ).show();
    }

}
