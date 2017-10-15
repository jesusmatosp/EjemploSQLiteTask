package pe.edu.sise.ejemplosqlitatareas;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    public static final int NEW_ITEM = 1;
    public static final int EDIT_ITEM = 2;
    public static final int SHOW_ITEM = 3;

    // Elemento Seleccionado
    public static DataBaseHelper mDbHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Abrir Base de Datos:
        mDbHelper = new DataBaseHelper(this);
        try{
            fillData();
        }catch (SQLException e){
            e.printStackTrace();
            showMessage(e.getLocalizedMessage());
        }
    }

    public void showMessage(String message){
        Context context = getApplicationContext();
        CharSequence text = message.toString();
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void fillData(){
        mDbHelper.open();
        Cursor itemCursor = mDbHelper.getItems();
        ListEntry item = null;
        ArrayList<ListEntry> resultList = new ArrayList<ListEntry>();
        // Procesamos el Resultado
        while (itemCursor.moveToNext()){
            int id = itemCursor.getInt(itemCursor.getColumnIndex(DataBaseHelper.SL_ID));
            String task = itemCursor.getString(itemCursor.getColumnIndex(DataBaseHelper.SL_ITEM));
            String place = itemCursor.getString(itemCursor.getColumnIndex(DataBaseHelper.SL_PLACE));
            int importance = itemCursor.getInt(itemCursor.getColumnIndex(DataBaseHelper.SL_IMPORTANCE));
            item = new ListEntry();
            item.id = id;
            item.task = task;
            item.place = place;
            item.importance = importance;
            resultList.add(item);
        }

        itemCursor.close();
        mDbHelper.close();

        // Se genera el adaptador
        TaskAdapter items = new TaskAdapter(this, R.layout.row_list, resultList, getLayoutInflater());
        //ListView lista = (ListView) findViewById(R.id.list);
        //lista.setAdapter(items);
        setListAdapter(items);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_item:
                Intent intent = new Intent(this, Item.class);
                startActivityForResult(intent, NEW_ITEM);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class TaskAdapter extends ArrayAdapter<ListEntry> {

        private LayoutInflater mInfalter;
        private List<ListEntry> mObjects;

        public TaskAdapter(Context context, int resource, List<ListEntry> objects, LayoutInflater mInfalter){
            super(context, resource, objects);
            this.mInfalter = mInfalter;
            this.mObjects = objects;
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            ListEntry listEntry = mObjects.get(position);
            // Obtenciónde la vista de la línea de la tabla
            View row = mInfalter.inflate(R.layout.row_list, null);
            //rellenamos datos
            TextView place = (TextView) row.findViewById(R.id.row_place);
            TextView item = (TextView) row.findViewById(R.id.row_item);
            place.setText(listEntry.place);
            item.setText(listEntry.task);

            ImageView icon = (ImageView) row.findViewById(R.id.row_importance);
            icon.setTag(new Integer(listEntry.id));
            switch (listEntry.importance){
                case 1:
                    icon.setImageResource(R.drawable.ic_green);
                    break;
                case 2:
                    icon.setImageResource(R.drawable.ic_yellow);
                    break;
                case 3:
                    icon.setImageResource(R.drawable.ic_red);
                    break;
            }
            return row;
        }
    }

    private class ListEntry {
        int id;
        String task;
        String place;
        int importance;
    }
}
